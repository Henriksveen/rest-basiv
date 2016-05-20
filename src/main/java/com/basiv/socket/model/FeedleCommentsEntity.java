/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.model;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 
 * @author Ivar Østby
 */
@Entity("comments")
public class FeedleCommentsEntity {
    @Id
    String matchId;
    @Embedded
    List<Comment> comments;

    public FeedleCommentsEntity(String matchId) {
        this.matchId = matchId;
        this.comments = new ArrayList<>();
    }
    public FeedleCommentsEntity() {

    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    

}
