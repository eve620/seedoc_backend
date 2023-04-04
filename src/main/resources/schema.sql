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
    `parent`        VARCHAR(1024),
    INDEX (`parent`)
);

CREATE TABLE IF NOT EXISTS users
(
    `id`            VARCHAR(128) NOT NULL PRIMARY KEY,
    `group`         VARCHAR(128),
    `context`       VARCHAR(1024),
    `password`      VARCHAR(128),
    `role`          ENUM('user','admin'),
    `name`          VARCHAR(128) NOT NULL
);