package com.example.pixistageserver.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

import java.util.Collection;

@Data
public class Message {
    Integer type;
    String content;
    String id;
    Integer x;
    Integer y;
    String src;
    String nickname;
    String direction;
    Collection<Player> players;

    public static TextMessage message(String con, Integer code) {
        JSONObject object = new JSONObject();
        object.put("type", code);
        object.put("content", con);
        return new TextMessage(object.toJSONString());
    }

    public TextMessage toTextMessage() {
        return new TextMessage(JSON.toJSONString(this));
    }

}
