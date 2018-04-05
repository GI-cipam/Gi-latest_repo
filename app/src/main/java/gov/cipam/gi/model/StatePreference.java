package gov.cipam.gi.model;

/**
 * Created by karan on 1/19/2018.
 */

public class StatePreference {
    States state;
    boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public StatePreference(States state) {
        this.state = state;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }
}
