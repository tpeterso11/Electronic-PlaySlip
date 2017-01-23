package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestOpenTicketsFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private ArrayList<String> categories;
    private Spinner ticketChoices;
    private String[] languages = {"Filter By Status", "All Tickets", "Winning Tickets", "Ready To Play", "Redeemed", "Pending Tickets",
            "History"};
    private ArrayList<Ticket> tickets;
    private CustomExpandableListView grid;
    private JSONObject response;
    private ArrayList<Ticket> openTickets;
    private ArrayList<Ticket> allTickets;
    private ArrayList<Ticket> unviewed;
    private ArrayList<Ticket> winningTickets;
    private ArrayList<Ticket> pending;
    private ArrayList<Ticket> ready;
    private ArrayList<Ticket> redeemed;
    private ArrayList<Ticket> sorting;
    private ArrayList<Ticket> viewed;
    private TextView openTag;
    private TextView zero;
    private ArrayList<String> batchIds;

    private ArrayList<ArrayList<Ticket>> batches;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.open_tickets, container, false);

        grid = (CustomExpandableListView) rootView.findViewById(R.id.grid);
        zero = (TextView)rootView.findViewById(R.id.zero);
        zero.setTypeface(getEpsApplication().main);
        openTag = (TextView)rootView.findViewById(R.id.open_tag);
        batches = new ArrayList<ArrayList<Ticket>>();
        batchIds = new ArrayList<String>();

        ((LandingActivity)getActivity()).startLoading();

        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        /*if (hasBackKey && hasHomeKey) {
            // no navigation bar, unless it is enabled in the settings
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout
                    .LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0, getNavHeight());
            grid.setLayoutParams(layoutParams);
            // 99% sure there's a navigation bar
        }*/

        grid.setExpanded(true);

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

        new GetTicketsUserTask(getResources().getString(R.string.get_tickets_user), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
    }


    public static TestOpenTicketsFragment newInstance() {
        TestOpenTicketsFragment fragmentFirst = new TestOpenTicketsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 0);
        args.putString("someTitle", "Landing");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
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
        private ArrayList<ArrayList<Ticket>> array;
        private String list;

        public GridViewAdapter(Context mContext, String list, ArrayList<ArrayList<Ticket>> array) {
            this.mContext = mContext;
            this.list = list;
            this.array = array;
        }


        @Override
        public int getCount() {
            return batches.size();
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
                gridView = inflater.inflate(R.layout.open_tickets_row, null);

                // set value into textview
                TextView name = (TextView)gridView.findViewById(R.id.position);
                int current = position +1;
                String str = "Order #"+ current;
                name.setText(str);
                name.setTypeface(getEpsApplication().sub);

                TextView cost = (TextView)gridView.findViewById(R.id.cost);
                int totalCost = 0;
                for(Ticket ticket : array.get(position)){
                    totalCost = totalCost + Integer.valueOf(ticket.getWager());
                }
                String costString = "Total: $" + totalCost;
                cost.setText(costString);

                TextView purchase = (TextView)gridView.findViewById(R.id.added_date);
                String st = "Created: " + array.get(position).get(0).getDatePurchased().substring(0,10);
                purchase.setText(st);

                LinearLayout open = (LinearLayout)gridView.findViewById(R.id.open);
                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int totalInt = 0;

                        for(int i = 0; i < array.get(position).size(); i++){
                            for(Game game : getEpsApplication().games){
                                if(array.get(position).get(i).getGameName().equals(game.getGameName())){
                                    int current = Integer.valueOf(game.getWager());
                                    totalInt = totalInt + current;

                                    array.get(position).get(i).setGameId(game.getGameId());
                                    break;
                                }
                            }
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("batchId", array.get(position).get(0).getBatchId());
                        bundle.putString("total", String.valueOf(totalInt));
                        bundle.putString("past", "open");

                        ArrayList<Ticket> master = getEpsApplication().masterCartObjects;
                        master.clear();
                        master.addAll(array.get(position));

                        ((LandingActivity)getActivity()).launchConfirm(bundle);
                    }
                });

                /*if((array.get(position).getIsViewed()).equals("false")){
                    TextView status = (TextView)gridView.findViewById(R.id.status);
                    status.setText("Ready to View");
                    status.setTypeface(getEpsApplication().main);

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
                    //TextView purchase = (TextView)gridView.findViewById(R.id.purchase_date);
                    //String st = "Action: " + parseDateToddMMyyyy(array.get(position).getDatePurchased().substring(0,10)).replace("-", "/");
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
                    //TextView purchase = (TextView)gridView.findViewById(R.id.purchase_date);

                    if(list.equals("redeemed")){
                        view.setBackgroundResource(R.drawable.rounded_layout_purp);
                    }
                    //String st = "Action: " + array.get(position).getDatePurchased().substring(0,10);
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
                }*/

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
                    current.setDatePurchased(obj.getString("actiondate"));
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

                }

                if(openTickets.size() > 0) {
                    for (Ticket ticket : openTickets) {
                        if (batchIds.isEmpty() || !(batchIds.contains(ticket.getBatchId()))) {
                            ArrayList<Ticket> current = new ArrayList<Ticket>();
                            current.add(ticket);
                            batches.add(current);

                            batchIds.add(ticket.getBatchId());
                        } else {
                            for (int i = 0; i < batches.size(); i++) {
                                if (batches.get(i).get(0).getBatchId().equals(ticket.getBatchId())) {
                                    batches.get(i).add(ticket);
                                }
                            }
                        }
                    }
                }

                /*if(batches.size() > 0){
                    Collections.sort(viewed, new Comparator<Ticket>() {
                        public int compare(Ticket o1, Ticket o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    Collections.reverse(viewed);
                }*/

                NumberFormat f = new DecimalFormat("00");

                if(batches.size() == 0){
                    zero.setVisibility(View.VISIBLE);
                    zero.bringToFront();
                }
                else{
                    grid.setAdapter(new GridViewAdapter(getActivity(), "open", batches));
                    ((LandingActivity)getActivity()).scrollToTop();
                }

                ((LandingActivity)getActivity()).clearLoading();

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

    private int getNavHeight(){
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void setFonts(){
        //winningTag.setTypeface(getEpsApplication().sub);
        openTag.setTypeface(getEpsApplication().sub);
        //historyTag.setTypeface(getEpsApplication().sub);
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
}

