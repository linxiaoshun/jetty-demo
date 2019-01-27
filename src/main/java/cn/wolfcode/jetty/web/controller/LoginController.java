package cn.wolfcode.jetty.web.controller;

import cn.wolfcode.jetty.agent.DefaultPushAgent;
import cn.wolfcode.jetty.cache.PushAgentManager;
import cn.wolfcode.jetty.cache.TokenManager;
import cn.wolfcode.jetty.dto.ResponseDto;
import cn.wolfcode.jetty.vo.UserVo;
import cn.wolfcode.jetty.web.controller.base.BaseController;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author leo
 * @version v1.0
 * @Title: LoginController
 * @Description: 登录控制器
 * @date Created in 2017/12/22 下午2:22
 */
@RestController
public class LoginController extends BaseController {

    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "admin";

    @RequestMapping("/login")
    public ResponseDto login(UserVo user) {
        // 检查是否可以登录
        if (check(user.getUsername(), user.getPassword())) {
            // 生成 token
            String token = UUID.randomUUID().toString();
            // 保存 token 信息
            TokenManager.ME.put(token, user);
            // 注册消息监听器
            PushAgentManager.ME.registry(token, new DefaultPushAgent());

            return success("登录成功！", token);
        }
        return failed("登录失败,用户名或密码错误！");
    }

    private boolean check(String username, String password) {
        return !StringUtils.isEmpty(username) && DEFAULT_USERNAME.equals(username)
                && !StringUtils.isEmpty(password) && DEFAULT_PASSWORD.equals(password);
    }
}
