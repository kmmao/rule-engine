# 📌 规则引擎 RuleEngine 📌
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub Stars](https://img.shields.io/github/stars/DingQianWen/rule-engine)](https://github.com/DingQianWen/rule-engine/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/DingQianWen/rule-engine)](https://github.com/DingQianWen/rule-engine/fork)
[![GitHub issues](https://img.shields.io/github/issues/DingQianWen/rule-engine.svg)](https://github.com/DingQianWen/rule-engine/issues)
[![Percentage of issues still open](http://isitmaintained.com/badge/open/DingQianWen/rule-engine.svg)](https://github.com/DingQianWen/rule-engine/issues "Percentage of issues still open")

#### 业务逻辑实现不再依赖于代码开发，可零代码实现复杂业务逻辑。

前端代码地址：https://github.com/DingQianWen/rule-engine-front  
项目展示地址：http://ruleengine.cn/   
申请登录权限联系QQ：761945125，Email：761945125@qq.com

### 技术文档
简单使用：https://blog.csdn.net/weixin_42756198/article/details/109680143  

### 技术栈/版本介绍：
- 所涉及的相关的技术有：
    - SpringBoot 2.1.2
    - RabbitMQ
    - Redis、Redisson
    - MySQL 5.7.9
    - Mybatis-plus
    - Mybatis-plus-generator
    - Maven 3.3
    - Swagger  

### 规则如何调用
规则通过在 http://ruleengine.cn 配置完成后，就可以调用接口来执行引擎中的规则了  
```http
POST http://ruleserver.cn/ruleEngine/execute
Content-Type: application/json

{
      "ruleCode": "phoneRuletest",
      "workspaceCode": "default",
      "accessKeyId": "略", 
      "accessKeySecret": "略",
      "param": {
            "phone": "13400000000"
      }
}
```

现在我们让此使用方式更加简单易用！
调用规则方项目pom.xml文件引入以下依赖
```pom
    <dependency>
        <groupId>cn.ruleengine</groupId>
        <artifactId>rule-engine-client</artifactId>
        <version>1.5</version>
    </dependency>
```
然后编写如下代码进行测试：  
```java
@EnableRuleEngine
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTest {

    @Resource
    private RuleEngineClient ruleEngineClient;

    @Test
    public void test() {
        // 构建规则请求参数
        PhoneTestRule phoneTestRule = new PhoneTestRule();
        phoneTestRule.setPhone("134000000000");
        // 调用执行引擎中的规则
        OutPut outPut = this.ruleEngineClient.execute(phoneTestRule);
        System.out.println(outPut);
    }

}

@Data
@RuleModel(ruleCode = "phoneRuletest")
public class PhoneTestRule {

    /**
     * ElementField可选，默认code为属性name
     */
    @ElementField(code = "phone")
    private String phone;

}
```
现在你就已经学会了如何使用，更多使用方式敬请期待我们将文档补全！


### 下一步进展
 - 规则版本（开发中）  
 - 规则监控（待开发）  
 - 评分卡（待开发）  
 - 决策树（待开发）   
 - 决策表（待开发）   

目前忙于工作，功能待完善，欢迎有兴趣伙伴加入我们！
 
### 如果觉得本项目对您有任何一点帮助，请点右上角 "Star" 支持一下， 并向您的基友、同事们宣传一下吧，谢谢！

