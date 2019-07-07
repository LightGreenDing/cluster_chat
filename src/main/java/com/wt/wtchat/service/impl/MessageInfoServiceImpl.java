package com.wt.wtchat.service.impl;

import com.wt.wtchat.model.MessageInfo;
import com.wt.wtchat.service.MessageInfoService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 聊天消息接口实现类
 *
 * @author Zed
 */
@Service("messageInfoService")
public class MessageInfoServiceImpl implements MessageInfoService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<MessageInfo> findHistoryByRoomId(String platform, String roomId, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Query query = new Query();
        query.limit(size);
        query.addCriteria(Criteria.where("roomId").is(roomId));
        query.addCriteria(Criteria.where("platform").is(platform));
        //查询当天时间范围
        List<MessageInfo> messageInfos = mongoTemplate.find(query.with(sort), MessageInfo.class);
        //倒叙展示
        Collections.reverse(messageInfos);
        return messageInfos;
    }

    @Override
    public void save(MessageInfo messageInfo) {
        mongoTemplate.save(messageInfo);
    }
}
