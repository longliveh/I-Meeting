package com.example.auth.demo.entity.common;

import com.example.auth.demo.JSONApi.BookRoomApi;
import lombok.Data;

import java.util.Date;

@Data
public class Reservation {
    private int default_id;
    private int room_id;
    private long originator;
    private Date startdatetime;
    private Date enddatetime;
    private String participant;
    private String theme;
    private String remarks;
    private int state;          //0待添加参会人员，1成功添加参会人员，2预定成功，3预定失败，4取消预定
    private Date createtime;

    public Reservation(BookRoomApi api,long userid) {
        this.room_id = api.getRoom_id();
        this.originator = userid;
        this.startdatetime = api.getStartdatetime();
        this.enddatetime = api.getEnddatetime();
        this.theme = api.getTheme();
        this.remarks = api.getRemarks();
    }


}
