package com.wt.wtchat.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.wt.wtchat.dao.RoomDao;
import com.wt.wtchat.model.MessageInfo;
import com.wt.wtchat.model.Room;
import com.wt.wtchat.result.ResultGenerator;
import com.wt.wtchat.service.MessageInfoService;
import com.wt.wtchat.service.RoomService;
import com.wt.wtchat.utils.SocketConstant;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 房间接口实现类
 *
 * @author Zed
 */
@Service("roomService")
public class RoomServiceImpl implements RoomService {
    @Resource
    private RoomDao roomDao;
    @Resource
    private MessageInfoService messageInfoService;

    @Override
    public List<Room> getAll() {
        return roomDao.findAll();
    }

    @Override
    public void sendMessageToRoom(MessageInfo message, SocketIOServer socketServer, RedissonClient redissonClient) {
        if (null == message.getRoomId() || null == message.getPlatform()) {
            ResultGenerator.failResult("roomId或者platform为null");
            return;
        }
        message.setTime(new Date());
        message.setType(SocketConstant.C_Chat_C);
//        String room = message.getPlatform() + "_" + message.getRoomId();
//        BroadcastOperations roomOperations = socketServer.getRoomOperations(room);
//        //只能推送给当前socket服务器中连接到该房间的用户的消息
//        roomOperations.sendEvent(SocketConstant.C_Chat_C, message);
        messageInfoService.save(message);
        //然后发送给订阅的其他客户端
        RTopic topic = redissonClient.getTopic(SocketConstant.BROADCAST, new SerializationCodec());
        topic.publish(message);
        ResultGenerator.successResult();
    }

    @Override
    public void sendWinningToRoom(MessageInfo message, SocketIOServer socketServer, RedissonClient redissonClient) {
        if (null == message.getRoomId() || null == message.getPlatform()) {
            ResultGenerator.failResult("roomId或者platform为null");
            return;
        }
        message.setType(SocketConstant.WINNING);
        message.setTime(new Date());
//        String room = message.getPlatform() + "_" + message.getRoomId();
//        BroadcastOperations roomOperations = socketServer.getRoomOperations(room);
        //只能推送给当前socket服务器中连接到该房间的用户的消息
//        roomOperations.sendEvent(SocketConstant.WINNING, message);
        //然后派送给订阅的其他服务器
        RTopic topic = redissonClient.getTopic(SocketConstant.BROADCAST, new SerializationCodec());
        topic.publish(message);
        ResultGenerator.successResult();
    }
}
