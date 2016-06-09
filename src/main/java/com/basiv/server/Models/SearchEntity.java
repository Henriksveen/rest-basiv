package com.basiv.server.Models;

import java.util.List;

/**
 * @author Henriksveen
 */
public class SearchEntity {

    List<MatchEntity> matchList;
    List<ProfileEntity> profileList;

    public SearchEntity() {

    }

    public List<MatchEntity> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<MatchEntity> matchList) {
        this.matchList = matchList;
    }

    public List<ProfileEntity> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<ProfileEntity> profileList) {
        this.profileList = profileList;
    }

}
