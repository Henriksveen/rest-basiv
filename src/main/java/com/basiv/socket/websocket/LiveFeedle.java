/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket;

import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Models.ScoreEntity;
import com.basiv.server.Models.UserEntity;
import com.basiv.socket.config.SocketConstants;
import com.basiv.socket.model.Comment;
import com.basiv.socket.model.CommentEntity;
import com.basiv.socket.model.FeedleCommentsEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 *
 * @author Ivar Østby
 */
@ApplicationScoped
public class LiveFeedle {

    String id;
    WaitingPool pool;
    List<Session> sessions;
    Datastore db;
    List<Session> creators;
    boolean isLive;

    public LiveFeedle(String id, WaitingPool pool, Datastore db) {
        this.id = id;
        this.pool = pool;
        this.db = db;
        sessions = new ArrayList<>();
        creators = new ArrayList<>();
        isLive = false;

    }

    public void setUpFeedle() {
        sessions.add(pool.getSessions());
    }

    //Is this needed?
    public void addCreator(Session e) {
        creators.add(e);
    }

    public void removeCreator(Session e) {
        creators.remove(e);
    }

    public void addSubscriber(Session session) {
        System.out.println("addSub; adding session");
        if (!sessions.contains(session)) {
            sessions.add(session);
            System.out.println("addSub; adding was ok");
        }
    }

    public void removeSubscriber(Session session) {
        if (!sessions.remove(session)) {
            Logger.getLogger(LiveFeedle.class.getName()).log(Level.WARNING, null,
                    "Session id " + session.getId()
                    + " failed to be unsubscribed from Feedle id"
                    + id + ". Already removed?");
        }
    }

    private CommentEntity jsonToEntity(JsonObject jsonMessage) {
        try {
            CommentEntity entity = new CommentEntity();
            entity.setMatchId(jsonMessage.getString(SocketConstants.COMMENT_MATCHID));
            entity.setValue(jsonMessage.getString(SocketConstants.COMMENT_TEXT));
            return entity;
        } catch (NullPointerException | ClassCastException ex) {
            Logger.getLogger(LiveFeedle.class.getName()).log(Level.WARNING, null, ex);
            return null;
        }
    }

    public boolean isCreator(Session s) {
        for (Session creator : creators) {
            if (creator.getId().equals(s.getId())) {
                System.out.println("Creator found!! Matched id: " + creator.getId());
                return true;
            }
        }
        return false;
    }

    public void newMessage(Session session, JsonObject jsonMessage) {

        System.out.println("New Feedle comment received on feedle id: " + id);
//        checks if creator is already authenticated
        if (!isCreator(session)) {
            return;
        }
        System.out.println("newmessage; creator found");

        CommentEntity entity = jsonToEntity(jsonMessage);
        if (entity == null) {
            return;
        }
        entity.setCommentTime(new Date().getTime());
        System.out.println("newmessage; sedning and storing");
        sendToSubscribers(buildJsonComment(entity, SocketConstants.ACTION_NEWCOMMENT));
        sendToDatabase(entity);

    }

    private void sendToDatabase(CommentEntity entity) {
        if (db.get(FeedleCommentsEntity.class, id) == null) {
            db.save(new FeedleCommentsEntity(id));
        }
        Query<FeedleCommentsEntity> updateQuery = db.createQuery(FeedleCommentsEntity.class).field("_id").equal(id);
        UpdateOperations<FeedleCommentsEntity> update = db.createUpdateOperations(FeedleCommentsEntity.class).add("comments", entity);
        db.update(updateQuery, update);
    } 

    //Create a json object. Not dynamic pt. 
    private JsonObject buildJsonComment(CommentEntity c, String action) {
        JsonProvider provider = JsonProvider.provider();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
        
        JsonObjectBuilder addMessage = provider.createObjectBuilder()
                .add(SocketConstants.ACTION, action)
                .add(SocketConstants.COMMENT_MATCHID, c.getMatchId())
                .add(SocketConstants.COMMENT_TEXT, c.getValue())
//                .add("commentTime", formatter.format(c.getCommentTime()));
                .add("commentTime", c.getCommentTime());
        return addMessage.build();
    }

    //send json to a single session
    private void sendToSession(Session session, JsonObject message) {
        System.out.println(message.toString());
        try {
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
        System.out.println("Sending to subbers " + json.toString());

        for (Session session : sessions) {
            System.out.println("Sending to sessionid: " + session.getId());
            sendToSession(session, json);
        }
    }

    //removes and closes session
    private void closeSession(Session session) {
        try {
            sessions.remove(session);
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(LiveFeedle.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public void updateScore(Session session, JsonObject jsonMessage) {
        if (!isCreator(session)) {
            return;
        }

        System.out.println("updatescore called on feedle id: " + id);
        ScoreEntity e = new ScoreEntity();
        e.setScore1(jsonMessage.getJsonObject("score").getInt(SocketConstants.SCORE_TEAM1));
        e.setScore2(jsonMessage.getJsonObject("score").getInt(SocketConstants.SCORE_TEAM2));
        e.setTime(new Date());
        sendToSubscribers(buildJsonScore(e, SocketConstants.ACTION_NEWCOMMENT));
        storeScore(e);

    }

    private void storeScore(ScoreEntity s) {
        Query<MatchEntity> updateQuery = db.createQuery(MatchEntity.class).field("_id").equal(id);
        UpdateOperations<MatchEntity> update = db.createUpdateOperations(MatchEntity.class).add("score", s);
        db.update(updateQuery, update);
    }

    private JsonObject buildJsonScore(ScoreEntity e, String action) {
        JsonProvider provider = JsonProvider.provider();
        JsonObjectBuilder addMessage = provider.createObjectBuilder()
                .add(SocketConstants.ACTION, action)
                .add(SocketConstants.SCORE_TEAM1, e.getScore1())
                .add(SocketConstants.SCORE_TEAM2, e.getScore2())
                .add(SocketConstants.SCORE_DATE, e.getTime().toString());
        return addMessage.build();
    }

    String getId() {
        return id;
    }

    void setLive(boolean live) {
        isLive = live;
        if (!live) {
            //TODO close sessions?
        }
    }

    boolean isLive() {
        return isLive;
    }

    List<Session> getCreators() {
        return creators;
    }
}
