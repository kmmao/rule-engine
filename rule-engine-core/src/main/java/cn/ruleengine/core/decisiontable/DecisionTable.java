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
package cn.ruleengine.core.decisiontable;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.decisiontable.strategey.Strategy;
import cn.ruleengine.core.decisiontable.strategey.StrategyFactory;
import cn.ruleengine.core.value.Value;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Slf4j
@Data
public class DecisionTable {

    /**
     * 规则id
     */
    private Integer id;

    /**
     * 规则Code
     */
    private String code;
    /**
     * 规则名称
     */
    private String name;

    private String description;

    /**
     * 工作空间
     */
    private Integer workspaceId;

    /**
     * 工作空间code
     */
    private String workspaceCode;

    /**
     * 执行策略
     * <p>
     * 默认：返回命中的最高优先级所有结果
     */
    private StrategyType strategyType = StrategyType.HIGHEST_PRIORITY_ALL;

    /**
     * coll头
     */
    private List<CollHead> collHeads = new ArrayList<>();

    /**
     * 决策树，key为同一个优先级，value为此优先级下所有的行
     * <p>
     * 优先级越小越先执行
     */
    private Map<Integer, List<Row>> decisionTree = new TreeMap<>();

    /**
     * 决策表默认值
     */
    private Value defaultActionValue;

    public void addCollHead(CollHead collHead) {
        this.collHeads.add(collHead);
    }

    public void addRow(Row row) {
        Integer priority = row.getPriority();
        if (this.decisionTree.containsKey(priority)) {
            this.decisionTree.get(priority).add(row);
        } else {
            List<Row> rows = new ArrayList<>();
            rows.add(row);
            this.decisionTree.put(priority, rows);
        }
    }

    @Nullable
    public List<Value> execute(@NonNull Input input, @NonNull Configuration configuration) {
        // 获取执行策略执行决策表
        Strategy strategy = StrategyFactory.getInstance(this.strategyType);
        // 计算表头值，获取到表头比较器，与下面单元格比较
        Map<Integer, CollHeadCompare> collHeadCompareMap = new LinkedHashMap<>();
        for (int index = 0; index < this.collHeads.size(); index++) {
            CollHead collHead = this.collHeads.get(index);
            Object value = collHead.getLeftValue(input, configuration);
            CollHeadCompare collHeadCompare = new CollHeadCompare();
            collHeadCompare.setLeftValue(collHead.getLeftValue());
            collHeadCompare.setOperator(collHead.getOperator());
            collHeadCompare.setValue(value);
            collHeadCompareMap.put(index, collHeadCompare);
        }
        List<Value> actions = strategy.compute(collHeadCompareMap, this.decisionTree);
        if (CollUtil.isNotEmpty(actions)) {
            return actions;
        }
        if (Objects.nonNull(this.defaultActionValue)) {
            log.info("结果未命中，存在默认结果，返回默认结果");
            return Collections.singletonList(this.defaultActionValue);
        }
        log.info("结果未命中，不存在默认结果，返回:null");
        return null;
    }

}
