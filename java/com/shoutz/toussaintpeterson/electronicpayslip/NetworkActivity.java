package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by toussaintpeterson on 8/25/16.
 */
public class NetworkActivity extends AbstractEPSActivity {
    @Bind(R.id.ring_sm) ImageView ringSm;
    @Bind(R.id.ring_med) ImageView ringMd;
    @Bind(R.id.ring_lg) ImageView ringLg;
    @Bind(R.id.gotIt) Button gotIt;
    private Animation in;
    private Animation in2;
    private Animation in3;
    private Animation out;
    private Animation out2;
    private Animation out3;

    @Override
    public void onBackPressed(){
        Intent i = new Intent(NetworkActivity.this, SplashLandingActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_layout);
        ButterKnife.bind(this);

        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        in2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        in3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        out2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        out3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NetworkActivity.this, SplashLandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        //final View image = findViewById(R.id.image);
        /*final ViewTreeObserver observer = image.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this);
                // start animators
            }
        });*/
        showSignal();
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

    private void showSignal(){
        in.setDuration(500);
        ringSm.startAnimation(in);
        ringSm.setVisibility(View.VISIBLE);

        in2.setStartOffset(600);
        in2.setDuration(500);
        ringMd.startAnimation(in2);
        ringMd.setVisibility(View.VISIBLE);

        in3.setStartOffset(1300);
        in3.setDuration(500);
        ringLg.startAnimation(in3);
        ringLg.setVisibility(View.VISIBLE);

        in3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hideSignal();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void hideSignal(){
        out.setDuration(500);
        ringLg.startAnimation(out);
        ringLg.setVisibility(View.INVISIBLE);

        out2.setStartOffset(600);
        out2.setDuration(500);
        ringMd.startAnimation(out2);
        ringMd.setVisibility(View.INVISIBLE);

        out3.setStartOffset(1300);
        out3.setDuration(500);
        ringSm.startAnimation(out3);
        ringSm.setVisibility(View.INVISIBLE);

        out3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        NetworkActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                showSignal();
                            }
                        });
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //showSignal();
    }
}
