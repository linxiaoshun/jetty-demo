package cn.wolfcode.jetty.dto;

import cn.wolfcode.jetty.constants.RespConstants;

import java.io.Serializable;

/**
 * @Title: ResponseDto
 * @Description: 响应数据对象
 * @author leo
 * @date Created in 2017/12/22 下午2:15
 * @version v1.0
 */
public class ResponseDto implements Serializable {

    /**
     * 默认成功请求信息
     */
    public static final String DEFAULT_SUCCESS_MSG = "请求成功";
    /**
     * 默认失败请求信息
     */
    public static final String DEFAULT_FAILED_MSG = "请求失败";
    /**
     * 默认成功请求状态
     */
    public static final Integer DEFAULT_SUCCESS_CODE = RespConstants.HTTP_RESP_STATUS_SUCCESS;
    /**
     * 默认失败请求状态
     */
    public static final Integer DEFAULT_FAILED_CODE = RespConstants.HTTP_RESP_STATUS_FAILED;

    private String msg;
    private Integer code;
    private Object data;

    public ResponseDto() {
        this(null);
    }

    public ResponseDto(Object data) {
        this(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
    }

    public ResponseDto(Integer code, String msg) {
        this(code, msg, null);
    }

    public ResponseDto(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
