package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
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

public class LandingActivity extends AbstractEPSActivity implements SwipeRefreshLayout.OnRefreshListener, GoogleApiClient.ConnectionCallbacks, AdapterView.OnItemSelectedListener{
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @Bind(R.id.app_bar) Toolbar toolbar;
    @Bind(R.id.frame) FrameLayout frame;
    @Bind(R.id.left_drawer) LinearLayout drawer;
    @Bind(R.id.favorite) LinearLayout myTickets;
    @Bind(R.id.open_tickets_layout) LinearLayout openTicketsLayout;
    //@Bind(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.profile_layout) LinearLayout profile;
    @Bind(R.id.home_search) ImageView homeSearch;
    @Bind(R.id.cart_layout) LinearLayout cartLayout;
    @Bind(R.id.home_layout) LinearLayout homeLayout;
    @Bind(R.id.ticket_choice) Spinner ticketChoices;
    @Bind(R.id.retailer_one_layout) RelativeLayout retailerOne;
    //@Bind(R.id.scrollView) CustomScrollView scroll;
    @Bind(R.id.menu_email) TextView menuEmail;
    @Bind(R.id.menu_phone) TextView menuPhone;
    @Bind(R.id.drawer_menu) LinearLayout drawerMenu;
    @Bind(R.id.save_text) TextView saveText;
    @Bind(R.id.nearest) RelativeLayout retailer;
    @Bind(R.id.logout_text) TextView logoutText;
    @Bind(R.id.settings_text) TextView settingText;
    @Bind(R.id.ticket_text) TextView ticketText;
    @Bind(R.id.list_text) TextView listText;
    @Bind(R.id.profile_text) TextView profileText;
    @Bind(R.id.home_text) TextView homeText;
    @Bind(R.id.loading) RelativeLayout loading;
    @Bind(R.id.loading_logo) ImageView loadingLogo;
    @Bind(R.id.progressBar1) ProgressBar bar;
    //@Bind(R.id.one_tag) TextView oneTag;
    //@Bind(R.id.all_tag) TextView allTag;
    @Bind(R.id.submit_tag) TextView submitTag;
    @Bind(R.id.notifications) RelativeLayout notifications;
    @Bind(R.id.scrollView) CustomScrollView scrollView;
    @Bind(R.id.ticket_filter_layout) LinearLayout ticketFilters;
    @Bind(R.id.temp_layout) LinearLayout tempLayout;
    @Bind(R.id.purchase) RelativeLayout purchaseButton;
    @Bind(R.id.nearest_button) TextView nearestButton;
    @Bind(R.id.save_check) CheckBox saveCheck;
    private String[] languages = {"Filter By Status", "All Tickets", "Winning Tickets", "Ready To Play", "Redeemed", "Pending Tickets",
            "History"};
    private TestCartFragment cartFrag;
    private TestLandingFragment landingFrag;
    private TestOpenTicketsFragment openTicketsFrag;
    private TestRedeemFragment redeemFrag;
    private TestTicketsFragment ticketsFrag;
    private TestSelectFragment selectFrag;
    private TestSearchFragment searchFrag;
    private TestProfileFragment profileFrag;
    private TestTempCartFragment tempCartFrag;
    private TestConfirmFragment confirmFrag;
    private TestMapFragment mapFrag;
    private TestScratcherFragment scratchFrag;
    private Fragment currentFrag;
    private ArrayList<Game> games;
    private boolean isDelete;
    private ViewFlipper vf;
    private ArrayList<Game> favorites;
    public ArrayList<ArrayList> categories;
    private TestGameFragment gameFrag;
    private ArrayList<ArrayList> masterArray;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    //private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    public int cartNumber;
    private JSONObject response;
    private static String TAG;
    private boolean isBlockedScrollView;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, String> savedNumbers;


    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

        else if(currentFrag == gameFrag){
            launchLanding();
        }

        else if(currentFrag == cartFrag){
            enableScroll();
            launchTempCart();
        }

        else if(currentFrag == mapFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestMapFragment fragment = (TestMapFragment) fm.findFragmentByTag("Map");

            enableScroll();
            fragment.cancelApi();
        }

        else if(currentFrag == profileFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestProfileFragment fragment = (TestProfileFragment) fm.findFragmentByTag("Profile");

            fragment.resetProfile();
        }

        else if(currentFrag == tempCartFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestTempCartFragment fragment = (TestTempCartFragment) fm.findFragmentByTag("Temp Cart");

            fragment.back();
            //launchLanding();
        }

        else if(currentFrag == redeemFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestRedeemFragment fragment = (TestRedeemFragment) fm.findFragmentByTag("Redeem");

            fragment.back();
        }

        else if(currentFrag == searchFrag){
            launchLanding();
        }

        else if(currentFrag == ticketsFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestTicketsFragment fragment = (TestTicketsFragment) fm.findFragmentByTag("My Tickets");

            fragment.dismiss();
        }

        else if(currentFrag == openTicketsFrag){
            launchLanding();
        }

        else if(currentFrag == confirmFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestConfirmFragment fragment = (TestConfirmFragment) fm.findFragmentByTag("Confirm");

            fragment.back();
        }

        else if(currentFrag == selectFrag){
            FragmentManager fm = getSupportFragmentManager();
            TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

            fragment.resetSelect();
        }

        else {
            if (getEpsApplication().getUser() == null) {
                Intent i = new Intent(getApplicationContext(), SplashLandingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            } else if (getEpsApplication().getUser() != null && !(getEpsApplication().getUser().getUsername() == null)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this);
                builder.setTitle(getResources().getString(R.string.log_out));
                builder.setMessage(getResources().getString(R.string.log_out_sure));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEpsApplication().createFonts();
        setContentView(R.layout.final_landing_activity);
        ButterKnife.bind(this);

        favoriteGames = new ArrayList<Game>();
        masterArray = getEpsApplication().masterArray;
        landingFrag = TestLandingFragment.newInstance();
        games = getEpsApplication().games;
        favorites = new ArrayList<Game>();
        drawer.bringToFront();
        setFonts();

        //allTag.setTypeface(getEpsApplication().sub);
        //oneTag.setTypeface(getEpsApplication().sub);
        submitTag.setTypeface(getEpsApplication().sub);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTickets();
            }
        });

        if(loading.getVisibility() == View.VISIBLE){
            stopLoadingFail();
        }

        updatePrefs();
        populateFavorites();

        if(getEpsApplication().getUser() != null) {
            if (getEpsApplication().getUser().getEmail() != null && !(getEpsApplication().getUser().getEmail().equals("null"))){
                menuEmail.setText(getEpsApplication().getUser().getEmail());
            }
            else{
                menuEmail.setVisibility(View.GONE);
            }

            if(getEpsApplication().getUser().getPhoneNumber() != null){
                menuPhone.setText(getEpsApplication().getUser().getPhoneNumber());
            }
            else{
                menuPhone.setVisibility(View.GONE);
            }
        }

        else{
            menuEmail.setText("Register with PlayPort!");
            menuPhone.setVisibility(View.GONE);
        }

        if(getIntent().hasExtra("key") && getIntent().getStringExtra("key").equals("tickets")){
            launchTickets();
        }
        else if(getIntent().hasExtra("key") && getIntent().getStringExtra("key").equals("game")){
            launchGame(getIntent().getBundleExtra("bundle"));
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, landingFrag, "Landing");
            transaction.commit();
            currentFrag = landingFrag;
        }

        saveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    FragmentManager fm = getSupportFragmentManager();
                    TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

                    StringBuilder sb;

                    //this part is crashing because if the numbers are saved, the checkbox is checked and it tries
                    if (fragment.iconSet != null) {
                        switch (fragment.iconSet) {
                            case "letters":

                                if (fragment.letterValuesArray.isEmpty()) {
                                    //do nothing
                                } else {
                                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                                    saveText.startAnimation(out);

                                    switch (fragment.iconSet) {
                                        case "letters":
                                            saveText.startAnimation(out);
                                            saveText.setText("Letters Will Be Saved");
                                            saveText.startAnimation(in);
                                            break;
                                        case "numbers":
                                            saveText.startAnimation(out);
                                            saveText.setText("Numbers Will Be Saved");
                                            saveText.startAnimation(in);
                                            break;
                                        case "phone":
                                            saveText.startAnimation(out);
                                            saveText.setText("Phone Number Will Be Saved");
                                            saveText.startAnimation(in);
                                            break;
                                        case "cards":
                                            saveText.startAnimation(out);
                                            saveText.setText("Cards Will Be Saved");
                                            saveText.startAnimation(in);
                                            break;
                                        case "symbols":
                                            saveText.startAnimation(out);
                                            saveText.setText("Symbols Will Be Saved");
                                            saveText.startAnimation(in);
                                            break;
                                        case "none":
                                            saveText.startAnimation(out);
                                            saveText.setText("Save these for future plays?");
                                            saveText.startAnimation(in);
                                            break;
                                    }

                                /*sb = new StringBuilder();
                                for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                    sb.append(fragment.letterValuesArray.get(i));
                                    sb.append(", ");
                                }
                                sb.setLength(sb.length() - 2);
                                saveNumbers(fragment.gameName, sb.toString(), "save");*/
                                }
                                break;
                            case "numbers":
                                if (fragment.tileValuesArray.isEmpty()) {
                                    //do nothing
                                } else {
                                    sb = new StringBuilder();
                                    for (int i = 0; i < fragment.tileValuesArray.size(); i++) {
                                        sb.append(fragment.tileValuesArray.get(i));
                                        sb.append(", ");
                                    }
                                    sb.setLength(sb.length() - 2);
                                    saveNumbers(fragment.gameName, sb.toString(), "save");
                                }
                                break;
                            case "phone":

                                break;
                            case "cards":
                                if (fragment.letterValuesArray.isEmpty()) {
                                    //do nothing
                                } else {
                                    sb = new StringBuilder();
                                    for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                        sb.append(fragment.letterValuesArray.get(i));
                                        sb.append(", ");
                                    }
                                    sb.setLength(sb.length() - 2);
                                    saveNumbers(fragment.gameName, sb.toString(), "save");
                                }
                                break;
                            case "symbols":
                                if (fragment.letterValuesArray.isEmpty()) {
                                    //do nothing
                                } else {
                                    sb = new StringBuilder();
                                    for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                        sb.append(fragment.letterValuesArray.get(i));
                                        sb.append(", ");
                                    }
                                    sb.setLength(sb.length() - 2);
                                    saveNumbers(fragment.gameName, sb.toString(), "save");
                                }
                                break;
                        }
                    }
                }
                else{
                    resetText();
                    saveCheck.setChecked(false);
                    /*FragmentManager fm = getSupportFragmentManager();
                    TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

                    saveNumbers(fragment.gameName, fragment.savedNumbers, "delete");*/
                }
            }
        });

        isBlockedScrollView = true;

        /*if(getEpsApplication().userLocation == null){
            retailer.setAlpha(Float.valueOf(".3"));
            retailer.setEnabled(false);
        }
        else {
            retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchMap();
                }
            });
        }*/

        retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLanding();
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                TestTempCartFragment fragment = (TestTempCartFragment) fm.findFragmentByTag("Temp Cart");

                fragment.handleClick("add");
            }
        });

        MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.custom_spinner2, languages);
        ticketChoices.setAdapter(adapter);

        ticketChoices.setOnItemSelectedListener(this);

        myTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != ticketsFrag) {
                    launchTickets();
                }
                else{
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        homeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != searchFrag) {
                    enableScroll();
                    launchSearch();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != profileFrag) {
                    enableScroll();
                    launchProfileLanding();
                }
                else{
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        openTicketsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != openTicketsFrag){
                    enableScroll();
                    launchOpen();
                }
                else{
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != tempCartFrag) {
                    enableScroll();
                    launchTempCart();
                }
                else{
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag != landingFrag) {
                    launchLanding();
                }
                else{
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });


        /*vf = (ViewFlipper)findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });*/

        TAG = "Landing Activity";

        retailerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!prefs.contains("favoriteRetail")){
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("favoriteRetail", "Ho-Chunk").apply();

                    getEpsApplication().primary = "#6c0000";
                    getEpsApplication().secondary = "#FFD700";

                    toolbar.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
                    retailerOne.setBackgroundColor(getResources().getColor(R.color.white));
                    hideRetailers();

                    if(currentFrag == profileFrag){
                        FragmentManager fm = getSupportFragmentManager();
                        TestProfileFragment fragment = (TestProfileFragment) fm.findFragmentByTag("Profile");

                        fragment.changeBG();
                    }
                }
                else if(!prefs.getString("favoriteRetail", "null").equals("Ho-Chunk")){
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("favoriteRetail", "Ho-Chunk").apply();

                    getEpsApplication().primary = "#6c0000";
                    getEpsApplication().secondary = "#FFD700";

                    toolbar.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
                    retailerOne.setBackgroundColor(getResources().getColor(R.color.white));
                    hideRetailers();

                    if(currentFrag == profileFrag){
                        FragmentManager fm = getSupportFragmentManager();
                        TestProfileFragment fragment = (TestProfileFragment) fm.findFragmentByTag("Profile");

                        fragment.changeBG();
                    }
                }
                else{
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.remove("favoriteRetail").apply();

                    getEpsApplication().primary = "#132847";
                    getEpsApplication().secondary = "#5b3675";

                    toolbar.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
                    retailerOne.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
                    hideRetailers();

                    if(currentFrag == profileFrag){
                        FragmentManager fm = getSupportFragmentManager();
                        TestProfileFragment fragment = (TestProfileFragment) fm.findFragmentByTag("Profile");

                        fragment.changeBG();
                    }
                }
            }
        });


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        setupNewToolBar();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        //deleteCache(getApplicationContext());
        //onTrimMemory(TRIM_MEMORY_UI_HIDDEN);
        //onTrimMemory(TRIM_MEMORY_RUNNING_CRITICAL);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                LandingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        System.gc();
                    }
                });
            }
        }, 2000);

        if(ticketFilters.getVisibility() == View.VISIBLE && currentFrag != ticketsFrag){
            hideTicketFilters();
        }

        if(tempLayout.getVisibility() == View.VISIBLE && currentFrag != tempCartFrag){
            hideTicketFilters();
        }

        if(loading.getVisibility() == View.VISIBLE && currentFrag == landingFrag){
            loading.setVisibility(View.GONE);
        }

        //onTrimMemory(60);
    }

    @Override
    public void onResume() {
        super.onResume();
            getEpsApplication().createGames();
            new GameTask(getResources().getString(R.string.game_service), this).execute();
            //getEpsApplication().createFavorites();
        getEpsApplication().findUser();

        ticketChoices.setSelection(0);

        if(getEpsApplication().userLocation == null){
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClient.connect();

        }

        if(getEpsApplication().cartObjects == null){
            getEpsApplication().createCart();
        }

        if(getEpsApplication().masterFavorites == null || getEpsApplication().masterFavorites.isEmpty()){
            getEpsApplication().createFavorites();
            populateFavorites();
        }

        toolbar.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        //drawerMenu.setBackgroundColor(Color.parseColor(getEpsApplication().primary));

        cartNumber = getEpsApplication().cartObjects.size();
        System.gc();

        if(getEpsApplication().isSpanish){
            String languageToLoad = "es"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        else{
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private void setupNewToolBar() {
        //toolbar.bringToFront();
        setSupportActionBar(toolbar);
        toolbar.getMenu().clear();

        final CustomScrollView frame = (CustomScrollView) findViewById(R.id.scrollView);
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
                if(ticketFilters.getVisibility() == View.VISIBLE){
                    ticketFilters.setTranslationX(slideOffset * drawerView.getWidth());
                }
                if(tempLayout.getVisibility() == View.VISIBLE){
                    tempLayout.setTranslationX(slideOffset * drawerView.getWidth());
                }
                LinearLayout select = (LinearLayout)findViewById(R.id.number_select);
                if(select.getVisibility() == View.VISIBLE){
                    select.setTranslationX(slideOffset * drawerView.getWidth());
                }
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
        mDrawerToggle.syncState();
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
    private void setFonts(TextView landingTitle, TextView landingSub){
        landingTitle.setTypeface(main);
        landingSub.setTypeface(sub);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onConnected(Bundle bundle) {
        getEpsApplication().userLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        if(!item.equals("Filter By Status")) {
            FragmentManager fm = getSupportFragmentManager();
            TestTicketsFragment fragment = (TestTicketsFragment) fm.findFragmentByTag("My Tickets");

            fragment.setGrid(item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    vf.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left));
                    vf.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));
                    vf.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    vf.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right));
                    vf.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right));
                    vf.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }*/

    private void logout(){
        getEpsApplication().setUser(null);
        getEpsApplication().setIsLoggingOut(true);
        getEpsApplication().removeStoredUser();
        if(masterArray != null) {
            masterArray.clear();
        }
        if(getEpsApplication().masterFavorites != null){
            getEpsApplication().masterFavorites = null;
        }
        if(getEpsApplication().cartObjects != null) {
            getEpsApplication().cartObjects.clear();
        }

        Intent i = new Intent(getApplicationContext(), SplashLandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.v(TAG, "onTrimMemory() with level=" + level);

        // Memory we can release here will help overall system performance, and
        // make us a smaller target as the system looks for memory

        if (level >= TRIM_MEMORY_MODERATE) { // 60
            // Nearing middle of list of cached background apps; evict our
            // entire thumbnail cache
            deleteCache(getApplicationContext());
            Log.v(TAG, "evicting entire thumbnail cache");
            //mCache.evictAll();

        } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
            // Entering list of cached background apps; evict oldest half of our
            // thumbnail cache
            Log.v(TAG, "evicting oldest half of thumbnail cache");
            //mCache.trimToSize(mCache.size() / 2);
        }
    }

    private void attemptSendingQueue() throws JSONException, IOException {
        ArrayList<File> inFiles = new ArrayList<File>();
        ArrayList<JSONArray> jsonObjects = new ArrayList<JSONArray>();

        File folder = new File(getFilesDir(), "Orders");
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".json")) {
                inFiles.add(file);
            }
        }

        for (File json : inFiles) {
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

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(jsonObjects.size() > 0 && (networkInfoWifi.isConnected()) || jsonObjects.size() > 0 && ((networkInfoPhone.isConnected()))){
            new DelayedTask(getResources().getString(R.string.post_ticket_service), jsonObjects, getApplicationContext()).execute();
        }
    }

    public class DelayedTask extends AbstractWebService {
        private ArrayList<JSONArray> array;

        public DelayedTask(String urlPath, ArrayList<JSONArray> array, Context context){
            super(urlPath, true, false, context);
            this.urlPath = urlPath;
            this.array = array;
            this.context = context;
        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            File folder = new File(getFilesDir(), "Orders");
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    file.delete();
                }
            }

            Crouton.makeText(LandingActivity.this, "Connection Reestablished, tickets sent!", Style.CONFIRM).show();
        }

        @Override
        protected void onError(Object response) {
            clearLoading();
            //Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            for(JSONArray jArray : array){
                JSONObject params = new JSONObject();
                params.put("gameid", 1);
                params.put("betamount", 1);
                params.put("numbers", jArray.getJSONObject(0).getString("numbers"));

                response = doPost(params);
            }

            return response; //@todo stop using handler and use onSuccess\Error
        }
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
            if (!(getEpsApplication().games == null) && getEpsApplication().games.isEmpty()) {
                try {
                    JSONObject responseJSON = new JSONObject(response.toString());

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

    public class GetTicketsUserTask extends AbstractWebService {
        private String authtoken;

        public GetTicketsUserTask(String urlPath, String authtoken, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.authtoken = authtoken;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            try{
                JSONObject responseJSON = new JSONObject(response.toString());
                /*if(responseJSON.getJSONArray("open").length() > 0){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    notificationBell.startAnimation(shake);
                    notificationDot.setVisibility(View.VISIBLE);
                    notification = true;
                    isNotified = false;

                    showNotification("open");
                }
                else*/ if(responseJSON.getJSONArray("winning").length() > 0){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    //notificationBell.startAnimation(shake);
                    //notificationDot.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(responseJSON.getJSONArray("open").length()), Toast.LENGTH_SHORT).show();
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
            param2.put("authtoken", authtoken);

            /*JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("username", email);
            params.put("password", password);*/

            JSONObject json = new JSONObject();

            response = doGet(json, param2);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void initView(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                LandingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
                        //RelativeLayout main = (RelativeLayout)findViewById(R.id.main_screen);
                        //LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);
                        //main.startAnimation(in);
                        //bottom.startAnimation(in);
                        //main.setVisibility(View.VISIBLE);
                        //bottom.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 500);
    }

    public void launchLanding(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        if(currentFrag != landingFrag) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    LandingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }, 300);
        }
    }

    public void launchGame(Bundle bundle){
        gameFrag = TestGameFragment.newInstance();
        gameFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, gameFrag, "Game");
        transaction.addToBackStack("Game");
        transaction.commit();
        currentFrag = gameFrag;
    }

    public void launchOpen(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        if(getEpsApplication().getUser().getAuthToken().equals("null")){
            alertLogin(LandingActivity.this);
        }
        else {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    LandingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            openTicketsFrag = TestOpenTicketsFragment.newInstance();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.frame, openTicketsFrag, "Open Tickets");
                            transaction.addToBackStack("Open Tickets");
                            transaction.commit();
                            currentFrag = openTicketsFrag;
                            hideTicketFilters();
                        }
                    });
                }
            }, 300);
        }
    }

    public void launchRedeem(Bundle bundle){
        redeemFrag = TestRedeemFragment.newInstance();
        redeemFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, redeemFrag, "Redeem");
        transaction.addToBackStack("Redeem");
        transaction.commit();
        currentFrag = redeemFrag;

        hideTicketFilters();

    }

    public void launchGameBack(Bundle bundle){
        gameFrag = TestGameFragment.newInstance();
        gameFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, gameFrag, "Game");
        transaction.addToBackStack("Game");
        transaction.commit();
        currentFrag = gameFrag;
    }

    public void launchSelect(Bundle bundle){
        selectFrag = TestSelectFragment.newInstance();
        selectFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, selectFrag, "Select");
        transaction.addToBackStack("Select");
        transaction.commit();
        currentFrag = selectFrag;

        /*private void saveNumbers(String gameName, String userNumbers, String method){
            if(method.equals("enable")) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString(gameName, userNumbers).apply();

                savedNumbers.put(gameName, userNumbers);
                Crouton.makeText(getActivity(), "Numbers Saved", Style.CONFIRM);
            }
            else{
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove(gameName).apply();
                savedNumbers.remove(gameName);
                Crouton.makeText(getActivity(), "Numbers Removed", Style.CONFIRM);
            }

            //grid.setAdapter(gridViewAdapter);
            gridViewAdapter.notifyDataSetChanged();
            grid.setAdapter(gridViewAdapter);
        }*/

        /*AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) appBarLayout.getLayoutParams();
        p.setScrollFlags(0);
        appBarLayout.setLayoutParams(p);*/
    }

    public void launchSearch(){
        searchFrag = TestSearchFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, searchFrag, "Search");
        transaction.addToBackStack("Search");
        transaction.commit();
        currentFrag = searchFrag;

        hideTicketFilters();

    }

    public void launchConfirm(Bundle bundle){
        confirmFrag = TestConfirmFragment.newInstance();
        confirmFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, confirmFrag, "Confirm");
        transaction.addToBackStack("Confirm");
        transaction.commit();
        currentFrag = confirmFrag;

        hideTicketFilters();
    }

    public void launchScratch(String ticketId, String batchId, String isViewed, int templateId, String backUrl, Bundle bundle){
        Intent i = new Intent(getApplicationContext(), TestScratcherActivity.class);
        i.putExtra("ticketId",ticketId);
        i.putExtra("batchId", batchId);
        i.putExtra("viewed", isViewed);
        i.putExtra("template", templateId);
        i.putExtra("backUrl", backUrl);
        i.putExtra("bundle", bundle);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void launchProfileLanding(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        if(getEpsApplication().getUser().getAuthToken().equals("null")){
            alertLogin(LandingActivity.this);
        }
        else {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    LandingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            profileFrag = TestProfileFragment.newInstance();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.frame, profileFrag, "Profile");
                            transaction.addToBackStack("Profile");
                            transaction.commit();
                            currentFrag = profileFrag;

                            hideTicketFilters();

                        }
                    });
                }
            }, 300);
        }
    }

    public void launchCartLanding(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                LandingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        cartFrag = TestCartFragment.newInstance();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        transaction.replace(R.id.frame, cartFrag, "Cart");
                        transaction.addToBackStack("Cart");
                        transaction.commit();
                        currentFrag = cartFrag;
                        hideTicketFilters();
                    }
                });
            }
        }, 300);
    }

    public void launchMap(){
        mapFrag = TestMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, mapFrag, "Map");
        transaction.addToBackStack("Map");
        transaction.commit();
        currentFrag = mapFrag;
    }

    public void launchTickets(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        if(getEpsApplication().getUser().getAuthToken().equals("null")){
            alertLogin(LandingActivity.this);
        }
        else {
            if (getEpsApplication().getUser() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this);
                builder.setTitle("Must Be Logged In");
                builder.setMessage("In order to access my tickets, you must be a registered user!");
                builder.setPositiveButton("Ok", null);

                Dialog dialog = builder.create();
                dialog.show();
            } else {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        LandingActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                ticketsFrag = TestTicketsFragment.newInstance();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                transaction.replace(R.id.frame, ticketsFrag, "My Tickets");
                                transaction.addToBackStack("My Tickets");
                                transaction.commit();
                                currentFrag = ticketsFrag;

                            /*if(!(loading.getVisibility() == View.VISIBLE)){
                                startLoading();
                            }*/
                            }
                        });
                    }
                }, 300);
            }
        }
    }

    public void launchTempCart(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        if(getEpsApplication().getUser().getAuthToken().equals("null")){
            alertLogin(LandingActivity.this);
        }
        else {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    LandingActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            tempCartFrag = TestTempCartFragment.newInstance();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.frame, tempCartFrag, "Temp Cart");
                            transaction.addToBackStack("Temp Cart");
                            transaction.commit();
                            currentFrag = tempCartFrag;

                            hideTicketFilters();

                        }
                    });
                }
            }, 300);
        }
    }

    public void scrollToTop(){
        //NestedScrollView scroll = (NestedScrollView)findViewById(R.id.scrollView);
        if(scrollView.canScrollVertically(1)) {
            scrollView.smoothScrollTo(0,0);
        }
        appBarLayout.setExpanded(true);
    }

    public void enableScroll(){
        scrollView.setEnableScrolling(true);
    }

    public void disableScroll(){
        scrollView.setEnableScrolling(false);
    }

    public void showSelect(){
        LinearLayout select = (LinearLayout)findViewById(R.id.number_select);
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        select.startAnimation(in);
        select.setVisibility(View.VISIBLE);

        RelativeLayout submit = (RelativeLayout)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveCheck.isChecked()){
                    FragmentManager fm = getSupportFragmentManager();
                    TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

                    StringBuilder sb;

                    switch (fragment.iconSet) {
                        case "letters":
                            if(fragment.letterValuesArray.isEmpty()){
                                //do nothing
                            }
                            else {
                                Map<String, ?> keys = prefs.getAll();

                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    String current = entry.getKey();
                                    if(fragment.gameName.equals(current)){
                                        saveNumbers(fragment.gameName, fragment.savedNumbers, "delete");
                                        break;
                                    }
                                }

                                sb = new StringBuilder();
                                for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                    sb.append(fragment.letterValuesArray.get(i));
                                    sb.append(", ");
                                }
                                sb.setLength(sb.length() - 2);
                                saveNumbers(fragment.gameName, sb.toString(), "save");
                            }
                            break;
                        case "numbers":
                            if(fragment.tileValuesArray.isEmpty()){
                                //do nothing
                            }
                            else {
                                Map<String, ?> keys = prefs.getAll();

                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    String current = entry.getKey();
                                    if(fragment.gameName.equals(current)){
                                        saveNumbers(fragment.gameName, fragment.savedNumbers, "delete");
                                        break;
                                    }
                                }

                                sb = new StringBuilder();
                                for (int i = 0; i < fragment.tileValuesArray.size(); i++) {
                                    sb.append(fragment.tileValuesArray.get(i));
                                    sb.append(", ");
                                }
                                sb.setLength(sb.length() - 2);
                                saveNumbers(fragment.gameName, sb.toString(), "save");
                            }
                            break;
                        case "phone":

                            break;
                        case "cards":
                            if(fragment.letterValuesArray.isEmpty()){
                                //do nothing
                            }
                            else {
                                Map<String, ?> keys = prefs.getAll();

                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    String current = entry.getKey();
                                    if(fragment.gameName.equals(current)){
                                        saveNumbers(fragment.gameName, fragment.savedNumbers, "delete");
                                        break;
                                    }
                                }

                                sb = new StringBuilder();
                                for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                    sb.append(fragment.letterValuesArray.get(i));
                                    sb.append(", ");
                                }
                                sb.setLength(sb.length() - 2);
                                saveNumbers(fragment.gameName, sb.toString(), "save");
                            }
                            break;
                        case "symbols":
                            if(fragment.letterValuesArray.isEmpty()){
                                //do nothing
                            }
                            else {
                                Map<String, ?> keys = prefs.getAll();

                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    String current = entry.getKey();
                                    if(fragment.gameName.equals(current)){
                                        saveNumbers(fragment.gameName, fragment.savedNumbers, "delete");
                                        break;
                                    }
                                }

                                sb = new StringBuilder();
                                for (int i = 0; i < fragment.letterValuesArray.size(); i++) {
                                    sb.append(fragment.letterValuesArray.get(i));
                                    sb.append(", ");
                                }
                                sb.setLength(sb.length() - 2);
                                saveNumbers(fragment.gameName, sb.toString(), "save");
                            }
                            break;
                    }
                }
                FragmentManager fm = getSupportFragmentManager();
                TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");
                fragment.submit();
            }
        });
    }

    public void dismissSelect(){
        LinearLayout select = (LinearLayout)findViewById(R.id.number_select);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
        select.startAnimation(out);
        select.setVisibility(View.GONE);
    }

    public void showRetailers(){
        LinearLayout retailers = (LinearLayout) findViewById(R.id.retailer_select_layout);
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        retailers.startAnimation(in);
        retailers.setVisibility(View.VISIBLE);
    }

    public void showShade(){
        loading.setVisibility(View.VISIBLE);
    }

    public void hideRetailers(){
        LinearLayout retailers = (LinearLayout) findViewById(R.id.retailer_select_layout);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
        retailers.startAnimation(out);
        retailers.setVisibility(View.GONE);
    }

    public void showTicketFilters(){
        //drawer.bringToFront();
        if(currentFrag == ticketsFrag) {
            if(tempLayout.getVisibility() == View.VISIBLE){
                tempLayout.setVisibility(View.GONE);
            }
            Animation in = AnimationUtils.loadAnimation(LandingActivity.this, R.anim.fade_in);
            ticketFilters.startAnimation(in);
            ticketFilters.setVisibility(View.VISIBLE);
        }
        else{
            Animation in = AnimationUtils.loadAnimation(LandingActivity.this, R.anim.slide_in_right);
            tempLayout.startAnimation(in);
            tempLayout.setVisibility(View.VISIBLE);

            isDelete = false;
        }
    }

    public void hideTicketFilters(){
        if (tempLayout.getVisibility() == View.VISIBLE) {
            tempLayout.setVisibility(View.GONE);
        }
        if(ticketFilters.getVisibility() == View.VISIBLE) {
            ticketFilters.setVisibility(View.GONE);
        }
    }

    public void dismissFilters(){
        RelativeLayout filters = (RelativeLayout) findViewById(R.id.ticket_filter);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
        filters.startAnimation(out);
        filters.setVisibility(View.GONE);
    }

    private void setFonts(){
        logoutText.setTypeface(getEpsApplication().sub);
        settingText.setTypeface(getEpsApplication().sub);
        ticketText.setTypeface(getEpsApplication().sub);
        listText.setTypeface(getEpsApplication().sub);
        profileText.setTypeface(getEpsApplication().sub);
        homeText.setTypeface(getEpsApplication().sub);

        menuEmail.setTypeface(getEpsApplication().main);
        menuPhone.setTypeface(getEpsApplication().main);
        saveText.setTypeface(getEpsApplication().main);
    }

    public void startLoading(){
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fade.setDuration(500);
        loading.startAnimation(fade);
        loading.setVisibility(View.VISIBLE);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingLogo.setVisibility(View.VISIBLE);

                bar.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startLoadingProgressed(){
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
        in.setInterpolator(new BounceInterpolator());
        in.setDuration(800);
        in.setStartOffset(800);
        loadingLogo.startAnimation(in);
        loadingLogo.setVisibility(View.VISIBLE);

        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bar.setVisibility(View.VISIBLE);
                FragmentManager fm = getSupportFragmentManager();
                TestTicketsFragment fragment = (TestTicketsFragment) fm.findFragmentByTag("My Tickets");

                fragment.loadTickets();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void stopLoadingSuccess(){
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        bar.startAnimation(fade);
        bar.setVisibility(View.GONE);

        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation roll = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(roll);
                set.addAnimation(slide);
                set.addAnimation(fade);
                set.setDuration(600);
                loadingLogo.startAnimation(set);
                loadingLogo.setVisibility(View.GONE);
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        scrollToTop();
                        clearLoading();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void stopLoadingFail(){
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        fade.setDuration(500);
        bar.startAnimation(fade);
        bar.setVisibility(View.GONE);

        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation roll = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(roll);
                set.addAnimation(slide);
                set.addAnimation(fade);
                set.setDuration(600);
                loadingLogo.startAnimation(set);
                loadingLogo.setVisibility(View.GONE);

                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                        loading.startAnimation(fade);
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void clearLoading(){
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        bar.startAnimation(out);
        bar.setVisibility(View.GONE);

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                fade.setDuration(300);
                loading.startAnimation(fade);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public boolean isLoading(){
        if(loading.getVisibility() == View.VISIBLE && bar.getVisibility() == View.VISIBLE) {
            return true;
        }
        else{
            return false;
        }
    }

    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

// Inflating the layout for the custom Spinner
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_spinner, parent, false);

// Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout
                    .findViewById(R.id.spinner_text);

// Setting the text using the array
            tvLanguage.setText(languages[position]);

// Setting the color of the text
            tvLanguage.setTextColor(Color.rgb(75, 180, 225));
            tvLanguage.setTypeface(getEpsApplication().main);

// Declaring and Typecasting the imageView in the inflated layout
            ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

// Setting an image using the id's in the array
            //img.setImageResource(images[position]);

// Setting Special atrributes for 1st element
            if (position != 0) {
                img.setVisibility(View.GONE);
                tvLanguage.setTypeface(getEpsApplication().sub);
            }

            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

    public void switchNearest(ArrayList<String> list){
        if(list.size() == 0) {
            isDelete = false;
            Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            retailer.startAnimation(out);
            retailer.setVisibility(View.INVISIBLE);

            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    retailer.setBackgroundResource(R.drawable.rounded_layout_gold);
                    nearestButton.setText("Play More Games");

                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    retailer.startAnimation(in);
                    retailer.setVisibility(View.VISIBLE);

                    /*if (getEpsApplication().userLocation == null) {
                        retailer.setAlpha(Float.valueOf(".3"));
                        retailer.setEnabled(false);
                    } else {
                        retailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                launchMap();
                            }
                        });
                    }*/
                    retailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            launchLanding();
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        else{
            isDelete = true;
            Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            retailer.startAnimation(out);
            retailer.setVisibility(View.INVISIBLE);

            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    retailer.setBackgroundResource(R.drawable.rounded_layout_red);
                    nearestButton.setText("Delete All");

                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    retailer.startAnimation(in);
                    retailer.setVisibility(View.VISIBLE);
                    retailer.setAlpha(Float.valueOf("1"));
                    retailer.setEnabled(true);

                    retailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager fm = getSupportFragmentManager();
                            TestTempCartFragment fragment = (TestTempCartFragment) fm.findFragmentByTag("Temp Cart");

                            fragment.handleClick("delete");
                            //new TestTempCartFragment.DeleteHeldTicketsTask(getResources().getString(R.string.delete_ticket), list, "stay", getApplicationContext()).execute();
                            //purchaseButton.setEnabled(false);
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void switchDelete(){
        if(isDelete) {
            retailer.setBackgroundResource(R.drawable.rounded_layout_gold);
            nearestButton.setText("Play More Games");

            /*if (getEpsApplication().userLocation == null) {
                retailer.setAlpha(Float.valueOf(".3"));
                retailer.setEnabled(false);
            } else {
                retailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchMap();
                    }
                });
            }*/
            retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchLanding();
                }
            });
        }
    }

    public void checkSaved(){
        saveCheck.setChecked(false);
    }

    public void showHideSaveCheck(String method){
        LinearLayout save = (LinearLayout)findViewById(R.id.save_layout);

        FragmentManager fm = getSupportFragmentManager();
        TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

        switch (fragment.iconSet) {
            case "letters":
                saveText.setText("Save these letters for future plays?");
                break;
            case "numbers":
                saveText.setText("Save these numbers for future plays?");
                break;
            case "phone":
                saveText.setText("Save this phone number?");
                break;
            case "cards":
                saveText.setText("Save these cards for future plays?");
                break;
            case "symbols":
                saveText.setText("Save these symbols for future plays?");
                break;
            case "none":
                saveText.setText("Save these for future plays?");
                break;
        }

        if(save.getVisibility() == View.INVISIBLE) {
            if (method.equals("show")) {
                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(slide);
                set.addAnimation(fade);

                save.startAnimation(fade);
                save.setVisibility(View.VISIBLE);
            } else {
                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(slide);
                set.addAnimation(fade);

                save.startAnimation(fade);
                save.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void saveNumbers(String gameName, String userNumbers, String method){
            if(method.equals("save")) {
                //Toast.makeText(getApplicationContext(), userNumbers, Toast.LENGTH_LONG).show();
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString(gameName, userNumbers).apply();

                //savedNumbers.put(gameName, userNumbers);
                //Crouton.makeText(LandingActivity.this, "Numbers Saved", Style.CONFIRM).show();
            }
            else{
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove(gameName).apply();
                //savedNumbers.remove(gameName);
                //Crouton.makeText(LandingActivity.this, "Numbers Removed", Style.CONFIRM).show();
            }
    }

    public void revealSavedButton(final String gamename, final String gameId, final String wager){
        saveCheck.setChecked(false);

        RelativeLayout saved = (RelativeLayout)findViewById(R.id.saved_button);
        if(saved.getVisibility() == View.INVISIBLE) {
            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            saved.startAnimation(in);
            saved.setVisibility(View.VISIBLE);
        }

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");
                if(fragment.ticketNumberLeft > 1){
                    StringTokenizer tokenizer = new StringTokenizer(prefs.getString(gamename, "null"), ", ");
                    ArrayList<String> numbers = new ArrayList<String>();
                    while(tokenizer.hasMoreTokens()){
                        numbers.add(tokenizer.nextToken());
                    }

                    for(Game game : getEpsApplication().games){
                        if(gamename.equals(game.getGameName())){
                            String wager = game.getWager();
                            String gameId = game.getGameId();

                            Ticket ticket = new Ticket();
                            ticket.setGameId(gameId);
                            ticket.setWager(wager);
                            ticket.setNumbers(numbers);
                            fragment.gamePicks.add(ticket);

                            saveCheck.setChecked(false);

                            if(saveText.equals("Letters Will Be Saved")){
                                resetText();
                            }
                            fragment.transitionTicket();
                            break;
                        }
                    }
                }
                else{
                    StringTokenizer tokenizer = new StringTokenizer(prefs.getString(gamename, "null"), ", ");
                    ArrayList<String> numbers = new ArrayList<String>();
                    while(tokenizer.hasMoreTokens()){
                        numbers.add(tokenizer.nextToken());
                    }

                    for(Game game : getEpsApplication().games) {
                        if (gamename.equals(game.getGameName())) {
                            String wager = game.getWager();
                            String gameId = game.getGameId();

                            Ticket ticket = new Ticket();
                            ticket.setGameId(gameId);
                            ticket.setWager(wager);
                            ticket.setNumbers(numbers);
                            fragment.gamePicks.add(ticket);

                            saveCheck.setChecked(false);

                            if (saveText.equals("Letters Will Be Saved")) {
                                resetText();
                            }


                            dismissSelect();
                            startLoading();
                            new PostTicketTask(getResources().getString(R.string.post_ticket_service), fragment.gamePicks, gameId, "", getApplicationContext()).execute();
                            break;
                        }
                    }
                }
            }
        });
    }

    public class PostTicketTask extends AbstractWebService {
        private ArrayList<Ticket> gamePicks;
        private String batch;
        private String gameId;

        public PostTicketTask(String urlPath, ArrayList<Ticket> gamePicks, String gameId, String batch, Context context) {
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.gamePicks = gamePicks;
            this.batch = batch;
            this.gameId = gameId;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            launchTempCart();
        }

        @Override
        protected void onError(Object response) {
            clearLoading();
            //Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            for (Ticket ticket : gamePicks) {
                JSONObject params = new JSONObject();
                params.put("gameid", gameId);
                params.put("betamount", ticket.getWager());
                params.put("numbers", ticket.getNumbers());
                params.put("authtoken", getEpsApplication().getUser().getAuthToken());
                if (!(batch == null)) {
                    params.put("batchid", batch);
                }

                response = doPost(params);
            }

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void resetText(){
        FragmentManager fm = getSupportFragmentManager();
        TestSelectFragment fragment = (TestSelectFragment) fm.findFragmentByTag("Select");

        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);

        switch (fragment.iconSet) {
            case "letters":
                saveText.startAnimation(out);
                saveText.setText("Save these letters for future plays?");
                saveText.startAnimation(in);
                break;
            case "numbers":
                saveText.startAnimation(out);
                saveText.setText("Save these numbers for future plays?");
                saveText.startAnimation(in);
                break;
            case "phone":
                saveText.startAnimation(out);
                saveText.setText("Save this phone number?");
                saveText.startAnimation(in);
                break;
            case "cards":
                saveText.startAnimation(out);
                saveText.setText("Save these cards for future plays?");
                saveText.startAnimation(in);
                break;
            case "symbols":
                saveText.startAnimation(out);
                saveText.setText("Save these symbols for future plays?");
                saveText.startAnimation(in);
                break;
            case "none":
                saveText.startAnimation(out);
                saveText.setText("Save these for future plays?");
                saveText.startAnimation(in);
                break;
        }
    }
}
