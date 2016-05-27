/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.model;

import java.util.Date;
import org.mongodb.morphia.annotations.Embedded;

/**
 * 
 * @author Ivar Østby
 */
@Embedded
public class CommentEntity {
    String matchId;
    String value;
    long commentTime;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }



    
}
