package com.erp.common.exception;

/**
 * 业务异常类
 *
 * 用于在 Service 层抛出可预见的业务逻辑异常，
 * 由全局异常处理器 GlobalExceptionHandler 统一拦截处理。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final Integer code;

    /**
     * 构造方法（默认错误码 500）
     *
     * @param message 错误提示
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造方法（自定义错误码）
     *
     * @param code    错误码
     * @param message 错误提示
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
