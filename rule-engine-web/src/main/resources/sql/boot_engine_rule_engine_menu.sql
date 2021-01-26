create table rule_engine_menu
(
    id          int auto_increment
        primary key,
    name        varchar(20)          not null comment '用户id',
    description varchar(500)         null,
    parent_id   int                  null,
    icon        varchar(100)         null,
    sort        int                  null,
    menu_path   varchar(500)         not null comment '菜单路径',
    create_time timestamp            null on update CURRENT_TIMESTAMP,
    update_time timestamp            null on update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0 not null
);

INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('规则引擎', null, null, null, 0, '/', '2020-11-21 03:09:56', '2020-11-21 03:09:57', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('首页', null, 9, 'el-icon-s-home', 0, '/home', '2020-11-21 03:17:13', '2020-11-21 03:17:13', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('基础组件', null, 9, 'el-icon-menu', 1, '/', '2020-11-21 03:17:13', '2020-11-21 03:17:13', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('元素', null, 11, null, 0, '/element', '2020-11-21 03:16:24', '2020-11-21 03:16:25', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('变量', null, 11, null, 2, '/variable', '2020-12-28 23:46:45', '2020-12-28 23:46:45', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('函数', null, 11, null, 3, '/function', '2020-12-28 23:46:45', '2020-12-28 23:46:45', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('条件', null, 11, null, 4, '/condition', '2020-12-28 23:46:45', '2020-12-28 23:46:45', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('功能', null, 9, 'el-icon-s-marketing', 2, '/', '2020-11-21 03:17:49', '2020-11-21 03:17:50', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('普通规则', null, 16, null, 0, '/generalRule', '2020-12-29 21:39:59', '2020-12-29 21:39:59', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('设置', null, 9, 'el-icon-s-tools', 4, '/', '2020-12-19 01:30:40', '2020-12-19 01:30:40', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('个人设置', null, 18, null, 0, '/personalSettings', '2020-11-21 03:52:51', '2020-11-21 03:52:51', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('系统设置', null, 18, '', 2, '/systemSetting', '2020-12-28 21:53:17', '2020-12-28 21:53:17', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('决策表', null, 16, null, 2, '/decisionTable', '2020-12-28 21:55:40', '2020-12-28 21:55:40', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('评分卡', null, 16, null, 3, '/scoreCard', '2021-01-14 09:39:48', '2021-01-14 09:39:48', 1);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('权限管理', null, 9, 'el-icon-s-custom', 3, '/', '2020-12-19 01:30:40', '2020-12-19 01:30:40', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('工作空间', null, 23, null, 1, '/workspace', '2020-12-19 01:31:18', '2020-12-19 01:31:20', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('规则集', null, 16, null, 1, '/ruleSet', '2020-12-28 21:53:08', '2020-12-28 21:53:09', 0);
INSERT INTO boot_engine.rule_engine_menu (name, description, parent_id, icon, sort, menu_path, create_time, update_time, deleted) VALUES ('元素组', '一组元素,可以类似一个java pojo', 11, null, 1, '/elementGroup', '2021-01-14 09:39:47', '2021-01-14 09:39:47', 1);