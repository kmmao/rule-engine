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
    comment '工作空间';

INSERT INTO boot_engine.rule_engine_workspace (code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES ('default', '默认工作空间', 'root', '123456', '默认的', '2020-11-21 02:41:33', '2020-11-21 02:41:34', 0);
INSERT INTO boot_engine.rule_engine_workspace (code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES ('test', '测试', 'gdfhdgfh', 'sdfasdfas', '供测试使用', '2020-11-21 19:36:12', '2020-11-21 19:36:13', 0);
INSERT INTO boot_engine.rule_engine_workspace (code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES ('prd', '线上', 'asdfasdf', 'asasdfasdfas', '请勿随意修改', '2020-11-07 21:49:36', '2020-11-07 21:49:38', 0);