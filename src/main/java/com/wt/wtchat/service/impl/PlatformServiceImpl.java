package com.wt.wtchat.service.impl;

import com.wt.wtchat.dao.PlatformDao;
import com.wt.wtchat.model.Platform;
import com.wt.wtchat.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台服务实现类
 *
 * @author Zed
 */
@Service("platformService")
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformDao platformDao;

    @Override
    public List<Platform> getAll() {
        return platformDao.findAll();
    }
}
