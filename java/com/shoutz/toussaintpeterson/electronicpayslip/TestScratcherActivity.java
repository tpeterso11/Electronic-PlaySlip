package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.Box;
import models.CardObject;
import models.CartObject;
import models.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import static java.security.AccessController.getContext;

/**
 * Created by toussaintpeterson on 6/21/16.
 */
public class TestScratcherActivity extends AbstractEPSActivity {
    //@Bind(R.id.header) ImageView header;
    //@Bind(R.id.sample_barcode) ImageView barcode;
    //@Bind(R.id.ticket_back) LinearLayout ticket_back;
    //@Bind(R.id.default_scratcher) LinearLayout ticket_front;
    //@Bind(R.id.last_four_layout) RelativeLayout template_three;
    //@Bind(R.id.template_two) RelativeLayout template_two;
    //@Bind(R.id.template_three) LinearLayout template_one;
    //@Bind(R.id.template_four) LinearLayout template_four;
    //@Bind(R.id.lineView) RecyclerView lineView;
    //@Nullable
    //@Bind(R.id.loading) RelativeLayout loading;
    private RelativeLayout loading;
    private int template;
    private ArrayList<Integer> yourNumbers;
    private ArrayList<Integer> drawNumbers;
    private ArrayList<ImageView> yourFields;
    private ArrayList<ImageView> drawFields;
    private ImageView your1;
    private ImageView your2;
    private ImageView your3;
    private ImageView your4;
    private ImageView draw1;
    private ImageView draw2;
    private ImageView draw3;
    private ImageView draw4;
    private ImageView userCard;
    boolean isBackVisible;
    private boolean isCardBack1Visible;
    private boolean isCardBack2Visible;
    private boolean isCardBack3Visible;
    private boolean isCardBack4Visible;
    private Bitmap icon;
    private ArrayList<CardObject> winningCards;
    private ArrayList<CardObject> userCards;
    private View sevenLayout;
    private ArrayList<ImageView> cards;
    private ImageView win1;
    private ImageView win1Back;
    private ImageView win2;
    private ImageView win2Back;
    private ImageView win3;
    private ImageView win3Back;
    private ImageView win4;
    private ImageView win4Back;

    private ImageView userCard1;
    private ImageView userCard2;
    private ImageView userCard3;
    private ImageView userCard4;
    private String background;
    private String ticketLogo;
    private String ticketPrize;
    private String drawHeader;
    private String yourHeader;
    private String directions;
    private String[] winning;
    private String[] your;
    private String resultImage;
    private JSONObject response;
    private ScratcherView actual;
    private String ticketViewed;
    private ImageView scratcherViewed;
    private RelativeLayout myView;
    private String scratchUrl;
    private String price;
    private String density;
    private ImageView flipBack;
    private RelativeLayout sec1;
    private RelativeLayout sec2;
    private RelativeLayout sec3;
    private RelativeLayout sec4;
    private RelativeLayout sec5;
    private RelativeLayout sec6;
    private RelativeLayout sec7;
    private RelativeLayout sec8;
    private ImageView sec1Img;
    private ImageView sec2Img;
    private ImageView sec3Img;
    private ImageView sec4Img;
    private ImageView sec5Img;
    private ImageView sec6Img;
    private ImageView sec7Img;
    private ImageView sec8Img;
    private DisplayMetrics displayMetrics;
    private ArrayList<TextView> textViews;
    private ArrayList<ArrayList<String>> box1;
    private ArrayList<ArrayList<String>> box2;
    private ArrayList<ArrayList<String>> box3;
    private ArrayList<ArrayList<String>> box4;
    private ArrayList<ArrayList<String>> box5;
    private ArrayList<ArrayList<String>> box6;
    private ArrayList<ArrayList<String>> box7;
    private ArrayList<ArrayList<String>> box8;
    private ArrayList<Box> boxes;
    private ArrayList<String> row1;
    private ArrayList<String> row2;
    private ArrayList<String> row3;
    private long duration;
    private Bundle args;
    private String ticketId;
    private String batchId;
    private String backUrl;
    private Bundle argsBundle;
    //@Bind(R.id.banners) LinearLayout banners;

    @Override
    public void onBackPressed(){
        if(ticketId.equals("demo")){
            Intent i = new Intent(TestScratcherActivity.this, LandingActivity.class);
            i.putExtra("bundle", argsBundle);
            i.putExtra("key", "game");
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
            finish();
        }
        else {
            new MarkAsViewedTask(getResources().getString(R.string.markviewed), ticketId, getIntent().getStringExtra("batchId"), getApplicationContext()).execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test_scratcher);

        argsBundle = getIntent().getBundleExtra("bundle");
        args = getIntent().getExtras();
        ticketId = args.getString("ticketId");
        template = args.getInt("template");
        ticketViewed = args.getString("viewed");
        batchId = args.getString("batchId");
        backUrl = args.getString("backUrl");


        ImageView logo;
        ScratcherViewUser actualUser;
        ScratcherViewDraw actualDraw;
        ImageView instruc;
        ImageView prize;
        ImageView yourImg;
        ImageView draw;
        ImageView back;
        FrameLayout frameLayout;
        LayoutInflater inflater;

        switch(template){
            case 4:
                setContentView(R.layout.test_scratcher);
                ButterKnife.bind(this);

                frameLayout = (FrameLayout)findViewById(R.id.template_holder);
                inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                myView = (RelativeLayout) inflater.inflate(R.layout.ticket_template_four, null);
                frameLayout.addView(myView);

                new GetTicketsForView(getResources().getString(R.string.getticketview), ticketId, batchId, getDensity(), getApplicationContext()).execute();

                /*String[] first = new String[3];
                String[] second = new String[3];
                String[] third = new String[3];

                first[0] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_3.png";

                second[0] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_3.png";
                second[1] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_3.png";

                third[0] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_6.png";
                third[1] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_6.png";
                third[2] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_6.png";

                bocCreateNumbers(first, second, third, box1);

                second = new String[2];
                third = new String[2];

                second[0] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_3.png";
                second[1] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_3.png";

                third[0] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_9.png";
                third[1] = "https://www.shadowfirearms.com/wp-content/uploads/2016/10/boc_9.png";
                bocCreateNumbers(first, second, third, box2);
                bocCreateNumbers(first, second, third, box3);
                bocCreateNumbers(first, second, third, box4);
                bocCreateNumbers(first, second, third, box5);
                bocCreateNumbers(first, second, third, box6);
                bocCreateNumbers(first, second, third, box7);
                bocCreateNumbers(first, second, third, box8);

                insertNumbers(sec1.getChildAt(1), box1);
                insertNumbers(sec2.getChildAt(1), box2);
                insertNumbers(sec3.getChildAt(1), box3);
                insertNumbers(sec4.getChildAt(1), box4);
                insertNumbers(sec5.getChildAt(1), box5);
                insertNumbers(sec6.getChildAt(1), box6);
                insertNumbers(sec7.getChildAt(1), box7);
                insertNumbers(sec8.getChildAt(1), box8);

                boxes.add(box1);
                boxes.add(box2);
                boxes.add(box3);
                boxes.add(box4);
                boxes.add(box5);
                boxes.add(box6);
                boxes.add(box7);
                boxes.add(box8);
                //row3.add(6);

                layouts.add(sec1);
                layouts.add(sec2);
                layouts.add(sec3);
                layouts.add(sec4);
                layouts.add(sec5);
                layouts.add(sec6);
                layouts.add(sec7);
                layouts.add(sec8);*/

                /*for(final RelativeLayout layout : layouts){
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for ( int i = 0; i < layout.getChildCount();  i++ ) {
                                int tes3 = layout.getChildCount();
                                View img = layout.getChildAt(0);

                                if(i == 0){
                                    final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                                    shake.setDuration(100);
                                    //duration = shake.getDuration();
                                    //Log.d("Duration", String.valueOf(duration));
                                    layout.getChildAt(0).startAnimation(shake);

                                    shake.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            for ( int i = 0; i < layout.getChildCount();  i++ ){
                                                View view = layout.getChildAt(i);
                                                if(view instanceof ImageView){
                                                    //use "chosen" image from backend, and set it universally
                                                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                                            R.drawable.boc_openbox);
                                                    ((ImageView) view).setImageBitmap(icon);
                                                }
                                                return;
                                            }
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                }

                                if(i == 1) {
                                    Timer t = new Timer();
                                    t.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            TestScratcherActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    View linear = layout.getChildAt(1);
                                                    linear.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    }, 1300);
                                }
                            }
                        }
                    });
                }*/

                //useRetrofit(getIntent().getStringExtra("ticketId"), getIntent().getStringExtra("batchId"), density);

                break;
            case 3:
                setContentView(R.layout.test_scratcher);
                ButterKnife.bind(this);

                frameLayout = (FrameLayout)findViewById(R.id.template_holder);
                inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //getDensity();
                myView = (RelativeLayout) inflater.inflate(R.layout.ticket_template_three, null);
                frameLayout.addView(myView);

                yourNumbers = new ArrayList<Integer>();
                drawNumbers = new ArrayList<Integer>();
                yourFields = new ArrayList<ImageView>();
                drawFields = new ArrayList<ImageView>();

                //useRetrofit(ticketId, batchId, getDensity());
                new GetTicketsForView(getResources().getString(R.string.getticketview), ticketId, batchId, getDensity(), getApplicationContext()).execute();

            break;

            case 2:
                setContentView(R.layout.ticket_template_two);
                ButterKnife.bind(this);

                //getDensity();

                logo = (ImageView) findViewById(R.id.two_logo);
                logo.setImageResource(R.drawable.alphabet_soup_logo);
                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout back = (LinearLayout)findViewById(R.id.ticket_back2);
                        LinearLayout front = (LinearLayout)findViewById(R.id.ticket_front2);
                        flip(front, back);
                    }
                });

                prize = (ImageView) findViewById(R.id.two_prize);
                prize.setImageResource(R.drawable.alphabet_soup_instructions);

                back = (ImageView) findViewById(R.id.two_back);
                back.setImageResource(R.drawable.alphabet_soup_bg);

                getEpsApplication().userBitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.alphabet_soup_your_letters);

                getEpsApplication().drawBitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.alphabet_soup_draw_letters);

                actualUser = (ScratcherViewUser)findViewById(R.id.scratcherView1);
                actualUser.setZOrderOnTop(true);

                actualDraw = (ScratcherViewDraw)findViewById(R.id.scratcherView2);
                actualDraw.setZOrderOnTop(true);
            break;

            case 1:
                setContentView(R.layout.test_scratcher);

                if(!ticketId.equals("demo")) {
                    getDensity();
                    new GetTicketsForView(getResources().getString(R.string.getticketview), ticketId, batchId, getDensity(), getApplicationContext()).execute();
                }

                else{
                    setupScratcher();
                }
                //createScratcher();
                //useRetrofit(getIntent().getStringExtra("ticketId"), getIntent().getStringExtra("batchId"), density);
                break;
        }
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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int id, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeResource(res, id, options);
    }

    private void setUpFields(){
        your1 = (ImageView)myView.findViewById(R.id.your_1);
        your2 = (ImageView)myView.findViewById(R.id.your_2);
        your3 = (ImageView)myView.findViewById(R.id.your_3);
        your4 = (ImageView)myView.findViewById(R.id.your_4);

        draw1 = (ImageView)myView.findViewById(R.id.draw_1);
        draw2 = (ImageView)myView.findViewById(R.id.draw_2);
        draw3 = (ImageView)myView.findViewById(R.id.draw_3);
        draw4 = (ImageView)myView.findViewById(R.id.draw_4);

        if(winning == null || winning.length == 0) {
            draw1.setVisibility(View.GONE);
            draw2.setVisibility(View.GONE);
            draw3.setVisibility(View.GONE);
            draw4.setVisibility(View.GONE);

            ImageView pending = (ImageView)myView.findViewById(R.id.pending);
            pending.setVisibility(View.VISIBLE);
        }
        else{
            ImageView pending = (ImageView)myView.findViewById(R.id.pending);
            pending.setVisibility(View.GONE);

            draw1.setVisibility(View.VISIBLE);
            draw2.setVisibility(View.VISIBLE);
            draw3.setVisibility(View.VISIBLE);
            draw4.setVisibility(View.VISIBLE);
        }

        yourFields.add(your1);
        yourFields.add(your2);
        yourFields.add(your3);
        yourFields.add(your4);

        //String[] your = getIntent().getStringArrayExtra("yournumbers");
        for(int i = 0; i < yourFields.size(); i++) {
                ImageView current = yourFields.get(i);
                Glide.with(getApplicationContext())
                        .load(your[i])
                        .crossFade(R.anim.fade_in, 1000)
                        .into(current);
        }

        drawFields.add(draw1);
        drawFields.add(draw2);
        drawFields.add(draw3);
        drawFields.add(draw4);

        //if(getIntent().getStringArrayExtra("winningnumbers") != null) {
            String[] draw = getIntent().getStringArrayExtra("winningnumbers");
            for (int i = 0; i < drawFields.size(); i++) {
                ImageView current = drawFields.get(i);
                Glide.with(getApplicationContext())
                        .load(winning[i])
                        .into(current);
            }
        //}
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        loading = (RelativeLayout)findViewById(R.id.loading);
        loading.startAnimation(out);
        loading.setVisibility(View.GONE);

        LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        in.setStartOffset(500);
        back.startAnimation(in);
        back.setVisibility(View.VISIBLE);
    }

    private void flip(LinearLayout front, LinearLayout back){
        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);

        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);

        if(!isBackVisible){
            setRightOut.setTarget(back);
            setLeftIn.setTarget(front);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = true;
        }
        else{
            setRightOut.setTarget(front);
            setLeftIn.setTarget(back);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = false;
        }
    }

    private void flipCard(ImageView front, ImageView back, Boolean visible){
        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);

        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);

        if(!visible){
            setRightOut.setTarget(back);
            setLeftIn.setTarget(front);
            setRightOut.start();
            setLeftIn.start();
            visible = true;
        }
        else{
            setRightOut.setTarget(front);
            setLeftIn.setTarget(back);
            setRightOut.start();
            setLeftIn.start();
            visible = false;
        }
    }

    private Integer[] cardImages = {
            /*R.drawable.clubs_2, R.drawable.clubs_3,
            R.drawable.clubs_4, R.drawable.clubs_5,
            R.drawable.clubs_6, R.drawable.clubs_7,
            R.drawable.clubs_8, R.drawable.clubs_9,
            R.drawable.clubs_10, R.drawable.clubs_jack,
            R.drawable.clubs_queen, R.drawable.clubs_king,
            R.drawable.two_diamond, R.drawable.three_diamond,
            R.drawable.four_diamond, R.drawable.five_diamond,
            R.drawable.six_diamond, R.drawable.seven_diamond,
            R.drawable.eight_diamond, R.drawable.nine_diamond,
            R.drawable.ten_diamond, R.drawable.jdiamond,
            R.drawable.qdiamond, R.drawable.kdiamond,
            R.drawable.two_hearts, R.drawable.three_hearts,
            R.drawable.four_hearts, R.drawable.five_hearts,
            R.drawable.six_hearts, R.drawable.seven_hearts,
            R.drawable.eight_hearts, R.drawable.nine_heart,
            R.drawable.ten_hearts, R.drawable.jhearts,
            R.drawable.qhearts, R.drawable.khearts,
            R.drawable.two_spade, R.drawable.three_spade,
            R.drawable.four_spades, R.drawable.five_spades,
            R.drawable.six_spades, R.drawable.seven_spades,
            R.drawable.eight_spades, R.drawable.nine_spades,
            R.drawable.ten_spades, R.drawable.jspades,
            R.drawable.qspades, R.drawable.kspades,
            R.drawable.aspades, R.drawable.clubs_ace,
            R.drawable.adiamond, R.drawable.ahearts*/
    };

    private String[] cardNames = {
        "2Club", "3Club", "4Club", "5Club","6Club","7Club","8Club","9Club",
            "10Club","JClub","QClub","KClub","2Diamond", "3Diamond",
            "4Diamond", "5Diamond","6Diamond","7Diamond","8Diamond","9Diamond",
            "10Diamond","JDiamond","QDiamond","KDiamond","2Heart", "3Heart",
            "4Heart", "5Heart","6Heart","7Heart","8Heart","9Heart",
            "10Heart","JHeart","QHeart","KHeart","2Spade", "3Spade",
            "4Spade", "5Spade","6Spade","7Spade","8Spade","9Spade",
            "10Spade","JSpade","QSpade","KSpade", "ASpade", "AClub", "ADiamond",
            "AHeart"
    };

    private void setUpCards(){
        CardObject win = new CardObject();
        win.setNumber("2Club");
        win.setImgView("win3");
        winningCards.add(win);

        win = new CardObject();
        win.setNumber("5Club");
        win.setImgView("win1");
        winningCards.add(win);

        win = new CardObject();
        win.setNumber("9Club");
        win.setImgView("win2");
        winningCards.add(win);

        win = new CardObject();
        win.setNumber("KClub");
        win.setImgView("win4");
        winningCards.add(win);

        CardObject user = new CardObject();
        user.setNumber("3Club");
        user.setImgView("user1");
        userCards.add(user);

        user = new CardObject();
        user.setNumber("9Club");
        user.setImgView("user2");
        userCards.add(user);

        user = new CardObject();
        user.setNumber("5Club");
        user.setImgView("user3");
        userCards.add(user);

        user = new CardObject();
        user.setNumber("KClub");
        user.setImgView("user4");
        userCards.add(user);

        cards = new ArrayList<ImageView>();
        cards.add(userCard);
        cards.add(userCard2);
        cards.add(userCard3);
        cards.add(userCard4);

        for(final CardObject card : userCards){
            int current;
            for(String name : cardNames){
                if(card.getNumber().equals(name)){
                    //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                    int position = Arrays.asList(cardNames).indexOf(name);
                    card.setPosition(position);
                }
            }
            switch(card.getImgView()){
                case "user1":
                    current = cardImages[card.getPosition()];
                    userCard.setImageResource(current);
                    userCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            seekWinner(card.getNumber());
                            changeVisibility(card.getImgView());
                        }
                    });
                    break;
                case "user2":
                    current = cardImages[card.getPosition()];
                    userCard2.setImageResource(current);
                    userCard2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            seekWinner(card.getNumber());
                            changeVisibility(card.getImgView());
                        }
                    });
                    break;
                case "user3":
                    current = cardImages[card.getPosition()];
                    userCard3.setImageResource(current);
                    userCard3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            seekWinner(card.getNumber());
                        }
                    });
                    break;
                case "user4":
                    current = cardImages[card.getPosition()];
                    userCard4.setImageResource(current);
                    userCard4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            seekWinner(card.getNumber());
                            changeVisibility(card.getImgView());
                        }
                    });
                    break;
            }
        }

        for(CardObject card : winningCards){
            int current;
            for(String name : cardNames){
                if(card.getNumber().equals(name)){
                    //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                    int position = Arrays.asList(cardNames).indexOf(name);
                    card.setPosition(position);
                }
            }
            switch(card.getImgView()){
                case "win1":
                    current = cardImages[card.getPosition()];
                    win1Back.setImageResource(current);
                    break;
                case "win2":
                    current = cardImages[card.getPosition()];
                    win2Back.setImageResource(current);
                    break;
                case "win3":
                    current = cardImages[card.getPosition()];
                    win3Back.setImageResource(current);
                    break;
                case "win4":
                    current = cardImages[card.getPosition()];
                    win4Back.setImageResource(current);
                    break;
            }
        }
    }

    private void seekWinner(String value){
            for(int i = 0; i < winningCards.size(); i++){
                CardObject currentCard = winningCards.get(i);
                if(winningCards.get(i).getNumber().equals(value)){
                    switch(currentCard.getImgView()){
                        case "win1":
                            if(!isCardBack1Visible) {
                                flipCard(win1, win1Back, isCardBack1Visible);
                                isCardBack1Visible = true;
                            }
                            break;
                        case "win2":
                            if(!isCardBack2Visible) {
                                flipCard(win2, win2Back, isCardBack2Visible);
                                isCardBack2Visible = true;
                            }
                            break;
                        case "win3":
                            if(!isCardBack3Visible) {
                                flipCard(win3, win3Back, isCardBack3Visible);
                                isCardBack3Visible = true;
                            }
                            break;
                        case "win4":
                            if(!isCardBack4Visible) {
                                flipCard(win4, win4Back, isCardBack4Visible);
                                isCardBack4Visible = true;
                            }
                            break;
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Nope", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void changeVisibility(String cardPos){
        switch(cardPos){
            case "win1": isCardBack1Visible = true;
                break;
            case "win2": isCardBack2Visible = true;
                break;
            case "win3": isCardBack3Visible = true;
                break;
            case "win4": isCardBack4Visible = true;
                break;
        }
    }

    public class LineAdapter extends RecyclerView.Adapter<LineAdapter.DataObjectHolder> {
        private ArrayList<ArrayList> mDataset;

        public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView label;
            LinearLayout numbers;

            public DataObjectHolder(View itemView) {
                super(itemView);
                label = (TextView) itemView.findViewById(R.id.line_number);
                numbers = (LinearLayout) itemView.findViewById(R.id.line_numbers);
            }

            @Override
            public void onClick(View v) {
                //myClickListener.onItemClick(getPosition(), v);
            }
        }

        public LineAdapter(ArrayList<ArrayList> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.template_three_cell, parent, false);

            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
        }

        @Override
        public void onBindViewHolder(DataObjectHolder holder, final int position) {
            String ticketPos = "Line "+ String.valueOf(position+1);
            holder.label.setText(ticketPos);

            for(int i=0; i < mDataset.get(position).size(); i++){
                TextView current = new TextView(TestScratcherActivity.this);
                current.setText(mDataset.get(position).get(i).toString());
                current.setTextColor(getResources().getColor(R.color.black));
                current.setTextSize(18);
                current.setPadding(15,15,15,15);
                holder.numbers.addView(current);
            }
        }

        public void addItem(CartObject cartObj, int index) {
            //mDataset.add(cartObj);
            //notifyItemInserted(index);
        }

        public void deleteItem(int index) {
            //if(!(totalsIndex == index)) {
            //    mDataset.remove(index);
            //    notifyItemRemoved(index);
            //}
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }

    private void dynamicImages(){
        //Toast.makeText(getApplicationContext(), background, Toast.LENGTH_LONG).show();
        ImageView back = (ImageView)myView.findViewById(R.id.dynamic_back);
        back.setVisibility(View.VISIBLE);

        if(background != null) {
            Glide.with(getApplicationContext())
                    .load(background)
                    .crossFade(R.anim.fade_in, 1000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            String exception = e.toString();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            LinearLayout front = (LinearLayout) findViewById(R.id.ticket_front3);
                            front.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(back);
        }

        /*if(ticketLogo != null) {
            ImageView logo = (ImageView) myView.findViewById(R.id.logo3);
            Glide.with(getApplicationContext())
                    .load(ticketLogo)
                    .crossFade(R.anim.fade_in, 1000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(logo);
        }

        if(directions != null) {
            ImageView instruc = (ImageView) myView.findViewById(R.id.instruc);
            Glide.with(getApplicationContext())
                    .load(directions)
                    .crossFade(R.anim.fade_in, 1000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(instruc);
        }

        if(ticketPrize != null) {
            ImageView prize = (ImageView) myView.findViewById(R.id.prize);
            Glide.with(getApplicationContext())
                    .load(ticketPrize)
                    .crossFade(R.anim.fade_in, 1000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(prize);
        }

        if(price != null) {
            ImageView priceTag = (ImageView) myView.findViewById(R.id.price);
            Glide.with(getApplicationContext())
                    .load(price)
                    .crossFade(R.anim.fade_in, 1000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(priceTag);
        }*/

        if(template == 3) {
            setUpFields();
        }

        else{
            loading = (RelativeLayout)findViewById(R.id.loading);
            if(loading.getVisibility() == View.VISIBLE){
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TestScratcherActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                                loading.startAnimation(out);
                                loading.setVisibility(View.GONE);



                                LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                in.setStartOffset(500);
                                back.startAnimation(in);
                                back.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }, 1500);
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap mBitmap;
        String url;

        public DownloadImageTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Bitmap doInBackground(String... urls) {
            mBitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            getEpsApplication().scratchImage = mBitmap;

            //Toast.makeText(getApplicationContext(), "Got it", Toast.LENGTH_SHORT).show();

            FrameLayout frameLayout = (FrameLayout)findViewById(R.id.template_holder);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = (RelativeLayout) inflater.inflate(R.layout.ticket_template_one, null);
            frameLayout.addView(myView);


            ScratcherView actual = (ScratcherView)myView.findViewById(R.id.scratcherView3);
            actual.setZOrderOnTop(true);

            if(ticketViewed.equals("true")){
                actual.setVisibility(View.GONE);
                scratcherViewed = (ImageView)myView.findViewById(R.id.scratcher_viewed);
                scratcherViewed.setVisibility(View.VISIBLE);
                scratcherViewed.setImageBitmap(getEpsApplication().bothBitmap);
            }
            dynamicImages();
        }
    }

    public class GetTicketsForView extends AbstractWebService {
        private String ticketid;
        private String batchid;
        private String mDensity;

        public GetTicketsForView(String urlPath, String ticketId, String batchId, String density, Context context){
            super(urlPath, true, false, context);
            this.urlPath = urlPath;
            this.ticketid = ticketId;
            this.batchid = batchId;
            this.mDensity = density;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            if(template == 3) {
                try {
                    JSONObject resultObj = new JSONObject(response.toString());
                    background = resultObj.getString("background");
                    ticketPrize = resultObj.getString("prizes");
                    ticketLogo = resultObj.getString("logo");
                    directions = resultObj.getString("directions");
                    price = resultObj.getString("price");

                    if (resultObj.has("yournumbers")) {
                        yourHeader = resultObj.getString("yourheader");
                        your = new String[4];
                        for (int j = 0; j < resultObj.getJSONArray("yournumbers").length(); j++) {
                            your[j] = resultObj.getJSONArray("yournumbers").getString(j);
                        }
                    }

                    if (resultObj.has("drawnumbers")) {
                        drawHeader = resultObj.getString("drawheader");
                        winning = new String[4];
                        for (int k = 0; k < resultObj.getJSONArray("drawnumbers").length(); k++) {
                            winning[k] = resultObj.getJSONArray("drawnumbers").getString(k);
                        }
                    }

                    if (resultObj.has("scratchimage") && !resultObj.get("scratchimage").equals("")) {
                        scratchUrl = resultObj.getString("scratchimage");

                        resultImage = resultObj.getString("resultimagebinary");
                        resultImage.replaceAll("\\\\", "");

                        byte[] decodedString = Base64.decode(resultImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        getEpsApplication().bothBitmap = decodedByte;

                        String background = getIntent().getStringExtra("background");

                        /*flip = (ImageView)myView.findViewById(R.id.flip);
                        flip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                flip(front, back);
                            }
                        });

                        flipBack = (ImageView)myView.findViewById(R.id.flip_back);
                        flipBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                flip(front, back);
                            }
                        });*/

                        new DownloadImageTask(scratchUrl).execute();
                    } else {
                        dynamicImages();
                    }
                } catch (JSONException ex) {
                   // Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            else if(template == 4){
                ArrayList<RelativeLayout> layouts = new ArrayList<RelativeLayout>();

                boxes = new ArrayList<Box>();
                try {
                    JSONObject resultObj = new JSONObject(response.toString());
                    JSONObject numbers = resultObj.getJSONObject("numbers");
                    background = resultObj.getString("background");
                    for(int i=0; i < numbers.length(); i++){
                        Box box = new Box();
                        if(numbers.getJSONObject("box"+i).getJSONArray("row1").length() > 0 && !(numbers.getJSONObject("box"+i).getJSONArray("row1").get(0).equals(""))) {
                            ArrayList<String> current = new ArrayList<String>();
                            for(int j=0; j < numbers.getJSONObject("box"+i).getJSONArray("row1").length(); j++){
                                current.add(numbers.getJSONObject("box"+i).getJSONArray("row1").get(j).toString());
                            }
                            box.setRow1(current);
                        }
                        else{
                            box.setRow1(null);
                        }
                        if(numbers.getJSONObject("box"+i).getJSONArray("row2").length() > 0 && !(numbers.getJSONObject("box"+i).getJSONArray("row2").get(0).equals(""))) {
                            ArrayList<String> current = new ArrayList<String>();
                            for(int j=0; j < numbers.getJSONObject("box"+i).getJSONArray("row2").length(); j++){
                                current.add(numbers.getJSONObject("box"+i).getJSONArray("row2").get(j).toString());
                            }
                            box.setRow2(current);
                        }
                        else{
                            box.setRow2(null);
                        }
                        if(numbers.getJSONObject("box"+i).getJSONArray("row3").length() > 0 && !(numbers.getJSONObject("box"+i).getJSONArray("row3").get(0).equals(""))) {
                            ArrayList<String> current = new ArrayList<String>();
                            for(int j=0; j < numbers.getJSONObject("box"+i).getJSONArray("row3").length(); j++){
                                current.add(numbers.getJSONObject("box"+i).getJSONArray("row3").get(j).toString());
                            }
                            box.setRow3(current);
                        }
                        else{
                            box.setRow3(null);
                        }
                        boxes.add(box);
                    }
                    setupBoxes();
                }
                catch(JSONException ex){

                }
            }
                else{
                    try {
                        JSONObject obj = new JSONObject(response.toString());

                        background = obj.getString("background");
                        ticketPrize = obj.getString("prizes");
                        ticketLogo = obj.getString("logo");
                        directions = obj.getString("directions");
                        price = obj.getString("price");

                        if (obj.has("yournumbers")) {
                            yourHeader = obj.getString("yourheader");
                            your = new String[4];
                            for (int j = 0; j < obj.getJSONArray("yournumbers").length(); j++) {
                                your[j] = obj.getJSONArray("yournumbers").getString(j);
                            }
                        }

                        if (obj.has("drawnumbers")) {
                            drawHeader = obj.getString("drawheader");
                            winning = new String[4];
                            for (int k = 0; k < obj.getJSONArray("drawnumbers").length(); k++) {
                                winning[k] = obj.getJSONArray("drawnumbers").getString(k);
                            }
                        }

                        if (obj.has("scratchimage") && !obj.get("scratchimage").equals("")) {
                            scratchUrl = obj.getString("scratchimage");

                            resultImage = obj.getString("resultimagebinary");
                            resultImage.replaceAll("\\\\", "");

                            byte[] decodedString = Base64.decode(resultImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            getEpsApplication().bothBitmap = decodedByte;

                            new DownloadImageTask(scratchUrl).execute();
                        } else {
                            dynamicImages();
                        }
                    } catch (JSONException ex) {
                     //   Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        }

        @Override
        protected void onError(Object response) {
          //  Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new LinkedHashMap<String, String>();
            //param2.put("language", "en");
            param2.put("ticketid", ticketid);
            param2.put("size", "size="+mDensity);
            if(getEpsApplication().isSpanish){
                param2.put("language", "language=es");
            }

            /*JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("username", email);
            params.put("password", password);*/

            JSONObject json = new JSONObject();

            response = doGet(json, param2);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }


    public class MarkAsViewedTask extends AbstractWebService {
        private String ticketid;
        private String batchid;

        public MarkAsViewedTask(String urlPath, String ticketid, String batchid, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.ticketid = ticketid;
            this.batchid = batchid;
        }

        @Override
        protected void onFinish() {

        }

        @Override
        protected void onSuccess(Object response) {
            if(getEpsApplication().userBitmap != null) {
                getEpsApplication().userBitmap.recycle();
            }
            if(getEpsApplication().bothBitmap != null){
                getEpsApplication().bothBitmap.recycle();
            }
            if(getEpsApplication().drawBitmap != null){
                getEpsApplication().drawBitmap.recycle();
            }
            if(getEpsApplication().scratchImage != null){
                getEpsApplication().scratchImage.recycle();
            }

            System.gc();

            Intent i = new Intent(TestScratcherActivity.this, LandingActivity.class);
            i.putExtra("bundle", args);
            i.putExtra("key", "tickets");
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
            finish();
        }

        @Override
        protected void onError(Object response) {

        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();
            //param2.put("ticketid", ticketid);
            //param2.put("batchid", batchid);
            JSONObject params = new JSONObject();
            params.put("ticketid", ticketid);
            params.put("batchid", batchid);
            //params.put("password", password);

            JSONObject json = new JSONObject();

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void useRetrofit(String ticketId, String batchId, String size){
        String url =  "http://demo-eps.shoutz.com/getticketforview.php";

        RetrofitInterface retrofitInterface = new RestAdapter.Builder()
                .setEndpoint(url).build().create(RetrofitInterface.class);

        retrofitInterface.getTicket(ticketId, batchId, size, new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                //Try to get response body
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String newresult = sb.toString();

                try {
                    JSONObject resultObj = new JSONObject(newresult.toString());
                    background = resultObj.getString("background");
                    ticketPrize = resultObj.getString("prizes");
                    ticketLogo = resultObj.getString("logo");
                    directions = resultObj.getString("directions");
                    price = resultObj.getString("price");

                    if (resultObj.has("yournumbers")) {
                        yourHeader = resultObj.getString("yourheader");
                        your = new String[4];
                        for (int j = 0; j < resultObj.getJSONArray("yournumbers").length(); j++) {
                            your[j] = resultObj.getJSONArray("yournumbers").getString(j);
                        }
                    }

                    if (resultObj.has("drawnumbers")) {
                        drawHeader = resultObj.getString("drawheader");
                        winning = new String[4];
                        for (int k = 0; k < resultObj.getJSONArray("drawnumbers").length(); k++) {
                            winning[k] = resultObj.getJSONArray("drawnumbers").getString(k);
                        }
                    }

                    if (resultObj.has("scratchimage") && !resultObj.get("scratchimage").equals("")) {
                        scratchUrl = resultObj.getString("scratchimage");

                        resultImage = resultObj.getString("resultimagebinary");
                        resultImage.replaceAll("\\\\", "");

                        byte[] decodedString = Base64.decode(resultImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        getEpsApplication().bothBitmap = decodedByte;

                        String background = getIntent().getStringExtra("background");

                        /*flip = (ImageView)myView.findViewById(R.id.flip);
                        flip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                flip(front, back);
                            }
                        });

                        flipBack = (ImageView)myView.findViewById(R.id.flip_back);
                        flipBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                flip(front, back);
                            }
                        });*/

                        new DownloadImageTask(scratchUrl).execute();
                    } else {
                        dynamicImages();
                    }
                }
                catch(JSONException ex){

                }
            }


            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public interface RetrofitInterface {
        // asynchronously with a callback
        @GET("")
        void getTicket(@Query("ticketid") String ticketId, @Query("batchid") String batchId, @Query("size") String size, Callback<Response> callback);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void bocCreateNumbers(String[] first, String[] second, String[] third, ArrayList<ArrayList<String>> box){
        row1 = new ArrayList<String>();
        row2 = new ArrayList<String>();
        row3 = new ArrayList<String>();

        if(first.length > 0){
            for(String string : first){
                row1.add(string);
            }
        }

        if(second.length > 0){
            for(String string : second){
                row2.add(string);
            }
        }

        if(third.length > 0){
            for(String string : third){
                row3.add(string);
            }
        }

        box.add(row1);
        box.add(row2);
        box.add(row3);
    }

    private void insertNumbers(View linear, ArrayList<ArrayList<String>> box){
        linear.setVisibility(View.INVISIBLE);

        if (box.get(0) != null && !box.get(0).isEmpty()) {
            LinearLayout LL = new LinearLayout(getApplicationContext());
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LL.setWeightSum(box.get(0).size());
            LL.setLayoutParams(LLParams);
            LL.setGravity(Gravity.CENTER);

            for (String number : box.get(0)) {
                ImageView current = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams vp =
                        new LinearLayout.LayoutParams(50, 50);
                current.setLayoutParams(vp);
                Glide.with(getApplicationContext())
                        .load(number)
                        .crossFade(R.anim.fade_in, 1000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(current);
                LL.addView(current);
            }

            ((ViewGroup) linear).addView(LL);
        }
        else{
            LinearLayout LL = new LinearLayout(getApplicationContext());
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            LL.setLayoutParams(LLParams);
            LL.setVisibility(View.INVISIBLE);
            ((ViewGroup) linear).addView(LL);
        }

        if (box.get(1) != null && !box.get(1).isEmpty()) {
            LinearLayout LL = new LinearLayout(getApplicationContext());
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LL.setWeightSum(box.get(1).size());
            LL.setLayoutParams(LLParams);
            LL.setGravity(Gravity.CENTER_HORIZONTAL);

            for (String number : box.get(1)) {
                ImageView current = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams vp =
                        new LinearLayout.LayoutParams(50, 50);
                current.setLayoutParams(vp);

                Glide.with(getApplicationContext())
                        .load(number)
                        .crossFade(R.anim.fade_in, 1000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(current);


                LL.addView(current);
            }

            ((ViewGroup) linear).addView(LL);
        }

        if (box.get(2) != null && !box.get(2).isEmpty()) {
            LinearLayout LL = new LinearLayout(getApplicationContext());
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LL.setWeightSum(box.get(2).size());
            LL.setLayoutParams(LLParams);
            LL.setGravity(Gravity.CENTER);

            for (String number : box.get(2)) {
                ImageView current = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams vp =
                        new LinearLayout.LayoutParams(50, 50);
                current.setLayoutParams(vp);
                Glide.with(getApplicationContext())
                        .load(number)
                        .crossFade(R.anim.fade_in, 1000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(current);
                LL.addView(current);
            }

            ((ViewGroup) linear).addView(LL);
        }
    }

    private String getDensity() {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    private void setupScratcher(){
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.template_holder);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView = (RelativeLayout) inflater.inflate(R.layout.ticket_template_one, null);
        frameLayout.addView(myView);

        ImageView back = (ImageView)myView.findViewById(R.id.dynamic_back);
        if(backUrl != null) {
            Uri uri;
            uri = Uri.parse(backUrl);

            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(back);
        }

        Bitmap scratch = BitmapFactory.decodeResource(getResources(),
                R.drawable.find9_numbers_preview);

        getEpsApplication().bothBitmap = scratch;

        Bitmap over = BitmapFactory.decodeResource(getResources(),
                R.drawable.find9_numbers_scratch);

        getEpsApplication().scratchImage = over;

        ScratcherView actual = (ScratcherView)myView.findViewById(R.id.scratcherView3);
        actual.setZOrderOnTop(true);
    }

    private void setupBoxes(){
        sec1 = (RelativeLayout)findViewById(R.id.sec1);
        sec2 = (RelativeLayout)findViewById(R.id.sec2);
        sec3 = (RelativeLayout)findViewById(R.id.sec3);
        sec4 = (RelativeLayout)findViewById(R.id.sec4);
        sec5 = (RelativeLayout)findViewById(R.id.sec5);
        sec6 = (RelativeLayout)findViewById(R.id.sec6);
        sec7 = (RelativeLayout)findViewById(R.id.sec7);
        sec8 = (RelativeLayout)findViewById(R.id.sec8);

        ArrayList<View> layouts = new ArrayList<View>();
        layouts.add(sec1.getChildAt(1));
        layouts.add(sec2.getChildAt(1));
        layouts.add(sec3.getChildAt(1));
        layouts.add(sec4.getChildAt(1));
        layouts.add(sec5.getChildAt(1));
        layouts.add(sec6.getChildAt(1));
        layouts.add(sec7.getChildAt(1));
        layouts.add(sec8.getChildAt(1));

        ArrayList<RelativeLayout> rLayouts = new ArrayList<RelativeLayout>();
        rLayouts.add(sec1);
        rLayouts.add(sec2);
        rLayouts.add(sec3);
        rLayouts.add(sec4);
        rLayouts.add(sec5);
        rLayouts.add(sec6);
        rLayouts.add(sec7);
        rLayouts.add(sec8);

        for(final RelativeLayout layout : rLayouts) {
            if (ticketViewed.equals("false")) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View view = layout.getChildAt(i);
                    if (view instanceof ImageView) {
                        ((ImageView) view).setVisibility(View.VISIBLE);
                    }
                }
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < layout.getChildCount(); i++) {
                            int tes3 = layout.getChildCount();
                            View img = layout.getChildAt(0);

                            if (i == 0) {
                                final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                                shake.setDuration(100);
                                //duration = shake.getDuration();
                                //Log.d("Duration", String.valueOf(duration));
                                layout.getChildAt(0).startAnimation(shake);

                                shake.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        for (int i = 0; i < layout.getChildCount(); i++) {
                                            View view = layout.getChildAt(i);
                                            if (view instanceof ImageView) {
                                                //use "chosen" image from backend, and set it universally
                                                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                                        R.drawable.boc_openbox);
                                                ((ImageView) view).setImageBitmap(icon);
                                            }
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }

                            if (i == 1) {
                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        TestScratcherActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                //Toast.makeText(getApplicationContext(), String.valueOf(layout.getChildCount()), Toast.LENGTH_SHORT).show();
                                                LinearLayout linear = (LinearLayout) layout.getChildAt(1);
                                                linear.setVisibility(View.VISIBLE);
                                                linear.bringToFront();
                                            }
                                        });
                                    }
                                }, 1300);
                            }
                        }
                    }
                });
            }
            else {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View view = layout.getChildAt(i);
                    if (view instanceof ImageView) {
                        //use "chosen" image from backend, and set it universally
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.boc_openbox);
                        ((ImageView) view).setImageBitmap(icon);
                        ((ImageView) view).setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        for(int i=0; i < boxes.size(); i++){
            LinearLayout current = (LinearLayout)layouts.get(i);
            current.setWeightSum(3);
            if (ticketViewed.equals("false")) {
                current.setVisibility(View.INVISIBLE);
            }
            else{
                current.setVisibility(View.VISIBLE);
            }
            if(boxes.get(i).getRow1() != null && boxes.get(i).getRow1().size() > 0 && !(boxes.get(i).getRow1().get(0).equals(""))) {
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        0);
                lp.weight = 1;
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setWeightSum(boxes.get(i).getRow2().size());
                for(String string : boxes.get(i).getRow2()){
                    ImageView currentImg = new ImageView(getApplicationContext());
                    currentImg.setLayoutParams(new LinearLayout.LayoutParams(80,
                            80));
                    Uri uri;
                    uri = Uri.parse(string);

                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(currentImg);

                    newLinear.addView(currentImg);
                }
                current.addView(newLinear);
            }
            else{
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        20);
                lp.weight = 1;
                newLinear.setLayoutParams(lp);
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setVisibility(View.INVISIBLE);
                current.addView(newLinear);
            }
            if(boxes.get(i).getRow2() != null && boxes.get(i).getRow2().size() > 0) {
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                       0);
                lp.weight = 1;
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setWeightSum(boxes.get(i).getRow2().size());
                for(String string : boxes.get(i).getRow2()){
                    ImageView currentImg = new ImageView(getApplicationContext());
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                    //currentImg.setLayoutParams(params);
                    currentImg.setLayoutParams(new LinearLayout.LayoutParams(80,
                            80));
                    Uri uri;
                    uri = Uri.parse(string);
                    //currentImg.setText("1");

                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(currentImg);

                    newLinear.addView(currentImg);
                    //currentImg.setText("1");

                    //newLinear.addView(currentImg);
                    /*Glide.with(getApplicationContext())
                            .load(uri)
                            .into(currentImg);
                    newLinear.addView(currentImg);*/
                }
                current.addView(newLinear);
            }
            else{
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        0);
                lp.weight = 1;
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setVisibility(View.INVISIBLE);
                current.addView(newLinear);
            }

            if(boxes.get(i).getRow2() != null && boxes.get(i).getRow3().size() > 0) {
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.weight = 1;
                newLinear.setLayoutParams(lp);
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setWeightSum(boxes.get(i).getRow3().size());
                for(String string : boxes.get(i).getRow3()){
                    ImageView currentImg = new ImageView(getApplicationContext());
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
                    //currentImg.setLayoutParams(params);
                    currentImg.setLayoutParams(new LinearLayout.LayoutParams(80,
                            80));
                    Uri uri;
                    uri = Uri.parse(string);
                    //currentImg.setText("2");

                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(currentImg);

                    newLinear.addView(currentImg);
                    //currentImg.setText("1");

                    //newLinear.addView(currentImg);
                    /*Glide.with(getApplicationContext())
                            .load(uri)
                            .into(currentImg);
                    newLinear.addView(currentImg);*/
                }
                current.addView(newLinear);
            }
            else{
                LinearLayout newLinear = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        0);
                lp.weight = 1;
                newLinear.setGravity(Gravity.CENTER);
                newLinear.setVisibility(View.INVISIBLE);
                current.addView(newLinear);
            }
        }
        dynamicImages();
    }
}
