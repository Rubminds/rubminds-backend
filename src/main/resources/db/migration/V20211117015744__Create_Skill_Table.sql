DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `url`  VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `skill` (name, url)
VALUES ('Java', 'Java'),
       ('Python', 'Python'),
       ('C', 'C'),
       ('C++', 'C++'),
       ('Javascript', 'Javascript'),
       ('Springboot', 'Springboot'),
       ('React', 'React'),
       ('Vue', 'Vue'),
       ('NodeJS', 'NodeJS'),
       ('Aws', 'Aws'),
       ('Docker', 'Docker'),
       ('Html', 'Html'),
       ('Css','Css'),
       ('Django', 'Django'),
       ('Flask', 'Flask');