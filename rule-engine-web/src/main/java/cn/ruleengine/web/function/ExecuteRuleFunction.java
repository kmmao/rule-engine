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
package cn.ruleengine.web.function;

import com.alibaba.fastjson.JSON;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.OutPut;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.FailureStrategy;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;
import cn.ruleengine.core.exception.FunctionException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 执行规则函数，当满足指定条件时可以触发此规则执行所需规则
 *
 * @author dingqianwen
 * @date 2020/8/18
 * @since 1.0.0
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Function
public class ExecuteRuleFunction {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 执行规则函数方法
     *
     * @param info 例如：{"param":{"ele":"123"},"ruleCode":"test"}
     * @return 规则执行结果
     */
    @Executor
    public Object executor(@Param String info) {
        log.info("规则执行函数入参：{}", info);
        ExecuteRule executeRule = JSON.parseObject(info, ExecuteRule.class);
        if (executeRule == null) {
            throw new FunctionException("规则执行函数入参错误");
        }
        String ruleCode = executeRule.getRuleCode();
        String workspaceCode = executeRule.getWorkspaceCode();
        Engine engine = applicationContext.getBean(Engine.class);
        if (!engine.isExistsRule(workspaceCode, ruleCode)) {
            throw new FunctionException("规则在引擎中不存在：{}:{}", workspaceCode, ruleCode);
        }
        // 规则入参
        Map<String, Object> inputParam = executeRule.getParam();
        Input input = new DefaultInput();
        inputParam.forEach(input::put);
        // 执行规则
        OutPut execute = engine.execute(input, workspaceCode, ruleCode);
        return execute.getValue();
    }

    @FailureStrategy
    public Object failureStrategy() {
        return null;
    }

    @Data
    public static class ExecuteRule {

        private String workspaceCode;

        /**
         * 执行的规则code
         */
        @NotBlank
        private String ruleCode;

        /**
         * 调用规则入参
         */
        private Map<String, Object> param = new HashMap<>();

    }

}
