package javajob.log4;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * @program: workjob
 * @description: 自定义log Appender 只输出同等级别的信息  而不是全部输出
 * @author: 刘文强  kingcall
 * @create: 2018-03-31 21:25
 **/
public class MyLogAppender extends DailyRollingFileAppender {
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {
        return this.getThreshold().equals(priority);
    }
}
