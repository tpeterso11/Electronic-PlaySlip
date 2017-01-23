package com.shoutz.toussaintpeterson.electronicpayslip;

import android.os.Bundle;
import android.view.SurfaceView;

import butterknife.Bind;

/**
 * Created by toussaintpeterson on 1/11/16.
 */
public class SignatureActivity extends AbstractEPSActivity {
    @Bind(R.id.customer_sign)
    TouchEventView signArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature);
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
