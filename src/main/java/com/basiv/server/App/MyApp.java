package com.basiv.server.App;

import com.basiv.server.MessageBodyWriters.ArrayListMessageBodyWriter;
import com.basiv.server.MessageBodyWriters.StringListMessageBodyWriter;
import com.basiv.server.Models.ScoreEntity;
import com.basiv.server.Resources.AuthResource;
import com.basiv.server.Resources.CategoryResource;
import com.basiv.server.Resources.HashtagResource;
import com.basiv.server.Resources.ImageResource;
import com.basiv.server.Resources.MatchResource;
import com.basiv.server.Resources.ProfileResource;
import com.basiv.server.Resources.TeamResource;
import com.basiv.server.Resources.UserResource;
import com.basiv.server.Services.MatchService;
import com.basiv.server.config.MongoDB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;


/**
 * @author Henriksveen
 */
@ApplicationPath("webapi")
public class MyApp extends Application {
    
    
    
    @Override
    public Set<Class<?>> getClasses() {

        final Set<Class<?>> resources = new HashSet<Class<?>>();

        // Add your resources.
        resources.add(ImageResource.class);
        resources.add(MatchResource.class);
        resources.add(TeamResource.class);
        resources.add(AuthResource.class);
        resources.add(ProfileResource.class);
        resources.add(UserResource.class);
        resources.add(CategoryResource.class);
        resources.add(HashtagResource.class);
      
        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);
        resources.add(ArrayListMessageBodyWriter.class);
        resources.add(StringListMessageBodyWriter.class);
//        resources.add(ServiceFeature.class); 
        return resources;
    }
    

    @Override
    public Set<Object> getSingletons() {
        final Set<Object> resources = new HashSet<Object>();
        // Add singletons.
        
        return resources;
    }
//    public MyApp(){
//        this.getClasses().add(ImageResource.class);
//        this.getClasses().add(Testt.class);
//        this.getClasses().add(MatchResource.class);
//    }
}
