package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.service.RoleService;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleSaveOrUpdateRequest;
import life.klstoys.admin.template.validator.group.Save;
import life.klstoys.admin.template.validator.group.Update;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:31
 */
@RestController
@RequestMapping("role")
public class RoleController {
    @Setter(onMethod_ = @Autowired)
    private RoleService roleService;

    @GetMapping("{id}")
    public BaseHttpResponse<RoleInfoEntity> getRoleInfo(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(roleService.getRoleById(id));
    }

    @GetMapping
    public BaseHttpResponse<PageResult<RoleInfoEntity>> page(@Validated RoleListRequest request) {
        request.checkAndFillDefaultPageIfNull();
        return BaseHttpResponse.success(roleService.list(request));
    }

    @PostMapping
    public BaseHttpResponse<String> save(@Validated(Save.class) @RequestBody RoleSaveOrUpdateRequest request) {
        roleService.save(request);
        return BaseHttpResponse.success();
    }

    @GetMapping("{id}/exist-user")
    public BaseHttpResponse<Boolean> existUser(@PathVariable("id") Long id) {
        roleService.existUser(id);
        return BaseHttpResponse.success(roleService.existUser(id));
    }

    @GetMapping("{id}/users")
    public BaseHttpResponse<PageResult<UserInfoEntity>> queryRoleUsers(@PathVariable("id") Long id, PageRequest request) {
        request.checkAndFillDefaultPageIfNull();
        roleService.existUser(id);
        return BaseHttpResponse.success(roleService.queryRoleUsers(id, request));
    }

    @PutMapping("{id}")
    public BaseHttpResponse<String> update(@PathVariable("id") Long id, @Validated(Update.class) @RequestBody RoleSaveOrUpdateRequest request) {
        request.setId(id);
        roleService.update(request);
        return BaseHttpResponse.success();
    }

    @DeleteMapping("{id}")
    public BaseHttpResponse<String> deleteById(@PathVariable("id") Long id) {
        roleService.deleteById(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/disable")
    public BaseHttpResponse<String> disable(@PathVariable("id") Long id) {
        roleService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/enable")
    public BaseHttpResponse<String> enable(@PathVariable("id") Long id) {
        roleService.enable(id);
        return BaseHttpResponse.success();
    }
}
