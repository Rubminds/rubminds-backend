DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `skill` (name)
VALUES ('Java'),
       ('Python'),
       ('C'),
       ('C++'),
       ('Javascript'),
       ('Springboot'),
       ('React'),
       ('Vue'),
       ('NodeJS'),
       ('Aws'),
       ('Docker'),
       ('Html'),
       ('Css'),
       ('Django'),
       ('Flask');