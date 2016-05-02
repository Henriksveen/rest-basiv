package com.basiv.server.Resources;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Services.ImageService;
import com.basiv.server.Services.MatchService;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Henriksveen
 */
@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchResource {

    MatchService matchService = new MatchService();
    ImageService imageService = new ImageService();

    @GET
    public Response getMatches() {
        return Response.ok().entity(matchService.getMatches()).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("{matchId}")
    public Response getMatch(@PathParam("matchId") String matchId) {
        try {
            return Response.ok()
                    .entity(matchService.getMatch(matchId))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (DataNotFoundException e) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @POST
    public Response addMatch(MatchEntity match, @Context UriInfo uriInfo, @HeaderParam("userId") String userId) {
        System.out.println(userId);
        MatchEntity newMatch = matchService.addMatch(match);
        if (newMatch == null) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
        String newId = String.valueOf(newMatch.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build(); //locationheader: basiv-server/webapi/matches/ + id
        return Response.created(uri) //created --> statuscode 201, legger til header
                .entity(newMatch)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .build();

    }

    @OPTIONS
    public Response acceptPost() {
        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "POST");
        rb.header("Access-Control-Allow-Headers", "accept, access-control-allow-headers, access-control-allow-origin, content-type");
        return rb.build();
    }

    @PUT
    @Path("/{matchId}")
    public Response updateMatch(@PathParam("matchId") String matchId, MatchEntity match) {
        match.setId(matchId);
        return Response.ok()
                .entity(matchService.updateMatch(match))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .build();

    }

    @OPTIONS
    @Path("/{matchId}")
    public Response acceptPut() {
        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "PUT");
        rb.header("Access-Control-Allow-Headers", "Content-Type, Accept");
        return rb.build();
    }

    @DELETE
    @Path("/{matchId}")
    public void deleteMatch(@PathParam("matchId") String matchId) {
        matchService.deleteMatch(matchId);
    }
}
