package cn.ruleengine.core.decisiontable;

import cn.ruleengine.core.value.Value;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 一个row可以看做一个规则
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Data
public class Row {

    /**
     * 决策表行优先级 越小越先执行
     */
    private Integer priority;

    /**
     * 决策表列，为条件 条件与条件之间并且关系
     */
    private List<Coll> colls = new ArrayList<>();

    /**
     * 结果
     */
    private Value action;


    public void addColl(Coll coll) {
        this.colls.add(coll);
    }

}
