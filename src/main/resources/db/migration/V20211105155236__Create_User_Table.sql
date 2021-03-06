DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `oauth_id`     VARCHAR(50) NOT NULL,
    `avatar_id`    BIGINT      DEFAULT NULL,
    `nickname`     VARCHAR(50),
    `job`          VARCHAR(50),
    `introduce`    VARCHAR(255),
    `provider`     VARCHAR(30) NOT NULL,
    `signup_check` BOOLEAN     NOT NULL,
    `role`         VARCHAR(20) NOT NULL,
    `attend_level` DOUBLE   DEFAULT 0,
    `work_level`   DOUBLE   DEFAULT 0,
    `created_at`   DATETIME    NOT NULL,
    `updated_at`   DATETIME DEFAULT NULL,
    `deleted_at`   DATETIME DEFAULT NULL,
    `created_by`   BIGINT   DEFAULT NULL,
    `updated_by`   BIGINT   DEFAULT NULL,
    PRIMARY KEY (`id`)
);
