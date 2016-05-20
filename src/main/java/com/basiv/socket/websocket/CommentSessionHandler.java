/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import com.basiv.server.Models.EventEntities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import com.basiv.server.config.MongoDB;
import com.basiv.socket.model.Comment;
import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Models.ScoreEntity;
import java.util.Collection;
import com.basiv.socket.config.SocketConstants;
import java.util.Date;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 * DEPRECATED
 * @author Ivar Østby
 */
//@ApplicationScoped
public class CommentSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Set<Comment> messages = new HashSet<>();
    private final Map<String, ArrayList<Session>> mapSession = new HashMap();
    private final Map<String, ArrayList<Comment>> mapComment = new HashMap();

    private final Datastore db = MongoDB.instance().getDatabase();
    private boolean preload = false;

    //TODO should be called on inject
    public void preLoadMatches() {
 
        List<MatchEntity> list = db.find(MatchEntity.class).asList();
        for (MatchEntity c : list) {
            mapSession.put(c.getId(), new ArrayList<Session>());
            System.out.println("-Adding match with empty sessions to list. Id: " + c.getId());
        }
        System.out.println("Preloaded keys: " + mapSession.keySet().size());

    }

    public void addSession(Session session) {
        if (!preload) {
            preLoadMatches();
            preload = true;
        }
        sessions.add(session);
//        for (Comment m : messages) {
//            JsonObject addMessage = createAddMessage(m);
//            sendToSession(session, addMessage);
//        }

    }

    public void subscribe(Session session, JsonObject json) {
        System.out.println("subscribe called, session: " + session.getId());
        if (json.containsKey(SocketConstants.COMMENT_MATCHID)) {
            System.out.println("if1 passed");
            if (mapSession.containsKey(json.getString(SocketConstants.COMMENT_MATCHID))) {
                System.out.println("if2 passed");
                System.out.println("User subscribed to Match: "
                        + json.getString(SocketConstants.COMMENT_MATCHID));
                mapSession.get(json.getString(SocketConstants.COMMENT_MATCHID)).add(session);
                for (Session s : mapSession.get(json.getString(SocketConstants.COMMENT_MATCHID))) {
                    System.out.println("session to map: " + session.getId());
                }
            }
        }

    }

    public void unsubscribe(Session session, JsonObject json) {
        if (json.containsKey(SocketConstants.COMMENT_MATCHID)) {
            System.out.println("UNSUB: " + json.getString(SocketConstants.COMMENT_MATCHID));
            if (mapSession.containsKey(json.getString(SocketConstants.COMMENT_MATCHID))) {
                if (json.getString(SocketConstants.COMMENT_MATCHID).equals("*")) {
                    unsubscribeHard(session);
                } else {
                    mapSession.get(json.getString(SocketConstants.COMMENT_MATCHID)).remove(session);
                }
            }
        }
    }

    //TODO Rufffff code

    private void unsubscribeHard(Session session) {
        System.out.println("Unsubscribe HARD called");
        Collection<ArrayList<Session>> col = mapSession.values();
        for (ArrayList<Session> col1 : col) {
            for (Session col11 : col1) {
                if (col11.getId().equals(session.getId()));
                col1.remove(col11);
            }
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        //TODO sjekk om denne slettes totalt nå
    }

    public List getMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessage(Comment message) {
//        messages.add(message);
//        messageId++;
//        JsonObject addMessage = createAddMessage(message);
//        sendToAllConnectedSessions(addMessage);
    }

    private Comment getMessageById(String id) {
//        for (Comment m : messages) {
//            if (m.getMatchId() == id) {
//                return m;
//            }
//        }
        return null;
    }

    private JsonObject buildComment(Comment m, String action) {
        JsonProvider provider = JsonProvider.provider();
        JsonObjectBuilder addMessage = provider.createObjectBuilder()
                .add(SocketConstants.ACTION, action)
                .add(SocketConstants.COMMENT_MATCHID, m.getMatchId())
                .add(SocketConstants.COMMENT_TEXT, m.getValue());
//                .add(SocketConstants.COMMENT_TAG, m.getTag());
        if (m.getTag() != null) {
            addMessage.add(SocketConstants.COMMENT_TAG, m.getTag());
        }
        if (m.getImgSrc() != null) {
            addMessage.add(SocketConstants.COMMENT_IMAGESOURCE, m.getImgSrc());
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
            Logger.getLogger(CommentSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex){
            Logger.getLogger(CommentSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void authenticate(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void create(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void activate(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void deactivate(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void newcomment(Session session, JsonObject jsonMessage) {
        System.out.println("newcomment called"
                + jsonMessage.getString(SocketConstants.COMMENT_MATCHID));
        if (mapSession.containsKey(jsonMessage.getString(SocketConstants.COMMENT_MATCHID))) {
            System.out.println("if1 ok");
            Comment comment = new Comment();
            comment.setMatchId(jsonMessage.getString(SocketConstants.COMMENT_MATCHID));
            comment.setValue(jsonMessage.getString(SocketConstants.COMMENT_TEXT));
//            comment.setTag(jsonMessage.getString(SocketConstants.COMMENT_TAG));
            if (jsonMessage.containsKey(SocketConstants.COMMENT_IMAGESOURCE)) {
                if (!jsonMessage.isNull(SocketConstants.COMMENT_IMAGESOURCE)) {
                    comment.setImgSrc(jsonMessage.getString("imgSrc"));
                }
            }
            if (mapComment.containsKey(comment.getMatchId())) {
                mapComment.get(comment.getMatchId()).add(comment);
            } else {
                mapComment.put(comment.getMatchId(), new ArrayList<Comment>());
                mapComment.get(comment.getMatchId()).add(comment);
            }

            sendToSubscribers(comment, SocketConstants.ACTION_NEWCOMMENT);

        }
    }

    void updatecomment(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void deletecomment(Session session, JsonObject jsonMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sendToSubscribers(Comment comment, String action) {
        JsonObject json = buildComment(comment, action);
        System.out.println("Sending to subbers " + json.toString());
        for (Session session : mapSession.get(comment.getMatchId())) {
            System.out.println("Sending to sub/sessionid: " + session.getId());
            sendToSession(session, json);
        }
    }

    void updatescore(Session session, JsonObject jsonMessage) {
        System.out.println("updatescore called on matchid: "
                + jsonMessage.getString(SocketConstants.MATCHID));
        if (mapSession.containsKey(jsonMessage.getString(SocketConstants.MATCHID))) {
            System.out.println("if1 ok");
            ScoreEntity e = new ScoreEntity();
            
            e.setScore1(jsonMessage.getJsonObject("score").getInt(SocketConstants.SCORE_TEAM1));
            e.setScore2(jsonMessage.getJsonObject("score").getInt(SocketConstants.SCORE_TEAM2));
            e.setTime(new Date());

            sendScoreToSubscribers(e, jsonMessage.getString(SocketConstants.MATCHID),SocketConstants.ACTION_UPDATESCORE);
            
        }
    }
    
    
    private void storeScore(String matchId, ScoreEntity s){
// Better impl, but -> SEVERE:   org.bson.codecs.configuration.CodecConfigurationException: Can't find a codec for class com.basiv.server.Models.ScoreEntity.
//        BasicDBObject match = new BasicDBObject("_id", matchId);
//        BasicDBObject score = new BasicDBObject("$push", new BasicDBObject("score", s));
//        DBCollection col = db.getCollection(MatchEntity.class);
//        col.update(match, score);
        System.out.println(s.toString()); 
        Query<MatchEntity> updateQuery = db.createQuery(MatchEntity.class).field("_id").equal(matchId);
        UpdateOperations<MatchEntity> update = db.createUpdateOperations(MatchEntity.class).add("score", s);        
        db.update(updateQuery, update);
//        db.update(matchId, m);
    }

    private JsonObject buildScoreResponse(ScoreEntity e, String action) {
        JsonProvider provider = JsonProvider.provider();
        JsonObjectBuilder addMessage = provider.createObjectBuilder()
                .add(SocketConstants.ACTION, action)
                .add(SocketConstants.SCORE_TEAM1, e.getScore1())
                .add(SocketConstants.SCORE_TEAM2, e.getScore2())
                .add(SocketConstants.SCORE_DATE, e.getTime().toString());
        return addMessage.build();
    }

    private void sendScoreToSubscribers(ScoreEntity e,String matchId, String action) {
        JsonObject json = buildScoreResponse(e, action);
        System.out.println("Sending to subbers " + json.toString());
        for (Session session : mapSession.get(matchId)) {
            System.out.println("Sending to sub/sessionid: " + session.getId());
            sendToSession(session, json);
        }
        storeScore(matchId, e);
    }
}
