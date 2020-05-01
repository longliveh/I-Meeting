package com.example.auth.demo.service.room.impl;

import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Reservation_;
import com.example.auth.demo.entity.face.FaceUserInfo;
import com.example.auth.demo.entity.face.UserFaceInfo;
import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.mapper.room.RoomMapper;
import com.example.auth.demo.service.face.FaceEngineService;
import com.example.auth.demo.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    private RoomMapper roomMapper;

    @Override
    public UserDetail getUserByFace(byte[] facefeature, int roomid) {
        //faceEngineService.compareFaceFeature()

        return null;
    }

    @Override
    public List<UserFaceInfo> getCunnentParticipant(Integer room_id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();
        //System.out.println("当前时间：" + sdf.format(now));
        Date nowafter10min = new Date(now.getTime() + 600000);
        String liststr = roomMapper.getCurrentMeettingParticipant(room_id, nowafter10min);
        List<UserFaceInfo> list = new ArrayList<>();
        int[] arrayid = liststr2array(liststr);
        if (arrayid != null) {
            for (int i = 0; i < arrayid.length; i++) {
                UserFaceInfo userFaceInfo = roomMapper.getFaceFeatureById(arrayid[i]);
                if (userFaceInfo == null)
                    continue;
                userFaceInfo.setUserid(arrayid[i]);
                list.add(userFaceInfo);
            }
            return list;
        }
        return null;
    }

    @Override
    public List<Reservation_> gettodayMeettingInfo(Integer room_id) {
        Date now = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String today = sdf0.format(now);
        String today_s = today + " 00:00:01";
        String today_e = today + " 23:59:59";
        try {
            Date date_s = sdf1.parse(today_s);
            Date date_e = sdf1.parse(today_e);
            List<Reservation_> list = roomMapper.getMeeting(room_id, date_s, date_e);
            for (Reservation_ item : list) {
                item.setParticipant("" + item.getParticipant().split(",").length);
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int[] liststr2array(String liststr) {
        if (liststr != null) {
            liststr = liststr.replace(" ", "");
            String[] str = liststr.substring(1, liststr.length() - 1).split(",");
            int[] array = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                array[i] = Integer.parseInt(str[i]);
            }
            return array;
        }
        return null;
    }

}
