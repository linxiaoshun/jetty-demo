package cn.wolfcode.jetty.web.controller.base;

import cn.wolfcode.jetty.dto.ResponseDto;

/**
 * @Title: BaseController
 * @Description: Controller 公共基类
 * @author leo
 * @date Created in 2017/12/22 下午2:18
 * @version v1.0
 */
public class BaseController {

    protected ResponseDto failed() {
        return failed(ResponseDto.DEFAULT_FAILED_CODE);
    }

    protected ResponseDto failed(Integer code) {
        return failed(code, ResponseDto.DEFAULT_FAILED_MSG);
    }

    protected ResponseDto failed(String msg) {
        return failed(ResponseDto.DEFAULT_FAILED_CODE, msg);
    }

    protected ResponseDto failed(Integer code, String msg) {
        return failed(code, msg, null);
    }

    protected ResponseDto failed(Integer code, String msg, Object data) {
        return new ResponseDto(code, msg, data);
    }

    protected ResponseDto success() {
        return success(null);
    }

    protected ResponseDto success(Object data) {
        return success(ResponseDto.DEFAULT_SUCCESS_MSG, data);
    }

    protected ResponseDto success(String msg, Object data) {
        return success(ResponseDto.DEFAULT_SUCCESS_CODE, msg, data);
    }

    protected ResponseDto success(Integer code, String msg, Object data) {
        return new ResponseDto(code, msg, data);
    }
}
