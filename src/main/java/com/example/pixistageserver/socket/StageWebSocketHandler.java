package com.example.pixistageserver.socket;

import com.alibaba.fastjson.JSON;
import com.example.pixistageserver.entity.Message;
import com.example.pixistageserver.entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class StageWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final Map<String, Player> players = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("websocket成功建立连接,sessionId:{}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        if(players.containsKey(session.getId())){
            players.remove(session.getId());
        }
        session.close();
        log.info("websocket成功断开连接,sessionId:{}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message mess = JSON.parseObject(message.getPayload(), Message.class);
        if (mess.getType() == null) {
            session.sendMessage(Message.message("未知的消息类型", 20));
            return;
        }
        for (WebSocketSession s : sessions) {
            if (s == session) {
                switch (mess.getType()) {
                    case 4:
                        mess.setType(14);
                        mess.setPlayers(players.values());
                        s.sendMessage(mess.toTextMessage());
                        Player player = JSON.parseObject(JSON.toJSONString(mess), Player.class);
                        players.put(s.getId(), player);
                        break;
                    case 5:
                        players.remove(s.getId());
                        break;
                    case 6:
                        mess.setType(16);
                        s.sendMessage(mess.toTextMessage());
                        break;
                }
                continue;
            }
            s.sendMessage(message);
        }
    }
}