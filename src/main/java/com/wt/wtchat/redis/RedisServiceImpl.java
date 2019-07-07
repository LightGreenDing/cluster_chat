package com.wt.wtchat.redis;

import com.wt.wtchat.model.Platform;
import com.wt.wtchat.model.Room;
import com.wt.wtchat.service.PlatformService;
import com.wt.wtchat.service.RoomService;
import com.wt.wtchat.utils.SocketConstant;
import com.wt.wtchat.utils.SpringBeanFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis接口实现类
 *
 * @author Zed
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService<String, Object> {
    @Autowired
    private RoomService roomService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void roomCache() {
        if (roomService == null) {
            roomService = SpringBeanFactoryUtil.getBean(RoomService.class);
        }
        List<Room> list = roomService.getAll();
        Map<String, Object> roomMap = new HashMap<>(list.size());
        list.forEach(room -> {
            roomMap.put(String.valueOf(room.getGameType()), room);
        });
        addMap(SocketConstant.SOCKET_ROOM, roomMap);
    }

    @Override
    public void platformCache() {
        if (platformService == null) {
            platformService = SpringBeanFactoryUtil.getBean(PlatformService.class);
        }
        List<Platform> list = platformService.getAll();
        Map<String, Object> platformMap = new HashMap<>(list.size());
        list.forEach(platform -> {
            platformMap.put(platform.getCode(), platform);
        });
        addMap(SocketConstant.PLATFORM, platformMap);
    }

    @Override
    public void addMap(String key, Map<String, Object> map) {
        redisTemplate.boundHashOps(key).putAll(map);
    }

    @Override
    public <T> T getMapField(String key, String field) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

}
