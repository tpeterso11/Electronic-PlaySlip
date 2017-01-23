package com.shoutz.toussaintpeterson.electronicpayslip;

import android.os.Bundle;
import android.widget.ListView;

import butterknife.Bind;

/**
 * Created by toussaintpeterson on 11/7/16.
 */

public class TestTickets extends AbstractEPSActivity {
    @Bind(R.id.ticket_listview)
    ListView tickets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_my_tickets);



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
