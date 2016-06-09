/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.websocket;

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
@ServerEndpoint("/feed")
public class FeedleGatekeeper {
    
    @Inject
    private FeedleManager manager;

    @OnOpen
    public void open(Session session) {
        manager.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        manager.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(FeedleGatekeeper.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String data, Session session) {
        manager.onMessage(session, data);
    }

}
