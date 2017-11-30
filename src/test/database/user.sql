DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id`      INT(10)     NOT NULL AUTO_INCREMENT,
  `name`    VARCHAR(50) NOT NULL DEFAULT '',
  `age`     INT(4)      NOT NULL DEFAULT 0,
  `address` VARCHAR(50) NOT NULL DEFAULT '',
  `email`   VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;

INSERT INTO user (id, name, age, address, email) VALUES (1, '任欢', 25, '湖南省安乡县', '469656844@qq.com');