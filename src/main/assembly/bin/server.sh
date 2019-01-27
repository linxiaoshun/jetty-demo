#! /bin/bash
#
# ##################################
#
#  Author: leo, Date: 2017/8/23
#
# ##################################
#
#  警告!!!：该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。
#  在杀死进程前，未作任何条件检查。在某些情况下，如程序正在进行文件或数据库写操作，
#  可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本，
#  增加在执行kill命令前的一系列检查。
#
# ##################################

# 接收应用名参数
APP_NAME=$1
#
# 接收动作指令参数
ACTION_CMD=$2

# 提示内容
tips="Usage: $0 {app_name} {start|stop|restart|status|info}"

# 判断参数是否为空，若为空则提示并结束
if [ ! -n "$APP_NAME" -o ! -n "$ACTION_CMD" ]; then
   echo $tips
   exit 1;
fi

# 获取路径中的应用名
APP_NAME=${APP_NAME##*/}

# 获取文件拓展名
EXT=${APP_NAME##*.}

# 匹配拓展名是否有效
case "$EXT" in
   war|jar)
     echo "checked $APP_NAME success!"
     ;;
   *)
     echo "only support jar or war files!"
     exit 1;
     ;;
esac

cd `dirname $0`
# bin 目录（脚本所在的目录）
BIN_DIR=`pwd`

cd ..
# 应用程序根目录
APP_HOME=`pwd`

# 如果日志目录不存在，则创建
if [ ! -d "$APP_HOME/logs" ];then
    mkdir $APP_HOME/logs
fi

#java虚拟机启动参数
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"

# 初始化psid变量（全局）
psid=0

# ##################################
#  (函数)判断程序是否已启动
#
#  说明：
#  使用JDK自带的JPS命令及grep命令组合，准确查找pid
#  jps 加 l 参数，表示显示java的完整包路径
#  使用awk，分割出pid ($1部分)，及Java程序名称($2部分)
# ##################################
checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_NAME`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

# ##################################
#  (函数)启动程序
#
#  说明：
#   1. 首先调用checkpid函数，刷新$psid全局变量
#   2. 如果程序已经启动（$psid不等于0），则提示程序已启动
#   3. 如果程序没有被启动，则执行启动命令行
#   4. 启动命令执行后，再次调用checkpid函数
#   5. 如果步骤4的结果能够确认程序的pid,则打印[OK]，否则打印[Failed]
#  注意：echo -n 表示打印字符后，不换行
#  注意: "nohup 某命令 >/dev/null 2>&1 &" 的用法
# ##################################
start() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo "================================"
      echo "warn: $APP_NAME already started! (pid=$psid)"
      echo "================================"
   else
      echo -n "Starting $APP_NAME ..."
      nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_NAME >> $APP_HOME/logs/${APP_NAME%*.$EXT}_`date +%Y-%m-%d`.log +%Y-%m-%d.log 2>&1 &
      checkpid
      if [ $psid -ne 0 ]; then
         echo "(pid=$psid) [OK]"
      else
         echo "[Failed]"
      fi
   fi
}

# ##################################
#  (函数)停止程序
#
#  说明：
#   1. 首先调用checkpid函数，刷新$psid全局变量
#   2. 如果程序已经启动（$psid不等于0），则开始执行停止，否则，提示程序未运行
#   3. 使用kill -9 pid命令进行强制杀死进程
#   4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
#   5. 如果步骤4的结果$?等于0,则打印[OK]，否则打印[Failed]
#   6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理（递归调用stop）。
#  注意：echo -n 表示打印字符后，不换行
#  注意: 在shell编程中，"$?" 表示上一句命令或者一个函数的返回值
# ##################################
stop() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_NAME ...(pid=$psid) "
      kill -9 $psid
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi

      checkpid
      if [ $psid -ne 0 ]; then
         stop
      fi
   else
      echo "================================"
      echo "warn: $APP_NAME is not running"
      echo "================================"
   fi
}

# ##################################
#  (函数)检查程序运行状态
#
#  说明：
#   1. 首先调用checkpid函数，刷新$psid全局变量
#   2. 如果程序已经启动（$psid不等于0），则提示正在运行并表示出pid
#   3. 否则，提示程序未运行
# ##################################
status() {
   checkpid

   if [ $psid -ne 0 ];  then
      echo "$APP_NAME is running! (pid=$psid)"
   else
      echo "$APP_NAME is not running"
   fi
}

# ##################################
#  (函数)打印系统环境参数
# ##################################
info() {
   echo "System Information:"
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_NAME=$APP_NAME"
   echo "****************************"
}

# ##################################
#  读取脚本的第二个参数($2)也就是动作指令（$ACTION_CMD），进行判断
#  参数取值范围：{start|stop|restart|status|info}
#  如参数不在指定范围之内，则打印帮助信息
# ##################################
case "$ACTION_CMD" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
  *)
     echo $tips
     exit 1
esac
exit 0
