package com.example.auth.demo.entity.common;


import com.example.auth.demo.JSONApi.RoomApi;
import lombok.Data;

@Data
public class Room {
    private int room_id = 0;
    private String name;
    private String imagepath;
    private float area;
    private int min_people;
    private int max_people;
    private String equipment;
    private String place;
    private String label;
    private int isNeedPeopleCheck;

    public Room(RoomApi api) {
        this.name = api.getName();
        this.area = api.getArea();
        this.min_people = api.getMin_people();
        this.max_people = api.getMax_people();
        this.equipment = api.getEquipment();
        this.place = api.getPlace();
        this.label = api.getLabel();
        this.isNeedPeopleCheck = api.isNeedPeopleCheck()==true?1:0;
    }
}
