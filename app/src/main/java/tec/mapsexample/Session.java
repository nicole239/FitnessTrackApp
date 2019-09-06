package tec.mapsexample;

import android.location.Location;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Session {

    private Date date;
    private float distance;
    private int duration;
    ArrayList<Location> locations;

    public Session(Date date, float distance, ArrayList<Location> locations){
        this.date = date;
        this.distance = distance;
        this.locations = locations;
    }

    @NonNull
    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(date)+" "+String.valueOf(distance)+"km";
    }
}
