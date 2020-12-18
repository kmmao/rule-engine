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
