package life.klstoys.admin.template.simple.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/2 10:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LoginResponse {
    private String token;
}
