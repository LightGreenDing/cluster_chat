package com.wt.wtchat.service;

import com.wt.wtchat.model.Platform;

import java.util.List;

/**
 * 平台接口
 *
 * @author Zed
 */
public interface PlatformService {
    /**
     * 获取所有的平台
     */
    List<Platform> getAll();
}
