package cn.wolfcode.jetty.util;

import java.net.URL;

/**
 * @Title: ReflectUtils
 * @Description: 反射工具类
 * @author leo
 * @date Created in 2017/12/22 下午2:11
 * @version v1.0
 */
public enum ReflectUtils {

    ME;

    private void ReflectUtils() {
    }

    /**
     * @return 获取此 URL 字符串展示形式
     */
    public String getExternalForm() {
        return getURL().toExternalForm();
    }

    /**
     * @return 获取真实路径
     */
    public String getRealPath() {
        return getURL().getPath();
    }

    private URL getURL() {
        return this.getClass().getProtectionDomain().getCodeSource().getLocation();
    }

}
