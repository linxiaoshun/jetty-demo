package cn.wolfcode.jetty.vo;

/**
 * @author leo
 * @version v1.0
 * @Title: Default
 * @Description: 推送中的消息实体
 * @date Created in 2017/12/22 下午3:17
 */
public class Message implements Comparable<Message> {

    private String target;
    private String content;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Message o) {
        return o.getTarget().equals(this.target) ? 0 : 1;
    }
}
