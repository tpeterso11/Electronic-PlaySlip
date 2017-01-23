package com.shoutz.toussaintpeterson.electronicpayslip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.quentinklein.slt.LocationTracker;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestMapFragment extends AbstractEPSFragment implements GoogleApiClient.OnConnectionFailedListener {
    private String title;
    private int page;
    private ViewFlipper vf;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ArrayList<String> categories;
    private ArrayList<String> favoritesList;
    private TextView noFave;
    private TextView noLocation;
    private CustomExpandableListView locations;
    private GoogleMap googleMap;
    private MapView mMapView;
    private Location myLocation;
    private GoogleApiClient mGoogleApiClient;
    private JSONObject response;
    private MarkerOptions[] places;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;


    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_frag, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        myLocation = getEpsApplication().userLocation;


        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .build();

        ((LandingActivity)getActivity()).disableScroll();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        placeMarkers = new Marker[MAX_PLACES];

        new JsonTask().execute();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 5);
        title = getArguments().getString("Profile");
    }


    public static TestMapFragment newInstance() {
        TestMapFragment fragmentFirst = new TestMapFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 5);
        args.putString("someTitle", "Profile");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class GetPlacesTask extends AbstractWebService {
        private String apiKey;
        private Location location;
        private String radius;
        private String keyword;

        public GetPlacesTask(String urlPath, Location location, String apiKey, String radius, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.apiKey = apiKey;
            this.location = location;
            this.radius = radius;
            this.keyword = keyword;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            try{
                JSONObject responseJSON = new JSONObject(response.toString());

            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            param2.put("location", location.getLatitude()+","+location.getLongitude());
            param2.put("radius", "500");
            param2.put("type", "restaurant");
            param2.put("keyword", "cruise");
            param2.put("key", getResources().getString(R.string.google_api));

            /*JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("username", email);
            params.put("password", password);*/

            JSONObject json = new JSONObject();

            response = doGet(json, param2);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void connect(Location location){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/nearbysearch/");
        urlString.append("json?");
        urlString.append("location=");
        urlString.append(location.getLatitude());
        urlString.append(",");
        urlString.append(location.getLongitude());
        urlString.append("&radius=500");
        urlString.append("&types="+"gas_station");
        urlString.append("&sensor=false&key=" + getResources().getString(R.string.google_api));


    }

    public interface MyInterface {
        @GET("/api/place/nearbysearch/json")
        void getMyThing2(@Query("location") Location location, @Query("radius") String radius, @Query("type") String type, @Query("key") String key, Callback<Response> callback);
    }

    private void useRetrofit(Location location, String radius, String type, String key){
        String url =  "https://maps.googleapis.com/maps";

        MyInterface retrofitInterface = new RestAdapter.Builder()
                .setEndpoint(url).build().create(MyInterface.class);

        retrofitInterface.getMyThing2(location, radius, type, key, new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String newresult = sb.toString();

                try {
                    JSONObject resultObj = new JSONObject(newresult);

                    Toast.makeText(getActivity(), newresult, Toast.LENGTH_LONG).show();

                }
                catch (JSONException ex){

                }
            }


            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            while(myLocation == null){
                //do nothing
            }

            StringBuilder urlString = new StringBuilder();
            urlString.append("https://maps.googleapis.com/maps/api/place/nearbysearch/");
            urlString.append("json?");
            urlString.append("location=");
            urlString.append(myLocation.getLatitude());
            urlString.append(",");
            urlString.append(myLocation.getLongitude());
            urlString.append("&radius=5000");
            urlString.append("&keyword="+"quiktrip");
            urlString.append("&sensor=false&key=" + getResources().getString(R.string.google_api));

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlString.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                String newString = buffer.toString();

                try{
                    JSONObject resultObject = new JSONObject(newString);
                    JSONArray placesArray = resultObject.getJSONArray("results");
                    places = new MarkerOptions[placesArray.length()];

                    for (int p = 0; p < placesArray.length(); p++) {
                        //parse each place
                        //if any values are missing we won't show the marker
                        boolean missingValue = false;
                        LatLng placeLL = null;
                        String placeName = "";
                        String vicinity = "";
                        try {
                            //attempt to retrieve place data values
                            missingValue = false;
                            //get place at this index
                            JSONObject placeObject = placesArray.getJSONObject(p);
                            //get location section
                            JSONObject loc = placeObject.getJSONObject("geometry")
                                    .getJSONObject("location");
                            //read lat lng
                            placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                    Double.valueOf(loc.getString("lng")));
                            //get types
                            JSONArray types = placeObject.getJSONArray("types");
                            //loop through types
                            for(int t=0; t<types.length(); t++){
                                //what type is it
                                String thisType=types.get(t).toString();
                                //check for particular types - set icons
                                /*if(thisType.contains("hospital")){
                                    currIcon = foodIcon;
                                    break;
                                }
                                else if(thisType.contains("health")){
                                    currIcon = drinkIcon;
                                    break;
                                }
                                else if(thisType.contains("doctor")){
                                    currIcon = shopIcon;
                                    break;
                                }*/
                            }
                            //vicinity
                            vicinity = placeObject.getString("vicinity");
                            //name
                            placeName = placeObject.getString("name");
                        } catch (JSONException jse) {
                            Log.v("PLACES", "missing value");
                            missingValue = true;
                            jse.printStackTrace();
                        }
                        //if values missing we don't display
                        if (missingValue) places[p] = null;
                        else
                            places[p] = new MarkerOptions()
                                    .position(placeLL)
                                    .title(placeName)
                                    .snippet(vicinity);
                    }

                }
                catch(JSONException ex){

                }

//                Toast.makeText(getActivity(), newString, Toast.LENGTH_LONG).show();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MapsInitializer.initialize(getActivity().getApplicationContext());

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    while(places == null){
                        //do nothing
                    }

                    for (int p = 0; p < places.length; p++) {
                        //will be null if a value was missing
                        if (places[p] != null) {
                            placeMarkers[p] = googleMap.addMarker(new MarkerOptions().position(places[p].getPosition()).title(places[p].getTitle()));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(places[p].getPosition()).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }

                /*Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                        .appendQueryParameter("q", "64063")
                        .build();*/

                    // For showing a move to my location button
                    //googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                /*if(getEpsApplication().userLocation != null) {
                    LatLng my = new LatLng(getEpsApplication().userLocation.getLatitude(), getEpsApplication().userLocation.getLongitude());
                    //LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(my).title("You").snippet("You are here."));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(my).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }*/
                }
            });

        }
    }

    public void cancelApi(){
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        ((LandingActivity)getActivity()).launchCartLanding();
    }
}

