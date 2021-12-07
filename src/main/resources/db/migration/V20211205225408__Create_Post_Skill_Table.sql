DROP TABLE IF EXISTS `post_skill`;
CREATE TABLE `post_skill`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `skill_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);