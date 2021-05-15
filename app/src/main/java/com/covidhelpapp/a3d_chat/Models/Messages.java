package com.covidhelpapp.a3d_chat.Models;

public class Messages {

    private String from;
    private String to;
    private String messageId;
    private String message;

    public Messages() {
    }

    public Messages(String from, String to, String messageId, String message) {
        this.from = from;
        this.to = to;
        this.messageId = messageId;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
