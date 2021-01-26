create table rule_engine_general_rule
(
    id                        int auto_increment
        primary key,
    name                      varchar(50)   not null,
    code                      varchar(50)   not null,
    rule_id                   int           null,
    description               varchar(500)  null,
    workspace_id              int           null,
    workspace_code            varchar(20)   null,
    create_user_id            int           null,
    create_user_name          varchar(100)  null,
    status                    tinyint       null,
    enable_default_action     tinyint       null,
    default_action_value      varchar(2000) null,
    default_action_type       tinyint       null,
    default_action_value_type varchar(50)   null,
    abnormal_alarm            json          null,
    reference_data            json          null,
    create_time               timestamp     null,
    update_time               timestamp     null,
    deleted                   tinyint       null
);

create index rule_engine_rule_code_workspace_id_index
    on rule_engine_general_rule (code, workspace_id);

INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('篮球苗子', 'bs', 121, '是否为篮球苗子测试', 1, 'default', 1, 'dqw', 1, 0, '不是', 2, 'STRING', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', null, '2020-12-29 03:07:07', '2020-12-29 16:20:53', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('手机号规则测试', 'phoneRuletest', 122, null, 1, 'default', 1, 'dqw', 1, 0, 'false', 2, 'BOOLEAN', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', null, '2020-12-29 03:12:45', '2021-01-18 08:48:49', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('kevin', 'A001', 145, null, 2, 'test', 2, 'lq', 2, 0, '8', 2, 'NUMBER', '{"email": ["test-a001"], "enable": true, "timeOutThreshold": 3000}', null, '2021-01-12 03:33:11', '2021-01-12 03:33:11', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('测试2', 'A002', 146, null, 2, 'test', 2, 'lq', 0, null, null, null, null, null, null, '2021-01-12 03:38:59', '2021-01-12 03:38:59', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('事件判断', 'event', 150, null, 1, 'default', 2, 'lq', 2, 0, '103', 1, 'NUMBER', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', null, '2021-01-13 09:26:13', '2021-01-13 10:41:24', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('手机号码不为空且是137开头的', 'TELNOTNAULLANDBEG134', 157, null, 1, 'default', 2, 'lq', 1, 0, 'false', 2, 'BOOLEAN', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', '{"elementIds": [77], "variableIds": [79], "conditionIds": [69, 73]}', '2021-01-15 01:08:29', '2021-01-23 16:00:56', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('关键事件', 'events_impo', 346, null, 1, 'default', 2, 'lq', 1, 0, 'false', 2, 'BOOLEAN', '{"email": [""], "enable": false, "timeOutThreshold": 3000}', '{"elementIds": [145, 130], "variableIds": [144], "conditionIds": [116, 117, 124, 126]}', '2021-01-19 01:19:59', '2021-01-19 01:19:59', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('规则222', 'rule2021', 549, null, 1, 'default', 2, 'lq', 1, null, null, null, null, null, '{"elementIds": [145], "variableIds": [], "conditionIds": [126]}', '2021-01-25 09:31:07', '2021-01-25 09:31:07', 0);
INSERT INTO boot_engine.rule_engine_general_rule (name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, reference_data, create_time, update_time, deleted) VALUES ('f', 'f', 559, null, 1, 'default', 2, 'lq', 0, null, null, null, null, null, null, '2021-01-25 12:56:44', '2021-01-25 12:56:44', 0);