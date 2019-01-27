package cn.wolfcode.jetty.adapter;

import cn.wolfcode.jetty.agent.PushAgent;
import org.eclipse.jetty.continuation.Continuation;

/**
 * @author leo
 * @version v1.0
 * @Title: PushAdapter
 * @Description: 推送适配器
 * @date Created in 2017/12/22 下午3:25
 */
public class PushAdapter {

    public static final String PUSH_ADAPTER_ATTR_NAME = "adapter";

    private Continuation continuation;
    private PushAgent pushAgent;

    public PushAdapter(Continuation continuation, PushAgent pushAgent) {
        this.continuation = continuation;
        this.pushAgent = pushAgent;
        // 添加消息监听器，当消息来的时候唤醒线程
        this.pushAgent.addListener(message -> {
            // 若当前servlet处于被挂起状态
            if (this.continuation.isSuspended()) {
                // 将其唤醒
                this.continuation.resume();
            }
        });
    }

    public Continuation getContinuation() {
        return continuation;
    }

    public void setContinuation(Continuation continuation) {
        this.continuation = continuation;
    }

    public PushAgent getPushAgent() {
        return pushAgent;
    }


    public void setPushAgent(PushAgent pushAgent) {
        this.pushAgent = pushAgent;
    }
}