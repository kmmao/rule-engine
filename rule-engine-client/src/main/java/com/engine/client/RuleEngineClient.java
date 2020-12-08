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
package com.engine.client;


import javax.annotation.Resource;

import com.engine.client.exception.ValidException;
import com.engine.client.model.RuleModel;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
public class RuleEngineClient {

    @Resource
    private RuleEngineProperties ruleEngineProperties;
    @Resource
    private RestTemplate restTemplate;

    public RuleEngineClient() {

    }

    public OutPut exe(String ruleCode, Map<String, Object> input) {
        // TODO: 2020/12/6 ...
        return null;
    }

    /**
     * @param model 规则调用模型
     * @return 规则结果
     * @see RuleModel
     */
    public OutPut exe(@NonNull Object model) {
        Objects.requireNonNull(model);
        if (model.getClass().isAnnotationPresent(RuleModel.class)) {
            throw new ValidException("{}找不到RuleModel注解", model.getClass());
        }
        // TODO: 2020/12/9
        return null;
    }

}
