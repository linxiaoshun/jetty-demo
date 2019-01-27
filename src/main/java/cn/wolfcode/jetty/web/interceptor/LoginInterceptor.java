package cn.wolfcode.jetty.web.interceptor;

import cn.wolfcode.jetty.cache.TokenManager;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author leo
 * @version v1.0
 * @Title: LoginInterceptor
 * @Description: 登录拦截
 * @date Created in 2017/12/26 上午2:39
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 令牌参数名称
     */
    public static final String TOKEN_NAME = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter(TOKEN_NAME);

        if (StringUtils.isEmpty(token) || !TokenManager.ME.contains(token)) {
            response.sendRedirect("/static/login.html");
            return false;
        }

        return true;
    }
}
