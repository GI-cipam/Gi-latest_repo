package gov.cipam.gi.model;

/**
 * Created by karan on 1/25/2018.
 */

public class Bio {
    String name, email;
    int dp;

    public int getDp() {
        return dp;
    }

    public void setDp(int dp) {
        this.dp = dp;
    }

    public Bio(String name, String email, int dp) {
        this.name = name;
        this.email = email;
        this.dp = dp;
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
