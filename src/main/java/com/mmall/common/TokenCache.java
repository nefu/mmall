package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by jay on 2018/3/12.
 */
public class TokenCache {
    public static final String prefix = "token_";
    public static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    //LRU算法 本地缓存 初始化最大值为10000 有效期是12个 单位小时
    public static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                //默认的数据加载实现 当调用get值的时候 如果key没有对应的值 就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }

        }
        catch(Exception e){
            logger.error("localcache get errot");
        }
        return null;
    }



}
