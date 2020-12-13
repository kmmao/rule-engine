package cn.ruleengine.web.function;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.FailureStrategy;
import cn.ruleengine.core.annotation.Function;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 手机归属地
 *
 * @author 丁乾文
 * @create 2020/12/13
 * @since 1.0.0
 */
@Slf4j
@Function
public class MobilePhoneProvinceFunction {

    @Resource
    private RestTemplate restTemplate;

    @Executor
    public String executor(String phone) {
        if (!Validator.isMobile(phone)) {
            return StringPool.EMPTY;
        }
        String requestUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + phone;
        String result = this.restTemplate.getForObject(requestUrl, String.class);
        log.info("获取手机号码归属地：{}", result);
        if (result == null || result.length() == 0) {
            return StringPool.EMPTY;
        }
        // remove __GetZoneResult_ =
        String resultJson = result.substring(18, result.length() - 1);
        System.out.println(resultJson);
        JSONObject parse = JSON.parseObject(resultJson);
        System.out.println(parse);
        return parse.getString("province");
    }

    @FailureStrategy
    public String failureStrategy(String phone) {
        log.warn("获取手机号码归属地出错：" + phone);
        return StringPool.EMPTY;
    }


}
