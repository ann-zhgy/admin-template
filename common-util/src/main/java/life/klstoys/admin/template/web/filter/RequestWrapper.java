package life.klstoys.admin.template.web.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import life.klstoys.admin.template.constant.CommonConstant;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/5/20 15:21
 */
@Getter
public class RequestWrapper extends HttpServletRequestWrapper {
    private final String requestBody;
    private final String requestId;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.requestBody = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        String requestId = request.getHeader(CommonConstant.REQUEST_ID_HEADER_NAME);
        if (StringUtils.isBlank(requestId)) {
            this.requestId = UUID.randomUUID().toString();
        } else {
            this.requestId = requestId;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
