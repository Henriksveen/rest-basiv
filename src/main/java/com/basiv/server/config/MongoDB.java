package com.basiv.server.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.util.logging.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * @author Henriksveen
 */
//https://github.com/xeraa/mongouk2011/blob/master/src/main/java/at/ac/tuwien/ec/mongouk2011/config/MongoDB.java
public class MongoDB {

    private static final Logger LOG = Logger.getLogger(MongoDB.class.getName());
    private static final MongoDB INSTANCE = new MongoDB();

    private final Datastore datastore;
    public static final String DB_NAME = "rest";

    private MongoDB() {
        MongoClientOptions mongoOptions = MongoClientOptions.builder().socketTimeout(60000)
                .connectTimeout(1200000).build(); // SocketTimeout: 60s, ConnectionTimeout: 20min
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(new ServerAddress("10.0.0.129", 27017), mongoOptions);
            //Creates a Mongo instance based on a (single) mongo node using a given ServerAddress and default options.
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MongoDB", e);
        }

        //mongoClient.setWriteConcern(WriteConcern.SAFE);   //write operations will throw exceptions on failure 
        datastore = new Morphia().mapPackage("com.basiv.server.Models") 
                .createDatastore(mongoClient, DB_NAME);
        datastore.ensureIndexes();
        LOG.info("Connection to database '" + DB_NAME + "' initialized");
    }

    public static MongoDB instance() {
        return INSTANCE;
    }

    public Datastore getDatabase() {
        return datastore;
    }
}
