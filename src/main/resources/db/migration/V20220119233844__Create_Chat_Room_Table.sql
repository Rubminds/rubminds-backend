DROP TABLE IF EXISTS `chat_room`;
CREATE TABLE `chat_room`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT,
    `post_id`      BIGINT       NOT NULL,

    PRIMARY KEY (`id`)
);
