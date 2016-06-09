/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.Resources;

import com.basiv.server.Models.UserEntity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author Ivar Østby
 */
@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
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
