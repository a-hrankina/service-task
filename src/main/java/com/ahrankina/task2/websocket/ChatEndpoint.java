package com.ahrankina.task2.websocket;

import com.ahrankina.task2.websocket.model.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private static final Map<String, AtomicInteger> map = new ConcurrentHashMap<>();
    private Session session;
    private static Map<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        users.put(session.getId(), username);
        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected!");
        broadcast(message);
    }

    private String getRequestsNumberByWord(String word) {
        AtomicInteger value = map.get(word);
        if (value == null) {
            value = new AtomicInteger(1);
        } else {
            value.incrementAndGet();
        }
        map.putIfAbsent(word, value);
        return new StringBuffer("Requests Number by word \"")
                .append(word)
                .append("\" was found  ")
                .append(value.get())
                .append(" time(s)").toString();
    }

    @OnMessage
    public void onMessage(Session session, Message clientMessage) {
        Message message = new Message();
        message.setFrom("Server");
        message.setTo(clientMessage.getFrom());
        message.setContent((getRequestsNumberByWord(clientMessage.getContent().toLowerCase())));
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    private void broadcast(Message message) {
        synchronized (this) {
            try {
                this.session.getBasicRemote()
                        .sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }

}