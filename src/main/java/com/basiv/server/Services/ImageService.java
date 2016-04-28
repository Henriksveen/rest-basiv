/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.basiv.server.Services;

import com.basiv.server.Models.ImageEntity;
import com.basiv.server.config.MongoDB;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import jersey.repackaged.com.google.common.io.ByteStreams;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author Ivar Østby
 */
public class ImageService {

    private static final Logger LOG = Logger.getLogger(MatchService.class.getName());
    private final Datastore mongoDatastore;

    public ImageService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    public Response getImage(String id) {
        ImageEntity i = mongoDatastore.get(ImageEntity.class, id);
        if (i != null) {
            Response.ResponseBuilder rb = Response.ok();
            rb.header("Accept-Ranges", "bytes");
            rb.header("Content-Type", "image/jpeg").header("Content-Length", i.getBytes().length);
            rb.entity(i.getBytes());
            return rb.build();
        }
        return null;
    }

    public URI addImage(ImageEntity i, UriInfo uriInfo) {
        if (i == null) {
            return null;
        }
        i.setId(UUID.randomUUID().toString());
        i.setUrl(uriInfo.getAbsolutePath() + i.getId());

        mongoDatastore.save(i);
        return uriInfo.getAbsolutePathBuilder().path(i.getId()).build();
    }

    public URI addImage(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, UriInfo uriInfo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //        try {
//            BufferedImage BiIn =ImageIO.read(uploadedInputStream);
//            File fw = new File("D:\\bachelor\\data\\images\\"+fileDetail.getFileName());
//            ImageIO.write(BiIn, "jpg", fw);
//        } catch (IOException ex) {
//            Logger.getLogger(ImageService.class.getName()).log(Level.SEVERE, null, ex);
//        }
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = uploadedInputStream.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();

        } catch (IOException ex) {
            Logger.getLogger(ImageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (baos.size() > 0) {
            ImageEntity i = new ImageEntity();
            i.setId(UUID.randomUUID().toString());
            i.setUrl(uriInfo.getBaseUri()+uriInfo.getPathSegments().get(0).toString()+ '/' + i.getId());
            i.setBytes(baos.toByteArray());
            mongoDatastore.save(i);
            return uriInfo.getBaseUriBuilder().uri(i.getUrl()).build();
        }
        return null;
    }

}
