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


CREATE TABLE IF NOT EXISTS `users`
(
    `id`            varchar(40)   NOT NULL PRIMARY KEY,
    `username`      varchar(128)  NOT NULL UNIQUE,
    `password`      varchar(128)  NOT NULL,
    `permission`    varchar(1024) NOT NULL,
    `password_salt` varchar(128),
    `role`          enum('ADMIN','USER')  NOT NULL,
    INDEX `idx_username` (username)
);


INSERT IGNORE INTO `users`
    (`id`, `username`, `password`, `password_salt`, `role`, `permission`)
VALUES ('admin', 'admin', 'admin', NULL, 'ADMIN', '*');