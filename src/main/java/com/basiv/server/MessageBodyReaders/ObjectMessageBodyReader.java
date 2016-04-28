/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.server.MessageBodyReaders;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Scanner;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @author Ivar Østby
 */
@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ObjectMessageBodyReader implements MessageBodyReader<Object>{

    @Override
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        System.out.println("FEILET");
        return Object.class.isAssignableFrom(arg0);
    }

    @Override
    public Object readFrom(Class<Object> arg0, Type arg1, Annotation[] arg2, MediaType arg3, MultivaluedMap<String, String> arg4, InputStream arg5) throws IOException, WebApplicationException {
        Scanner s = new Scanner(arg5).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        System.out.println(result);
        return result;
    }   

}
