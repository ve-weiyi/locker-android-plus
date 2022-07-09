package com.ve.lib.common.exception;


/**
 * 业务异常
 *
 * @author weiyi
 * @date 2021/07/27
 */

public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = StatusCodeEnum.FAIL.getCode();

    /**
     * 错误信息
     */
    private String message ="系统错误";

    public BizException(String message) {
        this.message = message;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }

    public BizException (String message, Throwable cause){
        this.message = message;
    }
}
