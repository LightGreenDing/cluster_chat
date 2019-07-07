package com.wt.wtchat.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应结果封装
 * @author Zed
 */
public class Result {
    private int code;
    private String message;
    private Object data;

    Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

     Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

     Result setData(Object data) {
        this.data = data;
        return this;
    }


    public Map<String,Object> toMap() {
        Map<String,Object> data=new HashMap<>();
        data.put("code",this.code);
        data.put("message",this.message);
        data.put("data",this.data);
        return data;
    }
}
