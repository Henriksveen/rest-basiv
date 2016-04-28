/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import java.io.StringReader;
import java.util.UUID;
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


/**
 *
 * @author Ivar Østby
 */
@ApplicationScoped
@ServerEndpoint("/feed")

public class CommentWebSocketServer {
    
    @Inject
    private CommentSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        System.out.println("NY TILKOBLING!");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        System.out.println("CONNECTION CLOSED!");
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(CommentWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String comment, Session session) {
        System.out.println("----" + comment);
        System.out.println("session id: " + session.getId() + "----");
        try (JsonReader reader = Json.createReader(new StringReader(comment))) {
            JsonObject jsonMessage = reader.readObject();
            /*
             Bruk switch? ex "action":"subscribe", "matchId":"123123"
             */
            if (jsonMessage.containsKey("action")) {
                String action = jsonMessage.getString("action");
                switch (action) {
                    case "subscribe":
                        System.out.println("Switch Subscribe");
                        sessionHandler.subscribe(session, jsonMessage);
                        break;
                    case "unsubscribe":
                        sessionHandler.unsubscribe(session, jsonMessage);
                        break;
                    case "authenticate":
                        sessionHandler.authenticate(session, jsonMessage);
                        break;
                    case "create":
                        sessionHandler.create(session, jsonMessage);
                        break;
                    case "activate":
                        sessionHandler.activate(session, jsonMessage);
                        break;
                    case "deactivate":
                        sessionHandler.deactivate(session, jsonMessage);
                        break;
                    case "newcomment":
                        sessionHandler.newcomment(session, jsonMessage);
                        break;
                    case "updatecomment":
                        sessionHandler.updatecomment(session, jsonMessage);
                        break;
                    case "deletecomment":
                        sessionHandler.deletecomment(session, jsonMessage);
                        break;
                    case "add": //TODO REMOVE!
                    sessionHandler.newcomment(session, jsonMessage);
                    default:
                        onError(new Throwable(action + "invalid action-command from socket session id: " + session.getId()));
                }
            }

//            if ("add".equals(jsonMessage.getString("action"))) {
//                Comment m = new Comment();
//                m.setMatchId(jsonMessage.getString("matchId"));
//                m.setValue(jsonMessage.getString("value"));
//                m.setTag(jsonMessage.getString("tag"));
//                if (!jsonMessage.isNull("imgSrc")) {
//                    m.setImgSrc(jsonMessage.getString("imgSrc"));
//                }
//
//                sessionHandler.addMessage(m);
//            }
//            if ("subscribe".equals(jsonMessage.getString("action"))) {
//                sessionHandler.addSession(session);
//            }
//            if ("unsubscribe".equals(jsonMessage.getString("action"))) {
//                sessionHandler.removeSession(session);
//            }
        }
    }
}
