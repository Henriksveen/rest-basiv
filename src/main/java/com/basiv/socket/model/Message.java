/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.model;

import javax.json.JsonObject;



/**
 * 
 * @author Ivar Østby
 */
public class Message {
    String id;
    String sentAt;
    String author;
    String rank;
    String text;
    JsonObject thread;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonObject getThread() {
        return thread;
    }

    public void setThread(JsonObject thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", sentAt=" + sentAt + ", author=" + author + ", text=" + text + ", thread=" + thread + '}';
    }
    
    
    

}
