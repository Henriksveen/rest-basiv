/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.websocket.chat;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
@ServerEndpoint("/chat")
public class ChatGateKeeper {
    
    @Inject
    private ChatManager manager;

    @OnOpen
    public void open(Session session) {
        System.out.println("New Connection to chat. Id: " + session.getId());
        manager.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        System.out.println("-- CLOSING CONNECTION -- ");
        manager.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("-- ERROR THROWN -- ");
        Logger.getLogger(ChatGateKeeper.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String data, Session session) {
        System.out.println(data);
        manager.onMessage(session, data);
    }

}