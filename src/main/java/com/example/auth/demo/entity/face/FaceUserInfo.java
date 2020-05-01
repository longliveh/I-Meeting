package com.example.auth.demo.entity.face;


import lombok.Data;

@Data
public class FaceUserInfo {

    private Integer user_id;
    private String name;
    private Integer similarValue;
    private byte[] faceFeature;
}
