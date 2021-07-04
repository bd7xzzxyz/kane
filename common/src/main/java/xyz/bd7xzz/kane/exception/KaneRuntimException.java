package xyz.bd7xzz.kane.exception;

/**
 * @author bd7xzz
 * @version 1.0
 * @description: 运行时异常
 * @date 7/4/21 8:24 PM
 */
public class KaneRuntimException extends RuntimeException {
    private String message;

    public KaneRuntimException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
