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
);

create index rule_engine_condition_group_condition_condition_group_id_index
    on rule_engine_condition_group_condition (condition_group_id);

INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (103, 1445, 1, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (104, 1446, 1, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (102, 1446, 2, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (101, 1446, 3, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (100, 1446, 4, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (98, 1446, 5, '2021-01-07 05:07:39', '2021-01-07 05:07:39', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (111, 1465, 1, '2021-01-12 03:36:50', '2021-01-12 03:36:50', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (122, 1653, 1, '2021-01-18 12:46:58', '2021-01-18 12:46:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (71, 1790, 1, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (73, 1791, 1, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (70, 1791, 2, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (69, 1791, 3, '2021-01-20 10:03:17', '2021-01-20 10:03:17', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (119, 1869, 2, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (125, 1870, 1, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (47, 1871, 1, '2021-01-23 12:05:35', '2021-01-23 12:05:35', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (117, 1874, 1, '2021-01-23 12:05:58', '2021-01-23 12:05:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (116, 1874, 2, '2021-01-23 12:05:58', '2021-01-23 12:05:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (73, 1880, 1, '2021-01-23 16:00:58', '2021-01-23 16:00:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (69, 1880, 2, '2021-01-23 16:00:58', '2021-01-23 16:00:58', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (124, 1881, 1, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (126, 1881, 2, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (116, 1882, 1, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (117, 1883, 1, '2021-01-25 09:30:44', '2021-01-25 09:30:44', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (126, 1884, 1, '2021-01-25 09:32:16', '2021-01-25 09:32:16', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (47, 1892, 1, '2021-01-25 11:14:34', '2021-01-25 11:14:34', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (121, 1892, 2, '2021-01-25 11:14:34', '2021-01-25 11:14:34', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (119, 1893, 1, '2021-01-25 11:14:34', '2021-01-25 11:14:34', 0);
INSERT INTO boot_engine.rule_engine_condition_group_condition (condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (57, 1894, 1, '2021-01-25 11:14:35', '2021-01-25 11:14:35', 0);