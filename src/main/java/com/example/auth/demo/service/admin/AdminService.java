package com.example.auth.demo.service.admin;

import com.example.auth.demo.entity.common.Room;

public interface AdminService {

    boolean updateVerifyInfo(int id ,long userid, int state);

    boolean addRoom(Room room);

    boolean updateRoomImagePath(int roomid);
}
