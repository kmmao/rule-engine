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
);

create index rule_engine_condition_group_rule_id_index
    on rule_engine_condition_group (rule_id);

INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('是否为体育生条件组', 121, 1, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 121, 2, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 145, 1, '2021-01-12 03:36:50', '2021-01-12 03:36:50', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 344, 1, '2021-01-18 12:46:58', '2021-01-18 12:46:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 122, 1, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 122, 2, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 527, 1, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 527, 2, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 528, 1, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 150, 1, '2021-01-23 12:05:58', '2021-01-23 12:05:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 157, 1, '2021-01-23 16:00:58', '2021-01-23 16:00:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 346, 1, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 346, 2, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 346, 3, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 549, 1, '2021-01-25 09:32:16', '2021-01-25 09:32:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 549, 2, '2021-01-25 09:32:16', '2021-01-25 09:32:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 556, 1, '2021-01-25 11:14:34', '2021-01-25 11:14:34', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组3', 556, 2, '2021-01-25 11:14:34', '2021-01-25 11:14:34', 0);
INSERT INTO boot_engine.rule_engine_condition_group (name, rule_id, order_no, create_time, update_time, deleted) VALUES ('条件组', 557, 1, '2021-01-25 11:14:35', '2021-01-25 11:14:35', 0);