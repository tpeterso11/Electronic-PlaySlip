package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.telephony.SmsManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.CartObject;
import models.Ticket;

/**
 * Created by toussaintpeterson on 3/28/16.
 */
public class FinalizeActivity extends AbstractEPSActivity {
    /*@Bind(R.id.confirmation) RelativeLayout confirmLayout;
    @Bind(R.id.send_button) FloatingActionButton send;
    @Bind(R.id.sms_button) FloatingActionButton sms;
    @Bind(R.id.email_button) FloatingActionButton email;
    @Bind(R.id.redeem_button) Button redeem;
    @Bind(R.id.redeem_later_button) Button redeemLater;*/
    @Bind(R.id.loading) RelativeLayout loading;
    private ArrayList<CartObject> cartObjects;
    public ArrayList<ArrayList> masterArray;
    public ArrayList<ArrayList<Ticket>> editArray;
    private ArrayList<Ticket> cartTickets;
    private boolean isConfirm;
    private boolean isOpen;
    private boolean isRedeeming;
    private boolean isFinalized;
    private byte[] signature;
    private String confirmationCode;
    private JSONObject response;

    public void onBackPressed(){
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        loading.startAnimation(in);
        loading.setVisibility(View.VISIBLE);
        TextView prompt = (TextView)findViewById(R.id.welcome_text);

        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(in);
        set.addAnimation(slide);
        prompt.startAnimation(set);
        prompt.setVisibility(View.VISIBLE);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(FinalizeActivity.this, LandingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                finish();
            }
        }, 1200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.finalize_activity);
        ButterKnife.bind(this);

        isConfirm = true;
        isOpen = false;
        isFinalized = true;
        isRedeeming = false;
        cartObjects = new ArrayList<CartObject>();
        cartTickets = ((EPSApplication) getApplicationContext()).ticketsArray;
        masterArray = ((EPSApplication) getApplicationContext()).masterArray;
        editArray = getEpsApplication().editArray;
        confirmationCode = getIntent().getStringExtra("confirmation");

        signature = getIntent().getByteArrayExtra("signature");

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        /*if (!(networkInfoWifi.isConnected()) && !((networkInfoPhone.isConnected()))){
            redeem.setEnabled(false);
            Float alpha = Float.valueOf(".4");
            redeem.setAlpha(alpha);
        }
        else {
            redeem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(FinalizeActivity.this, CustomerInstructionsActivity.class);
                    i.putExtra("signature", signature);
                    i.putExtra("confirmation", confirmationCode);
                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    //new TestTask(getResources().getString(R.string.post_ticket_service), getApplicationContext()).execute();
                }
            });
        }

        redeemLater.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(!(networkInfoPhone.isConnected()) && !(networkInfoWifi.isConnected())) {
                    try {
                        createFile();
                    } catch (JSONException | IOException exception) {

                    }
                }

                masterArray.clear();
                cartObjects.clear();

                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                loading.startAnimation(in);
                loading.setVisibility(View.VISIBLE);
                TextView prompt = (TextView)findViewById(R.id.welcome_text);

                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(in);
                set.addAnimation(slide);
                prompt.startAnimation(set);
                prompt.setVisibility(View.VISIBLE);

                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent i = new Intent(FinalizeActivity.this, LandingActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                        finish();
                    }
                }, 1200);
                /*try {
                    readFile();
                }
                catch(IOException | JSONException ex){

                }
                /*ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!(networkInfoWifi.isConnected()) && !((networkInfoPhone.isConnected()))) {

                }
                onBackPressed();*/
        //    }
        //});

        /*send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showHideButtons();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS("1-816-645-9940");
            }
        });

        email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });*/

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

    /*public class CartAdapter extends ArrayAdapter<CartObject> {
        private LayoutInflater mInflater;

        public CartAdapter(Context context, ArrayList<CartObject> content) {
            super(context, android.R.layout.simple_list_item_1, content);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(R.layout.ticket_row_confirm, null);
            }

            final CartObject object = getItem(position);
            final TextView gameName = (TextView) v.findViewById(R.id.row_game_name);
            TextView quantity = (TextView) v.findViewById(R.id.row_game_quant);
            TextView cost = (TextView) v.findViewById(R.id.row_game_cost);
            ImageView edit = (ImageView) v.findViewById(R.id.edit_ticket);

            int quantityStart = Integer.valueOf(object.getQuantity());
            int costStart = Integer.valueOf(object.getCost());
            int finish = quantityStart * costStart;

            gameName.setText(object.getGameName());
            quantity.setText(object.getQuantity());
            //finalTotal = finalTotal + Integer.valueOf(object.getQuantity());
            //finalCost = finalCost + Integer.valueOf(object.getCost());
            cost.setText(object.getCost());
            if (object.getGameName().equals("Totals") || isConfirm) {
                edit.setVisibility(View.INVISIBLE);
                //    cost.setText(String.valueOf(finalCost));
            } else {
                edit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        editArray.add(object.getGames());

                        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FinalizeActivity.this, cartImage, "reveal");
                        Intent intent = new Intent(FinalizeActivity.this, EditTicketsActivity.class);
                        intent.putExtra("gameName", gameName.getText().toString());
                        //startActivity(intent, options.toBundle());

                        //Intent i = new Intent(ShoppingListViewActivity.this, EditTicketsActivity.class);
                        //i.putExtra("gameName", gameName.getText().toString());
                        //startActivity(i);
                        //overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);

                        //editArray.add(object.getGames());
                        //Intent i = new Intent(ShoppingListViewActivity.this, EditTicketsActivity.class);
                        //i.putExtra("gameName", gameName.getText().toString());
                        //startActivity(i);
                    }
                });
            }
            //cost.setText(object.getCost());

            return v;
        }
    }*/

    private void addArraystoCart() {
        int totals = 0;
        int cost = 0;

        for (ArrayList<Ticket> array : masterArray) {
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

    /*private void resetFinalize(){
        //signImage.setImageResource(android.R.color.transparent);
        //signImage.setVisibility(View.GONE);

        finalConfirm.setVisibility(View.VISIBLE);
        ticketCancel.setVisibility(View.VISIBLE);
    }

    /* Notifications Code */
    /*private void showHideButtons(){
        if(!isOpen){
            isOpen = true;
            ObjectAnimator anim = ObjectAnimator.ofFloat(sms, "translationX", -200);
            ObjectAnimator animEmail = ObjectAnimator.ofFloat(email, "translationY", -200);
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(email, "scaleX", 1);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(email, "scaleY", 1);
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);
            scaleDown.start();
            ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(sms, "scaleX", 1);
            ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(sms, "scaleY", 1);
            AnimatorSet scaleDown2 = new AnimatorSet();
            scaleDown2.play(scaleDownX2).with(scaleDownY2);
            scaleDown2.start();
            sms.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            anim.start();
            animEmail.start();

            PackageManager pm = getPackageManager();

            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfoPhone = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo networkInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


            if (!(networkInfoPhone.isConnected())){
                sms.setEnabled(false);
                Float alpha = Float.valueOf(".4");
                sms.setAlpha(alpha);
            }

            if(!(networkInfoWifi.isConnected()) && !((networkInfoPhone.isConnected()))){
                email.setEnabled(false);
                Float alpha = Float.valueOf(".4");
                email.setAlpha(alpha);
            }
        }
        else{
            isOpen = false;
            ObjectAnimator anim = ObjectAnimator.ofFloat(sms, "translationX", 0);
            ObjectAnimator animEmail = ObjectAnimator.ofFloat(email, "translationY", 0);
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(email, "scaleX", 0.3f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(email, "scaleY", 0.3f);
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);
            scaleDown.start();
            ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(sms, "scaleX", 0.3f);
            ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(sms, "scaleY", 0.3f);
            AnimatorSet scaleDown2 = new AnimatorSet();
            scaleDown2.play(scaleDownX2).with(scaleDownY2);
            scaleDown2.start();
            anim.start();
            animEmail.start();

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    FinalizeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            sms.setVisibility(View.GONE);
                            email.setVisibility(View.GONE);
                        }
                    });
                }
            }, 300);
        }
    }*/

    private void sendEmail(){
        String emailTo 		= "test@test.com";
        String emailSubject 	= "test subject";
        String emailContent 	= "test message";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailTo});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);

        //need this to prompts email client only
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));
    }

    private void sendSMS(String phoneNumber) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        String message = "From Electronic Play Slip: \n your confirmation code is: " + confirmationCode;

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Crouton.makeText(FinalizeActivity.this, "SMS Sent", Style.CONFIRM);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Crouton.makeText(FinalizeActivity.this, "Generic failure", Style.ALERT);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Crouton.makeText(FinalizeActivity.this, "No Service", Style.ALERT);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Crouton.makeText(FinalizeActivity.this, "Null PDU", Style.ALERT);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Crouton.makeText(FinalizeActivity.this, "Radio Off", Style.ALERT);
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Crouton.makeText(FinalizeActivity.this, "SMS Delivered", Style.CONFIRM);
                        break;
                    case Activity.RESULT_CANCELED:
                        Crouton.makeText(FinalizeActivity.this, "SMS Delivery Error", Style.ALERT);
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
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
        return String.valueOf(cost);
    }

    public class TestTask extends AbstractWebService {
        private String urlPath;

        public TestTask(String urlPath, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onError(Object response) {
            Toast.makeText(FinalizeActivity.this, "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> params = new HashMap<String, String>();
            params.put("gameid", "1");
            params.put("betamount", "1");
            params.put("numbers", "1|2|3|4");

            JSONObject object = new JSONObject();
            response = doPost(object, params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    private void createFile() throws IOException, JSONException{
        JSONArray data = new JSONArray();
        for(ArrayList<Ticket> tickets : masterArray) {
            for(int i = 0; i<tickets.size(); i++) {
                JSONObject ticketInfo = new JSONObject();
                try {
                    ticketInfo.put("authToken", getEpsApplication().getUser().getAuthToken());
                    ticketInfo.put("numbers", tickets.get(i).getNumbers());
                    data.put(ticketInfo);
                } catch (JSONException ex) {

                }
            }
        }

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File folder = new File(getFilesDir(), "Orders");
        if(!folder.exists()){
            folder.mkdir();
        }

        //to delete all files in the directory we created
        /*File[] files = folder.listFiles();
        for (File tmpf : files){
            tmpf.delete();
        }*/

        String path = folder.getAbsolutePath();
        String[] list = folder.list();
        String file = folder + "/delayedOrder" + ts + ".json";

        //File f = new File(file);
        File f = new File(folder, "/delayedOrder" + ts + ".json");

        try {
            FileOutputStream fileout = new FileOutputStream(f, true);
            OutputStreamWriter osw = new OutputStreamWriter(fileout);
            osw.write(data.toString());
            osw.close();

            System.out.println("Successfully Copied JSON Object to File...");
        }
        catch (FileNotFoundException ex) {

        }
//            readFile();
    }

    /*private void readFile() throws IOException, JSONException{
        ArrayList<File> inFiles = new ArrayList<File>();
        ArrayList<JSONArray> jsonObjects = new ArrayList<JSONArray>();

        File folder = new File(getFilesDir(), "Orders");
        File[] files = folder.listFiles();

        for (File file : files) {
            if(file.getName().endsWith(".json")){
                inFiles.add(file);
            }
        }

        for(File json : inFiles){
            FileInputStream fileInputStream = new FileInputStream(json);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);

                JSONArray temp = new JSONArray(responseStrBuilder.toString());
                jsonObjects.add(temp);
            }
        }
    }*/

}
