package com.basiv.server.Services;

import com.basiv.server.Models.CategoryEntity;
import com.basiv.server.config.MongoDB;
import java.util.List;
import java.util.logging.Logger;
import org.mongodb.morphia.Datastore;

/**
 * @author Henriksveen
 */
public class CategoryService {

    private static final Logger LOG = Logger.getLogger(MatchService.class.getName());
    private final Datastore mongoDatastore;

    public CategoryService() {
        this.mongoDatastore = MongoDB.instance().getDatabase();
    }

    public List<CategoryEntity> getCategories() {
        return mongoDatastore.createQuery(CategoryEntity.class).asList();
    }

    public CategoryEntity getCategory(String categoryId) {
        return mongoDatastore.get(CategoryEntity.class, categoryId);
    }

    public String[] getHashtagList(String categoryId) {
        return mongoDatastore.get(CategoryEntity.class, categoryId).getHashtags();
    }
}
