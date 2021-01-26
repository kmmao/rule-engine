create table rule_engine_function_param
(
    id          int auto_increment
        primary key,
    function_id int                                 not null,
    param_name  varchar(100)                        null,
    param_code  varchar(100)                        null,
    value_type  varchar(50)                         null,
    create_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_time timestamp                           null,
    deleted     tinyint                             null
);

create index rule_engine_function_param_function_id_index
    on rule_engine_function_param (function_id);

INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (1, '普通参数', 'value', 'STRING', '2020-08-27 17:43:54', '2020-07-16 13:01:21', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (2, '集合', 'list', 'COLLECTION', '2020-08-27 17:43:53', '2020-07-19 18:54:39', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '服务器地址', 'mailSmtpHost', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:05', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '发送人', 'user', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:20', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '发送人密码', 'password', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:44', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '邮件接收人', 'tos', 'COLLECTION', '2020-08-27 17:43:54', '2020-08-18 17:10:07', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '邮件标题', 'title', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:33', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '邮件内容', 'text', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:50', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (4, '集合', 'list', 'COLLECTION', '2020-08-28 14:40:29', '2020-08-28 14:40:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (6, '字符串', 'value', 'STRING', '2020-08-28 14:44:22', '2020-08-28 14:44:24', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (7, '数值', 'value', 'NUMBER', '2020-08-28 14:45:28', '2020-08-28 14:45:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, '端口号', 'mailSmtpPort', 'NUMBER', '2020-08-29 01:36:00', '2020-08-29 01:36:02', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (8, '集合', 'list', 'COLLECTION', '2020-08-30 02:06:06', '2020-08-30 02:06:07', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (8, '索引', 'index', 'NUMBER', '2020-08-30 02:06:24', '2020-08-30 02:06:25', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (9, '数值', 'value', 'NUMBER', '2020-08-30 02:17:18', '2020-08-30 02:17:19', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (9, '最小', 'min', 'NUMBER', '2020-08-30 02:17:37', '2020-08-30 02:17:39', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (9, '最大', 'max', 'NUMBER', '2020-08-30 02:17:57', '2020-08-30 02:17:58', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (10, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:42:10', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (10, '小树位', 'scale', 'NUMBER', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (11, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (12, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:49:34', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (13, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:50:41', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (14, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:51:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (15, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:39', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (16, '手机号', 'mobile', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:53:56', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (17, '身份证号', 'citizenId', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:54:59', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (18, '字符串', 'value', 'STRING', '2020-09-01 14:34:22', '2020-09-01 14:34:24', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (18, '分隔符', 'regex', 'STRING', '2020-09-01 14:34:51', '2020-09-01 14:34:52', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (21, '字符串', 'value', 'STRING', '2020-11-18 23:50:55', '2020-11-18 23:50:57', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (21, '目标', 'target', 'STRING', '2020-11-18 23:51:28', '2020-11-18 23:51:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (21, '替身', 'replacement', 'STRING', '2020-11-18 23:51:51', '2020-11-18 23:51:52', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (22, '集合', 'list', 'COLLECTION', '2020-11-19 00:00:44', '2020-11-19 00:00:45', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (23, '格式', 'pattern', 'STRING', '2020-11-19 00:38:14', '2020-11-19 00:38:15', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (23, '时区', 'timeZone', 'STRING', '2020-11-19 00:38:44', '2020-11-19 00:38:46', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (24, '手机号', 'phone', 'STRING', '2020-12-13 13:29:29', '2020-12-13 13:29:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (25, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 13:51:39', '2020-12-13 13:51:41', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (25, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 13:52:35', '2020-12-13 13:52:37', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (26, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 14:09:20', '2020-12-13 14:09:21', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (26, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 14:09:56', '2020-12-13 14:09:57', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (27, '字母', 'letter', 'STRING', '2020-12-24 00:17:14', '2020-12-24 00:17:15', 0);
INSERT INTO boot_engine.rule_engine_function_param (function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (28, '字母', 'letter', 'STRING', '2020-12-24 00:17:29', '2020-12-24 00:17:30', 0);