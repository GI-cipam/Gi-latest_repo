package gov.cipam.gi.directionAPI;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Nitant Sood on 1/02/2018.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
