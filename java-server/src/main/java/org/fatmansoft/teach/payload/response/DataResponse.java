package org.fatmansoft.teach.payload.response;

import lombok.Getter;

import java.util.Map;

/**
 * DataResponse 前端HTTP请求返回数据对象
 * Integer code 返回代码 0 正确返回 1 错误返回信息
 * Object data 返回数据对象
 * String msg 返回正确错误信息
 */
@Getter
public class DataResponse {
    private Integer code;
    private Object data;
    private String msg;
    public DataResponse(){

    }

    public DataResponse(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
