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
    comment '规则引擎用户表';

INSERT INTO boot_engine.rule_engine_user (username, password, email, phone, avatar, sex, create_time, update_time, deleted) VALUES ('lq', '5f329d3ac22671f7b214c461e58c27f3', '761945125@qq.com', 1021231, 'http://oss-boot-test.oss-cn-beijing.aliyuncs.com/ruleengine/99B65385-970A-479E-A5FF-2C423BC3D37A.jpeg?Expires=33146728805&OSSAccessKeyId=LTAIyEa5SulNXbQa&Signature=M4AT5btBHLJZ5cDbJhq0rE96%2B3o%3D', '女', '2020-09-25 23:05:06', '2021-01-22 09:47:31', 0);
INSERT INTO boot_engine.rule_engine_user (username, password, email, phone, avatar, sex, create_time, update_time, deleted) VALUES ('test', '5f329d3ac22671f7b214c461e58c27f3', '5f329d3ac22671f7b214c461e58c27f3', null, '/static/avatar.jpg', '男', '2020-11-22 00:53:08', '2020-11-22 00:53:09', 0);
