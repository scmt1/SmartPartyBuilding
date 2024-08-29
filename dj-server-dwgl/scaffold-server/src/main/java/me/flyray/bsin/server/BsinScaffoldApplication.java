package me.flyray.bsin.server;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ：bolei
 * @date ：Created in 2021/11/30 16:00
 * @description：bsin 脚手架启动类
 * @modified By：
 */
@ImportResource({"classpath*:sofa/rpc-provider-hello.xml"})
@MapperScan("me.flyray.bsin.server")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableScheduling //开启定时任务
public class BsinScaffoldApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BsinScaffoldApplication.class);
        springApplication.run(args);
    }

}
