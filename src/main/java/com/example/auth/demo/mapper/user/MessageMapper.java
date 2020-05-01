package com.example.auth.demo.mapper.user;


import com.example.auth.demo.cache.msg.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    /**
     * 查询离线会议通知消息
     * @param userid
     * @return
     */
    List<Integer> getMessage(@Param("userid") long userid);


    /**
     * 增加离线会议通知消息
     * @param target_userid
     * @param meeting_id
     * @return
     */
    int addMessage(@Param("target_userid") int target_userid,@Param("meeting_id") int meeting_id);

    /**
     * 增加离线会议通知消息
     * @param target_userid
     * @param meeting_id
     * @return
     */
    int rmMessage(@Param("target_userid")Integer target_userid, @Param("meeting_id")Integer meeting_id);

    /**
     * 返回用户相关的所有信息
     * @param case0
     * @param case1
     * @param case2
     * @param case3
     * @return
     */
    List<Message> getAllMeeting(@Param("case0")String case0,@Param("case1")String case1,
                          @Param("case2")String case2,@Param("case3")String case3);
}
