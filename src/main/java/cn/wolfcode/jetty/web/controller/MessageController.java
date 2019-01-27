package cn.wolfcode.jetty.web.controller;

import cn.wolfcode.jetty.adapter.PushAdapter;
import cn.wolfcode.jetty.agent.PushAgent;
import cn.wolfcode.jetty.cache.PushAgentManager;
import cn.wolfcode.jetty.dto.ResponseDto;
import cn.wolfcode.jetty.vo.Message;
import cn.wolfcode.jetty.web.controller.base.BaseController;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author leo
 * @version v1.0
 * @Title: MessageController
 * @Description: 消息相关的类
 * @date Created in 2017/12/23 上午10:46
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    @RequestMapping("/pull")
    public ResponseDto pull(String token, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 当前用户是否有注册消息推送
        if (PushAgentManager.ME.contains(token)) {
            // 获取该用户的推送代理
            PushAgent agent = PushAgentManager.ME.getAgent(token);
            // 获取当前请求的 Continuation 对象
            Continuation continuation = ContinuationSupport.getContinuation(req);
            // 第一次请求时
            if (continuation.isInitial()) {
                // 创建推送适配器
                PushAdapter adapter = new PushAdapter(continuation, agent);
                // 将其保存至 continuation
                continuation.setAttribute(PushAdapter.PUSH_ADAPTER_ATTR_NAME, adapter);
                // 添加 continuation 监听器
                continuation.addContinuationListener(new ContinuationListener() {
                    @Override
                    public void onComplete(Continuation continuation) {
                        // 到处理完成后，清除适配器
                        Object adapter = continuation.getAttribute(PushAdapter.PUSH_ADAPTER_ATTR_NAME);
                        if (adapter != null) {
                            continuation.removeAttribute(PushAdapter.PUSH_ADAPTER_ATTR_NAME);
                        }
                    }

                    @Override
                    public void onTimeout(Continuation continuation) {
                        // 超时与完成执行相同操作
                        onComplete(continuation);
                    }
                });
            }

            // 判断 continuation 是否失效
            if (continuation.isExpired()) {
                return failed("连接已失效！");
            }

            PushAdapter adapter = (PushAdapter) continuation.getAttribute(PushAdapter.PUSH_ADAPTER_ATTR_NAME);
            Message message;
            while (true) {
                // 获取队列中的消息
                message = adapter.getPushAgent().pull();
                if (message == null) {
                    break;
                }
                // 响应消息
                return success(message.getContent());
            }

            // 如果没有消息，则将当前线程挂起
            continuation.suspend();
        }
        return null;
    }

    @RequestMapping("/send")
    public ResponseDto send(String token, String message) {
        // 判断是否注册推送
        if (PushAgentManager.ME.contains(token)) {
            // 构建消息体
            Message msg = new Message();
            msg.setContent(message);
            msg.setTarget(token);

            // 获取推送代理
            PushAgent agent = PushAgentManager.ME.getAgent(token);
            // 判断是否已经初始化
            if (agent.isInited()) {
                // 通知消息事件
                agent.onEvent(msg);
            }
            // 发送消息
            agent.send(msg);
            return success();
        }
        return failed("请先订阅推送！");
    }
}
