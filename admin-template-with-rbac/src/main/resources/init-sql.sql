CREATE TABLE `user`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `username`    varchar(32)     NOT NULL COMMENT '用户名',
    `nickname`    varchar(32)     NOT NULL COMMENT '昵称',
    `password`    varchar(256)    NOT NULL COMMENT '密码',
    `phone`       varchar(32)              DEFAULT NULL COMMENT '手机号',
    `email`       varchar(32)              DEFAULT NULL COMMENT '邮箱',
    `status`      tinyint         NOT NULL COMMENT '状态。1-使用中，2-已注销',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_username` (`username`),
    UNIQUE KEY `uni_email` (`email`),
    UNIQUE KEY `uni_phone` (`phone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='账号信息';

INSERT INTO user (username, nickname, password, phone, email, status)
-- password：admin
VALUES ('admin', '管理员', '$2a$10$LmhRKSZDzOVm6bDm097/Ru9MiKmf7CgP/vDZ4U5vvjdATNfhTXPIC', null, null, 1),
-- password：test
       ('test', '测试1', '$2a$10$chBPolPjw3e/1dhTGW3t..Xra4z1YRL3Kwa6edlwai093iBBSpH5a', null, null, 1),
-- password：test2
       ('test2', '测试2', '$2a$10$nt6OUDD/bkXYvPeHVQ2CP.iGxSnQFpslxkO7VzlbJKQEtmyrv7tHu', null, null, 2);

CREATE TABLE `role`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `no`          varchar(32)     NOT NULL COMMENT '角色编码',
    `name`        varchar(32)     NOT NULL COMMENT '角色名称',
    `name_zh`     varchar(32)     NOT NULL COMMENT '角色中文名称',
    `description` varchar(256)             DEFAULT NULL COMMENT '角色描述',
    `app_key`     varchar(256)    NOT NULL COMMENT '系统标识',
    `status`      tinyint         NOT NULL COMMENT '状态。1-使用中，2-已停用',
    `creator`     varchar(64)     NOT NULL COMMENT '创建人',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)     NOT NULL COMMENT '更新人',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_no` (`no`),
    UNIQUE KEY `uni_appKey_name` (`app_key`, `name`),
    INDEX `idx_appKey` (`app_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色信息';

INSERT INTO role (no, name, name_zh, description, app_key, status, creator, updater)
VALUES ('2024121816420053594d7d550', 'basic', '基本角色', null, 'admin-template', 1, 'super-admin',
        'super-admin'),
       ('2024121816420053758022525', 'admin', '管理员', null, 'admin-template', 1, 'super-admin',
        'super-admin');

CREATE TABLE `frontend_page`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT,
    `no`            varchar(32)     NOT NULL COMMENT '菜单编码',
    `title`         varchar(32)     NOT NULL COMMENT '菜单标题',
    `component_key` varchar(32)     NOT NULL COMMENT '前端组件key',
    `parent_no`     varchar(32)     NULL COMMENT '父级菜单编码',
    `app_key`       varchar(256)    NOT NULL COMMENT '系统标识',
    `status`        tinyint         NOT NULL COMMENT '状态。1-使用中，2-已停用',
    `creator`       varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`       varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_no` (`no`),
    INDEX `idx_title` (`title`),
    INDEX `idx_appKey_title` (`app_key`, `title`),
    INDEX `idx_componentKey` (`component_key`),
    INDEX `idx_appKey_componentKey` (`app_key`, `component_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='前端菜单及组件信息';

INSERT INTO frontend_page (no, title, component_key, parent_no, app_key, status, creator, updater)
VALUES ('20250120104236416e5041bcf', '权限管理', 'Author', null, 'admin-template', 1, 'admin', 'admin'),
       ('2025012010423641885787cb9', '账号管理', 'RbacUserList', '20250120104236416e5041bcf', 'admin-template', 1,
        'admin', 'admin'),
       ('202501201042364184400ee2d', '角色管理', 'RoleList', '20250120104236416e5041bcf', 'admin-template', 1, 'admin',
        'admin'),
       ('2025012010423641889e4681e', '菜单管理', 'MenuList', '20250120104236416e5041bcf', 'admin-template', 1, 'admin',
        'admin'),
       ('2025012010423641864b1550f', '应用管理', 'AppInfoList', '20250120104236416e5041bcf', 'admin-template', 1,
        'admin', 'admin'),
       ('20250120104236418ec6318b6', '功能管理', 'FunctionList', '20250120104236416e5041bcf', 'admin-template', 1,
        'admin', 'admin'),
       ('20250123161554093b3be9ba9', '功能组管理', 'FunctionGroupList', '20250120104236416e5041bcf', 'admin-template',
        1, 'admin', 'admin');

CREATE TABLE `backend_function`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT,
    `no`             varchar(32)     NOT NULL COMMENT '菜单编码',
    `title`          varchar(32)     NOT NULL COMMENT '菜单标题',
    `request_method` tinyint         NOT NULL COMMENT '请求方法。1-GET，2-POST，3-PUT，4-DELETE',
    `request_url`    varchar(256)    NOT NULL COMMENT '请求url',
    `app_key`        varchar(256)    NOT NULL COMMENT '系统标识',
    `status`         tinyint         NOT NULL COMMENT '状态。1-使用中，2-已停用',
    `creator`        varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`    datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`        varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`    datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_no` (`no`),
    INDEX `idx_title` (`title`),
    INDEX `idx_appKey_title` (`app_key`, `title`),
    INDEX `idx_requestUrl` (`request_url`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='后端功能信息';

INSERT INTO backend_function (no, title, request_method, request_url, app_key, status, creator, updater)
VALUES ('20241218164200537976bafe8', '获取登录账号基本信息', 2, '/user/base-info', 'admin-template', 1, 'admin',
        'admin'),
       ('2024121816420053715ca7f0f', '修改登录账号基本信息', 3, '/user', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200537344dab91', '批量获取账号信息', 1, '/user', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005373662e680', '禁用账号信息', 2, '/user/{id}/disable', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005371009bee3', '启用账号', 2, '/user/{id}/enable', 'admin-template', 1, 'admin', 'admin'),
       ('2024121816420053781cff73f', '用户登出系统', 2, '/user/logout', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200537f71f4b02', '修改密码', 2, '/user/update-password', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005385a83fc52', '获取指定账号信息', 1, '/user/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538505bf81e', '获取指定账号的角色信息', 1, '/user/{id}/roles', 'admin-template', 1, 'admin',
        'admin'),
       ('202412181642005387dfe84cd', '为指定账号绑定角色信息', 2, '/user/{id}/bind-role', 'admin-template', 1, 'admin',
        'admin'),
       ('202412181642005387a3f6594', '为指定账号解绑角色信息', 2, '/user/{id}/unbind-role', 'admin-template', 1,
        'admin', 'admin'),
       ('202501031614575960e6db9b7', '获取指定账号指定应用的角色信息', 1, '/user/{id}/{appKey}/roles', 'admin-template',
        1, 'admin', 'admin'),
       ('20250113102656767549f7280', '为指定账号添加应用授权', 2, '/user/{id}/bind-app', 'admin-template', 1, 'admin',
        'admin'),
       ('20250113102656769b9a55fd0', '为指定账号取消应用授权', 2, '/user/{id}/unbind-app', 'admin-template', 1, 'admin',
        'admin'),
       ('202412181642005380b8d9d41', '新增角色信息', 2, '/role', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538fafdbafb', '修改角色信息', 3, '/role/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005382355d301', '获取角色信息', 1, '/role', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538e54b384e', '获取指定角色信息', 1, '/role/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005386ffb756c', '检查指定角色是否还有绑定的账号', 1, '/role/{id}/exist-user', 'admin-template', 1,
        'admin', 'admin'),
       ('20241218164200538563d4837', '获取指定角色绑定的账号', 1, '/role/{id}/user', 'admin-template', 1, 'admin',
        'admin'),
       ('2024121816420053835b83ff2', '删除指定角色', 4, '/role/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538987af84d', '禁用指定角色', 2, '/role/{id}/disable', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538b560ab69', '启用指定角色', 2, '/role/{id}/enable', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538f3c1b8c2', '为指定角色绑定菜单', 2, '/role/{id}/bind-menus', 'admin-template', 1, 'admin',
        'admin'),
       ('2024121816420053849645884', '为指定角色解绑菜单', 2, '/role/{id}/unbind-menus', 'admin-template', 1, 'admin',
        'admin'),
       ('20241218164200538663a7a35', '添加菜单（前端）', 2, '/menu', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005387a860f31', '修改菜单（前端）', 3, '/menu/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200538cfff4700', '获取菜单（前端）', 1, '/menu', 'admin-template', 1, 'admin', 'admin'),
       ('2024121816420053904a66a83', '获取指定应用的菜单，返回树形结构（前端）', 1, '/menu/appKey-menus', 'admin-template',
        1, 'admin', 'admin'),
       ('2024121816420053997da8141', '获取指定菜单（前端）', 1, '/menu/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200539942b31f3', '删除指定菜单（前端）', 4, '/menu/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200539b1946060', '启用指定菜单（前端）', 2, '/menu/{id}/enable', 'admin-template', 1, 'admin',
        'admin'),
       ('202412181642005394babeb41', '禁用指定菜单（前端）', 2, '/menu/{id}/disable', 'admin-template', 1, 'admin',
        'admin'),
       ('20241218164200539fdef73ea', '解除指定菜单（前端）与功能（后端）的关联', 2, '/menu/{id}/disassociate',
        'admin-template', 1, 'admin', 'admin'),
       ('20241218164200539705b28df', '为指定菜单（前端）与功能（后端）的添加关联', 2, '/menu/{id}/associate',
        'admin-template', 1, 'admin', 'admin'),
       ('202412181642005393d12337c', '添加功能（后端）', 2, '/function', 'admin-template', 1, 'admin', 'admin'),
       ('202412181642005396aa16eef', '修改功能（后端）信息', 3, '/function/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('2024121816420053967cc2df5', '获取功能（后端）信息列表', 1, '/function', 'admin-template', 1, 'admin', 'admin'),
       ('20241218164200539609083b6', '获取指定功能（后端）信息', 1, '/function/{id}', 'admin-template', 1, 'admin',
        'admin'),
       ('20241218164200539d30c5a35', '删除指定功能（后端）信息', 4, '/function/{id}', 'admin-template', 1, 'admin',
        'admin'),
       ('202412181642005392272f27a', '启用指定功能（后端）', 2, '/function/{id}/enable', 'admin-template', 1, 'admin',
        'admin'),
       ('2024121816420053963dc6bb9', '禁用指定功能（后端）', 2, '/function/{id}/disable', 'admin-template', 1, 'admin',
        'admin'),
       ('20241218173106180d26b2b48', '添加应用信息', 1, '/app-info', 'admin-template', 1, 'admin', 'admin'),
       ('202412181731061852356627b', '查询应用信息列表', 2, '/app-info', 'admin-template', 1, 'admin', 'admin'),
       ('202412181731061854d0c6c25', '查询指定应用信息', 1, '/app-info/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('20241218173106185e0d72178', '删除指定应用信息', 4, '/app-info/{id}', 'admin-template', 1, 'admin', 'admin'),
       ('202412181731061858279268d', '设置应用为启用', 2, '/app-info/{id}/enable', 'admin-template', 1, 'admin',
        'admin'),
       ('2024121817310618562604a00', '设置应用为停用', 2, '/app-info/{id}/disable', 'admin-template', 1, 'admin',
        'admin'),
       ('202501121859049130e372521', '获取指定账号的应用信息', 1, '/app-info/user', 'admin-template', 1, 'admin',
        'admin'),
       ('202501200952568253b859192', '获取指定功能组信息', 1, '/function/group/{id}', 'admin-template', 1, 'admin',
        'admin'),
       ('20250120095256828177e8a36', '分页查询功能组信息', 1, '/function/group', 'admin-template', 1, 'admin', 'admin'),
       ('20250120095256828d9bcb3b0', '创建功能组信息', 2, '/function/group', 'admin-template', 1, 'admin', 'admin'),
       ('202501200952568283728d899', '修改功能组信息', 3, '/function/group/{id}', 'admin-template', 1, 'admin',
        'admin'),
       ('20250120095256828b024de67', '删除指定功能组', 4, '/function/group/{id}', 'admin-template', 1, 'admin',
        'admin'),
       ('20250120095256828fb5954a9', '禁用功能组', 2, '/function/group/{id}/disable', 'admin-template', 1, 'admin',
        'admin'),
       ('202501200952568284715aeb3', '启用功能组', 2, '/function/group/{id}/enable', 'admin-template', 1, 'admin',
        'admin');

CREATE TABLE `function_group`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT,
    `no`               varchar(32)     NOT NULL COMMENT '功能组编码',
    `title`            varchar(32)     NOT NULL COMMENT '功能组标题',
    `app_key`          varchar(256)    NOT NULL COMMENT '系统标识',
    `frontend_page_no` varchar(32)     NOT NULL COMMENT 'web组件编号',
    `group_call_type`  tinyint         NOT NULL COMMENT '组调用类型：1-页面初始化就调用，2-通过按钮、链接等调用',
    `status`           tinyint         NOT NULL COMMENT '状态。1-使用中，2-已停用',
    `creator`          varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`          varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_no` (`no`),
    INDEX `idx_title` (`title`),
    UNIQUE KEY `uni_appKey_title` (`app_key`, `title`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='功能组，对应前端某个页面的功能需要使用到的后端接口，比如：新增、编辑等';

INSERT INTO function_group (no, title, app_key, frontend_page_no, group_call_type, status, creator, updater)
VALUES ('20250120101419647ea43395e', '账号列表', 'admin-template', '2025012010423641885787cb9', 1, 1, 'admin', 'admin'),
       ('20250120101419647ffd52280', '账号详情', 'admin-template', '2025012010423641885787cb9', 2, 1, 'admin', 'admin'),
       ('20250120101419647a79bfa09', '修改账号', 'admin-template', '2025012010423641885787cb9', 2, 1, 'admin', 'admin'),
       ('20250120101419647ae6b0ecf', '变更账号状态', 'admin-template', '2025012010423641885787cb9', 2, 1, 'admin',
        'admin'),
       ('202501231735476998283e204', '删除账号', 'admin-template', '2025012010423641885787cb9', 2, 1, 'admin', 'admin'),
       ('202501201014196470c77fcce', '角色列表', 'admin-template', '202501201042364184400ee2d', 1, 1, 'admin', 'admin'),
       ('20250120101419647c252f723', '角色详情', 'admin-template', '202501201042364184400ee2d', 2, 1, 'admin', 'admin'),
       ('20250120101419647c878fe10', '创建角色', 'admin-template', '202501201042364184400ee2d', 2, 1, 'admin', 'admin'),
       ('202501201014196470c5100cd', '修改角色', 'admin-template', '202501201042364184400ee2d', 2, 1, 'admin', 'admin'),
       ('20250120101419648c30e5f33', '变更角色状态', 'admin-template', '202501201042364184400ee2d', 2, 1, 'admin',
        'admin'),
       ('20250123173547703a3197913', '删除角色', 'admin-template', '202501201042364184400ee2d', 2, 1, 'admin', 'admin'),
       ('202501201014196485a6f1355', '菜单列表', 'admin-template', '2025012010423641889e4681e', 1, 1, 'admin', 'admin'),
       ('202501201014196480afc45f9', '菜单详情', 'admin-template', '2025012010423641889e4681e', 2, 1, 'admin', 'admin'),
       ('20250120101419648c7b1c47d', '创建菜单', 'admin-template', '2025012010423641889e4681e', 2, 1, 'admin', 'admin'),
       ('2025012010141964876efa1c4', '修改菜单', 'admin-template', '2025012010423641889e4681e', 2, 1, 'admin', 'admin'),
       ('2025012010141964886be0b2b', '变更菜单状态', 'admin-template', '2025012010423641889e4681e', 2, 1, 'admin',
        'admin'),
       ('202501231735477043a27692e', '删除菜单', 'admin-template', '2025012010423641889e4681e', 2, 1, 'admin', 'admin'),
       ('202501201014196484dfa3543', '功能组列表', 'admin-template', '20250123161554093b3be9ba9', 1, 1, 'admin',
        'admin'),
       ('20250120101419648d9c7f269', '功能组详情', 'admin-template', '20250123161554093b3be9ba9', 2, 1, 'admin',
        'admin'),
       ('20250120101419648b8645dbc', '创建功能组', 'admin-template', '20250123161554093b3be9ba9', 2, 1, 'admin',
        'admin'),
       ('2025012010141964839b7253e', '修改功能组', 'admin-template', '20250123161554093b3be9ba9', 2, 1, 'admin',
        'admin'),
       ('20250120101419648cf95fed1', '变更功能组状态', 'admin-template', '20250123161554093b3be9ba9', 2, 1, 'admin',
        'admin'),
       ('2025012317354770498f071ae', '删除功能组', 'admin-template', '20250123161554093b3be9ba9', 2, 1, 'admin',
        'admin'),
       ('2025012010141964898b4ec06', '功能列表', 'admin-template', '20250120104236418ec6318b6', 1, 1, 'admin', 'admin'),
       ('202501201014196484813c870', '功能详情', 'admin-template', '20250120104236418ec6318b6', 2, 1, 'admin', 'admin'),
       ('2025012010141964804d278d0', '创建功能', 'admin-template', '20250120104236418ec6318b6', 2, 1, 'admin', 'admin'),
       ('202501201014196483f07a776', '修改功能', 'admin-template', '20250120104236418ec6318b6', 2, 1, 'admin', 'admin'),
       ('20250120101419648a2932895', '变更功能状态', 'admin-template', '20250120104236418ec6318b6', 2, 1, 'admin',
        'admin'),
       ('20250123173547704c678b526', '删除功能', 'admin-template', '20250120104236418ec6318b6', 2, 1, 'admin', 'admin'),
       ('20250120104236418889520cb', '应用列表', 'admin-template', '2025012010423641864b1550f', 1, 1, 'admin', 'admin'),
       ('2025012010423641895886cf8', '应用详情', 'admin-template', '2025012010423641864b1550f', 2, 1, 'admin', 'admin'),
       ('20250120104236418ed8bbb37', '创建应用', 'admin-template', '2025012010423641864b1550f', 2, 1, 'admin', 'admin'),
       ('202501201042364189989b42a', '变更应用状态', 'admin-template', '2025012010423641864b1550f', 2, 1, 'admin',
        'admin'),
       ('2025012010423641867c0c6d2', '删除应用', 'admin-template', '2025012010423641864b1550f', 4, 1, 'admin', 'admin');

create table `function_group_map`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT,
    `group_no`            varchar(32)     NOT NULL COMMENT '功能组编号',
    `backend_function_no` varchar(32)     NOT NULL COMMENT '后端功能编号',
    `request_method`      tinyint         NOT NULL COMMENT '请求方法。1-GET，2-POST，3-PUT，4-DELETE',
    `request_url`         varchar(256)    NOT NULL COMMENT '请求url',
    `app_key`             varchar(256)    NOT NULL COMMENT '系统标识',
    `creator`             varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`             varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`         datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique key `uni_appKey_groupNo_backendFunctionNo` (`app_key`, `group_no`, `backend_function_no`),
    INDEX `idx_frontendPageNo_backendFunctionNo` (`group_no`, `backend_function_no`),
    INDEX `idx_backendFunctionNo` (`backend_function_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='菜单映射关系表';

INSERT INTO function_group_map (group_no, backend_function_no, request_method, request_url, app_key, creator, updater)
VALUES ('20250120101419647ea43395e', '20241218164200537344dab91', 1, '/user', 'admin-template', 'admin', 'admin'),
       ('20250120101419647ae6b0ecf', '202412181642005371009bee3', 2, '/user/{id}/enable', 'admin-template', 'admin',
        'admin'),
       ('20250120101419647ffd52280', '202412181642005373662e680', 2, '/user/{id}/disable', 'admin-template', 'admin',
        'admin'),
       ('20250120101419647a79bfa09', '202412181642005385a83fc52', 1, '/user/{id}', 'admin-template', 'admin', 'admin'),
       ('20250120101419647a79bfa09', '20241218164200538505bf81e', 1, '/user/{id}/roles', 'admin-template', 'admin',
        'admin'),
       ('20250120101419647a79bfa09', '202412181642005387dfe84cd', 2, '/user/{id}/bind-role', 'admin-template', 'admin',
        'admin'),
       ('20250120101419647a79bfa09', '202412181642005387a3f6594', 2, '/user/{id}/unbind-role', 'admin-template',
        'admin', 'admin'),
       ('20250120101419647a79bfa09', '202501031614575960e6db9b7', 1, '/user/{id}/{appKey}/roles', 'admin-template',
        'admin', 'admin'),
       ('20250120101419647a79bfa09', '20250113102656767549f7280', 2, '/user/{id}/bind-app', 'admin-template', 'admin',
        'admin'),
       ('20250120101419647a79bfa09', '20250113102656769b9a55fd0', 2, '/user/{id}/unbind-app', 'admin-template', 'admin',
        'admin'),
       ('202501201014196470c77fcce', '202412181642005382355d301', 1, '/role', 'admin-template', 'admin', 'admin'),
       ('20250120101419647c878fe10', '202412181642005380b8d9d41', 2, '/role', 'admin-template', 'admin', 'admin'),
       ('20250120101419647c252f723', '20241218164200538b560ab69', 2, '/role/{id}/enable', 'admin-template', 'admin',
        'admin'),
       ('20250120101419648c30e5f33', '20241218164200538987af84d', 2, '/role/{id}/disable', 'admin-template', 'admin',
        'admin'),
       ('202501201014196470c5100cd', '20241218164200538e54b384e', 1, '/role/{id}', 'admin-template', 'admin', 'admin'),
       ('202501201014196470c5100cd', '202412181642005386ffb756c', 1, '/role/{id}/exist-user', 'admin-template', 'admin',
        'admin'),
       ('202501201014196470c5100cd', '20241218164200538563d4837', 1, '/role/{id}/user', 'admin-template', 'admin',
        'admin'),
       ('202501201014196485a6f1355', '20241218164200538cfff4700', 1, '/menu', 'admin-template', 'admin', 'admin'),
       ('20250120101419648c7b1c47d', '20241218164200538663a7a35', 2, '/menu', 'admin-template', 'admin', 'admin'),
       ('2025012010141964876efa1c4', '202412181642005387a860f31', 3, '/menu/{id}', 'admin-template', 'admin', 'admin'),
       ('2025012010141964876efa1c4', '2024121816420053997da8141', 1, '/menu/{id}', 'admin-template', 'admin', 'admin'),
       ('2025012010141964876efa1c4', '20241218164200539fdef73ea', 2, '/menu/{id}/disassociate', 'admin-template',
        'admin', 'admin'),
       ('2025012010141964876efa1c4', '20241218164200539705b28df', 2, '/menu/{id}/associate', 'admin-template', 'admin',
        'admin'),
       ('202501201014196480afc45f9', '20241218164200539b1946060', 2, '/menu/{id}/enable', 'admin-template', 'admin',
        'admin'),
       ('2025012010141964886be0b2b', '202412181642005394babeb41', 2, '/menu/{id}/disable', 'admin-template', 'admin',
        'admin'),
       ('2025012010141964898b4ec06', '2024121816420053967cc2df5', 1, '/function', 'admin-template', 'admin', 'admin'),
       ('2025012010141964804d278d0', '202412181642005393d12337c', 2, '/function', 'admin-template', 'admin', 'admin'),
       ('202501201014196483f07a776', '202412181642005396aa16eef', 3, '/function/{id}', 'admin-template', 'admin',
        'admin'),
       ('202501201014196484813c870', '202412181642005392272f27a', 2, '/function/{id}/enable', 'admin-template', 'admin',
        'admin'),
       ('20250120101419648a2932895', '2024121816420053963dc6bb9', 2, '/function/{id}/disable', 'admin-template',
        'admin', 'admin'),
       ('202501201014196484dfa3543', '20250120095256828177e8a36', 1, '/function/group', 'admin-template', 'admin',
        'admin'),
       ('20250120101419648b8645dbc', '20250120095256828d9bcb3b0', 2, '/function/group', 'admin-template', 'admin',
        'admin'),
       ('2025012010141964839b7253e', '202501200952568253b859192', 1, '/function/group/{id}', 'admin-template', 'admin',
        'admin'),
       ('2025012010141964839b7253e', '202501200952568283728d899', 3, '/function/group/{id}', 'admin-template', 'admin',
        'admin'),
       ('20250120101419648d9c7f269', '202501200952568284715aeb3', 2, '/function/group/{id}/enable', 'admin-template',
        'admin', 'admin'),
       ('20250120101419648cf95fed1', '20250120095256828fb5954a9', 2, '/function/group/{id}/disable', 'admin-template',
        'admin', 'admin'),
       ('20250120104236418ed8bbb37', '20241218173106180d26b2b48', 1, '/app-info', 'admin-template', 'admin', 'admin'),
       ('2025012010423641895886cf8', '202412181731061858279268d', 2, '/app-info/{id}/enable', 'admin-template', 'admin',
        'admin'),
       ('202501201042364189989b42a', '2024121817310618562604a00', 2, '/app-info/{id}/disable', 'admin-template',
        'admin', 'admin'),
       ('2025012010423641867c0c6d2', '20241218173106185e0d72178', 4, '/app-info/{id}', 'admin-template', 'admin',
        'admin'),
       ('20250120104236418889520cb', '202412181731061852356627b', 2, '/app-info', 'admin-template', 'admin', 'admin'),
       ('20250120101419647a79bfa09', '202501121859049130e372521', 1, '/app-info/user', 'admin-template', 'admin',
        'admin');

create table `user_role`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `username`    varchar(32)     NOT NULL COMMENT '用户id',
    `role_no`     varchar(32)     NOT NULL COMMENT '角色id',
    `app_key`     varchar(256)    NOT NULL COMMENT '系统标识',
    `creator`     varchar(64)     NOT NULL COMMENT '创建人',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)     NOT NULL COMMENT '更新人',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_roleNo` (`role_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色用户关联表';

INSERT INTO user_role (username, role_no, app_key, creator, updater)
VALUES ('admin', '2024121816420053758022525', 'admin-template', 'admin', 'admin');

create table `role_menu`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT,
    `role_no`          varchar(32)     NOT NULL COMMENT '角色id',
    `frontend_page_no` varchar(32)     NOT NULL COMMENT 'web组件id',
    `group_no`         varchar(32)     NOT NULL COMMENT '功能组id',
    `app_key`          varchar(256)    NOT NULL COMMENT '系统标识',
    `creator`          varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`          varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`      datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_frontendPageNo` (frontend_page_no),
    INDEX `idx_group_no` (group_no),
    INDEX `idx_roleNo` (`role_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='角色用户关联表';

INSERT INTO role_menu (role_no, frontend_page_no, group_no, app_key, creator, updater)
VALUES ('2024121816420053758022525', '2025012010423641885787cb9', '20250120101419647ea43395e', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641885787cb9', '20250120101419647ffd52280', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641885787cb9', '20250120101419647a79bfa09', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641885787cb9', '20250120101419647ae6b0ecf', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641885787cb9', '202501231735476998283e204', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '202501201014196470c77fcce', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '20250120101419647c252f723', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '20250120101419647c878fe10', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '202501201014196470c5100cd', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '20250120101419648c30e5f33', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '202501201042364184400ee2d', '20250123173547703a3197913', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '202501201014196485a6f1355', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '202501201014196480afc45f9', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '20250120101419648c7b1c47d', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '2025012010141964876efa1c4', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '2025012010141964886be0b2b', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641889e4681e', '202501231735477043a27692e', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '202501201014196484dfa3543', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '20250120101419648d9c7f269', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '20250120101419648b8645dbc', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '2025012010141964839b7253e', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '20250120101419648cf95fed1', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250123161554093b3be9ba9', '2025012317354770498f071ae', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '2025012010141964898b4ec06', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '202501201014196484813c870', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '2025012010141964804d278d0', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '202501201014196483f07a776', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '20250120101419648a2932895', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '20250120104236418ec6318b6', '20250123173547704c678b526', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641864b1550f', '20250120104236418889520cb', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641864b1550f', '2025012010423641895886cf8', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641864b1550f', '20250120104236418ed8bbb37', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641864b1550f', '202501201042364189989b42a', 'admin-template',
        'admin', 'admin'),
       ('2024121816420053758022525', '2025012010423641864b1550f', '2025012010423641867c0c6d2', 'admin-template',
        'admin', 'admin');

create table `app_info`
(
    `id`                         bigint unsigned NOT NULL AUTO_INCREMENT,
    `app_key`                    varchar(64)     NOT NULL COMMENT 'appKey',
    `app_name`                   varchar(64)     NOT NULL COMMENT 'app名称',
    `description`                varchar(512) COMMENT 'app描述',
    `app_type`                   smallint        NOT NULL COMMENT 'app 类型。1-后端，10-web端，100-小程序，1000-移动端安卓，10000-移动端ios',
    `access_control_by`          tinyint         NOT NULL COMMENT '权限控制方式。1-rbac服务控制，2-自己控制',
    `grant_access_permission_by` tinyint         NOT NULL COMMENT '访问权限授予方式。1-自动，2-手动',
    `status`                     tinyint         NOT NULL COMMENT '状态。1-使用中，2-已停用',
    `creator`                    varchar(64)     NOT NULL COMMENT '创建人',
    `create_time`                datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                    varchar(64)     NOT NULL COMMENT '更新人',
    `update_time`                datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique key `uni_appKey` (`app_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='应用信息表';

INSERT INTO app_info (app_key, app_name, description, app_type, access_control_by,
                      grant_access_permission_by, status, creator, updater)
VALUES ('admin-template', ' 后台管理模板项目', null, 1, 1, 2, 1, 'admin', 'admin');

create table `user_app`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT,
    `username`    varchar(32)     NOT NULL COMMENT '用户id',
    `app_key`     varchar(64)     NOT NULL COMMENT 'appKey',
    `creator`     varchar(64)     NOT NULL COMMENT '创建人',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)     NOT NULL COMMENT '更新人',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique key `uni_username_appKey` (`username`, `app_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户应用关联表';

insert into user_app (username, app_key, creator, updater)
values ('admin', 'admin-template', 'admin', 'admin');

