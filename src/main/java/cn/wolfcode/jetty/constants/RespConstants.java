package cn.wolfcode.jetty.constants;

import org.springframework.http.HttpStatus;

/**
 * @Title: RespConstants
 * @Description: 响应信息常量
 * @author leo
 * @date Created in 2017/12/20 下午2:09
 * @version v1.0
 */
public interface RespConstants {

    /**
     * 请求成功状态
     */
    int HTTP_RESP_STATUS_SUCCESS = HttpStatus.OK.value();

    /**
     * 请求失败状态
     */
    int HTTP_RESP_STATUS_FAILED = HttpStatus.BAD_REQUEST.value();

    /**
     * 服务器错误状态
     */
    int HTTP_RESP_STATUS_SERVER_ERR = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
