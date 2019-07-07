package com.wt.wtchat.handler;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.wt.wtchat.config.NettySocketConfig;
import com.wt.wtchat.model.MessageInfo;
import com.wt.wtchat.result.Result;
import com.wt.wtchat.result.ResultGenerator;
import com.wt.wtchat.service.MessageInfoService;
import com.wt.wtchat.service.RoomService;
import com.wt.wtchat.task.ThreadPoolTaskExecutorEx;
import com.wt.wtchat.utils.SocketConstant;
import com.wt.wtchat.utils.SpringBeanFactoryUtil;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 监听事件
 *
 * @author Zed
 */
@RestController
public class MessageAnnotation {

    @Autowired
    private RoomService roomService;
    @Autowired
    private MessageInfoService messageInfoService;

    @Autowired
    private ThreadPoolTaskExecutorEx threadPoolTaskExecutorEx;
    @Autowired
    private SocketIOServer socketServer;
    @Autowired
    private RedissonClient redissonClient;

    private NettySocketConfig nettySocketConfig;

    public MessageAnnotation(SocketIOServer socketServer, RedissonClient redisson, NettySocketConfig nettySocketConfig) {
        this.socketServer = socketServer;
        this.redissonClient = redisson;
        this.nettySocketConfig = nettySocketConfig;
    }

    /**
     * 用户连接
     *
     * @param client 客户端
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        SocketAddress remoteAddress = client.getRemoteAddress();
        System.out.println("当前服务器Port:" + nettySocketConfig.getPort() + " 连接IP:" + remoteAddress + ",连接sessionId" + client.getSessionId());
        //查看房间是否存在
        String roomId = client.getHandshakeData().getSingleUrlParam(SocketConstant.ROOM_ID);
        String platform = client.getHandshakeData().getSingleUrlParam(SocketConstant.PLATFORM);
        //加入聊天室(平台名称加房间号 为唯一房间)
        String room = platform + "_" + roomId;

        client.joinRoom(room);
        if (messageInfoService == null) {
            messageInfoService = SpringBeanFactoryUtil.getBean(MessageInfoService.class);
        }

        if (threadPoolTaskExecutorEx == null) {
            threadPoolTaskExecutorEx = SpringBeanFactoryUtil.getBean(ThreadPoolTaskExecutorEx.class);
        }
        //异步获取历史记录发送记录到房间
        threadPoolTaskExecutorEx.execute(() -> {
            List<MessageInfo> historyByRoomId = messageInfoService.findHistoryByRoomId(platform, roomId, 30);
            client.sendEvent(SocketConstant.GET_HISTORY_MESSAGE, historyByRoomId);
        });
        Collection<SocketIOClient> allClients = socketServer.getAllClients();
        System.out.println("断开连接数" + allClients.size());
    }

    /**
     * 用户断开连接
     *
     * @param client 客户端
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        threadPoolTaskExecutorEx.execute(() -> {
            Set<String> allRooms = client.getAllRooms();
            allRooms.forEach(client::leaveRoom);
        });
        //客户端离开房间
        //关闭连接
        client.disconnect();
        Collection<SocketIOClient> allClients = socketServer.getAllClients();
        System.out.println("断开连接数" + allClients.size());
    }

    /**
     * 交互聊天
     *
     * @param client      客户端
     * @param messageInfo 消息
     */
    @OnEvent("cChatC")
    public void onData(SocketIOClient client, MessageInfo messageInfo) {
        String roomId = messageInfo.getRoomId();
        String platform = messageInfo.getPlatform();
        String room = platform + "_" + roomId;
        BroadcastOperations roomOperations = socketServer.getRoomOperations(room);
        roomOperations.sendEvent("cChatC", messageInfo);
        client.sendEvent("cChatC", messageInfo);
    }


    /**
     * 测试显示
     */
    @RequestMapping("/index")
    public String index() {
        return "--------微投聊天室启动成功--------";
    }

    /**
     * 将推送消息发送到到房间
     *
     * @param message 要推送到聊天室内容
     */
    @RequestMapping("/sendMessageToRoom")
    public Result sendMessageToRoom(MessageInfo message) {
        if (roomService == null) {
            roomService = SpringBeanFactoryUtil.getBean(RoomService.class);
        }
        threadPoolTaskExecutorEx.execute(() -> roomService.sendMessageToRoom(message, socketServer, redissonClient));
        return ResultGenerator.successResult();
    }

    /**
     * 将中奖消息发送到到房间
     *
     * @param message 要推送到聊天室内容
     */
    @RequestMapping("/sendWinningToRoom")
    public Result sendWinningToRoom(MessageInfo message) {
        if (roomService == null) {
            roomService = SpringBeanFactoryUtil.getBean(RoomService.class);
        }
        threadPoolTaskExecutorEx.execute(() -> roomService.sendWinningToRoom(message, socketServer, redissonClient));
        return ResultGenerator.successResult();
    }
}
