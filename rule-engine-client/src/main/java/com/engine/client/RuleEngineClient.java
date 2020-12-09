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

    /**
     * 调用规则引擎中的规则
     *
     * @param ruleCode 规则code
     * @param input 规则参数
     * @return 规则结果
     */
    public OutPut exe(@NonNull String ruleCode, @NonNull Map<String, Object> input) {
        Objects.requireNonNull(ruleCode);
        Objects.requireNonNull(input);
        Map<String, Object> param = new HashMap<>(5);
        param.put("ruleCode", ruleCode);
        param.put("workspaceCode", this.ruleEngineProperties.getWorkspaceCode());
        param.put("accessKeyId", this.ruleEngineProperties.getAccessKeyId());
        param.put("accessKeySecret", this.ruleEngineProperties.getAccessKeySecret());
        param.put("param", input);
        return this.restTemplate.postForObject(this.ruleEngineProperties.getUrl(), param, OutPut.class);
    }

    /**
     * 根据规则模型解析调用引擎中的规则
     *
     * @param model 规则调用模型
     * @return 规则结果
     * @see RuleModel
     */
    public OutPut exe(@NonNull Object model) {
        Objects.requireNonNull(model);
        if (model.getClass().isAnnotationPresent(RuleModel.class)) {
            throw new ValidException("{}找不到RuleModel注解", model.getClass());
        }
        String ruleCode = null;
        Map<String, Object> input = new HashMap<>();
        // TODO: 2020/12/9
        return this.exe(ruleCode, input);
    }
    
    /**
     * 引擎中是否存在此规则
     *
     * @param ruleCode 规则code
     * @return true 存在
     */
    public boolean isExists(String ruleCode) {
        // TODO: 2020/12/9
        return true;
    }

}
