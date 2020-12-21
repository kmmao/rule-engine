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

import cn.ruleengine.core.decisiontable.Coll;
import cn.ruleengine.core.decisiontable.CollHeadCompare;
import cn.ruleengine.core.decisiontable.Row;
import cn.ruleengine.core.exception.DecisionException;
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
public class HighestPrioritySingleStrategy implements Strategy {

    private static HighestPrioritySingleStrategy highestPrioritySingleStrategy = new HighestPrioritySingleStrategy();

    public static HighestPrioritySingleStrategy getInstance() {
        return highestPrioritySingleStrategy;
    }

    /**
     * 先从高优先级规则执行，返回命中的最高优先级所有结果
     *
     * @param collHeadCompareMap 表头比较器
     * @param decisionTree       决策树
     * @return 命中的结果值
     */
    @Override
    public List<Value> compute(Map<Integer, CollHeadCompare> collHeadCompareMap, Map<Integer, List<Row>> decisionTree) {
        for (Map.Entry<Integer, List<Row>> tree : decisionTree.entrySet()) {
            Integer priority = tree.getKey();
            log.info("开始执行优先级规则：{}", priority);
            List<Row> rows = tree.getValue();
            // 一个row可以看做一个规则
            for (Row row : rows) {
                List<Coll> colls = row.getColls();
                // 这里的检查应该在配置时就需要校验，防止数据错乱，造成数据结果计算错误
                if (!Objects.equals(collHeadCompareMap.size(), colls.size())) {
                    throw new DecisionException("配置错误，左条件数量:{}，右值条件数量:{}", collHeadCompareMap.size(), colls.size());
                }
                for (int i = 0; i < colls.size(); i++) {
                    Coll coll = colls.get(i);
                    if (coll == null) {
                        continue;
                    }
                    // 获取到表头比较器，与下面单元格比较
                    CollHeadCompare collHeadCompare = collHeadCompareMap.get(i);
                    if (collHeadCompare.compare(coll.getRightValue())) {
                        // 最高优先级返回一个结果
                        return Collections.singletonList(row.getAction());
                    }
                }
            }
        }
        return Collections.emptyList();
    }

}
