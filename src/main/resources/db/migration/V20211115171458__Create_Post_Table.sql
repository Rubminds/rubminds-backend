DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`          BIGINT       NOT NULL,
    `team_id`          BIGINT       DEFAULT NULL,
    `title`            VARCHAR(50)  NULL NULL,
    `content`          VARCHAR(255) NOT NULL,
    `headcount`        INT          DEFAULT NULL,
    `kinds`            VARCHAR(30)  NOT NULL,
    `meeting`          VARCHAR(30)  NOT NULL,
    `post_status`      VARCHAR(20)  NOT NULL,
    `region`           VARCHAR(20)  NOT NULL,
    `complete_content` VARCHAR(255),
    `ref_link`         VARCHAR(255),
    `created_at`       DATETIME DEFAULT NULL,
    `updated_at`       DATETIME DEFAULT NULL,
    `deleted_at`       DATETIME DEFAULT NULL,
    `created_by`       BIGINT   DEFAULT NULL,
    `updated_by`       BIGINT   DEFAULT NULL,

    PRIMARY KEY (`id`)
);

