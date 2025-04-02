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
import life.klstoys.admin.template.utils.StreamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> files = getTemplatePathSubFiles(path);
        return buildResourceMapWithFilepathList(templatePath, files);
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
        List<String> files = getTemplatePathSubFiles(path);
        String finalPath = path;
        return StreamUtil.ofNullable(files).filter(this::isBackgroundPicture).map(file -> {
            int index = file.indexOf(finalPath);
            return file.substring(index);
        }).collect(Collectors.toList());
    }

    private List<ResourceMap> buildResourceMapWithFilepathList(String templatePath, List<String> filepathList) {
        if (CollectionUtils.isEmpty(filepathList)) {
            log.warn("template directory empty: {}", templatePath);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        // 去掉 templatePath 本身，然后按照上级路径分组
        Map<String, List<String>> map = filepathList.stream().filter(path -> !Objects.equals(templatePath, path))
                .collect(Collectors.groupingBy(path -> path.substring(0, path.lastIndexOf("/"))));
        return map.values().stream().map(strings -> {
            ResourceMap template = new ResourceMap("default", 2);
            for (String path : strings) {
                if (path.endsWith(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME)) {
                    template.put(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, path));
                } else if (path.endsWith(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME)) {
                    template.put(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, path));
                }
            }
            if (MapUtils.isEmpty(template.getResourceMap()) || template.getResourceMap().size() < 2) {
                return null;
            }
            return template;
        }).filter(Objects::nonNull).toList();
    }

    /**
     * 判断文件是否是校验器的背景图片
     *
     * @param filepath 文件名
     * @return true｜false
     */
    private boolean isBackgroundPicture(String filepath) {
        return filepath.endsWith(".jpg");
    }

    /**
     * 获取文件路径下的文件路径列表
     *
     * @param path path
     * @return 文件路径列表
     */
    private List<String> getTemplatePathSubFiles(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            log.error("template path invalid: {}", path);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
        String resourcePath = resource.getPath();
        if (resourcePath.contains(".jar!")) {
            if (resourcePath.startsWith("file:")) {
                resourcePath = resourcePath.substring(5, resourcePath.indexOf(".jar!") + 4);
            }
            try (JarFile jarFile = new JarFile(resourcePath);) {
                return jarFile.stream()
                        .map(JarEntry::getRealName)
                        .filter(name -> name.startsWith(path) && !Objects.equals(name, path))
                        .toList();
            } catch (IOException e) {
                log.error("template path invalid: {}", path, e);
                throw new BizException(ExceptionEnum.SYSTEM_ERROR);
            }
        }
        try (Stream<Path> pathStream = Files.walk(Path.of(resourcePath))) {
            return pathStream.map(Path::toFile)
                    .map(pathFile -> pathFile.getAbsolutePath() + (pathFile.isDirectory() ? "/" : ""))
                    .filter(name -> name.contains(path)).map(name -> name.substring(name.indexOf(path)))
                    .toList();
        } catch (IOException e) {
            log.error("template path invalid: {}", path, e);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR);
        }
    }
}
