package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.CartObject;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestTempCartFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private CustomExpandableListView grid;
    private ArrayList<ArrayList> masterArray;
    public Boolean isChanged;
    public ArrayList<CartObject> cartObjects;
    public boolean reset;
    private TextView noCart;
    public ArrayList<ArrayList> tempCart;
    public ArrayList<Ticket> masterCart;
    private RelativeLayout purchase;
    private JSONObject response;
    private ArrayList<Ticket> tickets;
    private ArrayList<String> temp;
    private ArrayList<Ticket> tempTickets;
    private RelativeLayout retailer;
    private ArrayList<String> deleted;
    private TextView shoppingTag;
    private TextView instructions;
    private TextView nearestButton;
    private TextView purchaseButton;
    private GridViewAdapter gridViewAdapter;
    private HashMap<String, String> savedNumbers;
    private boolean isDelete;
    private boolean isOpen;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temp_cart, container, false);

        grid = (CustomExpandableListView) rootView.findViewById(R.id.grid);
        masterArray = getEpsApplication().masterArray;
        noCart = (TextView)rootView.findViewById(R.id.no_cart);
        purchase = (RelativeLayout)rootView.findViewById(R.id.purchase);
        retailer = (RelativeLayout)rootView.findViewById(R.id.nearest);
        //nearestButton = (TextView)rootView.findViewById(R.id.nearest_button);
        instructions = (TextView)rootView.findViewById(R.id.instructions);
        //purchaseButton = (TextView)rootView.findViewById(R.id.purchase_button);

        //nearestButton.setTypeface(getEpsApplication().sub);
        instructions.setTypeface(getEpsApplication().main);
        //purchaseButton.setTypeface(getEpsApplication().sub);
        noCart.setTypeface(getEpsApplication().sub);
        isOpen = false;
        gridViewAdapter = new GridViewAdapter(getActivity());

        //isDelete = false;

        /*if(!((LandingActivity)getActivity()).isLoading()){
            ((LandingActivity)getActivity()).startLoading();
        }*/

        /*if(getEpsApplication().userLocation == null){
            retailer.setAlpha(Float.valueOf(".3"));
            retailer.setEnabled(false);
        }
        else {
            retailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LandingActivity) getActivity()).launchMap();
                }
            });
        }*/

        cartObjects = new ArrayList<CartObject>();
        tempCart = getEpsApplication().tempCart;
        reset = false;
        grid.setExpanded(true);
        temp = new ArrayList<String>();
        tempTickets = new ArrayList<Ticket>();
        deleted = new ArrayList<String>();
        shoppingTag = (TextView)rootView.findViewById(R.id.shopping_tag);
        shoppingTag.setTypeface(getEpsApplication().sub);

        //((LandingActivity)getActivity()).startLoading();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ((LandingActivity)getActivity()).showTicketFilters();
                    }
                });
            }
        }, 500);

        Timer t2 = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                new GetHeldTicketsTask(getResources().getString(R.string.held_tickets), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
            }
        }, 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 6);
        title = getArguments().getString("Cart");
    }


    public static TestTempCartFragment newInstance() {
        TestTempCartFragment fragmentFirst = new TestTempCartFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 6);
        args.putString("someTitle", "Cart");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onResume(){
        super.onResume();

        ((LandingActivity) getActivity()).switchDelete();

    }

    public class GridViewAdapter extends BaseAdapter {

        private Context mContext;

        public GridViewAdapter(Context mContext) {
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
           return tickets.size();
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

            if (convertView == null) {

                gridView = new View(getActivity());

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.temp_cart_row, null);

                TextView name = (TextView)gridView.findViewById(R.id.position);
                name.setText(tickets.get(position).getGameName());
                name.setTypeface(getEpsApplication().sub);

                ImageView icon = (ImageView)gridView.findViewById(R.id.icon);
                for(Game game : getEpsApplication().games){
                    if(game.getGameName().equals(tickets.get(position).getGameName())){
                        Glide.with(getActivity())
                                .load(game.getIconUrl())
                                .crossFade(R.anim.fade_in, 800)
                                .into(icon);
                    }
                }

                RelativeLayout save = (RelativeLayout) gridView.findViewById(R.id.save_numbers);
                save.setVisibility(View.GONE);
                /*if(!savedNumbers.containsValue(tickets.get(position).getUserNumbers()) && !(tickets.get(position).getUserNumbers().equals(""))) {
                    RelativeLayout save = (RelativeLayout) gridView.findViewById(R.id.save_numbers);
                    save.setEnabled(true);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveNumbers(tickets.get(position).getGameName(), tickets.get(position).getUserNumbers(), "enable");
                        }
                    });
                }
                else if(tickets.get(position).getUserNumbers().equals("")){
                    RelativeLayout save = (RelativeLayout) gridView.findViewById(R.id.save_numbers);
                    save.setVisibility(View.GONE);
                }
                else{
                    RelativeLayout save = (RelativeLayout) gridView.findViewById(R.id.save_numbers);
                    save.setAlpha(Float.valueOf(".4"));
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveNumbers(tickets.get(position).getGameName(), tickets.get(position).getUserNumbers(), "disable");
                        }
                    });
                }*/

                String dateStr = parseDateToddMMyyyy(tickets.get(position).getDateCreated().substring(0,10)).replaceAll("-", "/");
                String str = "Added: " + dateStr;

                TextView date = (TextView)gridView.findViewById(R.id.added_date);
                date.setText(str);
                date.setTypeface(getEpsApplication().main);

                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String formatted = formatter.format(Integer.valueOf(tickets.get(position).getWinAmount()));

                String prize = "Top Prize: $"+ formatted;

                String numbers = "Top Prize: $" + tickets.get(position).getWinAmount();
                TextView numbersTag = (TextView)gridView.findViewById(R.id.numbers);

                if(numbers.equals("")){
                    numbersTag.setVisibility(View.GONE);
                }
                else{
                    numbersTag.setText(prize);
                    numbersTag.setTypeface(getEpsApplication().main);
                }

                final RelativeLayout check = (RelativeLayout) gridView.findViewById(R.id.check);
                final TextView checkText = (TextView) gridView.findViewById(R.id.check_text);
                LinearLayout front = (LinearLayout)gridView.findViewById(R.id.front);
                front.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //RelativeLayout check = (RelativeLayout) gridView.findViewById(R.id.check);
                        if (check.getVisibility() == View.GONE) {
                            Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                            fade.setDuration(300);
                            fade.setStartOffset(200);
                            check.startAnimation(fade);
                            check.setVisibility(View.VISIBLE);

                            fade.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    //TextView checkText = (TextView) gridView.findViewById(R.id.check_text);
                                    checkText.setTypeface(getEpsApplication().sub);
                                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                                    checkText.startAnimation(in);
                                    checkText.setVisibility(View.VISIBLE);

                                    temp.add(tickets.get(position).getTicketId());

                                    if(temp.size() == 1){
                                        isDelete = true;
                                        ((LandingActivity)getActivity()).switchNearest(temp);
                                    }

                                    if (temp.size() >= 1) {
                                        for (String id : temp) {
                                            deleted.add(id);
                                        }
                                        //purchaseButton.setText("Add All");
                                    }

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                             else {

                                //TextView checkText = (TextView) gridView.findViewById(R.id.check_text);
                                Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
                                checkText.startAnimation(out);
                                checkText.setVisibility(View.GONE);

                                out.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        //RelativeLayout check = (RelativeLayout) gridView.findViewById(R.id.check);
                                        Animation fade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                                        fade.setDuration(400);
                                        fade.setStartOffset(500);
                                        check.setVisibility(View.GONE);

                                        Iterator<String> iter = deleted.iterator();
                                        while (iter.hasNext()) {
                                            if (iter.next().equals(tickets.get(position).getTicketId())) {
                                                iter.remove();
                                            }
                                        }

                                        if (deleted.size() == 0) {
                                            ((LandingActivity)getActivity()).switchNearest(deleted);
                                            //purchaseButton.setText("Add to Cart");
                                        }
                                        //Toast.makeText(getActivity(), String.valueOf(temp.size()), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                    }
                });

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }
    }

    private void checkCart(){
        if(tickets.size() == 0){
            noCart.setVisibility(View.VISIBLE);
        }
    }

    public void back(){
        ((LandingActivity)getActivity()).launchLanding();
    }

    public class GetHeldTicketsTask extends AbstractWebService {
        private String authtoken;

        public GetHeldTicketsTask(String urlPath, String authtoken, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.authtoken = authtoken;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            if(((LandingActivity)getActivity()).isLoading() && !isOpen){
                ((LandingActivity)getActivity()).stopLoadingSuccess();
            }
            else if(((LandingActivity)getActivity()).isLoading() && isOpen){
                ((LandingActivity)getActivity()).clearLoading();
            }

            tickets = new ArrayList<Ticket>();
            savedNumbers = new HashMap<String, String>();

            try{
                JSONObject responseJSON = new JSONObject(response.toString());
                for(int i=0; i < responseJSON.getJSONArray("tickets").length(); i++){
                    JSONArray current = responseJSON.getJSONArray("tickets");
                    Ticket ticket = new Ticket();
                    ticket.setGameId(current.getJSONObject(i).getString("gameid"));
                    ticket.setTicketId(current.getJSONObject(i).getString("ticketid"));
                    ticket.setGameName(current.getJSONObject(i).getString("gamename"));
                    ticket.setWinAmount(current.getJSONObject(i).getString("topprize"));
                    ticket.setDateCreated(current.getJSONObject(i).getString("createDate"));
                    ticket.setUserNumbers(current.getJSONObject(i).getString("numbers"));

                    StringTokenizer tokenizer = new StringTokenizer(current.getJSONObject(i).getString("numbers"), "-");
                    ArrayList<String> numbers = new ArrayList<String>();
                    while(tokenizer.hasMoreTokens()){
                        numbers.add(tokenizer.nextToken());
                    }
                    ticket.setNumbers(numbers);
                    ticket.setWager(current.getJSONObject(i).getString("betamount"));

                    tickets.add(ticket);

                    Map<String,?> keys = prefs.getAll();

                    for(Map.Entry<String,?> entry : keys.entrySet()){
                        String currentKey = entry.getKey();
                        if(ticket.getGameName().equals(currentKey)){
                            savedNumbers.put(currentKey, entry.getValue().toString());
                        }
                    }
                }
            }
            catch(JSONException ex){

            }

            if(tickets.size() > 0) {
                Collections.sort(tickets, new Comparator<Ticket>() {
                    public int compare(Ticket m1, Ticket m2) {
                        return m1.getDateCreated().compareTo(m2.getDateCreated());
                    }
                });

                Collections.reverse(tickets);
            }

            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
            grid.setLayoutAnimation(controller);
            grid.setAdapter(gridViewAdapter);
            grid.setPadding(0, 0, 0, 100);
            tempTickets.addAll(tickets);

            boolean hasMenuKey = ViewConfiguration.get(getActivity()).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(!hasMenuKey && !hasBackKey) {

                RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)grid.getLayoutParams();
                relativeParams.setMargins(0, 0, 0, navigationHeight());  // left, top, right, bottom
                grid.setLayoutParams(relativeParams);
            }


            checkCart();

            //((LandingActivity)getActivity()).stopLoadingFail();
        }

        @Override
        protected void onError(Object response) {
            if(((LandingActivity)getActivity()).isLoading() && !isOpen){
                ((LandingActivity)getActivity()).stopLoadingSuccess();
            }
            else if(((LandingActivity)getActivity()).isLoading() && isOpen){
                ((LandingActivity)getActivity()).clearLoading();
            }
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

    public class DeleteHeldTicketsTask extends AbstractWebService {
        private ArrayList<String> ids;
        private String where;

        public DeleteHeldTicketsTask(String urlPath, ArrayList<String> ids, String where, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.ids = ids;
            this.where = where;
        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            /*Crouton.makeText(getActivity(), "Ticket(s) Successfully Removed", Style.CONFIRM).setLifecycleCallback(new LifecycleCallback() {
                @Override
                public void onDisplayed() {
                }

                @Override
                public void onRemoved() {
                    switch(where){
                        case "back":
                            ((LandingActivity)getActivity()).launchLanding();
                            break;
                        case "final":
                            ((LandingActivity)getActivity()).launchCartLanding();
                            break;
                    }
                }
            });*/
            //tickets.clear();
            //Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
            switch(where){
                case "back":
                    ((LandingActivity) getActivity()).hideTicketFilters();
                    ((LandingActivity)getActivity()).launchLanding();
                    break;
                case "final":
                    ((LandingActivity) getActivity()).hideTicketFilters();
                    ((LandingActivity)getActivity()).launchCartLanding();
                    break;
                case "stay":
                    ((LandingActivity)getActivity()).startLoading();
                    isOpen = true;
                    tickets.clear();
                    temp = new ArrayList<String>();
                    new GetHeldTicketsTask(getResources().getString(R.string.held_tickets), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();

                    /*for(String id : deleted){
                        for(int i=0; i < tickets.size(); i++){
                            if(id.equals(tickets.get(i).getTicketId())){
                                tickets.remove(tickets.get(i));
                            }
                        }
                    }

                    //gridViewAdapter.notifyDataSetChanged();
                    Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                    GridLayoutAnimationController controller = new GridLayoutAnimationController(in, .2f, .2f);
                    grid.setLayoutAnimation(controller);
                    grid.setAdapter(gridViewAdapter);
                    grid.setExpanded(true);
                    tempTickets.addAll(tickets);
                    /*purchaseButton.setEnabled(true);

                    Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom_fast);
                    retailer.startAnimation(out);
                    retailer.setVisibility(View.INVISIBLE);

                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            retailer.setBackgroundResource(R.drawable.rounded_layout_gold);
                            nearestButton.setText("Nearest Retailer");

                            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
                            retailer.startAnimation(in);
                            retailer.setVisibility(View.VISIBLE);

                            if(getEpsApplication().userLocation == null){
                                retailer.setAlpha(Float.valueOf(".3"));
                                retailer.setEnabled(false);
                            }
                            else {
                                retailer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ((LandingActivity) getActivity()).launchMap();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    purchaseButton.setText("Add to Cart");*/

                    //if(tickets.size() == 0){
                    //    checkCart();
                   // }

                    //deleted.clear();
                    //adapter.notifyDataSetChanged();
                    //new GetHeldTicketsTask(getResources().getString(R.string.held_tickets), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
                    break;
            }
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            StringBuilder str = new StringBuilder();
            for(String string : ids){
                str.append(string);
                str.append(",");
            }
            str.setLength(str.length() - 1);

            params.put("ticketids", str.toString());

            response = doPost(params);


            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private int navigationHeight(){
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
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

    public void deleteTickets(){
       // new TestTempCartFragment.DeleteHeldTicketsTask(getResources().getString(R.string.delete_ticket), deleted, "stay", getActivity()).execute();
    }

    public void handleClick(String type){
        switch(type){
            case "delete":
                new DeleteHeldTicketsTask(getResources().getString(R.string.delete_ticket), deleted, "stay", getActivity()).execute();
                break;
            case "add":
                if(temp.size() == 0 && tickets.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Add All Tickets");
                    builder.setMessage("Add All Tickets to Cart?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            if (tickets.size() > 0) {
                                if (masterCart == null) {
                                    getEpsApplication().masterCartObjects = new ArrayList<Ticket>();
                                    masterCart = getEpsApplication().masterCartObjects;
                                }

                                for (int i = 0; i < tickets.size(); i++) {
                                    masterCart.add(tickets.get(i));
                                }

                                ((LandingActivity) getActivity()).hideTicketFilters();
                                ((LandingActivity) getActivity()).launchCartLanding();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    if (temp.size() > 0) {
                        if (masterCart == null) {
                            getEpsApplication().masterCartObjects = new ArrayList<Ticket>();
                            masterCart = getEpsApplication().masterCartObjects;
                        }

                        for (int i = 0; i < temp.size(); i++) {
                            for (Ticket ticket : tempTickets) {
                                if (ticket.getTicketId().equals(temp.get(i))) {
                                    masterCart.add(ticket);
                                    break;
                                }
                            }
                        }
                        ((LandingActivity) getActivity()).hideTicketFilters();
                        ((LandingActivity) getActivity()).launchCartLanding();
                    } else {
                        Crouton.makeText(getActivity(), "No Tickets Selected", Style.ALERT).show();
                    }
                    break;
                }
        }
    }

    public void loadTickets(){
        new GetHeldTicketsTask(getResources().getString(R.string.held_tickets), getEpsApplication().getUser().getAuthToken(), getActivity()).execute();
    }
}

