package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.exception.ExceptionEnum;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserBaseInfoQueryRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserBindAppInfoRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserBindRoleRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdateRequest;
import life.klstoys.admin.template.rbac.web.controller.response.UserRoleResponse;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:38
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Setter(onMethod_ = @Autowired)
    private UserService userService;

    @GetMapping("base-info")
    public BaseHttpResponse<UserInfoEntity> baseInfo(UserBaseInfoQueryRequest request) {
        if (request.getQueryMenus() && StringUtils.isBlank(request.getAppKey())) {
            throw new BizException(ExceptionEnum.PARAM_INVALID, "查询基础信息的菜单时，appKey不能为空");
        }
        return BaseHttpResponse.success(userService.baseInfo(request.getQueryMenus(), request.getAppKey()));
    }

    @PutMapping
    public BaseHttpResponse<String> update(@Validated @RequestBody UserUpdateRequest request) {
        userService.update(request);
        return BaseHttpResponse.success();
    }

    @PostMapping("update-password")
    public BaseHttpResponse<String> updatePassword(@Validated @RequestBody UserUpdatePasswordRequest request) {
        userService.updatePassword(request);
        return BaseHttpResponse.success();
    }

    @PostMapping("logout")
    public BaseHttpResponse<String> logout() {
        userService.logout();
        return BaseHttpResponse.success();
    }

    @GetMapping
    public BaseHttpResponse<PageResult<UserInfoEntity>> list(UserListRequest request) {
        return BaseHttpResponse.success(userService.list(request));
    }

    @GetMapping("{id}")
    public BaseHttpResponse<UserInfoEntity> query(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(userService.query(id));
    }

    @GetMapping("{id}/roles")
    public BaseHttpResponse<List<UserRoleResponse>> queryUserRoles(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(userService.queryUserRoles(id));
    }

    @GetMapping("{id}/{appKey}/roles")
    public BaseHttpResponse<PageResult<RoleInfoEntity>> queryUserAppRoles(@PathVariable("id") Long id, @PathVariable("appKey") String appKey, PageRequest request) {
        request.checkAndFillDefaultPageIfNull();
        return BaseHttpResponse.success(userService.queryUserAppRoles(id, appKey, request));
    }

    @PostMapping("{id}/bind-role")
    public BaseHttpResponse<String> bindRole(@PathVariable("id") Long id, @Validated @RequestBody UserBindRoleRequest request) {
        userService.bindRole(id, request.getRoleIds());
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/unbind-role")
    public BaseHttpResponse<String> unbindRole(@PathVariable("id") Long id, @Validated @RequestBody UserBindRoleRequest request) {
        userService.unbindRole(id, request.getRoleIds());
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/bind-app")
    public BaseHttpResponse<String> bindApp(@PathVariable("id") Long id, @Validated @RequestBody UserBindAppInfoRequest request) {
        userService.bindApp(id, request.getAppKeys());
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/unbind-app")
    public BaseHttpResponse<String> unbindApp(@PathVariable("id") Long id, @Validated @RequestBody UserBindAppInfoRequest request) {
        userService.unbindApp(id, request.getAppKeys());
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/disable")
    public BaseHttpResponse<String> disable(@PathVariable("id") Long id) {
        userService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/enable")
    public BaseHttpResponse<String> enable(@PathVariable("id") Long id) {
        userService.enable(id);
        return BaseHttpResponse.success();
    }
}
