package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.AppInfoEntity;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListByUserRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoSaveOrUpdateRequest;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:04
 */
public interface AppInfoService {
    AppInfoEntity getAppInfoById(Long id);

    PageResult<AppInfoEntity> list(AppInfoListRequest request);

    void save(AppInfoSaveOrUpdateRequest request);

    void update(AppInfoSaveOrUpdateRequest request);

    void deleteById(Long id);

    void disable(Long id);

    void enable(Long id);

    List<AppInfoEntity> listByUser(AppInfoListByUserRequest request);
}
