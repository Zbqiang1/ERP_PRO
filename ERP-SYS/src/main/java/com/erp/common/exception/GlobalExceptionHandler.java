package com.erp.common.exception;

import com.erp.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * 拦截所有 Controller 抛出的异常，统一返回 Result 格式。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 处理业务异常 */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常 [{}]：{}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /** 处理参数校验异常 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        log.warn("参数校验失败：{}", message);
        return Result.invalidParam(message);
    }

    /** 处理非法参数异常 */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数：{}", e.getMessage());
        return Result.invalidParam(e.getMessage());
    }

    /** 处理认证异常 */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证失败：{}", e.getMessage());
        return Result.unauthorized();
    }

    /** 处理权限异常 */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足：{}", e.getMessage());
        return Result.forbidden();
    }

    /** 处理其他未知异常 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.fail("系统繁忙，请稍后再试");
    }
}
