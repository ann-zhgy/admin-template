package top.ann.zhgy.admin.template.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.constant.CommonConstant;
import cloud.tianai.captcha.generator.impl.StandardRotateImageCaptchaGenerator;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.common.model.dto.ResourceMap;
import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/28 15:42
 */
@Component
@RequiredArgsConstructor
public class TianAiCaptchaResourceConfiguration {
    private final ResourceStore resourceStore;

    @PostConstruct
    public void init() {

        // 滑块验证码 模板 (系统内置) ,这里添加的模板等同于  captcha.init-default-resource=true , 如果配置中设置了加载默认模板，这里模板可不用配置
        ResourceMap template1 = new ResourceMap("default", 4);
        template1.put(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/slider_1/active.png")));
        template1.put(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/slider_1/fixed.png")));
        ResourceMap template2 = new ResourceMap("default", 4);
        template2.put(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/slider_2/active.png")));
        template2.put(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/slider_2/fixed.png")));
        // 旋转验证码 模板 (系统内置)
        ResourceMap template3 = new ResourceMap("default", 4);
        template3.put(StandardRotateImageCaptchaGenerator.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/rotate_1/active.png")));
        template3.put(StandardRotateImageCaptchaGenerator.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, CommonConstant.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/rotate_1/fixed.png")));

        // 1. 添加一些模板
        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template1);
        resourceStore.addTemplate(CaptchaTypeConstant.SLIDER, template2);
        resourceStore.addTemplate(CaptchaTypeConstant.ROTATE, template3);

        // 2. 添加自定义背景图片, resource 的参数1为资源类型(默认支持 classpath/file/url ), resource 的参数2为资源路径, resource 的参数3为标签
        String defaultBackgroundImage = "META-INF/cut-image/resource/1.jpg";
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource(ClassPathResourceProvider.NAME, defaultBackgroundImage, "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource(ClassPathResourceProvider.NAME, defaultBackgroundImage, "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource(ClassPathResourceProvider.NAME, defaultBackgroundImage, "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource(ClassPathResourceProvider.NAME, defaultBackgroundImage, "default"));
    }
}
