/**
 * Copyright @2020 dingqianwen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.engine.web.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈org.apache.ibatis.logging.stdout.StdOutImpl〉
 *
 * @author 丁乾文
 * @create 2019/9/20
 * @since 1.0.0
 */
@Slf4j
public class SqlOutConfig implements Log {
    private static final ThreadLocal<SqlOutBean> SQL_OUT_BEAN = new InheritableThreadLocal<>();
    private static final String PREPARING_SQL = "==>  Preparing: ";
    private static final String PARAMETERS_SQL = "==> Parameters: ";
    private static final String COLUMNS_NAME = "<==    Columns: ";
    private static final String ROW_SQL = "<==        Row: ";
    private static final String TOTAL_SQL = "<==      Total: ";
    private static final String UPDATES_SQL = "<==    Updates: ";

    public SqlOutConfig(String clazz) {
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(s, e);
        SQL_OUT_BEAN.remove();
    }

    @Override
    public void error(String s) {
        log.error(s);
        SQL_OUT_BEAN.remove();
    }

    @Override
    public void debug(String s) {
        if (s.startsWith(PREPARING_SQL)) {
            SqlOutBean sqlOutBean = new SqlOutBean();
            sqlOutBean.setSql(s.substring(16));
            sqlOutBean.setStartTime(new Date());
            SQL_OUT_BEAN.set(sqlOutBean);
        } else if (s.startsWith(PARAMETERS_SQL)) {
            SqlOutBean sqlOutBean = SQL_OUT_BEAN.get();
            sqlOutBean.setParameters(s.substring(16));
        } else if (s.startsWith(TOTAL_SQL) || s.startsWith(UPDATES_SQL)) {
            SqlOutBean sqlOutBean = SQL_OUT_BEAN.get();
            sqlOutBean.setIsSelect(!s.startsWith(UPDATES_SQL));
            sqlOutBean.setTotal(Long.valueOf(s.substring(16)));
            sqlOutBean.setRunTime(System.currentTimeMillis() - sqlOutBean.getStartTime().getTime());
            printSql(sqlOutBean);
            SQL_OUT_BEAN.remove();
        } else {
            log.debug(s);
        }
    }

    @Override
    public void trace(String s) {
        if (s.startsWith(ROW_SQL)) {
            SqlOutBean sqlOutBean = SQL_OUT_BEAN.get();
            List<String> resultRows = sqlOutBean.getResultRows();
            resultRows.add(s.substring(16));
        } else if (s.startsWith(COLUMNS_NAME)) {
            SqlOutBean sqlOutBean = SQL_OUT_BEAN.get();
            sqlOutBean.setColumnName(s.substring(16));
        } else {
            log.trace(s);
        }
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    private void printSql(SqlOutBean sqlOutBean) {
        List<String> resultRows = sqlOutBean.getResultRows();
        StringBuilder sb = new StringBuilder("\n");
        sb.append("┏━━━━━━━━SQL日志━━━━━━━━\n");
        sb.append("┣ SQL: ").append(sqlOutBean.getSql()).append("\n");
        sb.append("┣ 参数: ").append(sqlOutBean.getParameters()).append("\n");
        if (sqlOutBean.getIsSelect()) {
            if (CollUtil.isNotEmpty(resultRows)) {
                sb.append("┣ 列名: ").append(sqlOutBean.getColumnName()).append("\n");
                sb.append("┣ 结果: ").append(resultRows.get(0)).append("\n");
                for (int i = 1; i < resultRows.size(); i++) {
                    sb.append("┃      ").append(resultRows.get(i)).append("\n");
                }
            }
            sb.append("┣ 数量: ").append(sqlOutBean.getTotal()).append("\n");
        } else {
            sb.append("┣ 影响: ").append(sqlOutBean.getTotal()).append("\n");
        }
        sb.append("┣ 时间: ").append(sqlOutBean.getRunTime()).append("ms\n");
        sb.append("┗━━━━━━━━━━━━━━━━━━━━━━");
        log.info("{}", sb);
    }

    @Data
    private static class SqlOutBean {
        private String sql;
        private String parameters;
        private String columnName;
        private List<String> resultRows = new LinkedList<>();
        private Long total;
        private Boolean isSelect;
        private Date startTime;
        private Long runTime;
    }
}
