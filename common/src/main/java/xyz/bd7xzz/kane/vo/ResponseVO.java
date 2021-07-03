package xyz.bd7xzz.kane.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseVO<T> {
    @Getter
    @AllArgsConstructor
    private enum ResponseCode {
        SUCCESS(200, "success"),
        INVALID_PARAMS(400, "invalid params"),
        INTERNAL_ERROR(500, "server internal error");
        private int code;
        private String message;
    }

    private String message;
    private int code;
    private T data;

    /**
     * 返回成功并携带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseVO buildSuccess(T data) {
        return ResponseVO.builder().code(ResponseCode.SUCCESS.getCode()).message(ResponseCode.SUCCESS.getMessage()).data(data).build();
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static ResponseVO buildSuccess() {
        return buildSuccess(null);
    }

    /**
     * 返回参数错误并指定错误信息
     *
     * @param message
     * @return
     */
    public static ResponseVO buildParamsError(String message) {
        return ResponseVO.builder().code(ResponseCode.INVALID_PARAMS.getCode()).message(message).data(null).build();
    }

    /**
     * 返回参数错误
     *
     * @return
     */
    public static ResponseVO buildParamsError() {
        return buildParamsError(ResponseCode.INVALID_PARAMS.getMessage());
    }

    /**
     * 返回服务器内部错误
     *
     * @return
     */
    public static ResponseVO buildServerError() {
        return ResponseVO.builder().code(ResponseCode.INTERNAL_ERROR.getCode()).message(ResponseCode.INTERNAL_ERROR.getMessage()).data(null).build();
    }
}
