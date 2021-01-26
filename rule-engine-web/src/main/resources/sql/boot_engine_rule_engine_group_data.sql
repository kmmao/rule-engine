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
    comment '规则用户组与数据权限';

INSERT INTO boot_engine.rule_engine_group_data (group_id, data_id, data_type, create_time, update_time, deleted) VALUES (1, 1, 0, '2020-11-21 02:42:01', '2020-11-21 02:42:03', 0);