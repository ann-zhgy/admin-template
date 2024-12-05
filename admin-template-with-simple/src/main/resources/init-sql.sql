CREATE TABLE `user`
(
    `id`          bigint unsigned  NOT NULL AUTO_INCREMENT,
    `username`    varchar(32)      NOT NULL COMMENT '用户名',
    `password`    varchar(256)     NOT NULL COMMENT '密码',
    `phone`       varchar(32)      NOT NULL COMMENT '手机号',
    `email`       varchar(32)      NOT NULL COMMENT '邮箱',
    `permission`  tinyint unsigned NOT NULL COMMENT '权限，使用位运算表示，1<<0-basic，1<<1-admin，1<<7-SuperAdmin',
    `status`      tinyint          NOT NULL COMMENT '状态。1-使用中，2-已注销',
    `create_time` datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_username` (`username`),
    UNIQUE KEY `uni_phone` (`phone`),
    UNIQUE KEY `uni_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='账号信息';
