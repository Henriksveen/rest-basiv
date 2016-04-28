/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import com.basiv.socket.model.Message;

/**
 *
 * @author Ivar Østby
 */
@ApplicationScoped
public class MessageSessionHandler {

    private int messageId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Message> messages = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
//        for (Message m : messages) {
//            JsonObject addMessage = createAddMessage(m);
//            sendToSession(session, addMessage);
//        }

    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public List getMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessage(Message message) {
        messages.add(message);
        messageId++;
        JsonObject addMessage = createAddMessage(message);
        sendToAllConnectedSessions(addMessage);
    }

    public void removeMessage(int id) {
        Message message = getMessageById(id);
        if (message != null) {
            messages.remove(message);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

    public void toggleMessage(int id) {
//        JsonProvider provider = JsonProvider.provider();
//        Message message = getMessageById(id);
//        if (message != null) {
//            
//            if ("On".equals(message.getStatus())) {
//                device.setStatus("Off");
//            } else {
//                device.setStatus("On");
//            }
//            JsonObject updateDevMessage = provider.createObjectBuilder()
//                    .add("action", "toggle")
//                    .add("id", device.getId())
//                    .add("status", device.getStatus())
//                    .build();
//            sendToAllConnectedSessions(updateDevMessage);
//        }
    }

    private Message getMessageById(int id) {
//        for (Message m : messages) {
//            if (m.getId() == id) {
//                return m;
//            }
//        }
        return null;
    }

    private JsonObject createAddMessage(Message m) {
        System.out.println(m.toString());
        JsonProvider provider = JsonProvider.provider();
        JsonObjectBuilder addMessage = provider.createObjectBuilder()
                .add("id", m.getId())
                .add("sentAt", m.getSentAt())
                .add("text", m.getText())
                .add("thread", m.getThread());
        //Missing Chat Thread
        if (m.getAuthor() != null) {
            addMessage.add("author", m.getAuthor());
        }else{
            addMessage.add("author", "Anonymous");
        }

        return addMessage.build();
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(MessageSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
