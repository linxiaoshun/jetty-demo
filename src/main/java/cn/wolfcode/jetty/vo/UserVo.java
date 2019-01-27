package cn.wolfcode.jetty.vo;

/**
 * @author leo
 * @version v1.0
 * @Title: UserVo
 * @Description: 封装用户相关信息
 * @date Created in 2017/12/26 上午2:36
 */
public class UserVo {

    private String username;
    private String password;
    /* ................ */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
