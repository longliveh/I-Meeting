package com.example.auth.demo.entity.common;

import com.example.auth.demo.JSONApi.BookRoomApi;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Reservation_ {
    private int default_id;
    private int room_id;
    private long originator;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdatetime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddatetime;
    private String participant;
    private String theme;
    private String remarks;
    private int state;          //0待添加参会人员，1成功添加参会人员，2预定成功，3预定失败，4取消预定
    private Date createtime;

    public Reservation_(Reservation reservation) {
        this.default_id = reservation.getDefault_id();
        this.room_id = reservation.getRoom_id();
        this.originator = reservation.getOriginator();
        this.startdatetime = reservation.getStartdatetime();
        this.enddatetime = reservation.getEnddatetime();
        this.participant = reservation.getParticipant();
        this.theme = reservation.getTheme();
        this.remarks = reservation.getRemarks();
        this.state = reservation.getState();
        this.createtime = reservation.getCreatetime();
    }
}
