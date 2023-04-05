CREATE TABLE IF NOT EXISTS files
(
    `id`            BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`          VARCHAR(128)    NOT NULL,
    `size`          BIGINT,
    `content_type`  VARCHAR(24),
    `owner`         VARCHAR(128)    NOT NULL,
    `created`       DATETIME DEFAULT NOW(),
    `last_modified` DATETIME DEFAULT NOW(),
    `etag`          VARCHAR(128),
    `upload_id`     VARCHAR(128),
    `parent`        VARCHAR(512),
    INDEX (`parent`)
);

CREATE TABLE IF NOT EXISTS users
(
    `id`            VARCHAR(128) NOT NULL PRIMARY KEY,
    `group`         VARCHAR(128),
    `context`       VARCHAR(1024),
    `password`      VARCHAR(128),
    `role`          ENUM('USER','ADMIN'),
    `name`          VARCHAR(128) NOT NULL
);

INSERT IGNORE INTO users
    (id, `group`, context,password,role,name)
VALUES
    ('admin', '','','admin','ADMIN','admin');

CREATE TABLE IF NOT EXISTS `groups`
(
    `id`            VARCHAR(48) NOT NULL PRIMARY KEY,
    `context`         VARCHAR(1024),
    `name`          VARCHAR(128)
);

INSERT IGNORE INTO `groups`
    (id, context, name)
VALUES
    ('','','admin');