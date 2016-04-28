/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.Resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author Ivar Østby
 */
@Path("/test")
public class Testt {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(){
        return "YOLO";
    }

}
