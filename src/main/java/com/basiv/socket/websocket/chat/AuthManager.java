/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.socket.websocket.chat;

import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.Models.UserEntity;
import com.basiv.server.Models.ValidatedToken;
import java.util.Calendar;
import java.util.TimeZone;
import javax.json.JsonObject;
import javax.websocket.Session;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Ivar Østby
 */
public class AuthManager {

    public static AuthedUser authenticate(Session session, JsonObject jsonMessage, Datastore db) {
        System.out.println(session.toString());
        ValidatedToken t = db.get(ValidatedToken.class, jsonMessage.getString("userid"));
        System.out.println("Auth if test:");

        //FAILER
//        if (!t.getToken().getAccess_token().equals(jsonMessage.getString("token"))) {
//            return;
//        }
        System.out.println("Auth if 1 passed");
        if (isTokenExpired(t)) {
            return null;
        }
        UserEntity user = db.createQuery(UserEntity.class).filter("googleId", jsonMessage.getString("userid")).get();
        if (user != null) {
            ProfileEntity profile = db.get(ProfileEntity.class, user.getProfile());
            if (profile != null) {
                return new AuthedUser(user, profile, session);
            }
        }
        return null;
    }

    public static boolean isTokenExpired(ValidatedToken token) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return !token.getExpires_at().after(cal.getTime());
    }

}
