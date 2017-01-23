package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestRedeemFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private Bundle args;
    private String gameName;
    private String purchaseDate;
    private String bannerUrl;
    private String prizeAmount;
    private String batchId;
    private String ticketId;
    private TextView batch;
    private TextView gameNameField;
    private ImageView gameTile;
    private TextView date;
    private TextView view;
    private String winAmount;
    private TextView winAmountField;
    private TextView purchaseDateField;
    private String isViewed;
    private RelativeLayout viewTicket;
    private int template;
    private JSONObject response;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.redeem_layout, container, false);

        args = getArguments();

        gameName = args.getString("gameName");
        batchId = args.getString("batchId");
        isViewed = args.getString("viewed");
        ticketId = args.getString("ticketId");
        template = args.getInt("template");

        gameTile = (ImageView)rootView.findViewById(R.id.game_tile);
        batch = (TextView)rootView.findViewById(R.id.batch);
        gameNameField = (TextView)rootView.findViewById(R.id.game_name);
        date = (TextView)rootView.findViewById(R.id.date);
        view = (TextView)rootView.findViewById(R.id.view);
        winAmountField = (TextView)rootView.findViewById(R.id.prize_amount);
        viewTicket = (RelativeLayout)rootView.findViewById(R.id.view_ticket);

        purchaseDate = "Purchase Date: " + args.getString("purchaseDate").substring(0, 10);
        date.setText(purchaseDate);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedWin = formatter.format(Integer.valueOf(args.getString("winAmount")));
        winAmount = "Prize Amount: $" + formattedWin;
        winAmountField.setText(winAmount);
        viewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).launchScratch(ticketId, batchId, isViewed, template, "", args);
            }
        });

        gameNameField.setText(gameName);
        gameNameField.setTypeface(getEpsApplication().main);
        date.setTypeface(getEpsApplication().main);
        view.setTypeface(getEpsApplication().sub);

        for(Game game : getEpsApplication().games){
            if(gameName.equals(game.getGameName())){
                bannerUrl = game.getBannerUrl();
                break;
            }
        }

        if(bannerUrl != null){
            Glide.with(getActivity())
                    .load(bannerUrl)
                    .into(gameTile);
        }

        batch.setText(batchId);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 6);
        title = getArguments().getString("Cart");
    }


    public static TestRedeemFragment newInstance() {
        TestRedeemFragment fragmentFirst = new TestRedeemFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 9);
        args.putString("someTitle", "Redeem");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public void back(){
        new RedeemTask(getResources().getString(R.string.redeem_ticket_service), "123", "124T", ticketId, getActivity()).execute();
    }

    public class RedeemTask extends AbstractWebService {
        private String agent;
        private String terminal;
        private String ticketId;

        public RedeemTask(String urlPath, String agent, String terminal, String ticketId, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.agent = agent;
            this.terminal = terminal;
            this.context = context;
            this.ticketId = ticketId;
        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            ((LandingActivity)getActivity()).launchTickets();
        }

        @Override
        protected void onError(Object response) {
            Crouton.makeText(getActivity(), "Error Redeeming Tickets!", Style.ALERT).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            JSONObject params = new JSONObject();
            params.put("ticketid", ticketId);
            params.put("agentid", agent);
            params.put("terminalid", terminal);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }
}

