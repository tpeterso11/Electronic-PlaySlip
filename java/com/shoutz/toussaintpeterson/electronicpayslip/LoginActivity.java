package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.User;

/**
 * Created by toussaintpeterson on 3/28/16.
 */
public class LoginActivity extends AbstractEPSActivity implements GoogleApiClient.OnConnectionFailedListener{
    private ImageView fingerPrint;
    private FingerprintManager mFingerprintManager;
    private FingerprintManagerCompat mFingerprintCompat;
    public SharedPreferences prefs;
    private Button submit;
    private EditText username;
    private TextView already;
    private RelativeLayout main;
    private EditText password;
    private TextView guest;
    private TextView headerText;
    private ImageView logo;
    private String previous;
    private JSONObject response;
    private FloatingActionButton facebook;
    private FloatingActionButton google;
    private GoogleApiClient mGoogleApiClient;
    private RelativeLayout loading;
    private ImageView loadingLogo;
    private RelativeLayout mainLogin;
    private ProgressBar bar;
    private TextView register;
    private TextView usernamePhone;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private static int keyDel;


    @Override
    public void onBackPressed(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,
                    new Pair(logo, "moveLogo"));
            Intent intent = new Intent(LoginActivity.this, SplashLandingActivity.class);
            startActivity(intent, options.toBundle());
            //finish();
        }
        else{
            Intent intent = new Intent(LoginActivity.this, SplashLandingActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        if(getIntent().hasExtra("previous")){
            previous = getIntent().getStringExtra("previous");
        }
        main = (RelativeLayout)findViewById(R.id.main);
        initView();

        logo = (ImageView)findViewById(R.id.logo);
        facebook = (FloatingActionButton)findViewById(R.id.facebook);
        google = (FloatingActionButton)findViewById(R.id.google);
        loading = (RelativeLayout)findViewById(R.id.loading);
        loadingLogo = (ImageView)findViewById(R.id.loading_logo);
        bar = (ProgressBar)findViewById(R.id.progressBar1);

        /*facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));// <--- THIS WORKS
                /*new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/1733282126927878/accounts/test-users",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result *///JSONObject object = response.getJSONObject();
                                /*try{
                                    String name = object.getString("username");
                                }
                                catch(JSONException ex){

                                }
                            }
                        }
                ).executeAsync();*/
         //   }
        //});

        fingerPrint = (ImageView)findViewById(R.id.initialize_finger);
        fingerPrint.setVisibility(View.INVISIBLE);
        mFingerprintCompat = FingerprintManagerCompat.from(getApplicationContext());

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M){
            mFingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
            if (!mFingerprintManager.isHardwareDetected()) {
                fingerPrint.setAlpha(Float.valueOf(".4"));
            } else if (!mFingerprintManager.hasEnrolledFingerprints()) {
                fingerPrint.setAlpha(Float.valueOf(".4"));
                Toast.makeText(getApplicationContext(), "No Fingerprints!", Toast.LENGTH_SHORT).show();
            } else {
                fingerPrint.setAlpha(Float.valueOf("1"));
                // Everything is ready for fingerprint authentication
            }
        } else{
            if(!(mFingerprintCompat.isHardwareDetected())){
                fingerPrint.setAlpha(Float.valueOf(".4"));
            }
            else if(mFingerprintCompat.isHardwareDetected()){
                if(!(mFingerprintCompat.hasEnrolledFingerprints())){
                    Toast.makeText(getApplicationContext(), "No Fingerprints!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fingerprints!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                fingerPrint.setAlpha(Float.valueOf("1"));
            }
        }
        // Inflate the layout for this fragment

        /*if (!mFingerprintManager.hasEnrolledFingerprints()) {
            fingerPrint.setAlpha(Float.valueOf(".4"));
        }*/

        //FloatingActionButton faceBook = (FloatingActionButton)findViewById(R.id.facebook);
        /*faceBook.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TestTask();
            }
        });*/

        guest = (TextView)findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("email", "null").apply();
                edit.putString("username", "null").apply();
                startActivity(i);
            }
        });


        prefs = getSharedPreferences("userInfo", 0);
        username = (EditText)findViewById(R.id.login_user_email);
        usernamePhone = (EditText)findViewById(R.id.login_user_phone);

        usernamePhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                usernamePhone.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        return false;
                    }
                });

                if (keyDel == 0) {
                    int len = usernamePhone.getText().length();
                    if(len == 1) {
                        EditText username = (EditText)findViewById(R.id.login_user_phone);
                        username.setText("+" + usernamePhone.getText());
                        username.setSelection(usernamePhone.getText().length());
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
        });

        password = (EditText)findViewById(R.id.login_pass);

        submit = (Button)findViewById(R.id.login_submit);
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(username.getWindowToken(), 0);

                if(usernamePhone.getText().toString().equals("")){// ||
                        //!(isValidEmail(usernamePhone.getText().toString()))){
                    Crouton.makeText(LoginActivity.this, "Invalid Phone Number! Try Again!", Style.ALERT).show();
                }
                else if(password.getText().toString().equals("")){
                    Crouton.makeText(LoginActivity.this, "Invalid Password! Try Again!", Style.ALERT).show();
                }

                else{
                    Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    loading.startAnimation(fade);

                    if (Build.VERSION.SDK_INT < 21) {
                        loading.setVisibility(View.VISIBLE);
                        loading.bringToFront();
                    }
                    else {
                        loading.setVisibility(View.VISIBLE);
                    }

                    if(!(usernamePhone.getText().toString().substring(0,1).equals("+"))){
                        String phone = "+" + usernamePhone.getText().toString();
                        usernamePhone.setText(phone);
                    }

                    new LoginTask(getResources().getString(R.string.login_service),
                            usernamePhone.getText().toString().toLowerCase(), password.getText().toString(), LoginActivity.this).execute();
                    //animateLoadingScreen();
                    /*Intent i = new Intent(LoginActivity.this, LandingActivity.class);
                    startActivity(i);
                    finish();*/
                }
            }
        });

        /*FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(getApplicationContext(), "Successful Facebook Login", Toast.LENGTH_LONG).show();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        //Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            String email = object.getString("email");
                                            String password = object.getString("id");
                                            new LoginTask(getResources().getString(R.string.login_service),
                                                    email, password, LoginActivity.this).execute();

                                            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            loading.startAnimation(fade);
                                            loading.setVisibility(View.VISIBLE);
                                        }
                                        catch(JSONException ex){

                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email, id");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Facebook Login Canceled", Toast.LENGTH_LONG).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(getApplicationContext(), "Failed Facebook Login", Toast.LENGTH_LONG).show();
                    }
                });*/

        //checkAPI();

        register = (TextView)findViewById(R.id.already_register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createApiClient();

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void resetFields(){
        //this textview should be bound in the fragment onCreate as a member variable
        username.setText("");
        password.setText("");
    }

    public class LoginTask extends AbstractWebService {
        private String phone, password, passwordConfirm;

        public LoginTask(String urlPath, String phone, String password, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.phone = phone;
            this.password = password;
            this.passwordConfirm = passwordConfirm;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            //loading.setVisibility(View.GONE);
            try {
                SharedPreferences.Editor editor = prefs.edit();
                JSONObject responseJSON = new JSONObject(response.toString());
                User user = new User();
                user.setAuthToken(responseJSON.getString("sectoken"));
                user.setUsername(phone);
                user.setPhoneNumber(phone);
                user.setPassword(password);


                if(!responseJSON.getString("firstname").equals("")){
                    user.setFirstname(responseJSON.getString("firstname"));
                    editor.putString("firstname", responseJSON.getString("firstname")).apply();
                }
                else{
                    user.setFirstname("null");
                }
                if(!responseJSON.getString("lastname").equals("")){
                    user.setLastname(responseJSON.getString("lastname"));
                    editor.putString("lastname", responseJSON.getString("lastname")).apply();
                }
                else{
                    user.setLastname("null");
                }
                if(!responseJSON.getString("dob").equals("")){
                    user.setBirthdate(responseJSON.getString("dob"));
                    editor.putString("birthdate", responseJSON.getString("dob")).apply();
                }
                else{
                    user.setBirthdate("null");
                }
                if(!responseJSON.getString("address1").equals("")){
                    user.setAddress(responseJSON.getString("address1"));
                    editor.putString("address", responseJSON.getString("address1")).apply();
                }
                else{
                    user.setAddress("null");
                }
                if(!responseJSON.getString("city").equals("")){
                    user.setCity(responseJSON.getString("city"));
                    editor.putString("city", responseJSON.getString("city")).apply();
                }
                else{
                    user.setCity("null");
                }
                if(!responseJSON.getString("stateprovince").equals("")){
                    user.setState(responseJSON.getString("stateprovince"));
                    editor.putString("state", responseJSON.getString("stateprovince")).apply();
                }
                else{
                    user.setState("null");
                }
                if(!responseJSON.getString("postalcode").equals("")){
                    user.setZipCode(responseJSON.getString("postalcode"));
                    editor.putString("zipcode", responseJSON.getString("postalcode")).apply();
                }
                else{
                    user.setZipCode("null");
                }
                if(!responseJSON.getString("email1").equals("")){
                    user.setEmail(responseJSON.getString("email1"));
                    editor.putString("email", responseJSON.getString("email1")).apply();
                }
                else{
                    user.setEmail("null");
                }
                getEpsApplication().setUser(user);

                editor.putString("password", password).apply();
                editor.putString("username", phone).apply();
                editor.putString("phone", phone).apply();
                editor.putString("authtoken", responseJSON.getString("sectoken")).apply();

                animateLoadingScreen();
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            if(response != null) {
                try {
                    JSONObject responseJSON = new JSONObject(response.toString());
                    Crouton.makeText(LoginActivity.this, responseJSON.getString("logmessage"), Style.ALERT).show();
                } catch (JSONException ex) {

                }
            }

            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            loading.startAnimation(fade);
            loading.setVisibility(View.GONE);
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();

            JSONObject params = new JSONObject();
            params.put("username", phone);
            params.put("password", password);

            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    /*public class LoginTask extends AbstractWebService {
        private final ProgressDialog progressDialog;
        private String email, password;

        public LoginTask(ProgressDialog progressDialog, String email, String password, String urlPath, Context context){
            super(urlPath, true, context);
            this.progressDialog = progressDialog;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onSuccess(Object response) {
            if(user == null ){
                onInvalidCredentials();
            } else {
                onSuccessfulLogin(user);
            }
        }

        @Override
        protected void onError(Object response) {
            onInvalidCredentials(); //JULI change to pass web service msg
        }

        @Override
        protected Object doWebOperation() throws Exception {
            JSONObject json = new JSONObject();
            json.put("username", email);
            json.put("password", password);

            LoginActivity.this.email = email;
            LoginActivity.this.password = password;

            response = doPost(json);
            loginResponseHandler.obtainMessage(1).sendToTarget();

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Login")
                    .setAction("Manual Sign In")
                    .build());

	/*		EasyTracker easyTracker = EasyTracker.getInstance(getApplicationContext());
			easyTracker.send(MapBuilder
					.createEvent("manual sign in", "sign in", "1", 0L)
					.build()
					);*/

            //return response; //@todo stop using handler and use onSuccess\Error
        //}
    //}

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

    private void initView(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
                        main.startAnimation(in);
                        main.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 600);
    }

    private void createApiClient(){
        mGoogleApiClient = null;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("577682642756-eunaup1p7oj2476a3hjp6lelbmqal25o.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        /*else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String username = acct.getEmail();
            String pass = acct.getId();

            new LoginTask(getResources().getString(R.string.login_service),
                    username, pass, LoginActivity.this).execute();

            Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            loading.startAnimation(fade);
            loading.setVisibility(View.VISIBLE);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void animateLoadingScreen(){
        submit.setEnabled(false);
        facebook.setEnabled(false);
        google.setEnabled(false);
        guest.setEnabled(false);
        register.setEnabled(false);

        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        out.setStartOffset(1200);
        bar.startAnimation(out);

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bar.setVisibility(View.GONE);

                int test = loading.getTop() - loadingLogo.getHeight();
                ObjectAnimator anim = ObjectAnimator.ofFloat(loadingLogo, "translationY", test +20);
                anim.setDuration(800);
                anim.setStartDelay(500);
                anim.start();

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Animation up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(up);
                        set.addAnimation(in);

                        TextView welcome = (TextView)findViewById(R.id.welcome_text);
                        welcome.startAnimation(set);
                        welcome.setVisibility(View.VISIBLE);

                        set.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

                                AnimationSet set = new AnimationSet(true);
                                set.addAnimation(up);
                                set.addAnimation(in);

                                User userObj = getEpsApplication().getUser();
                                TextView userText = (TextView)findViewById(R.id.user_text);
                                //if(userObj != null && userObj.getFullName() != null || userObj != null && !(userObj.getFullName().equals(""))){
                                //    userText.setText(userObj.getFullName());
                                //    userText.startAnimation(set);
                                //    userText.setVisibility(View.VISIBLE);
                                //}
                                //else{
                                    userText.setText(username.getText().toString());
                                    userText.startAnimation(set);
                                    userText.setVisibility(View.VISIBLE);
                                //}

                                set.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        if(getIntent().hasExtra("previous")){
                                            Intent i = new Intent(LoginActivity.this, com.shoutz.toussaintpeterson.electronicpayslip.LandingActivity.class);
                                            i.putExtra("location", "login");
                                            startActivity(i);
                                        }
                                        else if((!(prefs.contains("firstTime")) && getResources().getString(R.string.codere).equals("true")) || getEpsApplication().isSpanish){
                                            if (Build.VERSION.SDK_INT >= 21) {
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,
                                                        new Pair(logo, "moveLogo"));
                                                Intent intent = new Intent(LoginActivity.this, WalkthroughActivity.class);
                                                startActivity(intent, options.toBundle());
                                                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                            }
                                            else{
                                                Intent intent = new Intent(LoginActivity.this, WalkthroughActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                                            }
                                        }
                                        else {
                                            Intent i = new Intent(LoginActivity.this, LandingActivity.class);
                                            i.putExtra("location", "login");
                                            startActivity(i);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
