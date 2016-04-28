package com.basiv.server.MessageBodyWriters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * @author Henriksveen
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ArrayListMessageBodyWriter implements MessageBodyWriter<ArrayList> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ArrayList.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(ArrayList arrayList, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(ArrayList arrayList, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        ObjectMapper mapper = new ObjectMapper(); //Using mapper to convert ArrayList to JSON
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));//Fixing dateFormat for mapper
        entityStream.write(mapper.writeValueAsString(arrayList ).getBytes());
    }
}
