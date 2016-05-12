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
 * @author Henriksveen
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    CategoryService categoryService = new CategoryService();

    @GET
    public Response getCategories() {
        return Response.ok().entity(categoryService.getCategories()).header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("{categoryId}")
    public Response getCategory(@PathParam("categoryId") String categoryId) {
        return Response.ok().entity(categoryService.getCategory(categoryId)).header("Access-Control-Allow-Origin", "*").build();
    }

}
