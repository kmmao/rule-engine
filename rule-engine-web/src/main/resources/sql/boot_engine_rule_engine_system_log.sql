create table rule_engine_system_log
(
    id              int auto_increment comment 'id'
        primary key,
    user_id         int          null,
    description     varchar(300) null,
    ip              varchar(20)  not null comment '请求ip',
    browser         varchar(50)  null comment '浏览器',
    browser_version varchar(50)  null comment '浏览器版本',
    `system`        varchar(50)  not null comment '请求者系统',
    detailed        varchar(300) not null comment '请求者系统详情',
    mobile          tinyint(1)   null comment '是否为移动平台',
    ages            mediumtext   null comment '请求参数',
    request_url     varchar(100) not null comment '请求url',
    end_time        timestamp    null on update CURRENT_TIMESTAMP comment '请求结束时间',
    running_time    bigint(10)   null comment '运行时间',
    return_value    mediumtext   null,
    exception       text         null comment '异常',
    request_id      varchar(200) null,
    create_time     timestamp    null on update CURRENT_TIMESTAMP,
    update_time     timestamp    null on update CURRENT_TIMESTAMP,
    deleted         tinyint(1)   null
);
