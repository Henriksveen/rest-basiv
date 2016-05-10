package com.basiv.server.Models;

import java.util.Date;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Ivar Østby
 */
@Entity("validatedtokens")
public class ValidatedToken {

    @Id
    String user_id;
    String issued_to;
    String audience, scope, access_type;
    int expires_in;
    Date expires_at;
    AuthEntity token;

    public ValidatedToken() {

    }

    public Date getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Date expires_at) {
        this.expires_at = expires_at;
    }
  
    public String getIssued_to() {
        return issued_to;
    }

    public void setIssued_to(String issued_to) {
        this.issued_to = issued_to;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public AuthEntity getToken() {
        return token;
    }

    public void setToken(AuthEntity token) {
        this.token = token;
    }

}
