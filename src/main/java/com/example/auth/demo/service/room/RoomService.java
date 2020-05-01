package com.example.auth.demo.service.room;

import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Reservation_;
import com.example.auth.demo.entity.face.UserFaceInfo;
import com.example.auth.demo.entity.user.UserDetail;

import java.util.List;

public interface RoomService {
    UserDetail getUserByFace(byte[] facefeature,int roomid);
    List<UserFaceInfo> getCunnentParticipant(Integer room_id);
    List<Reservation_> gettodayMeettingInfo(Integer room_id);
}
