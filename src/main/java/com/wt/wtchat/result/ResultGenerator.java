package com.wt.wtchat.result;

/**
 * 返回数据封装
 *
 * @author Zed
 */
public class ResultGenerator {

    public static Result successResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(ResultCode.SUCCESS.desc());
    }

    public static Result successResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(ResultCode.SUCCESS.desc())
                .setData(data);
    }

    public static Result failResult() {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(ResultCode.FAIL.desc());
    }

    public static Result failResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result unauthorizedResult(String message) {
        return new Result()
                .setCode(ResultCode.UNAUTHORIZED)
                .setMessage(message);
    }
}
