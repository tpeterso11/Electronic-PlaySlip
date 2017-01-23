package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.User;

/**
 * Created by toussaintpeterson on 3/28/16.
 */
public class RegisterActivity extends AbstractEPSActivity implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener{
    private Button next;
    private EditText enterEmail;
    private EditText enterFname;
    private EditText enterLname;
    private Button enterDob;
    private EditText enterCity;
    private EditText enterZip;
    public SharedPreferences prefs;
    private JSONObject response;
    private RelativeLayout logo;
    private RelativeLayout loading;
    private ProgressBar bar;
    private ImageView loadingLogo;
    private static final String TAG = "SignInActivity";
    private static int keyDel;
    private TextView skip;
    private String fullName;
    private String email;
    private String dob;
    private String city;
    private String zip;
    private String state;
    private TextView finalDob;
    private String firstName;
    private String lastName;
    private Spinner states;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RegisterActivity.this, SplashLandingActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setEnterTransition(null);
        setContentView(R.layout.register_fragment);
        loading = (RelativeLayout)findViewById(R.id.loading);
        skip = (TextView)findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LandingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        bar = (ProgressBar)findViewById(R.id.progressBar1);
        loadingLogo = (ImageView)findViewById(R.id.loading_logo);
        next = (Button)findViewById(R.id.register_next);
        //initView();

        User stored = getEpsApplication().getUser();
        // Inflate the layout for this fragment
        logo = (RelativeLayout)findViewById(R.id.logo);
        enterEmail = (EditText)findViewById(R.id.enter_email);
        enterFname = (EditText)findViewById(R.id.enter_fname);
        enterLname = (EditText)findViewById(R.id.enter_lname);
        enterCity = (EditText)findViewById(R.id.enter_city);
        enterDob = (Button)findViewById(R.id.enter_dob);
        enterZip = (EditText)findViewById(R.id.enter_zip);
        finalDob = (TextView)findViewById(R.id.final_dob);

        states = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_state, getResources().getStringArray(R.array.states_array)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_state);
        states.setAdapter(spinnerArrayAdapter);

        enterEmail.hasFocus();

        /*enterDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Calendar calendar = Calendar.getInstance();
                com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(RegisterActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1915, 2016);
                //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), "tag");

                return true;
            }
        });*/

        enterDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(RegisterActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                //datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1915, 2016);
                //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), "tag");
            }
        });

        prefs = getSharedPreferences(getEpsApplication().getUser().getPhoneNumber(), 0);

        /*enterEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                enterEmail.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        return false;
                    }
                });

                if (keyDel == 0) {
                    int len = enterEmail.getText().length();
                    if(len == 1) {
                        EditText username = (EditText)findViewById(R.id.enter_email);
                        username.setText("+" + enterEmail.getText());
                        username.setSelection(enterEmail.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(enterEmail.getText().toString().equals(""))){
                    email = enterEmail.getText().toString();
                    getEpsApplication().getUser().setEmail(enterEmail.getText().toString());
                }
                else{
                    email = "";
                }

                if (!(enterFname.getText().toString().equals(""))){
                    firstName = enterFname.getText().toString();
                    getEpsApplication().getUser().setFirstname(enterFname.getText().toString());
                }
                else{
                    firstName = "";
                }

                if (!(enterLname.getText().toString().equals(""))){
                    lastName = enterLname.getText().toString();
                    getEpsApplication().getUser().setLastname(enterLname.getText().toString());
                }
                else{
                    lastName = "";
                }

                if (!(finalDob.getText().toString().equals("MM/DD/YY"))){
                    dob = finalDob.getText().toString();
                    getEpsApplication().getUser().setBirthdate(finalDob.getText().toString());
                }
                else{
                    dob = "";
                }

                if (!(enterCity.getText().toString().equals(""))){
                    city = enterCity.getText().toString();
                    getEpsApplication().getUser().setCity(enterCity.getText().toString());
                }
                else{
                    city = "";
                }

                if (!(enterZip.getText().toString().equals(""))){
                    zip = enterZip.getText().toString();
                    getEpsApplication().getUser().setZipCode(enterZip.getText().toString());
                }
                else{
                    zip = "";
                }

                if(!(states.getSelectedItem().toString().equals("State"))){
                    state = states.getSelectedItem().toString();
                    getEpsApplication().getUser().setState(states.getSelectedItem().toString());
                }
                else{
                    state = "";
                }

                new UpdateTask(getResources().getString(R.string.edit_user), firstName, lastName, email, dob, zip, state, getApplicationContext()).execute();
            }
        });

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

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {
        enterDob.setBackgroundResource(R.drawable.button_trans_blue_selected);
        enterDob.setTextColor(getResources().getColor(R.color.light_blue));
        enterDob.setText("DOB Set");

        String str = String.valueOf(month+1)+"/"+ String.valueOf(day)+"/"+year;
        finalDob.setText(str);
    }

    public class UpdateTask extends AbstractWebService {
        private String fname, lname, email, dob, zip, state;

        public UpdateTask(String urlPath, String fname, String lname, String email, String dob,
                        String zip, String state, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.fname = fname;
            this.lname = lname;
            this.email = email;
            this.dob = dob;
            this.zip = zip;
            this.state = state;

        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            Intent i = new Intent(RegisterActivity.this, LandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        @Override
        protected void onError(Object response) {
            try {
                JSONObject responseJSON = new JSONObject(response.toString());
                Crouton.makeText(RegisterActivity.this, responseJSON.getString("logmessage"), Style.ALERT).show();
            }
            catch(JSONException ex){

            }

            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            loading.startAnimation(fade);
            loading.setVisibility(View.GONE);
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            params.put("firstname", fname);
            params.put("lastname", lname);
            params.put("dob", dob);
            params.put("address1", "null");
            params.put("address2", "null");
            params.put("city", city);
            params.put("stateprovince", state);
            params.put("postalcode", zip);
            params.put("phone1", getEpsApplication().getUser().getUsername());
            params.put("phone2", "null");
            params.put("email1", email);
            params.put("email2", "null");
            params.put("authtoken", getEpsApplication().getUser().getAuthToken());

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

        private void updateLabel() {

            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            //birthdate.setText(sdf.format(birthdateCal.getTime()));
        }

    private View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            Calendar calendar = Calendar.getInstance();
            com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(RegisterActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            //datePickerDialog.setVibrate(isVibrate());
            datePickerDialog.setYearRange(1915, 2016);
            //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
            datePickerDialog.show(getSupportFragmentManager(), "tag");
            return true; // the listener has consumed the event
        }
    };
}
