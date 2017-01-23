package com.shoutz.toussaintpeterson.electronicpayslip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import models.Game;
import models.Ticket;
import models.User;

/**
 * Created by toussaintpeterson on 1/5/16.
 */
public class AbstractEPSActivity extends AppCompatActivity {
    private EPSApplication application;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout drawer;
    public SharedPreferences prefs;
    public Boolean isLoggingOut;
    public User user;
    public Typeface main;
    public Typeface sub;
    private ArrayList<String> masterFaves;
    public ArrayList<Game> favoriteGames;
    private ArrayList<Game> games;
    private Fragment landingFrag;
    private Fragment ticketFrag;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        isLoggingOut = false;

        if(getEpsApplication().devicePrefs == null){
            getEpsApplication().createPreferences();
        }

        user = getEpsApplication().getUser();

        if(user != null) {
            prefs = getApplicationContext().getSharedPreferences(user.getPhoneNumber(), 0);
            masterFaves = getEpsApplication().masterFavorites;
        }


        favoriteGames = new ArrayList<Game>();
        games = getEpsApplication().games;

        //if(!(getResources().getString(R.string.tablet_mode).equals("true"))) {

            main = Typeface.createFromAsset(getAssets(), "fonts/gothic.ttf");
            sub = Typeface.createFromAsset(getAssets(), "fonts/gothic.ttf");
        //}

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public EPSApplication getEpsApplication() {
        if(application==null)
            application = (EPSApplication)getApplicationContext();

        return application;
    }

    private void setupNewToolBar() {
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final RelativeLayout frame = (RelativeLayout)findViewById(R.id.app);
        mDrawerToggle = new ActionBarDrawerToggle(this,  mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                frame.setTranslationX(slideOffset * drawerView.getWidth());
                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();*/
    }

    /*public void launchProfile(View v) {
        if(!(prefs.getString("username", "null").equals("null"))) {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
        }
        else{
            alertLogin(AbstractEPSActivity.this);
        }
    }*/

    public void logout(View v) {
        /*User stored = getEpsApplication().getUser();
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("username", stored.getEmail()).apply();
        edit.putString("password", stored.getPassword()).apply();
        edit.putString("fullname", stored.getFullName()).apply();
        edit.putString("birthdate", stored.getBirthdate()).apply();
        edit.putString("signupdate", stored.getSignupDate()).apply();
        edit.putString("usercode", stored.getUserCode()).apply();
        edit.putString("email", stored.getEmail()).apply();*/

        getEpsApplication().setUser(null);
        getEpsApplication().setIsLoggingOut(true);
        getEpsApplication().removeStoredUser();

        Intent i = new Intent(getApplicationContext(), SplashLandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void setToFavorites(String gameName){
        StringBuilder str = new StringBuilder();
        if(prefs.getString("favorites", "null").equals("null") || prefs.getString("favorites", "null").equals("")){
            str.append(gameName);
            String newFaves = str.toString();

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("favorites", newFaves).apply();
            if(!(masterFaves.contains(gameName))){
                masterFaves.add(gameName);
            }
        }
        else if(!(getEpsApplication().masterFavorites.contains(gameName))){
            String old = prefs.getString("favorites", "null");
            str.append(old);
            str.append(",");
            str.append(gameName);
            String newStr = str.toString();
            String[] test = newStr.split(",");
            /*StringTokenizer tokenizer = new StringTokenizer(newStr, " ");
            getEpsApplication().masterFavorites.clear();
            while(tokenizer.hasMoreTokens()){
                getEpsApplication().masterFavorites.add(tokenizer.nextToken());
            }*/
            getEpsApplication().masterFavorites.clear();
            getEpsApplication().masterFavorites.addAll(Arrays.asList(test));
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("favorites", newStr).apply();
        }
    }

    public void removeFromFavorites(String name){
        StringBuilder str = new StringBuilder();
        String old = prefs.getString("favorites", "null");
        str.append(old);
        String newString = str.toString();
        StringTokenizer tokenizer = new StringTokenizer(newString, ",");
        while(tokenizer.hasMoreTokens()){
            if(name.equals(tokenizer.nextToken())){
                int i = str.indexOf(name);
                if(i != 0){
                    str.delete(i, i + name.length() + 1);
                }
                else {
                    str.delete(i, i + name.length() + 1);
                }
            }
        }
        String[] test = str.toString().split(",");
        String newFaves = str.toString();
        SharedPreferences.Editor edit = prefs.edit();
        StringBuilder newStr = new StringBuilder();
            for(String gameName : test){
                newStr.append(gameName);
            }
            edit.putString("favorites", newFaves.toString()).apply();
        masterFaves.clear();
        masterFaves.addAll(Arrays.asList(test));
    }

     public void populateFavorites(){
         if(!prefs.contains("favorites")){
             //SharedPreferences.Editor edit = prefs.edit();
             //edit.putString("favorites", "null").apply();
         }
         else if (!(prefs.getString("favorites","null").equals("null"))){
                String faves = prefs.getString("favorites","null");
                StringTokenizer tokenizer = new StringTokenizer(faves, ",");
                ArrayList<String> test = new ArrayList<String>();
                while(tokenizer.hasMoreTokens()){
                    test.add(tokenizer.nextToken());
                    /*Game game = new Game();
                    game.setGameName(tokenizer.nextToken());
                    game.setWager("1");
                    favoriteGames.add(game);*/
                }
                if (games != null) {
                    for(String name : test) {
                            for (Game game : games) {
                                if (game.getGameName().equals(name)) {
                                    favoriteGames.add(game);
                                }
                            }
                        }
                    }
                else{
                    Intent i = new Intent(getApplicationContext(), PreSplashActivity.class);
                    startActivity(i);
                }
            }
    }

    public void launchFavorites(View v){
            Intent i = new Intent(getApplicationContext(), CategoryViewActivity.class);
            i.putExtra("categoryName", "Favorites");
            i.putParcelableArrayListExtra("games", favoriteGames);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void launchTickets(View v){
        if(getEpsApplication().getUser() != null && getEpsApplication().getUser().getAuthToken() != null){
            /*Intent i = new Intent(getApplicationContext(), MyTicketsActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
            //((LandingActivity)getBaseContext()).switchFrag(ticketFrag, "Profile");
        }
        else{
            alertLogin(AbstractEPSActivity.this);
        }

    }

    public void launchFAQ(View v){
        Intent i = new Intent(getApplicationContext(), FAQActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void launchLegal(View v){
        Intent i = new Intent(getApplicationContext(), LegalActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void alertLogin(Context context){
        final String contextString = context.toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.login_required));
        builder.setMessage(getResources().getString(R.string.must_be));
        builder.setPositiveButton("Login/Register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), SplashLandingActivity.class);
                i.putExtra("previous", contextString);
                startActivity(i);
            }
        });
        builder.setNeutralButton("Cancel", null);

        //builder.setNegativeButton(getResources().getString(R.string.cancel), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }

    public void updatePrefs(){
        prefs = getEpsApplication().prefs;
    }

    public ArrayList<JSONArray> readFile() throws IOException, JSONException {
        ArrayList<File> inFiles = new ArrayList<File>();
        ArrayList<JSONArray> jsonObjects = new ArrayList<JSONArray>();

        File folder = new File(getFilesDir(), "Orders");
        File[] files = folder.listFiles();

        for (File file : files) {
            if(file.getName().endsWith(".json")){
                inFiles.add(file);
            }
        }

        for(File json : inFiles){
            FileInputStream fileInputStream = new FileInputStream(json);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);

                JSONArray temp = new JSONArray(responseStrBuilder.toString());
                jsonObjects.add(temp);
            }
        }

        return jsonObjects;
    }

    public void loadBitmap(int resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }

    private void initDrawer() {
        /*accessAction = (RelativeLayout)findViewById(R.id.access_action);
        accessView = (ImageView)findViewById(R.id.access_image);
        accessText = (TextView)findViewById(R.id.access_text);
        accessText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAccess(v);
            }
        });
        if(!isLoggedIn()) {
            accessView.setImageResource(R.drawable.menu_login);
            accessText.setText("Login or Sign Up");
        }
        else {
            accessView.setImageResource(R.drawable.key_logout);
            accessText.setText(getLotteryHubApplication().getUser().getUsername());
        }
        accessView.setOnTouchListener(gestureListener);*/
    }

}
