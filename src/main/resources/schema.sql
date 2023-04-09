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


CREATE TABLE `users`
(
    `id`            int          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username`      varchar(128) NOT NULL UNIQUE,
    `password`      varchar(128) NOT NULL,
    `password_salt` varchar(128),
    `role`          varchar(128) NOT NULL
);


INSERT IGNORE INTO `users`
    (`id`, `username`, `password`, `password_salt`, `role`)
VALUES
    ('admin', 'admin', 'admin', NULL , 'admin');