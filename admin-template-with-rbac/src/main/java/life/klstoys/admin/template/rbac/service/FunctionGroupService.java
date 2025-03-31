package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupSaveOrUpdateRequest;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 12:13
 */
public interface FunctionGroupService {
    /**
     * 获取功能组
     *
     * @param id 组id
     * @return 功能组
     */
    FunctionGroupEntity getFunctionGroupById(Long id);

    /**
     * 查询功能组列表
     *
     * @param request request
     * @return 列表
     */
    PageResult<FunctionGroupEntity> list(FunctionGroupListRequest request);

    /**
     * 保存功能组
     *
     * @param request 功能组
     */
    void save(FunctionGroupSaveOrUpdateRequest request);

    /**
     * 更新功能组
     *
     * @param request 功能组
     */
    void update(FunctionGroupSaveOrUpdateRequest request);

    /**
     * 删除功能组
     *
     * @param id id
     */
    void deleteById(Long id);

    /**
     * 停用
     *
     * @param id id
     */
    void disable(Long id);

    /**
     * 启用
     *
     * @param id id
     */
    void enable(Long id);
}
