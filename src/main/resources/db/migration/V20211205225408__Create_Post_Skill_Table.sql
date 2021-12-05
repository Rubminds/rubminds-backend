DROP TABLE IF EXISTS `postskill`;
CREATE TABLE `postskill`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `skill_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);