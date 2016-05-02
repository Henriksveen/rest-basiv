/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.Resources;

import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Models.UserEntity;
import com.basiv.server.Services.ImageService;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Ivar Østby
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    //ImageService service = new ImageService();
    @GET
    @Path("/user/{googleId}")
    public UserEntity getUser(@PathParam("matchId") String googleId) {
        System.out.println("googleId!");
        return null;
        //return Response.ok().entity(matchService.getMatches()).header("Access-Control-Allow-Origin", "*").build();
    }
}
