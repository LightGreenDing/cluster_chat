package com.wt.wtchat.model;


import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息
 *
 * @author Zed
 */
public class MessageInfo implements Serializable {
    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 消息内容
     */
    private String message;
    /**
     * 时间
     */
    private Date time;
    /**
     * 平台
     */
    private String platform;
    /**
     * 消息类型
     */
    private String type;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public MessageInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "roomId='" + roomId + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", platform='" + platform + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
