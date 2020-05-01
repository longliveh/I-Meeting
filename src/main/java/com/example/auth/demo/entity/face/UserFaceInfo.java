package com.example.auth.demo.entity.face;


import com.arcsoft.face.FaceFeature;
import lombok.Data;



@Data
public class UserFaceInfo {
    private Integer userid;
    private String name;
    private FaceFeature faceFeature;
}