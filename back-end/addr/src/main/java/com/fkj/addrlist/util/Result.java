package com.fkj.addrlist.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public Result(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /* 构造返回成功消息体，无data */
    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    /* 构造返回成功消息体 */
    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    /* 构造返回失败消息体，无data */
    public static Result error(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }
    /* 构造返回失败消息体 */
    public static Result error(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    private void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }
}
