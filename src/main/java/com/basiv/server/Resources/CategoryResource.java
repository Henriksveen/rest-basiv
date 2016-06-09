package com.basiv.server.Resources;

import com.basiv.server.Exceptions.DataNotFoundException;
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
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class CategoryResource {

    CategoryService categoryService = new CategoryService();

    @GET
    public Response getCategories() {
        try {
            return Response.ok().entity(categoryService.getCategories()).header("Access-Control-Allow-Origin", "*").build();
        } catch (DataNotFoundException e) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @GET
    @Path("{categoryId}")
    public Response getCategory(@PathParam("categoryId") String categoryId) {
        try {
            return Response.ok().entity(categoryService.getCategory(categoryId)).header("Access-Control-Allow-Origin", "*").build();
        } catch (DataNotFoundException e) {
            return Response.status(404).header("Access-Control-Allow-Origin", "*").build();
        }
    }

    @Path("{categoryName}/matches")
    public MatchCategoryResource getMatchResource() {
        return new MatchCategoryResource();
    }
}
