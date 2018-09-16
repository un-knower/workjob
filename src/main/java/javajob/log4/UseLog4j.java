package javajob.log4;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @program: workjob
 * @description: 测试学习log4j
 * @author: 刘文强  kingcall
 * @create: 2018-03-31 18:33
 **/
public class UseLog4j {
    //日志记录器
    private static Logger LOGGER = LogManager.getLogger(UseLog4j.class);

    public static void main(String[] args) {
        //自动快速地使用缺省Log4j环境   当你的 资源路径下有这个配置文件，就可以自动去使用
        //BasicConfigurator.configure();
        // 有多个log4j 的日志配置文件的时候可以指定去读  其实这才是合理的
        PropertyConfigurator.configure("log4j.properties");
        //打印日志信息
        LOGGER.info("hello log4j !");
        LOGGER.error("hello error");
        LOGGER.warn("hello warn");
    }
}