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

INSERT INTO boot_engine.rule_engine_user_role (user_id, role_id, create_time, update_time, deleted) VALUES (1, 1, '2020-09-25 22:20:31', '2020-09-25 22:20:32', 0);
INSERT INTO boot_engine.rule_engine_user_role (user_id, role_id, create_time, update_time, deleted) VALUES (2, 2, '2020-09-25 23:05:20', '2020-09-25 23:05:21', 0);
INSERT INTO boot_engine.rule_engine_user_role (user_id, role_id, create_time, update_time, deleted) VALUES (3, 2, '2020-11-22 00:53:38', '2020-11-22 00:53:39', 0);