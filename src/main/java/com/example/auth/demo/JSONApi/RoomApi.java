package com.example.auth.demo.JSONApi;

import lombok.Data;

@Data
public class RoomApi {
    private String name;
    private float area;
    private int min_people;
    private int max_people;
    private String equipment;
    private String place;
    private String label;
    private boolean isNeedPeopleCheck;
}
