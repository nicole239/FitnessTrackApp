package tec.mapsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class MapTrackerActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    private Location previousLocation;
    private Session session;
    final Handler handler = new Handler();
    Runnable run;
    boolean displaySession;

    LocationManager locationManager;
    LocationListener locationListener;
    PolylineOptions options = new PolylineOptions();
    TextView txtTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracker);

        displaySession = getIntent().getExtras().getBoolean("display");



        mapView = findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getResources().getString(R.string.google_maps_key));
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        txtTimer = findViewById(R.id.txtTimer);

        if(displaySession){
            session = SessionsList.getInstance().actual;
            session.duration--;
            increaseDuration();
            drawPrimaryLinePath();

        }else {
            txtTimer.setText("0:00");

            run = new Runnable() {
                @Override
                public void run() {
                    increaseDuration();
                    handler.postDelayed(this, 1000);
                }
            };


            setupLocationManager();

            getPermissions();

            setupDrawOptions();

            session = new Session();
        }
    }

    private void increaseDuration(){
        session.duration++;
        int minutes = session.duration/60;
        int seconds = session.duration%60;
        txtTimer.setText(String.format("%02d",minutes) + ":" + String.format("%02d",seconds));
    }

    private void getPermissions(){
        //preguntar si NO tengo permiso
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Es asíncronam hay otro metodo para recibir el resultado de la solicitud del permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //Si tengo permiso
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, locationListener);
        }
    }

    private void setupLocationManager(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,16));
                drawLineTwoPoints(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    private void setupDrawOptions(){
        options.color( Color.parseColor( "#9534eb" ) );
        options.width( 5 );
        options.visible( true );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Si nos dieron permiso
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
            }

        }
        //No nos dieron permiso
        else {
            Log.i(":(", "No nos dieron permiso");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng tECB3 = new LatLng(9.856345, -83.912658);
        //previousLocation = tECB3;
        //options.add(tECB3);
        //mMap.addMarker(new MarkerOptions().position(tECB3).title("Marker in TEC-B3"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tECB3,18));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(getResources().getString(R.string.google_maps_key));
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(getResources().getString(R.string.google_maps_key), mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void drawPrimaryLinePath(  )
    {
        if ( mMap == null )
        {
            return;
        }

        if ( session.locations.size() < 2 )
        {
            return;
        }

        mMap.addMarker(new MarkerOptions().position(session.locations.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(session.locations.get(session.locations.size()-1)).title("Finish"));
        for ( LatLng locRecorded : session.locations )
        {
            options.add(locRecorded );
        }

        mMap.addPolyline( options );
    }

    private void drawLineTwoPoints(Location endPoint){
        LatLng latLng = new LatLng(endPoint.getLatitude(),endPoint.getLongitude());
        options.add(latLng);
        session.addLocation(latLng);
        if(previousLocation != null)
            session.addDistance(previousLocation.distanceTo(endPoint));
        else
            handler.postDelayed(run, 1000);
        previousLocation = endPoint;
        mMap.addPolyline(options);
    }

    public void btnEnd_onClick(View view){
        SessionsList.getInstance().addSession(session);
        handler.removeCallbacks(run);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
