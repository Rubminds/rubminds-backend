DROP TABLE IF EXISTS `avatar`;
CREATE TABLE `avatar`
(
    `avatar_id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(255)  NOT NULL,
    `extension`    VARCHAR(255)  NOT NULL,
    `url`          VARCHAR(2083) NOT NULL,
    `size`         BIGINT        NOT NULL,
    `width`        INT           NOT NULL,
    `height`       INT           NOT NULL,
    `created_at`   DATETIME      NOT NULL,
    `updated_at`   DATETIME  DEFAULT NULL,
    `deleted_at`   DATETIME  DEFAULT NULL,
    `created_by`   BIGINT    DEFAULT NULL,
    `updated_by`   BIGINT    DEFAULT NULL,
    PRIMARY KEY (`avatar_id`)
);