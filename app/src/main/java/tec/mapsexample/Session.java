package tec.mapsexample;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Session {

    private Date date;
    private float distance;
    public int duration;
    ArrayList<LatLng> locations;

    public Session(Date date, float distance,int duration, ArrayList<LatLng> locations){
        this.date = date;
        this.distance = distance;
        this.locations = locations;
    }

    public Session(){
        date = new Date();
        distance = 0f;
        locations = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(date)+" "+String.valueOf(distance)+"km";
    }

    public void addDistance(float distance) {
        this.distance += distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addLocation(LatLng location) {
        locations.add(location);
    }
}
