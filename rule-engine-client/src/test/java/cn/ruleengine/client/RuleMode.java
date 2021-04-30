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
package cn.ruleengine.client;

import cn.ruleengine.client.model.BatchSymbol;
import cn.ruleengine.client.model.ElementField;
import cn.ruleengine.client.model.Model;
import lombok.Data;

import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@Data
@Model(code = "phoneRuletest")
public class RuleMode {


    @ElementField(code = "phone")
    private String phone;

    /**
     * 批量时对数据做个区分
     */
    @BatchSymbol
    private String uuid = UUID.randomUUID().toString();

}
