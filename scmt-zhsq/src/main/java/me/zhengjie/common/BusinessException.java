package me.zhengjie.common;


import me.zhengjie.common.utils.ResultCode;

/**
 * 业务异常
 * 
 * @author yongyan.pu
 * @version $Id: BusinessException.java, v 0.1 2018年12月12日 上午9:48:34 yongyan.pu Exp $
 */
public class BusinessException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1L;

    protected int             code             = 500;

    protected ResultCode errorCode;

    public BusinessException(int code) {
        this.code = code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode errorCode) {
        super(formatMsg(errorCode));
        this.code = errorCode.getCode();
        this.errorCode = errorCode;
    }

    public BusinessException(ResultCode errorCode, Throwable cause) {
        super(formatMsg(errorCode), cause);
        this.code = errorCode.getCode();
        this.errorCode = errorCode;
    }

    public int getCode() {
        return this.code;
    }

    public ResultCode getErrorCode() {
        return this.errorCode;
    }

    public final static String formatMsg(ResultCode errorCode) {
        return String.format("%s-%s", errorCode.getCode(), errorCode.getMessage());
    }
}
