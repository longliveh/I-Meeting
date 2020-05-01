package com.example.auth.demo.JSONApi;

import lombok.Data;

import java.util.Date;

@Data
public class BookRoomApi {
    private int room_id;
    private Date startdatetime;
    private Date enddatetime;
    private String theme;
    private String remarks;
}
