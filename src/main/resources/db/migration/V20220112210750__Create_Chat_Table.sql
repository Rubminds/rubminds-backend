DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `chatroom_id`      BIGINT       NOT NULL,
    `sender_id`        BIGINT       NULL NULL,
    `content`          VARCHAR(255)  NOT NULL,
    `created_at`       DATETIME DEFAULT NULL,
    `updated_at`       DATETIME DEFAULT NULL,
    `deleted_at`       DATETIME DEFAULT NULL,
    `created_by`       BIGINT   DEFAULT NULL,
    `updated_by`       BIGINT   DEFAULT NULL,

    PRIMARY KEY (`id`)
);
