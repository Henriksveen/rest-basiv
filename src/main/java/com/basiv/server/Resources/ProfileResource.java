/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.Resources;

import com.basiv.server.Models.ProfileEntity;
import com.basiv.server.Services.ProfileService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * 
 * @author Ivar Østby
 */
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {
    ProfileService service = new ProfileService();
    
    @GET
    @Path("{profileId}")
    public Response getProfile(@PathParam("profileId") String id) {
        System.out.println("Profile requested: " + id);

        return service.getProfile(id).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @POST
    public Response postProfile(ProfileEntity entity){
        System.out.println("POST PROFILE");
        return service.addProfile(entity).header("Access-Control-Allow-Origin", "*").build();
    }

}
