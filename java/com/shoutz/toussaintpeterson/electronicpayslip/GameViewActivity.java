package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
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
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import models.CartObject;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 1/9/16.
 */
public class GameViewActivity extends AbstractEPSActivity implements AdapterView.OnItemSelectedListener{
    private String gameName;
    @Bind(R.id.info) TextView info;
    @Bind(R.id.number) TextView number;
    @Bind(R.id.cost) TextView price;
    @Bind(R.id.select) FloatingActionButton select;
    @Bind(R.id.number_spinner) Spinner spinner;
    @Bind(R.id.quick_pick) FloatingActionButton quickPick;
    @Bind(R.id.quick_play) FloatingActionButton quickPlay;
    @Bind(R.id.cost_numbers) TextView costNumber;
    @Bind(R.id.counter) TextView counter;
    private int primaryCost;
    private ObjectAnimator cartPulse;
    @Bind(R.id.number_selection_grid) GridView grid;
    @Bind(R.id.symbol_selection_grid) GridView symbolGrid;
    private int[] availableNumbers;
    private ArrayList<TextView> ballViews;
    private ArrayList<String> numberChoices;
    private Random r;
    private String[] numbers;
    private String[] choiceList;
    @Bind(R.id.done_pick) FloatingActionButton donePick;
    @Bind(R.id.main_game) RelativeLayout mainGame;
    @Bind(R.id.select_numbers) RelativeLayout pick;
    private int ticketNumberLeft;
    public int cartNumber;
    private String wager;
    @Bind(R.id.more_info) RelativeLayout moreInfo;
    @Bind(R.id.add_to_list) Button addToList;
    @Bind(R.id.more_info_scroll) ScrollView moreScroll;
    @Bind(R.id.fave_star_open) FloatingActionButton faveStarOpen;
    @Bind(R.id.fave_star_closed) FloatingActionButton faveStarClosed;
    @Bind(R.id.clear_list) Button clearList;
    @Bind(R.id.review_layout) RelativeLayout reviewLayout;
    @Bind(R.id.digit_one) TextView digitOne;
    @Bind(R.id.digit_two) TextView digitTwo;
    @Bind(R.id.digit_three) TextView digitThree;
    @Bind(R.id.digit_four) TextView digitFour;
    @Bind(R.id.one) LinearLayout one;
    @Bind(R.id.two) LinearLayout two;
    @Bind(R.id.three) LinearLayout three;
    @Bind(R.id.four) LinearLayout four;
    @Bind(R.id.five) LinearLayout five;
    @Bind(R.id.six) LinearLayout six;
    @Bind(R.id.seven) LinearLayout seven;
    @Bind(R.id.eight) LinearLayout eight;
    @Bind(R.id.nine) LinearLayout nine;
    @Bind(R.id.zero) LinearLayout zero;
    @Bind(R.id.done_pick4) FloatingActionButton donePick4;
    @Bind(R.id.sample_ticket) ImageView sampleTicket;
    private ObjectAnimator pulse;
    private ObjectAnimator scaleDown;
    private ObjectAnimator scaleDown2;
    private ArrayList<TextView> textView;
    private Boolean subShown;
    private ArrayList<TextView> tiles;
    public ArrayList<ArrayList> categories;
    private ArrayList<String> masterFaves;
    private Boolean favorite;
    public ArrayList<ArrayList> masterArray;
    private ArrayList<Ticket> gamePicks;
    private Boolean contains;
    private ObjectAnimator gameNameMove;
    private String pastCat;
    private ArrayList<Game> pastGames;
    public Boolean isChanged;
    private String categoryName;
    private ArrayList<Integer> tileValuesArray;
    private ArrayList<String> letterValuesArray;
    private int maxNumber;
    private int numberLeft;
    private String gameType;
    private String iconSet;
    private String gameId;
    @Bind(R.id.countdown) TextView countdown;
    private String counterNumber;
    List<String> listDataHeader;
    //@Bind(R.id.cart_tag) TextView cartTag;
    @Bind(R.id.preview_float) ImageView previewFloat;
    @Bind(R.id.quickpick_tag) TextView quickTag;
    @Bind(R.id.select_tag) TextView selectTag;
    @Bind(R.id.fave_tage) TextView faveTag;
    HashMap<String, List<String>> listDataChild;
    private ArrayList<String> iconSetString;
    private ArrayList<String> chosenIconSetString;
    private String shortDesc;
    private int left;
    private String extended;
    @Bind(R.id.more_info_field) TextView moreInfoText;
    //@Bind(R.id.view_cart)
    FloatingActionButton viewCart;
    @Bind(R.id.info_float)
    ImageView infoFloat;
    @Bind(R.id.app_card) RelativeLayout mainGameView;//test
    @Bind(R.id.sample_tix) RelativeLayout sample;//test
    @Bind(R.id.plus) ImageView plus;
    @Bind(R.id.minus) ImageView minus;
    @Bind(R.id.new_counter) TextView newCounter;
    @Bind(R.id.cost_tag) TextView costTag;
    @Bind(R.id.game_logo) ImageView logo;
    private int maxNumberChoice;
    private Bitmap icon;
    private ArrayList<Integer> cardPos;

    @Override
    public void onBackPressed(){
        if(pick.getVisibility() == View.VISIBLE){
            dismissPick();
        }
        else if(sample.getVisibility() == View.VISIBLE){
            hideSample();
        }
        else if(!(info.getVisibility() == View.VISIBLE)){
            launchInfo();
        }
        else {
            if(!(gamePicks.isEmpty())){
                //addtoMaster();
            }

            getEpsApplication().gameBanner = null;

            if(icon != null){
                icon.recycle();
            }

            Intent i = new Intent(getApplicationContext(), CategoryViewActivity.class);
            i.putExtra("categoryName", categoryName);
            i.putParcelableArrayListExtra("games", pastGames);
            /*if(Build.VERSION.SDK_INT >= 21) {
                supportFinishAfterTransition();
            }
            else {*/
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
                finish();
            //}
            //super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.game_view); //layout.new_game_view
        ButterKnife.bind(this);

        gameName = getIntent().getStringExtra("gameName");

        maxNumberChoice = Integer.valueOf(getIntent().getStringExtra("maxValue"));

        String logoURL = getIntent().getStringExtra("logoURL");

        dynamicSetIcon();
        dynamicSetBack();

        //ImageView banner = (ImageView)findViewById(R.id.game_banner);


        if(getEpsApplication().masterArray == null){
            getEpsApplication().createMaster();
        }

        masterFaves = getEpsApplication().masterFavorites;
        figureFavorite();

        select.setVisibility(View.VISIBLE);
        quickPlay.setVisibility(View.VISIBLE);
        //cartTag.setTypeface(main);
        quickTag.setTypeface(main);
        selectTag.setTypeface(main);
        faveTag.setTypeface(main);

        TextView name = (TextView) findViewById(R.id.game_name);
        wager = getIntent().getStringExtra("wager");
        gameName = getIntent().getStringExtra("gameName");
        gameType = getIntent().getStringExtra("gameType");
        iconSet = getIntent().getStringExtra("iconSet");
        gameId = getIntent().getStringExtra("gameId");

        previewFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout buttons = (LinearLayout)findViewById(R.id.game_buttons);
                LinearLayout spinner = (LinearLayout)findViewById(R.id.new_spinner);
                RelativeLayout info = (RelativeLayout)findViewById(R.id.info_layout);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                buttons.startAnimation(fade);
                spinner.startAnimation(fade);
                info.startAnimation(fade);
                info.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                buttons.setVisibility(View.INVISIBLE);

                fade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Bitmap icon;
                        ImageView sample;
                        switch(gameName){
                            case "Find 9":
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_find9);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                            case "Alphabet Soup":
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_alphabetsoup);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                            case "Lucky 7's":
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_lucky7s);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                            case "7 Card Cash":
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_7cardcash);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                            case "Last Four":
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_last4);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                            default:
                                icon = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.gp_last4);
                                sample = (ImageView)findViewById(R.id.sample_ticket);
                                sample.setImageBitmap(icon);
                                break;
                        }
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                        RelativeLayout tix = (RelativeLayout)findViewById(R.id.sample_tix);
                        tix.startAnimation(in);
                        tix.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        TextView gameInstructions = (TextView)findViewById(R.id.game_instructions_text);
        switch(gameType){
            case("number"): gameInstructions.setText(getResources().getString(R.string.select_numbers));
                break;
            case("letter"): gameInstructions.setText(getResources().getString(R.string.select_letters));
                break;
            case("card"): gameInstructions.setText(getResources().getString(R.string.select_cards));
                break;
        }

        if(gameName.equalsIgnoreCase("Last 4") && user == null || gameName.equalsIgnoreCase("Last 4") && prefs.getString("phoneNumber", "null").equals("null")){
            Float alpha = Float.valueOf(".4");
            quickPlay.setEnabled(false);
            quickPlay.setAlpha(alpha);
            quickPick.setAlpha(alpha);
            quickPick.setEnabled(false);
        }

        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                incrementNewCounter("plus");
                return false;
            }
        });
        minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                incrementNewCounter("minus");
                return false;
            }
        });

        if(getIntent().getStringExtra("description") != null) {
            shortDesc = getIntent().getStringExtra("description");
        }
        else{
            shortDesc = getResources().getString(R.string.bacon_ipsum);
        }

        if(getIntent().getStringExtra("extended_description") != null){
            extended = getIntent().getStringExtra("extended_description");
        }
        else{
            extended = getResources().getString(R.string.extended_ipsum);
        }

        info.setText(shortDesc);
        moreInfoText.setText(extended);

        maxNumber = Integer.valueOf(getIntent().getStringExtra("max"));
        name.setText(gameName);
        costNumber.setText(wager);
        isChanged = getEpsApplication().isChanged;
        subShown = false;

        categories = getEpsApplication().categories;
        categoryName = getIntent().getStringExtra("category");
        gamePicks = new ArrayList<Ticket>();
        pastCat = getIntent().getStringExtra("category");
        pastGames = getIntent().getParcelableArrayListExtra("games");
        cartNumber = getEpsApplication().getCartNumber();

        disableButtons();

        if(gameType.equals("symbol")){
            iconSetString = new ArrayList<String>();
            chosenIconSetString = new ArrayList<String>();

            String one = getResources().getString(R.string.testImport1);
            String two = getResources().getString(R.string.testImport2);
            String three = getResources().getString(R.string.testImport3);
            String oneColor = getResources().getString(R.string.testImportSelect1);
            String twoColor = getResources().getString(R.string.testImportSelect2);
            String threeColor = getResources().getString(R.string.testImportSelect3);
            iconSetString.add(one);
            iconSetString.add(two);
            iconSetString.add(three);
            chosenIconSetString.add(oneColor);
            chosenIconSetString.add(twoColor);
            chosenIconSetString.add(threeColor);
        }

        scaleDown = ObjectAnimator.ofPropertyValuesHolder(quickPlay,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(500);

        scaleDown.setRepeatCount(5);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown2 = ObjectAnimator.ofPropertyValuesHolder(select,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown2.setDuration(500);

        scaleDown2.setRepeatCount(5);
        scaleDown2.setRepeatMode(ObjectAnimator.REVERSE);

        clearList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                clearSelections();
                gamePicks = new ArrayList<Ticket>();
                dismissPick();
            }
        });


        addToList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addtoMaster();
                dismissPick();
            }
        });

        if (!(Build.VERSION.SDK_INT >= 21)) {
            RelativeLayout info = (RelativeLayout)findViewById(R.id.info_layout);
            info.setBackgroundResource(R.drawable.edittext_border);
        }

        viewCart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(cartPulse != null && cartPulse.isRunning()) {
                    cartPulse.end();
                }
                if(scaleDown != null && scaleDown.isRunning()){
                    scaleDown.end();
                    scaleDown2.end();
                }
                if(gamePicks.size() != 0){
                    addtoMaster();
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(GameViewActivity.this, viewCart, "reveal");
                    Intent intent = new Intent(GameViewActivity.this, ShoppingListViewActivity.class);
                    intent.putExtra("category", pastCat);
                    intent.putParcelableArrayListExtra("games", pastGames);
                    startActivity(intent, options.toBundle());
                    if(icon != null) {
                        icon.recycle();
                    }
                }
                else{
                    Intent i = new Intent(GameViewActivity.this, ShoppingListViewActivity.class);
                    i.putExtra("category", pastCat);
                    i.putParcelableArrayListExtra("games", pastGames);
                    startActivity(i);
                    if(icon != null) {
                        icon.recycle();
                    }
                }
            }
        });

        if(getIntent().getStringExtra("numbersRequired").equals("0")){
            quickPick.setEnabled(false);
            quickPlay.setEnabled(false);
            select.setEnabled(false);

            Float alpha = Float.valueOf(".3");
            quickPick.setAlpha(alpha);
            quickPlay.setAlpha(alpha);
            select.setAlpha(alpha);

            //TextView cartTag = (TextView)findViewById(R.id.cart_tag);
            //cartTag.setText("Add to List");

            viewCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.valueOf(newCounter.getText().toString()) > 0){
                        ArrayList<String> numbers = new ArrayList<String>();
                        numbers.add("");

                        ticketNumberLeft = Integer.valueOf(newCounter.getText().toString());
                        while(ticketNumberLeft > 0){
                            Ticket ticket = new Ticket();
                            if(user != null && user.getUsername() != null) {
                                ticket.setUserName(user.getUsername());
                            }
                            else{
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(primaryCost));
                            ticket.setGameName(gameName);
                            gamePicks.add(ticket);
                            //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));

                            updateCart(gamePicks.size());
                            ticketNumberLeft--;
                        }

                        addtoMaster();

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(GameViewActivity.this, viewCart, "reveal");
                            Intent intent = new Intent(GameViewActivity.this, ShoppingListViewActivity.class);
                            intent.putExtra("category", pastCat);
                            intent.putParcelableArrayListExtra("games", pastGames);
                            startActivity(intent, options.toBundle());
                            if(icon != null) {
                                icon.recycle();
                            }
                        }
                        else {
                            Intent i = new Intent(GameViewActivity.this, ShoppingListViewActivity.class);
                            i.putExtra("category", pastCat);
                            i.putParcelableArrayListExtra("games", pastGames);
                            startActivity(i);
                            if (icon != null) {
                                icon.recycle();
                            }
                        }
                    }
                }
            });
        }

        cartNumber = getEpsApplication().getCartNumber();
        //cartImage = (ImageView)findViewById(R.id.cart);
        masterArray = ((EPSApplication)getApplicationContext()).masterArray;

        gameName = getIntent().getStringExtra("gameName");
        if(masterFaves.contains(gameName)){
            favorite = true;
        }
        else{
            favorite = false;
        }

        if(!favorite){
            faveStarOpen.setVisibility(View.VISIBLE);
            faveStarClosed.setVisibility(View.GONE);
        }
        else {
            faveStarOpen.setVisibility(View.GONE);
            faveStarClosed.setVisibility(View.VISIBLE);
        }

        if(gameName.equals("Last Four")){
            info.setText("Last 4 is an instant win game! Enter the last 4 digits of your phone number and see if you've won!");
            TextView moreInfo = (TextView)findViewById(R.id.more_info_field);
            moreInfo.setText("Last 4 is an instant play game! Enter the last 4 digits of your phone number and select how many tickets you would like to purchase. When your ticket is printed, you will immediately see if you've won!");
        }

        faveStarOpen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                faveStarOpen.setVisibility(View.GONE);
                faveStarClosed.setVisibility(View.VISIBLE);
                favorite = true;
                setToFavorites(gameName);
            }
        });
        faveStarClosed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                faveStarOpen.setVisibility(View.VISIBLE);
                faveStarClosed.setVisibility(View.GONE);
                favorite = false;
                removeFromFavorites(gameName);
            }
        });


        primaryCost = Integer.valueOf(((TextView) findViewById(R.id.cost_numbers)).getText().toString());
        numbers = getResources().getStringArray(R.array.number_select1);
        cardPos = new ArrayList<Integer>();

        numberChoices = new ArrayList<String>();

        setIconSet();

        r = new Random();

        availableNumbers = new int[maxNumber];
        //ballViews = new ArrayList<TextView>();

        CustomAdapter dataAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, numbers, 0);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);

        /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, numbers); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_list_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);*/

        //for(int i = 0; i < availableNumbers.length; i++){
        //    LinearLayout layout = (LinearLayout)findViewById(R.id.number_line);
        //    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);
        //    params.setMargins(10, 0, 0, 0);

        //    ballViews.add(ball);
        //}

        if(gameType.equals("letter") || gameType.equals("number")) {
            grid.setAdapter(new BallAdapter(this));
            symbolGrid.setVisibility(View.GONE);
        }
        else if(gameType.equals("phone")){
            grid.setVisibility(View.GONE);
            symbolGrid.setVisibility(View.GONE);
        }
        else {
            symbolGrid.setAdapter(new SymbolAdapter(this));
            grid.setVisibility(View.GONE);
        }

        info.setTypeface(sub);
        moreInfoText.setTypeface(sub);
        number.setTypeface(sub);
        price.setTypeface(sub);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        donePick4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> numbers = new ArrayList<String>();
                if(gamePicks == null) {
                    gamePicks = new ArrayList<Ticket>();
                }
                for (int i = 0; i < textView.size(); i++) {
                    if (tileValuesArray.size() < maxNumber) {
                        Crouton.makeText(GameViewActivity.this, getString(R.string.missing), Style.ALERT).show();
                        return;
                    } else {
                        numbers.add(tileValuesArray.get(i).toString());
                    }
                }
                while(ticketNumberLeft > 0){
                    Ticket ticket = new Ticket();
                    if(user != null && user.getUsername() != null) {
                        ticket.setUserName(user.getUsername());
                    }
                    else{
                        ticket.setUserName("null");
                    }
                    ticket.setNumbers(numbers);
                    ticket.setGameId(gameId);
                    ticket.setWager(String.valueOf(primaryCost));
                    ticket.setGameName(gameName);
                    gamePicks.add(ticket);
                    //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));

                    updateCart(gamePicks.size());
                    ticketNumberLeft--;
                }

                clearNumbers();

                showVerification();
            }
        });

        donePick.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ArrayList<String> numbers = new ArrayList<String>();
                if(gamePicks == null) {
                    gamePicks = new ArrayList<Ticket>();
                }
                if(gameType.equals("number")) {
                    for (int i = 0; i < availableNumbers.length; i++) {
                        if (tileValuesArray.size() < maxNumber) {
                            Crouton.makeText(GameViewActivity.this, getString(R.string.missing), Style.ALERT).show();
                            return;
                        } else {
                            numbers.add(tileValuesArray.get(i).toString());
                        }
                    }
                }
                else if(gameType.equals("letter") || gameType.equals("card") || gameType.equals("symbol")){
                    for (int i = 0; i < availableNumbers.length; i++) {
                        if (letterValuesArray.size() < maxNumber) {
                            Crouton.makeText(GameViewActivity.this, getString(R.string.missing), Style.ALERT).show();
                            return;
                        } else {
                            numbers.add(letterValuesArray.get(i));
                        }
                    }
                }

                if(ticketNumberLeft > 1) {
                    Collections.sort(numbers);
                    Ticket ticket = new Ticket();
                    if(user != null && user.getUsername() != null) {
                        ticket.setUserName(user.getUsername());
                    }
                    else{
                        ticket.setUserName("null");
                    }
                    ticket.setNumbers(numbers);
                    ticket.setGameId(gameId);
                    ticket.setWager(String.valueOf(primaryCost));
                    ticket.setGameName(gameName);

                    gamePicks.add(ticket);
                    ticketNumberLeft--;

                    if(gameType.equals("number")) {
                        tileValuesArray.clear();
                    }
                    else if(gameType.equals("letter") || gameType.equals("card") || gameType.equals("symbol")){
                        letterValuesArray.clear();
                    }
                    if(gameType.equals("letter") || gameType.equals("number")) {
                        grid.setAdapter(new BallAdapter(GameViewActivity.this));
                        symbolGrid.setVisibility(View.GONE);
                    }
                    else {
                        symbolGrid.setAdapter(new SymbolAdapter(GameViewActivity.this));
                        grid.setVisibility(View.GONE);
                    }

                    for(int i=0; i<maxNumber; i++){
                        numberLeft++;
                        counter.setText(new DecimalFormat("00").format(numberLeft));
                    }

                    if(ticketNumberLeft == 1){
                        Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(spin);
                        set.addAnimation(fade);
                        //set.setFillAfter(true);
                        donePick.startAnimation(set);
                        donePick.setVisibility(View.GONE);
                        set.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                                quickPick.startAnimation(drop);
                                quickPick.setVisibility(View.VISIBLE);

                                Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                AnimationSet set = new AnimationSet(false);
                                set.addAnimation(spin);
                                set.addAnimation(fade);
                                set.setFillAfter(true);
                                counter.startAnimation(set);
                                counter.setVisibility(View.VISIBLE);
                                //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                    else {
                        Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(spin);
                        set.addAnimation(fade);
                        //set.setFillAfter(true);
                        donePick.startAnimation(set);
                        donePick.setVisibility(View.GONE);
                        set.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                                quickPick.startAnimation(drop);
                                quickPick.setVisibility(View.VISIBLE);

                                Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                AnimationSet set = new AnimationSet(false);
                                set.addAnimation(spin);
                                set.addAnimation(fade);
                                set.setFillAfter(true);
                                counter.startAnimation(set);
                                counter.setVisibility(View.VISIBLE);
                                //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_white_24dp));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        //donePick.setVisibility(View.GONE);
                        //quickPick.setVisibility(View.VISIBLE);
                        //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_white_24dp));
                    }
                    clearNumbers();
                    return;

                }
                else{
                    Collections.sort(numbers);
                    Ticket ticket = new Ticket();
                    if(user != null && user.getUsername() != null) {
                        ticket.setUserName(user.getUsername());
                    }
                    else{
                        ticket.setUserName("null");
                    }
                    ticket.setNumbers(numbers);
                    ticket.setGameId(gameId);
                    ticket.setWager(String.valueOf(primaryCost));
                    ticket.setGameName(gameName);
                    gamePicks.add(ticket);
                    //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));

                    updateCart(gamePicks.size());
                    clearNumbers();

                    showVerification();
                    //dismissPick();
                }
            }
        });


        infoFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(moreInfo.getVisibility() == View.VISIBLE)) {
                    launchInfo();
                }
                else{
                    dismissInfo();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    gamePicks.clear();
        masterArray = ((EPSApplication)getApplicationContext()).masterArray;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //deleteCache(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        String number = parent.getItemAtPosition(pos).toString();
        updateCost(number);
        ticketNumberLeft = Integer.valueOf(parent.getItemAtPosition(pos).toString());

        if(cartPulse != null && cartPulse.isRunning()){
            cartPulse.end();

            quickPick.setEnabled(true);
            quickPlay.setEnabled(true);
            select.setEnabled(true);
        }

        if(!number.equals("00")){
            quickPick.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    quickPick();
                }
            });

            quickPlay.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    numberLeft = maxNumber;

                    if (gameType.equals("number") || gameType.equals("phone")) {
                        tileValuesArray = new ArrayList<Integer>();
                    } else if (gameType.equals("letter") || gameType.equals("card") || gameType.equals("symbol")) {
                        letterValuesArray = new ArrayList<String>();
                    }
                    quickPick();
                }
            });

            select.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    launchPick();
                }
            });

            /*Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            arrow.startAnimation(slide);
            arrow.setVisibility(View.GONE);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(scaleDown, scaleDown2);
                    set.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });*/
        }
    }

    private void showVerification(){
        RelativeLayout phone = (RelativeLayout)findViewById(R.id.pick_4);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        if(pick.getVisibility() == View.VISIBLE) {
            pick.startAnimation(out);
            pick.setVisibility(View.GONE);
        }
        else if(phone.getVisibility() == View.VISIBLE){
            phone.startAnimation(out);
            phone.setVisibility(View.GONE);
        }

        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        //in.setStartOffset(300);
        //in.setDuration(500);
        reviewLayout.startAnimation(in);
        reviewLayout.setVisibility(View.VISIBLE);

        GridLayoutManager manager = new GridLayoutManager(GameViewActivity.this, 1);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(GameViewActivity.this, listDataHeader, listDataChild);


        int ticketNumber = 1;

        for(Ticket ticket : gamePicks){
            String ticketName = "Ticket " + ticketNumber++;
            listDataHeader.add(ticketName);

            if(!gameType.equals("phone")) {
                Collections.sort(ticket.getNumbers());
            }

            if(gameType.equals("number") || gameType.equals("letter") || gameType.equals("phone")) {
                StringBuffer buffer = new StringBuffer();
                Iterator iter = ticket.getNumbers().iterator();
                while (iter.hasNext()) {
                    buffer.append(iter.next());
                    if (iter.hasNext()) {
                        buffer.append("  -  ");
                    }
                }
                List<String> temp = new ArrayList<String>();
                temp.add(buffer.toString());

                int position = listDataHeader.indexOf(ticketName);

                listDataChild.put(listDataHeader.get(position), temp);
            }

            else {
                //this is where the position is set. Look for a way to use this (position) to get card value (I.E. ASpade, AClub).
                StringBuffer buffer = new StringBuffer();
                for(String card : ticket.getNumbers()){
                    String test = card;
                    int position;
                    Bitmap bm;
                    ByteArrayOutputStream baos;
                    String encoded;
                    byte[] b;

                    switch(iconSet){
                        case "cards":
                            position = Arrays.asList(choiceList).indexOf(card);

                            bm = BitmapFactory.decodeResource(getApplicationContext().getResources(), cards[position]);
                            baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 50, baos); //bm is the bitmap object
                            b = baos.toByteArray();

                            encoded = Base64.encodeToString(b, Base64.DEFAULT);

                            buffer.append(encoded.trim());

                            if (buffer.length() != 0) {
                                buffer.append(",");
                            }

                            bm.recycle();
                            break;
                        case "lucky":
                            position = Arrays.asList(choiceList).indexOf(card);

                            buffer.append(chosenIconSetString.get(position));

                            if (buffer.length() != 0) {
                                buffer.append(",");
                            }
                            break;
                    }
                }

                    List<String> temp = new ArrayList<String>();
                    temp.add(buffer.toString());

                    int ticketPosition = listDataHeader.indexOf(ticketName);

                    listDataChild.put(listDataHeader.get(ticketPosition), temp);
                }
        }

        // setting list adapter
        ExpandableListView expListView = (ExpandableListView)findViewById(R.id.numbers);
        expListView.setAdapter(listAdapter);
    }

    private void updateCost(String number){
        int mult = Integer.valueOf(number);
        int multBy = primaryCost;

        int finalNumber = mult * multBy;
        costNumber.setText(String.valueOf(finalNumber));
    }

    private void updateCart(int number){
        getEpsApplication().setCartNumber(number);
        DecimalFormat formatter = new DecimalFormat("00");
        String aFormatted = formatter.format(number);
    }

    private void quickPick() {
        while (ticketNumberLeft > 0) {
            numberLeft = maxNumber;
            if (gameType.equals("number")) {
                for (int j = 0; j < numberLeft; j++) {
                    if (tileValuesArray.size() < maxNumber) {
                        int index = r.nextInt(numberChoices.size());
                        String randomStr = numberChoices.get(index);
                        tileValuesArray.add(Integer.valueOf(randomStr));
                        //ballViews.get(i).setText(randomStr);
                        numberChoices.remove(randomStr);
                    }
                }
                numberChoices.clear();
                numberChoices.addAll(Arrays.asList(choiceList));

                Collections.sort(tileValuesArray);

                ArrayList<String> numbers = new ArrayList<String>();
                for (int number : tileValuesArray) {
                    numbers.add(String.valueOf(number));
                }

                Ticket ticket = new Ticket();
                if(user != null) {
                    ticket.setUserName(user.getUsername());
                }
                else{
                    ticket.setUserName("null");
                }
                ticket.setNumbers(numbers);
                ticket.setGameId(gameId);
                ticket.setWager(String.valueOf(primaryCost));
                ticket.setGameName(gameName);

                gamePicks.add(ticket);
                tileValuesArray.clear();
                ticketNumberLeft--;
            }
            else if (gameType.equals("letter")) {
                for (int j = 0; j < numberLeft; j++) {
                    if (letterValuesArray.size() < maxNumber) {
                        int index = r.nextInt(numberChoices.size());
                        String randomStr = numberChoices.get(index);
                        letterValuesArray.add(randomStr);
                        //ballViews.get(i).setText(randomStr);
                        numberChoices.remove(randomStr);
                    }
                }
                numberChoices.clear();
                numberChoices.addAll(Arrays.asList(choiceList));

                ArrayList<String> numbers = new ArrayList<String>();
                for (String letter : letterValuesArray) {
                    numbers.add(String.valueOf(letter));
                }

                Collections.sort(numbers);
                Ticket ticket = new Ticket();
                if(user != null) {
                    ticket.setUserName(user.getUsername());
                }
                else{
                    ticket.setUserName("null");
                }
                ticket.setNumbers(numbers);
                ticket.setGameId(gameId);
                ticket.setWager(String.valueOf(primaryCost));
                ticket.setGameName(gameName);

                gamePicks.add(ticket);
                letterValuesArray.clear();
                ticketNumberLeft--;
            }

            else if (gameType.equals("card")  || gameType.equals("symbol")) {
                for (int j = 0; j < numberLeft; j++) {
                    if (letterValuesArray.size() < maxNumber) {
                        int index = r.nextInt(numberChoices.size());
                        String randomStr = numberChoices.get(index);
                        letterValuesArray.add(randomStr);
                        //ballViews.get(i).setText(randomStr);
                        numberChoices.remove(randomStr);
                    }
                }
                numberChoices.clear();

                if(gameType.equals("card")) {
                    numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.card_select1)));
                }
                else{
                    numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.lucky_select1)));
                }

                ArrayList<String> numbers = new ArrayList<String>();
                for (String card : letterValuesArray) {
                    numbers.add(card);
                }

                Collections.sort(numbers);
                Ticket ticket = new Ticket();
                if(user != null) {
                    ticket.setUserName(user.getUsername());
                }
                else{
                    ticket.setUserName("null");
                }
                ticket.setNumbers(numbers);
                ticket.setGameId(gameId);
                ticket.setWager(String.valueOf(primaryCost));
                ticket.setGameName(gameName);

                gamePicks.add(ticket);
                letterValuesArray.clear();
                ticketNumberLeft--;
            }

            else if(gameType.equals("phone")){
                String phone = prefs.getString("username", "555-555-5555").substring(prefs.getString("username", "555-555-5555").length() - 4);
                ArrayList<String> numbers = new ArrayList<String>();

                String first = String.valueOf(phone.charAt(0));
                String two = String.valueOf(phone.charAt(1));
                String three = String.valueOf(phone.charAt(2));
                String four = String.valueOf(phone.charAt(3));

                numbers.add(first);
                numbers.add(two);
                numbers.add(three);
                numbers.add(four);

                Ticket ticket = new Ticket();
                if(user != null) {
                    ticket.setUserName(user.getUsername());
                }
                else{
                    ticket.setUserName("null");
                }
                ticket.setNumbers(numbers);
                ticket.setGameId(gameId);
                ticket.setWager(String.valueOf(primaryCost));
                ticket.setGameName(gameName);

                gamePicks.add(ticket);
//                letterValuesArray.clear();
                ticketNumberLeft--;
            }
        }

        if(ticketNumberLeft == 1 || ticketNumberLeft == 0) {
            showVerification();
        }

        else if(pick.getVisibility() == View.VISIBLE){
            Animation rotate = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.rotate);
            quickPick.startAnimation(rotate);

            if(gameType.equals("letter")) {
                letterValuesArray.clear();
            }
            else if(gameType.equals("number")){
                tileValuesArray.clear();
            }

            ticketNumberLeft--;
            numberLeft = maxNumber;

            if(pick.getVisibility() == View.VISIBLE) {
                if(gameType.equals("letter") || gameType.equals("number")) {
                    grid.setAdapter(new BallAdapter(this));
                    symbolGrid.setVisibility(View.GONE);
                }
                else {
                    symbolGrid.setAdapter(new SymbolAdapter(this));
                    grid.setVisibility(View.GONE);
                }

                if (ticketNumberLeft == 1) {
                    //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                }
            }
        }
        else{
            showVerification();
        }
    }

    private void clearNumbers(){
        /*if(!(gameName.equals("Last Four"))) {
            for (int i = 0; i < ballViews.size(); i++) {
                ballViews.get(i).setText("");
            }
        }*/
    }

    private void launchPick(){
        if(newCounter.getText().toString() != "0") {
            Animation out = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_out_top);
            LinearLayout buttons = (LinearLayout) findViewById(R.id.game_buttons);
            buttons.startAnimation(out);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    //mainGame.setVisibility(View.GONE);
                    LinearLayout buttons = (LinearLayout) findViewById(R.id.game_buttons);
                    buttons.setVisibility(View.GONE);
                    if (gameType.equals("phone")) {
                        tileValuesArray = new ArrayList<Integer>();
                        Animation slideIn = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
                        RelativeLayout phone = (RelativeLayout) findViewById(R.id.pick_4);
                        phone.startAnimation(slideIn);
                        phone.setVisibility(View.VISIBLE);
                        textView = new ArrayList<TextView>();

                        one.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("1");
                            }
                        });
                        two.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("2");
                            }
                        });
                        three.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("3");
                            }
                        });
                        four.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("4");
                            }
                        });
                        five.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("5");
                            }
                        });
                        six.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("6");
                            }
                        });
                        seven.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("7");
                            }
                        });
                        eight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("8");
                            }
                        });
                        nine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("9");
                            }
                        });
                        zero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setNumber("0");
                            }
                        });

                        textView.add(digitOne);
                        textView.add(digitTwo);
                        textView.add(digitThree);
                        textView.add(digitFour);
                    } else {
                        Animation slideIn = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.fade_in);
                        pick.startAnimation(slideIn);
                        pick.setVisibility(View.VISIBLE);
                        if (gameType.equals("number")) {
                            tileValuesArray = new ArrayList<Integer>();
                        } else if (gameType.equals("letter") || gameType.equals("card") || gameType.equals("symbol")) {
                            letterValuesArray = new ArrayList<String>();
                        }
                        tiles = new ArrayList<TextView>();

                        /*if (ticketNumberLeft > 1) {
                            donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_white_24dp));
                        } else {
                            donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                        }*/

                        numberLeft = maxNumber;
                        counter.setText(new DecimalFormat("00").format(numberLeft));

                    /*Animation out = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_out_top);
                    moreInfoTag.startAnimation(out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            moreInfoTag.setVisibility(View.GONE);
                            Animation in = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
                            countdown.startAnimation(in);
                            countdown.setVisibility(View.VISIBLE);
                            countdown.setText(String.valueOf(maxNumber));
                            numberLeft = maxNumber;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });*/
                    }
                }
            });
        }
    }

    private void launchInfo(){
        if(mainGame.getVisibility() == View.VISIBLE){
                    //TextView name = (TextView) findViewById(R.id.game_name);
                    //gameNameMove = ObjectAnimator.ofFloat(name, "translationX", 200);
                    //gameNameMove.start();

                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GameViewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    TextView info = (TextView) findViewById(R.id.more_info_field);
                                    TextView main = (TextView)findViewById(R.id.info);
                                    if(info.getVisibility() == View.INVISIBLE) {
                                        Animation out = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_out_left);
                                        Animation slide = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_left);
                                        main.setAnimation(out);
                                        main.setVisibility(View.INVISIBLE);
                                        moreScroll.startAnimation(slide);
                                        moreScroll.setVisibility(View.VISIBLE);
                                        //info.startAnimation(slide);
                                        info.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        Animation out = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_out_left);
                                        Animation slide = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_left);
                                        main.setAnimation(slide);
                                        main.setVisibility(View.VISIBLE);
                                        //info.startAnimation(out);
                                        moreScroll.startAnimation(out);
                                        moreScroll.setVisibility(View.INVISIBLE);
                                        info.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    }, 200);
        }
    }

    private void showPreview(){
        RelativeLayout main = (RelativeLayout) findViewById(R.id.main_game_head);
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        main.startAnimation(fade);
        main.setVisibility(View.INVISIBLE);
    }

    private void dismissPick(){
        RelativeLayout phone = (RelativeLayout) findViewById(R.id.pick_4);

        if(pick.getVisibility() == View.VISIBLE){
            Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            slideOut.setDuration(300);
            pick.startAnimation(slideOut);
            pick.setVisibility(View.GONE);
            resetSpinner();
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    //mainGame.startAnimation(slideIn);
                    //Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    //mainGame.startAnimation(slideIn);
                    //mainGame.setVisibility(View.VISIBLE);
                }
            });
        }
        /*else if(phone.getVisibility() == View.VISIBLE){
            Animation fade = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.fade_out);
            phone.startAnimation(fade);
            phone.setVisibility(View.GONE);
        }*/
        else if(reviewLayout.getVisibility() == View.VISIBLE){
            //pick.setVisibility(View.GONE);
            resetSpinner();
            Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            reviewLayout.startAnimation(slideOut);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    slideIn.setStartOffset(500);
                    //mainGame.startAnimation(slideIn);
                    reviewLayout.setVisibility(View.GONE);
                    //Animation in = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.fade_in);
                    if(mainGame.getVisibility() == View.GONE) {
                        mainGame.startAnimation(slideIn);
                        mainGame.setVisibility(View.VISIBLE);
                    }
                }
            });

            Animation out = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_out_top);
            countdown.startAnimation(out);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    countdown.setVisibility(View.GONE);
                    Animation in = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
                    countdown.setText(String.valueOf(maxNumber));
                    numberLeft = maxNumber;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            gamePicks = new ArrayList<Ticket>();
        }
    }

    private void dismissInfo(){
        if(moreInfo.getVisibility() == View.VISIBLE){
            TextView name = (TextView) findViewById(R.id.game_name);
            gameNameMove = ObjectAnimator.ofFloat(name, "translationX", 0);
            gameNameMove.start();
            gameNameMove.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GameViewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    RelativeLayout info = (RelativeLayout) findViewById(R.id.more_info);
                                    RelativeLayout main = (RelativeLayout) findViewById(R.id.main_game_head);
                                    LinearLayout ticketInfo = (LinearLayout) findViewById(R.id.ticket_info);
                                    LinearLayout priceInfo = (LinearLayout) findViewById(R.id.price_info);
                                    Animation in = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.abc_fade_out);
                                    Animation slide = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.abc_slide_out_top);
                                    main.setVisibility(View.VISIBLE);
                                    info.startAnimation(in);
                                    info.setVisibility(View.GONE);
                                }
                            });
                        }
                    }, 600);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    public class BallAdapter extends BaseAdapter {
        private Context mContext;

        // Gets the context so it can be used later
        public BallAdapter(Context c) {
            mContext = c;
        }

        // Total number of things contained within the adapter
        public int getCount() {
            return choiceList.length;
        }

        // Require for structure, not really used in my code.
        public Object getItem(int position) {
            return null;
        }

        // Require for structure, not really used in my code. Can
        // be used to get the id of an item in the adapter for
        // manual control.
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {
            TextView btn;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                btn = new TextView(mContext);
                if(getResources().getString(R.string.tablet_mode).equals("true")){
                    btn.setLayoutParams(new GridView.LayoutParams(50, 50));
                }
                else {
                    btn.setLayoutParams(new GridView.LayoutParams(150, 150));
                }

            }
            else {
                btn = (TextView) convertView;
            }

            if(gameType.equals("number")|| gameType.equals("letter")) {
                btn.setText(choiceList[position]);
                // filenames is an array of strings
                btn.setTextColor(Color.BLACK);
                //btn.setBackgroundResource(R.drawable.square);
                btn.setId(position);
                btn.setClickable(true);
                btn.setTypeface(null, Typeface.BOLD);
                btn.setGravity(Gravity.CENTER);
                btn.setOnClickListener(new TileListener(position));

                tiles.add(btn);

                //if(Integer.valueOf(btn.getText().toString()) > maxNumberChoice){
                //    btn.setVisibility(View.GONE);
                //    tiles.remove(btn);
               // }

                /*if(position == 0){
                    btn.setVisibility(View.GONE);
                    tiles.remove(btn);
                }*/
            }

            if(gameType.equals("number") && tileValuesArray.contains(Integer.valueOf(btn.getText().toString())) ||  gameType.equals("letter") && letterValuesArray.contains(btn.getText().toString())){
                btn.setBackgroundResource(R.drawable.tile_select);
                btn.setTextColor(Color.WHITE);
            }

            return btn;
        }
    }

    public class SymbolAdapter extends BaseAdapter {
        private Context mContext;

        // Gets the context so it can be used later
        public SymbolAdapter(Context c) {
            mContext = c;
        }

        // Total number of things contained within the adapter
        public int getCount() {
            if(iconSet.equals("cards")) {
                return cards.length;
            }
            else{
                return iconSetString.size();
            }
        }

        @Override

        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        // Require for structure, not really used in my code.
        public Object getItem(int position) {
            return null;
        }

        // Require for structure, not really used in my code. Can
        // be used to get the id of an item in the adapter for
        // manual control.
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position,
                            View convertView, ViewGroup parent) {
            ImageView image;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                image = new ImageView(mContext);
                image.setLayoutParams(new GridView.LayoutParams(100, 140));
                image.setPadding(2,2,2,2);
                image.setOnClickListener(new CardListener(position));

            }
            else {
                image = (ImageView) convertView;
            }
            //else if(iconSet.equals("lucky")){
            //    byte[] decodedString = Base64.decode(iconSetString.get(position), Base64.DEFAULT);
            //    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //    image.setImageBitmap(decodedByte);
            //}

            if(gameType.equals("card")){
                Drawable d;
                if(cardPos.contains(cards[position])){
                    //d = getResources().getDrawable(cardsBoxed[position]);
                    //image.setBackground(d);
                    //image.setBackgroundResource(cardsBoxed[position]);
                }
                else{
                    d = getResources().getDrawable(cards[position]);
                    image.setBackground(d);
                    //image.setBackgroundResource(cards[position]);
                }
            }

            return image;
        }
    }

    class TileListener implements View.OnClickListener
    {
        private final int position;

        public TileListener(int position)
        {
            this.position = position;
        }

        public void onClick(View v)
        {
            TextView tile = (TextView) v;

            if(gameType.equals("number")) {
                // Search for existing ball value
                int removeIndex = -1;
                int ballValue = position + 1;
                for (int i = 0; i < tileValuesArray.size(); i++) {
                    if (tileValuesArray.get(i) == ballValue) {
                        removeIndex = i; // value was found
                        break;
                    }
                }

                if (removeIndex > -1) {
                    // Remove the value from the array
                    tileValuesArray.remove(removeIndex);
                    //tile.setBackgroundResource(R.drawable.square);
                    tile.setTextColor(getResources().getColor(R.color.black));
                    numberLeft++;
                    counter.setText(new DecimalFormat("00").format(numberLeft));

                    if (tileValuesArray.size() < maxNumber && donePick.getVisibility() == View.VISIBLE) {
                        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                        //set.setFillAfter(true);
                        donePick.startAnimation(slide);
                        counter.setText(new DecimalFormat("00").format(numberLeft));
                        slide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                donePick.setVisibility(View.GONE);
                                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                                quickPick.startAnimation(drop);
                                quickPick.setVisibility(View.VISIBLE);

                                Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                AnimationSet set = new AnimationSet(false);
                                set.addAnimation(spin);
                                set.addAnimation(fade);
                                set.setFillAfter(true);
                                counter.startAnimation(set);
                                counter.setVisibility(View.VISIBLE);
                                //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                } else {
                    if (tileValuesArray.size() == maxNumber) {
                        //do nothing
                    } else {
                        // It's a new value, so add it to the array
                        tileValuesArray.add(ballValue);
                        tile.setBackgroundResource(R.drawable.tile_select);
                        tile.setTextColor(getResources().getColor(R.color.white));
                        numberLeft--;
                        counter.setText(new DecimalFormat("00").format(numberLeft));
                        if(counter.getText().toString().equals("00")){
                            Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            AnimationSet set = new AnimationSet(true);
                            set.addAnimation(spin);
                            set.addAnimation(fade);
                            set.setFillAfter(true);
                            counter.startAnimation(set);
                            counter.setVisibility(View.GONE);
                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                                    quickPick.startAnimation(drop);
                                    quickPick.setVisibility(View.GONE);

                                    Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                    Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                    AnimationSet set = new AnimationSet(false);
                                    set.addAnimation(spin);
                                    set.addAnimation(fade);
                                    //set.setFillAfter(true);
                                    donePick.startAnimation(set);
                                    donePick.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                    }
                }
            }

            else{
                int removeIndex = -1;
                String ballValue = tile.getText().toString();

                for (int i = 0; i < letterValuesArray.size(); i++) {
                    if (letterValuesArray.get(i).equals(ballValue)) {
                        removeIndex = i; // value was found
                        break;
                    }
                }

                if (removeIndex > -1) {
                    // Remove the value from the array
                    letterValuesArray.remove(removeIndex);
                    //tile.setBackgroundResource(R.drawable.square);
                    tile.setTextColor(getResources().getColor(R.color.black));
                    numberLeft++;
                    counter.setText(new DecimalFormat("00").format(numberLeft));

                    if (letterValuesArray.size() < maxNumber && donePick.getVisibility() == View.VISIBLE) {
                        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                        //set.setFillAfter(true);
                        donePick.startAnimation(slide);
                        counter.setText(new DecimalFormat("00").format(numberLeft));
                        slide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                donePick.setVisibility(View.GONE);
                                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                                quickPick.startAnimation(drop);
                                quickPick.setVisibility(View.VISIBLE);

                                Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                AnimationSet set = new AnimationSet(false);
                                set.addAnimation(spin);
                                set.addAnimation(fade);
                                set.setFillAfter(true);
                                counter.startAnimation(set);
                                counter.setVisibility(View.VISIBLE);
                                //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                } else {
                    if (letterValuesArray.size() == maxNumber) {
                        //do nothing
                    } else {
                        // It's a new value, so add it to the array
                        letterValuesArray.add(ballValue);
                        tile.setBackgroundResource(R.drawable.tile_select);
                        tile.setTextColor(getResources().getColor(R.color.white));
                        numberLeft--;
                        counter.setText(new DecimalFormat("00").format(numberLeft));
                        if(counter.getText().toString().equals("00")){
                            Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                            AnimationSet set = new AnimationSet(true);
                            set.addAnimation(spin);
                            set.addAnimation(fade);
                            set.setFillAfter(true);
                            counter.startAnimation(set);
                            counter.setVisibility(View.GONE);
                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                                    quickPick.startAnimation(drop);
                                    quickPick.setVisibility(View.GONE);

                                    Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                    Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                    AnimationSet set = new AnimationSet(false);
                                    set.addAnimation(spin);
                                    set.addAnimation(fade);
                                    //set.setFillAfter(true);
                                    donePick.startAnimation(set);
                                    donePick.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                    }
                }
            }

            if(gameType.equals("number")) {
                // Always Sort array to make sure it is lowest to highest
                Collections.sort(tileValuesArray);
            }
            else if(gameType.equals("letter")){
                Collections.sort(letterValuesArray);
            }

            // Now repopulate the balls with current values
            // Any balls after the length of the value array get cleared.

            // When we have collected 5 values then switch to the red balls
        }
    }

    class CardListener implements View.OnClickListener {
        private final int position;

        public CardListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            ImageView tile = (ImageView) v;

            // Search for existing ball value
            int removeIndex = -1;
            int ballValue = position;

            cardPos.add(position);
            if(letterValuesArray == null){
                letterValuesArray = new ArrayList<String>();
            }
            for (int i = 0; i < letterValuesArray.size(); i++) {
                switch(iconSet){
                    case "cards" :
                        if (letterValuesArray.get(i).equals(choiceList[position])) {
                        removeIndex = i; // value was found
                        break;
                    }
                        break;
                    case "lucky" :
                        if (letterValuesArray.get(i).equals(choiceList[position])) {
                            removeIndex = i; // value was found
                            break;
                        }
                        break;
                }
            }

            if (removeIndex > -1) {
                // Remove the value from the array
                letterValuesArray.remove(removeIndex);

                switch(iconSet){
                    case "cards" : tile.setImageResource(cards[position]);
                        break;
                    //case "lucky" :
                    //    byte[] decodedString = Base64.decode(iconSetString.get(position), Base64.DEFAULT);
                    //    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //    tile.setImageBitmap(decodedByte);
                    //    break;
                }
                numberLeft++;
                counter.setText(new DecimalFormat("00").format(numberLeft));

                if (letterValuesArray.size() < maxNumber && donePick.getVisibility() == View.VISIBLE) {
                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                    //set.setFillAfter(true);
                    donePick.startAnimation(slide);
                    counter.setText(new DecimalFormat("00").format(numberLeft));
                    slide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            donePick.setVisibility(View.GONE);
                            Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                            quickPick.startAnimation(drop);
                            quickPick.setVisibility(View.VISIBLE);

                            Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            AnimationSet set = new AnimationSet(false);
                            set.addAnimation(spin);
                            set.addAnimation(fade);
                            set.setFillAfter(true);
                            counter.startAnimation(set);
                            counter.setVisibility(View.VISIBLE);
                            //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

            }
            else {
                if (letterValuesArray.size() == maxNumber) {
                    counter.setText("Limit");
                }
                else {
                    // It's a new value, so add it to the array
                    switch(iconSet){
                        case "cards" :
                            letterValuesArray.add(choiceList[position]);
                            //tile.setImageResource(cardsBoxed[position]);
                            break;
                        //case "lucky" :
                        //    letterValuesArray.add(choiceList[position]);

                        //    byte[] decodedString = Base64.decode(chosenIconSetString.get(position), Base64.DEFAULT);
                        //    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //    tile.setImageBitmap(decodedByte);
                        //    break;
                    }
                    numberLeft--;
                    counter.setText(new DecimalFormat("00").format(numberLeft));

                    if(counter.getText().toString().equals("00")){
                        Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(spin);
                        set.addAnimation(fade);
                        set.setFillAfter(true);
                        counter.startAnimation(set);
                        counter.setVisibility(View.GONE);
                        set.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation drop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                                quickPick.startAnimation(drop);
                                quickPick.setVisibility(View.GONE);

                                Animation spin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                AnimationSet set = new AnimationSet(false);
                                set.addAnimation(spin);
                                set.addAnimation(fade);
                                //set.setFillAfter(true);
                                donePick.startAnimation(set);
                                donePick.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            }
            Collections.sort(letterValuesArray);
        }

        // Now repopulate the balls with current values
        // Any balls after the length of the value array get cleared.

        // When we have collected 5 values then switch to the red balls
        //}
    }

    private void figureFavorite(){
        if(masterFaves.contains(gameName)){
            favorite = true;
        }
        else{
            favorite = false;
        }

        if(!favorite){
            faveStarOpen.setVisibility(View.VISIBLE);
            faveStarClosed.setVisibility(View.GONE);
        }
        else {
            faveStarOpen.setVisibility(View.GONE);
            faveStarClosed.setVisibility(View.VISIBLE);
        }

        faveStarOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                faveStarOpen.setVisibility(View.GONE);
                TextView tag = (TextView)findViewById(R.id.fave_tage);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tag.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.fave_star_closed);
                tag.setLayoutParams(params);
                faveStarClosed.setVisibility(View.VISIBLE);
                favorite = true;
                setToFavorites(gameName);
            }
        });
        faveStarClosed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                faveStarOpen.setVisibility(View.VISIBLE);
                TextView tag = (TextView)findViewById(R.id.fave_tage);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tag.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.fave_star_open);
                tag.setLayoutParams(params);
                faveStarClosed.setVisibility(View.GONE);
                favorite = false;

                for (Game game : favoriteGames) {
                    if (game.getGameName().equals(gameName)) {
                        favoriteGames.remove(game);
                    }
                }

                removeFromFavorites(gameName);
            }
        });
    }

    private void addtoMaster(){
        if(getEpsApplication().masterArray == null){
            getEpsApplication().createMaster();
        }
        if (masterArray.isEmpty()) {
            masterArray.add(gamePicks);
        } else {
            ListIterator<ArrayList> iter = masterArray.listIterator();
            System.out.println("Forward iteration :");
            if (!(gamePicks.isEmpty())) {
                while (iter.hasNext()) {
                    contains = false;
                    ArrayList<Ticket> n = iter.next();
                    if (n.get(0).getGameName().equals(gamePicks.get(0).getGameName())) {
                        ArrayList<Ticket> newArray = new ArrayList<Ticket>();
                        newArray.addAll(gamePicks);
                        for(Ticket ticket : n){
                            newArray.add(ticket);
                        }
                        iter.remove();
                        iter.add(newArray);

                        contains = true;

                        getEpsApplication().isChanged = true;
                        clearSelections();

                        return;

                    } else {
                        contains = false;
                    }
                }
                if (!contains) {
                    masterArray.add(gamePicks);
                    contains = true;
                }
            }
            else{
                return;
            }
        }
        getEpsApplication().isChanged = true;
        clearSelections();
    }

    private void resetSpinner(){
        /*ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, numbers); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_list_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);*/

        newCounter.setText("00");
        costTag.setText("00");

        LinearLayout buttons = (LinearLayout)findViewById(R.id.game_buttons);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(slide);
        set.addAnimation(fade);
        set.setStartOffset(500);
        buttons.startAnimation(set);
        buttons.setVisibility(View.VISIBLE);

        setIconSet();

        scaleDown.end();
        scaleDown2.end();

        //Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        //arrow.startAnimation(fade);
        //arrow.setVisibility(View.VISIBLE);

        if(!gamePicks.isEmpty()) {
            cartPulse = ObjectAnimator.ofPropertyValuesHolder(viewCart,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f));
            cartPulse.setDuration(310);

            cartPulse.setRepeatCount(10);
            cartPulse.setRepeatMode(ObjectAnimator.REVERSE);

            cartPulse.setStartDelay(1000);
            cartPulse.start();

            //arrow.setVisibility(View.VISIBLE);
        }

            disableButtons();
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

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
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_sub, null);
            }

            if(gameType.equals("number") || gameType.equals("letter") || gameType.equals("phone") && !subShown) {
                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItem);
                LinearLayout main = (LinearLayout)convertView
                        .findViewById(R.id.confirm_cards);
                main.setVisibility(View.GONE);

                txtListChild.setText("");
                txtListChild.setTypeface(sub);

                txtListChild.setText(childText);
                //subShown = true;
            }
            else if(gameType.equals("symbol") || gameType.equals("card")){
                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItem);
                LinearLayout main = (LinearLayout)convertView
                        .findViewById(R.id.confirm_cards);
                main.removeAllViews();
                txtListChild.setVisibility(View.GONE);

                StringTokenizer tokenizer = new StringTokenizer(childText, ", ");
                while(tokenizer.hasMoreTokens()) {
                    int tokens = tokenizer.countTokens();
                    String token = tokenizer.nextToken().trim();
                    byte[] decodedString = Base64.decode(token, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImageView temp = new ImageView(getApplicationContext());
                    temp.setLayoutParams(new GridView.LayoutParams(100, 100));
                    temp.setImageBitmap(decodedByte);

                    main.addView(temp);
                }
            }
            return convertView;
        }

        @Override
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
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            /*if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_cell, null);
            }*/

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
    }

    private void clearSelections(){
        //gamePicks.clear();
        if(gameType.equals("number") || gameType.equals("letter")) {
            //gamePicks = new ArrayList<Ticket>();
            listDataHeader.clear();
            listDataChild.clear();
            grid.setAdapter(new BallAdapter(this));
        }
        else if(gameType.equals("phone")){
            listDataHeader.clear();
            listDataChild.clear();
            digitOne.setText("");
            digitTwo.setText("");
            digitThree.setText("");
            digitFour.setText("");
        }
        else{
            //gamePicks = new ArrayList<Ticket>();
            listDataHeader.clear();
            listDataChild.clear();
            symbolGrid.setAdapter(new SymbolAdapter(this));
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(GameViewActivity.this, listDataHeader, listDataChild);
        ExpandableListView expListView = (ExpandableListView)findViewById(R.id.numbers);
        expListView.setAdapter(listAdapter);

        subShown = false;
    }

    private void animateButtons(){
        /*ObjectAnimator quickMove = ObjectAnimator.ofFloat(quickPlay, "translationX", -200);
        ObjectAnimator selectMove = ObjectAnimator.ofFloat(select, "translationX", -400);
        ObjectAnimator faveMove = ObjectAnimator.ofFloat(faveStarOpen, "translationX", -600);
        ObjectAnimator faveCLosedMove = ObjectAnimator.ofFloat(faveStarClosed, "translationX", -600);
        AnimatorSet set = new AnimatorSet();
        quickMove.setDuration(400);
        quickMove.setStartDelay(100);
        selectMove.setDuration(500);
        selectMove.setStartDelay(100);
        faveMove.setDuration(600);
        faveMove.setStartDelay(100);
        faveCLosedMove.setDuration(600);
        faveCLosedMove.setStartDelay(100);
        set.play(quickMove).with(selectMove).with(faveMove).with(faveCLosedMove).after(200);
        set.start();*/
        Animation visible = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
        Animation visible2 = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
        Animation visible3 = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
        Animation visible4 = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
        Animation visible5 = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.slide_in_bottom);
        visible.setStartOffset(300);
        viewCart.setVisibility(View.VISIBLE);
        viewCart.startAnimation(visible);
        visible2.setStartOffset(500);
        quickPlay.setVisibility(View.VISIBLE);
        quickPlay.startAnimation(visible2);
        visible3.setStartOffset(500);
        select.setVisibility(View.VISIBLE);
        select.startAnimation(visible3);
        visible3.setStartOffset(800);
        faveStarClosed.startAnimation(visible4);
        visible4.setStartOffset(1100);
        faveStarOpen.startAnimation(visible5);
        visible5.setStartOffset(1100);
    }

    private Integer[] cards = {
            R.drawable.clubs_2, R.drawable.clubs_3,
            R.drawable.clubs_4, R.drawable.clubs_5,
            R.drawable.clubs_6, R.drawable.clubs_7,
            R.drawable.clubs_8, R.drawable.clubs_9,
            R.drawable.clubs_10, R.drawable.clubs_jack,
            R.drawable.clubs_queen, R.drawable.clubs_king,
            R.drawable.diamonds_2, R.drawable.diamonds_3,
            R.drawable.diamonds_4, R.drawable.diamonds_5,
            R.drawable.diamonds_6, R.drawable.diamonds_7,
            R.drawable.diamonds_8, R.drawable.diamonds_9,
            R.drawable.diamonds_10, R.drawable.diamonds_jack,
            R.drawable.diamonds_queen, R.drawable.diamonds_king,
            R.drawable.hearts_2, R.drawable.hearts_3,
            R.drawable.hearts_4, R.drawable.hearts_5,
            R.drawable.hearts_6, R.drawable.hearts_7,
            R.drawable.hearts_8, R.drawable.hearts_9,
            R.drawable.hearts_10, R.drawable.hearts_jack,
            R.drawable.hearts_queen, R.drawable.hearts_king,
            R.drawable.spades_2, R.drawable.spades_3,
            R.drawable.spades_4, R.drawable.spades_5,
            R.drawable.spades_6, R.drawable.spades_7,
            R.drawable.spades_8, R.drawable.spades_9,
            R.drawable.spades_10, R.drawable.spades_jack,
            R.drawable.spades_queen, R.drawable.spades_king,
            R.drawable.spades_ace, R.drawable.clubs_ace,
            R.drawable.diamonds_ace, R.drawable.hearts_ace
    };

    /*private Integer[] cardsBoxed = {
            R.drawable.two_club_box, R.drawable.three_club_box,
            R.drawable.four_club_box, R.drawable.five_club_box,
            R.drawable.six_club_box, R.drawable.seven_club_box,
            R.drawable.eight_club_box, R.drawable.nine_club_box,
            R.drawable.ten_club_box, R.drawable.jclub_box,
            R.drawable.qclub_box, R.drawable.kclub_box,
            R.drawable.two_diamond_box, R.drawable.three_diamond_box,
            R.drawable.four_diamond_box, R.drawable.five_diamond_box,
            R.drawable.six_diamond_box, R.drawable.seven_diamond_box,
            R.drawable.eight_diamond_box, R.drawable.nine_diamond_box,
            R.drawable.ten_diamond_box, R.drawable.jdiamond_box,
            R.drawable.qdiamond_box, R.drawable.kdiamond_box,
            R.drawable.two_heart_box, R.drawable.three_heart_box,
            R.drawable.four_heart_box, R.drawable.five_heart_box,
            R.drawable.six_heart_box, R.drawable.seven_heart_box,
            R.drawable.eight_heart_box, R.drawable.nine_heart_box,
            R.drawable.ten_heart_box, R.drawable.jheart_box,
            R.drawable.qheart_box, R.drawable.kheart_box,
            R.drawable.two_spade_box, R.drawable.three_spade_box,
            R.drawable.four_spade_box, R.drawable.five_spade_box,
            R.drawable.six_spade_box, R.drawable.seven_spade_box,
            R.drawable.eight_spade_box, R.drawable.nine_spade_box,
            R.drawable.ten_spade_box, R.drawable.jspade_box,
            R.drawable.qspade_box, R.drawable.kspade_box,
            R.drawable.aspade_box, R.drawable.aclub_box,
            R.drawable.adiamond_box, R.drawable.aheart_box
    };*/

    private void setIconSet(){
        switch(iconSet){
            case "numbers":
                choiceList = new String[maxNumberChoice];
                for(int i=0; i < maxNumberChoice; i++){
                    choiceList[i] = String.valueOf(i+1);
                }
                numberChoices.addAll(Arrays.asList(choiceList));
                break;
            case "letters": numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.letter_select1)));
                choiceList = getResources().getStringArray(R.array.letter_select1);
                break;
            case "cards": numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.card_select1)));
                choiceList = getResources().getStringArray(R.array.card_select1);
                break;
            case "lucky" : numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.lucky_select1)));
                String[] test = new String[12];
                test[0] = "acorn";
                test[1] = "clover";
                test[2] = "coins";
                /*test[3] = "dice";
                test[4] = "shoes";
                test[5] = "ladybug";
                test[6] = "hat";
                test[7] = "star";
                test[8] = "gold";
                test[9] = "foot";
                test[10] = "slots";
                test[11] = "wishbone";*/

                choiceList = test;
                break;
        }
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

    private void setNumber(String number){
        for(int i = 0; i < textView.size(); i++){
            if(i == 3){
                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                donePick4.startAnimation(in);
                donePick4.setVisibility(View.VISIBLE);
            }
            if(textView.get(i).getText().equals("")){
                textView.get(i).setText(number);
                tileValuesArray.add(Integer.valueOf(number));
                return;
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private int hidingItemIndex = 0;

        public CustomAdapter(Context context, int textViewResourceId, String[] objects, int hidingItemIndex) {
            super(context, textViewResourceId, objects);
            this.hidingItemIndex = hidingItemIndex;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = null;
            if (position == hidingItemIndex) {
                TextView tv = new TextView(getContext());
                tv.setVisibility(View.GONE);
                v = tv;
            } else {
                v = super.getDropDownView(position, null, parent);
            }
            return v;
        }
    }

    private void hideSample(){
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
        RelativeLayout tix = (RelativeLayout)findViewById(R.id.sample_tix);
        tix.startAnimation(out);
        tix.setVisibility(View.GONE);

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout buttons = (LinearLayout)findViewById(R.id.game_buttons);
                LinearLayout spinner = (LinearLayout)findViewById(R.id.new_spinner);
                RelativeLayout info = (RelativeLayout)findViewById(R.id.info_layout);
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                buttons.startAnimation(fade);
                spinner.startAnimation(fade);
                info.startAnimation(fade);
                info.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void incrementNewCounter(String which){
        if(which.equals("plus")){
            int current = Integer.valueOf(newCounter.getText().toString());
            current++;
            newCounter.setText(String.valueOf(current));
            multiplyCost();

            activateUI();
        }
        else{
            int current = Integer.valueOf(newCounter.getText().toString());
            if(current > 0) {
                current--;
                newCounter.setText(String.valueOf(current));
                multiplyCost();

                if(current == 0){
                    disableButtons();

                    if((scaleDown != null && scaleDown.isRunning()) && (scaleDown2 != null && scaleDown2.isRunning())) {
                        scaleDown.end();
                        scaleDown2.end();
                    }
                }
            }
            else{
                disableButtons();

                if(scaleDown != null && scaleDown.isRunning()) {
                    scaleDown.end();
                }
            }
        }
    }

    private void multiplyCost(){
        int a = Integer.valueOf(wager);
        int b = Integer.valueOf(newCounter.getText().toString());
        int finalInt = a * b;

        costTag.setText(String.valueOf(finalInt));
    }

    private void activateUI(){
        if(cartPulse != null && cartPulse.isRunning()){
            cartPulse.end();

            quickPick.setEnabled(true);
            quickPlay.setEnabled(true);
            select.setEnabled(true);
        }

        if(!newCounter.getText().toString().equals("00")){
            quickPick.setEnabled(true);
            quickPlay.setEnabled(true);
            select.setEnabled(true);

            quickPick.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    ticketNumberLeft = Integer.valueOf(newCounter.getText().toString());
                    quickPick();
                }
            });

            quickPlay.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    numberLeft = maxNumber;

                    if (gameType.equals("number") || gameType.equals("phone")) {
                        tileValuesArray = new ArrayList<Integer>();
                    } else if (gameType.equals("letter") || gameType.equals("card") || gameType.equals("symbol")) {
                        letterValuesArray = new ArrayList<String>();
                    }
                    ticketNumberLeft = Integer.valueOf(newCounter.getText().toString());
                    quickPick();
                }
            });

            select.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    ticketNumberLeft = Integer.valueOf(newCounter.getText().toString());
                    launchPick();
                }
            });

            if(scaleDown != null && !(scaleDown.isRunning())) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(scaleDown, scaleDown2);
                set.start();
            }

            /*Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            arrow.startAnimation(slide);
            arrow.setVisibility(View.GONE);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(scaleDown, scaleDown2);
                    set.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });*/
        }
    }

    private void disableButtons(){
        quickPick.setEnabled(true);
        quickPlay.setEnabled(true);
        select.setEnabled(true);

        quickPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newCounter.getText().toString().equals("0") || newCounter.getText().toString().equals("00")){
                    Crouton.makeText(GameViewActivity.this, "Must Select How Many Tickets First", Style.ALERT).show();
                }
            }
        });
        quickPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newCounter.getText().toString().equals("0") || newCounter.getText().toString().equals("00")){
                    Crouton.makeText(GameViewActivity.this, "Must Select How Many Tickets First", Style.ALERT).show();
                }
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newCounter.getText().toString().equals("0") || newCounter.getText().toString().equals("00")){
                    Crouton.makeText(GameViewActivity.this, "Must Select How Many Tickets First", Style.ALERT).show();
                }
            }
        });
    }

    private void dynamicSetIcon(){
        /*Ion.with(getApplicationContext())
                .load(getIntent().getStringExtra("logoURL"))
                .withBitmap()
                .intoImageView(logo);*/
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("logoURL"))
                .into(logo);
    }

    private void dynamicSetBack(){
        ImageView back = (ImageView)findViewById(R.id.testDynamic);
        back.setVisibility(View.VISIBLE);
        /*Ion.with(getApplicationContext())
                .load(getIntent().getStringExtra("backURL"))
                .setLogging("Test", Log.DEBUG)
                .withBitmap()
                .error(getResources().getDrawable(R.drawable.cp_pyramid))
                .intoImageView(back);

        Log.d("test2", "test");*/
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("backURL"))
                .into(back);
    }
}
