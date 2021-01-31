package com.ahrankina.task2.websocket.model;

import lombok.Data;

@Data
public class Message {
    private String from;
    private String to;
    private String content;
}