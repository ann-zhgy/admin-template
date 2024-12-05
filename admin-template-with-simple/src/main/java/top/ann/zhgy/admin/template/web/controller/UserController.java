package top.ann.zhgy.admin.template.web.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ann.zhgy.admin.template.aop.annotation.Permission;
import top.ann.zhgy.admin.template.common.response.BaseHttpResponse;
import top.ann.zhgy.admin.template.common.response.PageResult;
import top.ann.zhgy.admin.template.entity.UserInfoEntity;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;
import top.ann.zhgy.admin.template.service.UserService;
import top.ann.zhgy.admin.template.web.controller.request.UserListRequest;
import top.ann.zhgy.admin.template.web.controller.request.UserUpdatePermissionRequest;
import top.ann.zhgy.admin.template.web.controller.request.UserUpdateRequest;

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
    @Permission({UserPermissionEnum.SUPER_ADMIN, UserPermissionEnum.ADMIN, UserPermissionEnum.BASIC})
    public BaseHttpResponse<UserInfoEntity> baseInfo() {
        return BaseHttpResponse.success(userService.baseInfo());
    }

    @PutMapping
    @Permission({UserPermissionEnum.SUPER_ADMIN, UserPermissionEnum.ADMIN, UserPermissionEnum.BASIC})
    public BaseHttpResponse<String> update(@Validated @RequestBody UserUpdateRequest request) {
        userService.update(request);
        return BaseHttpResponse.success();
    }

    @PostMapping("logout")
    @Permission({UserPermissionEnum.SUPER_ADMIN, UserPermissionEnum.ADMIN, UserPermissionEnum.BASIC})
    public BaseHttpResponse<String> logout() {
        userService.logout();
        return BaseHttpResponse.success();
    }

    @GetMapping
    @Permission({UserPermissionEnum.SUPER_ADMIN})
    public BaseHttpResponse<PageResult<UserInfoEntity>> list(UserListRequest request) {
        return BaseHttpResponse.success(userService.list(request));
    }

    @GetMapping("{id}")
    @Permission({UserPermissionEnum.SUPER_ADMIN})
    public BaseHttpResponse<UserInfoEntity> query(@PathVariable Long id) {
        return BaseHttpResponse.success(userService.query(id));
    }

    @PostMapping("disable/{id}")
    @Permission({UserPermissionEnum.SUPER_ADMIN})
    public BaseHttpResponse<String> disable(@PathVariable Long id) {
        userService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("enable/{id}")
    @Permission({UserPermissionEnum.SUPER_ADMIN})
    public BaseHttpResponse<String> enable(@PathVariable Long id) {
        userService.enable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("permission/{id}")
    @Permission({UserPermissionEnum.SUPER_ADMIN})
    public BaseHttpResponse<String> permission(@PathVariable Long id, @Validated @RequestBody UserUpdatePermissionRequest request) {
        request.setId(id);
        userService.updatePermission(request);
        return BaseHttpResponse.success();
    }
}
