package com.basiv.server.Resources;

import com.basiv.server.Exceptions.DataNotFoundException;
import com.basiv.server.Models.TeamEntity;
import com.basiv.server.Services.TeamService;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {
     TeamService teamService = new TeamService();

    @GET
    public Response getTeams() {
        return Response.ok().entity(teamService.getTeams()).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("{teamId}")
    public Response getTeam(@PathParam("teamId") String teamId) {
        try {
            return Response.ok().entity(teamService.getTeam(teamId)).header("Access-Control-Allow-Origin", "*").build();
        } catch (DataNotFoundException e) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @POST
    public Response addTeam(TeamEntity team, @Context UriInfo uriInfo) {
        TeamEntity newTeam = teamService.addTeam(team);
        if(newTeam == null){
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
        String newId = String.valueOf(newTeam.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build(); //locationheader: basiv-server/webapi/teams/ + id
        return Response.created(uri) //created --> statuscode 201, legger til header
                .entity(newTeam)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .build();

    }

    @OPTIONS
    public Response acceptPost() {
        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "POST");
        rb.header("Access-Control-Allow-Headers", "Content-Type,Access-Control-Allow-Origin, Accept");
        return rb.build();
    }

    @PUT
    @Path("/{teamId}")
    public Response updateTeam(@PathParam("teamId") String teamId, TeamEntity team) {
        team.setId(teamId);
        return Response.ok()
                .entity(teamService.updateTeam(team))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .build();

    }

    @OPTIONS
    @Path("/{teamId}")
    public Response acceptPut() {
        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "PUT");
        rb.header("Access-Control-Allow-Headers", "Content-Type, Accept");
        return rb.build();
    }

    @DELETE
    @Path("/{teamId}")
    public void deleteTeam(@PathParam("teamId") String teamId) {
        teamService.deleteTeam(teamId);
    }
}
