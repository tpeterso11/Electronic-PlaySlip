package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import models.Location;

/**
 * Created by toussaintpeterson on 11/15/16.
 */

public class TestScratcherFragment extends AbstractEPSFragment {
    private String title;
    private int page;
    private int template;
    private RelativeLayout myView;
    private FrameLayout frameLayout;
    private LayoutInflater lInflater;
    private String ticketId;
    private JSONObject response;
    private String background;
    private String ticketLogo;
    private String ticketPrize;
    private String directions;
    private String resultImage;
    private String ticketViewed;
    private String price;
    private String[] winning;
    private String[] your;
    private String drawHeader;
    private String yourHeader;
    private String scratchUrl;
    private ImageView scratcherViewed;

    //private ImageView shopIcon;
    //@BindView(R.id.shop_layout) RelativeLayout shopLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_ticket_template, container, false);

        Bundle args = getArguments();
        template = args.getInt("template");
        ticketId = args.getString("ticketId");

        switch(template){
            case 1:
                frameLayout = (FrameLayout)rootView.findViewById(R.id.template_holder);

                lInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = (RelativeLayout) lInflater.inflate(R.layout.ticket_template_one, null);
                frameLayout.addView(myView);

                if(!ticketId.equals("demo")){
                    getDensity();
                    new GetTicketsForView(getResources().getString(R.string.getticketview), args.getString("ticketId"), args.getString("batchId"), getDensity(), getActivity()).execute();
                }
                else{
                    setupScratcher();
                }

                break;
            case 2:
                frameLayout = (FrameLayout)rootView.findViewById(R.id.template_holder);

                lInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = (RelativeLayout) lInflater.inflate(R.layout.ticket_template_two, null);
                frameLayout.addView(myView);
                break;
            case 3:
                frameLayout = (FrameLayout)rootView.findViewById(R.id.template_holder);

                lInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = (RelativeLayout) lInflater.inflate(R.layout.ticket_template_three, null);
                frameLayout.addView(myView);
                break;
            case 4:
                frameLayout = (FrameLayout)rootView.findViewById(R.id.template_holder);

                lInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = (RelativeLayout) lInflater.inflate(R.layout.ticket_template_four, null);
                frameLayout.addView(myView);
                break;

        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 5);
        title = getArguments().getString("Profile");
    }


    public static TestScratcherFragment newInstance() {
        TestScratcherFragment fragmentFirst = new TestScratcherFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 5);
        args.putString("someTitle", "Profile");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
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
            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            try {
                JSONObject obj = new JSONObject(response.toString());

                background = obj.getString("background");
                ticketPrize = obj.getString("prizes");
                ticketLogo = obj.getString("logo");
                directions = obj.getString("directions");
                price = obj.getString("price");

                if(obj.has("yournumbers")) {
                    yourHeader = obj.getString("yourheader");
                    your = new String[4];
                    for (int j = 0; j < obj.getJSONArray("yournumbers").length(); j++) {
                        your[j] = obj.getJSONArray("yournumbers").getString(j);
                    }
                }

                if(obj.has("drawnumbers")) {
                    drawHeader = obj.getString("drawheader");
                    winning = new String[4];
                    for (int k = 0; k < obj.getJSONArray("drawnumbers").length(); k++) {
                        winning[k] = obj.getJSONArray("drawnumbers").getString(k);
                    }
                }

                if(obj.has("scratchimage") && !obj.get("scratchimage").equals("")){
                    scratchUrl = obj.getString("scratchimage");

                    resultImage = obj.getString("resultimagebinary");
                    resultImage.replaceAll("\\\\","");

                    byte[] decodedString = Base64.decode(resultImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    getEpsApplication().bothBitmap = decodedByte;

                    new DownloadImageTask(scratchUrl).execute();
                }
                else {
                    dynamicImages();
                }
            }
            catch(JSONException ex){
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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

    private void dynamicImages(){
        //Toast.makeText(getApplicationContext(), background, Toast.LENGTH_LONG).show();
        ImageView back = (ImageView)myView.findViewById(R.id.dynamic_back);
        back.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(background)
                .crossFade(R.anim.fade_in, 1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(back);
        ImageView logo = (ImageView)myView.findViewById(R.id.logo3);
        Glide.with(getActivity())
                .load(ticketLogo)
                .crossFade(R.anim.fade_in, 1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);

        ImageView instruc = (ImageView)myView.findViewById(R.id.instruc);
        Glide.with(getActivity())
                .load(directions)
                .crossFade(R.anim.fade_in, 1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(instruc);

        ImageView prize = (ImageView)myView.findViewById(R.id.prize);
        Glide.with(getActivity())
                .load(ticketPrize)
                .crossFade(R.anim.fade_in, 1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(prize);

        ImageView priceTag = (ImageView)myView.findViewById(R.id.price);
        Glide.with(getActivity())
                .load(price)
                .crossFade(R.anim.fade_in, 1000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(priceTag);

        if(template == 3) {
            //setUpFields();

            /*flip = (ImageView)findViewById(R.id.flip);
            flip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                    LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                    flip(front, back);
                    flip.setVisibility(View.INVISIBLE);
                }
            });

            flipBack = (ImageView)myView.findViewById(R.id.flip_back);
            flipBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                    LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                    flip(front, back);
                    flip.setVisibility(View.VISIBLE);
                }
            });*/
        }

        /*else{
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

                                flip = (ImageView)findViewById(R.id.flip);
                                flip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                        LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                        flip(front, back);
                                        flip.setVisibility(View.INVISIBLE);
                                    }
                                });

                                flipBack = (ImageView)myView.findViewById(R.id.flip_back);
                                flipBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LinearLayout back = (LinearLayout)myView.findViewById(R.id.ticket_back3);
                                        LinearLayout front = (LinearLayout)myView.findViewById(R.id.ticket_front3);
                                        flip(front, back);
                                        flip.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });
                    }
                }, 1500);
            }
        }*/
    }

    private void setupScratcher(){
        Bitmap scratch = BitmapFactory.decodeResource(getResources(),
                R.drawable.find9_numbers_scratched);

        getEpsApplication().bothBitmap = scratch;

        Bitmap over = BitmapFactory.decodeResource(getResources(),
                R.drawable.find9_numbers_scratch);

        getEpsApplication().scratchImage = over;

        ScratcherView actual = (ScratcherView)myView.findViewById(R.id.scratcherView3);
        actual.setZOrderOnTop(true);

        ((LandingActivity)getActivity()).disableScroll();
    }

    private String getDensity() {
        float density = getActivity().getResources().getDisplayMetrics().density;
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
}

