package com.basiv.server.Resources;

import com.basiv.server.Services.SearchService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Henriksveen
 */
@Path("/search")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class SearchResource {

    SearchService searchSevice = new SearchService();

    @GET
    @Path("{searchName}")
    public Response getSearchObjects(@PathParam("searchName") String name) {
        return Response.ok().entity(searchSevice.getSearchObjects(name)).header("Access-Control-Allow-Origin", "*").build();
    }
}
