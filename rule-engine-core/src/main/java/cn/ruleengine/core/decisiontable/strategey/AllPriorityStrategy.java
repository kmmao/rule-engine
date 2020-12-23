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
package cn.ruleengine.core.decisiontable.strategey;

import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.decisiontable.CollHeadCompare;
import cn.ruleengine.core.decisiontable.Row;
import cn.ruleengine.core.value.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/19
 * @since 1.0.0
 */
@Slf4j
public class AllPriorityStrategy implements Strategy {

    private static AllPriorityStrategy allPriorityStrategy = new AllPriorityStrategy();

    private AllPriorityStrategy() {
    }

    public static AllPriorityStrategy getInstance() {
        return allPriorityStrategy;
    }

    /**
     * 先从高优先级规则执行，返回命中的最高优先级所有结果
     *
     * @param collHeadCompareMap 表头比较器
     * @param decisionTree       决策树
     * @param configuration      规则引擎配置信息
     * @return 命中的结果值
     */
    @Override
    public List<Value> compute(Map<Integer, CollHeadCompare> collHeadCompareMap, Map<Integer, List<Row>> decisionTree, Configuration configuration) {
        List<Value> actions = new ArrayList<>();
        for (Map.Entry<Integer, List<Row>> tree : decisionTree.entrySet()) {
            List<Row> rows = tree.getValue();
            // 一个row可以看做一个规则
            for (Row row : rows) {
                Value action = this.getActionByRow(collHeadCompareMap, row, configuration);
                Optional.ofNullable(action).ifPresent(p -> {
                    actions.add(action);
                });
            }
        }
        return actions;
    }

}
