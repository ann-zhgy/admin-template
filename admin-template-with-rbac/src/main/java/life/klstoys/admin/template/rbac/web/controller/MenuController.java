package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.MenuInfoEntity;
import life.klstoys.admin.template.rbac.service.MenuService;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuSaveOrUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:31
 */
@RestController
@RequestMapping("menu")
public class MenuController {
    @Setter(onMethod_ = @Autowired)
    private MenuService menuService;

    @GetMapping("{id}")
    public BaseHttpResponse<MenuInfoEntity> getMenuInfo(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(menuService.getMenuById(id));
    }

    @GetMapping("/appKey-menus")
    public BaseHttpResponse<List<MenuInfoEntity>> getMenuInfoByAppKey(@RequestParam("appKey") String appKey) {
        return BaseHttpResponse.success(menuService.getMenuByAppKey(appKey));
    }

    @GetMapping
    public BaseHttpResponse<PageResult<MenuInfoEntity>> page(@Validated MenuListRequest request) {
        return BaseHttpResponse.success(menuService.list(request));
    }

    @PostMapping
    public BaseHttpResponse<String> save(@Validated(Save.class) @RequestBody MenuSaveOrUpdateRequest request) {
        menuService.save(request);
        return BaseHttpResponse.success();
    }

    @PutMapping("{id}")
    public BaseHttpResponse<String> update(@PathVariable("id") Long id, @Validated(Update.class) @RequestBody MenuSaveOrUpdateRequest request) {
        request.setId(id);
        menuService.update(request);
        return BaseHttpResponse.success();
    }

    @DeleteMapping("{id}")
    public BaseHttpResponse<String> deleteById(@PathVariable("id") Long id) {
        menuService.deleteById(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/disable")
    public BaseHttpResponse<String> disable(@PathVariable("id") Long id) {
        menuService.disable(id);
        return BaseHttpResponse.success();
    }

    @PostMapping("{id}/enable")
    public BaseHttpResponse<String> enable(@PathVariable("id") Long id) {
        menuService.enable(id);
        return BaseHttpResponse.success();
    }

    @GetMapping("{id}/is-leaf")
    public BaseHttpResponse<Boolean> menuIsLeafNode(@PathVariable("id") Long id) {
        return BaseHttpResponse.success(menuService.menuIsLeafNode(id));
    }
}
