package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.FunctionInfoEntity;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionSaveOrUpdateRequest;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:12
 */
public interface FunctionService {
    FunctionInfoEntity getFunctionById(Long id);

    PageResult<FunctionInfoEntity> list(FunctionListRequest request);

    void save(FunctionSaveOrUpdateRequest request);

    void update(FunctionSaveOrUpdateRequest request);

    void deleteById(Long id);

    void disable(Long id);

    void enable(Long id);
}
