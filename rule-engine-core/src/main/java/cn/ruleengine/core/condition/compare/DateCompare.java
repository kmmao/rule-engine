/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
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
package cn.ruleengine.core.condition.compare;

import cn.hutool.core.util.NumberUtil;
import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ConditionException;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/2/18
 * @since 1.0.0
 */
public class DateCompare implements Compare {

    /**
     * 支持以下格式日期
     */
    public static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    private DateCompare() {
    }

    private static final DateCompare DATE_COMPARE = new DateCompare();

    public static DateCompare getInstance() {
        return DATE_COMPARE;
    }

    @Override
    public boolean compare(Object leftValue, Operator operator, Object rightValue) {
        if (leftValue == null || rightValue == null) {
            return false;
        }
        Date lValue = this.convertDate(leftValue);
        Date rValue = this.convertDate(rightValue);
        int compare = lValue.compareTo(rValue);
        switch (operator) {
            case EQ:
                return compare == 0;
            case GT:
                return compare > 0;
            case NE:
                return compare != 0;
            case LT:
                return compare < 0;
            case GE:
                return compare == 0 || compare > 0;
            case LE:
                return compare == 0 || compare < 0;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
    }

    /**
     * 转换日期对象
     * <p>
     * 比较器兼容valueObject instanceof Number与valueObject instanceof String && NumberUtil.isNumber((String) valueObject)
     *
     * @param valueObject 兼容日期对象以及时间戳
     * @return Date
     */
    private Date convertDate(Object valueObject) {
        // 一般都是Date类型
        if (valueObject instanceof Date) {
            return (Date) valueObject;
        }
        // 比较器兼容处理时间戳格式
        String valueStr = String.valueOf(valueObject);
        if (valueObject instanceof Number || NumberUtil.isNumber(valueStr)) {
            return new Date(Long.parseLong(valueStr));
        } else {
            throw new ConditionException("左值/右值必须是DATE或者时间戳");
        }
    }

}
