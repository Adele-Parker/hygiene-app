package com.example.adelelephant.assignmenthygiene;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONObject;

/**
 * Created by Adelelephant on 3/20/2018.
 */

public class CustomClusterItem extends DefaultClusterRenderer<MapsClusterItem>{

    private Context context;

    public CustomClusterItem(Context context, GoogleMap map, ClusterManager<MapsClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    //@Override
    /*
    Sets the marker image for none cluster markers using the place's rating as the marker and the places name and address for the title.
     */
    protected  void onBeforeClusterItemRendered(MapsClusterItem item, MarkerOptions markerOptions){
        Places p = item.getPlaces();

        String rating = p.getRating();
        String description = item.getTitle();

        switch(rating){
            case "-1":
            case "Exempt":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ratingexemptmarker)).title(description);
                break;}
            case "0":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating0marker)).title(description);
                break;}
            case "1":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating1marker)).title(description);
                break;}
            case "2":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating2marker)).title(description);
                break;}
            case "3":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating3marker)).title(description);
                break;}
            case "4":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating4marker)).title(description);
                break;}
            case "5":{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rating5marker)).title(description);
                break;}
            default:{markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ratingawaited)).title(description);
                break;}
        }
    }

    /*
    Sets what the cluster images is and sets the places names as the information in the title.
     */
    protected  void onBeforeClusterRendered(Cluster<MapsClusterItem> cluster, MarkerOptions markerOptions){
        String titles = "";

        for(ClusterItem item : cluster.getItems()){
            titles = titles + " " + item.getTitle();
        }

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ratingclustermarker)).title(titles);
    }

    /*
    Sets the ratio for how are apart the places have to be before becoming a cluster.
     */
    protected boolean shouldRenderAsCluster(Cluster<MapsClusterItem> cluster, MarkerOptions markerOptions){
        return cluster.getSize() > 1;
    }



}
