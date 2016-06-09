package com.basiv.server.Resources;

/**
 *
 * @author Ivar Østby
 */
import com.basiv.server.Models.AuthEntity;
import com.basiv.server.Models.UserEntity;
import com.basiv.server.Services.AuthService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})

public class AuthResource {

    AuthService service = new AuthService();
    private String clientId = "1057885418811-4f04mik9v1t65af68hutbtdd3heb4eit.apps.googleusercontent.com";

    @POST
    @Path("/token")
    public Response validateToken(AuthEntity ent) throws IOException, GeneralSecurityException {
        UserEntity id = service.authenticateToken(ent);
        if(id != null){
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Request-Headers", "*")
                .entity(id)
                .build();
        }
        else{
            return Response.status(401).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @OPTIONS
    @Path("/token")
    public Response acceptToken() {
        System.out.println("/token options called");

        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "POST");
        // rb.header("Access-Control-Max-Age", "86400");
        rb.header("Access-Control-Allow-Headers", "Content-Type,Access-Control-Allow-Origin, Accept");
        return rb.build();
    }
}
