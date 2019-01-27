package cn.wolfcode.jetty.cache;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: TokenManager
 * @Description: 令牌管理器，用来管理已经登录的用户（模拟）
 * @author leo
 * @date Created in 2017/12/22 下午2:30
 * @version v1.0
 */
public enum TokenManager {
    ME;

    private Map<String, Object> tokenMap = new HashMap<>();

    public void put(String key, Object value) {
        tokenMap.put(key, value);
    }

    public void remove(String key) {
        tokenMap.remove(key);
    }

    public boolean contains(String key) {
        return tokenMap.containsKey(key);
    }
}
