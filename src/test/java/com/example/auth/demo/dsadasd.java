package com.example.auth.demo;

import cn.hutool.json.JSONArray;

import com.example.auth.demo.cache.MessageCache;
import com.example.auth.demo.cache.msg.Message;
import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.face.UserFaceInfo;
import com.example.auth.demo.entity.user.IdAndName;
import com.example.auth.demo.mapper.room.RoomMapper;
import com.example.auth.demo.mapper.user.BookMapper;
import com.example.auth.demo.mapper.user.MessageMapper;
import com.example.auth.demo.service.room.RoomService;
import com.example.auth.demo.service.user.BookService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class dsadasd {


    @Value("${filepath.roomimage}")
    private String roompath;

    @Test
    public void getFileName()

    {
        File file = new File(roompath + 123);
        String[] fileName = file.list();
        System.out.println(Arrays.asList(fileName).toString());
    }

    public static void getAllFileName(String path, ArrayList<String> fileName)

    {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null)
            fileName.addAll(Arrays.asList(names));
        for (File a : files) {
            if (a.isDirectory()) {
                getAllFileName(a.getAbsolutePath(), fileName);
            }
        }
    }

    @Autowired
    MessageCache cache;

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    @Test
    public void heh() {
        List<Message> list = new ArrayList<>();
        Message message = new Message();
        message.setReservation(bookService.selectReservation(1));
        message.setRoom(bookService.selectRoom(message.getReservation().getRoom_id()));
        list.add(message);
        JSONArray jsonArray = new JSONArray(list, true);
        System.out.println(jsonArray.toString());
    }


    @Test
    public void fsa() {
        List<IdAndName> list = bookMapper.selectIdAndNameByDep(1);
        System.out.println(list.toString());
    }

    @Autowired
    RoomMapper roomMapper;

    @Test
    public void fsajfoi() {
        String s = roomMapper.getCurrentMeettingParticipant(1, new Date());
        System.out.println(s);
    }

    @Test
    public void liststr2array() {
        String liststr = "[7, 8, 9]";
        liststr = liststr.replace(" ", "");
        String[] str = liststr.substring(1, liststr.length() - 1).split(",");
        int[] array = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            array[i] = Integer.parseInt(str[i]);
        }
    }

    @Test
    public void strdate() {
        Date now = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String today = sdf0.format(now);
        String today_s = today + " 00:00:01";
        String today_e = today + " 23:59:59";
        System.out.println(today);
        try {
            Date date_s = sdf1.parse(today_s);
            Date date_e = sdf1.parse(today_e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(today_s + "\n" + today_e);
    }

    @Autowired
    RoomService roomService;

    @Test
    public void fsaf() {
        List<Room> roomList = bookMapper.getRoomVagueByName("会议");
        System.out.println(roomList);
    }

    @Test
    public void fsda() throws ParseException {
        float[] hours = {8.5f, 9.5f, 10.5f, 11.5f, 12.5f, 13.5f, 14.5f, 15.5f, 16.5f, 17.5f};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date_s = sdf.parse("2019-03-11 09:00:00");
        Date date_e = sdf.parse("2019-03-11 12:00:00");
        int hour_s = date_s.getHours();
        int hour_e = date_e.getHours();
        System.out.println(hour_s);
        boolean[] timestamp = new boolean[10];
        for (int i = 0; i < 10; i++) {
            if (hours[i]>hour_s&&hours[i]<hour_e)
            {
                timestamp[i] = true;
            }
        }
        System.out.println(Arrays.toString(timestamp));
    }


    @Test
    public void fsadfasdd()
    {
        boolean[] re =  bookService.getTimeStamp(1,new Date());
        System.out.println(Arrays.toString(re));

    }

    @Test
    public void fsadfadsg()
    {
        List<String> des  = new ArrayList<>();
        String[] strings = {"投影仪","音响","无线麦克"};
        des.add("投影仪");
        des.add("音响");
        des.add("无线麦克");
        List<Room> list = bookMapper.getSuitableRoom(null,null,des,null);
        System.out.println(list.size());
    }

    @Test
    public void fasdfikasd()
    {
        List<Department> l =  bookService.getdepartment();
        System.out.println(13213);
    }


    @Autowired
    MessageMapper messageMapper;

    @Test
    public void fasdf()
    {
        Integer userid = 1;
        String case0 = "["+userid+"]";
        String case1 = "["+userid+",";
        String case2 = ", "+userid+",";
        String case3 = ", "+userid+"]";
        List<Message> messages = messageMapper.getAllMeeting(case0,case1,case2,case3);
        System.out.println(messages);
    }

    @Test
    public void fasfasfd() {
        List<Room> list = bookService.getSuitableRoom(null);
        for (Room room: list) {
            String[] l = room.getEquipment().substring(1,room.getEquipment().length()-1).split(",");
            String a = "";
            for (int i = 0; i < l.length; i++) {
                if (i==0)
                {
                    a = "'"+l[i]+"'";
                }
                else
                {
                    a = a+","+"'"+l[i]+"'";

                }
            }
            a = '['+a+']';
            String str = "{\n" +
                    "                        area: '"+room.getArea()+"',\n" +
                    "                        equipment: "+a+",\n" +
                    "                        label: '报告厅',\n" +
                    "                        max_people: "+room.getMax_people()+",\n" +
                    "                        min_people: "+room.getMin_people()+",\n" +
                    "                        name: '"+room.getName()+"',\n" +
                    "                        needPeopleCheck: false,\n" +
                    "                        needCheck: this.needPeopleCheck ? '是' : '否',\n" +
                    "                        place: '"+room.getPlace()+"'\n" +
                    "                    },";
            System.out.println(str);
        }
    }

    @Test
    public void fsdaf() {
        HttpServlet
    }
}
