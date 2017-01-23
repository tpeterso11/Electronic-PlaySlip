package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.CartObject;
import models.Game;
import models.Ticket;
import models.User;

/**
 * Created by toussaintpeterson on 1/11/16.
 */
public class ShoppingListViewActivity extends AbstractEPSActivity {
    private int cartObjectCount;
    public ArrayList<CartObject> cartObjects;
    @Bind(R.id.tickets_list) RecyclerView ticketsList;
    @Bind(R.id.cart) FloatingActionButton cartImage;
    private ArrayList<Ticket> cartTickets;
    private ArrayList<Ticket> ticket;
    private ArrayList<String> objectNames;
    private Boolean add;
    public ArrayList<ArrayList> masterArray;
    //@Bind(R.id.checkout)
    Button checkout;
    @Bind(R.id.discard_sig) Button discard;
    @Bind(R.id.sign_layout) RelativeLayout sig;
    //@Bind(R.id.shopping_list) RelativeLayout mainView;
    //@Bind(R.id.sign_field) TouchEventView sign;
    @Bind(R.id.no_tickets) TextView noTickets;
    private Animation animOut;
    private Animation animIn;
    private Boolean finalize;
    private int cartNumber;
    /*@Bind(R.id.ticket_tags)
    LinearLayout ticketTags;
    @Bind(R.id.shopping_header)*/
    TextView shoppingHead;
    /*@Bind(R.id.game_head)
    TextView game;
    @Bind(R.id.quan_head)
    TextView cost;
    @Bind(R.id.close_list) ImageView closeList;*/
    private int finalCost;
    private int finalTotal;
    public ArrayList<ArrayList<Ticket>> editArray;
    private Boolean isConfirm;
    private int totalsIndex;
    public boolean reset;
    private boolean isFinalized;
    private boolean isOpen;
    private String confirmationCode;
    @Bind(R.id.app) RelativeLayout app;
    private String pastCat;
    private ArrayList<Game> pastGames;
    public Boolean isChanged;
    private JSONObject response;
    private ImageView signatureImage;
    //@Bind(R.id.sign_layout) RelativeLayout confirm;

    @Override
    public void onBackPressed() {
        getEpsApplication().isChanged = false;
        getEpsApplication().cartObjects = cartObjects;
        exitList();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.shopping_list);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            pastCat = getIntent().getStringExtra("category");
            pastGames = getIntent().getParcelableArrayListExtra("games");
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && pastCat != null) {
            setupEnterAnimation();
        } else {
            cartImage.setVisibility(View.GONE);
            //initViews();
        }

        /*if (!(Build.VERSION.SDK_INT >= 21)) {
            sig.setBackgroundResource(R.drawable.edittext_border_black);
            sign.setBackgroundResource(R.drawable.edittext_border);
        }*/

        //setFonts(shoppingHead, game, cost);

        isConfirm = false;
        reset = false;
        isFinalized = false;
        isOpen = false;
        confirmationCode = "1 2 3 4 5 6";
        isChanged = getEpsApplication().isChanged;

        cartNumber = getEpsApplication().getCartNumber();
        cartObjectCount = 0;
        finalCost = 0;
        finalTotal = 0;
        cartObjects = new ArrayList<CartObject>();
        objectNames = new ArrayList<String>();
        ticket = new ArrayList<Ticket>();
        //signatureImage = (ImageView)findViewById(R.id.signature_image);
        animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_bottom);
        cartTickets = ((EPSApplication) getApplicationContext()).ticketsArray;
        masterArray = ((EPSApplication) getApplicationContext()).masterArray;
        finalize = false;

        if (masterArray == null || masterArray.isEmpty() || masterArray.get(0).isEmpty()) {
            getEpsApplication().createMaster();
            String alpha = ".4";
            checkout.setAlpha(Float.valueOf(alpha));
            checkout.setEnabled(false);
        } else {
            if(isChanged) {
                cartObjects.clear();
                addArraystoCart();
            }
            else{
                cartObjects = getEpsApplication().cartObjects;
            }
        }

        if(cartObjects.size() == 0){
            noTickets.setVisibility(View.VISIBLE);
            noTickets.bringToFront();
        }

        /*closeList.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exitList();
                return false;
            }
        });*/

        discard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(sig, "translationY", 0);
                anim.start();
            }
        });

        // Setup RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ticketsList.setLayoutManager(linearLayoutManager);
        ticketsList.setHasFixedSize(true);

        // Setup Adapter
        //NewCartAdapter adapter = new NewCartAdapter(cartObjects);
        //ticketsList.setAdapter(adapter);

        // Setup ItemTouchHelper
        //ItemTouchHelper.Callback callback = new SwipeAdapter(adapter);
        //ItemTouchHelper helper = new ItemTouchHelper(callback);
        //helper.attachToRecyclerView(ticketsList);

        /*ItemTouchHelper.Callback callback = new CartTouchHelper(CartAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(ticketsList);*/

        /*checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (getEpsApplication().getUser() != null && !(checkout.getText().equals("Reserve"))) {
                    RelativeLayout sign = (RelativeLayout) findViewById(R.id.sign_layout);
                    ObjectAnimator anim = ObjectAnimator.ofFloat(sign, "translationY", -sign.getHeight());
                    anim.start();
                    //sig.setVisibility(View.VISIBLE);
                    //Intent i = new Intent(ShoppingListViewActivity.this, FinalizeActivity.class);
                    //startActivity(i);

                    isFinalized = true;
                }
                /*else if(checkout.getText().toString().equals("Reserve")){
                    if(!(masterArray.isEmpty())){
                        for(ArrayList<Ticket> array : masterArray){
                            for(int i = 0; i < array.size(); i++){
                                if(array.get(i).getUserName().equals("null")){
                                    array.get(i).setUserName(user.getEmail());
                                }
                            }
                        }
                    }
                    new PostTicketTask(getResources().getString(R.string.post_ticket_service), masterArray, getApplicationContext()).execute();
                }*/
                 //else {
                 //   alertLogin(ShoppingListViewActivity.this);
                //}
            //}
        }//);

    @Override
    protected void onResume() {
        super.onResume();

        if(masterArray != null && !(masterArray.isEmpty())) {
            TextView quantity = (TextView) findViewById(R.id.quantity);
            quantity.setVisibility(View.VISIBLE);
            quantity.setText(factorTotals(masterArray));
            TextView costText = (TextView) findViewById(R.id.cost);
            costText.setVisibility(View.VISIBLE);
            costText.setText(factorCost(masterArray));
        }
        else{
            TextView quantity = (TextView) findViewById(R.id.quantity);
            quantity.setVisibility(View.GONE);
            TextView costText = (TextView) findViewById(R.id.cost);
            costText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //deleteCache(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* ShoppingList Adapter Code */

    /*public class NewCartAdapter extends RecyclerView.Adapter<NewCartAdapter.DataObjectHolder> {
        private ArrayList<CartObject> mDataset;

        public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView label;
            TextView cost;
            TextView quantity;
            TextView odds;
            TextView prize;
            TextView freq;
            ImageView edit;
            ImageView background;

            public DataObjectHolder(View itemView) {
                super(itemView);
                label = (TextView) itemView.findViewById(R.id.row_game_name);
                cost = (TextView) itemView.findViewById(R.id.row_game_cost);
                quantity = (TextView) itemView.findViewById(R.id.row_game_quant);
                edit = (ImageView) itemView.findViewById(R.id.edit_ticket);
                //background = (ImageView) itemView.findViewById(R.id.background);
                odds = (TextView) itemView.findViewById(R.id.row_game_odds);
                prize = (TextView) itemView.findViewById(R.id.row_game_prize);
                freq = (TextView) itemView.findViewById(R.id.row_game_freq);
                //itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //myClickListener.onItemClick(getPosition(), v);
            }
        }

        public NewCartAdapter(ArrayList<CartObject> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ticket_row, parent, false);

            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
        }

        @Override
        public void onBindViewHolder(DataObjectHolder holder, final int position) {
            holder.label.setText(mDataset.get(position).getGameName());
            holder.cost.setText("$"+ mDataset.get(position).getCost());
            holder.quantity.setText(mDataset.get(position).getQuantity());

            if (mDataset.get(position).getGameName().equals("Totals") || isConfirm) {
                holder.edit.setVisibility(View.INVISIBLE);
                holder.freq.setVisibility(View.INVISIBLE);
                holder.odds.setVisibility(View.INVISIBLE);
                holder.prize.setVisibility(View.INVISIBLE);
                //holder.background.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, 150);
                holder.itemView.setLayoutParams(params);
                //    cost.setText(String.valueOf(finalCost));
            }

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editArray.add(mDataset.get(position).getGames());
                    Intent i = new Intent(ShoppingListViewActivity.this, EditTicketsActivity.class);
                    i.putExtra("gameName", mDataset.get(position).getGameName());
                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                }
            });



            if(mDataset.get(position).getGameName().equals("Totals")){
                totalsIndex = position;
            }
            reset = false;
        }

        public void addItem(CartObject cartObj, int index) {
            mDataset.add(cartObj);
            notifyItemInserted(index);
        }

        public void deleteItem(int index) {
            if(!(totalsIndex == index)) {
                mDataset.remove(index);
                notifyItemRemoved(index);
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        /*public interface MyClickListener {
            public void onItemClick(int position, View v);
        }*/
    //}

    /*public class SwipeAdapter extends ItemTouchHelper.SimpleCallback {
        private ShoppingListViewActivity.NewCartAdapter adapter;
        public boolean isTotal;

        public SwipeAdapter(ShoppingListViewActivity.NewCartAdapter adapter){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            return true;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //if (viewHolder.getPosition() == totalsIndex) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.RIGHT){
                int index = cartObjects.indexOf(cartObjects.get(viewHolder.getAdapterPosition()));
                adapter.deleteItem(viewHolder.getAdapterPosition());
                masterArray.remove(index);
                reset = true;
                addArraystoCart();
                ticketsList.setAdapter(adapter);
            }
            else {

            }
        }
    }*/

    private void addArraystoCart(){
        int totals = 0;
        int cost = 0;

        //SEE IF THERE'S A CLEANER WAY TO DO THIS
        //if(cartObjects.isEmpty()) {
            for (ArrayList<Ticket> array : masterArray) {
                CartObject object = new CartObject();
                object.setGameName(array.get(0).getGameName());
                object.setQuantity(String.valueOf(array.size()));
                object.setCost(array.get(0).getWager());
                object.setGames(array);
                cartObjects.add(object);
                isChanged = false;

                //totals = totals + array.size();
                //cost = totals * Integer.valueOf(array.get(0).getWager());
        //    }
        }
        /*else{
            ListIterator<CartObject> iter = cartObjects.listIterator();
            while(iter.hasNext()){
                CartObject temp = iter.next();
                if()
            }
        }*/

        if(reset) {
            if(cartObjects.size() == 1){
                cartObjects.clear();
                //TextView noTickets = (TextView)findViewById(R.id.no_tickets);
                //noTickets.setVisibility(View.VISIBLE);
                String alpha = ".4";
                checkout.setAlpha(Float.valueOf(alpha));
                checkout.setEnabled(false);
            }
            else {
                cartObjects.clear();
                for(ArrayList<Ticket> array : masterArray){
                    CartObject object = new CartObject();
                    object.setGameName(array.get(0).getGameName());
                    object.setQuantity(String.valueOf(array.size()));
                    object.setCost(array.get(0).getWager());
                    object.setGames(array);
                    cartObjects.add(object);

                    //totals = totals + array.size();
                    //cost = totals * Integer.valueOf(array.get(0).getWager());
                }
            }
        }

        if(!(masterArray.isEmpty())) {
            TextView quantity = (TextView) findViewById(R.id.quantity);
            quantity.setVisibility(View.VISIBLE);
            quantity.setText(factorTotals(masterArray));
            TextView costText = (TextView) findViewById(R.id.cost);
            costText.setVisibility(View.VISIBLE);
            costText.setText(factorCost(masterArray));
        }
        else{
            TextView quantity = (TextView) findViewById(R.id.quantity);
            quantity.setVisibility(View.GONE);
            TextView costText = (TextView) findViewById(R.id.cost);
            costText.setVisibility(View.GONE);
        }
        /*else {
            CartObject object = new CartObject();
            object.setGameName("Totals");
            object.setQuantity(factorTotals(masterArray));
            object.setCost(factorCost(masterArray));
            cartObjects.add(object);
        }*/
    }

    private String factorTotals(ArrayList<ArrayList> list){
        int total = 0;

        for(ArrayList<Ticket> array : masterArray){
            for(Ticket ticket : array) {
                total++;
            }
        }
        return String.valueOf(total);
    }

    private String factorCost(ArrayList<ArrayList> list){
        int cost = 0;

        for(ArrayList<Ticket> array : masterArray){
            for(Ticket ticket : array) {
                int temp = Integer.valueOf(ticket.getWager());
                cost = cost + temp;
            }
        }
        return String.valueOf("$"+cost);
    }

    private void setFonts(TextView shoppingTitle, TextView gameTitle, TextView costTitle){
        shoppingTitle.setTypeface(main);
        gameTitle.setTypeface(sub);
        costTitle.setTypeface(sub);
        //questions.setTypeface(sub);
    }

    /* Transition Animation Code */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        transition.setDuration(300);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealShow(app);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        cartImage.setVisibility(View.VISIBLE);
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        transition.setDuration(300);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                //animateRevealShow(app);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }


    private void animateRevealShow(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        GUIUtils.animateRevealShow(this, viewRoot, cartImage.getWidth() / 2, R.color.white,
                cx, cy, new OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        cartImage.setVisibility(View.GONE);
                        //initViews();
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(final Context ctx, final View view, final int startRadius,
                                         final @ColorRes int color, int x, int y, final OnRevealAnimationListener listener) {
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, finalRadius);
        anim.setDuration(300);
        anim.setStartDelay(80);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.app_green));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
                if (listener != null) {
                    listener.onRevealShow();
                }
            }
        });
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealHide(final Context ctx, final View view, final @ColorRes int color,
                                         final int finalRadius, final OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        int startRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.setInterpolator(new FastOutLinearInInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.app_green));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onRevealHide();
                }
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(300);
        anim.start();
    }

    /*private void initViews() {
        Animation anim = AnimationUtils.loadAnimation(ShoppingListViewActivity.this, R.anim.abc_fade_in);
        RelativeLayout header = (RelativeLayout)findViewById(R.id.list_header);
        RelativeLayout list = (RelativeLayout)findViewById(R.id.shopping_list);
        list.startAnimation(anim);
        header.startAnimation(anim);
        list.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        //app.setVisibility(View.VISIBLE);
    }*/

    private void exitList(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ShoppingListViewActivity.this, cartImage, "reveal");
            if((pastCat != null) && !(pastCat.equals("landing"))){
            //    setupExitAnimation();
                Intent intent = new Intent(ShoppingListViewActivity.this, CategoryViewActivity.class);
                intent.putExtra("categoryName", pastCat);
                intent.putExtra("games", pastGames);
                startActivity(intent);
                overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
                finish();
            }
            else {
                //Intent intent = new Intent(ShoppingListViewActivity.this, TestLandingActivity.class);
                Intent intent = new Intent(ShoppingListViewActivity.this, LandingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
                finish();
            }
        }
        else {
            if(pastCat != null && !(pastCat.equals("landing"))) {
                Intent intent = new Intent(ShoppingListViewActivity.this, CategoryViewActivity.class);
                intent.putExtra("categoryName", pastCat);
                intent.putExtra("games", pastGames);
                startActivity(intent);
                finish();
            }
            else{
                Intent i = new Intent(ShoppingListViewActivity.this, LandingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
                finish();
            }
        }
    }

    public class PostTicketTask extends AbstractWebService {
        private ArrayList<ArrayList> masterArray;

        public PostTicketTask(String urlPath, ArrayList<ArrayList> masterArray, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.masterArray = masterArray;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            signatureImage.setDrawingCacheEnabled(true);
            signatureImage.buildDrawingCache();
            Bitmap b = signatureImage.getDrawingCache();

            try {
                JSONObject answer = new JSONObject(response.toString());
                String confirmation = answer.getString("ticketid");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] byteArray = stream.toByteArray();

                Intent i = new Intent(ShoppingListViewActivity.this, FinalizeActivity.class);
                i.putExtra("signature", byteArray);
                i.putExtra("confirmation", confirmation);
                startActivity(i);
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(ShoppingListViewActivity.this, "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            for(ArrayList<Ticket> array : masterArray) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject params = new JSONObject();
                    params.put("gameid", 1);
                    params.put("betamount", array.get(i).getWager());
                    params.put("numbers", array.get(i).getNumbers());

                    response = doPost(params);
                }
            }

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    public class BatchTask extends AbstractWebService {
        private ArrayList<ArrayList> masterArray;

        public BatchTask(String urlPath, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            signatureImage.setDrawingCacheEnabled(true);
            signatureImage.buildDrawingCache();
            Bitmap b = signatureImage.getDrawingCache();

            try {
                JSONObject answer = new JSONObject(response.toString());
                String confirmation = answer.getString("ticketid");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] byteArray = stream.toByteArray();

                Intent i = new Intent(ShoppingListViewActivity.this, FinalizeActivity.class);
                i.putExtra("signature", byteArray);
                i.putExtra("confirmation", confirmation);
                startActivity(i);
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(ShoppingListViewActivity.this, "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            /*for(ArrayList<Ticket> array : masterArray) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject params = new JSONObject();
                    params.put("gameid", 1);
                    params.put("betamount", array.get(i).getWager());
                    params.put("numbers", array.get(i).getNumbers());

                    response = doPost(params);
                }
            }*/

            JSONObject params = new JSONObject();
            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }
}
