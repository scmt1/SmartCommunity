package me.zhengjie.common;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import me.zhengjie.common.utils.ResultCode;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常统一处理
 * 
 * @author yongyan.pu
 * @version $Id: RestExceptionHandler.java, v 0.1 2018年12月12日 下午8:54:18 yongyan.pu Exp $
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("", ex);
        return ResultUtil.error(ResultCode.FAILURE);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class,
                        HttpMessageNotReadableException.class,
                        UnsatisfiedServletRequestParameterException.class,
                        MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> badRequestException(Exception ex) {
        log.error("badRequestException", ex);
        return ResultUtil.error(ResultCode.FAILURE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private Result<?> constraintViolationException(ConstraintViolationException ex) {
        log.error("", ex);
        return ResultUtil.error(ResultCode.PARAM_VALID_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException ex) {
        log.error("", ex);
        ResultCode errorCode = ex.getErrorCode();
        return ResultUtil.error(errorCode);
    }
}
