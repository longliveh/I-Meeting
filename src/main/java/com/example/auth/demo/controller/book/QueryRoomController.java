package com.example.auth.demo.controller.book;


import com.example.auth.demo.JSONApi.AddparticipantApi;
import com.example.auth.demo.JSONApi.BookRoomApi;
import com.example.auth.demo.JSONApi.QueryApi;
import com.example.auth.demo.JSONApi.RoomDetailApi;
import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.service.user.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Api(description = "会议室预定查询")
@RequestMapping("/api/v1/user")
public class QueryRoomController {

    @Autowired
    private BookService bookService;


    @PostMapping("/book")
    @ApiOperation(value = "发起预定")
    public ResultJson bookroom(@RequestBody BookRoomApi bookRoomApi, HttpServletRequest request) {
        long userid = (long) request.getAttribute("user_id");
        Reservation reservation = new Reservation(bookRoomApi, userid);
        if (bookService.bookroom(reservation)) {
            return ResultJson.ok(reservation.getDefault_id());
        }
        return ResultJson.failure(ResultCode.BOOK_FAIL, ResultCode.BOOK_FAIL.getMsg());
    }


    @GetMapping("/getdepartment")
    @ApiOperation(value = "获取部门列表")
    public ResultJson getdepartment()
    {
        return ResultJson.ok(bookService.getdepartment());
    }


    @GetMapping("/getAllMeeting")
    @ApiOperation(value = "获取部门列表")
    public ResultJson getAllMeeting(HttpServletRequest request)
    {
        String user_id = request.getParameter("user_id");
        return ResultJson.ok(bookService.getAllMeeting(user_id));
    }


    @GetMapping("/dep_user")
    public ResultJson getUser(@RequestParam("dep_id") Integer dep_id) {
        return ResultJson.ok(bookService.selectIdAndNameByDep(dep_id));
    }

    @PostMapping("/addparticipant")
    @ApiOperation(value = "添加参会人员")
    public ResultJson addparticipant(@RequestBody AddparticipantApi addparticipantApi) {
        if(addparticipantApi!=null) {
            Reservation reservation = bookService.selectReservation(addparticipantApi.getReservation_id());
            if (bookService.addparticipant(addparticipantApi.getParticipants(), reservation)) {
                return ResultJson.ok();
            }
        }
        return ResultJson.failure(ResultCode.SERVER_ERROR);
    }

    @PostMapping("/getroomdetial")
    @ApiOperation(value = "查询会议室详情和使用情况")
    public ResultJson getroomdetial(@RequestBody RoomDetailApi detailApi) {
        if (detailApi.getRoomid() != null && detailApi.getDate() != null) {
            Map map = new HashMap<>();
            Room room = bookService.selectRoom(detailApi.getRoomid());
            boolean[] timestamp = bookService.getTimeStamp(detailApi.getRoomid(), detailApi.getDate());
            map.put("room", room);
            map.put("timestamp", timestamp);
            return ResultJson.ok(map);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }


    @PostMapping("/getRoomList")
    @ApiOperation(value = "查询会议室列表")
    public ResultJson getRoomList(@RequestBody QueryApi queryApi) {
        List<Map> res_list = new ArrayList<>();
        List<Room> list = bookService.getSuitableRoom(queryApi);
        if (list != null) {
            for (Room room : list) {
                Map map = new HashMap();
                boolean[] timeStamp = bookService.getTimeStamp(room.getRoom_id(),queryApi.getQuerydate()==null?new Date():queryApi.getQuerydate());
                map.put("room",room);
                map.put("timeStamp",timeStamp);
                res_list.add(map);
            }
        }
        return ResultJson.ok(res_list);
    }

    @PostMapping("/getRoomByName")
    @ApiOperation(value = "根据名字模糊查询会议室")
    public ResultJson getRoomByName(String name)
    {
        if (name==null)
        {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }
        return ResultJson.ok(bookService.getRoomVagueByName(name));
    }



}
