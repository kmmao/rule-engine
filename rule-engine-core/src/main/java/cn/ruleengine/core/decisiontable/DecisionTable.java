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
import cn.ruleengine.core.DataSupport;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.decisiontable.strategey.DecisionTableStrategy;
import cn.ruleengine.core.decisiontable.strategey.DecisionTableStrategyFactory;
import cn.ruleengine.core.exception.DecisionTableException;
import cn.ruleengine.core.JsonParse;
import cn.ruleengine.core.value.Value;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
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
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DecisionTable extends DataSupport implements JsonParse {

    /**
     * 执行策略
     * <p>
     * 默认：返回所有优先级所有结果
     */
    private DecisionTableStrategyType strategyType = DecisionTableStrategyType.ALL_PRIORITY;

    /**
     * coll头
     */
    private List<CollHead> collHeads;

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

    /**
     * 规则运行发生异常，邮件接收人
     **/
    @JsonIgnore
    private AbnormalAlarm abnormalAlarm = new AbnormalAlarm();

    /**
     * 决策表列头
     *
     * @param collHead 列头
     */
    public void addCollHead(CollHead collHead) {
        if (collHeads == null) {
            this.collHeads = new ArrayList<>();
        }
        this.collHeads.add(collHead);
    }

    /**
     * 添加一行规则
     *
     * @param row 决策表一行数据，相当于一个规则
     */
    public void addRow(Row row) {
        if (this.collHeads == null) {
            throw new DecisionTableException("请先初始化决策表表头");
        }
        // 防止数据错乱，造成数据结果计算错误 初始化决策表时校验
        if (!Objects.equals(row.getColls().size(), this.collHeads.size())) {
            throw new DecisionTableException("配置错误，左条件数量:{}，右值条件数量:{}", this.collHeads.size(), row.getColls().size());
        }
        Integer priority = row.getPriority();
        if (this.decisionTree.containsKey(priority)) {
            this.decisionTree.get(priority).add(row);
        } else {
            List<Row> rows = new ArrayList<>();
            rows.add(row);
            this.decisionTree.put(priority, rows);
        }
    }

    /**
     * 执行决策表
     *
     * @param input         决策表输入参数
     * @param configuration 引擎配置信息
     * @return 决策表执行结果
     */
    @Override
    @Nullable
    public List<Object> execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        long startTime = System.currentTimeMillis();
        try {
            // 获取执行策略执行决策表
            DecisionTableStrategy strategy = DecisionTableStrategyFactory.getInstance(this.strategyType);
            // 计算表头值，获取到表头比较器，与下面单元格比较
            Map<Integer, CollHead.Comparator> collHeadCompareMap = this.getCollHeadCompare(input, configuration);
            List<Object> actions = strategy.compute(collHeadCompareMap, this.decisionTree, input, configuration);
            // 如果匹配到数据则返回
            if (actions != null) {
                return actions;
            }
            if (Objects.nonNull(this.getDefaultActionValue())) {
                log.debug("结果未命中，存在默认结果，返回默认结果");
                return Collections.singletonList(this.getDefaultActionValue().getValue(input, configuration));
            }
            log.debug("结果未命中，不存在默认结果，返回:null");
            return null;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
                log.debug("决策表计算耗时:{}ms", cost);
            }
            if (cost >= this.getAbnormalAlarm().getTimeOutThreshold()) {
                log.warn("警告：决策表执行超过最大阈值，请检查决策表配置，决策表Code:{}", this.getCode());
            }
        }
    }

    /**
     * 计算表头值，获取到表头比较器，与下面单元格比较
     *
     * @param input         决策表输入参数
     * @param configuration 引擎配置信息
     * @return Map<Integer, CollHeadCompare>
     */
    private Map<Integer, CollHead.Comparator> getCollHeadCompare(Input input, RuleEngineConfiguration configuration) {
        Map<Integer, CollHead.Comparator> collHeadCompareMap = new LinkedHashMap<>();
        for (int index = 0; index < this.collHeads.size(); index++) {
            CollHead collHead = this.collHeads.get(index);
            Object value = collHead.getLeftValue(input, configuration);
            CollHead.Comparator collHeadCompare = new CollHead.Comparator();
            collHeadCompare.setLeftValue(collHead.getLeftValue());
            collHeadCompare.setOperator(collHead.getOperator());
            collHeadCompare.setValue(value);
            collHeadCompareMap.put(index, collHeadCompare);
        }
        return collHeadCompareMap;
    }

    @SneakyThrows
    public static DecisionTable buildDecisionTable(@NonNull String jsonString) {
        return OBJECT_MAPPER.readValue(jsonString, DecisionTable.class);
    }

    @Override
    public void fromJson(String jsonString) {
        DecisionTable decisionTable = DecisionTable.buildDecisionTable(jsonString);
        this.setId(decisionTable.getId());
        this.setCode(decisionTable.getCode());
        this.setName(decisionTable.getName());
        this.setDescription(decisionTable.getDescription());
        this.setWorkspaceId(decisionTable.getWorkspaceId());
        this.setWorkspaceCode(decisionTable.getWorkspaceCode());
        this.setStrategyType(decisionTable.getStrategyType());
        this.setCollHeads(decisionTable.getCollHeads());
        this.setDecisionTree(decisionTable.getDecisionTree());
        this.setDefaultActionValue(decisionTable.getDefaultActionValue());
    }


}
