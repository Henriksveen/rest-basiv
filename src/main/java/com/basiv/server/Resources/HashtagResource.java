/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.server.Resources;

import com.basiv.server.Services.CategoryService;
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
@Path("/hashtags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HashtagResource {

    CategoryService categoryService = new CategoryService();

    @GET
    @Path("{categoryId}")
    public Response getHashtagList(@PathParam("categoryId") String categoryId) {
        return Response.ok().entity(categoryService.getHashtagList(categoryId)).header("Access-Control-Allow-Origin", "*").build();
    }
}
