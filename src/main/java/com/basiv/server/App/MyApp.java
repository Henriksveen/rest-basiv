package com.basiv.server.App;

import com.basiv.server.MessageBodyWriters.ArrayListMessageBodyWriter;
import com.basiv.server.MessageBodyWriters.StringListMessageBodyWriter;
import com.basiv.server.Resources.AuthResource;
import com.basiv.server.Resources.CategoryResource;
import com.basiv.server.Resources.HashtagResource;
import com.basiv.server.Resources.ImageResource;
import com.basiv.server.Resources.MatchResource;
import com.basiv.server.Resources.ProfileResource;
import com.basiv.server.Resources.SearchResource;
import com.basiv.server.Resources.TeamResource;
import com.basiv.server.Resources.UserResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

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
        resources.add(SearchResource.class);

        // Add additional features such as support for Multipart.
        resources.add(MultiPartFeature.class);
        resources.add(ArrayListMessageBodyWriter.class);
        resources.add(StringListMessageBodyWriter.class);

        return resources;
    }

//    public MyApp(){
//        this.getClasses().add(ImageResource.class);
//        this.getClasses().add(Testt.class);
//        this.getClasses().add(MatchResource.class);
//    }
}
