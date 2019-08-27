package com.example.adelelephant.assignmenthygiene;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adelelephant on 3/6/2018.
 * @author Adele Parker
 * Uses parcelable to pass information from one class to another.
 * Can pass a String, jsString, and two doubles, lat and lng.
 */

public class JsParcel implements Parcelable {

    public String jsString;
    public double lat;
    public double lng;

    //getters
    public String getString(){return jsString;}
    public double getLat(){return lat;}
    public double getLng(){return lng;}

    //reads the information in from the parcel
    public JsParcel(Parcel in) {
        jsString = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public JsParcel(){}

    //setters
    public void setJsString(String jsString){this.jsString = jsString;}
    public void setLat(double lat){this.lat = lat;}
    public void setLng(double lng){this.lng = lng;}

    @Override
    public int describeContents() {
        return hashCode();
    }

    //writes the information into the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jsString);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public JsParcel createFromParcel(Parcel in) {
            return new JsParcel(in);
        }

        @Override
        public JsParcel[] newArray(int size) {
            return new JsParcel[size];
        }
    };

}

