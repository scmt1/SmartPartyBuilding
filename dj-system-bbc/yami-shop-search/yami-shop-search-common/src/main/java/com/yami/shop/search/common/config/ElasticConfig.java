/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.config;

import cn.hutool.core.util.StrUtil;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/8/4
 */
@Configuration
public class ElasticConfig {

    @Value("${elastic.address}")
    private List<String> addressList;

    @Value("${elastic.passWord:}")
    private String passWord;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        if (StrUtil.isNotBlank(passWord)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", passWord));

            HttpHost[] httpHosts = new HttpHost[addressList.size()];

            for (int i = 0; i < addressList.size(); i++) {
                httpHosts[i] = HttpHost.create(addressList.get(i));
            }
            return new RestHighLevelClient(
                    RestClient.builder(httpHosts)
                            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                    .setDefaultCredentialsProvider(credentialsProvider)));
        }
        else {
            HttpHost[] httpHosts = new HttpHost[addressList.size()];

            for (int i = 0; i < addressList.size(); i++) {
                httpHosts[i] = HttpHost.create(addressList.get(i));
            }
            return new RestHighLevelClient(
                    RestClient.builder(httpHosts));
        }
    }
}
