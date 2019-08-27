package com.example.adelelephant.assignmenthygiene;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
@author Adele Parker
 */

public class MainActivity extends AppCompatActivity {

    double lat = 0.0;
    double lng = 0.0;
    private JSONArray jsArray;
    Places p = new Places();
    String[] title;
    String[] description;
    Integer[] image;
    MainActivity parent;

    /*
    Runs when the app is opened, displays the activity_main. It also checks and asks for permission
    for using the internet, access fine location and updates lat and lng with the users current position.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.INTERNET"},1);
        }else{
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}
                @Override
                public void onProviderEnabled(String s) {}
                @Override
                public void onProviderDisabled(String s) {}
            });
        }
    }

    /*
    Stores the jsArray parameter in the jsArray variable
    */
    public void setJsArray(JSONArray jsArray) {
        this.jsArray = jsArray;
    }

    /*
    Is used when the user pushes the search button, it checks what radio button has been pressed
    and sets the type parameter accordingly. A Async thread is used to retrieve the information
    and is shown as a list view within a linear layout. If loctaion radio button is pressed a
    button leading to google maps is shown which is then hidden if another radio button the
    google maps button is hidden again.
     */
    public void search_onClick(View v){

        RadioButton rbLocation = (RadioButton)findViewById(R.id.rbLocation);
        RadioButton rbName = (RadioButton)findViewById(R.id.rbName);
        RadioButton rbPostcode = (RadioButton)findViewById(R.id.rbPostcode);
        RadioButton rbRUpdate = (RadioButton)findViewById(R.id.rbRUpdate);
        EditText etSearch = (EditText)findViewById(R.id.etSearch);
        LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayout);
        Button bToGoogle = (Button)findViewById(R.id.bToGoogle);

        //checks id the location radio button is pressed
        if(rbLocation.isChecked()){
            linLayout.removeAllViews();
            String type = "?op=s_loc&lat=" + lat + "&long=" + lng;
            new ExtraThread(this).execute(type);
            bToGoogle.setVisibility(View.VISIBLE);

            /*
            checks if the name radio button is pressed and the search edit text isn't empty, if it is
            empty it tells the user
             */
        }else if(rbName.isChecked()){
            linLayout.removeAllViews();
            String nameSearch = etSearch.getText().toString();
            if(nameSearch.isEmpty()){
                TextView blank = new TextView(getApplicationContext());
                blank.setText("Search line is empty. Please enter a name.");
                linLayout.addView(blank);
            }else{
            String type = "?op=s_name&name=" + nameSearch;
            new ExtraThread(this).execute(type);
            }
            bToGoogle.setVisibility(View.INVISIBLE);

        /*
        checks is the postcode radio button is pressed and the search edit text isn't empty, if it is
            empty it tells the user
         */
        }else if(rbPostcode.isChecked()){
            linLayout.removeAllViews();
            String postcodeSearch = etSearch.getText().toString();
            if(postcodeSearch.isEmpty()){
                TextView blank = new TextView(getApplicationContext());
                blank.setText("Search line is empty. Please enter a postcode.");
                linLayout.addView(blank);
            }else {
                String type = "?op=s_postcode&postcode=" + postcodeSearch;
                new ExtraThread(this).execute(type);
            }
            bToGoogle.setVisibility(View.INVISIBLE);

        //checks if the update radio button is pressed
        }else if(rbRUpdate.isChecked()){
            linLayout.removeAllViews();
            String type = "?op=s_recent";
            new ExtraThread(this).execute(type);
            bToGoogle.setVisibility(View.INVISIBLE);

            //if no radio button is pressed
        }else{
            linLayout.removeAllViews();
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Please select a search option");
            linLayout.addView(tv);
            bToGoogle.setVisibility(View.INVISIBLE);
        }

    }

    /*
    Is used when the google maps button is clicked. It passed the users current latitude and longitude
    as well as the jsArray being used to MapsActivity using parcelable and starts the google maps intent.
     */
    public void googleMap_onClick(View v){
        String responseBody = jsArray.toString();
        Intent intent = new Intent(v.getContext(),MapsActivity.class);
        JsParcel par = new JsParcel();
        par.setJsString(responseBody);
        par.setLat(lat);
        par.setLng(lng);
        intent.putExtra("responseBody",responseBody);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        startActivityForResult(intent,0);
    }

    /*
    Loops through the Json array and sets the information as a Places object for use.
    The business name and description, featuring the address and postcode and if the location
    radio button was used, the distance to the place, is stored in a list for use in the custom list view class.
    the correct rating images is found using a switch statement and also stored in a list for use in the custom list view.
    the created list view is displayed in the linear layout in the activity_main.
    The Json array parameter is also stored in the jsArray variable.
     */
    public void loopArray(JSONArray jsArray){
        LinearLayout layout = (LinearLayout)findViewById(R.id.linLayout);
        RadioButton rbLocation = (RadioButton)findViewById(R.id.rbLocation);
        ListView listView = new ListView(getApplicationContext());
        setJsArray(jsArray);
        title = new String[jsArray.length()];
        description = new String[jsArray.length()];
        image = new Integer[jsArray.length()];
        for(int i = 0; i<jsArray.length(); i++){
            try{
                JSONObject jsObj = jsArray.getJSONObject(i);
                p.jsonToClass(jsObj);
                title[i] = p.getBusinessName();
                double distance = p.getDistanceKM();
                if(rbLocation.isChecked()){
                    description[i] = ("Address Line 1: " + p.getAddress1() + "\n Address Line 2: " + p.getAddress2() +
                        "\n Address Line 3: " + p.getAddress3() + "\n Postcode: " + p.getPostcode()) + "\n Distance " + p.getDistanceKM();
                }else{
                    description[i] = ("Address Line 1: " + p.getAddress1() + "\n Address Line 2: " + p.getAddress2() +
                            "\n Address Line 3: " + p.getAddress3() + "\n Postcode: " + p.getPostcode());
                }
                String rating = p.getRating();
                switch (rating){
                    case "-1":
                    case "Exempt":{image[i] = R.drawable.ratingexempt;
                        break;}
                    case "0": {image[i] = R.drawable.rating0;
                        break;}
                    case "1": {image[i] = R.drawable.rating1;
                        break;}
                    case "2": {image[i] = R.drawable.rating2;
                        break;}
                    case "3": {image[i] = R.drawable.rating3;
                        break;}
                    case "4": {image[i] = R.drawable.rating4;
                        break;}
                    case "5": {image[i] = R.drawable.rating5;
                        break;}
                    default: {image[i] = R.drawable.ratingawaited;
                        break;}
                }

            }catch(JSONException e){e.printStackTrace();}
        }
        CustomListView customListView = new CustomListView(this,title,description,image);
        listView.setAdapter(customListView);
        layout.addView(listView);
    }

/* before list view was implemented this showed the information using linear layouts, text views and image views
    public LinearLayout setViews(Places p){

        String rating = p.getRating();
        ImageView ratingImage = new ImageView(getApplicationContext());
        TextView placeDisplay = new TextView(getApplicationContext());
        TextView blank = new TextView(getApplicationContext());
        LinearLayout innerLayout = new LinearLayout(getApplicationContext());

        float scale = getResources().getDisplayMetrics().density;
        int dpHeight = (int)(200*scale);
        int dpWidth = (int)(100*scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpHeight,dpWidth);
        ratingImage.setLayoutParams(layoutParams);
        placeDisplay.setText("Business Name: " + p.getBusinessName() + "\n Address Line 1: "
                + p.getAddress1() + "\n Address Line 2: " + p.getAddress2() +
                "\n Address Line 3: " + p.getAddress3() + "\n Postcode: " + p.getPostcode());
        double distance = p.getDistanceKM();
        if(distance == 0.0){
            innerLayout.addView(placeDisplay);
        }else{

            TextView tvDistance = new TextView(getApplicationContext());
            tvDistance.setText("Distance in KM: " + p.getDistanceKM());
            innerLayout.addView(placeDisplay);
            innerLayout.addView(tvDistance);
        }



        switch(rating) {
            case "-1":
            case "Exempt":{TextView tvRate = new TextView(getApplicationContext());
                tvRate.setText("Exempt");
                innerLayout.addView(tvRate);
                break;}
            case "0": {ratingImage.setImageResource(R.drawable.rating0);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
            case "1": {ratingImage.setImageResource(R.drawable.rating1);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
            case "2": {ratingImage.setImageResource(R.drawable.rating2);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
            case "3": {ratingImage.setImageResource(R.drawable.rating3);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
            case "4": {ratingImage.setImageResource(R.drawable.rating4);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
            case "5": {ratingImage.setImageResource(R.drawable.rating5);
                blank.setText("\n");
                innerLayout.addView(ratingImage);
                break;}
                default: {ratingImage.setImageResource(R.drawable.ratingawaited);
                    blank.setText("\n");
                    innerLayout.addView(ratingImage);
                    break;}
        }

        innerLayout.addView(blank);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        return innerLayout;

    }*/
}

