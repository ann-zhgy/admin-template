package life.klstoys.admin.template.captcha.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/6 15:24
 */
@Data
@ConfigurationProperties(prefix = "captcha.resource.path")
public class CaptchaResourcePathProperties {
    /**
     * 模板路径下必须包含一层文件夹，文件夹下的模板文件必须是拥有透明设置的png图片切必须以 active.png 和 fixed.png 命名<br/>
     * 滑块验证码模板大小为 110*110 格式为png<br/>
     * 旋转验证码模板大小为 200*200 格式为png
     */
    private TemplateResourcePath template = new TemplateResourcePath();
    /**
     * 只会读取背景图片路径下的jpg图片，非图片文件和文件夹将会忽略<br/>
     * 自定义图片资源大小为 600*360 格式为jpg
     */
    private BackgroundResourcePath background = new BackgroundResourcePath();

    @Data
    public static class TemplateResourcePath {
        private String slider = "captcha/template/slider";
        private String rotate = "captcha/template/rotate";
    }

    @Data
    public static class BackgroundResourcePath {
        private String slider = "captcha/background-image";
        private String rotate = "captcha/background-image";
        private String concat = "captcha/background-image";
        private String wordImageClick = "captcha/background-image";
    }
}
