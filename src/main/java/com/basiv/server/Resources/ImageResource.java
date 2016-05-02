package com.basiv.server.Resources;

import com.basiv.server.Models.MatchEntity;
import com.basiv.server.Services.ImageService;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * @author Henriksveen
 */
@Path("/image")
public class ImageResource {

    ImageService service = new ImageService();
    
    @GET
    public List<MatchEntity> getMatches() {
        System.out.println("LISTE!");
        System.out.println("TEST");
        return null;
        //return Response.ok().entity(matchService.getMatches()).header("Access-Control-Allow-Origin", "*").build();
    }
    
//    @OPTIONS
//    public Response postImage(ImageEntity i, Request rq, @Context UriInfo uriInfo){
//        
//        System.out.println("NYTT BILDE!");
//        URI uri = service.addImage(i, uriInfo);
//        if(uri == null) return Response.serverError().build();
//        return Response.created(uri).header("Access-Control-Allow-Origin", "*").build();
//    }
//    
     
    @GET
    @Path("{matchId}")
    public Response test(@PathParam("matchId") String id,  @Context UriInfo uriInfo) {
        System.out.println(uriInfo.getBaseUri()+uriInfo.getPathSegments().get(0).toString());
                
        Response r = service.getImage(id);
        if(r != null){
            return r;
        }
        return Response.serverError().build();

    }
    
//    @POST
//    @Path("/upload")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response addImage(Object obj) {
//        //@FormDataParam("file") FormDataContentDisposition fileDetail
//        //uploadedInputStream
//        System.out.println("Mottatt multipart");
//        return Response.serverError().build();
//    }
//    //    public Response addImage(ImageEntity i, @Context UriInfo uriInfo){
////        System.out.println("NYTT BILDE!");
////        URI uri = service.addImage(i, uriInfo);
////        return Response.created(uri).header("Access-Control-Allow-Origin", "*").build();
////    }
    @POST
    @Path("/upload")
    //@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("file") InputStream uploadedInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @Context UriInfo uriInfo) {
        URI uri = service.addImage(uploadedInputStream, fileDetail, uriInfo);
        if(uri != null){
            return Response.created(uri).header("Access-Control-Allow-Origin", "*").build();
        }else{
            return Response.serverError().build();
        }
    }
    
    @OPTIONS
    @Path("/upload")
    public Response acceptPost(){
        Response.ResponseBuilder rb = Response.ok();
            rb.header("Access-Control-Allow-Origin", "*");
            rb.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            rb.header("Access-Control-Max-Age", "86400");
            rb.header("Access-Control-Request-Headers", "content-type");
            return rb.build();
    }
    
}
