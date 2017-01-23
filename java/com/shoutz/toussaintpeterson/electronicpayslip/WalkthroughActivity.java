package com.shoutz.toussaintpeterson.electronicpayslip;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by toussaintpeterson on 7/21/16.
 */
public class WalkthroughActivity extends AbstractEPSActivity {
    @Bind(R.id.enter_button) Button enter;
    @Bind(R.id.logo) ImageView logo;

    @Override
    public void onBackPressed(){
        //SharedPreferences.Editor edit = prefs.edit();
        //edit.putString("firstTime", "false").apply();

        Intent i = new Intent(WalkthroughActivity.this, SplashLandingActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough);
        ButterKnife.bind(this);

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("firstTime", "false").apply();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 21) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WalkthroughActivity.this,
                            new Pair(logo, "moveLogo"));
                    Intent intent = new Intent(WalkthroughActivity.this, LandingActivity.class);
                    intent.putExtra("location", "walkthrough");
                    startActivity(intent, options.toBundle());
                }
                else{
                    Intent intent = new Intent(WalkthroughActivity.this, LandingActivity.class);
                    intent.putExtra("location", "walkthrough");
                    startActivity(intent);
                }
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
}
