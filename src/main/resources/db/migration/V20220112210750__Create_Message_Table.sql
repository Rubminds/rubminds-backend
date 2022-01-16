DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `post_id`          BIGINT       NOT NULL,
    `sender_id`        BIGINT       NULL NULL,
    `receiver_id`      BIGINT       NOT NULL,
    `content`          VARCHAR(255)  NOT NULL,
    `is_read`             BOOLEAN       NOT NULL,
    `created_at`       DATETIME DEFAULT NULL,
    `updated_at`       DATETIME DEFAULT NULL,
    `deleted_at`       DATETIME DEFAULT NULL,
    `created_by`       BIGINT   DEFAULT NULL,
    `updated_by`       BIGINT   DEFAULT NULL,

    PRIMARY KEY (`id`)
);
