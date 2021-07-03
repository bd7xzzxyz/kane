package xyz.bd7xzz.kane.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.bd7xzz.kane.vo.ResponseVO;

@ControllerAdvice
@Slf4j
public class ExceptionInterceptor {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseVO error(Exception ex) {
        log.error("error!", ex);
        return ResponseVO.buildServerError();
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseVO paramsError(IllegalArgumentException ex) {
        if (StringUtils.isEmpty(ex.getMessage())) {
            return ResponseVO.buildParamsError();
        }
        return ResponseVO.buildParamsError(ex.getMessage());
    }
}
