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
    `update_id`     VARCHAR(128),
    `parent`        VARCHAR(1024),
    INDEX (`parent`)
);