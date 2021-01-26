create table rule_engine_decision_table
(
    id               int auto_increment
        primary key,
    name             varchar(50)  not null,
    code             varchar(50)  not null,
    description      varchar(500) null,
    workspace_id     int          null,
    workspace_code   varchar(20)  null,
    table_data       json         null,
    strategy_type    tinyint      null,
    create_user_id   int          null,
    create_user_name varchar(100) null,
    status           tinyint      null,
    reference_data   json         null,
    abnormal_alarm   json         null,
    create_time      timestamp    null,
    update_time      timestamp    null,
    deleted          tinyint      null
);

create index rule_engine_rule_code_workspace_id_index
    on rule_engine_decision_table (workspace_id);

INSERT INTO boot_engine.rule_engine_decision_table (name, code, description, workspace_id, workspace_code, table_data, strategy_type, create_user_id, create_user_name, status, reference_data, abnormal_alarm, create_time, update_time, deleted) VALUES ('test', 'asdf', null, 1, 'default', '{"rows": [{"result": {"type": 2, "value": "true", "valueType": "BOOLEAN"}, "priority": 2, "conditions": [{"type": 2, "value": "true", "valueType": "BOOLEAN"}, {"type": 2, "value": "qwe", "valueType": "STRING"}]}, {"result": {"type": 2, "value": "true", "valueType": "BOOLEAN"}, "priority": 1, "conditions": [{"type": 2, "value": "true", "valueType": "BOOLEAN"}, {}]}, {"result": {"type": 2, "value": "false", "valueType": "BOOLEAN"}, "priority": 1, "conditions": [{"type": 2, "value": "true", "valueType": "BOOLEAN"}, {}]}, {"result": {"type": 2, "value": "true", "valueType": "BOOLEAN"}, "priority": 1, "conditions": [{"type": 1, "value": "118", "valueName": "年龄在7-11之间", "valueType": "BOOLEAN"}, {}]}], "collResultHead": {"type": 2, "valueType": "BOOLEAN", "defaultAction": {"valueType": "COLLECTION", "enableDefaultAction": 1}}, "collConditionHeads": [{"name": "条件", "symbol": "EQ", "leftValue": {"type": 0, "value": "122", "valueName": "布尔元素", "valueType": "BOOLEAN"}}, {"name": "条件", "symbol": "EQ", "leftValue": {"type": 2, "value": "q", "valueType": "STRING"}}]}', 1, 1, 'dqw', 2, '{"elementIds": [122], "variableIds": [118]}', '{"enable": false, "timeOutThreshold": 3000}', '2021-01-02 18:38:25', '2021-01-22 16:51:10', 0);
INSERT INTO boot_engine.rule_engine_decision_table (name, code, description, workspace_id, workspace_code, table_data, strategy_type, create_user_id, create_user_name, status, reference_data, abnormal_alarm, create_time, update_time, deleted) VALUES ('测试决策表', 'test', null, 1, 'default', '{"rows": [{"result": {"type": 2, "value": "1", "valueType": "NUMBER"}, "priority": 1, "conditions": [{"type": 2, "value": "123", "valueType": "STRING"}]}], "collResultHead": {"type": 2, "valueType": "NUMBER", "defaultAction": {}}, "collConditionHeads": [{"name": "条件", "symbol": "STARTS_WITH", "leftValue": {"type": 2, "value": "1234", "valueType": "STRING"}}]}', 1, 1, 'dqw', 1, '{"elementIds": [], "variableIds": [], "conditionIds": []}', null, '2021-01-23 16:04:14', '2021-01-23 16:09:06', 0);
INSERT INTO boot_engine.rule_engine_decision_table (name, code, description, workspace_id, workspace_code, table_data, strategy_type, create_user_id, create_user_name, status, reference_data, abnormal_alarm, create_time, update_time, deleted) VALUES ('测试', 'test_w', null, 1, 'default', '{"rows": [{"result": {}, "priority": 1, "conditions": [{}]}], "collResultHead": {"defaultAction": {}}, "collConditionHeads": [{"name": "条件", "leftValue": {}}]}', 1, 2, 'lq', 0, null, null, '2021-01-25 10:43:02', '2021-01-25 10:43:02', 0);