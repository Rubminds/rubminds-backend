DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`      BIGINT      NOT NULL,
    `post_id`      BIGINT      NOT NULL,
    PRIMARY KEY (`id`)
);