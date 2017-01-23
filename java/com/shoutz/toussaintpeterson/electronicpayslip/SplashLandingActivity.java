package com.shoutz.toussaintpeterson.electronicpayslip;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.transition.Slide;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import net.hockeyapp.android.CrashManager;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.User;

/**
 * Created by toussaintpeterson on 1/5/16.
 */
public class SplashLandingActivity extends AbstractEPSActivity {
    private TextView headerText;
    private TextView start;
    private TextView brought;
    private TextView description;
    private Animation animOut;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout welcome;
    private RelativeLayout enter;
    private ImageView logo;
    private TabLayout.Tab one;
    private User current;
    private ObjectAnimator anim;
    private Button login;
    private Button register;
    private Switch language;
    private TextView guest;
    private TextView languageTag;
    private boolean isSpanish;
    private boolean networkConnected;
    private boolean wifiConnected;
    private ImageView loadingLogo;
    //private ArrayList<Game> games;
    private ProgressBar bar;
    private Button facebook;
    private Button google;
    private Button twitter;
    private JSONObject response;
    private RelativeLayout loading;
    private EditText phone;
    private EditText pass;
    public String[] featured;
    public String[] newest;
    public String[] topPrize;
    public String[] topOdds;
    public String[] popular;
    private KeyStore mKeyStore;
    private boolean isFingerprint;
    //private FingerprintAuthenticationDialogFragment mFragment;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyGenerator keyGenerator;
    private KeyStore keyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal mCancellationSignal;
    private boolean mSelfCancelled;
    private TextView fingerId;
    private static final String KEY_NAME = "example_key";
    private RelativeLayout touch;
    private AlertDialog dialog;
    private static int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;
    private boolean isFingerprintAuthAvailable;
    private FingerprintHandler helper;

    @Override
    public void onBackPressed(){
        if(touch.getVisibility() == View.VISIBLE){
            Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
            touch.startAnimation(out);
            touch.setVisibility(View.GONE);
        }
        else {
            this.finishAffinity();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //REMOVE THIS TO MAKE LOCALE AUTO
        super.onCreate(savedInstanceState);

        //initView();
        String test = getString(R.string.tablet_mode);
        setContentView(R.layout.final_splash_landing);

        loading = (RelativeLayout)findViewById(R.id.loading);
        phone = (EditText)findViewById(R.id.phone);
        pass = (EditText)findViewById(R.id.pass);
        loadingLogo = (ImageView)findViewById(R.id.loading_logo);
        bar = (ProgressBar)findViewById(R.id.progressBar1);
        fingerId = (TextView)findViewById(R.id.fingerId);
        User user = getEpsApplication().getUser();
        touch = (RelativeLayout)findViewById(R.id.touch_info);
        isFingerprint = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(networkInfoPhone != null) {
            if (!(networkInfoPhone.isConnected())) {
                networkConnected = false;
            } else {
                networkConnected = true;
            }
        }

        if (!(networkInfoWifi.isConnected())) {
            wifiConnected = false;
        }
        else{
            wifiConnected = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.USE_FINGERPRINT},
                        FINGERPRINT_PERMISSION_REQUEST_CODE);
            }
            //createKey();
            //Fingerprint API only available on from Android 6.0 (M)
                fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);

                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    fingerId.setVisibility(View.GONE);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    pass.setLayoutParams(param);
                }

                else {
                    fingerId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (cipherInit()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    isFingerprint = true;
                                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashLandingActivity.this);
                                    builder.setTitle("Fingerprint Authentication");
                                    builder.setMessage("Place Thumb on Scanner to Proceed");
                                    builder.setIcon(R.drawable.fingerprint_icon);
                                /*builder.setPositiveButton("Fingerprint", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startListening(cryptoObject);
                                    }
                                });*/
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            helper.cancelAuth();
                                            isFingerprint = false;
                                        }
                                    });

                                    dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        }
                    });

                    keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                    if (!keyguardManager.isKeyguardSecure()) {
                        Toast.makeText(this,
                                "Lock screen security not enabled in Settings",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    generateKey();

                    if (cipherInit()) {
                        isFingerprint = true;
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        helper = new FingerprintHandler(this);
                        helper.startAuth(fingerprintManager, cryptoObject);
                    }
                }

                /*else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                }*/
            }
            else{
                fingerId.setVisibility(View.GONE);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                pass.setLayoutParams(param);
            }

        guest = (TextView)findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (networkConnected || wifiConnected) {
                    updateGames("guest");
                }
                else{
                    Intent i = new Intent(SplashLandingActivity.this, NetworkActivity.class);
                    startActivity(i);
                }
            }
        });

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getEpsApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(login.getWindowToken(), 0);


                if((phone.getText().toString().equals(""))){
                    Crouton.makeText(SplashLandingActivity.this, "Must Enter A Phone Number", Style.ALERT).show();
                }
                else if((pass.getText().toString().equals(""))){
                    Crouton.makeText(SplashLandingActivity.this, "Must Enter A Valid Password", Style.ALERT).show();
                }

                else {
                    if ((networkConnected || wifiConnected)) {
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        loading.startAnimation(fade);
                        loading.setVisibility(View.VISIBLE);

                        Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                        drop.setDuration(800);
                        drop.setInterpolator(new BounceInterpolator());
                        loadingLogo.startAnimation(drop);
                        loadingLogo.setVisibility(View.VISIBLE);
                        drop.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                bar.setVisibility(View.VISIBLE);
                                new LoginTask(getResources().getString(R.string.login_service), phone.getText().toString(), pass.getText().toString(), getApplicationContext()).execute();

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    } else {
                        Intent i = new Intent(SplashLandingActivity.this, NetworkActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

        /*register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (networkConnected || wifiConnected) {
                    updateGames("register");
                }
                else{
                    Intent i = new Intent(SplashLandingActivity.this, NetworkActivity.class);
                    startActivity(i);
                }
            }
        });*/

        /*if(getResources().getString(R.string.tablet_mode).equals("true")){
            headerText = (TextView)findViewById(R.id.header_text);
        }*/
        welcome = (LinearLayout)findViewById(R.id.welcome_view);
        logo = (ImageView)findViewById(R.id.logo);
        animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_bottom);
        login.setTypeface(getEpsApplication().sub);
        guest.setTypeface(getEpsApplication().sub);
    }

@Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(networkInfoPhone != null) {
            if (!(networkInfoPhone.isConnected())) {
                networkConnected = false;
            } else {
                networkConnected = true;
            }
        }

        if (!(networkInfoWifi.isConnected())) {
            wifiConnected = false;
        }
        else{
            wifiConnected = true;
        }
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }


    public class LoginTask extends AbstractWebService {
        private String phone, password, passwordConfirm;

        public LoginTask(String urlPath, String phone, String password, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.phone = phone;
            this.password = password;
            this.passwordConfirm = passwordConfirm;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            //loading.setVisibility(View.GONE);
            try {
                JSONObject responseJSON = new JSONObject(response.toString());
                User user = new User();
                user.setAuthToken(responseJSON.getString("sectoken"));
                user.setUsername(phone);
                user.setPhoneNumber(phone);
                user.setPassword(password);
                getEpsApplication().setUser(user);

                getEpsApplication().createUserPrefs();
                updatePrefs();
                SharedPreferences.Editor editor = getEpsApplication().devicePrefs.edit();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManager.isHardwareDetected()) {
                    if(!getEpsApplication().devicePrefs.contains("fingerLog")) {
                        SharedPreferences.Editor edit = getEpsApplication().devicePrefs.edit();
                        edit.putString("fingerLog", phone).apply();
                        edit.putString("fingerPass", password).apply();
                    }
                }

                if(responseJSON.has("firstname")) {
                    if (!responseJSON.getString("firstname").equals("")) {
                        user.setFirstname(responseJSON.getString("firstname"));
                        editor.putString("firstname", responseJSON.getString("firstname")).apply();
                    } else {
                        user.setFirstname("null");
                    }
                    if (!responseJSON.getString("lastname").equals("")) {
                        user.setLastname(responseJSON.getString("lastname"));
                        editor.putString("lastname", responseJSON.getString("lastname")).apply();
                    } else {
                        user.setLastname("null");
                    }
                    if (!responseJSON.getString("dob").equals("")) {
                        user.setBirthdate(responseJSON.getString("dob"));
                        editor.putString("birthdate", responseJSON.getString("dob")).apply();
                    } else {
                        user.setBirthdate("null");
                    }
                    if (!responseJSON.getString("address1").equals("")) {
                        user.setAddress(responseJSON.getString("address1"));
                        editor.putString("address", responseJSON.getString("address1")).apply();
                    } else {
                        user.setAddress("null");
                    }
                    if (!responseJSON.getString("city").equals("")) {
                        user.setCity(responseJSON.getString("city"));
                        editor.putString("city", responseJSON.getString("city")).apply();
                    } else {
                        user.setCity("null");
                    }
                    if (!responseJSON.getString("stateprovince").equals("")) {
                        user.setState(responseJSON.getString("stateprovince"));
                        editor.putString("state", responseJSON.getString("stateprovince")).apply();
                    } else {
                        user.setState("null");
                    }
                    if (!responseJSON.getString("postalcode").equals("")) {
                        user.setZipCode(responseJSON.getString("postalcode"));
                        editor.putString("zipcode", responseJSON.getString("postalcode")).apply();
                    } else {
                        user.setZipCode("null");
                    }
                    if (!responseJSON.getString("email1").equals("")) {
                        user.setEmail(responseJSON.getString("email1"));
                        editor.putString("email", responseJSON.getString("email1")).apply();
                    } else {
                        user.setEmail("null");
                    }
                }

                editor.putString("password", password).apply();
                editor.putString("username", phone).apply();
                editor.putString("phone", phone).apply();
                editor.putString("authtoken", responseJSON.getString("sectoken")).apply();

                updateGames("login");
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            if(response != null) {
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                loading.startAnimation(fade);
                loading.setVisibility(View.GONE);

                try {
                    JSONObject responseJSON = new JSONObject(response.toString());
                    if(responseJSON.getString("logmessage").equals("User not found")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashLandingActivity.this);
                        builder.setTitle("PlayPort");
                        builder.setMessage("Register As a New User?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new TestTask(getResources().getString(R.string.registration_service), phone, password, password, getApplicationContext()).execute();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Crouton.makeText(SplashLandingActivity.this, "Error Logging In", Style.ALERT).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } catch (JSONException ex) {

                }
            }
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            params.put("username", phone);
            params.put("password", password);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void changeLanguage(){
        if(isSpanish) {
            String languageToLoad = "es"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            if(getEpsApplication().games != null) {
                getEpsApplication().games.clear();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("spanish", "true").apply();
        }
        else{
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

            if(getEpsApplication().games != null) {
                getEpsApplication().games.clear();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("spanish", "false").apply();
        }
    }

    Resources getResourcesByLocale(Context context, String localeName ) {
        Resources res = context.getResources();
        Configuration conf = new Configuration(res.getConfiguration());
        conf.locale = new Locale(localeName);
        return new Resources(res.getAssets(), res.getDisplayMetrics(), conf);
    }

    private void updateGames(String clicked){
        new GameTask(getString(R.string.game_service), getApplicationContext(), clicked).execute();
    }

    public class GameTask extends AbstractWebService {
        private String clicked;

        public GameTask(String urlPath, Context context, String clicked){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.clicked = clicked;
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

                    JSONArray newGames = responseJSON.getJSONArray("game");
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
                    //Toast.makeText(getApplicationContext(), String.valueOf(games.size()), Toast.LENGTH_SHORT).show();
                } catch (JSONException ex) {
                    Log.d("Games", ex.toString());
                }
            }
            switch(clicked){
                case "register":
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if(isFingerprint) {
                            helper.cancelAuth();
                        }
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                    else{
                        if(isFingerprint) {
                            helper.cancelAuth();
                        }
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                    break;
                case "login":
                    //Intent j = new Intent(SplashLandingActivity.this, LandingActivity.class);
                    //j.putExtra("location", "login");
                    //startActivity(j);
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    animateLoadingScreen();
                    break;
                case "guest":
                    if((!(getEpsApplication().devicePrefs.contains("firstTime")) && getResources().getString(R.string.codere).equals("true") || getEpsApplication().isSpanish)){
                        if (Build.VERSION.SDK_INT >= 21) {
                            if(isFingerprint) {
                                helper.cancelAuth();
                            }

                            User user = new User();
                            user.setAuthToken("null");
                            user.setUsername("Guest");
                            user.setPhoneNumber(sessionIdentifierGenerator(12));
                            getEpsApplication().setUser(user);

                            getEpsApplication().createUserPrefs();
                            updatePrefs();

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashLandingActivity.this,
                                    new Pair(logo, "moveLogo"));
                            Intent intent = new Intent(SplashLandingActivity.this, WalkthroughActivity.class);
                            intent.putExtra("method", "guest");
                            startActivity(intent, options.toBundle());
                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                        }
                        else{
                            if(isFingerprint) {
                                helper.cancelAuth();
                            }

                            User user = new User();
                            user.setAuthToken("null");
                            user.setUsername("Guest");
                            user.setPhoneNumber(sessionIdentifierGenerator(12));
                            getEpsApplication().setUser(user);

                            getEpsApplication().createUserPrefs();
                            updatePrefs();

                            Intent intent = new Intent(SplashLandingActivity.this, WalkthroughActivity.class);
                            startActivity(intent);
                            intent.putExtra("method", "guest");
                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                        }
                    }
                    else {
                        if((!(getEpsApplication().devicePrefs.contains("firstTime")))) {
                            if(isFingerprint) {
                                helper.cancelAuth();
                            }
                            User user = new User();
                            user.setAuthToken("null");
                            user.setUsername("Guest");
                            user.setPhoneNumber(sessionIdentifierGenerator(12));
                            getEpsApplication().setUser(user);

                            getEpsApplication().createUserPrefs();
                            updatePrefs();

                            Intent i = new Intent(SplashLandingActivity.this, TestIntroSlides.class);
                            SharedPreferences.Editor edit = getEpsApplication().devicePrefs.edit();
                            //getEpsApplication().setUser(null);
                            edit.putString("email", "null").apply();
                            edit.putString("username", "null").apply();
                            i.putExtra("method", "guest");
                            startActivity(i);
                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                        }
                        else{
                            if(isFingerprint) {
                                helper.cancelAuth();
                            }

                            User user = new User();
                            user.setAuthToken("null");
                            user.setUsername("Guest");
                            user.setPhoneNumber(sessionIdentifierGenerator(12));
                            getEpsApplication().setUser(user);

                            getEpsApplication().createUserPrefs();
                            updatePrefs();

                            Intent i = new Intent(SplashLandingActivity.this, LandingActivity.class);
                            SharedPreferences.Editor edit = getEpsApplication().devicePrefs.edit();
                            edit.putString("email", "null").apply();
                            edit.putString("username", "null").apply();
                            startActivity(i);
                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                        }
                    }
                    break;
            }
        }

        @Override
        protected void onError(Object response) {
//            Toast.makeText(LandingActivity.this, "Fail", Toast.LENGTH_LONG).show();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void alertConnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashLandingActivity.this);
        builder.setTitle(getResources().getString(R.string.no_internet));
        builder.setMessage(getResources().getString(R.string.internet_message));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               SplashLandingActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                SplashLandingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
                        RelativeLayout main = (RelativeLayout)findViewById(R.id.splash_main);
                        main.startAnimation(in);
                        main.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 800);
    }

    private void animateLoadingScreen() {
            login.setEnabled(false);
            guest.setEnabled(false);
            //register.setEnabled(false);

            Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            out.setStartOffset(1200);
            bar.startAnimation(out);

            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bar.setVisibility(View.GONE);

                    int test = loading.getTop() - loadingLogo.getHeight();
                    ObjectAnimator anim = ObjectAnimator.ofFloat(loadingLogo, "translationY", test + 20);
                    anim.setDuration(800);
                    anim.setStartDelay(500);
                    anim.start();

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            Animation up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            AnimationSet set = new AnimationSet(true);
                            set.addAnimation(up);
                            set.addAnimation(in);

                            TextView welcome = (TextView) findViewById(R.id.welcome_text);
                            welcome.startAnimation(set);
                            welcome.setVisibility(View.VISIBLE);

                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    if (getIntent().hasExtra("previous")) {
                                        Intent i = new Intent(SplashLandingActivity.this, com.shoutz.toussaintpeterson.electronicpayslip.LandingActivity.class);
                                        i.putExtra("location", "login");
                                        startActivity(i);
                                    } else if ((!(getEpsApplication().devicePrefs.contains("firstTime")) && getResources().getString(R.string.codere).equals("true")) || getEpsApplication().isSpanish) {
                                        if (Build.VERSION.SDK_INT >= 21) {
                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashLandingActivity.this,
                                                    new Pair(logo, "moveLogo"));
                                            if(isFingerprint) {
                                                helper.cancelAuth();
                                            }
                                            Intent intent = new Intent(SplashLandingActivity.this, WalkthroughActivity.class);
                                            startActivity(intent, options.toBundle());
                                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                        } else {
                                            if(isFingerprint) {
                                                helper.cancelAuth();
                                            }
                                            Intent intent = new Intent(SplashLandingActivity.this, WalkthroughActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                        }
                                    } else {
                                        if(isFingerprint) {
                                            helper.cancelAuth();
                                        }
                                        Intent i = new Intent(SplashLandingActivity.this, LandingActivity.class);
                                        i.putExtra("location", "login");
                                        startActivity(i);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    public class TestTask extends AbstractWebService {
        private String phone, password, passwordConfirm;

        public TestTask(String urlPath, String email, String password,
                        String passwordConfirm, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.phone = email;
            this.password = password;
            this.passwordConfirm = passwordConfirm;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            try {
                JSONObject responseObj = new JSONObject(response.toString());

                SharedPreferences.Editor editor = getEpsApplication().devicePrefs.edit();
                User user = new User();
                user.setFirstname("null");
                user.setLastname("null");
                user.setBirthdate("null");
                user.setCity("null");
                user.setAddress("null");
                user.setState("null");
                user.setZipCode("null");
                user.setEmail("null");
                user.setPassword(password);
                editor.putString("password", password).apply();
                editor.putString("phone", phone).apply();
                user.setPhoneNumber(phone);
                user.setUsername(phone);
                user.setAuthToken(responseObj.getString("sectoken"));
                editor.putString("authtoken", responseObj.getString("sectoken")).apply();

                getEpsApplication().setUser(user);

                getEpsApplication().createUserPrefs();
                updatePrefs();

                updateGames("register");
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            try {
                JSONObject responseJSON = new JSONObject(response.toString());
                Crouton.makeText(SplashLandingActivity.this, responseJSON.getString("logmessage"), Style.ALERT).show();
            }
            catch(JSONException ex){

            }

            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            loading.startAnimation(fade);
            loading.setVisibility(View.GONE);
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            params.put("phone1", phone);
            params.put("username", phone);
            params.put("password", password);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
            }
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;
        }
        catch (KeyPermanentlyInvalidatedException e) {
            return false;
        }

        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException
                | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SplashLandingActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            keyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        fingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("E", "Authentication error " + errorCode + " " + errString);
                //...
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
                Log.e("E", "Authentication help message thrown " + helpCode + " " + helpString);
                //...
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.e("E", "Authentication succeeded");
                //...
            }

            /*
             * Called when authentication failed but the user can try again
             * When called four times - on the next fail onAuthenticationError(FINGERPRINT_ERROR_LOCKOUT)
             * will be called
             */
            /*@Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e("E", "Authentication failed");
                //...
            }
        }, null);
        //fingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags *///, Callback, null);
    //}

    /*public boolean isFingerprintAuthAvailable() {
        return fingerprintManager.isHardwareDetected()
                && fingerprintManager.hasEnrolledFingerprints();
    }*/

    public void cancelFinger(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void loginFinger(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }

        if(getEpsApplication().devicePrefs.contains("fingerLog")){
            if ((networkConnected || wifiConnected)) {
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                loading.startAnimation(fade);
                loading.setVisibility(View.VISIBLE);

                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                drop.setDuration(800);
                drop.setInterpolator(new BounceInterpolator());
                loadingLogo.startAnimation(drop);
                loadingLogo.setVisibility(View.VISIBLE);
                drop.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        bar.setVisibility(View.VISIBLE);
                        if(isFingerprint) {
                            helper.cancelAuth();
                        }
                        new LoginTask(getResources().getString(R.string.login_service), getEpsApplication().devicePrefs.getString("fingerLog", "null"), getEpsApplication().devicePrefs.getString("fingerPass", "null"), getApplicationContext()).execute();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            } else {
                Intent i = new Intent(SplashLandingActivity.this, NetworkActivity.class);
                startActivity(i);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No Username or Password on file", Toast.LENGTH_LONG).show();
        }
    }

    private String sessionIdentifierGenerator(int length) {
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i=0; i<length; i++) //12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }
}
