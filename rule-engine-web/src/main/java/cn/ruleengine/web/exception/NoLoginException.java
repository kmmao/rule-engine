package cn.ruleengine.web.exception;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/31
 * @since 1.0.0
 */
public class NoLoginException extends RuntimeException {

    private static final long serialVersionUID = -494678828864504957L;

    public NoLoginException() {
        super();
    }

    public NoLoginException(String message) {
        super(message);
    }

}
