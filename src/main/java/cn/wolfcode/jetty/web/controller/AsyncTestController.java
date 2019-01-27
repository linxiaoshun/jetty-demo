package cn.wolfcode.jetty.web.controller;

import cn.wolfcode.jetty.dto.ResponseDto;
import cn.wolfcode.jetty.web.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Title: AsyncTestController
 * @Description: 异步 Servlet 测试 Controller
 * @author leo
 * @date Created in 2017/12/22 下午2:17
 * @version v1.0
 */
@Controller
public class AsyncTestController extends BaseController {

    @Autowired
    private ExecutorService executorPool;

    @RequestMapping("/async")
    @ResponseBody
    public DeferredResult<ResponseDto> async() {
        DeferredResult<ResponseDto> defer = new DeferredResult<>(12_000L);
        executorPool.submit(() -> {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            defer.setResult(success());
        });
        return defer;
    }

    @RequestMapping("/sync")
    @ResponseBody
    public ResponseDto sync() throws Exception {
        Future<ResponseDto> future = executorPool.submit(() -> {
            Thread.sleep(100);
            return success();
        });
        return future.get();
    }
}
