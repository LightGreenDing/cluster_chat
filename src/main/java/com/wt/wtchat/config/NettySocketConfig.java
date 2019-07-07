package com.wt.wtchat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 启动时候加载socket参数
 * ip,端口
 *
 * @author Administrator
 */
@Configuration
@ConfigurationProperties(prefix = "wss.socket")
public class NettySocketConfig {
    private String host;
    private Integer port;
    private String singleRedis;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSingleRedis() {
        return singleRedis;
    }

    public void setSingleRedis(String singleRedis) {
        this.singleRedis = singleRedis;
    }
}
