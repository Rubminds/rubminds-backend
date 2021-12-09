DROP TABLE IF EXISTS `post_applicant`;
CREATE TABLE `post_applicant`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);