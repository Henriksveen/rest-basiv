package com.basiv.server.Resources;

import com.basiv.server.Models.Match;
import com.basiv.server.Services.MatchService;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

    @GET
    public List<Match> getMatches() {
        return matchService.getMatches();
        //return Response.ok().entity(matchService.getMatches()).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("{matchId}")
    public Response test(@PathParam("matchId") String matchId) {
        System.out.println("GET MATCH");
        return Response.ok().entity(matchService.getMatch(matchId)).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    public Response addMatch(Match match, @Context UriInfo uriInfo) {
        Match newMatch = matchService.addMatch(match);
        String newId = String.valueOf(newMatch.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build(); //locationheader: basiv-server/webapi/matches/ + id
        return Response.created(uri) //created --> statuscode 201, legger til header
                .entity(newMatch)
                .build();

    }

    @PUT
    @Path("/{matchId}")
    public Match updateMatch(@PathParam("matchId") String matchId, Match match) {
        match.setId(matchId);
        return matchService.updateMatch(match);
    }

    @DELETE
    @Path("/{matchId}")
    public void deleteMatch(@PathParam("matchId") String matchId) {
        matchService.deleteMatch(matchId);
    }
}
