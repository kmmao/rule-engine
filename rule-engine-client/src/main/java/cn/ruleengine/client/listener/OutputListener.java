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
package cn.ruleengine.client.listener;


import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 为了满足一些特殊需求，例如需要一直调用某个规则来判断是否满足条件得到结果，频繁http请求不太好
 * <p>
 * 封装长链接
 * <p>
 * 待开发
 *
 * @author 丁乾文
 * @create 2021/1/15
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OutputListener {

    /**
     * 监听哪个的规则或者决策表code
     * <p>
     * 如果不写规则或者决策表code，默认监听所有的结果消息自行判断
     *
     * @return 规则或者决策表code
     */
    String code() default "";

    // type

}
