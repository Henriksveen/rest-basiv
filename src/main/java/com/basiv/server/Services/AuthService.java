/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.server.Services;

import com.basiv.server.Models.AuthEntity;
import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.Models.UserEntity;
import com.basiv.server.Models.ValidatedToken;
import com.basiv.server.config.MongoDB;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

/**
 *
 * @author Ivar Østby
 */
public class AuthService {

    private static final Logger LOG = Logger.getLogger(MatchService.class.getName());
    private final Datastore mongoDatastore;
    private final String googleUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
    private String CLIENTID = "1057885418811-4f04mik9v1t65af68hutbtdd3heb4eit.apps.googleusercontent.com";

    public AuthService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    /*
     Google response on OK:
     issued_to
     audience
     user_id
     scope
     expires_in
     access_type
     */
    public UserEntity authenticateToken(AuthEntity ent) throws MalformedURLException {
        try {
            System.out.println("Authenticated locally stored token on user_id: " + ent.getUser_id());
            //Kjør egen metode for dette

            if (ent.getUser_id() != null) {
                if (!isTokenExpired(ent.getUser_id())) {
                    System.out.println("TOKEN NOT EXPIRED");
                    return mongoDatastore.createQuery(UserEntity.class).filter("googleId", ent.getUser_id()).get();
                } else {
                    System.out.println("TOKEN IS EXPIRED");
                }
                //return database data instead?
                //check this
            }
            System.out.println("CHECKING GOOGLE");
            URLConnection con = new URL("https://www.googleapis.com/oauth2/v2/tokeninfo?access_token=" + ent.getAccess_token()).openConnection();
            con.setRequestProperty("Accept-Charset", "UTF-8");
            InputStream response = con.getInputStream();
            JsonObject obj;
            try (JsonReader reader = Json.createReader(response)) {
                obj = reader.readObject();
            }

            if (!obj.isNull("issued_to") && !obj.isNull("user_id")) {
                if (obj.getString("issued_to").equals(CLIENTID)) {
                    LOG.log(Level.INFO, "Token validated: {0}", obj.getString("issued_to"));
                    LOG.log(Level.INFO, "Expires in: {0}", String.valueOf(obj.getInt("expires_in")));
                    ValidatedToken token = createToken(obj.keySet().toArray(), obj);
                    token.setToken(ent);
                    Key<ValidatedToken> key = mongoDatastore.save(token);
                    LOG.log(Level.INFO, "KEY: {0}, User ID: {1}", new Object[]{key.getId(), token.getUser_id()});

                    //UserEntity user = mongoDatastore.get(UserEntity.class, obj.getString("user_id"));
                    UserEntity user = mongoDatastore.createQuery(UserEntity.class).filter("googleId", obj.getString("user_id")).get();
                    if (user == null) {
                        //TODO sjekk database om user_id finnes fra før også
                        user = new UserEntity();
                        //Oppretter profil til bruker
                        ProfileEntity profile = createProfile(ent);
                        user.setId(UUID.randomUUID().toString());
                        user.setProfile(profile.getId());
                        user.setGoogleId(obj.getString("user_id"));
                        user.setNickname(profile.getName());
                        mongoDatastore.save(user);
                        mongoDatastore.save(profile);
                    }//Mer logikk her?
                    return user;
                }
            } else {
                return null;
            }

        } catch (IOException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.WARNING, "Token rejected-> ", ex);
            return null;
        }
        return null;
    }
    
    private ProfileEntity createProfile(AuthEntity ent) {
        ProfileEntity profile = new ProfileEntity();
        try {
            profile.setId(UUID.randomUUID().toString());
            profile.setRank("Creator");
            //Connect googleapis for user info
            URLConnection con = new URL("https://www.googleapis.com/oauth2/v2/userinfo").openConnection();
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Authorization", "Bearer " + ent.getAccess_token());
            InputStream response = con.getInputStream();
            JsonObject obj;
            try (JsonReader reader = Json.createReader(response)) {
                obj = reader.readObject();
            }
            profile.setName(obj.getString("name"));
            profile.setImage(obj.getString("picture"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profile;
    }

    private boolean isTokenExpired(String user_id) {
        ValidatedToken token = mongoDatastore.get(ValidatedToken.class, user_id);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return !token.getExpires_at().after(cal.getTime());

    }

    private boolean isTokenExpired(ValidatedToken token) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return !token.getExpires_at().after(cal.getTime());

    }

    private boolean isTokenExpired() {
        return false;
    }

    private Date getExpiresAt(int time) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.SECOND, time);
        System.out.println("Token expires at:" + cal.getTime());
        return cal.getTime();
    }

    void printJson(JsonObject obj) {
        //TODO 
        Object[] set = obj.keySet().toArray();
        for (Object set1 : set) {
            LOG.info((String) set1);
        }
    }

    public boolean checkToken(String googleId, String token) {
        ValidatedToken t = mongoDatastore.get(ValidatedToken.class, googleId);
        if (!t.getToken().getAccess_token().equals(token)) {
            return false;
        }
        if (isTokenExpired(t)) {
            return false; //returns false if token is expired
        }
        return true;
    }

    private ValidatedToken createToken(Object[] set, JsonObject obj) {
        ValidatedToken token = new ValidatedToken();
        token.setIssued_to(obj.getString("issued_to"));
        token.setUser_id(obj.getString("user_id"));
        token.setAudience(obj.getString("audience"));
        token.setScope(obj.getString("scope"));
        token.setAccess_type(obj.getString("access_type"));
        token.setExpires_in(obj.getInt("expires_in"));
        token.setExpires_at(getExpiresAt(obj.getInt("expires_in")));
        return token;
    }
}
