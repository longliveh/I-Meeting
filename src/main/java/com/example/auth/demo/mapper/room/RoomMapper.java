package com.example.auth.demo.mapper.room;

import com.arcsoft.face.FaceFeature;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Reservation_;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.face.UserFaceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface RoomMapper {

    /**
     * 查询face
     * @param userid
     * @return
     */
    UserFaceInfo getFaceFeatureById(@Param("userid") long userid);


    /**
     * 查询当前时间10分钟后参加room_id会议室的用户id
     * @param room_id
     * @return
     */
    String getCurrentMeettingParticipant(@Param("room_id")Integer room_id,@Param("time") Date nowAfter10min);


    /**
     * 根据roomid查询当天会议室使用情况
     * @param room_id
     * @return
     */
    List<Reservation_> getMeeting(@Param("room_id")Integer room_id, @Param("datestart")Date day_0, @Param("dateend")Date day_23);
}
