create table rule_engine_role_menu
(
    id          int auto_increment
        primary key,
    role_id     int                  not null comment '用户id',
    menu_id     varchar(500)         null,
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 not null
);

INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '9', '2020-11-21 03:20:17', '2020-11-21 03:20:18', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '10', '2020-11-21 03:20:22', '2020-11-21 03:20:19', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '11', '2020-11-21 03:20:23', '2020-11-21 03:20:20', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '12', '2020-11-21 03:20:24', '2020-11-21 03:20:34', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '13', '2020-11-21 03:20:26', '2020-11-21 03:20:35', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '14', '2020-11-21 03:20:28', '2020-11-21 03:20:37', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '15', '2020-11-21 03:20:29', '2020-11-21 03:20:39', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '16', '2020-11-21 03:20:30', '2020-11-21 03:20:41', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '17', '2020-11-21 03:20:32', '2020-11-21 03:20:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '18', '2020-11-21 03:55:02', '2020-11-21 03:55:04', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '19', '2020-11-21 03:55:08', '2020-11-21 03:55:05', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '20', '2020-11-21 03:55:09', '2020-11-21 03:55:06', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '21', '2020-11-21 03:55:09', '2020-11-21 03:55:07', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '9', '2020-11-21 20:50:38', '2020-11-21 20:50:39', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '10', '2020-11-21 20:50:45', '2020-11-21 20:50:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '11', '2020-11-21 20:50:46', '2020-11-21 20:50:41', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '12', '2020-11-21 20:50:47', '2020-11-21 20:50:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '13', '2020-11-21 20:50:47', '2020-11-21 20:50:42', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '14', '2020-11-21 20:50:48', '2020-11-21 20:50:43', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '15', '2020-11-21 20:50:49', '2020-11-21 20:50:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '16', '2020-11-21 20:50:49', '2020-11-21 20:50:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '17', '2020-11-21 20:51:39', '2020-11-21 20:51:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '18', '2020-11-21 20:51:38', '2020-11-21 20:51:40', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '19', '2020-11-21 20:51:37', '2020-11-21 20:51:44', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '23', '2020-12-19 01:29:48', '2020-12-19 01:29:49', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '24', '2020-12-19 01:31:32', '2020-12-19 01:31:34', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '23', '2020-12-19 01:53:04', '2020-12-19 01:53:05', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '24', '2020-12-19 01:53:14', '2020-12-19 01:53:16', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '21', '2020-12-19 15:56:26', '2020-12-19 15:56:28', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (1, '25', '2020-12-28 21:53:35', '2020-12-28 21:53:36', 0);
INSERT INTO boot_engine.rule_engine_role_menu (role_id, menu_id, create_time, update_time, deleted) VALUES (2, '25', '2020-12-28 21:53:37', '2020-12-28 21:53:37', 0);