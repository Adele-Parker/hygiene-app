package com.example.adelelephant.assignmenthygiene;

/**
 * Created by Adelelephant on 3/20/2018.
 * @Author Adele Parker
 */
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapsClusterItem implements ClusterItem {
    private LatLng position;
    private String title;
    private String snippet;
    private Places p;

    // Sets the postion variable with the lat, lng parameters
    public MapsClusterItem(double lat, double lng){
        position = new LatLng(lat,lng);
    }

    // sets the title and snippet variables with the corresponding parameters and the position variable
    // with the lat, lng parameters
    public MapsClusterItem(double lat, double lng, String title, String snippet){
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
    }

    //Extracts the information needed from the Places object and sets the relevant information into the variables.
    public MapsClusterItem(Places p){
        this.p = p;
        int id = p.getId();
        String name = p.getBusinessName();
        String address1 = p.getAddress1();
        String address2 = p.getAddress2();
        String address3 = p.getAddress3();
        String postcode = p.getPostcode();
        String rating = p.getRating();
        String ratingDate = p.getRatingDate();
        double lat = p.getLat();
        double lng = p.getLng();
        double distance = p.getDistanceKM();

        position = new LatLng(lat,lng);
        title = name;
        snippet = "Address Line 1: " + address1 + "\n Address Line 2: " + address2
                + "\n Address Line 3: " + address3 + "\n Postcode: " + postcode;

    }

    public Places getPlaces(){
        return p;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}

//