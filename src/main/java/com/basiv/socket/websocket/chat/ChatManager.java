/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.websocket.chat;

import com.basiv.server.config.MongoDB;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.Session;
import org.mongodb.morphia.Datastore;

/**
 * 
 * @author Ivar Østby
 */
@ApplicationScoped
public class ChatManager {
    
//    @Inject
//    private MongoDatabase mongoImpl;
//    private Datastore db = null;
    private Datastore db = MongoDB.instance().getDatabase();
    private final Map<String, ChatRoom> rooms;
    private final Map<String, AuthedUser> authedUsers;
    private final List<Session> sessions;
    
    public ChatManager(){
//        db = mongoImpl.getDatabase();
        rooms = new HashMap<>();
        authedUsers = new HashMap<>();
        sessions = new ArrayList<>();
    }
    
    void addSession(Session session) {
        sessions.add(session);
    }

    void removeSession(Session session) {
        for(Object obj: rooms.values().toArray()){
            System.out.println("-- REMOVING CHATROOM: IF TEST -- ");
            if(obj instanceof ChatRoom){
                System.out.println("-- REMOVING CHATROOM: IF TEST PASSED -- ");
                ChatRoom room = (ChatRoom) obj;
                room.removeSession(session);
            }
        }
        sessions.remove(session);
    }

    void onMessage(Session session, String data) {
        try (JsonReader reader = Json.createReader(new StringReader(data))) {
            JsonObject jsonMessage = reader.readObject();
            System.out.println(data);
            if (jsonMessage.containsKey("action")) {
                String action = jsonMessage.getString("action");
                switch (action) {
                    case "subscribe":
                        subscribe(session, jsonMessage);
                        break;
                    case "unsubscribe":
                        unsubscribe(session, jsonMessage);
                        break;
                    case "authenticate":
                        authenticate(session, jsonMessage);
                        break;
                    case "newmessage":
                        newcomment(session, jsonMessage);
                        break;
                    default:
                        return;
                }
            }
        }
    }

    private void subscribe(Session session, JsonObject jsonMessage) {
       if(rooms.containsKey(jsonMessage.getString("id"))){
           rooms.get(jsonMessage.getString("id")).addSession(session);
       }else{
           rooms.put(jsonMessage.getString("id"), newRoom(jsonMessage.getString("id")));
           rooms.get(jsonMessage.getString("id")).addSession(session);
       }
    }

    private void unsubscribe(Session session, JsonObject jsonMessage) {
        if(rooms.containsKey(jsonMessage.getString("id"))){
           rooms.get(jsonMessage.getString("id")).removeSession(session);
       }
    }

    private void authenticate(Session session, JsonObject jsonMessage) {
        AuthedUser auth = AuthManager.authenticate(session, jsonMessage, db);
        if(auth != null){
            authedUsers.put(auth.getSession().getId(), auth);
        }
        //return auth error
    }

    private void newcomment(Session session, JsonObject jsonMessage) {
        if(rooms.containsKey(jsonMessage.getString("id"))){
            rooms.get(jsonMessage.getString("id")).newMessage(session, jsonMessage);
        }
    }
    
    private ChatRoom newRoom(String matchId){
        return new ChatRoom(matchId, db, authedUsers);
    }
    

}
