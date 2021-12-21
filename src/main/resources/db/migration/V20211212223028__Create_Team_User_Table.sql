DROP TABLE IF EXISTS `team_user`;
CREATE TABLE `team_user`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`      BIGINT      NOT NULL,
    `team_id`      BIGINT      NOT NULL,
    `finish`       boolean DEFAULT NULL,
    `attend_level` DOUBLE  DEFAULT NULL,
    `work_level`   DOUBLE  DEFAULT NULL,
    PRIMARY KEY (`id`)
);
