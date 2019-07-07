package com.wt.wtchat.service;

import com.wt.wtchat.model.MessageInfo;

import java.util.List;

/**
 * 聊天消息接口
 *
 * @author Zed
 */
public interface MessageInfoService {

    /**
     * 根据房间号和数据量 获取聊天记录
     *
     * @param platform 平台
     * @param roomId   房间号
     * @param size     消息数据
     * @return 查询的数据
     */
    List<MessageInfo> findHistoryByRoomId(String platform, String roomId, int size);

    /**
     * 保存聊天记录
     *
     * @param messageInfo 聊天消息
     */
    void save(MessageInfo messageInfo);
}
