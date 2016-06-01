/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket.chat;

import com.basiv.socket.websocket.CommentSessionHandler;
import com.basiv.socket.websocket.LiveFeedle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Ivar Østby
 */
public class ChatRoom {

    String id;
    List<Session> sessions;
    Map<String, AuthedUser> authedUsers; //shared
    Datastore db;
    List<Session> creators;

    public ChatRoom(String id, Datastore db, Map<String, AuthedUser> authedUsers) {
        this.sessions = new ArrayList<>();
        this.authedUsers = authedUsers;
        this.id = id;
        this.db = db;
        creators = new ArrayList<>();
    }

    public void addSession(Session session) {
        sessions.add(session);
        //Checks if they are the creator. Rough...
        if (authedUsers.containsKey(session.getId())) {
            for (String match : authedUsers.get(session.getId()).getUser().getCreatedMatches()) {
                if (id.equals(match)) {
                    creators.add(session);
                }
            }
        }
    }

    void removeSession(Session session) {
        sessions.remove(session);
    }

    void newMessage(Session session, JsonObject jsonMessage) {
        System.out.println("Building json to send");
        JsonProvider provider = JsonProvider.provider();
        JsonObjectBuilder message = provider.createObjectBuilder()
                .add("id", id)
                .add("sentAt", new Date().getTime())
                .add("text", jsonMessage.getString("text"));
        AuthedUser user = authedUsers.get(session.getId());
        if(user != null){
            message.add("author", user.profile.getName());
            if(creators.contains(session)){
                message.add("rank", "Creator");
            }else{
                message.add("rank", user.getProfile().getRank());
            }
        }else{
            message.add("author", "Anonymous");
        }        
        sendToSubscribers(message.build());
    }   


    //send json to a single session
    private void sendToSession(Session session, JsonObject message) {
        try {
            if(session.isOpen())
            session.getBasicRemote().sendText(message.toString());
            
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(CommentSessionHandler.class.getName()).log(Level.WARNING, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(CommentSessionHandler.class.getName()).log(Level.WARNING, null, ex);

            //also remove faulty/closed connections
            closeSession(session);
        }
    }

    //send json to a all subscribed sessions 
    private void sendToSubscribers(JsonObject json) {
        System.out.println("Sending to chat message subbers: " + json.toString());
        for (Session session : sessions) {
            System.out.println("Sending to sessionid: " + session.getId());
            sendToSession(session, json);
        }
    }

    //removes and closes session
    private void closeSession(Session session) {
        try {
            sessions.remove(session);
            creators.remove(session);
            authedUsers.remove(session.getId());
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(LiveFeedle.class.getName()).log(Level.WARNING, null, ex);
        }
    }

}
