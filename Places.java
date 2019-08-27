package com.example.adelelephant.assignmenthygiene;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adelelephant on 2/27/2018.
 * Places class with setters and getters for all the information retrieved from the web link.
 */

class Places {

    private int id;
    private String businessName;
    private String address1;
    private String address2;
    private String address3;
    private String postcode;
    private String rating;
    private String ratingDate;
    private double lng;
    private double lat;
    private double distanceKM;
    private JSONArray jsArray;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setBusinessName(String businessName){
        this.businessName = businessName;
    }

    public String getBusinessName(){
        return this.businessName;
    }

    public void setAddress1(String address1){
        this.address1 = address1;
    }

    public String getAddress1(){
        return this.address1;
    }

    public void setAddress2(String address2){
        this.address2 = address2;
    }

    public String getAddress2(){
        return this.address2;
    }

    public void setAddress3(String address3){
        this.address3 = address3;
    }

    public String getAddress3(){
        return this.address3;
    }

    public void setPostcode(String postcode){
        this.postcode = postcode;
    }

    public String getPostcode(){
        return this.postcode;
    }

    // If the rating is -1 it is returned as Exempt.
    public void setRating(String rating){
        if(rating.equals("-1"))
    {
        this.rating = "Exempt";
    }else {
            this.rating = rating;
        }
    }

    public String getRating(){return this.rating;}

    public void setRatingDate(String ratingDate){
        this.ratingDate = ratingDate;
    }

    public String getRatingDate(){
        return this.ratingDate;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public double getLng(){
        return this.lng;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLat(){
        return this.lat;
    }

    public void setDistanceKM(double distanceKM){this.distanceKM = distanceKM;}

    public double getDistanceKM(){return this.distanceKM;}

    public String toString(){
        return "ID: " + getId() + "\n Business Name: " + getBusinessName() + "\n Address Line 1: " + getAddress1() + "\n Address Line 2: " + getAddress2() +
                "\n Address Line 3: " + getAddress3() + "\n Postcode: " + getPostcode() + "\n Rating: " + getRating() + "\n Rating Date: " + getRatingDate() + "\n Longitude: " + getLng() +
                "\n Latitude: " + getLat() + "\n Distance in KM: " + getDistanceKM();
    }

    /*
    Gets the information from the Json object and sets, and returns it as a new place object.
     */
    public Places jsonToClass(JSONObject jsObject){

        Places place = new Places();

        try{
            id = jsObject.getInt("id");
            businessName = jsObject.getString("BusinessName");
            address1 = jsObject.getString("AddressLine1");
            address2 = jsObject.getString("AddressLine2");
            address3 = jsObject.getString("AddressLine3");
            postcode = jsObject.getString("PostCode");
            rating = jsObject.getString("RatingValue");
            ratingDate = jsObject.getString("RatingDate");
            lng = jsObject.getDouble("Longitude");
            lat = jsObject.getDouble("Latitude");
            distanceKM = jsObject.getDouble("DistanceKM");

            place.setId(id);
            place.setBusinessName(businessName);
            place.setAddress1(address1);
            place.setAddress2(address2);
            place.setAddress3(address3);
            place.setPostcode(postcode);
            place.setRating(rating);
            place.setRatingDate(ratingDate);
            place.setLng(lng);
            place.setLat(lat);
            place.setDistanceKM(distanceKM);

        }catch(JSONException e){e.printStackTrace();}
        return place;
    }
}
