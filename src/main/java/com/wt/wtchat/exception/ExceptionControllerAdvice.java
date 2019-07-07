package com.wt.wtchat.exception;

import com.wt.wtchat.result.Result;
import com.wt.wtchat.result.ResultGenerator;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常抓取处理
 *
 * @author Zed
 */
@ControllerAdvice
public class ExceptionControllerAdvice {


    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @param ex 错误异常
     * @return 封装异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception ex) {
        //发生异常进行日志记录，写入数据库或者其他处理，此处省略
        return ResultGenerator.failResult(ex.getMessage());
    }

}
