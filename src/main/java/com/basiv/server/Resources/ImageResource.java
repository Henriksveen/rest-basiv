package com.basiv.server.Resources;

import com.basiv.server.Services.ImageService;
import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * @author Henriksveen
 */
@Path("/image")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class ImageResource {

    ImageService service = new ImageService();

    @GET
    @Path("{matchId}")
    public Response test(@PathParam("matchId") String id, @Context UriInfo uriInfo) {
        System.out.println(uriInfo.getBaseUri() + uriInfo.getPathSegments().get(0).toString());
        Response r = service.getImage(id);
        if (r != null) {
            return r;
        }
        return Response.serverError().build();

    }

    @POST
    @Path("/upload")
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context UriInfo uriInfo) {
        URI uri = service.addImage(uploadedInputStream, fileDetail, uriInfo);
        if (uri != null) {
            return Response.created(uri).header("Access-Control-Allow-Origin", "*").header("Access-Control-Expose-Headers", "Location").build();
        } else {
            return Response.serverError().build();
        }
    }

    @OPTIONS
    @Path("/upload")
    public Response acceptPost() {
        Response.ResponseBuilder rb = Response.ok();
        rb.header("Access-Control-Allow-Origin", "*");
        rb.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        rb.header("Access-Control-Max-Age", "86400");
        rb.header("Access-Control-Request-Headers", "content-type");
        return rb.build();
    }

}
