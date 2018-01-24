package gov.cipam.gi.model;

/**
 * Created by karan on 1/25/2018.
 */

public class Bio {
    String name,email;

    public Bio(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
