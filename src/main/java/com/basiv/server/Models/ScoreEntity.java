package com.basiv.server.Models;

import java.util.Date;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Henriksveen
 */
public class ScoreEntity {

    private double score1 = 0;
    private double score2 = 0;
    private Date[] time; //When the score is changed

    public ScoreEntity() {
    }

    public double getScore1() {
        return score1;
    }

    public void setScore1(double score1) {
        this.score1 = score1;
    }

    public double getScore2() {
        return score2;
    }

    public void setScore2(double score2) {
        this.score2 = score2;
    }

    public Date[] getTime() {
        return time;
    }

    public void setTime(Date[] time) {
        this.time = time;
    }
}
