/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.Models;
import java.util.Date;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * 
 * @author Ivar Østby
 */
@Entity("users")
public class UserEntity {
    @Id
    private String id;
    String googleId;
    String email;
    Date created;
    String profile;
    String[] createdEvents;
    String[] createdMatches;
    String nickname;

    public UserEntity() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String[] getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(String[] createdEvents) {
        this.createdEvents = createdEvents;
    }

    public String[] getCreatedMatches() {
        return createdMatches;
    }

    public void setCreatedMatches(String[] createdMatches) {
        this.createdMatches = createdMatches;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", googleId=" + googleId + ", email=" + email + ", created=" + created + ", createdEvents=" + createdEvents + ", createdMatches=" + createdMatches + '}';
    }
    
    

}
