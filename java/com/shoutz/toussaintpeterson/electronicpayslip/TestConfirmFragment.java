package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Game;
import models.Location;
import models.Ticket;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestConfirmFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private String confirmation;
    private GridView grid;
    private ArrayList<Ticket> finalCart;
    private Bundle args;
    private RelativeLayout mobile;
    private String batchId;
    private TextView batch;
    private JSONObject response;
    private ProgressBar bar;
    private RelativeLayout loading;
    private ImageView loadingLogo;
    private RelativeLayout nearest;
    private TextView present;
    private TextView totalDue;
    private TextView totalValue;
    private int totalCost;
    private TextView gameTag;
    private TextView costTag;
    private TextView quantityTag;
    private TextView totalTag;
    private TextView how;
    private TextView printText;
    private TextView eText;
    private TextView nearestText;
    private TextView saveText;
    private String currentTotal;
    private String past;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_confirm_screen, container, false);
        finalCart = getEpsApplication().masterCartObjects;
        grid = (GridView)rootView.findViewById(R.id.confirm_list);
        grid.setAdapter(new GridViewAdapter(getContext()));
        mobile = (RelativeLayout)rootView.findViewById(R.id.mobile);
        batch = (TextView)rootView.findViewById(R.id.batch);

        args = getArguments();
        loading = (RelativeLayout)rootView.findViewById(R.id.loading);
        loadingLogo = (ImageView)rootView.findViewById(R.id.loading_logo);
        nearest = (RelativeLayout)rootView.findViewById(R.id.nearest);
        present = (TextView)rootView.findViewById(R.id.present);
        totalDue = (TextView)rootView.findViewById(R.id.total_due);
        totalValue = (TextView)rootView.findViewById(R.id.total_value);
        gameTag = (TextView)rootView.findViewById(R.id.game_tag);
        totalTag = (TextView)rootView.findViewById(R.id.total_tag);
        how = (TextView)rootView.findViewById(R.id.how);
        quantityTag = (TextView)rootView.findViewById(R.id.quantity_tag);
        costTag = (TextView)rootView.findViewById(R.id.price_tag);
        saveText = (TextView)rootView.findViewById(R.id.save_text);
        printText = (TextView)rootView.findViewById(R.id.print_text);
        eText = (TextView)rootView.findViewById(R.id.e_text);
        nearestText = (TextView)rootView.findViewById(R.id.retail_text);

        gameTag.setTypeface(getEpsApplication().sub);
        totalTag.setTypeface(getEpsApplication().sub);
        gameTag.setTypeface(getEpsApplication().sub);
        quantityTag.setTypeface(getEpsApplication().sub);
        present.setTypeface(getEpsApplication().sub);
        totalDue.setTypeface(getEpsApplication().sub);
        totalValue.setTypeface(getEpsApplication().sub);
        how.setTypeface(getEpsApplication().main);
        eText.setTypeface(getEpsApplication().sub);
        printText.setTypeface(getEpsApplication().sub);
        saveText.setTypeface(getEpsApplication().sub);
        nearestText.setTypeface(getEpsApplication().sub);
        totalCost = 0;
        currentTotal = args.getString("total");
        String str = "$" + currentTotal;
        totalValue.setText(str);
        /*nearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).launchMap();
            }
        });*/
        batchId = args.getString("batchId");
        past = args.getString("past");
        batch.setText(batchId);
        bar = (ProgressBar)rootView.findViewById(R.id.progressBar1);


        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LandingActivity)getActivity()).startLoading();
                new PurchaseTask(getResources().getString(R.string.purchase_batch), "123", "124T", getActivity()).execute();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 6);
        title = getArguments().getString("Confirm");
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public static TestConfirmFragment newInstance() {
        TestConfirmFragment fragmentFirst = new TestConfirmFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 6);
        args.putString("someTitle", "Confirm");
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
            return finalCart.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(getActivity());

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.confirm_row, null);

                // set value into textview
                for(Game game : getEpsApplication().games){
                    if(finalCart.get(position).getGameId().equals(game.getGameId())){
                        TextView textView = (TextView) gridView
                                .findViewById(R.id.game_name);
                        textView.setText(game.getGameName());
                        textView.setTypeface(getEpsApplication().sub);

                        TextView cost = (TextView) gridView
                                .findViewById(R.id.price);
                        String str = "$"+ String.valueOf(game.getWager());
                        cost.setText(str);
                        cost.setTypeface(getEpsApplication().sub);

                        //TextView quantity = (TextView)gridView.findViewById(R.id.quantity);
                        if(finalCart.get(position).getIds() != null) {
                            String totalStr = String.valueOf(Integer.valueOf(game.getWager()) * finalCart.get(position).getIds().size());
                            TextView total = (TextView) gridView.findViewById(R.id.total);
                            String strT = "$" + totalStr;
                            total.setText(strT);
                            total.setTypeface(getEpsApplication().sub);
                            totalCost = totalCost + Integer.valueOf(totalStr);

                            TextView quantity = (TextView)gridView.findViewById(R.id.quantity);
                            quantity.setText(String.valueOf(finalCart.get(position).getIds().size()));
                            quantity.setTypeface(getEpsApplication().sub);
                            quantity.setVisibility(View.VISIBLE);
                        }
                        else{
                            TextView total = (TextView) gridView.findViewById(R.id.total);
                            total.setText(finalCart.get(position).getWager());

                            TextView quantity = (TextView)gridView.findViewById(R.id.quantity);
                            quantity.setText("1");
                            quantity.setTypeface(getEpsApplication().sub);
                            quantity.setVisibility(View.VISIBLE);
                        }
                    }
                }

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }
    }

    public class PurchaseTask extends AbstractWebService {
        private String agent;
        private String terminal;

        public PurchaseTask(String urlPath, String agent, String terminal, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.agent = agent;
            this.terminal = terminal;
            this.context = context;
        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            ((LandingActivity)getActivity()).launchTickets();
            //((LandingActivity)getActivity()).stopLoadingSuccess();
        }

        @Override
        protected void onError(Object response) {
            ((LandingActivity)getActivity()).stopLoadingFail();
            Crouton.makeText(getActivity(), "Error Redeeming Tickets!", Style.ALERT).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            JSONObject params = new JSONObject();
            params.put("batchid", batchId);
            params.put("agentid", agent);
            params.put("terminalid", terminal);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    public void back(){
        if(past.equals("cart")){
            ((LandingActivity)getActivity()).launchLanding();
        }
        else{
            ((LandingActivity)getActivity()).launchOpen();
        }
    }
}

