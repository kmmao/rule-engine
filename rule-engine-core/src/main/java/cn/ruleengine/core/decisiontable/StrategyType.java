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

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 决策表执行策略
 *
 * @author dingqianwen
 * @date 2020/12/19
 * @since 1.0.0
 */
public enum StrategyType {

    /**
     * 返回所有优先级命中的结果
     */
    ALL_PRIORITY(1),
    /**
     * 返回命中的最高优先级第一个结果
     */
    HIGHEST_PRIORITY_SINGLE(2),
    /**
     * 返回命中的最高优先级所有结果
     */
    HIGHEST_PRIORITY_ALL(3);

    @Getter
    private Integer value;

    StrategyType(Integer value) {
        this.value = value;
    }

}
