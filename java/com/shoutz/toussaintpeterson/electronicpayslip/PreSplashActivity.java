package com.shoutz.toussaintpeterson.electronicpayslip;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Game;
import models.Ticket;
import models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by toussaintpeterson on 5/18/16.
 */
public class PreSplashActivity extends AbstractEPSActivity {
    //@Bind(R.id.logo) ImageView logo;
    private ImageView logo;
    private static double TENSION = 1500;
    private static double DAMPER = 10;
    private float mOrigY;
    private JSONObject response;
    public static final String DATEPICKER_TAG = "datepicker";
    private String[] featured;
    private String[] newest;
    private String[] topPrize;
    private String[] topOdds;
    private String[] popular;
    //private EPSApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_splash);

        getEpsApplication().createGames();
        getEpsApplication().createFonts();
        //getEpsApplication().setColors();

        if(getResources().getString(R.string.tablet_mode).equals("true")){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if(getResources().getString(R.string.kiosk).equals("true")){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if(prefs != null) {
            if (prefs.contains("spanish") && prefs.getString("spanish", "false").equals("true")) {
                getEpsApplication().isSpanish = true;
                changeLanguage();
            } else {
                getEpsApplication().isSpanish = false;
                changeLanguage();
            }
        }

        getEpsApplication().createPreferences();

        /*if(getResources().getString(R.string.codere).equals("true")){
            String languageToLoad = "es"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            getEpsApplication().isSpanish = true;
        }*/

    }

    @Override
    public void onPostResume(){
        super.onPostResume();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                PreSplashActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if(getEpsApplication().getUser() == null && prefs != null) {
                            if((!prefs.contains("authtoken")) || prefs.contains("authtoken") && prefs.getString("authtoken", "null").equals("null")){
                                Intent intent = new Intent(PreSplashActivity.this, SplashLandingActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                            else{
                                User user = new User();
                                user.setAuthToken(prefs.getString("authtoken", "null"));
                                user.setUsername(prefs.getString("phone", "null"));
                                user.setPhoneNumber(prefs.getString("phone", "null"));
                                user.setPassword(prefs.getString("password", "null"));
                                getEpsApplication().setUser(user);

                                new GameTask(getResources().getString(R.string.game_service), getApplicationContext()).execute();
                            }
                        }
                        else if(getEpsApplication().getUser() != null && (!getEpsApplication().getUser().getAuthToken().equals("null")) && getEpsApplication().devicePrefs != null){
                            //getEpsApplication().getUser().setAuthToken(prefs.getString("authtoken", "null"));
                            //Intent i = new Intent(PreSplashActivity.this, LandingActivity.class);
                            //i.putExtra("location", "presplash");
                            //startActivity(i);
                            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            new GameTask(getResources().getString(R.string.game_service), getApplicationContext()).execute();
                        }

                        else{
                            Intent intent = new Intent(PreSplashActivity.this, SplashLandingActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }
                });
            }
        }, 800);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public class GameTask extends AbstractWebService {

        public GameTask(String urlPath, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            //Toast.makeText(LandingActivity.this, "Success", Toast.LENGTH_LONG).show();
            if (!(getEpsApplication().games == null) && getEpsApplication().games.isEmpty()) {
                try {
                    JSONObject responseJSON = new JSONObject(response.toString());
                    JSONArray newGames = responseJSON.getJSONArray("game");
                    JSONObject featuredObj = responseJSON.getJSONObject("featurereel");

                    JSONArray featuredArray = featuredObj.getJSONArray("featured");
                    featured = new String[featuredArray.length()];
                    for(int j=0; j < featuredArray.length(); j++){
                        featured[j] = featuredArray.get(j).toString();
                    }
                    getEpsApplication().featured = featured;

                    JSONArray newArray = featuredObj.getJSONArray("newest");
                    newest = new String[newArray.length()];
                    for(int j=0; j < newArray.length(); j++){
                        newest[j] = newArray.get(j).toString();
                    }
                    getEpsApplication().newest = newest;

                    JSONArray topArray = featuredObj.getJSONArray("topprize");
                    topPrize = new String[topArray.length()];
                    for(int j=0; j < topArray.length(); j++){
                        topPrize[j] = topArray.get(j).toString();
                    }
                    getEpsApplication().topPrize = topPrize;

                    JSONArray oddsArray = featuredObj.getJSONArray("topodds");
                    topOdds = new String[oddsArray.length()];
                    for(int j=0; j < oddsArray.length(); j++){
                        topOdds[j] = oddsArray.get(j).toString();
                    }
                    getEpsApplication().topOdds = topOdds;

                    JSONArray popArray = featuredObj.getJSONArray("mostpopular");
                    popular = new String[popArray.length()];
                    for(int j=0; j < popArray.length(); j++){
                        popular[j] = popArray.get(j).toString();
                    }
                    getEpsApplication().popular = popular;

                    for (int i = 0; i < newGames.length(); i++) {
                        Game game = new Game();
                        game.setGameName(newGames.getJSONObject(i).getString("gameName"));
                        game.setGamePlay(newGames.getJSONObject(i).getString("gamePlay"));
                        game.setIconSet(newGames.getJSONObject(i).getString("iconSet"));
                        game.setGameId(newGames.getJSONObject(i).getString("gameId"));
                        game.setTopPrize(newGames.getJSONObject(i).getString("topPrize"));
                        game.setMaxNumber(newGames.getJSONObject(i).getString("maxNumber"));
                        game.setMaxValue(newGames.getJSONObject(i).getString("maxValue"));
                        game.setMaxWin(newGames.getJSONObject(i).getString("topPrize"));
                        game.setTemplateId(newGames.getJSONObject(i).getInt("templateid"));
                        game.setNumbersRequired(String.valueOf(newGames.getJSONObject(i).getInt("numbersRequired")));
                        if(getEpsApplication().isSpanish){
                            if(!(newGames.getJSONObject(i).getString("gameName_es").equals(""))){
                                game.setGameName(newGames.getJSONObject(i).getString("gameName_es"));
                            }
                            else{
                                game.setGameName(newGames.getJSONObject(i).getString("gameName"));
                            }
                            game.setFilterString(newGames.getJSONObject(i).getString("categoryName_es"));
                            game.setGameDescription(newGames.getJSONObject(i).getString("gameDescription_es"));
                            game.setExtendedDescription(newGames.getJSONObject(i).getString("extendedDescription_es"));
                        }
                        else {
                            game.setFilterString(newGames.getJSONObject(i).getString("categoryName"));
                            game.setGameDescription(newGames.getJSONObject(i).getString("gameDescription"));
                            game.setExtendedDescription(newGames.getJSONObject(i).getString("extendedDescription"));
                        }

                        switch (getEpsApplication().getDensity()) {
                            case "ldpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("hdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("hdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("hdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("hdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("hdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("hdpi"));
                                break;
                            case "mdpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("mdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("mdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("mdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("mdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("mdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("mdpi"));
                                break;
                            case "hdpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("hdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("hdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("hdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("hdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("hdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("hdpi"));
                                break;
                            case "xhdpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("xhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xhdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("xhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xhdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("xhdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("xhdpi"));
                                break;
                            case "xxhdpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("xxhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xxhdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("xxhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xxhdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("xxhdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("xxhdpi"));
                                break;
                            case "xxxhdpi":
                                if(getEpsApplication().isSpanish){
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl_es").getString("xxxhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xxxhdpi"));
                                }
                                else {
                                    game.setLogoURL(newGames.getJSONObject(i).getJSONObject("iconUrl").getString("xxxhdpi"));
                                    game.setIconUrl(newGames.getJSONObject(i).getJSONObject("thumbImg").getString("xxxhdpi"));
                                }
                                game.setBannerUrl(newGames.getJSONObject(i).getJSONObject("bannerImg").getString("xxxhdpi"));
                                game.setBackURL(newGames.getJSONObject(i).getJSONObject("backgroundUrl").getString("xxxhdpi"));
                                break;
                        }
                        game.setWager(newGames.getJSONObject(i).getString("wager"));
                        getEpsApplication().games.add(game);
                    }

                    if(getEpsApplication().devicePrefs != null) {
                        getEpsApplication().getUser().setAuthToken(getEpsApplication().devicePrefs.getString("authtoken", "null"));
                        getEpsApplication().createUserPrefs();

                        Intent i = new Intent(PreSplashActivity.this, LandingActivity.class);
                        i.putExtra("location", "presplash");
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                } catch (JSONException ex) {
                    Log.d("Games", ex.toString());
                }
            }
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(PreSplashActivity.this, "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            /*JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("username", email);
            params.put("password", password);*/

            JSONObject json = new JSONObject();

            response = doGet(json, param2);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void changeLanguage(){
        if(getEpsApplication().isSpanish) {
            String languageToLoad = "es"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            getEpsApplication().games.clear();

            if(prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("spanish", "true").apply();
            }
        }
        else{
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            getEpsApplication().games.clear();

            if(prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("spanish", "false").apply();
            }
        }
    }
}
