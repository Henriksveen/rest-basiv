package com.basiv.server.Models;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Ivar Østby
 */
@Entity("profiles")
public class ProfileEntity {

    @Id
    String id;
    String name;
    String image, bio, rank;
    int points;
    String[] createdMatches;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String[] getCreatedMatches() {
        return createdMatches;
    }

    public void setCreatedMatches(String[] createdMatches) {
        this.createdMatches = createdMatches;
    }

}
