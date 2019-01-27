package cn.wolfcode.jetty.cache;

import cn.wolfcode.jetty.agent.PushAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leo
 * @version v1.0
 * @Title: PushAgentManager
 * @Description: 推送代理的管理类
 * @date Created in 2017/12/23 上午10:49
 */
public enum PushAgentManager {
    ME;

    private final Map<String, PushAgent> agentMap = new HashMap<>();

    public void registry(String token, PushAgent agent) {
        agentMap.put(token, agent);
    }

    public boolean contains(String token) {
        return agentMap.keySet().contains(token);
    }

    public PushAgent getAgent(String token) {
        return agentMap.get(token);
    }

}
