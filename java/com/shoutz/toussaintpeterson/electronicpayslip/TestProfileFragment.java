package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import models.Game;
import models.Location;
import models.Ticket;
import models.User;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestProfileFragment extends AbstractEPSFragment {
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
    private ArrayList<Location> locationList;
    private LinearLayout profileMain;
    private LinearLayout profileEdit;
    private RelativeLayout accessSettings;
    private EditText editEmail;
    private EditText editPass;
    private EditText editPhone;
    private TextView english;
    private TextView spanish;
    private RelativeLayout saveButton;
    private boolean isSpanish;
    private boolean englishHighlight;
    private boolean spanishHighlight;
    private ImageView trash;
    //private CheckBox noNotification;
    //private CheckBox yesNotification;
    private ArrayList<Game> favorites;
    private ArrayList<Game> savedNumbers;
    private HorizontalGridView faveGrid;
    private HorizontalGridView savedGrid;
    private RelativeLayout retailerSelect;
    private LinearLayout app;
    private RelativeLayout dividerOne;
    private RelativeLayout dividerTwo;
    private RelativeLayout dividerThree;
    private RelativeLayout dividerFour;
    private RelativeLayout dividerFive;
    private RelativeLayout dividerSix;
    private RelativeLayout header;
    private JSONObject response;
    private TextView menuEmail;
    private TextView menuPhone;
    private TextView profileTitle;
    private TextView emailTag;
    private TextView phoneTag;
    private TextView yesTag;
    private TextView noTag;
    private TextView faveGames;
    private TextView faveLocales;
    private TextView settingsTag;
    private TextView languageTag;
    private TextView push;
    private TextView saveText;
    private Game currentSave;
    private ArrayList<Ticket> gamePicks;
    private boolean notificationsOn;
    private AlertDialog dialog;
    private View invisible;
    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private boolean isOnClick;
    private HorizontalGridView faveGridEdit;
    private HorizontalGridView savedGridEdit;
    private boolean isDeleteFave;
    private boolean isDeleteSave;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_profile_frag, container, false);

        favoritesList = getEpsApplication().masterFavorites;
        noFave = (TextView)rootView.findViewById(R.id.no_faves);
        //locations = (CustomExpandableListView) rootView.findViewById(R.id.locations);
        //locationList = new ArrayList<Location>();
        profileEdit = (LinearLayout)rootView.findViewById(R.id.profile_edit);
        faveGridEdit = (HorizontalGridView)rootView.findViewById(R.id.fave_grid_edit);
        savedGridEdit = (HorizontalGridView)rootView.findViewById(R.id.save_grid_edit);
        profileMain = (LinearLayout)rootView.findViewById(R.id.profile_main);
        accessSettings = (RelativeLayout) rootView.findViewById(R.id.settings);
        editEmail = (EditText)rootView.findViewById(R.id.edit_email);
        editPass = (EditText)rootView.findViewById(R.id.edit_pass);
        editPhone = (EditText)rootView.findViewById(R.id.edit_phone);
        header = (RelativeLayout)rootView.findViewById(R.id.profile_header);
        settingsTag = (TextView) rootView.findViewById(R.id.edit_tag);
        languageTag = (TextView)rootView.findViewById(R.id.language_tag);
        push = (TextView)rootView.findViewById(R.id.push);
        english = (TextView)rootView.findViewById(R.id.english);
        spanish = (TextView)rootView.findViewById(R.id.spanish);
        menuEmail = (TextView)rootView.findViewById(R.id.menu_email);
        menuPhone = (TextView)rootView.findViewById(R.id.menu_phone);
        isSpanish = getEpsApplication().isSpanish;
        trash = (ImageView)rootView.findViewById(R.id.trash);
        dividerOne = (RelativeLayout)rootView.findViewById(R.id.divider1);
        dividerTwo = (RelativeLayout)rootView.findViewById(R.id.divider2);
        dividerThree = (RelativeLayout)rootView.findViewById(R.id.divider3);
        dividerFour = (RelativeLayout)rootView.findViewById(R.id.divider4);
        dividerFive = (RelativeLayout)rootView.findViewById(R.id.divider5);
        //dividerSix = (RelativeLayout)rootView.findViewById(R.id.divider6);
        yesTag = (TextView)rootView.findViewById(R.id.yes_tag);
        noTag = (TextView)rootView.findViewById(R.id.no_tag);
        //yesNotification = (CheckBox)rootView.findViewById(R.id.yes_note);
        //noNotification = (CheckBox)rootView.findViewById(R.id.no_note);
        app = (LinearLayout)rootView.findViewById(R.id.app);
        saveButton = (RelativeLayout)rootView.findViewById(R.id.save_button);
        favorites = new ArrayList<Game>();
        faveGrid = (HorizontalGridView)rootView.findViewById(R.id.fave_grid);
        retailerSelect = (RelativeLayout)rootView.findViewById(R.id.retailer_select);
        profileTitle = (TextView)rootView.findViewById(R.id.profile_title);
        emailTag = (TextView)rootView.findViewById(R.id.email_tag);
        phoneTag = (TextView)rootView.findViewById(R.id.phone_tag);
        faveGames = (TextView)rootView.findViewById(R.id.fave_games_tag);
        faveLocales = (TextView)rootView.findViewById(R.id.fave_locales);
        languageTag = (TextView)rootView.findViewById(R.id.language_edit);
        saveText = (TextView)rootView.findViewById(R.id.save_text);
        savedGrid = (HorizontalGridView)rootView.findViewById(R.id.save_grid);
        gamePicks = new ArrayList<Ticket>();

        profileEdit.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        header.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        profileMain.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        app.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        //dividerOne.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerTwo.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerThree.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerFour.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerFive.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        saveText.setTypeface(getEpsApplication().sub);
        savedNumbers = new ArrayList<Game>();
        //noNotification.setTypeface(getEpsApplication().main);
        //yesNotification.setTypeface(getEpsApplication().main);
        //saveButton.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //retailerSelect.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));

        //int count = 0;

        if(getEpsApplication().games != null){
            Map<String, ?> keys = prefs.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String current = entry.getKey();
                for (Game game : getEpsApplication().games) {
                    if (current.equals(game.getGameName())) {
                        game.setSavedNumbers(entry.getValue().toString());
                        savedNumbers.add(game);
                    }
                }
            }
        }
        else{
            Intent i = new Intent(getActivity(), PreSplashActivity.class);
            startActivity(i);
        }

        GridElementAdapter savedAdapter = new GridElementAdapter(getActivity(), "saved", savedNumbers);
        savedGrid.setHorizontalMargin(20);
        savedGrid.setRowHeight(600);
        savedGrid.setAdapter(savedAdapter);
        savedGrid.invalidate();

        GridElementAdapter savedEditAdapter = new GridElementAdapter(getActivity(), "savedEdit", savedNumbers);
        savedGridEdit.setHorizontalMargin(20);
        savedGridEdit.setRowHeight(600);
        savedGridEdit.setAdapter(savedEditAdapter);
        savedGridEdit.invalidate();


        if(getEpsApplication().getUser().getPhoneNumber() != null) {
            menuPhone.setText(getEpsApplication().getUser().getPhoneNumber());
        }
        else{
            menuPhone.setVisibility(View.GONE);
        }

        if(getEpsApplication().getUser().getEmail() != null && !(getEpsApplication().getUser().getEmail().equals("null"))) {
            menuEmail.setText(getEpsApplication().getUser().getEmail());
        }
        else{
            menuEmail.setVisibility(View.GONE);
        }

        retailerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).showRetailers();
            }
        });

        if(getEpsApplication().masterFavorites != null) {
            ArrayList<String> masterFaves = getEpsApplication().masterFavorites;
            for (String name : masterFaves) {
                for (int i = 0; i < getEpsApplication().games.size(); i++) {
                    if (name.equals(getEpsApplication().games.get(i).getGameName())) {
                        favorites.add(getEpsApplication().games.get(i));
                        break;
                    }
                }
            }
        }
        else{
            Intent i = new Intent(getActivity(), PreSplashActivity.class);
            startActivity(i);
        }

        GridElementAdapter adapter = new GridElementAdapter(getActivity(), "favorites", favorites);
        faveGrid.setHorizontalMargin(20);
        faveGrid.setRowHeight(400);
        faveGrid.setAdapter(adapter);
        faveGrid.invalidate();

        GridElementAdapter editAdapter = new GridElementAdapter(getActivity(), "favoritesEdit", favorites);
        faveGridEdit.setHorizontalMargin(20);
        faveGridEdit.setRowHeight(400);
        faveGridEdit.setAdapter(editAdapter);
        faveGridEdit.invalidate();

        accessSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profileEdit.getVisibility() == View.GONE){
                    Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);

                    profileMain.startAnimation(out);
                    profileMain.setVisibility(View.GONE);

                    profileEdit.startAnimation(in);
                    profileEdit.setVisibility(View.VISIBLE);

                    if(getEpsApplication().getUser().getUsername().contains("@")){
                        editEmail.setHint(getEpsApplication().getUser().getUsername());
                        if(getEpsApplication().getUser().getPhoneNumber()!=null){
                            editPhone.setHint(getEpsApplication().getUser().getPhoneNumber());
                        }
                    }
                    else{
                        editPhone.setHint(getEpsApplication().getUser().getUsername());
                        if(getEpsApplication().getUser().getEmail()!=null){
                            editEmail.setHint(getEpsApplication().getUser().getEmail());
                        }
                    }

                    editPass.setHint(getEpsApplication().getUser().getPassword());
                    editPass.setTransformationMethod(new AsteriskPasswordTransformationMethod());

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new EditProfileTask(getResources().getString(R.string.edit_user), editEmail.getText().toString(), editPhone.getText().toString(), getActivity()).execute();
                        }
                    });
                }

                if(isSpanish){
                    english.setBackgroundColor(getResources().getColor(R.color.new_black));
                    spanish.setBackgroundColor(getResources().getColor(R.color.tomato));
                    spanishHighlight = true;
                    englishHighlight = false;
                }
                else{
                    english.setBackgroundColor(getResources().getColor(R.color.tomato));
                    spanish.setBackgroundColor(getResources().getColor(R.color.black));
                    englishHighlight = true;
                    spanishHighlight = false;
                }

                english.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!englishHighlight) {
                            english.setBackgroundColor(getResources().getColor(R.color.tomato));
                            spanish.setBackgroundColor(getResources().getColor(R.color.new_black));
                            englishHighlight = true;
                            spanishHighlight = false;

                            getEpsApplication().isSpanish = false;
                            isSpanish = false;
                            //changeLanguage();
                        }
                    }
                });

                spanish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!spanishHighlight) {
                            english.setBackgroundColor(getResources().getColor(R.color.new_black));
                            spanish.setBackgroundColor(getResources().getColor(R.color.tomato));
                            englishHighlight = false;
                            spanishHighlight = true;

                            getEpsApplication().isSpanish = true;
                            isSpanish = true;
                            //changeLanguage();
                        }
                    }
                });

                if(prefs.contains("notifications") && prefs.getString("notifications", "null").equals("true")){
                    noTag.setBackgroundColor(getResources().getColor(R.color.new_black));
                    yesTag.setBackgroundColor(getResources().getColor(R.color.tomato));
                    notificationsOn = true;
                }
                else if(prefs.contains("notifications") && prefs.getString("notifications", "null").equals("false")){
                    yesTag.setBackgroundColor(getResources().getColor(R.color.new_black));
                    noTag.setBackgroundColor(getResources().getColor(R.color.tomato));
                    notificationsOn = false;
                }
                else{
                    yesTag.setBackgroundColor(getResources().getColor(R.color.new_black));
                    noTag.setBackgroundColor(getResources().getColor(R.color.tomato));
                    notificationsOn = false;
                }

                yesTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(!notificationsOn){
                           SharedPreferences.Editor edit = prefs.edit();
                           edit.putString("notifications", "true").apply();

                           noTag.setBackgroundColor(getResources().getColor(R.color.new_black));
                           yesTag.setBackgroundColor(getResources().getColor(R.color.tomato));
                           notificationsOn = true;
                       }
                    }
                });

                noTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(notificationsOn){
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putString("notifications", "false").apply();

                            yesTag.setBackgroundColor(getResources().getColor(R.color.new_black));
                            noTag.setBackgroundColor(getResources().getColor(R.color.tomato));
                            notificationsOn = false;
                        }
                    }
                });
            }
        });

        if(favoritesList.size() == 0){
            noFave.setVisibility(View.VISIBLE);
        }
        else{
            noFave.setVisibility(View.GONE);
        }

        setFonts();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 5);
        title = getArguments().getString("Profile");
    }


    public static TestProfileFragment newInstance() {
        TestProfileFragment fragmentFirst = new TestProfileFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 5);
        args.putString("someTitle", "Profile");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public class MobileArrayAdapter extends ArrayAdapter<Location> {
        private final Context context;
        private final ArrayList<Location> values;

        public MobileArrayAdapter(Context context, ArrayList<Location> values) {
            super(getActivity(), R.layout.location_cell, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.location_cell, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.cell_title);
            textView.setText(values.get(position).getName());

            return rowView;
        }
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

    private void changeLanguage(){
        if(isSpanish) {
            String languageToLoad = "es"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config,
                    getActivity().getResources().getDisplayMetrics());

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
            getActivity().getResources().updateConfiguration(config,
                    getActivity().getResources().getDisplayMetrics());

            if(getEpsApplication().games != null) {
                getEpsApplication().games.clear();
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("spanish", "false").apply();
        }
    }

    public void resetProfile(){
        if(profileEdit.getVisibility() == View.VISIBLE){
            Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);

            profileEdit.startAnimation(out);
            profileEdit.setVisibility(View.GONE);

            profileMain.startAnimation(in);
            profileMain.setVisibility(View.VISIBLE);
        }
        else{
            ((LandingActivity)getActivity()).launchLanding();
        }
    }

    public class GridElementAdapter extends RecyclerView.Adapter<GridElementAdapter.SimpleViewHolder>{

        private Context context;
        private List<String> elements;
        private ArrayList<Game> list;
        private String type;
        private String cat;

        public GridElementAdapter(Context context, String type, ArrayList<Game> list){
            this.context = context;
            this.list = list;
            this.elements = new ArrayList<String>();
            this.type = type;
            // Fill dummy list
            for(int i = 0; i < 40 ; i++){
                this.elements.add(i, "Position : " + i);
            }
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public ImageView image;
            public RelativeLayout body;
            public ImageView close;

            public SimpleViewHolder(View view) {
                super(view);
                image = (ImageView)view.findViewById(R.id.image);
                body = (RelativeLayout)view.findViewById(R.id.body);
                close = (ImageView)view.findViewById(R.id.close);
            }
        }

        @Override
        public GridElementAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(this.context).inflate(R.layout.profile_slider_cell_edit, parent, false);
            return new GridElementAdapter.SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GridElementAdapter.SimpleViewHolder holder, final int position) {
            Glide.with(getActivity())
                    .load(list.get(position).getIconUrl())
                    .centerCrop()
                    .into(holder.image);

            if(type.equals("favoritesEdit") || type.equals("savedEdit")) {
                holder.close.setVisibility(View.VISIBLE);
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type.equals("favoritesEdit")){
                            for(Game fave : favorites) {
                                if (list.get(position).getGameName().equals(fave.getGameName())) {
                                    favorites.remove(fave);

                                    GridElementAdapter adapter = new GridElementAdapter(getActivity(), "favorites", favorites);
                                    faveGridEdit.setHorizontalMargin(20);
                                    faveGridEdit.setRowHeight(400);
                                    faveGridEdit.setAdapter(adapter);
                                    faveGridEdit.invalidate();

                                    faveGrid.setHorizontalMargin(20);
                                    faveGrid.setRowHeight(400);
                                    faveGrid.setAdapter(adapter);
                                    faveGrid.invalidate();

                                    removeFromFavorites(list.get(position).getGameName());
                                }
                            }
                        }
                        else {
                            currentSave = list.get(position);
                            for (Game game : savedNumbers) {
                                if (currentSave.getGameName().equals(game.getGameName())) {
                                    savedNumbers.remove(game);

                                    GridElementAdapter savedAdapter = new GridElementAdapter(getActivity(), "saved", savedNumbers);
                                    savedGridEdit.setHorizontalMargin(20);
                                    savedGridEdit.setRowHeight(600);
                                    savedGridEdit.setAdapter(savedAdapter);
                                    savedGridEdit.invalidate();

                                    savedGrid.setHorizontalMargin(20);
                                    savedGrid.setRowHeight(600);
                                    savedGrid.setAdapter(savedAdapter);
                                    savedGrid.invalidate();

                                    SharedPreferences.Editor edit = prefs.edit();
                                    edit.remove(currentSave.getGameName()).apply();
                                }
                            }
                        }
                    }
                });
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type.equals("favorites")) {
                        ((LandingActivity) getActivity()).launchGame(createBundle(list.get(position).getGameName(), list.get(position).getTemplateId(), list.get(position).getMaxValue(), list.get(position).getGameId(), list.get(position).getIconSet(), list.get(position).getGameDescription(), list.get(position).getExtendedDescription(), list.get(position).getMaxNumber(), list.get(position).getWager(), list.get(position).getBannerUrl(), list.get(position).getTopPrize(), list.get(position).getBackURL()));
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialoglayout = inflater.inflate(R.layout.saved_number_pop, null);
                        TextView numbers = (TextView)dialoglayout.findViewById(R.id.numbers);
                        TextView counter = (TextView)dialoglayout.findViewById(R.id.new_counter);
                        ImageView plus = (ImageView)dialoglayout.findViewById(R.id.plus);
                        ImageView minus = (ImageView)dialoglayout.findViewById(R.id.minus);
                        counter.setText("1");

                        plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView counter = (TextView)dialoglayout.findViewById(R.id.new_counter);
                                int count = Integer.valueOf(counter.getText().toString());
                                count++;
                                counter.setText(String.valueOf(count));
                            }
                        });

                        minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView counter = (TextView)dialoglayout.findViewById(R.id.new_counter);
                                int count = Integer.valueOf(counter.getText().toString());
                                if(count > 0) {
                                    count--;
                                    counter.setText(String.valueOf(count));
                                }
                            }
                        });

                        numbers.setText(list.get(position).getSavedNumbers());

                        builder.setView(dialoglayout);
                        builder.setTitle("How Many Tickets?");
                        builder.setMessage("Each will use these saved numbers.");
                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((LandingActivity)getActivity()).startLoading();
                                TextView counter = (TextView)dialoglayout.findViewById(R.id.new_counter);
                                int count = Integer.valueOf(counter.getText().toString());
                                ArrayList<String> numbers = new ArrayList<String>();

                                StringTokenizer tokenizer = new StringTokenizer(list.get(position).getSavedNumbers(), "- ");
                                while(tokenizer.hasMoreTokens()){
                                    numbers.add(tokenizer.nextToken());
                                }


                                while(count > 0){
                                    Ticket ticket = new Ticket();
                                    ticket.setGameId(list.get(position).getGameId());
                                    ticket.setWager(list.get(position).getWager());
                                    ticket.setNumbers(numbers);
                                    gamePicks.add(ticket);

                                    count--;
                                }

                                new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, "", getActivity()).execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        dialog = builder.create();
                        dialog.show();
                        //dialog.getWindow().setBackgroundDrawableResource(R.color.new_blue);
                    }
                }
            });

            // TODO: 1/19/17 replace with "X" button
            /*if(type.equals("favoritesEdit") || type.equals("savedEdit")) {
                holder.image.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                mDownX = motionEvent.getX();
                                mDownY = motionEvent.getY();
                                isOnClick = true;
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                if (isOnClick) {
                                    Log.i("Tag", "onClick ");
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (isOnClick && (Math.abs(mDownX - motionEvent.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - motionEvent.getY()) > SCROLL_THRESHOLD)) {
                                    Log.i("Tag", "movement detected");
                                    isOnClick = false;

                                    currentSave = list.get(position);
                                    ClipData data = ClipData.newPlainText("", "");
                                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                                            holder.body);
                                    holder.body.startDrag(data, shadowBuilder, view, 0);
                                    holder.body.setVisibility(View.INVISIBLE);

                                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                                    Drawable normalShape = getResources().getDrawable(R.drawable.trash_icon_red);
                                    trash.setBackgroundDrawable(normalShape);
                                    trash.startAnimation(in);
                                    trash.setVisibility(View.VISIBLE);

                                    trash.setOnDragListener(new MyDragListener());

                                    if (type.equals("favoritesEdit")) {
                                        isDeleteFave = true;
                                    } else {
                                        isDeleteSave = true;
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }*/
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

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

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

    public void changeBG(){
        header.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        app.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        profileMain.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        profileEdit.setBackgroundColor(Color.parseColor(getEpsApplication().primary));
        //dividerOne.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerTwo.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerThree.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerFour.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //dividerFive.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //saveButton.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
        //retailerSelect.setBackgroundColor(Color.parseColor(getEpsApplication().secondary));
    }

    public class EditProfileTask extends AbstractWebService {
        private String email;
        private String phone;
        private String pass;

        public EditProfileTask(String urlPath, String email, String phone, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.email = email;
            this.phone = phone;
            this.pass = pass;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

            SharedPreferences.Editor edit = prefs.edit();
            User user = getEpsApplication().getUser();
            user.setEmail(editEmail.getText().toString());
            user.setPhoneNumber(editPhone.getText().toString());

            edit.putString("email", user.getEmail()).apply();
            edit.putString("phone", user.getPhoneNumber()).apply();

            editEmail.setHint(user.getEmail());
            editPhone.setHint(user.getPhoneNumber());
        }

        @Override
        protected void onError(Object response) {
            //Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            if(!email.equals("")) {
                params.put("email1", email);
            }
            if(!phone.equals("")){
                params.put("phone1", phone);
            }
            if(params.length() > 0) {
                params.put("authtoken", getEpsApplication().getUser().getAuthToken());
                response = doPost(params);
            }

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void setFonts(){
        emailTag.setTypeface(getEpsApplication().sub);
        phoneTag.setTypeface(getEpsApplication().sub);
        profileTitle.setTypeface(getEpsApplication().sub);
        menuEmail.setTypeface(getEpsApplication().main);
        menuPhone.setTypeface(getEpsApplication().main);

        faveGames.setTypeface(getEpsApplication().sub);
        faveLocales.setTypeface(getEpsApplication().sub);
        english.setTypeface(getEpsApplication().sub);
        spanish.setTypeface(getEpsApplication().sub);
        push.setTypeface(getEpsApplication().sub);
        settingsTag.setTypeface(getEpsApplication().sub);
        languageTag.setTypeface(getEpsApplication().sub);
    }

    public class PostTicketTask extends AbstractWebService {
        private ArrayList<Ticket> gamePicks;
        private String batch;

        public PostTicketTask(String urlPath, ArrayList<Ticket> gamePicks, String batch, Context context) {
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.gamePicks = gamePicks;
            this.batch = batch;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            dialog.dismiss();
            ((LandingActivity)getActivity()).launchTempCart();
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            for (Ticket ticket : gamePicks) {
                JSONObject params = new JSONObject();
                params.put("gameid", ticket.getGameId());
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

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.trash_icon_open_red);
        Drawable normalShape = getResources().getDrawable(R.drawable.trash_icon_red);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    invisible = view;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    int no = owner.getChildCount();
                    int pos = owner.indexOfChild(view);

                    if(isDeleteSave) {
                        for (Game game : savedNumbers) {
                            if (currentSave.getGameName().equals(game.getGameName())) {
                                savedNumbers.remove(game);

                                GridElementAdapter savedAdapter = new GridElementAdapter(getActivity(), "saved", savedNumbers);
                                savedGrid.setHorizontalMargin(20);
                                savedGrid.setRowHeight(600);
                                savedGrid.setAdapter(savedAdapter);
                                savedGrid.invalidate();

                                SharedPreferences.Editor edit = prefs.edit();
                                edit.remove(currentSave.getGameName()).apply();

                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                                                trash.startAnimation(out);
                                                out.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                        trash.setVisibility(View.GONE);
                                                        trash.setBackgroundDrawable(null);
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                            }
                                        });
                                    }
                                }, 500);
                            }
                        }
                    }
                            else{
                                for(Game fave : favorites) {
                                    if (currentSave.getGameName().equals(fave.getGameName())) {
                                        favorites.remove(fave);

                                        GridElementAdapter adapter = new GridElementAdapter(getActivity(), "favorites", favorites);
                                        faveGrid.setHorizontalMargin(20);
                                        faveGrid.setRowHeight(400);
                                        faveGrid.setAdapter(adapter);
                                        faveGrid.invalidate();

                                        removeFromFavorites(fave.getGameName());

                                        Timer t = new Timer();
                                        t.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                                                        trash.startAnimation(out);
                                                        out.setAnimationListener(new Animation.AnimationListener() {
                                                            @Override
                                                            public void onAnimationStart(Animation animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animation animation) {
                                                                trash.setVisibility(View.GONE);
                                                                trash.setBackgroundDrawable(null);
                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animation animation) {

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }, 500);
                                    }
                                }
                            break;
                        }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (dropEventNotHandled(event)) {
                        View view2 = (View) event.getLocalState();
                        view2.setVisibility(View.VISIBLE);
                    }
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    private boolean dropEventNotHandled(DragEvent dragEvent) {
        return !dragEvent.getResult();
    }

}

