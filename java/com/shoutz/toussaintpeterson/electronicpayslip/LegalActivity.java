package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by toussaintpeterson on 8/26/16.
 */
public class LegalActivity extends AbstractEPSActivity {
    @Bind(R.id.webview) WebView webview;

    @Override
    public void onBackPressed(){
        if(webview.canGoBack()){
            webview.goBack();
        }
        else{
            Intent i = new Intent(LegalActivity.this, LandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
        ButterKnife.bind(this);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //view.loadUrl(url);
                System.out.println("hello");
                return true;
            }
        });
        webview.loadUrl("https://shoutzinc.helpshift.com/a/gameport/?hpn=1&p=android&han=1&l=en&s=legal");
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
