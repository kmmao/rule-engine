package cn.ruleengine.web.enums;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/22
 * @since 1.0.0
 */
public class Permission {

    public enum DataType {
        /**
         * 数据类型
         */
        ELEMENT, ELEMENT_GROUP, VARIABLE, FUNCTION, CONDITION, GENERAL_RULE, RULE_SET, DECISION_TABLE;
    }

    public enum OperationType {
        /**
         * 权限操作类型
         */
        ADD, DELETE, UPDATE, VALID_WORKSPACE, ADD_OR_UPDATE, SELECT
    }

}
