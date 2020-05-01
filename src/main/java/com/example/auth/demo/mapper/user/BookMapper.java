package com.example.auth.demo.mapper.user;

import com.example.auth.demo.cache.msg.Message;
import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.user.IdAndName;
import com.example.auth.demo.entity.user.UserDetail;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookMapper {

    /**
     * 查namebyid
     * @param user_id
     * @return
     */
    String getName(@Param("user_id")Long user_id);

    /**
     * 预定会议室
     * @param reservation
     * @return
     */
    int bookroom(Reservation reservation);


    /**
     * 根据预定号查询预定会议室信息
     * @param default_id
     * @return
     */
    Reservation selectResById(@Param("default_id") int default_id);

    /**
     * 根据会议室编号查询会议室
     * @param room_id
     * @return
     */
    Room selectRoomById(@Param("room_id") int room_id);

    /**
     * 添加参会人员
     * @param participants
     * @return
     */
    Integer addparticipants(@Param("participants") String participants,@Param("default_id") Integer default_id);

    /**
     * 根据部门查询id和姓名
     * @param department_id
     * @return
     */
    List<IdAndName> selectIdAndNameByDep(@Param("department_id")Integer department_id);

    /**
     * 根据会议室名称模糊查询会议室
     * @param str
     * @return
     */
    List<Room> getRoomVagueByName(@Param("name")String str);

    /**
     * 根据日期，roomid查询会议室使用情况
     * @param room_id
     * @param day_0
     * @param day_23
     * @return
     */
    List<Reservation> getTimeStamp(@Param("room_id")Integer room_id, @Param("datestart")Date day_0, @Param("dateend")Date day_23);

    /**
     * 根据参数查询会议室list
     * @param min
     * @param max
     * @param device
     * @param type
     * @return
     */
    List<Room> getSuitableRoom(@Param("min")Integer min, @Param("max")Integer max, @Param("devices")List<String> devices, @Param("type")String type);


    /**
     * 查询部门列表
     * @return
     */
    List<Department> getdepartment();




}
