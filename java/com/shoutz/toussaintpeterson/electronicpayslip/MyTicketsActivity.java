package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 6/21/16.
 */
public class MyTicketsActivity extends AbstractEPSActivity {
    @Bind(R.id.tickets) ExpandableListView icons;
    @Bind(R.id.open_layout) RelativeLayout openLayout;
    @Bind(R.id.purchased_layout) RelativeLayout purchasedLayout;
    @Bind(R.id.mytickets_instructions) RelativeLayout myTicketsInstruc;
    @Bind(R.id.shoppinglist_instructions) RelativeLayout shoppingInstruc;
    @Bind(R.id.winning_layout) RelativeLayout winningLayout;
    @Bind(R.id.redeemed_layout) RelativeLayout redeemedLayout;
    @Bind(R.id.pending_layout) RelativeLayout pendingLayout;
    @Bind(R.id.purchased) TextView purchased;
    @Bind(R.id.open_tag) TextView openTag;
    @Bind(R.id.category_count) TextView catCount;
    @Bind(R.id.loading) RelativeLayout loading;
    @Bind(R.id.progressBar1) ProgressBar progressBar;
    @Bind(R.id.no_tickets)TextView noTickets;
    @Bind(R.id.cat_desc_text) TextView catDesc;
    @Bind(R.id.ready) RelativeLayout readyLayout;
    @Bind(R.id.all_1) TextView all1;
    @Bind(R.id.all_2) TextView all2;
    @Bind(R.id.all_3) TextView all3;
    @Bind(R.id.all_4) TextView all4;
    private ArrayList<Integer> positions;
    private String currentList;
    private ArrayList<Game> games;
    private GroupViewHolder groupViewHolder;
    private ArrayList<Ticket> tempTickets;
    private JSONObject response;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int openCount;
    private int allCount;
    private ArrayList<Ticket> openTickets;
    private ArrayList<Ticket> allTickets;
    private ArrayList<Ticket> unviewed;
    private ArrayList<Ticket> winningTickets;
    private ArrayList<Ticket> pending;
    private ArrayList<Ticket> ready;
    private ArrayList<Ticket> redeemed;
    private ArrayList<Integer> scratchers;
    private boolean contains;
    private Ticket temp;
    private Animation in;
    private Animation out;
    private ArrayList<Ticket> sorting;

    @Override
    public void onBackPressed(){
        if(allTickets != null) {
            allTickets.clear();
        }
        if(openTickets != null){
            openTickets.clear();
        }
        if(winningTickets != null){
            winningTickets.clear();
        }

        Intent i = new Intent(MyTicketsActivity.this, LandingActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tickets);
        ButterKnife.bind(this);

        openCount = 0;
        allCount = 0;
        loading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        positions = new ArrayList<Integer>();

        currentList = "all";

        scratchers = new ArrayList<Integer>();
        scratchers.add(1);
        scratchers.add(2);

        openTag.setTypeface(getEpsApplication().main);
        purchased.setTypeface(getEpsApplication().main);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);

        openLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentList.equals("shopping")) {
                    openLayout.setBackgroundResource(R.drawable.border_right_selected);
                    TextView text = (TextView) findViewById(R.id.open);
                    text.setTextColor(getResources().getColor(R.color.app_blue));
                    text.setTypeface(null, Typeface.BOLD);
                    openTag.setTypeface(null, Typeface.BOLD);
                    catCount.setText(String.valueOf(openCount));

                    purchasedLayout.setBackgroundResource(R.drawable.border_right);
                    purchased.setTextColor(getResources().getColor(R.color.white));
                    purchased.setTypeface(null, Typeface.NORMAL);

                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                    slide.setStartOffset(100);
                    in.setStartOffset(100);

                    myTicketsInstruc.startAnimation(slide);
                    myTicketsInstruc.setVisibility(View.INVISIBLE);

                    shoppingInstruc.startAnimation(in);
                    shoppingInstruc.setVisibility(View.VISIBLE);

                    currentList = "shopping";

                    createTickets(openTickets);

                    //new GetTicketsListTask(getResources().getString(R.string.get_tickets_list), "open", getApplicationContext()).execute();
                }
            }
        });


        pendingLayout.setEnabled(false);
        openLayout.setEnabled(false);

        purchasedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentList.equals("all")) {
                    purchasedLayout.setBackgroundResource(R.drawable.border_right_selected);
                    TextView text = (TextView) findViewById(R.id.purchased);
                    text.setTextColor(getResources().getColor(R.color.app_blue));
                    text.setTypeface(null, Typeface.BOLD);
                    catCount.setText(String.valueOf(allCount));

                    TextView open = (TextView) findViewById(R.id.open);
                    openLayout.setBackgroundColor(getResources().getColor(R.color.app_blue));
                    open.setTextColor(getResources().getColor(R.color.white));
                    open.setTypeface(null, Typeface.NORMAL);

                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                    slide.setStartOffset(100);
                    in.setStartOffset(100);

                    shoppingInstruc.startAnimation(slide);
                    shoppingInstruc.setVisibility(View.INVISIBLE);

                    myTicketsInstruc.startAnimation(in);
                    myTicketsInstruc.setVisibility(View.VISIBLE);

                    currentList = "all";

                    createTickets(allTickets);
                }
            }
        });

        if(myTicketsInstruc.getVisibility() == View.VISIBLE){
            winningLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterButton("winning", currentList);
                }
            });
            redeemedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterButton("redeemed", currentList);
                }
            });
            pendingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterButton("pending", currentList);
                }
            });

            readyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterButton("ready", currentList);
                }
            });

        }

        tempTickets = new ArrayList<Ticket>();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        TextView title = (TextView)findViewById(R.id.category_title);
        title.setTypeface(getEpsApplication().main);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(MyTicketsActivity.this, listDataHeader, listDataChild);
        ExpandableListView expListView = (ExpandableListView)findViewById(R.id.tickets);
        expListView.setAdapter(listAdapter);

        NumberFormat f = new DecimalFormat("###,##0.0");

        new GetTicketsUserTask(getResources().getString(R.string.get_tickets_user), getEpsApplication().getUser().getAuthToken(), getApplicationContext()).execute();
        //Toast.makeText(getApplicationContext(), String.valueOf(Runtime.getRuntime().maxMemory()), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "Max available memory: " + f.format(Runtime.getRuntime().maxMemory()) + "MB", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        public View getChildView(final int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_sub, null);
            }


                TextView txtListChild = (TextView) convertView
                        .findViewById(R.id.lblListItem);
                LinearLayout main = (LinearLayout)convertView
                        .findViewById(R.id.confirm_cards);
                main.setVisibility(View.GONE);

                txtListChild.setText("");
                txtListChild.setTypeface(sub);

                txtListChild.setText(childText);
                txtListChild.setTypeface(getEpsApplication().sub);

                txtListChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.gc();
                        String test = _listDataHeader.get(groupPosition);

                        if(currentList.equals("all")) {
                            if (!allTickets.get(groupPosition).getStatus().equals("open")) {
                                /*if(allTickets.get(groupPosition).getGameName().equals("Last Four") || allTickets.get(groupPosition).getGameName().equals("Los 4 últimos")){
                                    new GetTicketsForView(getResources().getString(R.string.getticketview), allTickets.get(groupPosition).getTicketId(), allTickets.get(groupPosition).getBatchId(), getApplicationContext()).execute();
                                }
                                else if(scratchers.contains(allTickets.get(groupPosition).getTemplateId())){
                                    new DownloadImageTask(allTickets.get(groupPosition).getTicketId(), "xxhdpi", "en", allTickets.get(groupPosition).getGameName(), allTickets.get(groupPosition).getTemplateId()).execute();
                                }

                                else if(allTickets.get(groupPosition).getGameName().equals("Alphabet Soup")){
                                    Intent i = new Intent(MyTicketsActivity.this, TestScratcherActivity.class);
                                    i.putExtra("gameName", _listDataHeader.get(groupPosition));
                                    i.putExtra("userNumbers", allTickets.get(groupPosition).getUserNumbers());
                                    i.putExtra("ticketId",allTickets.get(groupPosition).getTicketId());
                                    i.putExtra("batchId",allTickets.get(groupPosition).getBatchId());
                                    i.putExtra("winningNumbers", allTickets.get(groupPosition).getWinningNumbers());
                                    i.putExtra("template", 2);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                    finish();
                                }
                                else {*/
                                    Intent i = new Intent(MyTicketsActivity.this, TestScratcherActivity.class);
                                    i.putExtra("gameName", _listDataHeader.get(groupPosition));
                                    i.putExtra("userNumbers", allTickets.get(groupPosition).getUserNumbers());
                                    i.putExtra("ticketId",allTickets.get(groupPosition).getTicketId());
                                    i.putExtra("batchId",allTickets.get(groupPosition).getBatchId());
                                    i.putExtra("winningNumbers", allTickets.get(groupPosition).getWinningNumbers());
                                    i.putExtra("template", allTickets.get(groupPosition).getTemplateId());
                                    i.putExtra("viewed", allTickets.get(groupPosition).getIsViewed());
                                    startActivity(i);
                                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                    finish();
                                //}
                            } else {
                                Intent i = new Intent(MyTicketsActivity.this, TestScratcherActivity.class);
                                i.putExtra("userNumbers", allTickets.get(groupPosition).getUserNumbers());//tempTickets.get(groupPosition).getUserNumbers());
                                i.putExtra("winningNumbers", allTickets.get(groupPosition).getWinningNumbers());//tempTickets.get(groupPosition).getWinningNumbers());
                                i.putExtra("template", allTickets.get(groupPosition).getTemplateId());
                                startActivity(i);
                                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                finish();
                            }
                        }
                        else if(currentList.equals("shopping")){
                            Intent i = new Intent(MyTicketsActivity.this, CustomerInstructionsActivity.class);
                            i.putExtra("batchid", openTickets.get(groupPosition).getBatchId());
                            i.putExtra("signature", openTickets.get(groupPosition).getSignature());
                            i.putExtra("from", "mytickets");
                            startActivity(i);
                            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                            finish();
                        }
                    }
                });
                //subShown = true;

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
            if (convertView == null) {
                positions.add(groupPosition);
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                /*if ((groupPosition % 2) == 0) {
                    convertView = infalInflater.inflate(R.layout.expandable_list_cell_tickets_even, null);
                    convertView.setPadding(10, 10, 10, 10);
                } else {
                    convertView = infalInflater.inflate(R.layout.expandable_list_cell_tickets, null);
                    convertView.setPadding(10, 10, 10, 10);
                }


                TextView lblListHeader = (TextView) convertView
                        .findViewById(R.id.cell_title);
                lblListHeader.setTypeface(getEpsApplication().main);
                TextView lblListDate = (TextView) convertView
                        .findViewById(R.id.cell_date);

                ImageView lblStatusImg = (ImageView) convertView.findViewById(R.id.ticket_status);

                lblListHeader.setText("");
                lblListDate.setText("");
                lblStatusImg.setImageResource(0);

                lblListHeader.setText(headerTitle);

                if (currentList.equals("all")) {
                    lblListDate.setText(allTickets.get(groupPosition).getDate());
                    String status = allTickets.get(groupPosition).getStatus();

                    if (allTickets.get(groupPosition).getIsViewed().equals("false") && !(allTickets.get(groupPosition).getStatus().equals("open"))) {
                        lblStatusImg.setImageResource(R.drawable.ready_to_view);
                    } else if (allTickets.get(groupPosition).getIsViewed().equals("true") && !(allTickets.get(groupPosition).getStatus().equals("open")) && (allTickets.get(groupPosition).getStatus().equals("winner")) || (allTickets.get(groupPosition).getStatus().equals("ganador"))) {
                        lblStatusImg.setImageResource(R.drawable.winning_ticket_white);
                    }
                    else if (allTickets.get(groupPosition).getIsViewed().equals("true") && !(allTickets.get(groupPosition).getStatus().equals("open")) && (allTickets.get(groupPosition).getStatus().equals("redeemed")) || (allTickets.get(groupPosition).getStatus().equals("redimido"))) {
                        lblStatusImg.setImageResource(R.drawable.redeemed_white);
                    }
                    else if (allTickets.get(groupPosition).getIsViewed().equals("false") && (allTickets.get(groupPosition).getStatus().equals("open"))) {
                        lblStatusImg.setImageResource(R.drawable.pending_white);
                    }
                else {
                        lblStatusImg.setVisibility(View.INVISIBLE);
                    }
                } else if (currentList.equals("winning")) {
                    lblStatusImg.setImageResource(R.drawable.winning_ticket_white);
                    lblListDate.setText(allTickets.get(groupPosition).getDate());
                } else if (currentList.equals("ready")) {
                    lblListDate.setText(allTickets.get(groupPosition).getDate());
                    String status = allTickets.get(groupPosition).getStatus();
                    lblStatusImg.setImageResource(R.drawable.ready_to_view);
                }
                else if (currentList.equals("redeemed")) {
                    lblListDate.setText(allTickets.get(groupPosition).getDate());
                    String status = allTickets.get(groupPosition).getStatus();
                    lblStatusImg.setImageResource(R.drawable.redeemed_white);
                }
                else {
                    lblListDate.setText(openTickets.get(groupPosition).getDate());
                    String status = openTickets.get(groupPosition).getStatus();
                    lblStatusImg.setImageResource(R.drawable.store_white);
                }*/
                return convertView;
            }
            else{
                return convertView;
            }
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
                    openCount++;
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
                    current.setDate(obj.getString("actiondate"));
                    current.setWager(obj.getString("betamount"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));

                    allTickets.add(current);
                    winningTickets.add(current);
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
                    current.setWager(obj.getString("betamount"));
                    current.setWinAmount(obj.getString("winamount"));
                    current.setIsViewed(obj.getString("viewed"));
                    current.setTicketId(obj.getString("ticketid"));
                    current.setBatchId(obj.getString("batchid"));
                    current.setTemplateId(obj.getInt("templateid"));


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

                NumberFormat f = new DecimalFormat("00");
                catCount.setText(String.valueOf(f.format(allTickets.size())));

                currentList = "all";

                Ticket t = new Ticket();
                t.setIsViewed("false");
                t.setBatchId("null");
                t.setDate("October 18");
                t.setTemplateId(4);
                t.setGameName("Box of Cash");
                t.setStatus("closed");

                allTickets.add(t);

                createTickets(allTickets);
                purchasedLayout.setEnabled(true);
                openLayout.setEnabled(true);
                winningLayout.setEnabled(true);
                redeemedLayout.setEnabled(true);
                pendingLayout.setEnabled(true);
                readyLayout.setEnabled(true);

            }
            catch(JSONException ex){
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onError(Object response) {
            if(loading.getVisibility() == View.VISIBLE){
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                loading.startAnimation(fade);
                loading.setVisibility(View.GONE);
            }
            Toast.makeText(MyTicketsActivity.this, "Fail", Toast.LENGTH_LONG).show();
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

    /*public class GetTicketsForView extends AbstractWebService {
        private String ticketid;
        private String batchid;

        public GetTicketsForView(String urlPath, String ticketId, String batchId, Context context){
            super(urlPath, true, false, context);
            this.urlPath = urlPath;
            this.ticketid = ticketId;
            this.batchid = batchId;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            try {
                JSONObject obj = new JSONObject(response.toString());
                Intent i = new Intent(MyTicketsActivity.this, TestScratcherActivity.class);
                i.putExtra("gameName", "Last Four");
                i.putExtra("userNumbers", "3,3,3,3");
                i.putExtra("winningNumbers", "4- 4- 4- 4");
                i.putExtra("template", 3);
                i.putExtra("background", obj.getString("background"));
                i.putExtra("directions", obj.getString("directions"));
                i.putExtra("logo", obj.getString("logo"));
                i.putExtra("draw_header", obj.getString("drawheader"));
                i.putExtra("your_header", obj.getString("yourheader"));
                i.putExtra("prize", obj.getString("prizes"));
                i.putExtra("ticketId", ticketid);
                i.putExtra("batchId", batchid);

                String[] your = new String[4];
                for(int j = 0; j < obj.getJSONArray("yournumbers").length(); j++){
                    your[j] = obj.getJSONArray("yournumbers").getString(j);
                }

                String[] winning = new String[4];
                for(int k = 0; k < obj.getJSONArray("drawnumbers").length(); k++){
                    winning[k] = obj.getJSONArray("drawnumbers").getString(k);
                }

                i.putExtra("yournumbers", your);
                i.putExtra("winningnumbers", winning);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {

        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new LinkedHashMap<String, String>();
            //param2.put("language", "en");
            param2.put("ticketid", ticketid);
            param2.put("size", "size=xxhdpi");
            if(getEpsApplication().isSpanish){
                param2.put("language", "language=es");
            }

            /*JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("username", email);
            params.put("password", password);*/

            /*JSONObject json = new JSONObject();

            response = doGet(json, param2);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }*/

    private void createTickets(ArrayList<Ticket> tickets){
        if(tickets.size() > 0) {
            noTickets.setVisibility(View.GONE);
            if (listDataHeader.size() > 0) {
                listDataHeader.clear();
            }

            /*ArrayList<Ticket> sorting = new ArrayList<Ticket>();
            for(Ticket ticket : tickets) {
                if (ticket.getStatus().equals("closed, not a winner")) {
                    sorting.add(ticket);
                    tickets.remove(ticket);
                }
            }

            if(sorting.size() > 0) {
                Collections.sort(sorting, new Comparator<Ticket>() {
                    public int compare(Ticket m1, Ticket m2) {
                        return m1.getDate().compareTo(m2.getDate());
                    }
                });

                Collections.reverse(sorting);

                tickets.addAll(sorting);
            }*/


            for (Ticket ticket : tickets) {
                listDataHeader.add(ticket.getGameName());
            }

            List<String> temp = new ArrayList<String>();
            temp.add(getResources().getString(R.string.play_tickets));

            for (String name : listDataHeader) {
                int position = listDataHeader.indexOf(name);
                listDataChild.put(listDataHeader.get(position), temp);
            }

            ExpandableListAdapter listAdapter = new ExpandableListAdapter(MyTicketsActivity.this, listDataHeader, listDataChild);
            ExpandableListView expListView = (ExpandableListView) findViewById(R.id.tickets);
            expListView.setAdapter(listAdapter);
            //expListView.notify();

            if(loading.getVisibility() == View.VISIBLE){
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                loading.startAnimation(fade);
                loading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }
        else{
            if(loading.getVisibility() == View.VISIBLE){
                Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                loading.startAnimation(fade);
                loading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            if (listDataHeader.size() > 0) {
                listDataHeader.clear();
            }
            noTickets.setVisibility(View.VISIBLE);
        }
    }

    private void reset(){
        tempTickets.clear();
        listDataHeader.clear();
        listDataChild.clear();

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(MyTicketsActivity.this, listDataHeader, listDataChild);
        ExpandableListView expListView = (ExpandableListView)findViewById(R.id.tickets);
        expListView.setAdapter(listAdapter);
    }

    private void sortAndRecalibrate(ArrayList<Ticket> list){

        Collections.reverse(list);

        for(int i=0; i < list.size(); i++) {
            tempTickets.add(list.get(i));
        }

        if (listDataHeader.size() > 0) {
            listDataHeader.clear();
        }

        for (Ticket ticket : tempTickets) {
            listDataHeader.add(ticket.getGameName());
        }

        List<String> temp = new ArrayList<String>();
        temp.add(getResources().getString(R.string.play_tickets));

        for (String name : listDataHeader) {
            int position = listDataHeader.indexOf(name);
            listDataChild.put(listDataHeader.get(position), temp);
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(MyTicketsActivity.this, listDataHeader, listDataChild);
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.tickets);
        expListView.setAdapter(listAdapter);
    }

    private void filterButton(String layout, String nowList){
        switch(layout){
            case "ready":
                if(!nowList.equals("ready")){
                    createTickets(ready);
                    currentList = "ready";

                    TextView tag = (TextView)findViewById(R.id.ready_tag);
                    tag.startAnimation(out);
                    tag.setVisibility(View.GONE);

                    all1.startAnimation(in);
                    all1.setVisibility(View.VISIBLE);

                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }

                    readyLayout.setBackgroundColor(getResources().getColor(R.color.white_pressed));
                    winningLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    //winningLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    redeemedLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    pendingLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    if (Build.VERSION.SDK_INT >= 21) {
                        pendingLayout.setElevation(30);
                        redeemedLayout.setElevation(30);
                        winningLayout.setElevation(30);
                        readyLayout.setElevation(0);
                    }
                }
                else{
                    resetAll(all1, "ready");
                }
                break;

            case "pending":
                if(!nowList.equals("pending")){
                    createTickets(pending);
                    currentList = "pending";

                    TextView tag = (TextView)findViewById(R.id.pending_tag);
                    tag.startAnimation(out);
                    tag.setVisibility(View.GONE);

                    all2.startAnimation(in);
                    all2.setVisibility(View.VISIBLE);

                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }

                    readyLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    winningLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    //winningLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    redeemedLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    pendingLayout.setBackgroundColor(getResources().getColor(R.color.white_pressed));
                    if (Build.VERSION.SDK_INT >= 21) {
                        pendingLayout.setElevation(0);
                        redeemedLayout.setElevation(30);
                        winningLayout.setElevation(30);
                        readyLayout.setElevation(30);
                    }
                }
                else{
                    resetAll(all2, "pending");
                }
                break;

            case "winning":
                if(!nowList.equals("winning")){
                    createTickets(winningTickets);
                    currentList = "winning";

                    TextView tag = (TextView)findViewById(R.id.winning_tag2);
                    tag.startAnimation(out);
                    tag.setVisibility(View.GONE);

                    all3.startAnimation(in);
                    all3.setVisibility(View.VISIBLE);

                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }

                    readyLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    winningLayout.setBackgroundColor(getResources().getColor(R.color.white_pressed));
                    //winningLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    redeemedLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    pendingLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    if (Build.VERSION.SDK_INT >= 21) {
                        pendingLayout.setElevation(30);
                        redeemedLayout.setElevation(30);
                        winningLayout.setElevation(0);
                        readyLayout.setElevation(30);
                    }
                }
                else{
                    resetAll(all3, "winning");
                }
                break;

            case "redeemed":
                if(!nowList.equals("redeemed")){
                    createTickets(redeemed);
                    currentList = "redeemed";

                    TextView tag = (TextView)findViewById(R.id.redeemed_tag);
                    tag.startAnimation(out);
                    tag.setVisibility(View.GONE);

                    all4.startAnimation(in);
                    all4.setVisibility(View.VISIBLE);

                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }

                    readyLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    winningLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    //winningLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    redeemedLayout.setBackgroundColor(getResources().getColor(R.color.white_pressed));
                    pendingLayout.setBackgroundResource(R.drawable.border_right_tickets);
                    if (Build.VERSION.SDK_INT >= 21) {
                        pendingLayout.setElevation(30);
                        redeemedLayout.setElevation(0);
                        winningLayout.setElevation(30);
                        readyLayout.setElevation(30);
                    }
                }
                else{
                    resetAll(all4, "redeemed");
                }
                break;
        }

    }

    private void resetAll(TextView all, String layout){
        currentList = "all";
        createTickets(allTickets);
        all.startAnimation(out);
        all.setVisibility(View.GONE);

        TextView tag;
        switch(layout){
            case "ready":
                tag = (TextView)findViewById(R.id.ready_tag);
                tag.startAnimation(in);
                tag.setVisibility(View.VISIBLE);
                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                break;
            case "pending":
                tag = (TextView)findViewById(R.id.pending_tag);
                tag.startAnimation(in);
                tag.setVisibility(View.VISIBLE);
                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                break;
            case "winning":
                tag = (TextView)findViewById(R.id.winning_tag2);
                tag.startAnimation(in);
                tag.setVisibility(View.VISIBLE);
                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all4.getVisibility() == View.VISIBLE){
                        all4.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.redeemed_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                break;
            case "redeemed":
                tag = (TextView)findViewById(R.id.redeemed_tag);
                tag.startAnimation(in);
                tag.setVisibility(View.VISIBLE);
                    if(all2.getVisibility() == View.VISIBLE){
                        all2.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.pending_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all3.getVisibility() == View.VISIBLE){
                        all3.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.winning_tag2);
                        pending.setVisibility(View.VISIBLE);
                    }
                    if(all1.getVisibility() == View.VISIBLE){
                        all1.setVisibility(View.GONE);

                        TextView pending = (TextView)findViewById(R.id.ready_tag);
                        pending.setVisibility(View.VISIBLE);
                    }
                break;
        }

        readyLayout.setBackgroundResource(R.drawable.border_right_tickets);
        winningLayout.setBackgroundResource(R.drawable.border_right_tickets);
        redeemedLayout.setBackgroundResource(R.drawable.border_right_tickets);
        pendingLayout.setBackgroundResource(R.drawable.border_right_tickets);
        if (Build.VERSION.SDK_INT >= 21) {
            pendingLayout.setElevation(0);
            redeemedLayout.setElevation(0);
            winningLayout.setElevation(0);
            readyLayout.setElevation(0);
        }
    }

    public final class GroupViewHolder {
        TextView mLabel;
        //TextView mContactName;
        //ImageView mContactImage;
    }
}
