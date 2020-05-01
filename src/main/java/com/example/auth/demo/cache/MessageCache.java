package com.example.auth.demo.cache;

import com.example.auth.demo.mapper.user.MessageMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class MessageCache {
    private static final Logger LOG = LoggerFactory.getLogger(MessageCache.class);

    @Autowired
    private MessageMapper messageMapper;

    // cache对象的创建和缓存策略的设置。
    private LoadingCache<Long, Optional<List<Integer>>> cache = CacheBuilder.newBuilder().maximumSize(500)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<Long, Optional<List<Integer>>>() {
                // 在本地没有缓存的时候会调用该方法进行加载，将获取的值进行缓存并返回结果
                @Override
                public Optional<List<Integer>> load(Long userid) throws Exception {
                    List<Integer> messageslist = messageMapper.getMessage(userid);
                    if (messageslist.isEmpty())
                    {
                        return Optional.ofNullable(null);

                    }
                    return Optional.ofNullable(messageslist);
                }
            });


    // 调用方法从缓存中获取缓存对象，如果本地没有缓存则会调用load方法加载数据，加载数据后本地缓存并返回结果
    public List<Integer> getMessagelist(long userid) {
        try {
            Optional<List<Integer>> op = cache.get(userid);
            return op.isPresent() ? op.get() : null;
        } catch (Exception e) {
            LOG.error("读取消息缓存失败", userid, e);
        }
        return null;
    }
}
