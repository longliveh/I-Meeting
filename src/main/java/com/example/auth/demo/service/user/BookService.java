package com.example.auth.demo.service.user;


import com.example.auth.demo.JSONApi.QueryApi;
import com.example.auth.demo.JSONApi.RoomDetailApi;
import com.example.auth.demo.cache.msg.Message;
import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.user.IdAndName;
import com.example.auth.demo.entity.user.UserDetail;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface BookService {
    boolean bookroom(Reservation reservation);
    boolean addOfflineMsg(Reservation reservation,int userid);
    boolean removeOfflineMsg(Integer target_userid,Integer meeting_id);
    Reservation selectReservation(int id);
    Room selectRoom(int id);
    boolean addparticipant(List<Integer> participants,Reservation reservation);
    List<IdAndName> selectIdAndNameByDep(Integer depid);
    boolean[] getTimeStamp(Integer room_id,Date date);
    List<Room> getSuitableRoom(QueryApi queryApi);
    List<Room> getRoomVagueByName(String name);
    List<Department> getdepartment();
    List<Message> getAllMeeting(String user_id);
    String getNameByID(Long id);
}
