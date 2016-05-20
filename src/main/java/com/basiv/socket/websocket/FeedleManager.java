/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import com.basiv.server.Models.UserEntity;
import com.basiv.server.Models.ValidatedToken;
import com.basiv.server.config.MongoDB;
import com.basiv.socket.config.SocketConstants;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FeedleManager {

    private final Map<String, LiveFeedle> feedleList;
    private final Map<String, WaitingPool> waitingPool;
    //har oversikt over alle feedles
    //kalles av gatekeeper
    //valider som creator av feedle

    //Want to inject this...
    private final Datastore db = MongoDB.instance().getDatabase();

    public FeedleManager() {
        Logger.getLogger(FeedleManager.class.getName()).log(Level.INFO, null,
                "New instance of FeedleManager started!");
        System.out.println("New instance of FeedleManager started!");
        feedleList = new HashMap();
        waitingPool = new HashMap();
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
                    case "create":
                        create(session, jsonMessage);
                        break;
                    case "activate":
                        activate(session, jsonMessage);
                        break;
                    case "deactivate":
                        deactivate(session, jsonMessage);
                        break;
                    case "newcomment":
                        newcomment(session, jsonMessage);
                        break;
                    case "updatecomment":
                        updatecomment(session, jsonMessage);
                        break;
                    case "deletecomment":
                        deletecomment(session, jsonMessage);
                        break;
                    case "updatescore":
                        updatescore(session, jsonMessage);
                        break;
                    case "add": //TODO REMOVE!
                        newcomment(session, jsonMessage);
                        break;
                    default:
                        return;
                }
            }
        }
    }

    void addSession(Session session) {
        //TODO: must be implemented to manage sessions
    }

    void removeSession(Session session) {
        //TODO: must be implemented to manage sessions
    }

    //User and token check before adding session as creator of feedle
    void authenticate(Session session, JsonObject jsonMessage) {
        System.out.println(session.toString());
        ValidatedToken t = db.get(ValidatedToken.class, jsonMessage.getString("user_id"));
        System.out.println("Auth if test:");
        
        //FAILER
//        if (!t.getToken().getAccess_token().equals(jsonMessage.getString("token"))) {
//            return;
//        }
        System.out.println("Auth if 1 passed");
        if (isTokenExpired(t)) {
            return;
        }
        UserEntity user = db.createQuery(UserEntity.class).filter("googleId", jsonMessage.getString("user_id")).get();
       
        if (user == null) {
            return;
        }
        System.out.println("Auth if 2 passed");
        String matchid = null;
        for (String feedles : user.getCreatedMatches()) {
            if (feedles.equals(jsonMessage.getString(SocketConstants.MATCHID))) {
                matchid = feedles;
                break; //break loop
            }
        }
        System.out.println("Auth if 3 passed");
        if (matchid == null) {
            return;
        }
        System.out.println("user was authed");
        if (feedleList.containsKey(SocketConstants.MATCHID)) {
            feedleList.get(SocketConstants.MATCHID).addCreator(session);
            System.out.println("Added to existing feedle");
        } else {
            LiveFeedle newFeedle = new LiveFeedle(jsonMessage.getString(SocketConstants.COMMENT_MATCHID),
                    waitingPool.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID)), db);
            newFeedle.setLive(true);
            newFeedle.addCreator(session);
            feedleList.put(newFeedle.getId(), newFeedle);
            System.out.println("New feedle setup");
        }
    }

    //Make static?
    private boolean isTokenExpired(ValidatedToken token) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return !token.getExpires_at().after(cal.getTime());
    }

    void create(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void activate(Session session, JsonObject jsonMessage) {
        if (feedleList.containsKey(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))) {
            if (feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID)).isCreator(session)) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID)).setLive(true);
            }
        }
    }

    void deactivate(Session session, JsonObject jsonMessage) {
        if (feedleList.containsKey(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))) {
            if (feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID)).isCreator(session)) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID)).setLive(false);
            }
        }
    }

    private void subscribe(Session session, JsonObject jsonMessage) {
        if (jsonMessage.containsKey(SocketConstants.COMMENT_MATCHID)) {
            //If feedle is created, send to feedle
            if (feedleList.containsKey(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))
                        .addSubscriber(session);
            } //else send to waitingpool
            else {
                addToWaitingPool(jsonMessage.getString(SocketConstants.COMMENT_MATCHID), session);
            }
        }
    }

    private void addToWaitingPool(String matchId, Session session) {

    }

    private void unsubscribe(Session session, JsonObject jsonMessage) {
        if (jsonMessage.containsKey(SocketConstants.COMMENT_MATCHID)) {
            //If feedle is created, send to feedle
            if (feedleList.containsKey(SocketConstants.COMMENT_MATCHID)) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))
                        .removeSubscriber(session);
            }
        }
    }

    private void newcomment(Session session, JsonObject jsonMessage) {
        if (jsonMessage.containsKey(SocketConstants.COMMENT_MATCHID)) {
            System.out.println("new message, with id: " + session.getId());
            //If feedle is created, send to feedle
            if (feedleList.containsKey(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))
                        .newMessage(session, jsonMessage);
                System.out.println("feedle was found");
            }
        }
    }

    private void updatecomment(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deletecomment(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updatescore(Session session, JsonObject jsonMessage) {
        if (jsonMessage.containsKey(SocketConstants.COMMENT_MATCHID)) {
            //If feedle is created, send to feedle
            if (feedleList.containsKey(SocketConstants.COMMENT_MATCHID)) {
                feedleList.get(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))
                        .updateScore(session, jsonMessage);
            }
        }
    }
}
