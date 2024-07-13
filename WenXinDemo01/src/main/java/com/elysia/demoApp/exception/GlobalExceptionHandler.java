package com.elysia.demoApp.exception;

import com.elysia.demoApp.model.result.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常集中处理，将我们的异常信息传递给前端处理
 * @ClassName GlobalExceptionHandler
 * @author   cxb
 * @date  2023/2/11 23:59
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(WenXinException.class)
    @ResponseBody
    public Result wenxin_error(WenXinException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}
