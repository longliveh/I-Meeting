package com.example.auth.demo.service.user.impl;


import com.example.auth.demo.JSONApi.QueryApi;
import com.example.auth.demo.JSONApi.RoomDetailApi;
import com.example.auth.demo.WebSocket.WebSocketServer;
import com.example.auth.demo.cache.msg.Message;
import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.user.IdAndName;
import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.mapper.user.BookMapper;
import com.example.auth.demo.mapper.user.MessageMapper;
import com.example.auth.demo.service.user.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public String getNameByID(Long id) {
        return bookMapper.getName(id);
    }

    @Override
    public boolean bookroom(Reservation reservation) {
        return bookMapper.bookroom(reservation) > 0;
    }

    @Override
    public boolean addOfflineMsg(Reservation reservation, int userid) {
        int meettingid = reservation.getDefault_id();
        if (meettingid != 0)
            return messageMapper.addMessage(userid, meettingid) > 0;
        return false;
    }

    @Override
    public boolean removeOfflineMsg(Integer target_userid, Integer meeting_id) {
        return messageMapper.rmMessage(target_userid,meeting_id)>0;
    }

    @Override
    public Reservation selectReservation(int id) {
        return bookMapper.selectResById(id);
    }

    @Override
    public Room selectRoom(int id) {
        return bookMapper.selectRoomById(id);
    }

    @Override
    public boolean addparticipant(List<Integer> participants, Reservation reservation) {
        boolean res = bookMapper.addparticipants(participants.toString(), reservation.getDefault_id()) > 0;
        if (res) {
            try {
                Message message = new Message();
                reservation.setParticipant(participants.toString());
                message.setReservation(reservation);
                message.setRoom(bookMapper.selectRoomById(reservation.getRoom_id()));
                WebSocketServer.sendMessage(message,participants);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<IdAndName> selectIdAndNameByDep(Integer depid) {
        return bookMapper.selectIdAndNameByDep(depid);
    }

    @Override
    public boolean[] getTimeStamp(Integer room_id, Date date) {
        boolean[] timestamp = new boolean[10];
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String today = sdf0.format(date);
        String today_s = today + " 00:00:01";
        String today_e = today + " 23:59:59";
        try {
            Date date_s = sdf1.parse(today_s);
            Date date_e = sdf1.parse(today_e);
            List<Reservation> list = bookMapper.getTimeStamp(room_id, date_s, date_e);
            for (Reservation item : list) {
                float[] hours = {8.5f, 9.5f, 10.5f, 11.5f, 12.5f, 13.5f, 14.5f, 15.5f, 16.5f, 17.5f};
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date_s = item.getStartdatetime();
                date_e = item.getEnddatetime();
                int hour_s = date_s.getHours();
                int hour_e = date_e.getHours();
                System.out.println(hour_s);
                for (int i = 0; i < 10; i++) {
                    if (hours[i] > hour_s && hours[i] < hour_e) {
                        timestamp[i] = true;
                    }
                }
            }
            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Room> getSuitableRoom(QueryApi queryApi) {
        if (queryApi == null) {
            List<Room> list = bookMapper.getSuitableRoom(null, null, null, null);
            return list;
        }
        List<Room> list = bookMapper.getSuitableRoom(
                queryApi.getNum_limit() == null ? null : queryApi.getNum_limit().getMin(),
                queryApi.getNum_limit() == null ? null : queryApi.getNum_limit().getMax(),
                queryApi.getDevice(),
                queryApi.getType()
        );
        return list;
    }

    @Override
    public List<Room> getRoomVagueByName(String name) {
        List<Room> list = bookMapper.getRoomVagueByName(name);
        if (list != null && list.size() != 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<Department> getdepartment() {
        return bookMapper.getdepartment();
    }

    @Override
    public List<Message> getAllMeeting(String user_id) {
        Integer userid = 1;
        String case0 = "["+userid+"]";
        String case1 = "["+userid+",";
        String case2 = ", "+userid+",";
        String case3 = ", "+userid+"]";
        List<Message> messages = messageMapper.getAllMeeting(case0,case1,case2,case3);
        return messages;
    }


}
