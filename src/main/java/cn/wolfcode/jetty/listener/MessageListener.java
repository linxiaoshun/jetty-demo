package cn.wolfcode.jetty.listener;

import cn.wolfcode.jetty.vo.Message; /**
 * @author leo
 * @version v1.0
 * @Title: MessageListener
 * @Description: 消息监听器
 * @date Created in 2017/12/22 下午3:19
 */
public interface MessageListener {


    void onMessage(Message message);
}
