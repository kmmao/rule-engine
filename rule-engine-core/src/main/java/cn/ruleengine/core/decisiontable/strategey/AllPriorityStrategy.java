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

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.decisiontable.CollHead;
import cn.ruleengine.core.decisiontable.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

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
public class AllPriorityStrategy implements DecisionTableStrategy {

    private static final AllPriorityStrategy INSTANCE = new AllPriorityStrategy();

    private AllPriorityStrategy() {
    }

    public static AllPriorityStrategy getInstance() {
        return INSTANCE;
    }

    /**
     * 先从高优先级规则执行，返回所有优先级命中结果
     *
     * @param collHeadCompareMap 表头比较器
     * @param decisionTree       决策树
     * @param input              决策表输入参数
     * @param configuration      规则引擎配置信息
     * @return 命中的结果值
     */
    @Override
    public List<Object> compute(@NonNull Map<Integer, CollHead.Comparator> collHeadCompareMap, @NonNull Map<Integer, List<Row>> decisionTree, @NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        List<Object> actions = null;
        for (Map.Entry<Integer, List<Row>> tree : decisionTree.entrySet()) {
            // 查询此优先级内所有结果
            List<Object> priorityAllAction = this.getAllActionByPriority(collHeadCompareMap, input, configuration, tree.getValue());
            if (priorityAllAction == null) {
                continue;
            }
            // 如果查询到结果
            if (actions == null) {
                actions = priorityAllAction;
            } else {
                actions.addAll(priorityAllAction);
            }
        }
        return actions;
    }

}
