package com.mashu.assetpilot.common;
//这个类是我们自己的业务异常，用来替代在 Service 里直接 return Result.error(...)。它的好处是：
//
//Service 只管抛异常，不用管 HTTP 返回
//异常会被 GlobalExceptionHandler 自动捕获并包装成 Result
//代码分层更清晰

// 一句话：把“业务上不允许”的情况包装成一个异常对象，然后带着 code 和 msg 往上抛。
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(String msg) {
        super(msg);
        this.code = 500; // 这样 id 不存在时直接返回 500 错误，语义更清晰
    }

    public BusinessException (Integer code, String msg) {
        super(msg);
        this.code = code;
    }

}
