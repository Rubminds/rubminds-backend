DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `url`  VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `skill` (name, url)
VALUES ('Java', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/c29e19dd-d5b2-477e-a3f0-e8afce265549.png'),
       ('Python', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/9d742d01-8e0f-409a-86c9-8188bec6f9c3.png'),
       ('C', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/38c70473-69af-4227-a0c1-1cfb33ebabc7.png'),
       ('C++', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/41fc21a1-1ffe-421b-a765-4734035c991a.png'),
       ('Javascript', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/b4f683e2-de04-4ab2-8652-ef1ef8b25c9c.png'),
       ('Springboot', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/025e433f-533d-49e8-8209-c8049b02aa24.png'),
       ('React', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/63fc4ed0-dae0-47d8-9036-2fff4bf6df58.png'),
       ('Vue', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/e5d13b42-e837-4083-8713-3d1f1e91441d.png'),
       ('NodeJS', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/806f8054-6846-40aa-9098-2df9ea4bd054.png'),
       ('Aws', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/005cd543-8577-4598-bc69-8dab8c042125.png'),
       ('Docker', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/36bd12d4-7e4a-4770-9643-68d8cddefc44.png'),
       ('Html', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/1ae77af9-c2a5-4fdd-8263-db49510ce80c.png'),
       ('Css','https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/72784bf1-0c3d-42e1-9cf8-538e97666238.png'),
       ('Django', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/7df4f6a5-561d-4d8c-bec1-3919216578ba.png'),
       ('Flask', 'https://s3.ap-northeast-2.amazonaws.com/s3.rubminds.file/1f933f39-cb02-460a-813c-c5c48bc00d3c.png');