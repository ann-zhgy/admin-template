package top.ann.zhgy.admin.template.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/5/25 17:39
 */
@UtilityClass
public class ResourcePathUtil {
    private static String appBasePath;

    public static String getAppBasePath() {
        if (StringUtils.isNotBlank(appBasePath)) {
            return appBasePath;
        }
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        String path = "";
        if (Objects.nonNull(resource)) path = resource.getPath();
        File file = new File(path);
        if (!file.exists()) file = new File("");
        appBasePath = file.getAbsolutePath();
        return appBasePath;
    }
}
