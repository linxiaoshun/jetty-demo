package cn.wolfcode.jetty.agent;

import cn.wolfcode.jetty.listener.MessageListener;
import cn.wolfcode.jetty.vo.Message;

/**
 * @author leo
 * @version v1.0
 * @Title: Default
 * @Description: 推送代理接口
 * @date Created in 2017/12/22 下午3:16
 */
public interface PushAgent {

    Message send(Message message);

    Message pull();

    void addListener(MessageListener messageListener);

    void onEvent(Message message);

    boolean isInited();
}
