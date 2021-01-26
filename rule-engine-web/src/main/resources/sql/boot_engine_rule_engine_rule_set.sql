create table rule_engine_rule_set
(
    id                  int auto_increment
        primary key,
    name                varchar(50)  null,
    code                varchar(50)  null,
    description         varchar(500) null,
    workspace_id        int          null,
    workspace_code      varchar(20)  null,
    status              tinyint      null,
    strategy_type       tinyint      null,
    create_user_id      int          null,
    create_user_name    varchar(100) null,
    enable_default_rule bigint       null,
    default_rule_id     int          null,
    reference_data      json         null,
    create_time         timestamp    null,
    update_time         timestamp    null,
    deleted             tinyint      null
);

INSERT INTO boot_engine.rule_engine_rule_set (name, code, description, workspace_id, workspace_code, status, strategy_type, create_user_id, create_user_name, enable_default_rule, default_rule_id, reference_data, create_time, update_time, deleted) VALUES ('刚开发完毕（欢迎测试）', 'test', null, 1, 'default', 1, 1, 1, 'dqw', 0, 558, '{"elementIds": [136, 137], "variableIds": [112, 35], "conditionIds": [119, 121, 57, 47]}', '2021-01-16 15:55:14', '2021-01-25 11:14:35', 0);
INSERT INTO boot_engine.rule_engine_rule_set (name, code, description, workspace_id, workspace_code, status, strategy_type, create_user_id, create_user_name, enable_default_rule, default_rule_id, reference_data, create_time, update_time, deleted) VALUES ('两个阻车器联动规则', 'linked2StopperRule', '两个阻车器的联动规则', 1, 'default', 1, 1, 2, 'lq', null, 345, null, '2021-01-18 08:58:37', '2021-01-18 12:46:59', 0);
INSERT INTO boot_engine.rule_engine_rule_set (name, code, description, workspace_id, workspace_code, status, strategy_type, create_user_id, create_user_name, enable_default_rule, default_rule_id, reference_data, create_time, update_time, deleted) VALUES ('测试规则集', 'TEST001', null, 1, 'default', 2, 1, 2, 'lq', 0, 529, '{"elementIds": [136, 60, 142], "variableIds": [], "conditionIds": [119, 125, 47]}', '2021-01-19 08:02:01', '2021-01-23 12:05:35', 0);