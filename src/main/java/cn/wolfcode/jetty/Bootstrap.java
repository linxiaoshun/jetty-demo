package cn.wolfcode.jetty;

import cn.wolfcode.jetty.util.ReflectUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Title: Bootstrap
 * @Description: Jetty Server 引导程序
 * @author leo
 * @date Created in 2017/12/22 下午1:33
 * @version v1.0
 */
public class Bootstrap {

    /**
     * 默认端口
     */
    private static final int DEFAULT_PORT = 8080;
    /**
     * 默认上下文路径
     */
    private static final String DEFAULT_CONTEXT_PATH = "/";
    /**
     * 默认web根路径
     */
    private static final String DEFAULT_APP_ROOT_PATH = "src/main/webapp";

    public static void main(String[] args) throws Exception {
        runJettyServer(DEFAULT_PORT, DEFAULT_CONTEXT_PATH, DEFAULT_APP_ROOT_PATH);
    }

    /**
     * 运行 Jetty Server
     *
     * @throws Exception
     */
    public static void runJettyServer(int port, String contextPath, String webAppPath) throws Exception {
        Server server = null;
        try {
            // 1. 创建 Server
            server = createDeployServer(port, contextPath);
//            server = createDevServer(port, contextPath, webAppPath);
            // 6. 启动线程
            server.start();
            // 7. 将线程加入到主线程
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null)
                    server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建部署版的 Jetty Server
     */
    public static Server createDeployServer(int port, String contextPath) throws UnsupportedEncodingException {
        // 1. 创建 Server
        Server server = new Server();
        //设置在JVM退出时关闭Jetty的钩子
        //这样就可以在整个功能测试时启动一次Jetty,然后让它在JVM退出时自动关闭
        server.setStopAtShutdown(true);

        // 2. 创建 ServerConnector 对象关联 Server
        ServerConnector connector = new ServerConnector(server);
        //解决Windows下重复启动Jetty不报告端口冲突的问题
        //在Windows下有个Windows + Sun的connector实现的问题,reuseAddress=true时重复启动同一个端口的Jetty不会报错
        //所以必须设为false,代价是若上次退出不干净(比如有TIME_WAIT),会导致新的Jetty不能启动,但权衡之下还是应该设为False
        //connector.setReuseAddress(false);
        connector.setPort(port);
        //为了设置reuseAddress=true所以创建Connector,否则直接new Server(port)即可,通过查看Server源码发现,二者是等效的
        //不过使用Connector的好处是可以让Jetty监听多个端口,此时创建多个绑定不同端口的Connector即可,最后一起setConnectors
        server.setConnectors(new Connector[]{connector});

        // 3. 获取 warFile 真实路径
        String warFile = ReflectUtils.ME.getExternalForm();
        String realPath = ReflectUtils.ME.getRealPath();

        // 4. 创建 WebAppContext
        WebAppContext ctx = new WebAppContext(warFile, contextPath);
        // 5. 将 server 设置到 webAppContext 中
        ctx.setServer(server);
        // c:/www/p2p/p2p.war
        // c:/www/p2p/work
        // 6. 获取到 war 文件所在的目录，在该目录下创建一个临时目录用于存放运行时信息
        String currentDir = new File(realPath).getParent();
        currentDir = URLDecoder.decode(currentDir, "UTF-8");
        File workDir = new File(currentDir, "work");
        ctx.setTempDirectory(workDir);

        // 7. 将 webAppContext 设置到 server 中
        server.setHandler(ctx);
        return server;
    }

    /**
     * 创建开发版的 Jetty Server
     */
    public static Server createDevServer(int port, String contextPath, String webAppPath) {
        // 1. 创建 Server
        Server server = new Server();
        //设置在JVM退出时关闭Jetty的钩子
        //这样就可以在整个功能测试时启动一次Jetty,然后让它在JVM退出时自动关闭
        server.setStopAtShutdown(true);

        // 2. 创建 ServerConnector 对象关联 Server
        ServerConnector connector = new ServerConnector(server);

        // 2.1 设置服务端口
        connector.setPort(port);

        // 2.2 设置重用地址为 false
        //解决Windows下重复启动Jetty不报告端口冲突的问题
        //在Windows下有个Windows + Sun的connector实现的问题,reuseAddress=true时重复启动同一个端口的Jetty不会报错
        //所以必须设为false,代价是若上次退出不干净(比如有TIME_WAIT),会导致新的Jetty不能启动,但权衡之下还是应该设为False
//        connector.setReuseAddress(false);

        // 3. 将 connector 对象加载到 server 中
        server.setConnectors(new Connector[]{connector});

        // 4. 创建 WebAppContext 对象，传入 web 根目录和上下文路径
        WebAppContext ctx = new WebAppContext(webAppPath, contextPath);

        // 4.1 设置 web.xml 路径
        ctx.setDescriptor(webAppPath + "/WEB-INF/web.xml");

        // 4.2 设置项目根路径
        ctx.setResourceBase(webAppPath);

        // 4.3 设置类加载路径
        ctx.setClassLoader(Thread.currentThread().getContextClassLoader());

        // 5. 将 webAppContext 对象放入到 Server 中
        server.setHandler(ctx);
        return server;
    }
}
