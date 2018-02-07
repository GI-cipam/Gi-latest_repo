package gov.cipam.gi.DirectionAPI;


import java.util.List;



/**
 * Created by Nitant Sood on 1/02/2018.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route,boolean isCheckingDistance,int index);
}
