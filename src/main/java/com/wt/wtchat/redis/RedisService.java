package com.wt.wtchat.redis;

import java.util.Map;

/**
 * Redis接口
 *
 * @author Administrator
 */
public interface RedisService<K, V> {

    /**
     * 缓存房间
     */
    void roomCache();

    /**
     * 缓存平台
     */
    void platformCache();

    /**
     * 添加map
     *
     * @param key
     * @param map
     */
    void addMap(K key, Map<K, V> map);

    /**
     * 获取map缓存中的某个对象
     *
     * @param key   map对应的key
     * @param field map中该对象的key
     * @return
     */
    <T> T getMapField(K key, K field);


}
