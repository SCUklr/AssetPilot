package com.mashu.assetpilot.common;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 这是全局异常处理中心，用 @RestControllerAdvice 标记。
// 它的作用是：任何 Controller 抛出的异常，都会先到这里，由这里决定返回什么 JSON。
// 负责把异常翻译成 Result<T> 返回给前端
@RestControllerAdvice // 全局统一捕获所有 Controller 抛出的异常
public class GlobalExceptionHandler {

    // 处理参数校验失败（@Valid 校验 @RequestBody）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        return Result.error(400, msg);
    }

    // 处理自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    // 兜底：处理其他所有异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.error(500, e.getMessage());
    }
}
