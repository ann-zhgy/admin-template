package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.AppInfoEntity;
import life.klstoys.admin.template.rbac.service.AppInfoService;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListByUserRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoSaveOrUpdateRequest;
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

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:32
 */
@RestController
@RequestMapping("app-info")
public class AppInfoController {
    @Setter(onMethod_ = @Autowired)
    private AppInfoService appInfoService;

    @GetMapping("{id}")
    public BaseHttpResponse<AppInfoEntity> getAppInfo(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(appInfoService.getAppInfoById(id));
    }

    @GetMapping
    public BaseHttpResponse<PageResult<AppInfoEntity>> page(AppInfoListRequest request) {
        request.checkAndFillDefaultPageIfNull();
        return BaseHttpResponse.success(appInfoService.list(request));
    }

    @GetMapping("user")
    public BaseHttpResponse<List<AppInfoEntity>> listByUser(AppInfoListByUserRequest request) {
        return BaseHttpResponse.success(appInfoService.listByUser(request));
    }

    @PostMapping
    public BaseHttpResponse<String> save(@Validated @RequestBody AppInfoSaveOrUpdateRequest request) {
        appInfoService.save(request);
        return BaseHttpResponse.success();
    }

    @PutMapping("{id}")
    public BaseHttpResponse<String> update(@PathVariable("id") Long id, @Validated @RequestBody AppInfoSaveOrUpdateRequest request) {
        request.setId(id);
        appInfoService.update(request);
        return BaseHttpResponse.success();
    }

    @DeleteMapping("{id}")
    public BaseHttpResponse<String> deleteById(@PathVariable("id") Long id) {
        appInfoService.deleteById(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/disable")
    public BaseHttpResponse<String> disable(@PathVariable("id") Long id) {
        appInfoService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/enable")
    public BaseHttpResponse<String> enable(@PathVariable("id") Long id) {
        appInfoService.enable(id);
        return BaseHttpResponse.success();
    }
}
