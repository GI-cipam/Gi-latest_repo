package gov.cipam.gi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by karan on 1/19/2018.
 */

public class Uniqueness {
    private String info;
    @JsonIgnore
    private String uid;

    public Uniqueness(String info) {
        this.info = info;
    }

    public Uniqueness() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
