package cn.ruleengine.web.controller.exception;

import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.exception.DataPermissionException;
import cn.ruleengine.web.exception.NoLoginException;
import cn.ruleengine.web.interceptor.MDCLogInterceptor;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.core.exception.ConditionException;
import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.exception.FunctionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈统一异常处理〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    /**
     * Exception级别未捕获异常发送邮箱警告
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = Exception.class)
    public BaseResult exception(Exception e) {
        BaseResult result = BaseResult.err();
        log.error("Exception", e);
        // 抛出的未知异常 加上RequestId
        result.setMessage(ErrorCodeEnum.RULE500.getMsg().concat(MDCLogInterceptor.getRequestId()));
        result.setCode(ErrorCodeEnum.RULE500.getCode());
        return result;
    }


    /**
     * 规则引擎异常
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = EngineException.class)
    public BaseResult engineException(EngineException e) {
        BaseResult result = BaseResult.err();
        log.warn("EngineException", e);
        result.setMessage(e.getMessage());
        result.setCode(ErrorCodeEnum.RULE8900.getCode());
        return result;
    }

    /**
     * 无数据权限
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = DataPermissionException.class)
    public BaseResult dataPermissionException(DataPermissionException e) {
        BaseResult result = BaseResult.err();
        log.warn("DataPermissionException", e);
        result.setMessage(e.getMessage());
        result.setCode(ErrorCodeEnum.RULE8930.getCode());
        return result;
    }

    /**
     * 条件配置异常
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = ConditionException.class)
    public BaseResult conditionException(ConditionException e) {
        BaseResult result = BaseResult.err();
        log.warn("ConditionException", e);
        result.setMessage(e.getMessage());
        result.setCode(ErrorCodeEnum.RULE8920.getCode());
        return result;
    }

    /**
     * 规则函数异常
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = FunctionException.class)
    public BaseResult functionException(FunctionException e) {
        BaseResult result = BaseResult.err();
        log.warn("FunctionException", e);
        result.setMessage(Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse(e.getMessage()));
        result.setCode(ErrorCodeEnum.RULE8910.getCode());
        return result;
    }

    /**
     * 资源未找到异常
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public BaseResult noHandlerFoundException() {
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE9999404.getMsg());
        result.setCode(ErrorCodeEnum.RULE9999404.getCode());
        return result;
    }

    /**
     * 请求方法不匹配
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResult httpRequestMethodNotSupportedException() {
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE9999405.getMsg());
        result.setCode(ErrorCodeEnum.RULE9999405.getCode());
        return result;
    }

    /**
     * 验证异常
     * 需要把异常信息显示到页面的情况
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = ValidationException.class)
    public BaseResult validationException(ValidationException e) {
        log.warn("ValidationException", e);
        BaseResult result = BaseResult.err();
        result.setMessage(e.getMessage());
        result.setCode(ErrorCodeEnum.RULE99990100.getCode());
        return result;
    }

    /**
     * apiException
     * 说明:{@link ApiException#ApiException(String, Object...)}
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = ApiException.class)
    public BaseResult apiException(ApiException e) {
        log.warn("ApiException", e);
        BaseResult result = BaseResult.err();
        result.setMessage(e.getMessage());
        result.setCode(e.getCode());
        return result;
    }

    /**
     * 非法或不适当的参数
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public BaseResult illegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException", e);
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE99990100.getMsg());
        result.setCode(ErrorCodeEnum.RULE99990100.getCode());
        return result;
    }

    /**
     * bindException
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = BindException.class)
    public BaseResult bindException(BindException e) {
        log.warn("BindException", e);
        BaseResult result = BaseResult.err();
        FieldError error = e.getFieldError();
        result.setMessage(Optional.ofNullable(error).map(FieldError::getDefaultMessage).orElse(ErrorCodeEnum.RULE99990100.getMsg()));
        result.setCode(ErrorCodeEnum.RULE99990100.getCode());
        return result;
    }

    /**
     * 不支持的内容类型
     * 例如:Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public BaseResult httpMediaTypeNotSupportedException() {
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE99990001.getMsg());
        result.setCode(ErrorCodeEnum.RULE99990001.getCode());
        return result;
    }

    /**
     * 方法参数无效
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException", e);
        BaseResult result = BaseResult.err();
        BindingResult bindingResult = e.getBindingResult();
        FieldError error = bindingResult.getFieldError();
        result.setMessage(Optional.ofNullable(error).map(DefaultMessageSourceResolvable::getDefaultMessage).orElse(ErrorCodeEnum.RULE99990002.getMsg()));
        result.setCode(ErrorCodeEnum.RULE99990002.getCode());
        return result;
    }

    /**
     * 缺少所需的请求正文
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public BaseResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException", e);
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE10010003.getMsg());
        result.setCode(ErrorCodeEnum.RULE10010003.getCode());
        return result;
    }

    /**
     * 参数异常
     * 配合@Validated使用
     *
     * @return BaseResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public BaseResult constraintViolationException(ConstraintViolationException e) {
        log.warn("ConstraintViolationException", e);
        BaseResult result = BaseResult.err();
        List<ConstraintViolation<?>> arrayList = new ArrayList<>(e.getConstraintViolations());
        ConstraintViolation<?> constraintViolation = arrayList.get(0);
        result.setMessage(constraintViolation.getMessage());
        result.setCode(ErrorCodeEnum.RULE99990100.getCode());
        return result;
    }


    /**
     * 未登录
     *
     * @param e e
     * @return BaseResult
     */
    @ExceptionHandler(value = NoLoginException.class)
    public BaseResult noLoginException(NoLoginException e) {
        log.warn("NoLoginException", e);
        BaseResult result = BaseResult.err();
        result.setMessage(ErrorCodeEnum.RULE4009.getMsg());
        result.setCode(ErrorCodeEnum.RULE4009.getCode());
        return result;
    }
}
