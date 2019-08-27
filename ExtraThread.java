package com.example.adelelephant.assignmenthygiene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.adelelephant.assignmenthygiene.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adelelephant on 3/1/2018.
 * @author Adele Parker
 * uses Async thread to get the information and return is as a Json array as well as displaying it using
 * the loopArray method.
 */

public class ExtraThread extends AsyncTask<String,Integer,JSONArray>{

    MainActivity parent;

    public ExtraThread(MainActivity parent){
        this.parent = parent;
    }

    String responseBody = "";
    Places p = new Places();
    JSONArray jsArray;

    /*
    Sets the first parameter as the extension from the web address and retrieves the information using GET.
    Saves each line as responseBody and uses that to create the Json array
     */
    @Override
    protected JSONArray doInBackground(String... params){
        URL url;
        HttpURLConnection urlConnection = null;
        String type = params[0];
        try{
            url = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php" + type);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStreamReader input = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader in = new BufferedReader(input);

            String line = "";
            while((line = in.readLine())!= null){
                responseBody = responseBody + line;
            }
            try{
                jsArray = new JSONArray(responseBody);
            }catch(JSONException e){e.printStackTrace();}
        }catch(IOException e){e.printStackTrace();}
        finally{
            urlConnection.disconnect();}

        return jsArray;
    }

    //When the thread is finished displays the information using loopArray.
    @Override
    protected void onPostExecute(JSONArray jsArray){
        super.onPostExecute(jsArray);
        parent.loopArray(jsArray);
    }

}
