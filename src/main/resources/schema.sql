CREATE TABLE IF NOT EXISTS files
(
    `id`            BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`          VARCHAR(128)    NOT NULL,
    `size`          BIGINT,
    `content_type`  VARCHAR(64),
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
    `id`         varchar(40)           NOT NULL PRIMARY KEY,
    `username`   varchar(128)          NOT NULL UNIQUE,
    `password`   varchar(128)          NOT NULL,
    `permission` varchar(1024)         NOT NULL,
    `role`       enum ('ADMIN','USER') NOT NULL,
    INDEX `idx_username` (username)
);


INSERT IGNORE INTO `users`
    (`id`, `username`, `password`, `role`, `permission`)
VALUES ('admin', 'admin', 'admin', 'ADMIN', '*');

-- 目录结构;
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (11, '电子信息工程', null, 'dir', 'admin', null, '2023-04-11 16:25:48', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (12, '信息对抗技术', null, 'dir', 'admin', null, '2023-04-11 16:25:57', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (13, '电磁场与无线技术', null, 'dir', 'admin', null, '2023-04-11 16:26:12', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (14, '遥感科学与技术', null, 'dir', 'admin', null, '2023-04-11 16:26:28', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (15, '中法合作办学', null, 'dir', 'admin', null, '2023-04-11 16:26:38', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (16, '毕德显班', null, 'dir', 'admin', null, '2023-04-11 16:26:46', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (17, '卓越班', null, 'dir', 'admin', null, '2023-04-11 16:26:55', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (18, '教学办公室', null, 'dir', 'admin', null, '2023-04-11 16:27:02', null, null, '');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (19, '2022年', null, 'dir', 'admin', null, '2023-04-11 16:27:16', null, null, '教学办公室');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (20, '2023年', null, 'dir', 'admin', null, '2023-04-11 16:27:24', null, null, '教学办公室');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (22, '会议纪要', null, 'dir', 'admin', null, '2023-04-11 16:27:50', null, null, '教学办公室/2023年');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (23, '新闻稿', null, 'dir', 'admin', null, '2023-04-11 16:27:56', null, null, '教学办公室/2023年');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (24, '制度文件', null, 'dir', 'admin', null, '2023-04-11 16:28:05', null, null, '教学办公室/2023年');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (25, '专业建设', null, 'dir', 'admin', null, '2023-04-11 16:28:16', null, null, '教学办公室/2023年/制度文件');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (27, '培养方案', null, 'dir', 'admin', null, '2023-04-11 16:28:44', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (28, '招生宣传', null, 'dir', 'admin', null, '2023-04-11 16:28:53', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (29, '课程建设', null, 'dir', 'admin', null, '2023-04-11 16:29:02', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (30, '教材建设', null, 'dir', 'admin', null, '2023-04-11 16:29:10', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (31, '师资建设', null, 'dir', 'admin', null, '2023-04-11 16:29:19', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (32, '教改项目', null, 'dir', 'admin', null, '2023-04-11 16:29:26', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (33, '其他', null, 'dir', 'admin', null, '2023-04-11 16:29:33', null, null, '教学办公室/2023年/制度文件/专业建设');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (34, '学籍', null, 'dir', 'admin', null, '2023-04-11 16:29:42', null, null, '教学办公室/2023年/制度文件');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (35, '教务考务', null, 'dir', 'admin', null, '2023-04-11 16:29:50', null, null, '教学办公室/2023年/制度文件');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (36, '学生实践', null, 'dir', 'admin', null, '2023-04-11 16:29:58', null, null, '教学办公室/2023年/制度文件');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (37, '毕业设计', null, 'dir', 'admin', null, '2023-04-11 16:30:10', null, null, '教学办公室/2023年/制度文件/学生实践');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (38, '实习', null, 'dir', 'admin', null, '2023-04-11 16:30:17', null, null, '教学办公室/2023年/制度文件/学生实践');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (39, '学科竞赛', null, 'dir', 'admin', null, '2023-04-11 16:30:23', null, null, '教学办公室/2023年/制度文件/学生实践');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (40, '德显科创计划', null, 'dir', 'admin', null, '2023-04-11 16:30:28', null, null, '教学办公室/2023年/制度文件/学生实践');
-- INSERT INTO files (id, name, size, content_type, owner, created, last_modified, etag, upload_id, parent) VALUES (41, '大创', null, 'dir', 'admin', null, '2023-04-11 16:30:33', null, null, '教学办公室/2023年/制度文件/学生实践');

--  用户信息
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('18030110', '吴媛媛', '123456', '中法合作办学/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('18370003', '张燕菲', '123456', '中法合作办学/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('18500004', '司博', '123456', '中法合作办学/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4228', '任爱锋', '123456', '遥感科学与技术/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4362', '汤建龙', '123456', '信息对抗技术/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4456', '胡海虹', '123456', '中法合作办学/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4649', '董春曦', '123456', '信息对抗技术/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4809', '翟会清', '123456', '电子信息工程/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4893', '李军', '123456', '毕德显班/', 'USER');
-- INSERT INTO clouddisk.users (id, username, password, permission, role) VALUES ('4952', '李龙', '123456', '电磁场与无线技术/', 'USER');
