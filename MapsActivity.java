package com.example.adelelephant.assignmenthygiene;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
@Author Adele Parker
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<MapsClusterItem>,
        ClusterManager.OnClusterItemClickListener<MapsClusterItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MapsClusterItem>{

    private GoogleMap mMap;
    private JSONArray jsArray;
    private String jsString;
    private double cLat = 0.0;
    private double cLng = 0.0;
    Marker marker = null;

    /*
    Runs when the map feature is opened and uses the activity_maps layout.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*
    Checks internet and location permission and updated the users position on the map and centers
    the camera on the users marker on map opening and only when the users moves not just if the location is rechecked.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        UiSettings uiSet = mMap.getUiSettings();
        uiSet.setZoomControlsEnabled(true);
        setCluster();

        LatLng cLocation = new LatLng(getcLat(),getcLng());
        marker = mMap.addMarker(new MarkerOptions().position(cLocation).title("Me"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cLocation,14));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.INTERNET"},1);
        }else {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    LatLng cLocation = new LatLng(lat,lng);

                    if(lat != cLat || lng != cLng){
                        cLat = lat;
                        cLng = lng;
                    if(marker != null){
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(cLocation).title("Me"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cLocation,14));
                }}

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            });
        }
    }

    public double getcLat(){
        Bundle b= getIntent().getExtras();
        cLat = b.getDouble("lat");
        return cLat;
    }

    public double getcLng(){
        Bundle b= getIntent().getExtras();
        cLng = b.getDouble("lng");
        return cLng;
    }
/*
    public void showMarkers(GoogleMap googleMap){
        Places p = new Places();
        double lat = 0.0;
        double lng = 0.0;
        mMap = googleMap;

        Bundle b = getIntent().getExtras();
        String responseBody = b.getString("responseBody");

        try {
            jsArray = new JSONArray(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<jsArray.length(); i++){
            try{
                JSONObject jsObj = jsArray.getJSONObject(i);
                p.jsonToClass(jsObj);
                lat = p.getLat();
                lng = p.getLng();
                LatLng place = new LatLng(lat, lng);
                String description = "Business Name: " + p.getBusinessName() + "\n Address Line 1: "
                        + p.getAddress1() + "\n Address Line 2: " + p.getAddress2() +
                        "\n Address Line 3: " + p.getAddress3() + "\n Postcode: " + p.getPostcode();
                String rating = p.getRating();

                switch(rating){
                    case "-1":
                    case "Exempt":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.ratingexemptmarker)));
                        break;}
                    case "0":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating0marker)));
                        break;}
                    case "1":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating1marker)));
                        break;}
                    case "2":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating2marker)));
                        break;}
                    case "3":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating3marker)));
                        break;}
                    case "4":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating4marker)));
                        break;}
                    case "5":{mMap.addMarker(new MarkerOptions().position(place).title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.rating5marker)));
                        break;}
                    default:{mMap.addMarker(new MarkerOptions().position(place).title(description)/*.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating1marker)));
                        break;}
                }


                //mMap.addMarker(new MarkerOptions().position(place).title(description));
            }catch(JSONException e){e.printStackTrace();}
        }
    }
*/

    /*
    Creates markers and cluster Markers for when the pins are too close together using MapsClusterItem. Gets the location
    for each marker from the string sent from the MainActivity through parcelable.
    */
    private ClusterManager<MapsClusterItem> clusterManager;

    private void setCluster(){
        clusterManager = new ClusterManager<MapsClusterItem>(this,mMap);
        CustomClusterItem marker = new CustomClusterItem(this,mMap,clusterManager);
        clusterManager.setRenderer(marker);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        addItems();
        clusterManager.cluster();
    }

    private void addItems() {
        Places p = new Places();
        Bundle b = getIntent().getExtras();
        String responseBody = b.getString("responseBody");

        try {
            jsArray = new JSONArray(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsArray.length(); i++) {
            try {
                JSONObject jsObj = jsArray.getJSONObject(i);
                p.jsonToClass(jsObj);
                MapsClusterItem setItem = new MapsClusterItem(p);
                clusterManager.addItem(setItem);
                clusterManager.cluster();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    /*
    public boolean onClusterClick(Cluster<MapsClusterItem> cluster) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        String description = cluster.getItems().iterator().next().getTitle();
        Toast.makeText(this,cluster.getSize() + " Places in Cluster: " + description,Toast.LENGTH_LONG).show();

        //Marker<MapsClusterItem> marker = cluster.getItems();
        for(ClusterItem item: cluster.getItems()){
            //LatLng position = ;
            builder.include(item.getPosition());
            String title = item.getTitle();
        }

        LatLngBounds bounds = builder.build();

        try{
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }*/
    public boolean onClusterClick(Cluster<MapsClusterItem> cluster) {
        return false;
    }

    @Override
    public boolean onClusterItemClick(MapsClusterItem mapsClusterItem) {
        //CustomClusterItem marker = new CustomClusterItem(this,mMap,clusterManager);
        //clusterManager.setRenderer(marker);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MapsClusterItem mapsClusterItem) {
        //Intent markerActivity = new Intent(this,MapsClusterItem.class);
        //startActivity(markerActivity);
    }
}
