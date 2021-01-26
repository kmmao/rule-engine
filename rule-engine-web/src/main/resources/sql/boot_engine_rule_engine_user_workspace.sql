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
    comment '用户工作空间';

INSERT INTO boot_engine.rule_engine_user_workspace (user_id, workspace_id, create_time, update_time, deleted) VALUES ('2', 2, '2020-11-22 03:53:37', '2020-11-22 03:53:39', 1);
INSERT INTO boot_engine.rule_engine_user_workspace (user_id, workspace_id, create_time, update_time, deleted) VALUES ('3', 2, '2020-11-22 14:42:50', '2020-11-22 14:42:51', 0);
INSERT INTO boot_engine.rule_engine_user_workspace (user_id, workspace_id, create_time, update_time, deleted) VALUES ('2', 1, '2020-11-22 14:50:33', '2020-11-22 14:50:34', 0);
INSERT INTO boot_engine.rule_engine_user_workspace (user_id, workspace_id, create_time, update_time, deleted) VALUES ('3', 1, '2020-12-21 17:20:16', '2020-12-21 17:20:18', 0);