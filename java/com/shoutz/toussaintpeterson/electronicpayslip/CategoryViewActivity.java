package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.LandingCategory;
import models.Ticket;

/**
 * Created by toussaintpeterson on 1/28/16.
 */
public class CategoryViewActivity extends AbstractEPSActivity implements AdapterView.OnItemSelectedListener{
    @Bind(R.id.category_title) TextView title;
    @Bind(R.id.icon_layout) RecyclerView icons;
    @Nullable
    @Bind(R.id.cat_header) LinearLayout catHeader;
    @Bind(R.id.search_field) EditText searchField;
    @Nullable
    @Bind(R.id.quick_search_icon_two) ImageView quickSearchIcon2;
    private ArrayList<Game> games;
    private ArrayList<Game> tempGames;
    private String categoryName;
    @Nullable
    @Bind(R.id.quick_search_icon) ImageView quickSearch;
    @Nullable
    @Bind(R.id.quick_search) LinearLayout quickSearchField;
    //@Bind(R.id.more_tag) ImageView moreInfoTag;
    @Bind(R.id.cart_number) TextView cartTag;
    @Bind(R.id.category_count) TextView catCount;
    @Bind(R.id.search_icon) ImageView searchIcon;
    @Bind(R.id.app) RelativeLayout app;
    private Boolean isSearch;
    private Animation in;
    private Animation out;
    private int cartNumber;
    private int[] availableNumbers;
    private ArrayList<TextView> ballViews;
    public ArrayList<ArrayList> masterArray;
    private String gameName;
    private ArrayList<String> masterFaves;
    //@Bind(R.id.quick_pick) Button quickPick;
    List<String> listDataHeader;
    private boolean isSearching;
    HashMap<String, List<String>> listDataChild;
    //@Bind(R.id.fave_star_open) ImageView faveStarOpen;
    //@Bind(R.id.fave_star_closed) ImageView faveStarClosed;
    private String[] numbers;
    private ArrayList<String> numberChoices;
    private Spinner spinner;
    private MediaPlayer mp;
    private Boolean isActive;
    private Random r;
    private Animation slideIn;
    //@Bind(R.id.pick_4) RelativeLayout pick4;
    private Animation slideOut;
    private int ticketNumberLeft;
    private int primaryCost;
    //@Bind(R.id.select) FloatingActionButton select;
    //@Bind(R.id.quick_tag) TextView quickTitle;
    //@Bind(R.id.quickText) TextView quickText;
    MyCustomAdapter dataAdapter = null;
    private ArrayList<Ticket> gamePicks;
    private ArrayList<Integer> positons;
    private Boolean contains;
    private Boolean pickActive;
    private String altCatName;
    private ArrayList<String> filter;
    private ArrayList<Game> searchList;
    @Bind(R.id.criteria) TextView criteria;
    private boolean filtersAreOpen;

    @Override
    public void onBackPressed(){
        //EditText searchField = (EditText)findViewById(R.id.search_field);
            /*if(isSearch && filtersAreOpen){
                closeFilters();
            }
            else if(isSearch && searchField != null && searchField.getVisibility() == View.VISIBLE && categoryName.equals("Search")){
                showHideSearch();
            }
            else if(quickSearchIcon2 != null && quickSearchIcon2.getVisibility() == View.VISIBLE && quickSearchField != null){
                ObjectAnimator anim = ObjectAnimator.ofFloat(icons, "translationY", 0);
                anim.start();

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(criteria, "translationY", 0);
                anim2.start();

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        quickSearchIcon2.setVisibility(View.GONE);

                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(in);
                        set.addAnimation(fade);
                        quickSearch.startAnimation(set);
                        quickSearch.setVisibility(View.VISIBLE);

                        searchField.setText("");
                        searchField.clearFocus();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                isSearch = false;
                icons.setAdapter(new Adapter());
            }
            else {
                getEpsApplication().setCartNumber(Integer.valueOf(cartTag.getText().toString()));

                //tempGames.clear();

                Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
                finish();
            }
            //super.onBackPressed();
        //}*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryName = getIntent().getStringExtra("categoryName");
        /*if(categoryName.equalsIgnoreCase("Search")){
            setContentView(R.layout.search_view);
            isSearch = true;

            if (!(Build.VERSION.SDK_INT >= 21)) {
                RelativeLayout catHeader = (RelativeLayout)findViewById(R.id.cat_header_one);
                catHeader.bringToFront();
            }

            if(searchList == null){
                searchList = new ArrayList<Game>();
                isSearching = false;
            }
            else{
                searchList.clear();
            }
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        else {
            setContentView(R.layout.category_view);
            // setContentView(R.layout.category_view); PUT THIS BACK!
            isSearch = false;
        }*/
        ButterKnife.bind(this);
        setFonts(title, catCount);
        filtersAreOpen = false;

        //categoryName = getIntent().getStringExtra("categoryName");
        categoryName = getIntent().getStringExtra("categoryName");

        games = getEpsApplication().games;

        //if(games == null) {
        //    getEpsApplication().createGames();
       // }
        switch(categoryName){
            case("Instant Win"):
                title.setText(getResources().getString(R.string.instant_win));
                altCatName = "instantwin";
                break;
            case("Big Payout"):
                title.setText(getResources().getString(R.string.big_payout));
                altCatName = "bigpayout";
                break;
            case("Number Matching"):
                title.setText(getResources().getString(R.string.number_match));
                altCatName = "findnumbers";
                break;
            case("Letter Matching"):
                title.setText(getResources().getString(R.string.letter_match));
                altCatName = "findletters";
                break;
            case("Under Ten Pesos"):
                title.setText(getResources().getString(R.string.under_ten));
                altCatName = "undertenpesos";
                break;
            case("Favorites"):
                title.setText(getResources().getString(R.string.favorites));
                altCatName = "favorites";
                break;
            case("Search"):
                title.setText(getResources().getString(R.string.search));
                altCatName = "search";
                break;
            case("My Tickets"):
                title.setText(getResources().getString(R.string.my_tickets));
                altCatName = "mytickets";
                break;
        }

        if(categoryName.equalsIgnoreCase("Favorites")){
            populateFavorites();
            tempGames = favoriteGames;
        }
        else if(categoryName.equalsIgnoreCase("Search")){
            tempGames = games;
        }
        else if(!(getIntent().getParcelableArrayExtra("games") == null)){
            tempGames = getIntent().getParcelableArrayListExtra("games");
        }
        else{
            tempGames = new ArrayList<Game>();

//            if(categoryName.equalsIgnoreCase("Instant Win")) {
//                for (Game game : games) {
//                    String test = game.getIsFeatured();
//                    if (test.equals("true")) {
//                        tempGames.add(game);
//                    }
//                }
//            }
//            else{
            if(games == null){
                getEpsApplication().createGames();
            }
            for (Game mGame : games) {
                if (mGame.getFilterString().contains(title.getText().toString())) {
                    tempGames.add(mGame);
                }
                else if(mGame.getFilterString().contains("periodic") && categoryName.equals("Instant Win")){
                    tempGames.add(mGame);
                }
            }
//                    }
            /*for (Game mGame : games) {
                if (categoryName.equalsIgnoreCase("Number Matching") && mGame.getFilterString().contains("findnumbers")){
                    tempGames.add(mGame);
                }
                if (categoryName.equalsIgnoreCase("Letter Matching") && mGame.getFilterString().contains("findletters")){
                    tempGames.add(mGame);
                }
            }*/
//            }
                //else if(categoryName.equalsIgnoreCase(game.getCategoryName()) || categoryName.equalsIgnoreCase(game.getSubCategory())){
                //    tempGames.add(game);
               // }
        }

        if(!(tempGames.isEmpty())) {
            updateCatCount(tempGames);
        }
        else{
            catCount.setText("00");
            if(categoryName.equalsIgnoreCase("Favorites")){
                criteria.setText(getResources().getString(R.string.no_faves));
            }
            else {
                criteria.setText(getResources().getString(R.string.no_games));
            }
            criteria.setVisibility(View.VISIBLE);
        }

        cartNumber = getEpsApplication().getCartNumber();

        pickActive = false;
        masterArray = ((EPSApplication)getApplicationContext()).masterArray;
        masterFaves = getEpsApplication().masterFavorites;
        spinner = (Spinner)findViewById(R.id.number_spinner);
        slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_bottom);
        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        isActive = false;


        /*byte[] decodedString = Base64.decode(token, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView temp = new ImageView(getApplicationContext());
        temp.setLayoutParams(new GridView.LayoutParams(100, 100));
        temp.setImageBitmap(decodedByte);*/

        /*test.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                launchInfo();
            }
        });*/
        if(isSearch){
            final ImageView cartImage = (ImageView)findViewById(R.id.cart);
            cartImage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryViewActivity.this, cartImage, "reveal");
                        Intent intent = new Intent(CategoryViewActivity.this, ShoppingListViewActivity.class);
                        intent.putExtra("category", categoryName);
                        intent.putParcelableArrayListExtra("games", tempGames);
                        startActivity(intent, options.toBundle());
                    }
                    else{
                        Intent i = new Intent(CategoryViewActivity.this, ShoppingListViewActivity.class);
                        i.putExtra("category", categoryName);
                        i.putParcelableArrayListExtra("games", tempGames);
                        startActivity(i);
                        overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    }
                }
            });
        }
        else{
            final FloatingActionButton cartImage = (FloatingActionButton) findViewById(R.id.cart);
            cartImage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryViewActivity.this, cartImage, "reveal");
                        Intent intent = new Intent(CategoryViewActivity.this, ShoppingListViewActivity.class);
                        intent.putExtra("category", categoryName);
                        intent.putParcelableArrayListExtra("games", tempGames);
                        startActivity(intent, options.toBundle());
                    }
                    else{
                        Intent i = new Intent(CategoryViewActivity.this, ShoppingListViewActivity.class);
                        i.putExtra("category", categoryName);
                        i.putParcelableArrayListExtra("games", tempGames);
                        startActivity(i);
                        overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    }
                }
            });
        }

        /*if(isSearch){
                    filter = new ArrayList<String>();
                    positons = new ArrayList<Integer>();

                    setupFilters();

                    //ImageView searchTest = (ImageView)findViewById(R.id.search_test);

                    searchTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showHideSearch();
                        }
                    });

            //ATTN!!!!!! uncomment if going back to regular view

                    TextView activate = (TextView)findViewById(R.id.activate);
                    activate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView activate = (TextView)findViewById(R.id.activate);
                            activate.setVisibility(View.GONE);
                            openFilters();
                        }
                    });

                    TextView expand = (TextView)findViewById(R.id.tap);
                    expand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView activate = (TextView)findViewById(R.id.activate);
                            activate.setVisibility(View.GONE);
                            openFilters();
                        }
                    });

            //ATTN!!!!!! uncomment if going back to regular view

                ExpandableListAdapter listAdapter = new ExpandableListAdapter(CategoryViewActivity.this, listDataHeader, listDataChild);
                    final ExpandableListView expListView = (ExpandableListView)findViewById(R.id.numbers);
                    expListView.setAdapter(listAdapter);

                    expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int previousItem = -1;

                        @Override
                        public void onGroupExpand(int groupPosition) {
                            if(groupPosition != previousItem )
                                expListView.collapseGroup(previousItem );
                            previousItem = groupPosition;
                        }
                    });

                    /*TextView activate = (TextView)findViewById(R.id.activate);
                    activate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RelativeLayout filter = (RelativeLayout)findViewById(R.id.filters);
                            ObjectAnimator animIn = ObjectAnimator.ofFloat(filter, "translationY", filter.getHeight());
                            ObjectAnimator animOut = ObjectAnimator.ofFloat(icons, "translationY", filter.getHeight());
                            AnimatorSet set = new AnimatorSet();
                            set.play(animIn).with(animOut);
                            set.start();
                        }
                    });*/

                    /*Button apply = (Button)findViewById(R.id.apply_filters);
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ExpandableListAdapter listAdapter = new ExpandableListAdapter(CategoryViewActivity.this, listDataHeader, listDataChild);
                            ExpandableListView expListView = (ExpandableListView)findViewById(R.id.numbers);
                            expListView.setAdapter(listAdapter);
                            //expListView.invalidateViews();

                            if(!searchList.isEmpty()) {
                                searchList.clear();
                            }

                            expListView.collapseGroup(0);
                            expListView.collapseGroup(1);
                            expListView.collapseGroup(2);
                            expListView.collapseGroup(3);
                            expListView.collapseGroup(4);
                            expListView.collapseGroup(5);

                            searchWithFilters(filter);

                            closeFilters();
                        }
                    });
                    Button remove = (Button)findViewById(R.id.remove_filters);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ExpandableListAdapter listAdapter = new ExpandableListAdapter(CategoryViewActivity.this, listDataHeader, listDataChild);
                            ExpandableListView expListView = (ExpandableListView)findViewById(R.id.numbers);
                            expListView.setAdapter(listAdapter);

                            expListView.invalidateViews();
                            filter.clear();
                            searchList.clear();
                            closeFilters();
                        }
                    });

                    //EditText searchField = (EditText)findViewById(R.id.search_field);
                    searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    searchField.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            searchGames(searchField.getText().toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                            //searchGames(searchField.getText().toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            // TODO Auto-generated method stub

                        }

                    });

                    searchField.setOnEditorActionListener(
                            new EditText.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                            actionId == EditorInfo.IME_ACTION_DONE ||
                                            actionId == EditorInfo.IME_ACTION_NEXT ||
                                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                                        hideKeyBoard(searchField);
                                        return true;
                                    }
                                    return false;
                                }
                            });
        }*/

        if(quickSearch != null) {
            if (tempGames.size() == 0) {
                Float alpha = Float.valueOf(".3");
                quickSearch.setAlpha(alpha);
                quickSearch.setEnabled(false);
            }

            quickSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isSearch) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            catHeader.setElevation(50);
                            criteria.setElevation(50);
                            icons.setElevation(50);
                        }

                        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top_high);
                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            AnimationSet set = new AnimationSet(true);
                            set.addAnimation(out);
                            set.addAnimation(fade);
                            quickSearch.startAnimation(set);
                            quickSearch.setVisibility(View.GONE);
                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    ObjectAnimator anim = ObjectAnimator.ofFloat(icons, "translationY", quickSearchField.getHeight());
                                    anim.start();

                                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(criteria, "translationY", quickSearchField.getHeight());
                                    anim2.start();

                                    anim.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            ImageView quickSearch2 = (ImageView) findViewById(R.id.quick_search_icon_two);

                                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom_low);
                                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            AnimationSet set = new AnimationSet(true);
                                            set.addAnimation(in);
                                            set.addAnimation(fade);
                                            quickSearch2.startAnimation(set);
                                            quickSearch2.setVisibility(View.VISIBLE);

                                            searchField.setOnEditorActionListener(
                                                    new EditText.OnEditorActionListener() {
                                                        @Override
                                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                                                    actionId == EditorInfo.IME_ACTION_DONE ||
                                                                    actionId == EditorInfo.IME_ACTION_NEXT ||
                                                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                                                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                                                                //Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                                                                searchGames(searchField.getText().toString());
                                                                hideKeyBoard(searchField);
                                                                return true;
                                                            }
                                                            return false;
                                                        }
                                                    });

                                            quickSearch2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    hideKeyBoard(searchField);
                                                    searchGames(searchField.getText().toString());
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

                            if (searchList == null) {
                                searchList = new ArrayList<Game>();
                                isSearching = false;
                            } else {
                                searchList.clear();
                            }
                        }
                    }
            });
        }

        if(cartNumber == 0){
            cartTag.setVisibility(View.GONE);
        }
        else{
            DecimalFormat formatter = new DecimalFormat("00");
            String aFormatted = formatter.format(cartNumber);
            cartTag.setText(aFormatted);
        }


        //title.setText(categoryName);

        if(getResources().getString(R.string.tablet_mode).equals("true")) {
            GridLayoutManager manager = new GridLayoutManager(this, 2);

            icons.setAdapter(new Adapter());
            icons.setItemAnimator(new DefaultItemAnimator());
            icons.setLayoutManager(manager);
        }

        else if(getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_LOW || getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM){
            GridLayoutManager manager = new GridLayoutManager(this, 1);

            icons.setAdapter(new Adapter());
            icons.setItemAnimator(new DefaultItemAnimator());
            icons.setLayoutManager(manager);
        }
        else {
            GridLayoutManager manager = new GridLayoutManager(this, 1);
            icons.setAdapter(new Adapter());
            icons.setItemAnimator(new DefaultItemAnimator());
            icons.setLayoutManager(manager);
        }

        //Toast.makeText(getApplicationContext(), String.valueOf(getResources().getDisplayMetrics().densityDpi), Toast.LENGTH_SHORT).show();

        if(getResources().getString(R.string.tablet_mode).equals("true")){
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.tablet_dimen);
            icons.addItemDecoration(itemDecoration);
        }
        else if(getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_LOW || getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM){
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.small_dimen);
            VerticalSpaceItemDecoration vertical = new VerticalSpaceItemDecoration(100);
            icons.addItemDecoration(itemDecoration);
            icons.addItemDecoration(vertical);
        }
        else {
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.cardview_default_radius);
            icons.addItemDecoration(itemDecoration);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (!(Build.VERSION.SDK_INT >= 21) && getString(R.string.tablet_mode).equals("true") && isSearch) {
            RelativeLayout catHeader = (RelativeLayout)findViewById(R.id.cat_header_one);
            catHeader.bringToFront();
        }*/

        categoryName = getIntent().getStringExtra("categoryName");

        if(tempGames.isEmpty()) {
            if (categoryName.equalsIgnoreCase("Favorites")) {
                populateFavorites();
                tempGames = favoriteGames;
            } else if (categoryName.equalsIgnoreCase("Search")) {
                tempGames = games;
            } else if (!(getIntent().getParcelableArrayExtra("games") == null)) {
                tempGames = getIntent().getParcelableArrayListExtra("games");
            } else {
                tempGames = new ArrayList<Game>();

/*                for (Game mGame : games) {
                    if (categoryName.equalsIgnoreCase("Number Matching") && mGame.getFilterString().contains("findnumbers")){
                        tempGames.add(mGame);
                    }
                    if (categoryName.equalsIgnoreCase("Letter Matching") && mGame.getFilterString().contains("findletters")){
                        tempGames.add(mGame);
                    }
                }*/

//                if (categoryName.equalsIgnoreCase("Instant Win")) {
//                    for (Game game : games) {
//                        String test = game.getIsFeatured();
//                        if (test.equals("true")) {
//                            tempGames.add(game);
//                        }
//                    }
//                } else {
                    for (Game mGame : games) {
                        if (mGame.getFilterString().contains(title.getText().toString())) {
                            tempGames.add(mGame);
                        }
                   }

                //}
                //else if(categoryName.equalsIgnoreCase(game.getCategoryName()) || categoryName.equalsIgnoreCase(game.getSubCategory())){
                //    tempGames.add(game);
                // }
            }

            if (!(tempGames.isEmpty())) {
                updateCatCount(tempGames);
            } else {
                catCount.setText("00");
                if (categoryName.equalsIgnoreCase("Favorites")) {
                    String fave = getResources().getString(R.string.no_fave)+"\n"+getResources().getString(R.string.set_fave);
                    criteria.setText(fave);
                } else {
                    String game = getResources().getString(R.string.no_games)+"\n"+getResources().getString(R.string.check_back);
                    criteria.setText(game);
                }
                criteria.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //deleteCache(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //deleteCache(getApplicationContext());
    }

    private void setFonts(TextView landingTitle, TextView catCount){
        landingTitle.setTypeface(main);
        catCount.setTypeface(sub);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        String number = parent.getItemAtPosition(pos).toString();
        //updateCost(number);
        if(!(isSearch)) {
            ticketNumberLeft = Integer.valueOf(parent.getItemAtPosition(pos).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void updateCart(int number){
        getEpsApplication().setCartNumber(number);
        DecimalFormat formatter = new DecimalFormat("00");
        String aFormatted = formatter.format(number);

        //Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        //cartImage.startAnimation(shake);
        if(cartTag.getVisibility() == View.GONE){
            cartTag.setVisibility(View.VISIBLE);
        }
        cartTag.setText(aFormatted);
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private static final int ITEM_COUNT = 8;

        public Adapter() {
            super();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            if(getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_LOW || getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item_low, parent, false);
                return new ViewHolder(v);
            }
            else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item, parent, false);
                return new ViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            holder.setIsRecyclable(false);

            if(!isSearch) {
                final Game game = tempGames.get(i);
                holder.title.setText(game.getGameDescription());
                holder.subtitle.setText(game.getGameName());
            }
            else{
                final Game game = searchList.get(i);
                holder.title.setText(game.getGameDescription());
                holder.subtitle.setText(game.getGameName());
            }

            if(holder.subtitle.getText().toString().equals("Find 9")|| holder.subtitle.getText().toString().equals("Encuentra 9")){
                //byte[] decodedString = Base64.decode(getResources().getString(R.string.test), Base64.DEFAULT);
                //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //logo = decodedByte;
                //ImageView temp = new ImageView(getApplicationContext());
                //temp.setLayoutParams(new GridView.LayoutParams(100, 100));
                //temp.setImageBitmap(decodedByte);
                /*Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.tile_find_9);*/
                holder.icon.setBackgroundResource(R.drawable.tile_find_9);
                //holder.icon.setImageBitmap(decodedByte);
                //holder.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
            if(holder.subtitle.getText().toString().equals("Lucky 7's") || holder.subtitle.getText().toString().equals("Acierta 7")){
                /*Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.tile_lucky_7s);
                holder.icon.setImageBitmap(icon);*/
                holder.icon.setBackgroundResource(R.drawable.tile_lucky_7s);
            }
            if(holder.subtitle.getText().toString().equals("Last Four") || holder.subtitle.getText().toString().equals("Los 4 últimos")){
                if(getEpsApplication().isSpanish){
                    holder.icon.setBackgroundResource(R.drawable.tile_last4_es);
                }
                else {
                    holder.icon.setBackgroundResource(R.drawable.tile_last4_en);
                }
            }
            if(holder.subtitle.getText().toString().equals("Alphabet Soup") || holder.subtitle.getText().toString().equals("Sopa de Letras")){
                holder.icon.setBackgroundResource(R.drawable.tile_alphabet_soup);
            }
            if(holder.subtitle.getText().toString().equals("7 Card Cash") || holder.subtitle.getText().toString().equals("7 Card Cash")){
                holder.icon.setBackgroundResource(R.drawable.tile_7_card_cash);
            }
            /*if(holder.title.getText().toString().equals("Find 9")){
                holder.icon.setImageBitmap(null);
            }*/
            //else{
            //    holder.icon.setBackgroundResource(R.drawable.placeholder_banner);
                //logo = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder_banner);
            //}
        }

        @Override
        public int getItemCount() {
            if(!isSearch) {
                return tempGames.size();
            }
            else{
                return searchList.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                View.OnLongClickListener{
            TextView title;
            TextView subtitle;
            ImageView icon;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                subtitle = (TextView) itemView.findViewById(R.id.subtitle);
                icon = (ImageView) itemView.findViewById(R.id.banner);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(!isSearch) {
                    Intent i = new Intent(CategoryViewActivity.this, GameViewActivity.class);
                    i.putExtra("category", categoryName);
                    i.putParcelableArrayListExtra("games", tempGames);
                    i.putExtra("gameName", tempGames.get(getPosition()).getGameName());
                    i.putExtra("gameType", tempGames.get(getPosition()).getGamePlay());
                    i.putExtra("gameId", tempGames.get(getPosition()).getGameId());
                    i.putExtra("max", tempGames.get(getPosition()).getMaxNumber());
                    i.putExtra("iconSet", tempGames.get(getPosition()).getIconSet());
                    i.putExtra("wager", tempGames.get(getPosition()).getWager());
                    i.putExtra("numbersRequired", tempGames.get(getPosition()).getNumbersRequired());
                    //i.putExtra("numbersRequired", "0");
                    i.putExtra("description", tempGames.get(getPosition()).getGameDescription());
                    i.putExtra("extended_description", tempGames.get(getPosition()).getExtendedDescription());
                    i.putExtra("logoURL", tempGames.get(getPosition()).getLogoURL());
                    i.putExtra("backURL", tempGames.get(getPosition()).getBackURL());
                    i.putExtra("maxValue", tempGames.get(getPosition()).getMaxValue());

                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left); //PUT THIS BACK



                }
                else{
                    Intent i = new Intent(CategoryViewActivity.this, GameViewActivity.class);
                    i.putExtra("category", categoryName);
                    i.putParcelableArrayListExtra("games", tempGames);
                    i.putExtra("gameName", searchList.get(getPosition()).getGameName());
                    i.putExtra("gameType", searchList.get(getPosition()).getGamePlay());
                    i.putExtra("gameId", searchList.get(getPosition()).getGameId());
                    i.putExtra("max", searchList.get(getPosition()).getMaxNumber());
                    i.putExtra("iconSet", searchList.get(getPosition()).getIconSet());
                    i.putExtra("wager", searchList.get(getPosition()).getWager());
                    i.putExtra("numbersRequired", tempGames.get(getPosition()).getNumbersRequired());
                    i.putExtra("description", searchList.get(getPosition()).getGameDescription());
                    i.putExtra("extended_description", searchList.get(getPosition()).getExtendedDescription());
                    i.putExtra("logoURL", searchList.get(getPosition()).getLogoURL());
                    i.putExtra("backURL", searchList.get(getPosition()).getBackURL());
                    i.putExtra("maxValue", searchList.get(getPosition()).getMaxValue());
                    //getEpsApplication().gameBanner = ((BitmapDrawable)icon.getDrawable()).getBitmap();
                    /*if (Build.VERSION.SDK_INT >= 21) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(CategoryViewActivity.this, icon, "gameBanner");
                        startActivity(i, options.toBundle());
                    }
                    else{*/
                        startActivity(i);
                        overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    //}
                }
            }

            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        }
    }

    private void searchGames(String searchParam){
            isSearch = true;
            searchList.clear();

            for (Game game : tempGames) {
                String searchName = game.getGameName();
                String searchNameLower = game.getGameName().toLowerCase();
                if (!(searchParam.equals("")) && searchName.equalsIgnoreCase(searchParam.trim()) || !(searchParam.equals("")) && (searchName.contains(searchParam.trim()))
                        || !(searchParam.equals("")) && (searchNameLower.equalsIgnoreCase(searchParam)) || !(searchParam.equals("")) && (searchNameLower.contains(searchParam))) {
                    searchList.add(game);
                }
            }

            icons.setAdapter(new Adapter());
            updateCatCount(searchList);

            if (criteria != null && searchList.isEmpty() && !(searchParam.equals(""))) {
                criteria.setVisibility(View.VISIBLE);
            } else if (searchList.isEmpty() && !(searchParam.equals(""))) {
                criteria.setText("");
            } else {
                criteria.setVisibility(View.GONE);
            }
    }

    private void showKeyBoard(EditText target){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyBoard(EditText target){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
    }

    private void updateCatCount(ArrayList<Game> list){
        DecimalFormat formatter = new DecimalFormat("00");
        String aFormatted = formatter.format(list.size());
        if(list.size() != 0) {
            catCount.setText(String.valueOf(aFormatted));
        }
        else{
            catCount.setText("00");
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if(getResources().getString(R.string.tablet_mode).equals("true")){
                outRect.set(mItemOffset, 10, mItemOffset, mItemOffset);
            }
            else {
                outRect.set(mItemOffset, -100, mItemOffset, mItemOffset);
            }
        }
    }

    /*private void dismissCart(){
        Animation out = AnimationUtils.loadAnimation(CategoryViewActivity.this, R.anim.abc_slide_out_bottom);
        cartImage.startAnimation(out);
        cartImage.setVisibility(View.GONE);
    }
    private void activateCart(){
        Animation in = AnimationUtils.loadAnimation(CategoryViewActivity.this, R.anim.abc_slide_in_bottom);
        cartImage.startAnimation(in);
        cartImage.setVisibility(View.VISIBLE);
    }*/

    /*public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;
        //private CheckBox check;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_sub_list, null);
            }

                final TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItem);

                txtListChild.setText("");
                txtListChild.setTypeface(sub);

                txtListChild.setText(childText);

                if(filter.contains(childText) && txtListChild.getText().toString().equals(childText)){
                    ImageView check = (ImageView)convertView.findViewById(R.id.check);
                    check.setVisibility(View.VISIBLE);
                }
                else{
                    ImageView check = (ImageView)convertView.findViewById(R.id.check);
                    check.setVisibility(View.GONE);
                }

                //CheckBox check = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //CheckBox check = (CheckBox) view.findViewById(R.id.check);
                        String param = txtListChild.getText().toString();
                        if(filter.contains(param)){
                            int position = filter.indexOf(param);
                            filter.remove(position);
                            ImageView check = (ImageView)view.findViewById(R.id.check);
                            check.setVisibility(View.GONE);
                            //check.setChecked(false);
                        }
                        else {
                            //check.setTag(childPosition);
                            //check.setChecked(true);
                            ImageView check = (ImageView)view.findViewById(R.id.check);
                            check.setVisibility(View.VISIBLE);
                            filter.add(txtListChild.getText().toString());
                            //Toast.makeText(getApplicationContext(), txtListChild.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox check = (CheckBox) view.findViewById(R.id.check);
                        String param = txtListChild.getText().toString();
                        if(check.isChecked()){
                            check.setChecked(true);
                            filter.add(txtListChild.getText().toString());
                            Toast.makeText(getApplicationContext(), txtListChild.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            int position = filter.indexOf(param);
                            filter.remove(position);
                            check.setChecked(false);
                        }
                    }
                });*/
                //subShown = true;

            //return convertView;
        //}

        /*@Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_filter, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.cell_title);
            lblListHeader.setText(headerTitle);


            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }*/

    private class MyCustomAdapter extends ArrayAdapter<String> {

        private ArrayList<String> stringList;
        private int childPosition;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<String> filterList, int childPosition) {
            super(context, textViewResourceId, filterList);
            this.stringList = new ArrayList<String>();
            this.stringList.addAll(filterList);
            this.childPosition = childPosition;
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public String getItem(int position) {
            return stringList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            position++;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.filter_layout, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                /*holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());
                    }
                });*/
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            int test = position;
            if(positons.isEmpty()) {
                positons.add(test);
            }
            else{
                if(positons.contains(test)){
                    test++;
                    positons.add(position);
                }
            }


            String fiter = stringList.get(test);
            //Country country = countryList.get(position);
            //holder.code.setText(" (" +  country.getCode() + ")");
            holder.name.setText(fiter);
            //holder.name.setChecked(country.isSelected());
            //holder.name.setTag(country);

            return convertView;

        }

    }

    private void setupFilters(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(getResources().getString(R.string.play_style));
        listDataHeader.add(getResources().getString(R.string.odds));
        listDataHeader.add(getResources().getString(R.string.theme));
        listDataHeader.add(getResources().getString(R.string.top_prize));
        listDataHeader.add(getResources().getString(R.string.draw_freq));
        listDataHeader.add(getResources().getString(R.string.price));

        ArrayList<String> playList = new ArrayList<String>();
        playList.addAll(Arrays.asList(getResources().getStringArray(R.array.play_styles)));
        listDataChild.put(listDataHeader.get(0), playList);
        ArrayList<String> oddsList = new ArrayList<String>();
        oddsList.addAll(Arrays.asList(getResources().getStringArray(R.array.odds)));
        listDataChild.put(listDataHeader.get(1), oddsList);
        ArrayList<String> themeList = new ArrayList<String>();
        themeList.addAll(Arrays.asList(getResources().getStringArray(R.array.theme)));
        listDataChild.put(listDataHeader.get(2), themeList);
        ArrayList<String> prizeList = new ArrayList<String>();
        prizeList.addAll(Arrays.asList(getResources().getStringArray(R.array.top_prize)));
        listDataChild.put(listDataHeader.get(3), prizeList);
        ArrayList<String> drawList = new ArrayList<String>();
        drawList.addAll(Arrays.asList(getResources().getStringArray(R.array.frequency)));
        listDataChild.put(listDataHeader.get(4), drawList);
        ArrayList<String> priceList = new ArrayList<String>();
        priceList.addAll(Arrays.asList(getResources().getStringArray(R.array.price)));
        listDataChild.put(listDataHeader.get(5), priceList);
    }

    private void checkFilter(String[] array, String param){
        for(String string : array){
            if(filter.contains(string)) {
                filter.remove(string);
            }
        }
        filter.add(param);
    }

    private void searchWithFilters(ArrayList<String> params){
        for(String string : params){
            for(Game game : tempGames) {
                if (game.getFilterString().contains(string) && !(searchList.contains(game))) {
                    searchList.add(game);
                }
            }
        }
        icons.setAdapter(new Adapter());
        updateCatCount(searchList);
    }
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (Build.VERSION.SDK_INT >= 21) {
                            startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }

    /*private void showHideSearch() {
        if(categoryName.equals("Search")) {
            if (!isSearching) {
                final ImageView search = (ImageView) findViewById(R.id.search_test);
                //final EditText searchField = (EditText) findViewById(R.id.search_field);
                TextView searchString = (TextView) findViewById(R.id.search_string);
                if (searchString.getVisibility() == View.VISIBLE) {
                    searchString.setVisibility(View.GONE);
                }
                ObjectAnimator out = ObjectAnimator.ofFloat(search, "translationX", -searchField.getWidth() - 10);
                out.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchField.setVisibility(View.VISIBLE);
                        searchField.requestFocus();

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        isSearching = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                out.start();
            } else {
                final ImageView search = (ImageView) findViewById(R.id.search_test);
                //final EditText searchField = (EditText) findViewById(R.id.search_field);
                searchField.setVisibility(View.GONE);
                TextView searchString = (TextView) findViewById(R.id.search_string);
                if (searchString.getVisibility() == View.VISIBLE) {
                    searchString.setVisibility(View.GONE);
                }
                ObjectAnimator out = ObjectAnimator.ofFloat(search, "translationX", 0);
                out.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        isSearching = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                out.start();
            }
        }
    }*/

    /*private void closeFilters(){
        RelativeLayout options = (RelativeLayout)findViewById(R.id.options_layout);
        RecyclerView icons = (RecyclerView)findViewById(R.id.icon_layout);
        Button apply = (Button)findViewById(R.id.apply_filters);
        Button remove = (Button)findViewById(R.id.remove_filters);
        TextView expand = (TextView)findViewById(R.id.tap);
        TextView activate = (TextView)findViewById(R.id.activate);
        activate.setVisibility(View.VISIBLE);
        expand.setVisibility(View.VISIBLE);
        apply.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        filtersAreOpen = false;
        //options.setVisibility(View.INVISIBLE);
        //options.bringToFront();
        ObjectAnimator anim = ObjectAnimator.ofFloat(options, "translationY", 0);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(icons, "translationY", 0);
        anim.start();
        anim2.start();

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(searchList != null && searchList.isEmpty()){
                    TextView search = (TextView)findViewById(R.id.search_string);
                    search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }

    private void openFilters(){
        RelativeLayout options = (RelativeLayout)findViewById(R.id.options_layout);
        RecyclerView icons = (RecyclerView)findViewById(R.id.icon_layout);
        TextView search = (TextView)findViewById(R.id.search_string);
        TextView expand = (TextView)findViewById(R.id.tap);
        expand.setVisibility(View.GONE);
        Button apply = (Button)findViewById(R.id.apply_filters);
        Button remove = (Button)findViewById(R.id.remove_filters);
        search.setVisibility(View.GONE);
        apply.setVisibility(View.VISIBLE);
        remove.setVisibility(View.VISIBLE);
        filtersAreOpen = true;
        //options.setVisibility(View.VISIBLE);
        //options.bringToFront();
        if(getString(R.string.tablet_mode).equals("true")){
            ObjectAnimator anim = ObjectAnimator.ofFloat(options, "translationY", options.getHeight() - 50);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(icons, "translationY", options.getHeight() - 50);
            anim.start();
            anim2.start();
        }
        else {
            ObjectAnimator anim = ObjectAnimator.ofFloat(options, "translationY", options.getHeight());
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(icons, "translationY", options.getHeight());
            anim.start();
            anim2.start();
        }
    }*/

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}
