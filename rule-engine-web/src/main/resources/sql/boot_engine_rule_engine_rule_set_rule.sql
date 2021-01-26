create table rule_engine_rule_set_rule
(
    id          int auto_increment
        primary key,
    rule_set_id int       null,
    rule_id     int       null,
    order_no    int       null,
    create_time timestamp null,
    update_time timestamp null,
    deleted     tinyint   null
);

INSERT INTO boot_engine.rule_engine_rule_set_rule (rule_set_id, rule_id, order_no, create_time, update_time, deleted) VALUES (160, 344, 1, '2021-01-18 12:46:59', '2021-01-18 12:46:59', 0);
INSERT INTO boot_engine.rule_engine_rule_set_rule (rule_set_id, rule_id, order_no, create_time, update_time, deleted) VALUES (164, 527, 1, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_rule_set_rule (rule_set_id, rule_id, order_no, create_time, update_time, deleted) VALUES (164, 528, 2, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_rule_set_rule (rule_set_id, rule_id, order_no, create_time, update_time, deleted) VALUES (154, 556, 1, '2021-01-25 11:14:35', '2021-01-25 11:14:35', 0);
INSERT INTO boot_engine.rule_engine_rule_set_rule (rule_set_id, rule_id, order_no, create_time, update_time, deleted) VALUES (154, 557, 2, '2021-01-25 11:14:35', '2021-01-25 11:14:35', 0);