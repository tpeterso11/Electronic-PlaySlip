package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.Game;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestLandingFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private ViewFlipper vf;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private Handler handler = new Handler();
    private int position;
    private LinearLayout outer;
    private int scrollMax;
    private Timer scrollTimer;
    private TimerTask scrollerSchedule;
    private int scrollPos =	0;
    private ImageView first;
    private RelativeLayout second;
    private RelativeLayout third;
    private ArrayList<Game> favorites;
    private ArrayList<Game> newest;
    private ArrayList<Game> prize;
    private ArrayList<Game> odds;
    private ArrayList<Game> popular;
    private ArrayList<Game> featured;
    private ArrayList<Game> retailerGames;
    private LinearLayout favesList;
    private HorizontalGridView horizontalGridView;
    private HorizontalGridView newGrid;
    private HorizontalGridView popGrid;
    private HorizontalGridView prizeGrid;
    private HorizontalGridView oddGrid;
    private LinearLayout main;
    private TextView addedText;
    private TextView prizeText;
    private TextView oddsText;
    private TextView retailText;
    private TextView favoriteText;
    private LinearLayout favoritesLayout;
    private LinearLayout retailerLayout;
    private String gamename;
    private String wager;
    private String topPrize;
    private String bannerUrl;
    private String gameId;
    private String iconSet;
    private String maxValue;
    private String maxNumber;
    private String backUrl;
    private String maxPrize;
    private int templateId;
    private String gameDescription;
    private String extendedDesc;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_landing_frag, container, false);

        vf = (ViewFlipper) rootView.findViewById(R.id.viewFlipper);
        horizontalGridView = (HorizontalGridView)rootView.findViewById(R.id.fave_grid);
        newGrid = (HorizontalGridView)rootView.findViewById(R.id.newest_grid);
        prizeGrid = (HorizontalGridView)rootView.findViewById(R.id.prize_grid);
        oddGrid = (HorizontalGridView)rootView.findViewById(R.id.odds_grid);
        //popGrid = (HorizontalGridView)rootView.findViewById(R.id.pop_grid);
        addedText = (TextView)rootView.findViewById(R.id.added_text);
        prizeText = (TextView)rootView.findViewById(R.id.prize_text);
        oddsText = (TextView)rootView.findViewById(R.id.odds_text);
        retailText = (TextView)rootView.findViewById(R.id.retailer_text);
        favoriteText = (TextView)rootView.findViewById(R.id.favorite_text);
        favoritesLayout = (LinearLayout)rootView.findViewById(R.id.favorites_layout);
        retailerLayout = (LinearLayout)rootView.findViewById(R.id.retailer_layout);

        setFonts();

        newest = new ArrayList<Game>();
        odds = new ArrayList<Game>();
        prize = new ArrayList<Game>();
        popular = new ArrayList<Game>();
        featured = new ArrayList<Game>();
        retailerGames = new ArrayList<Game>();
        main = (LinearLayout)rootView.findViewById(R.id.main_back);

        favorites = new ArrayList<Game>();
        /*first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).launchGame(createBundle("Find 9", "50", "number", "1", "numbers", "Find 9", "9", "2", ""));
            }
        });*/

        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);

        vf.setInAnimation(in);
        vf.setOutAnimation(out);
        //vf.setAutoStart(true);
        //vf.setFlipInterval(5000);
        //vf.startFlipping();

        if(getEpsApplication().masterFavorites == null){
            getEpsApplication().createFavorites();
        }


        for(String integer : getEpsApplication().featured){
            for(Game game : getEpsApplication().games){
                if(game.getGameId().equals(integer)){
                    featured.add(game);
                }
            }
        }

        for(final Game game : featured){
            RelativeLayout currentLayout = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            currentLayout.setLayoutParams(rlp);

            ImageView currentImage = new ImageView(getActivity());
            Glide.with(getActivity())
                    .load(game.getBannerUrl())
                    .fitCenter()
                    .crossFade(R.anim.fade_in, 800)
                    .into(currentImage);
            currentLayout.addView(currentImage);

            currentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((LandingActivity) getActivity()).launchGame(createBundle(game.getGameName(), game.getTemplateId(), game.getMaxValue(), game.getGameId(), game.getIconSet(), game.getGameDescription(), game.getExtendedDescription(), game.getMaxNumber(), game.getWager(), game.getBannerUrl(), game.getTopPrize(), game.getBackURL()));
                    return false;
                }
            });

            currentLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View view, final MotionEvent event) {
                    gamename = game.getGameName();
                    maxValue = game.getMaxValue();
                    maxNumber = game.getMaxNumber();
                    gameId = game.getGameId();
                    iconSet = game.getIconSet();
                    gameDescription = game.getGameDescription();
                    extendedDesc = game.getExtendedDescription();
                    wager = game.getWager();
                    bannerUrl = game.getBannerUrl();
                    topPrize = game.getTopPrize();
                    templateId = game.getTemplateId();
                    backUrl = game.getBackURL();

                    detector.onTouchEvent(event);
                    return true;
                }
            });
            /*currentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LandingActivity) getActivity()).launchGame(createBundle(game.getGameName(), game.getMaxValue(), game.getGameId(), game.getIconSet(), game.getGameDescription(), game.getExtendedDescription(), game.getMaxNumber(), game.getWager(), game.getBannerUrl(), game.getTopPrize()));
                }
            });*/

            vf.addView(currentLayout);
        }

        ArrayList<String> masterFaves = getEpsApplication().masterFavorites;
        if(masterFaves.size() > 0) {
            for (String name : masterFaves) {
                for (int i = 0; i < getEpsApplication().games.size(); i++) {
                    if (name.equals(getEpsApplication().games.get(i).getGameName())) {
                        favorites.add(getEpsApplication().games.get(i));
                        break;
                    }
                }
            }
        }

        if(favorites.isEmpty()){
            favoritesLayout.setVisibility(View.GONE);
        }

        if(retailerGames.isEmpty()){
            retailerLayout.setVisibility(View.GONE);
        }

        for(String integer : getEpsApplication().newest){
            for(Game game : getEpsApplication().games){
                if(game.getGameId().equals(integer)){
                    newest.add(game);
                }
            }
        }

        for(String integer : getEpsApplication().topPrize){
            for(Game game : getEpsApplication().games){
                if(game.getGameId().equals(integer)){
                    prize.add(game);
                }
            }
        }

        for(String integer : getEpsApplication().topOdds){
            for(Game game : getEpsApplication().games){
                if(game.getGameId().equals(integer)){
                    odds.add(game);
                }
            }
        }

        for(String integer : getEpsApplication().popular){
            for(Game game : getEpsApplication().games){
                if(game.getGameId().equals(integer)){
                    popular.add(game);
                }
            }
        }

        GridElementAdapter adapter = new GridElementAdapter(getActivity(), favorites);
        horizontalGridView.setHorizontalMargin(20);
        horizontalGridView.setAdapter(adapter);
        horizontalGridView.invalidate();

        GridElementAdapter newAdapter = new GridElementAdapter(getActivity(), newest);
        newGrid.setHorizontalMargin(20);
        newGrid.setAdapter(newAdapter);
        newGrid.invalidate();
        //setFavorites();
        //GridElementAdapter popAdapter = new GridElementAdapter(getActivity(), popular);
        //popGrid.setHorizontalMargin(20);
        GridElementAdapter prizeAdapter = new GridElementAdapter(getActivity(), prize);
        prizeGrid.setHorizontalMargin(20);
        prizeGrid.setAdapter(prizeAdapter);
        GridElementAdapter oddsAdapter = new GridElementAdapter(getActivity(), odds);
        oddGrid.setHorizontalMargin(20);
        oddGrid.setAdapter(oddsAdapter);

        /*ViewTreeObserver vto = outer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                outer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getScrollMaxAmount();
                startAutoScrolling();
            }
        });*/

        /*vf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });*/

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("Landing");

        //handler.postDelayed(runnableCode, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        //main.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
    }

    public static  TestLandingFragment newInstance() {
        TestLandingFragment fragmentFirst = new TestLandingFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 0);
        args.putString("someTitle", "Landing");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public void getScrollMaxAmount(){
        int actualWidth = (outer.getMeasuredWidth()-512);
        scrollMax = actualWidth;
    }

    /*public void startAutoScrolling(){
        if (scrollTimer == null) {
            scrollTimer	= new Timer();
            final Runnable Timer_Tick = new Runnable() {
                public void run() {
                    moveScrollView();
                }
            };

            if(scrollerSchedule != null){
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }

            scrollerSchedule = new TimerTask(){
                @Override
                public void run(){
                    getActivity().runOnUiThread(Timer_Tick);
                }
            };
            scrollTimer.schedule(scrollerSchedule, 5, 5);
        }
    }

    /*public void moveScrollView(){
        scrollPos = (int) (scrollView.getScrollX() + 1.0);
        if(scrollPos >= scrollMax){
            scrollView.scrollTo(0, 0);
            //scrollPos =	0;
        }
        scrollView.scrollTo(scrollPos, 0);

    }*/

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    vf.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                    vf.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                    vf.showNext();
                    vf.stopFlipping();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    vf.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right));
                    vf.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right));
                    vf.showPrevious();
                    vf.stopFlipping();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            ((LandingActivity)getActivity()).launchGame(createBundle(gamename, templateId, maxValue, gameId, iconSet, gameDescription, extendedDesc, maxNumber, wager, bannerUrl, topPrize, backUrl));
            //handler.removeCallbacks(runnableCode);
            return true;
        }
    }

        private Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread

                //vf.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                //vf.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left));
                //vf.showNext();
                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(runnableCode, 5000);
            }
        };

        private Bundle createBundle(String gamename, int templateId, String maxValue, String gameId, String iconSet, String description, String extendedDescription, String maxChoice, String cost, String tileUrl, String topPrize, String backUrl) {
            Bundle args = new Bundle();
            args.putString("gameName", gamename);
            args.putInt("templateId", templateId);
            args.putString("gameId", gameId);
            args.putString("topPrize", topPrize);
            args.putString("maxValue", maxValue);
            args.putString("description", description);
            args.putString("extendedDescription", extendedDescription);
            args.putString("iconSet", iconSet);
            args.putString("maxChoice", maxChoice);
            args.putString("cost", cost);
            args.putString("tileUrl", tileUrl);
            args.putString("backUrl", backUrl);
            args.putInt("someInt", 2);
            args.putString("someTitle", "Game");

            return args;
        }

    public class GridElementAdapter extends RecyclerView.Adapter<GridElementAdapter.SimpleViewHolder>{

        private Context context;
        private List<String> elements;
        private ArrayList<Game> list;
        private String cat;

        public GridElementAdapter(Context context, ArrayList<Game> list){
            this.context = context;
            this.list = list;
            this.elements = new ArrayList<String>();
            // Fill dummy list
            for(int i = 0; i < 40 ; i++){
                this.elements.add(i, "Position : " + i);
            }
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public ImageView image;
            public LinearLayout main;

            public SimpleViewHolder(View view) {
                super(view);
                image = (ImageView)view.findViewById(R.id.image);
                main = (LinearLayout)view.findViewById(R.id.main_cell);
            }
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(this.context).inflate(R.layout.landing_slider_cell, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, final int position) {
            holder.main.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
            Glide.with(getActivity())
                    .load(list.get(position).getIconUrl())
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LandingActivity) getActivity()).launchGame(createBundle(list.get(position).getGameName(), list.get(position).getTemplateId(), list.get(position).getMaxValue(), list.get(position).getGameId(), list.get(position).getIconSet(), list.get(position).getGameDescription(), list.get(position).getExtendedDescription(), list.get(position).getMaxNumber(), list.get(position).getWager(), list.get(position).getBannerUrl(),  list.get(position).getTopPrize(), list.get(position).getBackURL()));
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }


    public void setFavorites(){
        ArrayList<String> masterFaves = getEpsApplication().masterFavorites;
        for(String name : masterFaves){
            for(int i=0; i < getEpsApplication().games.size(); i++){
                if(name.equals(getEpsApplication().games.get(i).getGameName())){
                    favorites.add(getEpsApplication().games.get(i));
                    break;
                }
            }
        }



        for(Game game : favorites){
            ImageView current = new ImageView(getActivity());

            Glide.with(getActivity())
                    .load(game.getIconUrl())
                    .crossFade(R.anim.fade_in, 800)
                    .into(current);

            current.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            favesList.addView(current);
        }
    }

    private void setFonts(){
        favoriteText.setTypeface(getEpsApplication().sub);
        oddsText.setTypeface(getEpsApplication().sub);
        prizeText.setTypeface(getEpsApplication().sub);
        addedText.setTypeface(getEpsApplication().sub);
        retailText.setTypeface(getEpsApplication().sub);
    }
}

