package com.example.auth.demo.service.face;


import com.arcsoft.face.FaceInfo;
import com.example.auth.demo.entity.common.ImageInfo;
import com.example.auth.demo.entity.face.FaceUserInfo;
import com.example.auth.demo.entity.face.ProcessInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FaceEngineService {



    List<FaceInfo> detectFaces(ImageInfo imageInfo);

    List<ProcessInfo> process(ImageInfo imageInfo);

    /**
     * 人脸特征
     * @param imageInfo
     * @return
     */
    byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException;

    /**
     * 人脸比对
     * @param groupId
     * @param faceFeature
     * @return
     */
    List<FaceUserInfo> compareFaceFeature(byte[] faceFeature, Integer room_id) throws InterruptedException, ExecutionException;


}
