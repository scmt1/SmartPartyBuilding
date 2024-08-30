///*
// * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
// *
// * https://www.mall4j.com/
// *
// * 未经允许，不可做商业用途！
// *
// * 版权所有，侵权必究！
// */
//package com.yami.shop.platform.config;
//
//import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.commons.util.InetUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * xxl-job config
// *
// * @author FrozenWatermelon
// * @date 2021/1/18
// */
////@Configuration
////public class XxlJobConfig {
////    private final Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);
////
////    @Value("${xxl-job.admin.addresses}")
////    private String adminAddresses;
////
////    @Value("${xxl-job.accessToken}")
////    private String accessToken;
////
////    @Value("${xxl-job.logPath}")
////    private String logPath;
////
////    @Value("${server.port}")
////    private int port;
////
////    @Autowired
////    private InetUtils inetUtils;
////
////    @Bean
////    public XxlJobSpringExecutor xxlJobExecutor() {
////
////        logger.info(">>>>>>>>>>> xxl-job config init.");
////        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
////        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
////        xxlJobSpringExecutor.setAppname("mall4j-bbc");
////        // 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP
////        xxlJobSpringExecutor.setIp(inetUtils.findFirstNonLoopbackAddress().getHostAddress());
////        xxlJobSpringExecutor.setPort(port + 1000);
////        xxlJobSpringExecutor.setAccessToken(accessToken);
////        xxlJobSpringExecutor.setLogPath(logPath);
////        return xxlJobSpringExecutor;
////    }
////
//}
