package com.shoutz.toussaintpeterson.electronicpayslip;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.util.Charsets;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.CartObject;
import models.Ticket;

/**
 * Created by toussaintpeterson on 5/18/16.
 */
public class CustomerInstructionsActivity extends AbstractEPSActivity {
    private String confirmationCode;
    private byte[] signature;
    //@Bind(R.id.signature_final) ImageView sigFinal;
    @Bind(R.id.confirmation_code) TextView confirmation;
    //@Bind(R.id.redeem_float) FloatingActionButton redeemFloat;
    private ArrayList<ArrayList> masterArray;
    private ArrayList<CartObject> cartObjects;
    private String signatureString;
    private JSONObject response;
    private Bitmap bitmap;
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInstructionsActivity.this);
        builder.setTitle(getResources().getString(R.string.all_done));
        builder.setMessage(getResources().getString(R.string.thank_you));
        builder.setNegativeButton("Cancel Purchase", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(masterArray != null) {
                    masterArray.clear();
                }
                if(cartObjects != null) {
                    cartObjects.clear();
                }
                if(bitmap != null) {
                    bitmap.recycle();
                }
                if(getIntent().getStringExtra("from") != null){
                    Intent i = new Intent(CustomerInstructionsActivity.this, MyTicketsActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    finish();
                }
                else {
                    Intent i = new Intent(CustomerInstructionsActivity.this, LandingActivity.class);
                    //i.putExtra("notification", "new ticket");
                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    finish();
                }
            }
        });
        //builder.setNeutralButton("Cancel", null);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(masterArray != null) {
                    masterArray.clear();
                }
                if(cartObjects != null) {
                    cartObjects.clear();
                }
                if(bitmap != null) {
                    bitmap.recycle();
                }

                if(getResources().getString(R.string.demo_mode).equals("true")){
                    new PurchaseTask(getResources().getString(R.string.purchase_batch), "123", "124T", getApplicationContext()).execute();
                }
                else {
                    Intent i = new Intent(CustomerInstructionsActivity.this, LandingActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                    finish();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.instructions);
        ButterKnife.bind(this);

        if(getIntent().getStringExtra("from") != null){
            confirmationCode = getIntent().getStringExtra("batchid");
            confirmation.setText(confirmationCode);

            if(!getIntent().getStringExtra("signature").equals("")) {
                signatureString = getIntent().getStringExtra("signature");
                signature = Base64.decode(signatureString, Base64.DEFAULT);

                bitmap = BitmapFactory.decodeByteArray(signature, 0, signature.length);
                //sigFinal.setImageBitmap(bitmap);
            }
            else{
                //sigFinal.setVisibility(View.GONE);
            }
        }
        else{
            signature = getIntent().getByteArrayExtra("signature");
            bitmap = BitmapFactory.decodeByteArray(signature, 0, signature.length);
            //sigFinal.setImageBitmap(bitmap);

            confirmationCode = getIntent().getStringExtra("confirmation");
            confirmation.setText(confirmationCode);
        }

        masterArray = getEpsApplication().masterArray;
        cartObjects = getEpsApplication().cartObjects;

       /* redeemFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            if(masterArray != null) {
                masterArray.clear();
            }
            if(cartObjects != null) {
                cartObjects.clear();
            }
            if(bitmap != null) {
                bitmap.recycle();
            }
            Intent i = new Intent(CustomerInstructionsActivity.this, LandingActivity.class);
            i.putExtra("notification", "You have new tickets available! Check \"My Tickets\" to redeem!");
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
            finish();
        }

        @Override
        protected void onError(Object response) {
            Crouton.makeText(CustomerInstructionsActivity.this, "Error Redeeming Tickets!", Style.ALERT).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            JSONObject params = new JSONObject();
            params.put("batchid", confirmationCode);
            params.put("agentid", agent);
            params.put("terminalid", terminal);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }
}
