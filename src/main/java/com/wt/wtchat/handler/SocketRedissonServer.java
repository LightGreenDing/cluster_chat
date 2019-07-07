package com.wt.wtchat.handler;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.corundumstudio.socketio.store.pubsub.BaseStoreFactory;
import com.wt.wtchat.config.NettySocketConfig;
import com.wt.wtchat.model.MessageInfo;
import com.wt.wtchat.redis.RedisService;
import com.wt.wtchat.utils.SocketConstant;
import com.wt.wtchat.utils.SpringBeanFactoryUtil;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;


/**
 * socket服务配置启动类
 */
@Component
public class SocketRedissonServer implements CommandLineRunner {
    @Autowired
    private RedisService redisService;
    private RedissonClient redisson;
    private SocketIOServer server;


    public SocketRedissonServer(NettySocketConfig nettySocketConfig) {
        server = new SocketIOServer(config(nettySocketConfig));
        //订阅其他服务器发布的消息
        RTopic topic = redisson.getTopic(SocketConstant.BROADCAST, new SerializationCodec());
        topic.addListener(MessageInfo.class, (charSequence, messageInfo) -> {
//                threadPoolTaskExecutorEx.execute(() -> {
            String roomIdSring = messageInfo.getPlatform() + "_" + messageInfo.getRoomId();
            BroadcastOperations roomOperations = server.getRoomOperations(roomIdSring);
            roomOperations.sendEvent(messageInfo.getType(), messageInfo);
            System.out.println(messageInfo.toString());
//                });
        });

        //添加监听
        server.addListeners(new MessageAnnotation(server, redisson, nettySocketConfig));
    }

    @Bean
    public SocketIOServer socketIOServer() {
        return server;
    }

    @Bean
    public RedissonClient redissonClient() {
        return redisson;
    }

    /**
     * socket服务配置
     */
    private Configuration config(NettySocketConfig nettySocketConfig) {
        Configuration socketConfig = new Configuration();
        socketConfig.setHostname(nettySocketConfig.getHost());
        socketConfig.setPort(nettySocketConfig.getPort());
        socketConfig.setMaxFramePayloadLength(1024 * 1024);
        socketConfig.setMaxHttpContentLength(1024 * 1024);
        // 开放跨域
        socketConfig.setOrigin(null);
        socketConfig.getSocketConfig().setReuseAddress(true);
        socketConfig.getSocketConfig().setSoLinger(0);
        //50工作线程
        socketConfig.setWorkerThreads(50);
        //服务是低延迟的
        socketConfig.getSocketConfig().setTcpNoDelay(true);
        //可以探测客户端的连接是否还存活着
        socketConfig.getSocketConfig().setTcpKeepAlive(true);
        //验证
        socketConfig.setAuthorizationListener(data -> {
            String roomId = data.getSingleUrlParam(SocketConstant.ROOM_ID);
            String platform = data.getSingleUrlParam(SocketConstant.PLATFORM);
            if (null == roomId || null == platform) {
                return false;
            }
            if (redisService == null) {
                redisService = SpringBeanFactoryUtil.getBean(RedisService.class);
            }
            Object mapField = redisService.getMapField(SocketConstant.SOCKET_ROOM, roomId);
            Object platformField = redisService.getMapField(SocketConstant.PLATFORM, platform);
            return null != mapField && null != platformField;
        });

        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress(nettySocketConfig.getSingleRedis());
        this.redisson = Redisson.create(redissonConfig);
        BaseStoreFactory baseStoreFactory = new RedissonStoreFactory(redisson);
        socketConfig.setStoreFactory(baseStoreFactory);
        return socketConfig;
    }

    @Override
    public void run(String... args) {
        server.start();
        //缓存房间信息
        redisService.roomCache();
        //缓存平台
        redisService.platformCache();
        System.out.println("--------微投聊天室启动成功--------");
    }

    @PreDestroy
    public void destory() {
        server.stop();
    }
}
