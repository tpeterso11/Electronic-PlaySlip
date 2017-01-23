package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestTicketsFragment extends AbstractEPSFragment implements AdapterView.OnItemSelectedListener {
    private String title;
    private int page;
    private ArrayList<String> categories;
    private Spinner ticketChoices;
    private String[] languages = {"Filter By Status", "All Tickets", "Winning Tickets", "Ready To Play", "Redeemed", "Pending Tickets",
            "History"};
    private ArrayList<Ticket> tickets;
    private CustomExpandableListView grid;
    private CustomExpandableListView winningGrid;
    private CustomExpandableListView historyGrid;
    private CustomExpandableListView redeemedGrid;
    private JSONObject response;
    private ArrayList<Ticket> openTickets;
    private ArrayList<Ticket> allTickets;
    private ArrayList<Ticket> unviewed;
    private ArrayList<Ticket> winningTickets;
    private ArrayList<Ticket> pending;
    private ArrayList<Ticket> ready;
    private ArrayList<Ticket> redeemed;
    private ArrayList<Ticket> sorting;
    private LinearLayout ticketFilterLayout;
    private RelativeLayout ticketFilters;
    private String currentList;
    private ImageView closeFilters;
    private ImageView openFilters;
    private RelativeLayout allTicketsLayout;
    private RelativeLayout readyLayout;
    private RelativeLayout pendingLayout;
    private int allCount;
    private ArrayList<Ticket> viewed;
    private boolean filtersVisible;
    private LinearLayout winningTagLayout;
    private LinearLayout redeemedTagLayout;
    private LinearLayout historyTagLayout;
    private LinearLayout readyTagLayout;
    private TextView winningTag;
    private TextView readyTag;
    private TextView historyTag;
    private TextView zero;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_tickets_frag, container, false);

        ticketFilterLayout = (LinearLayout) rootView.findViewById(R.id.ticket_filter_layout);
        ticketFilters = (RelativeLayout) rootView.findViewById(R.id.ticket_filter);
        closeFilters = (ImageView) rootView.findViewById(R.id.close_filters);
        //openFilters = (ImageView) rootView.findViewById(R.id.open_filters);
        //allTicketsLayout = (RelativeLayout)rootView.findViewById(R.id.all_ticket_layout);
        //readyLayout = (RelativeLayout)rootView.findViewById(R.id.ready_layout);
        //pendingLayout = (RelativeLayout)rootView.findViewById(R.id.pending_layout);
        grid = (CustomExpandableListView) rootView.findViewById(R.id.grid);
        winningGrid = (CustomExpandableListView) rootView.findViewById(R.id.winning_grid);
        historyGrid = (CustomExpandableListView) rootView.findViewById(R.id.history_grid);
        redeemedGrid = (CustomExpandableListView)rootView.findViewById(R.id.redeemed_grid);
        winningTagLayout = (LinearLayout)rootView.findViewById(R.id.winning_tag_layout);
        historyTagLayout = (LinearLayout)rootView.findViewById(R.id.history_tag_layout);
        redeemedTagLayout = (LinearLayout)rootView.findViewById(R.id.redeemed_tag_layout);
        readyTagLayout = (LinearLayout)rootView.findViewById(R.id.ready_tag_layout);
        ticketChoices = (Spinner)rootView.findViewById(R.id.ticket_choice);
        winningTag = (TextView)rootView.findViewById(R.id.winning_tag);
        historyTag = (TextView)rootView.findViewById(R.id.history_tag);
        readyTag = (TextView)rootView.findViewById(R.id.ready_tag);
        zero = (TextView)rootView.findViewById(R.id.zero);

        ticketChoices.setOnItemSelectedListener(this);

        ((LandingActivity)getActivity()).showTicketFilters();

        if(!((LandingActivity)getActivity()).isLoading()) {
            ((LandingActivity) getActivity()).showShade();
        }

        //if(!((LandingActivity)getActivity()).isLoading()){

        //}

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner2, R.id.spinner_text, categories);

        MyAdapter adapter = new MyAdapter(getContext(), R.layout.custom_spinner2, languages);
        ticketChoices.setAdapter(adapter);

        // Drop down layout style - list view with radio button
        //adapter.setDropDownViewResource(R.layout.custom_spinner);


        // attaching data adapter to spinner
        //ticketChoices.setAdapter(adapter);

        //((LandingActivity)getActivity()).disableScroll();

        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        if (hasBackKey && hasHomeKey) {
            // no navigation bar, unless it is enabled in the settings
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout
                    .LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0, getNavHeight());
            grid.setLayoutParams(layoutParams);
            // 99% sure there's a navigation bar
        }

        grid.setExpanded(true);
        winningGrid.setExpanded(true);
        historyGrid.setExpanded(true);
        redeemedGrid.setExpanded(true);

        /*ticketFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!filtersVisible) {
                    ((LandingActivity) getActivity()).showTicketFilters();
                    filtersVisible = true;
                }
            }
        });*/

        currentList = "all";

        setFonts();
        ((LandingActivity)getActivity()).scrollToTop();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("Landing");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!((LandingActivity)getActivity()).isLoading()) {
            ((LandingActivity) getActivity()).startLoadingProgressed();
        }
        else{
            new GetTicketsUserTask(getResources().getString(R.string.get_tickets_user), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
        }
    }


    public static TestTicketsFragment newInstance() {
        TestTicketsFragment fragmentFirst = new TestTicketsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 0);
        args.putString("someTitle", "Landing");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String item = parent.getItemAtPosition(i).toString();

        switch(item){
            case "Filter By Status":
                break;
            case "All Tickets":
                if(ready.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    grid.setVisibility(View.VISIBLE);
                    grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
                    readyTagLayout.setVisibility(View.VISIBLE);
                    readyTag.setText("Ready to Play");
                }
                else {
                    readyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.INVISIBLE);
                }
                if(winningTickets.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    winningGrid.setVisibility(View.VISIBLE);
                    winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
                    winningTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    winningGrid.setVisibility(View.INVISIBLE);
                    winningTagLayout.setVisibility(View.GONE);
                }

                if(redeemed.size() > 0){
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    redeemedGrid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    redeemedGrid.setVisibility(View.VISIBLE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    redeemedTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.INVISIBLE);
                }

                if(viewed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    historyGrid.setVisibility(View.VISIBLE);
                    historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
                    historyTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    historyGrid.setVisibility(View.INVISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                }

                if(viewed.size() == 0 && redeemed.size() == 0 && winningTickets.size() == 0 && ready.size() == 0){
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }

                //history
                //redeemed
                //pending transactions

                //winning tickets -> if redeemed, do not show

                //if list == 0 (no tickets at this time)
                ((LandingActivity)getActivity()).scrollToTop();
                currentList = "all";
                break;
            case "Winning Tickets":
                if(winningTickets.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
                    currentList = "winning";

                    winningTagLayout.setVisibility(View.VISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.VISIBLE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.VISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "Ready To Play":
                if(ready.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
                    currentList = "ready";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTag.setText("Ready to Play");
                    grid.setVisibility(View.VISIBLE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "History":
                if(viewed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
                    currentList = "viewed";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.VISIBLE);
                    readyTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.VISIBLE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.VISIBLE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;

            case "Redeemed":
                if(redeemed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    redeemedGrid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    redeemedGrid.setVisibility(View.VISIBLE);
                    currentList = "redeemed";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);
                    readyTagLayout.setVisibility(View.GONE);
                    //readyTag.setText("Redeemed Tickets");
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "Pending Tickets":
                if(pending.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    grid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    currentList = "redeemed";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTag.setText("Pending Tickets");
                    grid.setVisibility(View.VISIBLE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);

                    readyTag.setText("Pending Tickets");
                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
        }

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Creating an Adapter Class
    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

// Inflating the layout for the custom Spinner
            LayoutInflater inflater = getActivity().getLayoutInflater();
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

    public class GridViewAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Ticket> array;
        private String list;

        public GridViewAdapter(Context mContext, String list, ArrayList<Ticket> array) {
            this.mContext = mContext;
            this.list = list;
            this.array = array;
        }


        @Override
        public int getCount() {
            switch(list) {
                case "all":
                    array = allTickets;
                    return allTickets.size();
                case "ready":
                    array = ready;
                    return ready.size();
                case "pending":
                    array = pending;
                    return pending.size();
                case "winning":
                    array = winningTickets;
                    return winningTickets.size();
                case "viewed":
                    array = viewed;
                    return viewed.size();
                case "redeemed":
                    array = redeemed;
                    return redeemed.size();
                default:
                    return allTickets.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            convertView = null;

            if (convertView == null) {

                //gridView = new View(getActivity());

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.my_tickets_row, null);

                // set value into textview
                TextView name = (TextView)gridView.findViewById(R.id.game_name);
                name.setText((array.get(position).getGameName()));
                name.setTypeface(getEpsApplication().sub);

                ImageView icon = (ImageView)gridView.findViewById(R.id.icon);
                for(Game game : getEpsApplication().games){
                    if(game.getGameName().equals(array.get(position).getGameName())){
                        Glide.with(getActivity())
                                .load(game.getIconUrl())
                                .crossFade(R.anim.fade_in, 800)
                                .into(icon);
                    }
                }

                if((array.get(position).getIsViewed()).equals("false")){
                    TextView status = (TextView)gridView.findViewById(R.id.status);
                    status.setText("Ready to View");
                    status.setTypeface(getEpsApplication().main);

                    TextView purchase = (TextView)gridView.findViewById(R.id.purchase_date);
                    String st = "Action: " + array.get(position).getDatePurchased().substring(0,10);
                    purchase.setText(st);

                    RelativeLayout redeem = (RelativeLayout)gridView.findViewById(R.id.redeem);
                    redeem.setVisibility(View.VISIBLE);

                    redeem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            ArrayList<String> userNumberArray = new ArrayList<String>();

                            if(array.get(position).getUserNumbers() != null) {
                                StringTokenizer userTokenizer = new StringTokenizer(array.get(position).getUserNumbers(), "- ");
                                while (userTokenizer.hasMoreTokens()) {
                                    userNumberArray.add(userTokenizer.nextToken());
                                }
                                bundle.putStringArrayList("userNumbers" ,userNumberArray);
                            }

                            if(array.get(position).getWinningNumbers() != null) {
                                if(array.get(position).getWinningNumbers().contains("|")){
                                    ArrayList<String> numbers = new ArrayList<String>();
                                    StringTokenizer multiTokenizer = new StringTokenizer(array.get(position).getWinningNumbers(), "|");
                                    while(multiTokenizer.hasMoreTokens()){
                                        numbers.add(multiTokenizer.nextToken());
                                    }

                                    bundle.putStringArrayList("winningNumbers", numbers);
                                }
                                else {
                                    ArrayList<String> winNumberArray = new ArrayList<String>();
                                    StringTokenizer winTokenizer = new StringTokenizer(array.get(position).getWinningNumbers(), "- ");
                                    while (winTokenizer.hasMoreTokens()) {
                                        winNumberArray.add(winTokenizer.nextToken());
                                    }
                                    bundle.putStringArrayList("winningNumbers", winNumberArray);
                                }
                            }

                            bundle.putInt("template", array.get(position).getTemplateId());
                            bundle.putString("ticketId", array.get(position).getTicketId());
                            bundle.putString("batchId", array.get(position).getBatchId());
                            bundle.putString("viewed", array.get(position).getIsViewed());

                            ((LandingActivity)getActivity()).launchScratch(array.get(position).getTicketId(), array.get(position).getBatchId(), array.get(position).getIsViewed(), array.get(position).getTemplateId(), "", bundle);
                        }
                    });
                }
                else if(!(array.get(position).getStatus().equals("closed, not a winner") && (array.get(position).getIsViewed()).equals("true")) && winningTickets.contains(array.get(position))){
                    TextView status = (TextView)gridView.findViewById(R.id.status);
                    status.setText("Winner");
                    status.setTypeface(getEpsApplication().main);
                    TextView purchase = (TextView)gridView.findViewById(R.id.purchase_date);
                    String st = "Action: " + parseDateToddMMyyyy(array.get(position).getDatePurchased().substring(0,10)).replace("-", "/");
                    purchase.setText(st);

                    RelativeLayout redeem = (RelativeLayout)gridView.findViewById(R.id.redeem);
                    TextView redeemText = (TextView)gridView.findViewById(R.id.redeem_text);
                    redeem.setBackgroundResource(R.drawable.rounded_layout_purp);
                    redeemText.setText("Redeem!");
                    redeem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> userNumberArray = new ArrayList<String>();
                            ArrayList<String> winNumberArray = new ArrayList<String>();

                            if(array.get(position).getUserNumbers() != null) {
                                StringTokenizer userTokenizer = new StringTokenizer(array.get(position).getUserNumbers(), "- ");
                                while (userTokenizer.hasMoreTokens()) {
                                    userNumberArray.add(userTokenizer.nextToken());
                                }
                            }
                            else{
                                userNumberArray = null;
                            }

                            if(array.get(position).getWinningNumbers() != null) {
                                StringTokenizer winTokenizer = new StringTokenizer(array.get(position).getWinningNumbers(), "- ");
                                while (winTokenizer.hasMoreTokens()) {
                                    winNumberArray.add(winTokenizer.nextToken());
                                }
                            }
                            else{
                                winNumberArray = null;
                            }

                            ((LandingActivity)getActivity()).launchRedeem(createBundle(array.get(position).getGameName(), array.get(position).getGameId(), array.get(position).getTicketId(), array.get(position).getBatchId(), array.get(position).getWinAmount(), array.get(position).getDatePurchased(), array.get(position).getTemplateId(), array.get(position).getIsViewed(), userNumberArray, winNumberArray));
                        }
                    });
                }
                else{
                    RelativeLayout redeem = (RelativeLayout)gridView.findViewById(R.id.redeem);
                    redeem.setVisibility(View.GONE);

                    RelativeLayout view = (RelativeLayout)gridView.findViewById(R.id.view);
                    view.setVisibility(View.VISIBLE);
                    TextView purchase = (TextView)gridView.findViewById(R.id.purchase_date);

                    if(list.equals("redeemed")){
                        view.setBackgroundResource(R.drawable.rounded_layout_purp);
                    }
                    String st = "Action: " + array.get(position).getDatePurchased().substring(0,10);
                    purchase.setText(st);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            ArrayList<String> userNumberArray = new ArrayList<String>();

                            if(array.get(position).getUserNumbers() != null) {
                                StringTokenizer userTokenizer = new StringTokenizer(array.get(position).getUserNumbers(), "- ");
                                while (userTokenizer.hasMoreTokens()) {
                                    userNumberArray.add(userTokenizer.nextToken());
                                }
                                bundle.putStringArrayList("userNumbers" ,userNumberArray);
                            }

                            if(array.get(position).getWinningNumbers() != null) {
                                ArrayList<String> winNumberArray = new ArrayList<String>();
                                StringTokenizer winTokenizer = new StringTokenizer(array.get(position).getWinningNumbers(), "- ");
                                while (winTokenizer.hasMoreTokens()) {
                                    winNumberArray.add(winTokenizer.nextToken());
                                }
                                bundle.putStringArrayList("winningNumbers", winNumberArray);
                            }

                            bundle.putInt("template", array.get(position).getTemplateId());
                            bundle.putString("ticketId", array.get(position).getTicketId());
                            bundle.putString("batchId", array.get(position).getBatchId());
                            bundle.putString("viewed", array.get(position).getIsViewed());

                            ((LandingActivity)getActivity()).launchScratch(array.get(position).getTicketId(), array.get(position).getBatchId(), array.get(position).getIsViewed(), array.get(position).getTemplateId(), "", bundle);
                        }
                    });

                    TextView status = (TextView)gridView.findViewById(R.id.status);
                    status.setVisibility(View.INVISIBLE);
                }

                }

             else {
                gridView = (View) convertView;
            }

            gridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            gridView.setPadding(10,10,10,10);

            return gridView;
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
            allTickets = new ArrayList<Ticket>();
            openTickets = new ArrayList<Ticket>();

            unviewed = new ArrayList<Ticket>();
            ready = new ArrayList<Ticket>();
            winningTickets = new ArrayList<Ticket>();
            pending = new ArrayList<Ticket>();
            redeemed = new ArrayList<Ticket>();
            sorting = new ArrayList<Ticket>();
            viewed = new ArrayList<Ticket>();

            try{
                JSONObject responseJSON = new JSONObject(response.toString());
                for(int i = 0; i < responseJSON.getJSONArray("open").length(); i++){
                    JSONObject obj = responseJSON.getJSONArray("open").getJSONObject(i);
                    Ticket current = new Ticket();
                    if(getEpsApplication().isSpanish){
                        current.setGameName(obj.getString("gamename_es"));
                        current.setStatus(obj.getString("status_es"));
                    }
                    else {
                        current.setGameName(obj.getString("gamename"));
                        current.setStatus(obj.getString("status"));
                    }
                    current.setBatchId(obj.getString("batchid"));
                    current.setUserNumbers(obj.getString("numbers"));
                    current.setStatus(obj.getString("status"));
                    current.setDate(obj.getString("actiondate"));
                    current.setWager(obj.getString("betamount"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));
                    if(obj.getString("signature") != null) {
                        current.setSignature(obj.getString("signature"));
                    }
                    else{
                        current.setSignature("");
                    }

                    //allTickets.add(current);
                    openTickets.add(current);
                    allCount++;

                }

                for(int i = 0; i < responseJSON.getJSONArray("unviewed").length(); i++){
                    JSONObject obj = responseJSON.getJSONArray("unviewed").getJSONObject(i);
                    Ticket current = new Ticket();
                    if(getEpsApplication().isSpanish){
                        current.setGameName(obj.getString("gamename_es"));
                        current.setStatus(obj.getString("status_es"));
                    }
                    else {
                        current.setGameName(obj.getString("gamename"));
                        current.setStatus(obj.getString("status"));
                    }
                    current.setWinningNumbers(obj.getString("winningnumbers"));
                    current.setDate(obj.getString("actiondate"));
                    current.setUserNumbers(obj.getString("numbers"));
                    current.setWager(obj.getString("betamount"));
                    current.setDatePurchased(obj.getString("actiondate"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));

                    allTickets.add(current);
                    ready.add(current);

                    if((obj.getString("status").equals("redeemded") || (obj.getString("status_es").equals("redimido")))){
                        redeemed.add(current);
                    }

                    allCount++;

                }

                for(int i = 0; i < responseJSON.getJSONArray("winning").length(); i++){
                    JSONObject obj = responseJSON.getJSONArray("winning").getJSONObject(i);
                    Ticket current = new Ticket();
                    if(getEpsApplication().isSpanish){
                        current.setGameName(obj.getString("gamename_es"));
                        current.setStatus(obj.getString("status_es"));
                    }
                    else {
                        current.setGameName(obj.getString("gamename"));
                        current.setStatus(obj.getString("status"));
                    }
                    current.setWinningNumbers(obj.getString("winningnumbers"));
                    current.setUserNumbers(obj.getString("numbers"));
                    current.setStatus(obj.getString("status"));
                    current.setDatePurchased(obj.getString("actiondate"));
                    current.setDate(obj.getString("actiondate"));
                    current.setWager(obj.getString("betamount"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setDatePurchased(obj.getString("purchasedate"));

                    if(current.getIsViewed().equals("true")) {
                        winningTickets.add(current);
                    }
                    else{
                        allTickets.add(current);
                    }

                    allCount++;
                }

                for(int i = 0; i < responseJSON.getJSONArray("redeemed").length(); i++){
                    JSONObject obj = responseJSON.getJSONArray("redeemed").getJSONObject(i);
                    Ticket current = new Ticket();
                    if(getEpsApplication().isSpanish){
                        current.setGameName(obj.getString("gamename_es"));
                        current.setStatus(obj.getString("status_es"));
                    }
                    else {
                        current.setGameName(obj.getString("gamename"));
                        current.setStatus(obj.getString("status"));
                    }
                    current.setWinningNumbers(obj.getString("winningnumbers"));
                    current.setUserNumbers(obj.getString("numbers"));
                    current.setStatus(obj.getString("status"));
                    current.setDate(obj.getString("actiondate"));
                    current.setWager(obj.getString("betamount"));
                    current.setDatePurchased(obj.getString("actiondate"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));

                    allTickets.add(current);
                    redeemed.add(current);
                    allCount++;
                }

                for(int i = 0; i < responseJSON.getJSONArray("closed").length(); i++){
                    JSONObject obj = responseJSON.getJSONArray("closed").getJSONObject(i);
                    Ticket current = new Ticket();
                    if(getEpsApplication().isSpanish){
                        current.setGameName(obj.getString("gamename_es"));
                        current.setStatus(obj.getString("status_es"));
                    }
                    else {
                        current.setGameName(obj.getString("gamename"));
                        current.setStatus(obj.getString("status"));
                    }
                    current.setWinningNumbers(obj.getString("winningnumbers"));
                    current.setUserNumbers(obj.getString("numbers"));
                    current.setStatus(obj.getString("status"));
                    current.setDate(obj.getString("actiondate"));
                    current.setDatePurchased(obj.getString("actiondate"));
                    current.setWager(obj.getString("betamount"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));

                    if(current.getIsViewed().equals("true") && !(winningTickets.contains(current))){
                        viewed.add(current);
                    }

                    if(current.getStatus().equals("closed, not a winner")) {
                        sorting.add(current);
                    }
                    //allTickets.add(current);
                    allCount++;
                }

                if(sorting.size() > 0) {
                    Collections.sort(sorting, new Comparator<Ticket>() {
                        public int compare(Ticket m1, Ticket m2) {
                            return m1.getDate().compareTo(m2.getDate());
                        }
                    });

                    Collections.reverse(sorting);

                    allTickets.addAll(sorting);
                }

                if(redeemed.size() > 0) {
                    Collections.sort(redeemed, new Comparator<Ticket>() {
                        public int compare(Ticket o1, Ticket o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    Collections.reverse(redeemed);
                }

                if(viewed.size() > 0){
                    Collections.sort(viewed, new Comparator<Ticket>() {
                        public int compare(Ticket o1, Ticket o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    Collections.reverse(viewed);
                }

                if(winningTickets.size() > 0){
                    Collections.sort(winningTickets, new Comparator<Ticket>() {
                        public int compare(Ticket o1, Ticket o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    Collections.reverse(winningTickets);
                }

                if(ready.size() > 0){
                    Collections.sort(ready, new Comparator<Ticket>() {
                        public int compare(Ticket o1, Ticket o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    Collections.reverse(ready);
                }

                NumberFormat f = new DecimalFormat("00");

                currentList = "all";

                /*Ticket t = new Ticket();
                t.setIsViewed("false");
                t.setBatchId("null");
                t.setDate("October 18");
                t.setTemplateId(4);
                t.setGameName("Box of Cash");
                t.setStatus("closed");

                allTickets.add(t);*/
                //createTickets(allTickets);
                Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                if(winningTickets.size() > 0) {
                    winningTagLayout.setVisibility(View.VISIBLE);
                    winningGrid.setLayoutAnimation(controller);
                    winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
                }
                else{
                    winningTagLayout.setVisibility(View.INVISIBLE);
                    winningGrid.setVisibility(View.INVISIBLE);
                }
                if(ready.size() > 0) {
                    readyTagLayout.setVisibility(View.VISIBLE);
                    grid.setLayoutAnimation(controller);
                    grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
                }
                else{
                    readyTagLayout.setVisibility(View.INVISIBLE);
                    grid.setVisibility(View.INVISIBLE);
                }
                historyTagLayout.setVisibility(View.GONE);
                historyGrid.setVisibility(View.GONE);

                /*if(redeemed.size() > 0){
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    redeemedGrid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    redeemedGrid.setVisibility(View.VISIBLE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    redeemedTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.INVISIBLE);
                }*/

                ((LandingActivity)getActivity()).scrollToTop();
                /*if(viewed.size() > 0) {
                    historyTagLayout.setVisibility(View.VISIBLE);
                    historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
                }
                else{
                    historyTagLayout.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.INVISIBLE);
                }*/

                //viewed.clear();
                //ready.clear();
                //winningTickets.clear();

                if(winningTickets.size() == 0 && ready.size() == 0){
                    readyTagLayout.setVisibility(View.GONE);
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }

                ((LandingActivity)getActivity()).stopLoadingSuccess();

            }
            catch(JSONException ex){
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onError(Object response) {
            ((LandingActivity)getActivity()).clearLoading();
            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
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

    private void dismissFilters(){
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(out);
        set.addAnimation(fade);

        closeFilters.startAnimation(set);
        closeFilters.setVisibility(View.GONE);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
                Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(out);
                set.addAnimation(fade);
                openFilters.startAnimation(slide);
                openFilters.setVisibility(View.VISIBLE);

                ticketFilters.startAnimation(set);
                ticketFilters.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private int getNavHeight(){
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public void setGrid(String type){
        switch(type){
            case "All Tickets":
                if(ready.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    grid.setVisibility(View.VISIBLE);
                    grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
                    readyTagLayout.setVisibility(View.VISIBLE);
                    readyTag.setText("Ready to Play");
                }
                else {
                    readyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.INVISIBLE);
                }
                if(winningTickets.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    winningGrid.setVisibility(View.VISIBLE);
                    winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
                    winningTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    winningGrid.setVisibility(View.INVISIBLE);
                    winningTagLayout.setVisibility(View.GONE);
                }

                if(redeemed.size() > 0){
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    redeemedGrid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    redeemedGrid.setVisibility(View.VISIBLE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    redeemedTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.INVISIBLE);
                }

                if(viewed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    historyGrid.setVisibility(View.VISIBLE);
                    historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
                    historyTagLayout.setVisibility(View.VISIBLE);
                }
                else{
                    historyGrid.setVisibility(View.INVISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                }

                if(viewed.size() == 0 && redeemed.size() == 0 && winningTickets.size() == 0 && ready.size() == 0){
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }

                //history
                //redeemed
                //pending transactions

                //winning tickets -> if redeemed, do not show

                //if list == 0 (no tickets at this time)
                ((LandingActivity)getActivity()).scrollToTop();
                currentList = "all";
                break;
                case "Winning Tickets":
                if(winningTickets.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                    winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
                    winningGrid.setLayoutAnimation(controller);
                    currentList = "winning";

                    winningTagLayout.setVisibility(View.VISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.VISIBLE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.VISIBLE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "Ready To Play":
                if(ready.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }

                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                    grid.setLayoutAnimation(controller);
                    grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
                    ((LandingActivity)getActivity()).scrollToTop();
                    currentList = "ready";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTag.setText("Ready to Play");
                    grid.setVisibility(View.VISIBLE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.VISIBLE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
                case "History":
                if(viewed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                    historyGrid.setLayoutAnimation(controller);
                    currentList = "viewed";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.VISIBLE);
                    readyTagLayout.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.VISIBLE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.VISIBLE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.GONE);
                    readyTagLayout.setVisibility(View.GONE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "Redeemed":
                if(redeemed.size() > 0) {
                    if(zero.getVisibility() == View.VISIBLE){
                        zero.setVisibility(View.GONE);
                    }
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                    redeemedGrid.setLayoutAnimation(controller);
                    redeemedGrid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                    redeemedGrid.setVisibility(View.VISIBLE);
                    currentList = "redeemed";

                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);
                    readyTagLayout.setVisibility(View.GONE);
                    //readyTag.setText("Redeemed Tickets");
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    ((LandingActivity)getActivity()).scrollToTop();
                }
                else{
                    winningTagLayout.setVisibility(View.GONE);
                    historyTagLayout.setVisibility(View.GONE);
                    grid.setVisibility(View.GONE);
                    winningGrid.setVisibility(View.GONE);
                    historyGrid.setVisibility(View.GONE);
                    redeemedGrid.setVisibility(View.GONE);
                    redeemedTagLayout.setVisibility(View.VISIBLE);

                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                break;
            case "Pending Tickets":
            if(pending.size() > 0) {
                if(zero.getVisibility() == View.VISIBLE){
                    zero.setVisibility(View.GONE);
                }
                Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                grid.setLayoutAnimation(controller);
                grid.setAdapter(new GridViewAdapter(getActivity(), "redeemed", redeemed));
                currentList = "redeemed";

                winningTagLayout.setVisibility(View.GONE);
                historyTagLayout.setVisibility(View.GONE);
                readyTagLayout.setVisibility(View.VISIBLE);
                redeemedGrid.setVisibility(View.GONE);
                redeemedTagLayout.setVisibility(View.GONE);
                readyTag.setText("Pending Tickets");
                grid.setVisibility(View.VISIBLE);
                winningGrid.setVisibility(View.GONE);
                historyGrid.setVisibility(View.GONE);
                ((LandingActivity)getActivity()).scrollToTop();
            }
            else{
                winningTagLayout.setVisibility(View.GONE);
                historyTagLayout.setVisibility(View.GONE);
                grid.setVisibility(View.GONE);
                winningGrid.setVisibility(View.GONE);
                historyGrid.setVisibility(View.GONE);
                redeemedGrid.setVisibility(View.GONE);
                redeemedTagLayout.setVisibility(View.GONE);
                readyTagLayout.setVisibility(View.VISIBLE);

                readyTag.setText("Pending Tickets");
                zero.setVisibility(View.VISIBLE);
                zero.bringToFront();
            }
            break;
        }
    }

    public void dismiss(){
        if(!currentList.equals("all")){
            grid.setAdapter(new GridViewAdapter(getActivity(), "ready", ready));
            winningGrid.setAdapter(new GridViewAdapter(getActivity(), "winning", winningTickets));
            historyGrid.setAdapter(new GridViewAdapter(getActivity(), "viewed", viewed));
            currentList = "all";

            winningTagLayout.setVisibility(View.VISIBLE);
            historyTagLayout.setVisibility(View.VISIBLE);
            readyTagLayout.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);
            winningGrid.setVisibility(View.VISIBLE);
            historyGrid.setVisibility(View.VISIBLE);
        }
        else{
            ((LandingActivity)getActivity()).hideTicketFilters();
            ((LandingActivity)getActivity()).launchLanding();
        }
    }

    private void setFonts(){
        winningTag.setTypeface(getEpsApplication().sub);
        readyTag.setTypeface(getEpsApplication().sub);
        historyTag.setTypeface(getEpsApplication().sub);
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/RalewayThin.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }

    private Bundle createBundle(String gamename, String gameId, String ticketId, String batchId, String prizeAmount, String purchaseDate, int template, String viewed, ArrayList<String> userNumbers, ArrayList<String> winningNumbers) {
        Bundle args = new Bundle();
        args.putString("gameName", gamename);
        args.putString("gameId", gameId);
        args.putString("ticketId", ticketId);
        args.putString("batchId", batchId);
        args.putString("winAmount", prizeAmount);
        args.putString("purchaseDate", purchaseDate);
        args.putInt("template", template);
        args.putString("viewed", viewed);
        args.putStringArrayList("userNumbers", userNumbers);
        args.putStringArrayList("winningNumbers", winningNumbers);
        args.putInt("someInt", 2);
        args.putString("someTitle", "Game");

        return args;
    }

    private String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MM-dd-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void loadTickets(){
        new GetTicketsUserTask(getResources().getString(R.string.get_tickets_user), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
    }
}

