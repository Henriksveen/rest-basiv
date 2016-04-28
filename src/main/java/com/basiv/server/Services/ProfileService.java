
package com.basiv.server.Services;

import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.config.MongoDB;
import java.util.UUID;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;

/**
 * 
 * @author Ivar Østby
 */
public class ProfileService {

    private final Datastore db;

    public ProfileService() {
        this.db = MongoDB.instance().getDatabase();
    }
    
    public ResponseBuilder getProfile(String id) {
        ProfileEntity entity = db.get(ProfileEntity.class, id);
        if(entity == null){
            return Response.status(Response.Status.NOT_FOUND);
        }
        return Response.ok().entity(entity);
    }
    
    public ResponseBuilder addProfile(ProfileEntity entity){
        entity.setId(UUID.randomUUID().toString());
        entity.setPoints(0);
        Key<ProfileEntity> key = db.save(entity);
        if(key == null){
            return Response.serverError();
        }else{
            //hva skal returneres client?
            return Response.ok().entity(entity);
        }
    }
 

}
