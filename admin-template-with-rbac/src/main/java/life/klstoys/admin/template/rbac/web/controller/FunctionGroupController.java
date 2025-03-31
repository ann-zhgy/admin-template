package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.service.FunctionGroupService;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupSaveOrUpdateRequest;
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
 * @since 2024/12/9 15:32
 */
@RestController
@RequestMapping("function/group")
public class FunctionGroupController {
    @Setter(onMethod_ = @Autowired)
    private FunctionGroupService functionGroupService;

    @GetMapping("{id}")
    public BaseHttpResponse<FunctionGroupEntity> getFunctionInfo(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(functionGroupService.getFunctionGroupById(id));
    }

    @GetMapping
    public BaseHttpResponse<PageResult<FunctionGroupEntity>> page(FunctionGroupListRequest request) {
        request.checkAndFillDefaultPageIfNull();
        return BaseHttpResponse.success(functionGroupService.list(request));
    }

    @PostMapping
    public BaseHttpResponse<String> save(@Validated(Save.class) @RequestBody FunctionGroupSaveOrUpdateRequest request) {
        functionGroupService.save(request);
        return BaseHttpResponse.success();
    }

    @PutMapping("{id}")
    public BaseHttpResponse<String> update(@PathVariable("id") Long id, @Validated(Update.class) @RequestBody FunctionGroupSaveOrUpdateRequest request) {
        request.setId(id);
        functionGroupService.update(request);
        return BaseHttpResponse.success();
    }

    @DeleteMapping("{id}")
    public BaseHttpResponse<String> deleteById(@PathVariable("id") Long id) {
        functionGroupService.deleteById(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/disable")
    public BaseHttpResponse<String> disable(@PathVariable("id") Long id) {
        functionGroupService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/enable")
    public BaseHttpResponse<String> enable(@PathVariable("id") Long id) {
        functionGroupService.enable(id);
        return BaseHttpResponse.success();
    }
}
