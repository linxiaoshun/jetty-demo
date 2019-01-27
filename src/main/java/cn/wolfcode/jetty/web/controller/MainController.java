package cn.wolfcode.jetty.web.controller;

import cn.wolfcode.jetty.dto.ResponseDto;
import cn.wolfcode.jetty.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: MainController
 * @Description: 主接口类
 * @author leo
 * @date Created in 2017/12/22 下午2:19
 * @version v1.0
 */
@Controller
public class MainController extends BaseController {

    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/test")
    @ResponseBody
    public ResponseDto test(String msg) {
        return success("接收参数：" + msg);
    }
}
