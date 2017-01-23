package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestGameFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private LinearLayout buy;
    private String gameName;
    private TextView win;
    private TextView cost;
    private TextView per;
    private TextView how;
    private TextView instructions;
    private String extended;
    private String gameId;
    private String maxNumber;
    private String maxChoice;
    private String description;
    private String iconSet;
    private Bundle args;
    private TextView game;
    private TextView descrip;
    private ImageView tile;
    private boolean favorite;
    private ArrayList<String> masterFaves;
    private LinearLayout favorites;
    private LinearLayout demo;
    private String tileString;
    private URL tileUrl;
    private int templateId;
    private String backUrl;
    private LinearLayout app;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_game_frag, container, false);

        game = (TextView)rootView.findViewById(R.id.game_name);
        descrip = (TextView)rootView.findViewById(R.id.description);
        tile = (ImageView)rootView.findViewById(R.id.game_tile);
        favorites = (LinearLayout)rootView.findViewById(R.id.favorite);
        masterFaves = getEpsApplication().masterFavorites;
        demo = (LinearLayout)rootView.findViewById(R.id.demo);
        win = (TextView)rootView.findViewById(R.id.win);
        cost = (TextView)rootView.findViewById(R.id.cost);
        per = (TextView)rootView.findViewById(R.id.per);
        how = (TextView)rootView.findViewById(R.id.how);
        instructions = (TextView)rootView.findViewById(R.id.instructions);
        app = (LinearLayout)rootView.findViewById(R.id.app);

        /*boolean hasMenuKey = ViewConfiguration.get(getActivity()).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if(!hasMenuKey && !hasBackKey) {

            LinearLayout.LayoutParams relativeParams = (LinearLayout.LayoutParams)app.getLayoutParams();
            relativeParams.setMargins(0, 0, 0, navigationHeight());  // left, top, right, bottom
            app.setLayoutParams(relativeParams);
        }*/

        //((LandingActivity)getActivity()).disableScroll();

        args = getArguments();
        gameName = args.getString("gameName");
        description = args.getString("description");
        extended = args.getString("extendedDescription");
        templateId = args.getInt("templateId");
        backUrl = args.getString("backUrl");



        int number = Integer.valueOf(args.getString("topPrize"));
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedNum = formatter.format(number);

        descrip.setText(description);
        instructions.setText(extended);

        String winText = "Win up to $" + formattedNum + "!";
        win.setText(winText);

        ((LandingActivity)getActivity()).enableScroll();

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).launchScratch("demo", "demo", "true", templateId, backUrl, args);
            }
        });

        if(!gameName.equals("Find 9")) {
            demo.setEnabled(false);
            demo.setAlpha(Float.valueOf(".5"));
        }

        if(masterFaves.contains(gameName)){
            favorite = true;
        }
        else{
            favorite = false;
        }

        try {
            tileUrl = new URL(getArguments().getString("tileUrl"));
            Glide.with(getActivity())
                    .load(tileUrl)
                    .into(tile);
        }

        catch(MalformedURLException ex){
            //do nothing
        }

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!favorite){
                    favorite = true;
                    setToFavorites(gameName);
                    Crouton.makeText(getActivity(), "Added", Style.INFO).show();
                }
                else{
                    favorite = false;

                    for (Game game : favoriteGames) {
                        if (game.getGameName().equals(gameName)) {
                            favoriteGames.remove(game);
                        }
                    }

                    removeFromFavorites(gameName);
                    Crouton.makeText(getActivity(), "Removed", Style.INFO).show();
                }
            }
        });

        /*switch(gameName){
            case "Find 9":
                tile.setImageResource(R.drawable.tile_find_9);
                break;
            case "Alphabet Soup":
                tile.setImageResource(R.drawable.tile_alphabet_soup);
                break;
            case "Last Four":
                tile.setImageResource(R.drawable.tile_last4_en);
                break;
            default:
                tile.setImageResource(R.drawable.tile_find_9);
                break;
        }*/

        game.setText(gameName);
        descrip.setText(description);
        String str = "$" + args.getString("cost");
        cost.setText(str);
        setFonts();
        //gameId = args.getString("gameId");
        //maxNumber = args.getString("maxNumber");


        buy = (LinearLayout)rootView.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).launchSelect(args);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 2);
        title = getArguments().getString("Game");
    }


    public static TestGameFragment newInstance() {
        TestGameFragment fragmentFirst = new TestGameFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 2);
        args.putString("someTitle", "Game");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    private void setFonts(){
        game.setTypeface(getEpsApplication().sub);
        per.setTypeface(getEpsApplication().main);
        how.setTypeface(getEpsApplication().sub);
        win.setTypeface(getEpsApplication().main);
        instructions.setTypeface(getEpsApplication().main);
        cost.setTypeface(getEpsApplication().main);
        descrip.setTypeface(getEpsApplication().main);
    }

    private int navigationHeight(){
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}

