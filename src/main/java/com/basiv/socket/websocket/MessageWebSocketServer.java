/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.basiv.socket.model.Message;

/**
 * Deprecated
 * @author Ivar Østby
 */
//@ApplicationScoped
//@ServerEndpoint("/chat")

public class MessageWebSocketServer {

    @Inject
    private MessageSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        System.out.println("NY TILKOBLING!");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(MessageWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println(message);
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            System.out.println(jsonMessage);
            if ("add".equals(jsonMessage.getString("action"))) {
                //
                Message m = new Message();
                m.setId(jsonMessage.getString("id"));
                if (!jsonMessage.isNull("author")) {

                    m.setAuthor(jsonMessage.getJsonObject("author").getString("nickname"));
                }

                m.setSentAt(jsonMessage.getString("sentAt"));
                m.setText(jsonMessage.getString("text"));

                m.setThread(jsonMessage.getJsonObject("thread"));
                //System.out.println(jsonMessage.getJsonObject("thread"));
                sessionHandler.addMessage(m);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeMessage(id);
            }

            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.toggleMessage(id);
            }
        }
    }
}
