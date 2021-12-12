DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `admin_id`      BIGINT      NOT NULL,
    `post_id`     BIGINT      NOT NULL,
    `created_at`   DATETIME    NOT NULL,
    `updated_at`   DATETIME DEFAULT NULL,
    `deleted_at`   DATETIME DEFAULT NULL,
    `created_by`   BIGINT   DEFAULT NULL,
    `updated_by`   BIGINT   DEFAULT NULL,



    PRIMARY KEY (`id`)
);
