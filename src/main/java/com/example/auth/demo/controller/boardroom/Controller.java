package com.example.auth.demo.controller.boardroom;


import Decoder.BASE64Decoder;
import cn.hutool.json.JSONArray;
import com.example.auth.demo.JSONApi.FeatureAndRoom;
import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Reservation_;
import com.example.auth.demo.entity.face.FaceUserInfo;
import com.example.auth.demo.service.face.FaceEngineService;
import com.example.auth.demo.service.room.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@Api(description = "会议室前端api")
@RequestMapping("/api/v1/boardroom")
public class Controller {

    @Autowired
    private FaceEngineService faceEngineService;

    @Autowired
    private RoomService roomService;

    @PostMapping(value = "/facesearch")
    @ApiOperation(value = "人脸识别", notes = "会议室人脸签到开门")
    public ResultJson login(@RequestBody FeatureAndRoom featureAndRoom) throws IOException {
        //人脸特征获取
        BASE64Decoder decoder = new BASE64Decoder();
        //byte[] bytes = decoder.decodeBuffer(bytestr);

        String bytestr = featureAndRoom.getBytestr();
        //
        //
        // System.out.println(bytestr);
        byte[] bytes = decoder.decodeBuffer(bytestr);
        if (bytes == null) {
            return ResultJson.failure(ResultCode.NO_FACE_DETECTED, "未检测出人脸");
        }
        try {
            List<FaceUserInfo> faceUserInfoList = faceEngineService.compareFaceFeature(bytes,featureAndRoom.getBoardroomid());
            if (faceUserInfoList!=null)
            {
                FaceUserInfo faceUserInfo = faceUserInfoList.get(0);
                System.out.println(faceUserInfo.toString());
                return ResultJson.ok(faceUserInfoList.get(0));
            }
            System.out.println("nununununununu");
            return ResultJson.ok(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResultJson.failure(ResultCode.SERVER_ERROR);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResultJson.failure(ResultCode.SERVER_ERROR);
        }
    }

    @PostMapping(value = "/todayMeetting")
    @ApiOperation(value = "获取当天的日程信息", notes = "参数room_id会议室编号")
    public ResultJson getmeetting(@RequestParam("room_id")Integer room_id)
    {
        List<Reservation_> list = roomService.gettodayMeettingInfo(room_id);
        if (list==null||list.size()==0)
        {
            return ResultJson.ok(null);
        }
        return ResultJson.ok(list);
    }

    @PostMapping(value = "/getQRCode")
    @ApiOperation(value = "获取会议室二维码", notes = "参数room_id会议室编号")
    public ResultJson getQRCode(@RequestParam Integer room_id)
    {
        if (room_id!=null)
        {
            String url = "http://test.icms.work/QRCode?room_id="+room_id;
            return ResultJson.ok(url);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }


    public static boolean GenerateImage(String base64str, String savepath) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空
            return false;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            //  System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            // System.out.println("开始生成图片");
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
