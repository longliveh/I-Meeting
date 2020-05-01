package com.example.auth.demo.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.auth.demo.cache.MessageCache;
import com.example.auth.demo.cache.msg.Message;
import com.example.auth.demo.entity.common.Reservation;
import com.example.auth.demo.service.user.BookService;
import com.example.auth.demo.utils.PushList;
import lombok.extern.slf4j.Slf4j;

import netscape.javascript.JSObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

@Slf4j
@ServerEndpoint(value = "/websocket/{userid}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static Map<Integer,WebSocketServer> webSocketMap = new Hashtable<>();
 
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;


    private static MessageCache messageCache;


    @Autowired
    public void setMessageCache(MessageCache messageCache) {
        WebSocketServer.messageCache = messageCache;
    }


    private static BookService bookService;


    @Autowired
    public void setBookService(BookService bookService) {
        WebSocketServer.bookService = bookService;
    }

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam("userid")Integer userid, Session session) {
        this.session = session;
        webSocketMap.put(userid,this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有"+userid+"连接加入！当前在线人数为" + getOnlineCount());
        List<Integer> messages = messageCache.getMessagelist(userid);
        if (messages!=null)
        {
            try {
                List<Message> list = new ArrayList<>();
                for (int i = 0; i < messages.size(); i++) {
                    Integer integer = messages.get(i);
                    Message message = new Message();
                    message.setReservation(bookService.selectReservation(integer));
                    message.setRoom(bookService.selectRoom(message.getReservation().getRoom_id()));
                    sendMessage(new JSONObject(message,true).toString());
                    bookService.removeOfflineMsg(userid,integer);
                    messages.remove(i);
                }
            } catch (IOException e) {
                log.error("websocket IO异常");
            }
        }
    }
    
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(this.session==null)
            return;
        System.out.println(this.session.getRequestURI().getPath().substring(11));
        webSocketMap.remove(Integer.parseInt(this.session.getRequestURI().getPath().substring(11)));  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
 
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
    	log.info("来自客户端的消息:" + message);
    }
 
	/**
	 * 
	 * @param session
	 * @param error
	 */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误:"+error.getMessage());
        //error.printStackTrace();
    }
 
 
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
 
 
    /**
     * 群发自定义消息
     * */
    public static void sendMessage(Message message,List<Integer> userids) throws Exception {
        //"发起人：陈浩源，会议主题：软件测试，参会人数：10人，会议地点：东三604"
        String text = "发起人:"+bookService.getNameByID(message.getReservation().getOriginator())
                +",会议主题:"+message.getReservation().getTheme()
                +",参会人数:"+message.getReservation().getParticipant().split(",").length
                +",会议地点:"+message.getRoom().getPlace();
        PushList.push(text);
    	log.info(null);
        for (Integer item : userids) {
            WebSocketServer webSocketServer = webSocketMap.get(item);
            log.info(webSocketMap.toString());
            try {
                if(webSocketServer==null)
                {
                    bookService.addOfflineMsg(message.getReservation(),item);
                    log.info(item+"离线,消息存入数据库");
                }
                else
                {
                    System.out.println(webSocketServer.session.isOpen());
                    webSocketServer.sendMessage(new JSONObject(message,false).toString());
                    log.info("发送消息:"+item+","+ message.getReservation().getTheme());
                }
            } catch (IOException e) {
                webSocketServer.addMessage(message.getReservation(),item);
                log.error("socket error "+item);
            }
        }
    }

    /**
     * 添加离线会议消息
     * @param reservation
     * @param userid
     * @return
     */
    public boolean addMessage(Reservation reservation,int userid)
    {
        return bookService.addOfflineMsg(reservation,userid);
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static synchronized Map<Integer,WebSocketServer> getWebSocketMap(){
        return webSocketMap;
    }
}
