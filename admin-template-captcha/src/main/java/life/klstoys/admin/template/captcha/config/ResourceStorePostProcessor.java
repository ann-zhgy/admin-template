package life.klstoys.admin.template.captcha.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.impl.StandardRotateImageCaptchaGenerator;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.common.model.dto.ResourceMap;
import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
import jakarta.annotation.Nonnull;
import life.klstoys.admin.template.captcha.config.properties.CaptchaResourcePathProperties;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/7 14:31
 */
@Slf4j
@RequiredArgsConstructor
public class ResourceStorePostProcessor implements BeanPostProcessor {
    private final CaptchaResourcePathProperties properties;

    @Override
    public Object postProcessBeforeInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        if (bean instanceof ResourceStore resourceStore) {
            // 添加模板
            parseTemplateFromPath(properties.getTemplate().getRotate())
                    .forEach(resourceMap -> resourceStore.addTemplate(CaptchaTypeConstant.ROTATE, resourceMap));
            parseTemplateFromPath(properties.getTemplate().getSlider())
                    .forEach(resourceMap -> resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, resourceMap));

            // 2. 添加自定义背景图片, resource 的参数1为资源类型(默认支持 classpath/file/url ), resource 的参数2为资源路径, resource 的参数3为标签
            Map<String, List<String>> pictureMap = new HashMap<>(4);
            List<String> pictures = listPictureFromPath(properties.getBackground().getRotate());
            pictureMap.put(properties.getBackground().getRotate(), pictures);
            pictures.forEach(path -> resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource(ClassPathResourceProvider.NAME, path, "default")));
            List<String> sliderPictures = pictureMap.getOrDefault(properties.getBackground().getSlider(), listPictureFromPath(properties.getBackground().getSlider()));
            pictureMap.put(properties.getBackground().getSlider(), sliderPictures);
            sliderPictures.forEach(path -> resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource(ClassPathResourceProvider.NAME, path, "default")));
            List<String> concatPictures = pictureMap.getOrDefault(properties.getBackground().getConcat(), listPictureFromPath(properties.getBackground().getConcat()));
            pictureMap.put(properties.getBackground().getConcat(), concatPictures);
            concatPictures.forEach(path -> resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource(ClassPathResourceProvider.NAME, path, "default")));
            pictureMap.getOrDefault(properties.getBackground().getConcat(), listPictureFromPath(properties.getBackground().getWordImageClick()))
                    .forEach(path -> resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource(ClassPathResourceProvider.NAME, path, "default")));
        }
        return bean;
    }

    /**
     * 从指定文件解析文件并转换为 ResourceMap
     *
     * @param templatePath 模板路径
     * @return ResourceMap
     */
    private @Nonnull List<ResourceMap> parseTemplateFromPath(String templatePath) {
        String path = templatePath.startsWith("/") ? templatePath.substring(1) : templatePath;
        File[] files = getTemplatePathSubFiles(path);
        return Arrays.stream(files)
                .map(firstLevalFile -> buildResourceMapWithFile(templatePath, firstLevalFile))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取文件路径下的 jpg 图片
     *
     * @param path 路径
     * @return 图片路径
     */
    private @Nonnull List<String> listPictureFromPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        File[] files = getTemplatePathSubFiles(path);
        String finalPath = path;
        return Arrays.stream(files).filter(this::isPicture).map(file -> {
            int index = file.getAbsolutePath().indexOf(finalPath);
            return file.getAbsolutePath().substring(index);
        }).collect(Collectors.toList());
    }

    private ResourceMap buildResourceMapWithFile(String templatePath, File firstLevalFile) {
        if (!firstLevalFile.isDirectory()) {
            log.warn("template first level file is not a directory: {}", firstLevalFile.getAbsolutePath());
            return null;
        }
        File[] secondLevelFiles = firstLevalFile.listFiles();
        if (secondLevelFiles == null || secondLevelFiles.length < 2) {
            log.warn("template first level file is valid: {}", templatePath);
            return null;
        }
        ResourceMap template = new ResourceMap("default", 2);
        for (File secondLevelFile : secondLevelFiles) {
            if (Objects.equals(secondLevelFile.getName(), StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME)) {
                int index = secondLevelFile.getAbsolutePath().indexOf(templatePath);
                String templateFilePath = secondLevelFile.getAbsolutePath().substring(index);
                template.put(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, templateFilePath));
            }
            if (Objects.equals(secondLevelFile.getName(), StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME)) {
                int index = secondLevelFile.getAbsolutePath().indexOf(templatePath);
                String templateFilePath = secondLevelFile.getAbsolutePath().substring(index);
                template.put(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, templateFilePath));
            }
        }
        if (template.getResourceMap().size() != 2) {
            log.warn("template first level file is valid: {}", templatePath);
            return null;
        }
        return template;
    }

    /**
     * 判断文件是否是图片
     *
     * @param file 文件
     * @return true｜false
     */
    private boolean isPicture(File file) {
        return file.getName().endsWith(".jpg");
    }

    /**
     * 获取文件路径下的文件列表
     *
     * @param path path
     * @return File[]
     */
    private static File[] getTemplatePathSubFiles(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            log.error("template path invalid: {}", path);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        File file = new File(resource.getFile());
        if (!file.exists()) {
            log.error("template file not found: {}", path);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        if (!file.isDirectory()) {
            log.error("template file is not a directory: {}", path);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            log.error("template file is empty: {}", path);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        return files;
    }
}
