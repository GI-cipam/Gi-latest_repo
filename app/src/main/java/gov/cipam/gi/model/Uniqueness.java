package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by karan on 1/19/2018.
 */

public class Uniqueness {
    private String uniqueness;
    @JsonIgnore
    private String uid;

    public Uniqueness(String uniqueness) {
        this.uniqueness = uniqueness;
    }

    public Uniqueness() {
    }

    public String getUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(String uniqueness) {
        this.uniqueness = uniqueness;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
