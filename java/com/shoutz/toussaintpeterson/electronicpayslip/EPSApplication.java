package com.shoutz.toussaintpeterson.electronicpayslip;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import fr.quentinklein.slt.LocationTracker;
import models.CartObject;
import models.Game;
import models.LandingCategory;
import models.Ticket;
import models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by toussaintpeterson on 1/7/16.
 */
public class EPSApplication extends Application{
    private User user;
    public SharedPreferences devicePrefs;
    public SharedPreferences prefs;
    public Boolean isLoggingOut;
    public int cartNumber;
    public ArrayList<Ticket> ticketsArray;
    public ArrayList<ArrayList> masterArray;
    public ArrayList<ArrayList> tempCart;
    public ArrayList<String> masterFavorites;
    public ArrayList<ArrayList<Ticket>> editArray;
    public Typeface main;
    public Typeface sub;
    public Typeface newMain;
    public ArrayList<ArrayList> categories;
    public ArrayList<Game> games;
    public ArrayList<Game> instant;
    public ArrayList<Game> peso;
    public ArrayList<Game> numeric;
    public ArrayList<Game> letters;
    public ArrayList<Game> payout;
    public ArrayList<CartObject> cartObjects;
    public ArrayList<Ticket> masterCartObjects;
    public Boolean isChanged;
    public Bitmap gameBanner;
    public boolean wasShown;
    public boolean isSpanish;
    public boolean isInternetConnected;
    public ArrayList<Ticket> openTickets;
    public ArrayList<Ticket> unviewed;
    public ArrayList<Ticket> ready;
    public ArrayList<Ticket> winningTickets;
    public ArrayList<Ticket> pending;
    public ArrayList<Ticket> redeemed;
    public ArrayList<Ticket> allTickets;
    public Bitmap userBitmap;
    public Bitmap drawBitmap;
    public Bitmap bothBitmap;
    public Bitmap scratchImage;
    public Location userLocation;
    public String[] featured;
    public String[] newest;
    public String[] topPrize;
    public String[] topOdds;
    public String[] popular;
    public String primary;
    public String secondary;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        isLoggingOut = false;

        //findUser();

        createPreferences();

        /*if(prefs.contains("favoriteRetail")){
            if(prefs.getString("favoriteRetail", "null").equals("Ho-Chunk")){
                primary = "#6c0000";
                secondary = "#FFD700";
            }
            else{
                primary = "#132847";
                secondary = "#5b3675";
            }
        }*/

        //else{
            primary = "#132847";
            secondary = "#5b3675";
        //}

        gameBanner = null;

        wasShown = false;

        if(getResources().getString(R.string.codere).equals("true")){
            isSpanish = true;
        }
        else{
            isSpanish = false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(networkInfoPhone != null) {
            if (networkInfoPhone != null) {
                if (!(networkInfoPhone.isConnected())) {
                    isInternetConnected = false;
                } else {
                    isInternetConnected = true;
                }
            }
        }

        if (!(networkInfoWifi.isConnected())) {
            isInternetConnected = false;
        }
        else{
            isInternetConnected = true;
        }

        userBitmap = null;
        drawBitmap = null;
        bothBitmap = null;
        scratchImage = null;

        editArray = new ArrayList<ArrayList<Ticket>>();

    }

    public User getUser() {
        if(user == null){
            if(devicePrefs != null) {
                if (!(devicePrefs.getString("authtoken", "null").equals("null"))) {
                    user = getStoredUser();
                } else {
                    user = null;
                }
            }
            else{
                user = null;
            }
        }
        return user;
    }

    private User getStoredUser(){
        User user = new User();

            user.setEmail(devicePrefs.getString("email", "null"));
            user.setUsername(devicePrefs.getString("username", "null"));
            user.setPassword(devicePrefs.getString("password", "null"));
            user.setFirstname(devicePrefs.getString("firstname", "null"));
            user.setLastname(devicePrefs.getString("lastname", "null"));
            user.setBirthdate(devicePrefs.getString("birthdate", "null"));
            user.setCity(devicePrefs.getString("city", "null"));
            user.setState(devicePrefs.getString("state", "null"));
            user.setZipCode(devicePrefs.getString("zipcode", "null"));
            user.setAddress(devicePrefs.getString("address", "null"));
            user.setAuthToken(devicePrefs.getString("authtoken", "null"));
            user.setPhoneNumber(devicePrefs.getString("phone", "null"));
            if(user.getBitmapString() != null) {
                user.setBitmapString(devicePrefs.getString("bitmapString", null));
            }



        /*if(authToken != null){
            user = new User(username, password, authToken);
        }*/
        return user;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        setStoredUser(user);
    }

    public void setUser(User user) {
        this.user = user;

        if(user != null){
            setStoredUser(user);
        }
        /*else{
            removeStoredUser();
        }*/
    }

    public void setStoredUser(User user){
        if(devicePrefs == null){
            createPreferences();
        }
        SharedPreferences.Editor edit = devicePrefs.edit();
        edit.putString("username", user.getUsername()).apply();
        edit.putString("password", user.getPassword()).apply();
        edit.putString("email", user.getEmail()).apply();
        edit.putString("firstname", user.getFirstname()).apply();
        edit.putString("lastname", user.getLastname()).apply();
        edit.putString("birthdate", user.getBirthdate()).apply();
        edit.putString("city", user.getCity()).apply();
        edit.putString("state", user.getState()).apply();
        edit.putString("zipcode", user.getZipCode()).apply();
        edit.putString("address", user.getAddress()).apply();
        edit.putString("authtoken", user.getAuthToken()).apply();
        edit.putString("phone", user.getPhoneNumber()).apply();
        if(user.getBitmapString() != null) {
            edit.putString("signature", user.getBitmapString()).apply();
        }
    }

    public void removeStoredUser(){
        SharedPreferences.Editor edit = devicePrefs.edit();
        edit.remove("email").apply();
        edit.remove("username").apply();
        edit.remove("password").apply();
        edit.remove("fullname").apply();
        edit.remove("birthdate").apply();
        edit.remove("signupdate").apply();
        edit.remove("usercode").apply();
        edit.remove("signature").apply();
        edit.remove("phone").apply();
        edit.remove("authtoken").apply();
    }

    public int getCartNumber() {
        if(cartNumber < 1){
            cartNumber = 0;
        }
        else if(masterArray.isEmpty()){
            cartNumber = 0;
        }
        else{
            return cartNumber;
        }
        return cartNumber;
    }

    public void setCartNumber(int cartNumber) {
        this.cartNumber = cartNumber;
    }

    public Boolean getIsLoggingOut() {
        return isLoggingOut;
    }

    public void setIsLoggingOut(Boolean isLoggingOut) {
        this.isLoggingOut = isLoggingOut;
    }

    public void createMaster(){
        if(masterArray == null || masterArray.isEmpty()) {
            masterArray = new ArrayList<ArrayList>();
        }
    }

    public void createCart() {
        if(ticketsArray == null){
            ticketsArray = new ArrayList<Ticket>();
        }
        if(isChanged == null){
            isChanged = false;
        }

        if (cartObjects == null || cartObjects.isEmpty()) {
            cartObjects = new ArrayList<CartObject>();
        }

        if (tempCart == null || tempCart.isEmpty()){
            tempCart = new ArrayList<ArrayList>();
        }

        if (masterCartObjects == null || masterCartObjects.isEmpty()){
            masterCartObjects = new ArrayList<Ticket>();
        }
    }

    public void createFavorites(){
        if(masterFavorites == null || masterFavorites.isEmpty()) {
            masterFavorites = new ArrayList<String>();
        }

        if(user != null) {
            prefs = getApplicationContext().getSharedPreferences(user.getPhoneNumber(), 0);
            String old = prefs.getString("favorites", "null");
            StringTokenizer tokenizer = new StringTokenizer(old, ",");
            while (tokenizer.hasMoreTokens()) {
                masterFavorites.add(tokenizer.nextToken());
            }
        }
    }

    public void createGames() {
        if(games == null || games.isEmpty()){
            games = new ArrayList<Game>();
        }
    }

    public void createFonts(){
        if(main == null || sub == null) {
            main = Typeface.createFromAsset(getAssets(), "fonts/RalewayThin.ttf");
            sub = Typeface.createFromAsset(getAssets(), "fonts/RalewayBold.ttf");
        }
    }

    public String getDensity() {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public void findUser(){
        if(userLocation == null) {
            /*if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }*/
        }
            /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // You need to ask the user to enable the permissions
            } else {
                LocationTracker tracker = new LocationTracker(getApplicationContext()) {
                    @Override
                    public void onLocationFound(final Location location) {
                        try {
                            userLocation = location;
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onTimeout() {

                    }
                };
                tracker.startListening();
            }
        }*/
    }

    public void setColors(){
        if(prefs.contains("favoriteRetail")){
            if(prefs.getString("favoriteRetail", "null").equals("Ho-Chunk")){
                primary = "#6c0000";
                secondary = "#FFD700";
            }
            //else{
            //    primary = getResources().getColor(R.color.new_blue);
            //    secondary = getResources().getColor(R.color.app_purple);
            //}
        }

        else{
            primary = "#132847";
            secondary = "#5b3675";
        }
    }

    public void createPreferences(){
        devicePrefs = getApplicationContext().getSharedPreferences("device", 0);

        if(devicePrefs.contains("authtoken")){
            setUser(getStoredUser());
        }
        else{
            user = null;
        }
    }

    public void createUserPrefs(){
        if(user != null){
            prefs = getApplicationContext().getSharedPreferences(user.getPhoneNumber(), 0);
        }
    }
}
