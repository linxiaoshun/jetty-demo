package cn.wolfcode.jetty.agent;

import cn.wolfcode.jetty.listener.MessageListener;
import cn.wolfcode.jetty.vo.Message;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author leo
 * @version v1.0
 * @Title: Default
 * @Description: DefaultPushAgent
 * @date Created in 2017/12/22 下午3:21
 */
public class DefaultPushAgent implements PushAgent {

    /**
     * 客户端通过长连接接口获取消息时，不断从队列中 poll，拿到新消息后则返回
     */
    private Queue<Message> messages = new PriorityQueue<>();
    private MessageListener messageListener;

    @Override
    public Message send(Message message) {
        synchronized (messages) {
            messages.add(message);
        }
        return message;
    }

    @Override
    public Message pull() {
        synchronized (messages) {
            return messages.poll();
        }
    }

    @Override
    public void addListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void onEvent(Message message) {
        this.messageListener.onMessage(message);
    }

    @Override
    public boolean isInited() {
        return messageListener != null;
    }
}
