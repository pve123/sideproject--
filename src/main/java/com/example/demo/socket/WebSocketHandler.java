package com.example.demo.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        CLIENTS.put(session.getId(), session);
        CLIENTS.get(session.getId()).sendMessage(new TextMessage(session.getId() + "님이 채팅방에 접속하셨습니다.")); // 내 채팅방 표시
        CLIENTS.entrySet().forEach(arg -> {
            if (!arg.getKey().equals(session.getId())) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(new TextMessage(session.getId() + "님이 채팅방에 접속하셨습니다.")); // 상대방 채팅방 표시
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        CLIENTS.entrySet().forEach(arg -> {
            if (!arg.getKey().equals(session.getId())) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(new TextMessage(session.getId() + "님이 채팅방에서 퇴장하셨습니다.")); // 상대방 채팅방 표시
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        CLIENTS.remove(session.getId());

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


        String id = session.getId();  //메시지를 보낸 아이디
        CLIENTS.entrySet().forEach(arg -> {
            if (!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
