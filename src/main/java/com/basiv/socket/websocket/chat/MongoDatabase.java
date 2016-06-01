/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.basiv.socket.websocket.chat;

import com.basiv.server.Models.ScoreEntity;
import com.basiv.server.config.MongoDB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * 
 * @author Ivar Østby
 */
@ApplicationScoped
public class MongoDatabase {
private static final Logger LOG = Logger.getLogger(MongoDB.class.getName());
    private final Datastore datastore;
    private final String DB_NAMEE = "rest";

    public MongoDatabase() {
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

        Morphia morphia = new Morphia().mapPackage("com.basiv.server.Models");
        morphia.map(ScoreEntity.class);
        datastore = morphia.createDatastore(mongoClient, DB_NAMEE);
        datastore.ensureIndexes();
        LOG.info("Connection to database '" + DB_NAMEE + "' initialized");
    }

    public Datastore getDatabase() {
        return datastore;
    }
}
