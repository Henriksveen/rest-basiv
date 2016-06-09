/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.websocket.chat;

import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.Models.UserEntity;
import javax.websocket.Session;

/**
 * 
 * @author Ivar Østby
 */
public class AuthedUser {
    
    final UserEntity user;
    final ProfileEntity profile;
    final Session session;

    public AuthedUser(UserEntity user, ProfileEntity profile, Session session) {
        this.user = user;
        this.profile = profile;
        this.session = session;
    }

    public UserEntity getUser() {
        return user;
    }

    public ProfileEntity getProfile() {
        return profile;
    }

    public Session getSession() {
        return session;
    }
    
    
}
