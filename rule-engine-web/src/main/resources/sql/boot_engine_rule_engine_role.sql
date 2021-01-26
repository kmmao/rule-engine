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

INSERT INTO boot_engine.rule_engine_role (name, code, description, parent_id, role_path, create_time, update_time, deleted) VALUES ('系统管理员', 'admin', null, null, '1', '2020-09-25 22:18:42', '2020-09-25 22:18:42', 0);
INSERT INTO boot_engine.rule_engine_role (name, code, description, parent_id, role_path, create_time, update_time, deleted) VALUES ('用户', 'user', '', 1, '1@2', '2020-09-25 22:19:30', '2020-09-25 22:19:31', 0);