<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/static/js/jquery-2.1.3.min.js"></script>
    <script>
        // 失败重试次数 & 计数
        var try_num = 10, count = 0;

        $(function () {
            // 发送请求获取消息
            pull(getQueryString("token"));

            $(".show_message").click(function () {
                // 清零消息数量
                $(".msg_num").text(0);
                sessionStorage.msgCount = 0;

                // 显示消息
                $(".msg_container").show(500);
                // 五秒后清空消息
                var suped = setInterval(function () {
                    $(".msg_container").hide(500);
                    $(".msg_container").html("");
                    clearInterval(suped);
                }, 3000);
            });
        });

        function pull(token) {
            $.ajax({
                url: "/message/pull?token=" +token,
                timout: 50000,
                error: function (data) {
                    console.error("请求处理失败！\r", data);

                    // 当超过错误尝试次数，则停止调用
                    if (count == try_num) {
                        return;
                    }

                    // 出错后延迟三秒再重新请求
                    var timer = setInterval(function () {
                        pull(token);
                        count++;
                        clearInterval(timer);
                    }, 3000);
                },
                success: function (resp) {
                    if (resp.code == 200) {
                        // 获取提示消息数量并 +1
                        var msgCount = parseInt($(".msg_num").text() || 0) + 1;
                        // 将增加后的值设置到页面
                        $(".msg_num").text(msgCount);

                        // 将获取到的消息写回页面
                        var msg = "<span>" + resp.data + "</span></br>";
                        $(msg).appendTo(".msg_container");
                    }

                    // 不管状态码，成功响应后都立即重新发送请求等待获取消息
                    // 优化建议：根据实际状态码的意义判定延迟请求时间，避免无效请求
                    pull(token);
                }
            });
        }

        /**
         * 获取 url 中的参数
         * @param name url 的参数名
         * @returns 对应参数名的参数值
         */
        function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
    </script>
</head>
<body>
<h1>Main</h1>
<<a href="javascript:;" class="show_message">您有<span class="msg_num">0</span>条新消息</a>></a>
<div class="msg_container" style="display: none">

</div>
</body>
</html>
