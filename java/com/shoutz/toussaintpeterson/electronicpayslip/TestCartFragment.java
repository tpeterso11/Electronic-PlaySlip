package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import models.CartObject;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestCartFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private GridView grid;
    private ArrayList<Ticket> finalCart;
    private TextView noCart;
    public ArrayList<ArrayList> tempCart;
    private JSONObject response;
    private Button generate;
    private ArrayList<String> ids;
    private int total;
    private ArrayList<String> temp;
    private ArrayList<Ticket> tempTickets;
    private boolean contains;
    private ArrayList<Ticket> finalTickets;
    private TextView shopTag;
    private TextView costTag;
    private int totalCost;
    private Button map;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_cart_frag, container, false);

        grid = (GridView)rootView.findViewById(R.id.grid);
        noCart = (TextView)rootView.findViewById(R.id.no_cart);
        finalCart = getEpsApplication().masterCartObjects;
        tempCart = getEpsApplication().tempCart;
        generate = (Button)rootView.findViewById(R.id.generate);
        shopTag = (TextView)rootView.findViewById(R.id.final_shop_tag);
        costTag = (TextView)rootView.findViewById(R.id.cost_tag);
        shopTag.setTypeface(getEpsApplication().sub);
        map = (Button)rootView.findViewById(R.id.map);

        if(getEpsApplication().userLocation == null){
            map.setAlpha(Float.valueOf(".3"));
            map.setEnabled(false);
        }
        else {
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LandingActivity)getActivity()).launchMap();
                }
            });
        }

        generate.setTypeface(getEpsApplication().sub);
        costTag.setTypeface(getEpsApplication().sub);

        if(!(finalCart.size() > 0)){
            generate.setAlpha(Float.valueOf(".3"));
            generate.setEnabled(false);
        }

        pairUp();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BatchTask(getResources().getString(R.string.create_batch), finalCart, getActivity()).execute();
            }
        });

        grid.setAdapter(new GridViewAdapter(getContext()));

        if (tempTickets.size() == 0) {
            noCart.setVisibility(View.VISIBLE);
        }

        totalCost = 0;
        for(Ticket ticket : tempTickets){
            for(Game game : getEpsApplication().games){
                if(ticket.getGameId().equals(game.getGameId())){
                    int current = Integer.valueOf(game.getWager()) * ticket.getIds().size();
                    totalCost = totalCost + current;
                    break;
                }
            }
        }

        String str = "Total: $" + String.valueOf(totalCost);
        costTag.setText(str);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 6);
        title = getArguments().getString("Cart");
    }


    public static TestCartFragment newInstance() {
        TestCartFragment fragmentFirst = new TestCartFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 6);
        args.putString("someTitle", "Cart");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public class GridViewAdapter extends BaseAdapter {

        private Context mContext;

        public GridViewAdapter(Context mContext) {
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return tempTickets.size();
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

                TextView added = (TextView)gridView.findViewById(R.id.added_date);
                added.setVisibility(View.GONE);
                for(Game game : getEpsApplication().games){
                    if(tempTickets.get(position).getGameId().equals(game.getGameId())){
                        TextView gameName = (TextView)gridView.findViewById(R.id.position);
                        gameName.setText(game.getGameName());
                        gameName.setTypeface(getEpsApplication().sub);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String formatted = formatter.format(Integer.valueOf(game.getMaxWin()));

                        String prize = "Top Prize: $"+ formatted;
                        TextView prizeTag = (TextView)gridView.findViewById(R.id.numbers);
                        prizeTag.setText(prize);
                        prizeTag.setTypeface(getEpsApplication().main);
                    }
                }

                ImageView icon = (ImageView)gridView.findViewById(R.id.icon);
                for(Game game : getEpsApplication().games){
                    if(tempTickets.get(position).getGameId().equals(game.getGameId())){
                        Glide.with(getActivity())
                                .load(game.getIconUrl())
                                .crossFade(R.anim.fade_in, 800)
                                .into(icon);
                    }
                }

                TextView count = (TextView)gridView.findViewById(R.id.count);
                if(tempTickets.get(position).getIds() != null) {
                    if (tempTickets.get(position).getIds().size() > 0) {
                        count.setText(String.valueOf(tempTickets.get(position).getIds().size()));
                        count.setVisibility(View.VISIBLE);
                    }

                }

                RelativeLayout save = (RelativeLayout)gridView.findViewById(R.id.save_numbers);
                save.setVisibility(View.GONE);

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }
    }

    private void checkCart(){
        if(finalCart.size() == 0){
            noCart.setVisibility(View.VISIBLE);
        }
    }

    public class BatchTask extends AbstractWebService {
        private ArrayList<Ticket> masterArray;

        public BatchTask(String urlPath, ArrayList<Ticket> masterArray, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.masterArray = masterArray;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
            try {
                int totalInt = 0;

                for(int i = 0; i < tempTickets.size(); i++){
                    for(Game game : getEpsApplication().games){
                        if(tempTickets.get(i).getGameId().equals(game.getGameId())){
                            int current = Integer.valueOf(game.getWager()) * tempTickets.get(i).getIds().size();
                            totalInt = totalInt + current;
                            break;
                        }
                    }
                }


                JSONObject responseObj = new JSONObject(response.toString());
                Bundle bundle = new Bundle();
                bundle.putString("batchId", responseObj.getString("batchid"));
                bundle.putString("past", "cart");
                bundle.putString("total", String.valueOf(totalInt));

                ArrayList<Ticket> master = getEpsApplication().masterCartObjects;
                master.clear();
                master.addAll(tempTickets);

                ((LandingActivity)getActivity()).launchConfirm(bundle);
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            //Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            ArrayList<String> ids = new ArrayList<String>();
            for(Ticket ticket : tempTickets){
                ids.addAll(ticket.getIds());
            }

            StringBuilder builder = new StringBuilder();

            for (String i : ids) {
                if (builder.length() != 0) {
                    builder.append(",");
                }
                builder.append(i);
            }

            JSONObject params = new JSONObject();
            params.put("tickets" ,builder.toString());


            /*for(ArrayList<Ticket> array : masterArray) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject params = new JSONObject();
                    params.put("gameid", 1);
                    params.put("betamount", array.get(i).getWager());
                    params.put("numbers", array.get(i).getNumbers());

                    response = doPost(params);
                }
            }*/

            //JSONObject params = new JSONObject();
            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void pairUp(){
        ids = new ArrayList<String>();
        temp = new ArrayList<String>();
        tempTickets = new ArrayList<Ticket>();

        for(Ticket ticket : finalCart){
            if(!temp.contains(ticket.getGameId())){
                temp.add(ticket.getGameId());
            }
        }

        for(String id : temp){
            ArrayList<Ticket> current = new ArrayList<Ticket>();
            for(Ticket ticket : finalCart){
                if(ticket.getGameId().equals(id)){
                    current.add(ticket);
                }
            }

            ids = new ArrayList<String>();
            for(Ticket ticket : current){
                ids.add(ticket.getTicketId());
            }

            Ticket ticket = new Ticket();
            ticket.setGameId(id);
            ticket.setIds(ids);
            ticket.setTotal(ids.size());

            tempTickets.add(ticket);
        }
    }

}

