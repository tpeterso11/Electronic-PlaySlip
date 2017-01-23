package com.shoutz.toussaintpeterson.electronicpayslip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import butterknife.Bind;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestSelectFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private LinearLayout pick;
    private LinearLayout main;
    private LinearLayout second;
    private CustomExpandableListView grid;
    private CustomExpandableListView symbolGrid;
    private String[] choiceList;
    private int maxNumberChoice;
    private ArrayList<String> numberChoices;
    private String gameType;
    private ArrayList<TextView> tiles;
    public ArrayList<Integer> tileValuesArray;
    public ArrayList<String> letterValuesArray;
    private int numberLeft;
    private int maxNumber;
    private TextView choiceLeft;
    public int ticketNumberLeft;
    private Random r;
    private TextView howMany;
    //private int primaryCost;
    public ArrayList<Ticket> gamePicks;
    private TextView ticketNumber;
    private int[] availableNumbers;
    private RelativeLayout submit;
    public ArrayList<ArrayList> masterArray;
    public ArrayList<ArrayList> tempCart;
    private Boolean contains;
    private ImageView plus;
    private ImageView minus;
    private TextView ticketCount;
    private RelativeLayout quickPickAll;
    private RelativeLayout quickPickOne;
    private JSONObject response;
    private Bundle args;
    private URL tileUrl;
    public String gameName;
    public String savedNumbers;
    private String cost;
    public String gameId;
    private String maxChoice;
    private String description;
    public String iconSet;
    private TextView game;
    private TextView descrip;
    private TextView costTag;
    private ArrayList<TextView> textView;
    private ScrollView phoneLayout;
    private LinearLayout one;
    private LinearLayout two;
    private LinearLayout three;
    private LinearLayout four;
    private LinearLayout five;
    private LinearLayout six;
    private LinearLayout seven;
    private LinearLayout eight;
    private LinearLayout nine;
    private LinearLayout zero;
    private TextView digitOne;
    private TextView digitTwo;
    private TextView digitThree;
    private TextView digitFour;
    private ImageView tile;
    private ArrayList<String> iconSetString;
    private ArrayList<Integer> cardPos;
    private TextView gameNameView;
    private TextView remaining;
    public String wager;
    private TextView choose;
    private TextView pickText;
    private TextView savedText;
    private TextView countEnd;
    private LinearLayout saved;
    private Game currentGame;
    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.test_select_frag, container, false);

        args = getArguments();
        pick = (LinearLayout) rootView.findViewById(R.id.pick);
        main = (LinearLayout) rootView.findViewById(R.id.select_main);
        second = (LinearLayout) rootView.findViewById(R.id.select_second);
        grid = (CustomExpandableListView) rootView.findViewById(R.id.number_selection_grid);
        choiceLeft = (TextView) rootView.findViewById(R.id.choice_count);
        phoneLayout = (ScrollView) rootView.findViewById(R.id.phone_layout);
        symbolGrid = (CustomExpandableListView) rootView.findViewById(R.id.symbol_selection_grid);
        quickPickAll = (RelativeLayout)rootView.findViewById(R.id.quick_pick_all);
        quickPickOne = (RelativeLayout)rootView.findViewById(R.id.quick_pick_one);
        ticketNumber = (TextView) rootView.findViewById(R.id.ticket_number);
        submit = (RelativeLayout) rootView.findViewById(R.id.submit);
        plus = (ImageView) rootView.findViewById(R.id.plus);
        minus = (ImageView) rootView.findViewById(R.id.minus);
        costTag = (TextView) rootView.findViewById(R.id.cost_label);
        //quickPickAll = (RelativeLayout)rootView.findViewById(R.id.quick_pick_all);
        ticketCount = (TextView) rootView.findViewById(R.id.new_counter);
        tile = (ImageView) rootView.findViewById(R.id.game_tile);
        howMany = (TextView) rootView.findViewById(R.id.how_many);
        //remaining = (TextView) rootView.findViewById(R.id.remaining);
        choose = (TextView) rootView.findViewById(R.id.choose);
        saved = (LinearLayout) rootView.findViewById(R.id.saved);
        pickText = (TextView) rootView.findViewById(R.id.pick_text);
        savedText = (TextView) rootView.findViewById(R.id.saved_text);
        countEnd = (TextView)rootView.findViewById(R.id.choose_end);

        iconSet = args.getString("iconSet");
        wager = args.getString("wager");
        maxNumber = Integer.valueOf(args.getString("maxChoice"));
        if (maxNumber == 0) {
            iconSet = "none";
        }

        switch (iconSet) {
            case "letters":
                pickText.setText("Pick Your Letters");
                savedText.setText("Use Saved Letters");
                countEnd.setText("From Letters Below");
                break;
            case "numbers":
                pickText.setText("Pick Your Numbers");
                savedText.setText("Use Saved Numbers");
                countEnd.setText("From Numbers Below");
                break;
            case "phone":
                pickText.setText("Enter Your Number");
                countEnd.setVisibility(View.GONE);
                break;
            case "cards":
                pickText.setText("Pick Your Cards");
                savedText.setText("Use Saved Cards");
                countEnd.setText("From Cards Below");
                break;
            case "symbols":
                pickText.setText("Pick Your Symbols");
                savedText.setText("Use Saved Symbols");
                countEnd.setText("From Symbols Below");
                break;
            case "none":
                iconSet = "numbers";
                pickText.setText("Add to List");
                break;
        }

        pickText.setTypeface(getEpsApplication().sub);


        try {
            tileUrl = new URL(getArguments().getString("tileUrl"));
            Glide.with(getActivity())
                    .load(tileUrl)
                    .into(tile);
        } catch (MalformedURLException ex) {
            //do nothing
        }

        ticketNumberLeft = 0;

        ticketCount.setText(String.valueOf(ticketNumberLeft));

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticketNumberLeft++;
                ticketCount.setText(String.valueOf(ticketNumberLeft));

                int current = Integer.valueOf(cost);
                int newCurrent = ticketNumberLeft * current;

                String currentCost = "Total: $" + newCurrent;
                costTag.setText(currentCost);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(ticketCount.getText().toString()) > 0) {
                    ticketNumberLeft--;
                    ticketCount.setText(String.valueOf(ticketNumberLeft));

                    int current = Integer.valueOf(cost);
                    int newCurrent = ticketNumberLeft * current;

                    String currentCost = "Total: $" + newCurrent;
                    costTag.setText(currentCost);
                }
            }
        });

        masterArray = getEpsApplication().masterArray;
        tempCart = getEpsApplication().tempCart;
        gameNameView = (TextView) rootView.findViewById(R.id.game_name);
        ticketNumber.setText(String.valueOf(1));
        gamePicks = new ArrayList<Ticket>();
        grid.setExpanded(true);

        maxNumberChoice = Integer.valueOf(args.getString("maxValue"));
        gameId = args.getString("gameId");
        maxNumber = Integer.valueOf(args.getString("maxChoice"));
        //gameType = args.getString("gameType");
        gameName = args.getString("gameName");
        cost = args.getString("cost");

        String str = gameName + ": Ticket";
        gameNameView.setText(str);

        String currentCost = "Total: 0";
        costTag.setText(currentCost);
        numberChoices = new ArrayList<String>();

        setIconSet();
        tiles = new ArrayList<TextView>();
        tileValuesArray = new ArrayList<Integer>();
        letterValuesArray = new ArrayList<String>();
        numberLeft = maxNumber;
        availableNumbers = new int[maxNumber];
        currentGame = new Game();
        //primaryCost = 2;
        r = new Random();

        Map<String, ?> keys = prefs.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String current = entry.getKey();
            for (Game game : getEpsApplication().games) {
                if (current.equals(game.getGameName()) && current.equals(gameName)) {
                    currentGame.setSavedNumbers(entry.getValue().toString());
                    currentGame.setGameId(game.getGameId());
                    currentGame.setWager(game.getWager());
                    saved.setVisibility(View.VISIBLE);

                    ((LandingActivity)getActivity()).revealSavedButton(gameName, gameId, wager);
                }
            }
        }

        ((LandingActivity)getActivity()).checkSaved();
        //checkSaved();

        quickPickAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickPick("all");
            }
        });

        quickPickOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickPick("one");
            }
        });

        if (saved.getVisibility() == View.VISIBLE) {
            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LandingActivity)getActivity()).startLoading();
                    ticketNumberLeft = Integer.valueOf(ticketCount.getText().toString());

                    ArrayList<String> numbers = new ArrayList<String>();

                    StringTokenizer tokenizer = new StringTokenizer(currentGame.getSavedNumbers(), "- ");
                    while (tokenizer.hasMoreTokens()) {
                        numbers.add(tokenizer.nextToken());
                    }


                    while (ticketNumberLeft > 0) {
                        Ticket ticket = new Ticket();
                        ticket.setGameId(currentGame.getGameId());
                        ticket.setWager(currentGame.getWager());
                        ticket.setNumbers(numbers);
                        gamePicks.add(ticket);

                        ticketNumberLeft--;
                    }

                    new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, "", getActivity()).execute();
                }
            });
        }



        setFonts();

        //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
        choiceLeft.setText(String.valueOf(numberLeft));
        //maxNumberChoice = Integer.valueOf(getIntent().getStringExtra("maxValue"));

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getEpsApplication().getUser().getAuthToken().equals("null")) {
                    alertLogin(getActivity());
                }

                else {
                    if (maxNumber == 0) {
                        while (ticketNumberLeft > 0) {
                            ArrayList<String> numbers = new ArrayList<String>();

                            Ticket ticket = new Ticket();
                            if (user != null) {
                                ticket.setUserName(user.getUsername());
                            } else {
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(cost));
                            ticket.setGameName(gameName);

                            gamePicks.add(ticket);
                            ticketNumberLeft--;
                        }

                        new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, null, getActivity()).execute();
                        //((LandingActivity) getActivity()).launchTempCart();
                        //((LandingActivity) getActivity()).dismissSelect();
                    }

                    else if (ticketNumberLeft > 0) {
                        final Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom);
                        final Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
                        main.startAnimation(out);
                        second.startAnimation(in);
                        main.setVisibility(View.GONE);
                        second.setVisibility(View.VISIBLE);

                        switch (iconSet) {
                            case "numbers":
                                phoneLayout.setVisibility(View.GONE);
                                grid.setVisibility(View.VISIBLE);
                                break;
                            case "letters":
                                phoneLayout.setVisibility(View.GONE);
                                grid.setVisibility(View.VISIBLE);
                                break;
                            case "phone":
                                grid.setVisibility(View.GONE);
                                phoneLayout.setVisibility(View.VISIBLE);

                                one = (LinearLayout) rootView.findViewById(R.id.one);
                                two = (LinearLayout) rootView.findViewById(R.id.two);
                                three = (LinearLayout) rootView.findViewById(R.id.three);
                                four = (LinearLayout) rootView.findViewById(R.id.four);
                                five = (LinearLayout) rootView.findViewById(R.id.five);
                                six = (LinearLayout) rootView.findViewById(R.id.six);
                                seven = (LinearLayout) rootView.findViewById(R.id.seven);
                                eight = (LinearLayout) rootView.findViewById(R.id.eight);
                                nine = (LinearLayout) rootView.findViewById(R.id.nine);
                                zero = (LinearLayout) rootView.findViewById(R.id.zero);

                                digitOne = (TextView) rootView.findViewById(R.id.digit_one);
                                digitTwo = (TextView) rootView.findViewById(R.id.digit_two);
                                digitThree = (TextView) rootView.findViewById(R.id.digit_three);
                                digitFour = (TextView) rootView.findViewById(R.id.digit_four);

                                textView = new ArrayList<TextView>();


                                //Switch these to onTouch so we can catch the down event and change the color of the number buttons

                                one.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.one_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                one.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                one.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("1");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                two.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.two_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                two.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                two.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("2");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                three.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.three_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                three.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                three.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("3");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                four.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.four_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                four.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                four.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("4");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                five.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.five_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                five.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                five.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("5");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                six.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.six_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                six.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                six.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("6");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                seven.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.seven_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                seven.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                seven.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("7");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                eight.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.eight_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                eight.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                eight.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("8");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                nine.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.nine_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                nine.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                nine.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("9");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                zero.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        TextView tag = (TextView) rootView.findViewById(R.id.zero_tag);
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:
                                                zero.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                                                tag.setTextColor(getResources().getColor(R.color.blue));
                                                break;

                                            case MotionEvent.ACTION_UP:
                                                zero.setBackgroundResource(R.drawable.rounded_button_trans);
                                                tag.setTextColor(getResources().getColor(R.color.white));
                                                setNumber("0");
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                textView.add(digitOne);
                                textView.add(digitTwo);
                                textView.add(digitThree);
                                textView.add(digitFour);
                                break;
                        }
                        ((LandingActivity) getActivity()).showSelect();
                    } else {
                        Crouton.makeText(getActivity(), "Must Select At Least One Ticket!", Style.ALERT).show();
                    }

                    //((LandingActivity)getActivity()).scrollToTop();
                }
            }
        });

        if(iconSet.equals("letters") || iconSet.equals("numbers") || iconSet.equals("phone")) {
            grid.setAdapter(new TestSelectFragment.BallAdapter(getActivity()));
            symbolGrid.setVisibility(View.GONE);
        }
        else {
            cardPos = new ArrayList<Integer>();
            symbolGrid.setAdapter(new SymbolAdapter(getActivity()));
            symbolGrid.setExpanded(true);
            symbolGrid.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
        }

        //grid.setAdapter(new TestSelectFragment.BallAdapter(getActivity()));


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 3);
        title = getArguments().getString("Select");
    }


    public static TestSelectFragment newInstance() {
        TestSelectFragment fragmentFirst = new TestSelectFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 3);
        args.putString("someTitle", "Select");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
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
                if (getResources().getString(R.string.tablet_mode).equals("true")) {
                    btn.setLayoutParams(new GridView.LayoutParams(50, 50));
                } else {
                    btn.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    btn.setPadding(10, 15, 10, 15);
                }

            } else {
                btn = (TextView) convertView;
            }

            if (iconSet.equals("numbers") || iconSet.equals("letters")) {
                btn.setText(choiceList[position]);
                // filenames is an array of strings
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(R.drawable.rounded_button_trans);
                btn.setId(position);
                btn.setClickable(true);
                btn.setTypeface(getEpsApplication().sub, Typeface.BOLD);
                btn.setGravity(Gravity.CENTER);
                btn.setOnClickListener(new TestSelectFragment.TileListener(position));

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

            if (iconSet.equals("number") && tileValuesArray.contains(Integer.valueOf(btn.getText().toString())) || iconSet.equals("letter") && letterValuesArray.contains(btn.getText().toString())) {
                btn.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                btn.setTextColor(Color.BLUE);
            }

            return btn;
        }
    }

    private void setIconSet() {
        switch (iconSet) {
            case "numbers":
                choiceList = new String[maxNumberChoice];
                for (int i = 0; i < maxNumberChoice; i++) {
                    choiceList[i] = String.valueOf(i + 1);
                }
                numberChoices.addAll(Arrays.asList(choiceList));
                break;
            case "phone":
                choiceList = new String[maxNumberChoice];
                for (int i = 0; i < maxNumberChoice; i++) {
                    choiceList[i] = String.valueOf(i + 1);
                }
                numberChoices.addAll(Arrays.asList(choiceList));
                break;
            case "letters":
                numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.letter_select1)));
                choiceList = getResources().getStringArray(R.array.letter_select1);
                break;
            case "cards":
                numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.card_select1)));
                choiceList = getResources().getStringArray(R.array.card_select1);
                break;
            case "lucky":
                numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.lucky_select1)));
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

            if(iconSet.equals("cards")){
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

    class TileListener implements View.OnClickListener {
        private final int position;

        public TileListener(int position) {
            this.position = position;
        }

        public void onClick(View v) {
            TextView tile = (TextView) v;

            if (iconSet.equals("numbers")) {
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

                    if(tileValuesArray.size() == 0){
                        ((LandingActivity)getActivity()).showHideSaveCheck("hide");
                    }
                    tile.setBackgroundResource(R.drawable.rounded_button_trans);
                    tile.setTextColor(getResources().getColor(R.color.white));
                    numberLeft++;

                    choiceLeft.setText(String.valueOf(numberLeft));
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                    AnimationSet set2 = new AnimationSet(true);
                            set2.addAnimation(in);
                            set2.addAnimation(slideI);
                            choiceLeft.startAnimation(set2);
                            //choiceLeft.setVisibility(View.VISIBLE);


                    //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));

                } else {
                    if (tileValuesArray.size() == maxNumber) {
                        //do nothing
                    } else {
                        // It's a new value, so add it to the array
                        tileValuesArray.add(ballValue);

                        if(tileValuesArray.size() == 1){
                            ((LandingActivity)getActivity()).showHideSaveCheck("show");
                        }

                        tile.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                        tile.setTextColor(getResources().getColor(R.color.blue));
                        numberLeft--;


                        choiceLeft.setText(String.valueOf(numberLeft));
                        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                        AnimationSet set2 = new AnimationSet(true);
                        set2.addAnimation(in);
                        set2.addAnimation(slideI);
                        choiceLeft.startAnimation(set2);
                        //choiceLeft.setVisibility(View.VISIBLE);

                        //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                    }
                }
            } else {
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
                    tile.setBackgroundResource(R.drawable.rounded_button_trans);
                    tile.setTextColor(getResources().getColor(R.color.white));
                    numberLeft++;


                    if(letterValuesArray.size() == 0){
                        ((LandingActivity)getActivity()).showHideSaveCheck("hide");
                    }
                            choiceLeft.setText(String.valueOf(numberLeft));
                            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                            AnimationSet set2 = new AnimationSet(true);
                            set2.addAnimation(in);
                            set2.addAnimation(slideI);
                            choiceLeft.startAnimation(set2);
                            //choiceLeft.setVisibility(View.VISIBLE);

                    //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));

                } else {
                    if (letterValuesArray.size() == maxNumber) {
                        //do nothing
                    } else {
                        // It's a new value, so add it to the array
                        letterValuesArray.add(ballValue);
                        tile.setBackgroundResource(R.drawable.rounded_button_trans_selected);
                        tile.setTextColor(getResources().getColor(R.color.blue));
                        numberLeft--;

                        if(letterValuesArray.size() == 1){
                            ((LandingActivity)getActivity()).showHideSaveCheck("show");
                        }

                                choiceLeft.setText(String.valueOf(numberLeft));
                                Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                                Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                                AnimationSet set2 = new AnimationSet(true);
                                set2.addAnimation(in);
                                set2.addAnimation(slideI);
                                choiceLeft.startAnimation(set2);
                                //choiceLeft.setVisibility(View.VISIBLE);

                        //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                    }
                }
            }

            if (iconSet.equals("numbers")) {
                // Always Sort array to make sure it is lowest to highest
                Collections.sort(tileValuesArray);
            } else if (iconSet.equals("letters")) {
                Collections.sort(letterValuesArray);
            }
        }
    }


        public void resetSelect() {
            if (second.getVisibility() == View.VISIBLE) {
                Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);

                main.startAnimation(in);
                second.startAnimation(out);
                main.setVisibility(View.VISIBLE);
                second.setVisibility(View.GONE);

                ((LandingActivity) getActivity()).dismissSelect();
                ((LandingActivity) getActivity()).scrollToTop();
            } else {
                ((LandingActivity) getActivity()).launchGameBack(args);
            }
        }

        public void quickPick(String selection) {
            if(getEpsApplication().getUser().getAuthToken().equals("null")){
                alertLogin(getActivity());
            }
            else {
                if (selection.equals("all")) {
                    while (ticketNumberLeft > 0) {
                        numberLeft = maxNumber;
                        if (iconSet.equals("numbers")) {
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
                            if (user != null) {
                                ticket.setUserName(user.getUsername());
                            } else {
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(cost));
                            ticket.setGameName(gameName);

                            gamePicks.add(ticket);
                            tileValuesArray.clear();
                            ticketNumberLeft--;
                        } else if (iconSet.equals("letters")) {
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
                            if (user != null) {
                                ticket.setUserName(user.getUsername());
                            } else {
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(cost));
                            ticket.setGameName(gameName);

                            gamePicks.add(ticket);
                            letterValuesArray.clear();
                            ticketNumberLeft--;
                        } else if (iconSet.equals("cards") || iconSet.equals("symbols")) {
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

                            if (iconSet.equals("cards")) {
                                numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.card_select1)));
                            } else {
                                numberChoices.addAll(Arrays.asList(getResources().getStringArray(R.array.lucky_select1)));
                            }

                            ArrayList<String> numbers = new ArrayList<String>();
                            for (String card : letterValuesArray) {
                                numbers.add(card);
                            }

                            Collections.sort(numbers);
                            Ticket ticket = new Ticket();
                            if (user != null) {
                                ticket.setUserName(user.getUsername());
                            } else {
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(cost));
                            ticket.setGameName(gameName);

                            gamePicks.add(ticket);
                            letterValuesArray.clear();
                            ticketNumberLeft--;
                        } else if (iconSet.equals("phone")) {
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
                            if (user != null) {
                                ticket.setUserName(user.getUsername());
                            } else {
                                ticket.setUserName("null");
                            }
                            ticket.setNumbers(numbers);
                            ticket.setGameId(gameId);
                            ticket.setWager(String.valueOf(cost));
                            ticket.setGameName(gameName);

                            gamePicks.add(ticket);
//                letterValuesArray.clear();
                            ticketNumberLeft--;
                        }
                    }
                    //addtoMaster();
                    //new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, null, getActivity()).execute();
                    //((LandingActivity) getActivity()).launchTempCart();
                    //((LandingActivity) getActivity()).dismissSelect();

                } else {
                    numberLeft = maxNumber;
                    if (iconSet.equals("numbers")) {
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
                        if (user != null) {
                            ticket.setUserName(user.getUsername());
                        } else {
                            ticket.setUserName("null");
                        }
                        ticket.setNumbers(numbers);
                        ticket.setGameId(gameId);
                        ticket.setWager(String.valueOf(cost));
                        ticket.setGameName(gameName);

                        gamePicks.add(ticket);
                        tileValuesArray.clear();

                        transitionTicket();
                    } else if (iconSet.equals("letters")) {
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
                        if (user != null) {
                            ticket.setUserName(user.getUsername());
                        } else {
                            ticket.setUserName("null");
                        }
                        ticket.setNumbers(numbers);
                        ticket.setGameId(gameId);
                        ticket.setWager(String.valueOf(cost));
                        ticket.setGameName(gameName);

                        gamePicks.add(ticket);
                        letterValuesArray.clear();
                        transitionTicket();
                    }
                }

                if (ticketNumberLeft == 0) {
                    //addtoMaster();
                    new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, null, getActivity()).execute();
                    //((LandingActivity) getActivity()).launchTempCart();
                    ((LandingActivity) getActivity()).dismissSelect();
                    //showVerification();
                } /*else if (pick.getVisibility() == View.VISIBLE) {
                //Animation rotate = AnimationUtils.loadAnimation(GameViewActivity.this, R.anim.rotate);
                //quickPick.startAnimation(rotate);

                if (gameType.equals("letter")) {
                    letterValuesArray.clear();
                } else if (gameType.equals("number")) {
                    tileValuesArray.clear();
                }

                ticketNumberLeft--;
                numberLeft = maxNumber;

                if (pick.getVisibility() == View.VISIBLE) {
                    if (gameType.equals("letter") || gameType.equals("number")) {
                        grid.setAdapter(new BallAdapter(getActivity()));
                        //symbolGrid.setVisibility(View.GONE);
                    } else {
                        //symbolGrid.setAdapter(new SymbolAdapter(getActivity()));
                        grid.setVisibility(View.GONE);
                    }

                    if (ticketNumberLeft == 1) {
                        //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));
                    }
                }
            } else {
                //    showVerification();
            }
        }*/
            }
        }

        public void transitionTicket() {
            if (ticketNumberLeft > 0) {
                Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
                Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

                AnimationSet setOut = new AnimationSet(true);
                setOut.addAnimation(slide);
                setOut.addAnimation(fadeOut);

                ticketNumber.startAnimation(setOut);

                int current = Integer.valueOf(ticketNumber.getText().toString());
                current++;
                ticketNumber.setText(String.valueOf(current));

                Animation slideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                AnimationSet setIn = new AnimationSet(true);
                setIn.addAnimation(slideIn);
                setIn.addAnimation(fadeIn);

                ticketNumber.startAnimation(setIn);

                ticketNumberLeft--;

                if(iconSet.equals("phone")){
                    digitOne.setText("");
                    digitTwo.setText("");
                    digitThree.setText("");
                    digitFour.setText("");
                }

                ((LandingActivity)getActivity()).revealSavedButton(gameName, gameId, wager);

                numberLeft = maxNumber;



                        choiceLeft.setText(String.valueOf(numberLeft));
                        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                        AnimationSet set2 = new AnimationSet(true);
                        set2.addAnimation(in);
                        set2.addAnimation(slideI);
                        choiceLeft.startAnimation(set2);
                        //choiceLeft.setVisibility(View.VISIBLE);

                //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));

                grid.setAdapter(new BallAdapter(getActivity()));

                ((LandingActivity) getActivity()).scrollToTop();
            }
        }

        private void updateCart(int number) {
            getEpsApplication().setCartNumber(number);
            DecimalFormat formatter = new DecimalFormat("00");
            String aFormatted = formatter.format(number);
        }

        public void submit() {
            if(getEpsApplication().getUser().getAuthToken().equals("null")){
                alertLogin(getActivity());
            }
            else {
                ArrayList<String> numbers = new ArrayList<String>();
                if (gamePicks == null) {
                    gamePicks = new ArrayList<Ticket>();
                }
                if (iconSet.equals("numbers") || iconSet.equals("phone")) {
                    for (int i = 0; i < availableNumbers.length; i++) {
                        if (tileValuesArray.size() < maxNumber) {
                            Crouton.makeText(getActivity(), getString(R.string.missing), Style.ALERT).show();
                            return;
                        } else {
                            numbers.add(tileValuesArray.get(i).toString());
                        }
                    }
                } else if (iconSet.equals("letters") || iconSet.equals("cards") || iconSet.equals("symbols")) {
                    for (int i = 0; i < availableNumbers.length; i++) {
                        if (letterValuesArray.size() < maxNumber) {
                            Crouton.makeText(getActivity(), getString(R.string.missing), Style.ALERT).show();
                            return;
                        } else {
                            numbers.add(letterValuesArray.get(i));
                        }
                    }
                }

                if (ticketNumberLeft > 1) {
                    if (!iconSet.equals("phone")) {
                        Collections.sort(numbers);
                    }
                    Ticket ticket = new Ticket();
                    if (user != null && user.getUsername() != null) {
                        ticket.setUserName(user.getUsername());
                    } else {
                        ticket.setUserName("null");
                    }
                    ticket.setNumbers(numbers);
                    ticket.setGameId(gameId);
                    ticket.setWager(String.valueOf(cost));
                    ticket.setGameName(gameName);

                    gamePicks.add(ticket);
                    //ticketNumberLeft--;

                    if (iconSet.equals("numbers") || iconSet.equals("phone")) {
                        tileValuesArray.clear();
                    } else if (iconSet.equals("letters") || iconSet.equals("cards") || iconSet.equals("symbols")) {
                        letterValuesArray.clear();
                    }
                    /*if(gameType.equals("letter") || gameType.equals("number")) {
                        grid.setAdapter(new GameViewActivity.BallAdapter(GameViewActivity.this));
                        symbolGrid.setVisibility(View.GONE);
                    }
                    else {
                        symbolGrid.setAdapter(new GameViewActivity.SymbolAdapter(GameViewActivity.this));
                        grid.setVisibility(View.GONE);
                    }*/

                /*for (int i = 0; i < maxNumber; i++) {
                    numberLeft++;
                    choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                }*/

                    transitionTicket();

                    return;

                } else {
                    Collections.sort(numbers);
                    Ticket ticket = new Ticket();
                    if (user != null && user.getUsername() != null) {
                        ticket.setUserName(user.getUsername());
                    } else {
                        ticket.setUserName("null");
                    }
                    ticket.setNumbers(numbers);
                    ticket.setGameId(gameId);
                    ticket.setWager(String.valueOf(cost));
                    ticket.setGameName(gameName);
                    gamePicks.add(ticket);
                    //donePick.setImageDrawable(ContextCompat.getDrawable(GameViewActivity.this, R.drawable.ic_done_all_white_24dp));

                    updateCart(gamePicks.size());

                    //addtoMaster();
                    new PostTicketTask(getResources().getString(R.string.post_ticket_service), gamePicks, null, getActivity()).execute();
                    //((LandingActivity) getActivity()).launchTempCart();
                    ((LandingActivity) getActivity()).dismissSelect();

                    //Toast.makeText(getActivity(), "End", Toast.LENGTH_LONG).show();

                    //showVerification();
                    //dismissPick();
                }
            }
        }

        private void addtoMaster() {
        /*if(getEpsApplication().masterArray == null){
            getEpsApplication().createMaster();
            masterArray = getEpsApplication().masterArray;
        }
        if (masterArray.isEmpty()) {
            masterArray.add(gamePicks);
        }*/
            if (getEpsApplication().tempCart == null) {
                getEpsApplication().createCart();
                tempCart = getEpsApplication().tempCart;
            }
            if (tempCart.isEmpty()) {
                tempCart.add(gamePicks);
            } else {
                ListIterator<ArrayList> iter = tempCart.listIterator();
                System.out.println("Forward iteration :");
                if (!(gamePicks.isEmpty())) {
                    while (iter.hasNext()) {
                        contains = false;
                        ArrayList<Ticket> n = iter.next();
                        if (n.get(0).getGameName().equals(gamePicks.get(0).getGameName())) {
                            ArrayList<Ticket> newArray = new ArrayList<Ticket>();
                            newArray.addAll(gamePicks);
                            for (Ticket ticket : n) {
                                newArray.add(ticket);
                            }
                            iter.remove();
                            iter.add(newArray);

                            contains = true;

                            getEpsApplication().isChanged = true;

                            return;

                        } else {
                            contains = false;
                        }
                    }
                    if (!contains) {
                        tempCart.add(gamePicks);
                        contains = true;
                    }
                } else {
                    return;
                }
            }
            getEpsApplication().isChanged = true;
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
                ((LandingActivity)getActivity()).launchTempCart();
            }

            @Override
            protected void onError(Object response) {
                //Toast.makeText(getContext(), "Fail", Toast.LENGTH_LONG).show();
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

    private void setNumber(String number){
        for(int i = 0; i < textView.size(); i++){
            if(i == 3){
            }
            if(textView.get(i).getText().equals("")){
                textView.get(i).setText(number);
                tileValuesArray.add(Integer.valueOf(number));
                numberLeft--;


                        choiceLeft.setText(String.valueOf(numberLeft));
                        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                        AnimationSet set2 = new AnimationSet(true);
                        set2.addAnimation(in);
                        set2.addAnimation(slideI);
                        choiceLeft.startAnimation(set2);
                        //choiceLeft.setVisibility(View.VISIBLE);

                //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                return;
            }
        }
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


                        choiceLeft.setText(String.valueOf(numberLeft));
                        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                        AnimationSet set2 = new AnimationSet(true);
                        set2.addAnimation(in);
                        set2.addAnimation(slideI);
                        choiceLeft.startAnimation(set2);
                        //choiceLeft.setVisibility(View.VISIBLE);

                //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                }

            else {
                if (letterValuesArray.size() == maxNumber) {
                    //do nothing
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


                            choiceLeft.setText(String.valueOf(numberLeft));
                            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            Animation slideI = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                            AnimationSet set2 = new AnimationSet(true);
                            set2.addAnimation(in);
                            set2.addAnimation(slideI);
                            choiceLeft.startAnimation(set2);
                            //choiceLeft.setVisibility(View.VISIBLE);

                    //choiceLeft.setText(new DecimalFormat("00").format(numberLeft));
                }
            }
            Collections.sort(letterValuesArray);
        }

        // Now repopulate the balls with current values
        // Any balls after the length of the value array get cleared.

        // When we have collected 5 values then switch to the red balls
        //}
    }

    private void setFonts(){
        howMany.setTypeface(getEpsApplication().sub);
        costTag.setTypeface(getEpsApplication().main);
        gameNameView.setTypeface(getEpsApplication().sub);
        ticketCount.setTypeface(getEpsApplication().sub);
        ticketNumber.setTypeface(getEpsApplication().sub);
        countEnd.setTypeface(getEpsApplication().main);
        //remaining.setTypeface(getEpsApplication().main);
        choose.setTypeface(getEpsApplication().main);
    }

}