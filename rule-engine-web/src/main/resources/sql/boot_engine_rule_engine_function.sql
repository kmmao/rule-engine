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
);

INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('是否为邮箱', '是否为邮箱函数', 'isEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-16 13:00:43', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('是否为空集合', '是否为空集合函数', 'isEmptyCollectionFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-19 18:54:10', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('发送邮件', '发送邮件函数', 'sendEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-18 17:06:45', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('求集合大小', null, 'collectionSizeFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:39:39', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('是否为空字符串', '', 'isEmptyFunction', 'BOOLEAN', '2020-11-15 00:28:25', '2020-08-28 14:43:52', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('求绝对值', null, 'mathAbsFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:45:04', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('返回集合中第几个元素', '不存在则返回null', 'getCollectionElementsFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-30 02:05:37', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('在..之间', '', 'isBetweenFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-30 02:16:51', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('求平均值', '集合中必须为number类型的值', 'avgFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:41:44', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('集合中最大值', '集合中必须为number类型的值', 'collectionMaxFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:48:32', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('集合中最小值', '集合中必须为number类型的值', 'collectionMinFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:49:12', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字符串的长度', '', 'stringLengthFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:50:13', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字符串去除前后空格', null, 'stringTrimFunction', 'STRING', '2020-09-11 20:26:14', '2020-09-01 13:51:14', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('求和', '集合中必须为number类型的值', 'sumFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:52:08', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('验证是否为手机号码', null, 'isMobileFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:53:17', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('是否为身份证', null, 'isCitizenIdFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:54:34', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字符串转为集合', null, 'stringToCollectionFunction', 'COLLECTION', '2020-09-11 20:26:14', '2020-09-01 14:33:48', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字符串替换', null, 'stringReplaceFunction', 'STRING', '2020-11-18 23:50:09', '2020-11-18 23:50:10', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('集合去除重复', null, 'collectionDeduplicationFunction', 'COLLECTION', '2020-11-19 00:00:03', '2020-11-19 00:00:05', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('获取当前时间', '格式例如：yyyy-MM-dd HH:mm:ss;时区例如：Asia/Shanghai;', 'currentDateFunction', 'STRING', '2021-01-07 14:11:21', '2020-11-19 00:37:34', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('获取手机号码所在省份', null, 'mobilePhoneProvinceFunction', 'STRING', '2020-12-13 13:28:58', '2020-12-13 13:28:59', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('获取JSON中指定的值返回STRING类型', '例如：JOSN数据为:{"name":"abc"},获取name的值通过JSON值路径配置为$.name,更多使用方法待文档补全。', 'parseJsonStringFunction', 'STRING', '2020-12-13 13:56:04', '2020-12-13 13:50:52', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('获取JSON中指定的值返回NUMBER类型', '例如：JOSN数据为:{"age":"18"},获取name的值通过JSON值路径配置为$.age,更多使用方法待文档补全。', 'parseJsonNumberFunction', 'NUMBER', '2020-12-13 14:08:28', '2020-12-13 14:08:29', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字母转小写', null, 'letterToLowerCaseFunction', 'STRING', '2020-12-24 00:16:07', '2020-12-24 00:16:08', 0);
INSERT INTO boot_engine.rule_engine_function (name, description, executor, return_value_type, create_time, update_time, deleted) VALUES ('字母转大写', null, 'letterToUpperCaseFunction', 'STRING', '2020-12-24 00:16:38', '2020-12-24 00:16:39', 0);