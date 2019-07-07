package com.wt.wtchat.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.wt.wtchat.model.MessageInfo;
import com.wt.wtchat.model.Room;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * 房间接口
 *
 * @author Zed
 */
public interface RoomService {
    /**
     * 获取所有房间
     *
     * @return 房间集合
     */
    List<Room> getAll();

    /**
     * 推送消息到聊天室
     *
     * @param message        消息内容封装
     * @param socketServer   服务
     * @param redissonClient 发布订阅工具
     */
    void sendMessageToRoom(MessageInfo message, SocketIOServer socketServer, RedissonClient redissonClient);

    /**
     * 推送消息到聊天室
     *
     * @param message        消息内容封装
     * @param socketServer   服务
     * @param redissonClient 发布订阅工具
     */
    void sendWinningToRoom(MessageInfo message, SocketIOServer socketServer, RedissonClient redissonClient);
}
