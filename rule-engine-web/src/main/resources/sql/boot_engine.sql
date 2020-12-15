create table rule_engine_condition
(
    id               int auto_increment
        primary key,
    name             varchar(50)   not null,
    description      varchar(500)  null,
    workspace_id     int           null,
    create_user_id   int           null,
    create_user_name varchar(50)   null,
    left_type        tinyint       null,
    left_value       varchar(2000) null,
    left_value_type  varchar(20)   not null,
    right_type       tinyint       null,
    right_value_type varchar(20)   not null,
    right_value      varchar(2000) null,
    symbol           varchar(20)   null,
    update_time      timestamp     null,
    create_time      timestamp     null,
    deleted          tinyint       null
)
    charset = utf8;

create index rule_engine_condition_name_index
    on rule_engine_condition (name);

INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (46, '测试集合条件2', 'text', 1, null, null, 1, '34', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-08-26 17:54:18', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (47, 'true', null, 1, null, null, 2, 'true', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-09-04 20:50:46', '2020-08-24 15:57:57', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (48, 'false', null, 1, null, null, 2, 'true', 'BOOLEAN', 2, 'BOOLEAN', 'false', 'EQ', '2020-08-24 16:26:51', '2020-08-24 16:26:51', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (51, '年龄', null, 1, null, null, 0, '66', 'NUMBER', 2, 'NUMBER', '20', 'GE', '2020-08-27 00:15:15', '2020-08-27 00:15:15', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (52, 'vip等级', null, 1, null, null, 0, '67', 'NUMBER', 2, 'NUMBER', '3', 'GE', '2020-08-27 00:15:35', '2020-08-27 00:15:35', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (53, '消费金额', null, 1, null, null, 0, '68', 'NUMBER', 2, 'NUMBER', '30000', 'GE', '2020-08-27 00:17:05', '2020-08-27 00:17:05', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (54, '特殊省份', null, 1, null, null, 2, '北京,河南,上海', 'COLLECTION', 0, 'COLLECTION', '69', 'CONTAIN', '2020-08-27 01:05:05', '2020-08-27 01:05:05', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (55, '文化程度', null, 1, null, null, 2, '博士', 'STRING', 0, 'STRING', '70', 'EQ', '2020-08-27 01:08:04', '2020-08-27 01:08:04', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (56, '特殊市区', null, 1, null, null, 2, '北京市,商丘市', 'COLLECTION', 0, 'STRING', '71', 'CONTAIN', '2020-08-27 01:14:08', '2020-08-27 01:13:05', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (57, '审批人测试', null, 1, null, null, 1, '35', 'COLLECTION', 2, 'NUMBER', '123', 'CONTAIN', '2020-08-27 12:35:57', '2020-08-27 12:35:50', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (58, '测试条件-绝对值', null, 1, null, null, 1, '52', 'NUMBER', 2, 'NUMBER', '123', 'EQ', '2020-08-28 14:48:48', '2020-08-28 14:48:48', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (60, '集合在.测试', null, 1, null, null, 0, '61', 'COLLECTION', 2, 'COLLECTION', '1,2', 'IN', '2020-09-10 18:12:49', '2020-08-28 16:25:44', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (64, '年龄范围条件', null, 1, null, null, 1, '70', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-08-30 18:53:37', '2020-08-30 18:53:37', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (65, '年龄大于等于20条件', null, 1, null, null, 0, '66', 'NUMBER', 2, 'NUMBER', '20', 'GE', '2020-08-30 21:25:26', '2020-08-30 21:24:51', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (66, '年龄小于等于26条件', null, 1, null, null, 0, '66', 'NUMBER', 2, 'NUMBER', '26', 'LE', '2020-09-04 20:56:36', '2020-08-30 21:25:18', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (69, '号码开头', null, 1, null, null, 0, '77', 'STRING', 2, 'STRING', '137', 'STARTS_WITH', '2020-09-10 18:08:13', '2020-09-10 18:08:13', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (70, '限制号码长度', null, 1, null, null, 1, '76', 'NUMBER', 2, 'NUMBER', '17', 'LE', '2020-09-10 20:08:15', '2020-09-10 18:11:41', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (71, '排除号码在白名单的', null, 1, null, null, 1, '77', 'COLLECTION', 0, 'STRING', '77', 'CONTAIN', '2020-09-10 23:44:25', '2020-09-10 18:15:27', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (73, '验证是否为手机号码条件', null, 1, null, null, 1, '79', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2020-09-16 14:57:38', '2020-09-16 14:57:38', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (74, 'vip等级大于4以上', null, 1, null, null, 0, '67', 'NUMBER', 2, 'NUMBER', '4', 'GT', '2020-11-13 19:14:00', '2020-11-13 19:14:00', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (75, '消费金额大于3000', null, 1, null, null, 0, '68', 'NUMBER', 2, 'NUMBER', '3000', 'GT', '2020-11-13 19:14:33', '2020-11-13 19:14:33', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (80, '测试年龄在18到50 之间', null, 1, null, null, 2, '18', 'NUMBER', 2, 'NUMBER', '50', 'GE', '2020-11-24 17:20:53', '2020-11-24 17:20:53', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (85, '定期存款年限在', null, 1, null, null, 2, '0.5', 'NUMBER', 2, 'NUMBER', '1', 'LE', '2020-12-07 11:25:49', '2020-12-07 11:24:38', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (86, '存款年限在', null, 1, null, null, 2, '10', 'NUMBER', 2, 'NUMBER', '2', 'GE', '2020-12-07 13:43:09', '2020-12-07 11:25:26', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (87, '账户最低存款额度', null, 1, null, null, 0, '95', 'NUMBER', 2, 'NUMBER', '2000', 'GT', '2020-12-07 11:27:15', '2020-12-07 11:27:15', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (88, '存款年限', null, 1, null, null, 2, '1', 'NUMBER', 2, 'NUMBER', '2', 'LE', '2020-12-07 16:11:35', '2020-12-07 16:11:35', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (89, '最低存款额度大于等于2000', null, 1, null, null, 0, '95', 'NUMBER', 2, 'NUMBER', '2000', 'GE', '2020-12-07 16:27:39', '2020-12-07 16:27:39', 0);
INSERT INTO boot_engine.rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (90, '定期存款年限', null, 1, null, null, 0, '98', 'NUMBER', 2, 'NUMBER', '1', 'GE', '2020-12-14 04:57:40', '2020-12-07 16:28:24', 0);
create table rule_engine_condition_group
(
    id          int auto_increment
        primary key,
    name        varchar(50) not null,
    rule_id     int         null,
    order_no    int         not null,
    create_time timestamp   null,
    update_time timestamp   null,
    deleted     tinyint     null
)
    charset = utf8;

create index rule_engine_condition_group_rule_id_index
    on rule_engine_condition_group (rule_id);

INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (499, '条件组r12sdfasdf', 30, 10, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (500, '条件组', 30, 11, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (573, '条件组', 35, 1, '2020-08-30 18:18:29', '2020-08-30 18:18:29', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (582, '条件组', 37, 1, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (620, '条件组', 27, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (621, '条件组', 27, 2, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (623, '条件组', 42, 1, '2020-09-01 14:02:42', '2020-09-01 14:02:42', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (734, '条件组1', 41, 1, '2020-09-04 19:39:17', '2020-09-04 19:39:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (781, '条件组', 28, 1, '2020-09-07 14:36:48', '2020-09-07 14:36:48', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (785, '条件组', 39, 1, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (793, '条件组', 34, 2, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (794, '条件组', 34, 3, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (796, '条件组', 29, 2, '2020-09-07 22:35:58', '2020-09-07 22:35:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (870, '条件组', 44, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (871, '条件组1', 44, 2, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (886, '条件组', 46, 1, '2020-09-09 21:40:25', '2020-09-09 21:40:25', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (950, '测试条件组1', 21, 12, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (951, '条件组', 21, 16, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (985, '条件组', 48, 1, '2020-10-27 18:25:41', '2020-10-27 18:25:41', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1028, '条件组', 51, 1, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1050, '消费金额满足30000', 24, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1051, '如果学历为博士', 24, 2, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1052, '特殊市区条件组', 24, 4, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1217, '条件组', 71, 1, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1218, '条件组', 71, 2, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1219, '条件组', 71, 3, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1220, '条件组', 71, 4, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1233, '条件组1', 85, 1, '2020-12-15 08:47:31', '2020-12-15 08:47:31', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1234, '条件组2', 85, 2, '2020-12-15 08:47:31', '2020-12-15 08:47:31', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1237, '条件组1', 77, 1, '2020-12-15 14:21:56', '2020-12-15 14:21:56', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1238, '条件组2', 77, 2, '2020-12-15 14:21:56', '2020-12-15 14:21:56', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1239, '先执行简单校验', 47, 1, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
INSERT INTO boot_engine.rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (1240, '查询数据库验证', 47, 2, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
create table rule_engine_condition_group_condition
(
    id                 int auto_increment
        primary key,
    condition_id       int       not null,
    condition_group_id int       not null,
    order_no           int       not null,
    create_time        timestamp null,
    update_time        timestamp null,
    deleted            tinyint   null
)
    charset = utf8;

create index rule_engine_condition_group_condition_condition_group_id_index
    on rule_engine_condition_group_condition (condition_group_id);

INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (40, 45, 21, 1, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (41, 46, 21, 2, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (840, 47, 499, 1, '2020-08-28 21:29:23', '2020-08-28 21:29:23', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (927, 63, 573, 1, '2020-08-30 18:18:29', '2020-08-30 18:18:29', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (937, 64, 582, 1, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (938, 56, 582, 2, '2020-08-30 19:01:02', '2020-08-30 19:01:02', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (979, 58, 620, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (980, 59, 621, 1, '2020-09-01 14:00:25', '2020-09-01 14:00:25', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (982, 47, 623, 1, '2020-09-01 14:02:42', '2020-09-01 14:02:42', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1128, 47, 734, 1, '2020-09-04 19:39:17', '2020-09-04 19:39:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1198, 47, 781, 1, '2020-09-07 14:36:48', '2020-09-07 14:36:48', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1203, 66, 785, 1, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1204, 65, 785, 2, '2020-09-07 21:32:17', '2020-09-07 21:32:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1212, 47, 793, 1, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1213, 59, 794, 1, '2020-09-07 22:34:40', '2020-09-07 22:34:40', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1215, 60, 796, 1, '2020-09-07 22:35:58', '2020-09-07 22:35:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1280, 47, 870, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1281, 48, 871, 1, '2020-09-08 00:29:13', '2020-09-08 00:29:13', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1294, 68, 886, 1, '2020-09-09 21:40:26', '2020-09-09 21:40:26', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1393, 46, 950, 1, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1394, 45, 950, 2, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1395, 57, 951, 1, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1396, 53, 951, 2, '2020-09-10 23:47:27', '2020-09-10 23:47:27', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1450, 59, 985, 1, '2020-10-27 18:25:42', '2020-10-27 18:25:42', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1520, 74, 1028, 1, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1521, 75, 1028, 2, '2020-11-14 10:02:16', '2020-11-14 10:02:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1543, 53, 1050, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1544, 55, 1051, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1545, 56, 1052, 1, '2020-11-14 11:35:28', '2020-11-14 11:35:28', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1813, 80, 1217, 1, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1814, 75, 1217, 2, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1815, 74, 1217, 3, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1816, 73, 1218, 1, '2020-12-14 09:45:33', '2020-12-14 09:45:33', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1837, 69, 1233, 1, '2020-12-15 08:47:31', '2020-12-15 08:47:31', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1838, 71, 1233, 2, '2020-12-15 08:47:31', '2020-12-15 08:47:31', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1839, 73, 1234, 1, '2020-12-15 08:47:31', '2020-12-15 08:47:31', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1844, 90, 1237, 1, '2020-12-15 14:21:56', '2020-12-15 14:21:56', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1845, 89, 1237, 2, '2020-12-15 14:21:56', '2020-12-15 14:21:56', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1846, 47, 1238, 1, '2020-12-15 14:21:56', '2020-12-15 14:21:56', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1847, 73, 1239, 1, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1848, 70, 1239, 2, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1849, 69, 1239, 3, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (1850, 71, 1240, 1, '2020-12-15 14:22:47', '2020-12-15 14:22:47', 0);
create table rule_engine_element
(
    id               int auto_increment
        primary key,
    name             varchar(50)  not null,
    code             varchar(50)  not null,
    workspace_id     int          null,
    create_user_id   int          null,
    create_user_name varchar(50)  null,
    value_type       varchar(20)  null,
    description      varchar(500) null,
    create_time      timestamp    null,
    update_time      timestamp    null,
    deleted          tinyint      null
)
    charset = utf8;

create index rule_engine_element_name_code_index
    on rule_engine_element (name, code);

INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (60, '字符串元素', 'ele-string', 1, null, null, 'STRING', null, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (61, '集合元素', 'ele-collection', 1, null, null, 'COLLECTION', null, '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (66, '年龄', 'age', 1, null, null, 'NUMBER', null, '2020-08-27 00:14:03', '2020-08-27 00:14:03', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (67, 'vip等级', 'vipLevel', 1, null, null, 'NUMBER', null, '2020-08-27 00:14:34', '2020-08-27 00:14:34', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (68, '消费金额', 'AmountOfConsumption', 1, null, null, 'NUMBER', null, '2020-08-27 00:16:35', '2020-08-27 00:16:35', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (69, '家庭地址（省份）', 'HomeAddressProvince', 1, null, null, 'STRING', null, '2020-08-27 01:01:40', '2020-08-27 01:05:12', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (70, '学历', 'education', 1, null, null, 'STRING', null, '2020-08-27 01:07:30', '2020-08-27 01:07:30', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (71, '家庭地址（市区）', 'HomeAddressCityArea', 1, null, null, 'STRING', null, '2020-08-27 01:13:40', '2020-08-27 01:17:20', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (72, '数值', 'number', 1, null, null, 'NUMBER', null, '2020-08-28 14:47:06', '2020-08-28 14:47:06', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (73, '邮件内容', 'youjianneirong', 1, null, null, 'STRING', null, '2020-08-29 01:33:44', '2020-08-29 01:33:44', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (74, '邮件标题', 'youjianbiaoti', 1, null, null, 'STRING', '123', '2020-08-29 01:34:12', '2020-08-30 11:46:01', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (76, '身份证', 'identityCard', 1, null, null, 'STRING', null, '2020-09-02 13:36:00', '2020-09-02 13:36:00', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (77, '号码', 'phone', 1, null, null, 'STRING', null, '2020-09-10 18:07:45', '2020-09-10 18:07:45', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (81, '注册时间（时间戳）', 'registrationTime', 1, null, null, 'NUMBER', null, '2020-11-13 19:04:31', '2020-11-13 19:04:31', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (82, '号码', 'mailRecipient', 1, null, null, 'COLLECTION', null, '2020-11-13 19:23:02', '2020-11-14 11:14:43', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (94, '定期存款期限', 'fixedDeposit', 1, null, null, 'NUMBER', null, '2020-12-07 11:20:26', '2020-12-07 11:20:26', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (95, '账户最低存款额度', 'lowestLimit', 1, null, null, 'NUMBER', null, '2020-12-07 11:21:39', '2020-12-07 11:21:39', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (96, '每笔存款送多少', 'send', 1, null, null, 'NUMBER', null, '2020-12-07 11:21:56', '2020-12-07 11:21:56', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (97, '存款', 'asdsadsa', 1, null, null, 'NUMBER', null, '2020-12-07 16:10:14', '2020-12-07 16:10:14', 0);
INSERT INTO boot_engine.rule_engine_element (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (98, '定期存款年限', 'fixedMoney', 1, null, null, 'NUMBER', null, '2020-12-07 16:26:04', '2020-12-13 06:47:28', 0);
create table rule_engine_function
(
    id                int auto_increment
        primary key,
    name              varchar(50)                         null,
    description       varchar(500)                        null,
    executor          varchar(50)                         not null,
    return_value_type varchar(50)                         null,
    create_time       timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_time       timestamp                           null,
    deleted           tinyint                             null
)
    charset = utf8;

INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (1, '是否为邮箱', '是否为邮箱函数', 'isEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-16 13:00:43', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (2, '是否为空集合', '是否为空集合函数', 'isEmptyCollectionFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-19 18:54:10', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (3, '发送邮件', '发送邮件函数', 'sendEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-18 17:06:45', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (4, '求集合大小', null, 'collectionSizeFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:39:39', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (5, '执行规则(函数开发中)', '', 'executeRuleFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-28 14:41:53', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (6, '是否为空字符串', '', 'isEmptyFunction', 'BOOLEAN', '2020-11-15 00:28:25', '2020-08-28 14:43:52', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (7, '求绝对值', null, 'mathAbsFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:45:04', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (8, '返回集合中第几个元素', '不存在则返回null', 'getCollectionElementsFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-30 02:05:37', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (9, '在..之间', '', 'isBetweenFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-30 02:16:51', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (10, '求平均值', '集合中必须为number类型的值', 'avgFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:41:44', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (11, '集合中最大值', '集合中必须为number类型的值', 'collectionMaxFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:48:32', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (12, '集合中最小值', '集合中必须为number类型的值', 'collectionMinFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:49:12', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (13, '字符串的长度', '', 'stringLengthFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:50:13', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (14, '字符串去除前后空格', null, 'stringTrimFunction', 'STRING', '2020-09-11 20:26:14', '2020-09-01 13:51:14', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (15, '求和', '集合中必须为number类型的值', 'sumFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:52:08', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (16, '验证是否为手机号码', null, 'isMobileFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:53:17', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (17, '是否为身份证', null, 'isCitizenIdFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:54:34', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (18, '字符串转为集合', null, 'stringToCollectionFunction', 'COLLECTION', '2020-09-11 20:26:14', '2020-09-01 14:33:48', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (21, '字符串替换', null, 'stringReplaceFunction', 'STRING', '2020-11-18 23:50:09', '2020-11-18 23:50:10', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (22, '集合去除重复', null, 'collectionDeduplicationFunction', 'COLLECTION', '2020-11-19 00:00:03', '2020-11-19 00:00:05', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (23, '获取当前时间', null, 'currentDateFunction', 'STRING', '2020-11-19 00:37:33', '2020-11-19 00:37:34', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (24, '获取手机号码所在省份', null, 'mobilePhoneProvinceFunction', 'STRING', '2020-12-13 13:28:58', '2020-12-13 13:28:59', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (25, '获取JSON中指定的值返回STRING类型', '例如：JOSN数据为:{"name":"abc"},获取name的值通过JSON值路径配置为$.name,更多使用方法待文档补全。', 'parseJsonStringFunction', 'STRING', '2020-12-13 13:56:04', '2020-12-13 13:50:52', 0);
INSERT INTO boot_engine.rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (26, '获取JSON中指定的值返回NUMBER类型', '例如：JOSN数据为:{"age":"18"},获取name的值通过JSON值路径配置为$.age,更多使用方法待文档补全。', 'parseJsonNumberFunction', 'NUMBER', '2020-12-13 14:08:28', '2020-12-13 14:08:29', 0);
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
)
    charset = utf8;

create index rule_engine_function_param_function_id_index
    on rule_engine_function_param (function_id);

INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (1, 1, '普通参数', 'value', 'STRING', '2020-08-27 17:43:54', '2020-07-16 13:01:21', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (2, 2, '集合', 'list', 'COLLECTION', '2020-08-27 17:43:53', '2020-07-19 18:54:39', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, 3, '服务器地址', 'mailSmtpHost', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:05', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (4, 3, '发送人', 'user', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:20', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (5, 3, '发送人密码', 'password', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:44', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (6, 3, '邮件接收人', 'tos', 'COLLECTION', '2020-08-27 17:43:54', '2020-08-18 17:10:07', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (7, 3, '邮件标题', 'title', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:33', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (8, 3, '邮件内容', 'text', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:50', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (9, 4, '集合', 'list', 'COLLECTION', '2020-08-28 14:40:29', '2020-08-28 14:40:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (10, 5, '规则入参', 'info', 'STRING', '2020-08-28 14:42:56', '2020-08-28 14:42:57', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (11, 6, '字符串', 'value', 'STRING', '2020-08-28 14:44:22', '2020-08-28 14:44:24', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (12, 7, '数值', 'value', 'NUMBER', '2020-08-28 14:45:28', '2020-08-28 14:45:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (13, 3, '端口号', 'mailSmtpPort', 'NUMBER', '2020-08-29 01:36:00', '2020-08-29 01:36:02', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (14, 8, '集合', 'list', 'COLLECTION', '2020-08-30 02:06:06', '2020-08-30 02:06:07', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (15, 8, '索引', 'index', 'NUMBER', '2020-08-30 02:06:24', '2020-08-30 02:06:25', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (16, 9, '数值', 'value', 'NUMBER', '2020-08-30 02:17:18', '2020-08-30 02:17:19', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (17, 9, '最小', 'min', 'NUMBER', '2020-08-30 02:17:37', '2020-08-30 02:17:39', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (18, 9, '最大', 'max', 'NUMBER', '2020-08-30 02:17:57', '2020-08-30 02:17:58', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (19, 10, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:42:10', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (20, 10, '小树位', 'scale', 'NUMBER', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (21, 11, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (22, 12, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:49:34', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (23, 13, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:50:41', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (24, 14, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:51:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (25, 15, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:39', '2020-09-01 13:46:31', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (26, 16, '手机号', 'mobile', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:53:56', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (27, 17, '身份证号', 'citizenId', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:54:59', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (28, 18, '字符串', 'value', 'STRING', '2020-09-01 14:34:22', '2020-09-01 14:34:24', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (29, 18, '分隔符', 'regex', 'STRING', '2020-09-01 14:34:51', '2020-09-01 14:34:52', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (30, 21, '字符串', 'value', 'STRING', '2020-11-18 23:50:55', '2020-11-18 23:50:57', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (31, 21, '目标', 'target', 'STRING', '2020-11-18 23:51:28', '2020-11-18 23:51:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (32, 21, '替身', 'replacement', 'STRING', '2020-11-18 23:51:51', '2020-11-18 23:51:52', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (33, 22, '集合', 'list', 'COLLECTION', '2020-11-19 00:00:44', '2020-11-19 00:00:45', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (34, 23, '格式', 'pattern', 'STRING', '2020-11-19 00:38:14', '2020-11-19 00:38:15', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (35, 23, '时区', 'timeZone', 'STRING', '2020-11-19 00:38:44', '2020-11-19 00:38:46', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (36, 24, '手机号', 'phone', 'STRING', '2020-12-13 13:29:29', '2020-12-13 13:29:30', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (37, 25, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 13:51:39', '2020-12-13 13:51:41', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (38, 25, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 13:52:35', '2020-12-13 13:52:37', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (39, 26, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 14:09:20', '2020-12-13 14:09:21', 0);
INSERT INTO boot_engine.rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (40, 26, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 14:09:56', '2020-12-13 14:09:57', 0);
create table rule_engine_function_value
(
    id          int auto_increment
        primary key,
    function_id int           not null,
    variable_id int           not null,
    param_name  varchar(100)  null,
    param_code  varchar(100)  null,
    type        int           null comment '0:元素，1:变量,2:固定值',
    value_type  varchar(50)   null,
    value       varchar(2000) null,
    create_time timestamp     null,
    update_time timestamp     null,
    deleted     tinyint       null
)
    charset = utf8;

create index rule_engine_function_value_function_id_variable_id_index
    on rule_engine_function_value (function_id, variable_id);

INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (38, 1, 33, null, 'value', 0, 'STRING', '60', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (39, 2, 34, null, 'list', 0, 'COLLECTION', '61', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (42, 2, 50, '集合', 'list', 2, 'COLLECTION', '123，3', '2020-08-27 22:28:49', '2020-08-27 22:28:49', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (50, 1, 51, '普通参数', 'value', 0, 'STRING', '60', '2020-08-27 23:27:06', '2020-08-27 23:27:06', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (51, 7, 52, '数值', 'value', 0, 'NUMBER', '72', '2020-08-28 14:47:28', '2020-08-28 14:47:28', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (53, 4, 54, '集合', 'list', 1, 'COLLECTION', '35', '2020-08-28 15:22:10', '2020-08-28 15:22:10', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (54, 7, 55, '数值', 'value', 1, 'NUMBER', '53', '2020-08-28 15:26:00', '2020-08-28 15:26:00', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (56, 4, 35, '集合', 'list', 2, 'COLLECTION', '123,a,v', '2020-08-28 16:02:37', '2020-08-28 16:02:37', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (60, 4, 53, '集合', 'list', 0, 'COLLECTION', '61', '2020-08-28 16:22:59', '2020-08-28 16:22:59', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (61, 5, 57, '规则入参', 'info', 0, 'STRING', '60', '2020-08-28 21:29:09', '2020-08-28 21:29:09', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (64, 4, 58, '集合', 'list', 2, 'COLLECTION', '123,123', '2020-08-29 01:28:39', '2020-08-29 01:28:39', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (113, 8, 61, '集合', 'list', 0, 'COLLECTION', '61', '2020-08-30 12:35:49', '2020-08-30 12:35:49', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (114, 8, 61, '索引', 'index', 2, 'NUMBER', '12', '2020-08-30 12:35:49', '2020-08-30 12:35:49', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (115, 3, 63, '服务器地址', 'mailSmtpHost', 0, 'STRING', '60', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (116, 3, 63, '发送人', 'user', 2, 'STRING', '123', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (117, 3, 63, '发送人密码', 'password', null, 'STRING', null, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (118, 3, 63, '邮件接收人', 'tos', null, 'COLLECTION', null, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (119, 3, 63, '邮件标题', 'title', null, 'STRING', null, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (120, 3, 63, '邮件内容', 'text', null, 'STRING', null, '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (121, 3, 63, '端口号', 'mailSmtpPort', 2, 'NUMBER', '0', '2020-08-30 12:40:08', '2020-08-30 12:40:08', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (122, 3, 64, '服务器地址', 'mailSmtpHost', 2, 'STRING', '123', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (123, 3, 64, '发送人', 'user', 2, 'STRING', '123123123', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (124, 3, 64, '发送人密码', 'password', null, 'STRING', null, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (125, 3, 64, '邮件接收人', 'tos', null, 'COLLECTION', null, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (126, 3, 64, '邮件标题', 'title', null, 'STRING', null, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (127, 3, 64, '邮件内容', 'text', null, 'STRING', null, '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (128, 3, 64, '端口号', 'mailSmtpPort', 2, 'NUMBER', '12', '2020-08-30 12:42:12', '2020-08-30 12:42:12', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (136, 6, 67, '字符串', 'value', 2, 'STRING', '123', '2020-08-30 13:20:35', '2020-08-30 13:20:35', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (270, 3, 65, '服务器地址', 'mailSmtpHost', 2, 'STRING', '123', '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (271, 3, 65, '发送人', 'user', 2, 'STRING', null, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (272, 3, 65, '发送人密码', 'password', 2, 'STRING', null, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (273, 3, 65, '邮件接收人', 'tos', 2, 'COLLECTION', null, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (274, 3, 65, '邮件标题', 'title', 2, 'STRING', null, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (275, 3, 65, '邮件内容', 'text', 2, 'STRING', null, '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (276, 3, 65, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-08-30 13:53:58', '2020-08-30 13:53:58', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (286, 9, 70, '数值', 'value', 0, 'NUMBER', '66', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (287, 9, 70, '最小', 'min', 2, 'NUMBER', '20', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (288, 9, 70, '最大', 'max', 2, 'NUMBER', '26', '2020-08-30 23:52:20', '2020-08-30 23:52:20', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (291, 15, 72, '集合', 'list', 2, 'COLLECTION', '1,3', '2020-09-01 14:04:50', '2020-09-01 14:04:50', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (305, 17, 71, '身份证号', 'citizenId', 0, 'STRING', '76', '2020-09-04 19:38:53', '2020-09-04 19:38:53', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (306, 11, 73, '集合', 'list', 2, 'COLLECTION', '1,19,20,5', '2020-09-08 12:19:25', '2020-09-08 12:19:25', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (307, 8, 74, '集合', 'list', 2, 'COLLECTION', '123,34,33', '2020-09-08 12:23:25', '2020-09-08 12:23:25', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (308, 8, 74, '索引', 'index', 2, 'NUMBER', '0', '2020-09-08 12:23:25', '2020-09-08 12:23:25', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (309, 10, 75, '集合', 'list', 2, 'COLLECTION', '4,10', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (310, 10, 75, '小树位', 'scale', 2, 'NUMBER', '2', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (311, 13, 76, '字符串', 'value', 0, 'STRING', '77', '2020-09-10 18:11:06', '2020-09-10 18:11:06', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (314, 16, 79, '手机号', 'mobile', 0, 'STRING', '77', '2020-09-18 19:08:19', '2020-09-18 19:08:19', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (322, 3, 80, '服务器地址', 'mailSmtpHost', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (323, 3, 80, '发送人', 'user', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (324, 3, 80, '发送人密码', 'password', 2, 'STRING', '略', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (325, 3, 80, '邮件接收人', 'tos', 0, 'COLLECTION', '82', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (326, 3, 80, '邮件标题', 'title', 2, 'STRING', '恭喜你获得**优惠卷', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (327, 3, 80, '邮件内容', 'text', 2, 'STRING', '优惠券链接', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (328, 3, 80, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-11-13 19:23:25', '2020-11-13 19:23:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (368, 8, 87, '集合', 'list', 2, 'COLLECTION', '123123', '2020-11-15 00:26:14', '2020-11-15 00:26:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (369, 8, 87, '索引', 'index', 0, 'NUMBER', '83', '2020-11-15 00:26:14', '2020-11-15 00:26:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (374, 1, 84, '普通参数', 'value', 1, 'STRING', '88', '2020-11-15 01:08:50', '2020-11-15 01:08:50', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (377, 8, 88, '集合', 'list', 2, 'COLLECTION', '123', '2020-11-15 01:10:16', '2020-11-15 01:10:16', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (379, 3, 80, '服务器地址', 'mailSmtpHost', 2, 'STRING', 'http://www.baidu.com', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (380, 3, 80, '发送人', 'user', 2, 'STRING', '略', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (381, 3, 80, '发送人密码', 'password', 2, 'STRING', '略', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (382, 3, 80, '邮件接收人', 'tos', 0, 'COLLECTION', '82', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (383, 3, 80, '邮件标题', 'title', 2, 'STRING', '恭喜你获得**优惠卷', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (384, 3, 80, '邮件内容', 'text', 2, 'STRING', '优惠券链接', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (385, 3, 80, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-11-16 23:12:14', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (386, 21, 90, '字符串', 'value', 2, 'STRING', '123', '2020-11-19 01:21:46', '2020-11-19 01:21:46', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (387, 21, 90, '目标', 'target', 2, 'STRING', '1', '2020-11-19 01:21:46', '2020-11-19 01:21:46', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (388, 21, 90, '替身', 'replacement', 2, 'STRING', '1', '2020-11-19 01:21:46', '2020-11-19 01:21:46', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (389, 21, 90, '字符串', 'value', 2, 'STRING', '123', '2020-11-19 01:26:58', '2020-11-19 01:26:58', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (390, 21, 90, '目标', 'target', 2, 'STRING', '1', '2020-11-19 01:26:58', '2020-11-19 01:26:58', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (391, 21, 90, '替身', 'replacement', 1, 'STRING', '90', '2020-11-19 01:26:58', '2020-11-19 01:26:58', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (392, 21, 90, '字符串', 'value', 2, 'STRING', '123', '2020-11-19 01:28:48', '2020-11-19 01:28:48', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (393, 21, 90, '目标', 'target', 2, 'STRING', '1', '2020-11-19 01:28:48', '2020-11-19 01:28:48', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (394, 21, 90, '替身', 'replacement', 2, 'STRING', '2', '2020-11-19 01:28:48', '2020-11-19 01:28:48', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (395, 17, 91, '身份证号', 'citizenId', 2, 'STRING', '1', '2020-11-19 15:14:32', '2020-11-19 15:14:32', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (396, 14, 92, '字符串', 'value', 2, 'STRING', '123', '2020-11-19 15:16:04', '2020-11-19 15:16:04', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (397, 14, 93, '字符串', 'value', 1, 'STRING', '92', '2020-11-19 15:16:25', '2020-11-19 15:16:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (398, 14, 92, '字符串', 'value', 1, 'STRING', '93', '2020-11-19 15:17:36', '2020-11-19 15:17:36', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (399, 21, 94, '字符串', 'value', 2, 'STRING', '123', '2020-11-19 15:18:35', '2020-11-19 15:18:35', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (400, 21, 94, '目标', 'target', 2, 'STRING', '123', '2020-11-19 15:18:35', '2020-11-19 15:18:35', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (401, 21, 94, '替身', 'replacement', 1, 'STRING', '93', '2020-11-19 15:18:35', '2020-11-19 15:18:35', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (402, 14, 92, '字符串', 'value', 1, 'STRING', '94', '2020-11-19 15:18:43', '2020-11-19 15:18:43', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (403, 14, 93, '字符串', 'value', 2, 'STRING', '1', '2020-11-19 15:19:21', '2020-11-19 15:19:21', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (404, 14, 96, '字符串', 'value', 2, 'STRING', 'df', '2020-11-19 23:51:38', '2020-11-19 23:51:38', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (405, 14, 97, '字符串', 'value', 1, 'STRING', '96', '2020-11-19 23:51:59', '2020-11-19 23:51:59', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (406, 14, 96, '字符串', 'value', 2, 'STRING', 'vb', '2020-11-19 23:52:21', '2020-11-19 23:52:21', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (407, 14, 96, '字符串', 'value', 1, 'STRING', '97', '2020-11-19 23:53:17', '2020-11-19 23:53:17', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (408, 14, 97, '字符串', 'value', 2, 'STRING', 'df', '2020-11-20 00:14:52', '2020-11-20 00:14:52', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (409, 15, 98, '集合', 'list', 2, 'COLLECTION', '1,2,3', '2020-12-01 18:18:31', '2020-12-01 18:18:31', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (410, 15, 98, '集合', 'list', 0, 'COLLECTION', '61', '2020-12-01 18:20:32', '2020-12-01 18:20:32', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (411, 15, 99, '集合', 'list', 0, 'COLLECTION', '61', '2020-12-01 18:21:23', '2020-12-01 18:21:23', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (412, 15, 99, '集合', 'list', 0, 'COLLECTION', '93', '2020-12-01 18:30:32', '2020-12-01 18:30:32', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (413, 21, 100, '字符串', 'value', 0, 'STRING', '60', '2020-12-04 18:00:19', '2020-12-04 18:00:19', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (414, 21, 100, '目标', 'target', 2, 'STRING', 'zz', '2020-12-04 18:00:19', '2020-12-04 18:00:19', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (415, 21, 100, '替身', 'replacement', 2, 'STRING', '”“', '2020-12-04 18:00:19', '2020-12-04 18:00:19', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (416, 23, 101, '格式', 'pattern', 1, 'STRING', '100', '2020-12-04 18:02:06', '2020-12-04 18:02:06', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (417, 23, 101, '时区', 'timeZone', 2, 'STRING', '8', '2020-12-04 18:02:06', '2020-12-04 18:02:06', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (418, 23, 101, '格式', 'pattern', 1, 'STRING', '100', '2020-12-04 18:03:50', '2020-12-04 18:03:50', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (419, 23, 101, '时区', 'timeZone', 2, 'STRING', 'Asia/Tokyo', '2020-12-04 18:03:50', '2020-12-04 18:03:50', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (420, 23, 101, '格式', 'pattern', 1, 'STRING', '100', '2020-12-05 03:30:05', '2020-12-05 03:30:05', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (421, 23, 101, '时区', 'timeZone', 2, 'STRING', 'Asia/Tokyo1', '2020-12-05 03:30:05', '2020-12-05 03:30:05', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (422, 23, 101, '格式', 'pattern', 1, 'STRING', '100', '2020-12-05 03:30:25', '2020-12-05 03:30:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (423, 23, 101, '时区', 'timeZone', 2, 'STRING', 'Asia/Tokyo', '2020-12-05 03:30:25', '2020-12-05 03:30:25', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (424, 3, 102, '服务器地址', 'mailSmtpHost', 2, 'STRING', '阿达', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (425, 3, 102, '发送人', 'user', 2, 'STRING', '爱仕达多撒', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (426, 3, 102, '发送人密码', 'password', 2, 'STRING', '安顺达萨', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (427, 3, 102, '邮件接收人', 'tos', 0, 'STRING', '74', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (428, 3, 102, '邮件标题', 'title', 2, 'STRING', '恭喜你存款成功', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (429, 3, 102, '邮件内容', 'text', 2, 'STRING', '撒大大', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (430, 3, 102, '端口号', 'mailSmtpPort', 2, 'NUMBER', '21', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (431, 15, 104, '集合', 'list', 0, 'NUMBER', '96', '2020-12-07 16:21:51', '2020-12-07 16:21:51', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (432, 15, 104, '集合', 'list', 0, 'NUMBER', '96', '2020-12-07 16:25:27', '2020-12-07 16:25:27', 1);
INSERT INTO boot_engine.rule_engine_function_value (id, function_id, variable_id, param_name, param_code, type, value_type, value, create_time, update_time, deleted) VALUES (433, 15, 105, '集合', 'list', 0, 'NUMBER', '98', '2020-12-07 16:31:02', '2020-12-07 16:31:02', 1);
create table rule_engine_group_data
(
    id          int auto_increment
        primary key,
    group_id    int       not null,
    data_id     int       not null,
    data_type   tinyint   not null,
    create_time timestamp null,
    update_time timestamp null,
    deleted     tinyint   null
)
    comment '规则用户组与数据权限' charset = utf8;

INSERT INTO boot_engine.rule_engine_group_data (id, group_id, data_id, data_type, create_time, update_time, deleted) VALUES (1, 1, 1, 0, '2020-11-21 02:42:01', '2020-11-21 02:42:03', 0);
create table rule_engine_menu
(
    id          int auto_increment
        primary key,
    name        varchar(20)          not null comment '用户id',
    description varchar(500)         null,
    parent_id   int                  null,
    icon        varchar(100)         null,
    sort        int                  null,
    menu_path   varchar(500)         not null comment '菜单路径',
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 not null
);

INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (9, '规则引擎', null, null, null, 0, '/', '2020-11-21 03:09:56', '2020-11-21 03:09:57', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (10, '首页', null, 9, 'el-icon-s-home', 0, '/home', '2020-11-21 03:17:13', '2020-11-21 03:17:13', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (11, '基础组件', null, 9, 'el-icon-menu', 1, '/', '2020-11-21 03:17:13', '2020-11-21 03:17:13', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (12, '元素', null, 11, null, 0, '/element', '2020-11-21 03:16:24', '2020-11-21 03:16:25', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (13, '变量', null, 11, null, 1, '/variable', '2020-11-21 03:16:26', '2020-11-21 03:16:28', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (14, '函数', null, 11, null, 2, '/function', '2020-11-21 03:16:29', '2020-11-21 03:16:30', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (15, '条件', null, 11, null, 3, '/condition', '2020-11-21 03:16:33', '2020-11-21 03:16:31', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (16, '功能', null, 9, 'el-icon-s-marketing', 2, '/', '2020-11-21 03:17:49', '2020-11-21 03:17:50', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (17, '规则', null, 16, null, 0, '/rule', '2020-11-21 03:18:33', '2020-11-21 03:18:35', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (18, '设置', null, 9, 'el-icon-s-tools', 3, '/', '2020-11-21 03:55:38', '2020-11-21 03:55:38', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (19, '个人设置', null, 18, null, 0, '/personalSettings', '2020-11-21 03:52:51', '2020-11-21 03:52:51', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (20, '系统设置', null, 18, '', 1, '/systemSetting', '2020-11-21 03:55:38', '2020-11-21 03:55:38', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (21, '决策表', null, 16, null, 1, '/decisionTree', '2020-11-21 03:52:32', '2020-11-21 03:52:33', 0);
INSERT INTO boot_engine.rule_engine_menu (id, name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES (22, '评分卡', null, 16, null, 1, '/scoreCard', '2020-11-21 03:53:19', '2020-11-21 03:53:20', 0);
create table rule_engine_role
(
    id          int auto_increment
        primary key,
    name        varchar(50)          not null comment '名称',
    code        varchar(50)          not null,
    description varchar(300)         null comment '描述',
    parent_id   int                  null comment '此角色的父级',
    role_path   varchar(500)         not null comment '角色路径',
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 null
);

INSERT INTO boot_engine.rule_engine_role (id, name, code, description, parent_id, role_path, create_time, update_time, deleted) VALUES (1, '系统管理员', 'admin', null, null, '1', '2020-09-25 22:18:42', '2020-09-25 22:18:42', 0);
INSERT INTO boot_engine.rule_engine_role (id, name, code, description, parent_id, role_path, create_time, update_time, deleted) VALUES (2, '用户', 'user', '', 1, '1@2', '2020-09-25 22:19:30', '2020-09-25 22:19:31', 0);
create table rule_engine_role_menu
(
    id          int auto_increment
        primary key,
    role_id     int                  not null comment '用户id',
    menu_id     varchar(500)         null,
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 not null
);

INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (8, 1, '9', '2020-11-21 03:20:17', '2020-11-21 03:20:18', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (9, 1, '10', '2020-11-21 03:20:22', '2020-11-21 03:20:19', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (10, 1, '11', '2020-11-21 03:20:23', '2020-11-21 03:20:20', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (11, 1, '12', '2020-11-21 03:20:24', '2020-11-21 03:20:34', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (12, 1, '13', '2020-11-21 03:20:26', '2020-11-21 03:20:35', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (13, 1, '14', '2020-11-21 03:20:28', '2020-11-21 03:20:37', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (14, 1, '15', '2020-11-21 03:20:29', '2020-11-21 03:20:39', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (15, 1, '16', '2020-11-21 03:20:30', '2020-11-21 03:20:41', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (16, 1, '17', '2020-11-21 03:20:32', '2020-11-21 03:20:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (17, 1, '18', '2020-11-21 03:55:02', '2020-11-21 03:55:04', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (18, 1, '19', '2020-11-21 03:55:08', '2020-11-21 03:55:05', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (19, 1, '20', '2020-11-21 03:55:09', '2020-11-21 03:55:06', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (20, 1, '21', '2020-11-21 03:55:09', '2020-11-21 03:55:07', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (21, 1, '22', '2020-11-21 03:55:10', '2020-11-21 03:55:07', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (22, 2, '9', '2020-11-21 20:50:38', '2020-11-21 20:50:39', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (23, 2, '10', '2020-11-21 20:50:45', '2020-11-21 20:50:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (24, 2, '11', '2020-11-21 20:50:46', '2020-11-21 20:50:41', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (25, 2, '12', '2020-11-21 20:50:47', '2020-11-21 20:50:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (26, 2, '13', '2020-11-21 20:50:47', '2020-11-21 20:50:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (27, 2, '14', '2020-11-21 20:50:48', '2020-11-21 20:50:43', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (28, 2, '15', '2020-11-21 20:50:49', '2020-11-21 20:50:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (29, 2, '16', '2020-11-21 20:50:49', '2020-11-21 20:50:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (30, 2, '17', '2020-11-21 20:51:39', '2020-11-21 20:51:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (31, 2, '18', '2020-11-21 20:51:38', '2020-11-21 20:51:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (32, 2, '19', '2020-11-21 20:51:37', '2020-11-21 20:51:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (id, role_id, menu_id, create_time, update_time, deleted) VALUES (34, 2, '22', '2020-12-15 22:43:29', '2020-12-15 22:43:30', 0);
create table rule_engine_rule
(
    id                        int auto_increment
        primary key,
    name                      varchar(50)   not null,
    code                      varchar(50)   not null,
    description               varchar(500)  null,
    workspace_id              int           null,
    workspace_code            varchar(20)   null,
    create_user_id            int           null,
    create_user_name          varchar(100)  null,
    status                    tinyint       null,
    action_value              varchar(2000) null,
    action_type               tinyint       null,
    action_value_type         varchar(50)   null,
    enable_default_action     tinyint       null,
    default_action_value      varchar(2000) null,
    default_action_type       tinyint       null,
    default_action_value_type varchar(50)   null,
    abnormal_alarm            json          null,
    create_time               timestamp     null,
    update_time               timestamp     null,
    deleted                   tinyint       null
)
    charset = utf8;

create index rule_engine_rule_code_index
    on rule_engine_rule (code);

create index rule_engine_rule_name_index
    on rule_engine_rule (name);

INSERT INTO boot_engine.rule_engine_rule (id, name, code, description, workspace_id, workspace_code, create_user_id, create_user_name, status, action_value, action_type, action_value_type, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (22, '获取规则执行接口Url', 'getRuleExeInterfaceUrl', null, 1, 'default', null, 'admin', 2, '36', 1, 'STRING', 1, '', 2, 'STRING', '{"email": [""], "enable": false}', '2020-08-25 02:53:32', '2020-09-04 20:50:47', 0);
INSERT INTO boot_engine.rule_engine_rule (id, name, code, description, workspace_id, workspace_code, create_user_id, create_user_name, status, action_value, action_type, action_value_type, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (47, '号码规则测试', 'phoneRuletest', null, 1, 'default', null, 'dqw', 1, 'true', 2, 'BOOLEAN', 0, 'false', 2, 'BOOLEAN', '{"email": ["761945125@qq.com"], "enable": true, "timeOutThreshold": 3000}', '2020-09-10 18:07:05', '2020-11-18 02:10:42', 0);
INSERT INTO boot_engine.rule_engine_rule (id, name, code, description, workspace_id, workspace_code, create_user_id, create_user_name, status, action_value, action_type, action_value_type, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (71, '测试规则', 'testrule', '测试规则', 1, 'default', 2, 'lq', 1, '80', 1, 'BOOLEAN', 0, 'false', 2, 'BOOLEAN', '{"email": ["却无人企鹅无人汽车"], "enable": true, "timeOutThreshold": 3000}', '2020-11-24 17:22:45', '2020-12-12 18:32:50', 0);
INSERT INTO boot_engine.rule_engine_rule (id, name, code, description, workspace_id, workspace_code, create_user_id, create_user_name, status, action_value, action_type, action_value_type, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (77, '存款', 'sadsad', '123', 1, 'default', 2, 'lq', 1, 'true', 2, 'BOOLEAN', 1, null, 2, 'NUMBER', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', '2020-12-07 16:11:56', '2020-12-15 07:50:54', 0);
INSERT INTO boot_engine.rule_engine_rule (id, name, code, description, workspace_id, workspace_code, create_user_id, create_user_name, status, action_value, action_type, action_value_type, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (85, '1', 'xdwade1', '1', 1, 'default', 2, 'lq', 1, 'false', 2, 'BOOLEAN', 0, 'false', 2, 'BOOLEAN', '{"email": ["异常"], "enable": true, "timeOutThreshold": 3000}', '2020-12-15 08:39:49', '2020-12-15 08:39:49', 0);
create table rule_engine_rule_publish
(
    id             int auto_increment
        primary key,
    rule_id        int         not null,
    rule_code      varchar(50) null,
    workspace_id   int         null,
    workspace_code varchar(20) null,
    data           json        null,
    create_time    timestamp   null,
    update_time    timestamp   null,
    deleted        tinyint     null
)
    charset = utf8;

create index rule_engine_rule_publish_rule_code_index
    on rule_engine_rule_publish (rule_code);

create index rule_engine_rule_publish_rule_id_index
    on rule_engine_rule_publish (rule_id);

INSERT INTO boot_engine.rule_engine_rule_publish (id, rule_id, rule_code, workspace_id, workspace_code, data, create_time, update_time, deleted) VALUES (105, 77, 'sadsad', 1, 'default', '{"cn.ruleengine.core.rule.Rule": {"id": 77, "code": "sadsad", "name": "存款", "actionValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}, "description": "123", "workspaceId": 1, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.ConditionGroup": {"id": 1199, "name": null, "orderNo": 1, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 90, "name": "定期存款年限111", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "GE"}, "leftValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "elementId": 98, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "fixedMoney", "elementName": "定期存款年限"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 1}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 89, "name": "最低存款额度大于等于2000", "orderNo": 2, "operator": {"cn.ruleengine.core.condition.Operator": "GE"}, "leftValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "elementId": 95, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "lowestLimit", "elementName": "账户最低存款额度"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 2000}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}]}}}, {"cn.ruleengine.core.condition.ConditionGroup": {"id": 1200, "name": null, "orderNo": 2, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 47, "name": "true", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}]}}}]}}}, "abnormalAlarm": {"cn.ruleengine.core.rule.Rule$AbnormalAlarm": {"email": {"[Ljava.lang.String;": [""]}, "enable": false, "timeOutThreshold": 3000}}, "workspaceCode": "default", "defaultActionValue": null}}', '2020-12-12 06:50:05', '2020-12-12 06:50:05', 0);
INSERT INTO boot_engine.rule_engine_rule_publish (id, rule_id, rule_code, workspace_id, workspace_code, data, create_time, update_time, deleted) VALUES (106, 22, 'getRuleExeInterfaceUrl', 1, 'default', '{"cn.ruleengine.core.rule.Rule": {"id": 22, "code": "getRuleExeInterfaceUrl", "name": "获取规则执行接口Url", "actionValue": {"cn.ruleengine.core.value.Variable": {"dataType": {"cn.ruleengine.core.value.DataType": "STRING"}, "valueType": "cn.ruleengine.core.value.Variable", "variableId": 36, "variableName": "获取规则执行接口地址"}}, "description": null, "workspaceId": 1, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.ArrayList": []}}}, "abnormalAlarm": {"cn.ruleengine.core.rule.Rule$AbnormalAlarm": {"email": {"[Ljava.lang.String;": [""]}, "enable": false, "timeOutThreshold": 3000}}, "workspaceCode": "default", "defaultActionValue": null}}', '2020-12-12 12:39:27', '2020-12-12 12:39:27', 0);
INSERT INTO boot_engine.rule_engine_rule_publish (id, rule_id, rule_code, workspace_id, workspace_code, data, create_time, update_time, deleted) VALUES (108, 71, 'testrule', 1, 'default', '{"cn.ruleengine.core.rule.Rule": {"id": 71, "code": "testrule", "name": "测试规则", "actionValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}, "description": "测试规则", "workspaceId": 1, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.ConditionGroup": {"id": 1203, "name": null, "orderNo": 1, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 80, "name": "测试年龄在18到50 之间", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "GE"}, "leftValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 18}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 50}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 75, "name": "消费金额大于3000", "orderNo": 2, "operator": {"cn.ruleengine.core.condition.Operator": "GT"}, "leftValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "elementId": 68, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "AmountOfConsumption", "elementName": "消费金额"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 3000}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 74, "name": "vip等级大于4以上", "orderNo": 3, "operator": {"cn.ruleengine.core.condition.Operator": "GT"}, "leftValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "elementId": 67, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "vipLevel", "elementName": "vip等级"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 4}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}]}}}, {"cn.ruleengine.core.condition.ConditionGroup": {"id": 1204, "name": null, "orderNo": 2, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 73, "name": "验证是否为手机号码条件", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.Variable": {"dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Variable", "variableId": 79, "variableName": "验证是否为手机号码"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}]}}}]}}}, "abnormalAlarm": {"cn.ruleengine.core.rule.Rule$AbnormalAlarm": {"email": {"[Ljava.lang.String;": ["却无人企鹅无人汽车"]}, "enable": true, "timeOutThreshold": 3000}}, "workspaceCode": "default", "defaultActionValue": {"cn.ruleengine.core.value.Constant": {"value": false, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}', '2020-12-12 18:37:34', '2020-12-12 18:37:34', 0);
INSERT INTO boot_engine.rule_engine_rule_publish (id, rule_id, rule_code, workspace_id, workspace_code, data, create_time, update_time, deleted) VALUES (111, 47, 'phoneRuletest', 1, 'default', '{"cn.ruleengine.core.rule.Rule": {"id": 47, "code": "phoneRuletest", "name": "号码规则测试", "actionValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}, "description": null, "workspaceId": 1, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.ConditionGroup": {"id": 1225, "name": null, "orderNo": 1, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 73, "name": "验证是否为手机号码条件", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.Variable": {"dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Variable", "variableId": 79, "variableName": "验证是否为手机号码"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": true, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 70, "name": "限制号码长度", "orderNo": 2, "operator": {"cn.ruleengine.core.condition.Operator": "LE"}, "leftValue": {"cn.ruleengine.core.value.Variable": {"dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Variable", "variableId": 76, "variableName": "号码长度"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": {"java.math.BigDecimal": 17}, "dataType": {"cn.ruleengine.core.value.DataType": "NUMBER"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 69, "name": "号码开头", "orderNo": 3, "operator": {"cn.ruleengine.core.condition.Operator": "STARTS_WITH"}, "leftValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "STRING"}, "elementId": 77, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "phone", "elementName": "号码"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": "137", "dataType": {"cn.ruleengine.core.value.DataType": "STRING"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}]}}}, {"cn.ruleengine.core.condition.ConditionGroup": {"id": 1226, "name": null, "orderNo": 2, "conditions": {"java.util.ArrayList": [{"cn.ruleengine.core.condition.Condition": {"id": 71, "name": "排除号码在白名单的", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "CONTAIN"}, "leftValue": {"cn.ruleengine.core.value.Variable": {"dataType": {"cn.ruleengine.core.value.DataType": "COLLECTION"}, "valueType": "cn.ruleengine.core.value.Variable", "variableId": 77, "variableName": "全局白名单"}}, "rightValue": {"cn.ruleengine.core.value.Element": {"dataType": {"cn.ruleengine.core.value.DataType": "STRING"}, "elementId": 77, "valueType": "cn.ruleengine.core.value.Element", "elementCode": "phone", "elementName": "号码"}}}}]}}}]}}}, "abnormalAlarm": {"cn.ruleengine.core.rule.Rule$AbnormalAlarm": {"email": {"[Ljava.lang.String;": ["761945125@qq.com"]}, "enable": true, "timeOutThreshold": 3000}}, "workspaceCode": "default", "defaultActionValue": {"cn.ruleengine.core.value.Constant": {"value": false, "dataType": {"cn.ruleengine.core.value.DataType": "BOOLEAN"}, "valueType": "cn.ruleengine.core.value.Constant"}}}}', '2020-12-15 08:09:17', '2020-12-15 08:09:17', 0);
create table rule_engine_system_log
(
    id              int auto_increment comment 'id'
        primary key,
    user_id         int          null,
    description     varchar(300) null,
    ip              varchar(20)  not null comment '请求ip',
    browser         varchar(50)  null comment '浏览器',
    browser_version varchar(50)  null comment '浏览器版本',
    `system`        varchar(50)  not null comment '请求者系统',
    detailed        varchar(300) not null comment '请求者系统详情',
    mobile          tinyint(1)   null comment '是否为移动平台',
    ages            mediumtext   null comment '请求参数',
    request_url     varchar(100) not null comment '请求url',
    end_time        timestamp    null on update CURRENT_TIMESTAMP comment '请求结束时间',
    running_time    bigint(10)   null comment '运行时间',
    return_value    mediumtext   null,
    exception       text         null comment '异常',
    request_id      varchar(200) null,
    create_time     timestamp    null on update CURRENT_TIMESTAMP,
    update_time     timestamp    null on update CURRENT_TIMESTAMP,
    deleted         tinyint(1)   null
);


create table rule_engine_user
(
    id          int auto_increment
        primary key,
    username    varchar(30)  not null,
    password    varchar(50)  not null,
    email       varchar(50)  null,
    phone       bigint(16)   null,
    avatar      varchar(200) null comment '头像',
    sex         varchar(2)   null,
    create_time timestamp    null,
    update_time timestamp    null,
    deleted     tinyint      null
)
    comment '规则引擎用户表' charset = utf8;

INSERT INTO boot_engine.rule_engine_user (id, username, password, email, phone, avatar, sex, create_time, update_time, deleted) VALUES (2, 'lq', '5f329d3ac22671f7b214c461e58c27f3', '761945125@qq.com', null, '/static/avatar.jpg', '女', '2020-09-25 23:05:06', '2020-12-01 16:20:16', 0);
INSERT INTO boot_engine.rule_engine_user (id, username, password, email, phone, avatar, sex, create_time, update_time, deleted) VALUES (3, 'test', '5f329d3ac22671f7b214c461e58c27f3', '5f329d3ac22671f7b214c461e58c27f3', null, '/static/avatar.jpg', '男', '2020-11-22 00:53:08', '2020-11-22 00:53:09', 0);
create table rule_engine_user_role
(
    id          int auto_increment
        primary key,
    user_id     int                  not null comment '用户id',
    role_id     int                  not null comment '角色id',
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 not null
);

INSERT INTO boot_engine.rule_engine_user_role (id, user_id, role_id, create_time, update_time, deleted) VALUES (3, 1, 1, '2020-09-25 22:20:31', '2020-09-25 22:20:32', 0);
INSERT INTO boot_engine.rule_engine_user_role (id, user_id, role_id, create_time, update_time, deleted) VALUES (4, 2, 2, '2020-09-25 23:05:20', '2020-09-25 23:05:21', 0);
INSERT INTO boot_engine.rule_engine_user_role (id, user_id, role_id, create_time, update_time, deleted) VALUES (5, 3, 2, '2020-11-22 00:53:38', '2020-11-22 00:53:39', 0);
create table rule_engine_user_workspace
(
    id           int auto_increment
        primary key,
    user_id      varchar(30) not null,
    workspace_id int         null,
    create_time  timestamp   null,
    update_time  timestamp   null,
    deleted      tinyint     null
)
    comment '用户工作空间' charset = utf8;

INSERT INTO boot_engine.rule_engine_user_workspace (id, user_id, workspace_id, create_time, update_time, deleted) VALUES (2, '2', 2, '2020-11-22 03:53:37', '2020-11-22 03:53:39', 0);
INSERT INTO boot_engine.rule_engine_user_workspace (id, user_id, workspace_id, create_time, update_time, deleted) VALUES (3, '3', 2, '2020-11-22 14:42:50', '2020-11-22 14:42:51', 0);
INSERT INTO boot_engine.rule_engine_user_workspace (id, user_id, workspace_id, create_time, update_time, deleted) VALUES (4, '2', 1, '2020-11-22 14:50:33', '2020-11-22 14:50:34', 0);
create table rule_engine_variable
(
    id               int auto_increment
        primary key,
    name             varchar(50)   not null,
    description      varchar(500)  null,
    value_type       varchar(20)   null,
    workspace_id     int           null,
    create_user_id   int           null,
    create_user_name varchar(50)   null,
    type             tinyint       null,
    value            varchar(2000) null,
    create_time      timestamp     null,
    update_time      timestamp     null,
    deleted          tinyint       null
)
    charset = utf8;

create index rule_engine_variable_name_index
    on rule_engine_variable (name);

INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (34, '是否为空集合', null, 'BOOLEAN', 1, null, null, 3, '2', '2020-08-20 02:05:43', '2020-08-20 02:05:43', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (35, '审批人', null, 'COLLECTION', 1, null, null, 2, '123123,234234', '2020-08-24 22:12:53', '2020-08-28 16:10:07', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (36, '获取规则执行接口地址', null, 'STRING', 1, null, null, 2, 'http://ruleserver.cn/ruleEngine/execute', '2020-08-25 18:53:36', '2020-12-12 12:39:17', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (51, '测试变量', null, 'BOOLEAN', 1, null, null, 3, '1', '2020-08-27 23:27:06', '2020-08-27 23:27:06', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (52, '求绝对值', null, 'NUMBER', 1, null, null, 3, '7', '2020-08-28 14:47:28', '2020-08-28 14:47:28', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (54, '高级配置测试', null, 'NUMBER', 1, null, null, 3, '4', '2020-08-28 15:22:10', '2020-08-28 15:22:10', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (56, '是否开启', null, 'BOOLEAN', 1, null, null, 2, 'true', '2020-08-28 16:10:31', '2020-08-28 16:10:31', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (57, '执行规则', null, 'STRING', 1, null, null, 3, '5', '2020-08-28 21:29:09', '2020-08-28 21:29:09', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (58, '求集合的大小', null, 'NUMBER', 1, null, null, 3, '4', '2020-08-29 01:22:06', '2020-08-29 01:28:39', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (70, '年龄在20-26之间的', null, 'BOOLEAN', 1, null, null, 3, '9', '2020-08-30 18:34:53', '2020-08-30 23:52:20', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (71, '是否为身份证', null, 'BOOLEAN', 1, null, null, 3, '17', '2020-09-01 13:58:26', '2020-09-04 19:38:53', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (73, '集合中最大值测试', null, 'NUMBER', 1, null, null, 3, '11', '2020-09-08 12:19:25', '2020-09-08 12:19:25', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (75, '平均值', null, 'NUMBER', 1, null, null, 3, '10', '2020-09-08 12:25:40', '2020-09-08 12:25:40', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (76, '号码长度', null, 'NUMBER', 1, null, null, 3, '13', '2020-09-10 18:11:06', '2020-09-10 18:11:06', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (77, '全局白名单', null, 'COLLECTION', 1, null, null, 2, '1343493849384,1371728378123,1340000000000', '2020-09-10 18:14:43', '2020-12-11 06:44:37', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (79, '验证是否为手机号码', null, 'BOOLEAN', 1, null, null, 3, '16', '2020-09-16 14:57:18', '2020-09-18 19:08:19', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (80, '发送优惠券', null, 'BOOLEAN', 1, null, null, 3, '3', '2020-11-13 19:22:26', '2020-11-16 23:12:14', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (98, '求和测试', null, 'NUMBER', 1, null, null, 3, '15', '2020-12-01 18:18:31', '2020-12-01 18:20:32', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (102, '发送短信', null, 'BOOLEAN', 1, null, null, 3, '3', '2020-12-07 11:30:51', '2020-12-07 11:30:51', 0);
INSERT INTO boot_engine.rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (103, '赠送积分', null, 'NUMBER', 1, null, null, 2, '123', '2020-12-07 16:14:08', '2020-12-07 16:18:50', 0);
create table rule_engine_workspace
(
    id                int auto_increment
        primary key,
    code              varchar(20)  null,
    name              varchar(30)  not null,
    access_key_id     varchar(100) null,
    access_key_secret varchar(100) null,
    description       varchar(500) null,
    create_time       timestamp    null,
    update_time       timestamp    null,
    deleted           tinyint      null
)
    comment '工作空间' charset = utf8;

INSERT INTO boot_engine.rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (1, 'default', '默认工作空间', 'root', '123456', '默认的', '2020-11-21 02:41:33', '2020-11-21 02:41:34', 0);
INSERT INTO boot_engine.rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (2, 'test', '测试', '', '', '', '2020-11-21 19:36:12', '2020-11-21 19:36:13', 0);
INSERT INTO boot_engine.rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (4, 'prd', '线上', '', '', '请勿随意修改', '2020-11-07 21:49:36', '2020-11-07 21:49:38', 0);