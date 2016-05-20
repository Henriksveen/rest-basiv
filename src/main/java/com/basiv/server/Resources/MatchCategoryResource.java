package com.basiv.server.Resources;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Services.MatchService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Henriksveen
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchCategoryResource {

    MatchService matchService = new MatchService();

    @GET
    public Response getCategoryMatches(@PathParam("categoryName") String categoryName) {
        try {
            return Response.ok()
                    .entity(matchService.getCategoryMatches(categoryName))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (DataNotFoundException e) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
    }
}
