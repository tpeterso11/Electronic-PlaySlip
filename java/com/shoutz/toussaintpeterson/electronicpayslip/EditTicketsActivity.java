package com.shoutz.toussaintpeterson.electronicpayslip;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import models.CartObject;
import models.Ticket;

/**
 * Created by toussaintpeterson on 1/13/16.
 */
public class EditTicketsActivity extends AbstractEPSActivity {
    private String gameName;
    @Bind(R.id.edit_ticket_list) ListView editTicketsList;
    private ArrayList<Ticket> game1;
    private ArrayList<Ticket> game2;
    private ArrayList<Ticket> game3;
    private ArrayList<Ticket> game4;
    private ArrayList<Ticket> game5;
    private ArrayList<Ticket> game6;
    @Bind(R.id.gameName) TextView gameNameTag;
    @Bind(R.id.cart_image) ImageView cartImage;
    public ArrayList<ArrayList<Ticket>> editArray;
    private ArrayList<Ticket> mainTickets;
    private int originalPosition;
    private Ticket originalTicket;
    private int originalTicketPos;

    @Override
    public void onBackPressed(){
        ArrayList<Ticket> newTickets = new ArrayList<Ticket>();
        for(Ticket ticket : mainTickets){
            newTickets.add(ticket);
        }
        editArray.add(newTickets);
        //super.onBackPressed();
        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EditTicketsActivity.this, cartImage, "reveal");
        //Intent intent = new Intent(EditTicketsActivity.this, ShoppingListViewActivity.class);
        //startActivity(intent, options.toBundle());
        Intent i = new Intent(EditTicketsActivity.this, ShoppingListViewActivity.class);
        startActivity(i);
        //overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_tickets);
        ButterKnife.bind(this);

        gameName = getIntent().getStringExtra("gameName");
        gameNameTag.setText(gameName);
        editArray = getEpsApplication().editArray;
        for(ArrayList<Ticket> tickets : editArray){
            mainTickets = tickets;
            editTicketsList.setAdapter(new EditAdapter(getBaseContext(), tickets));
        }
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

    public class EditAdapter extends ArrayAdapter<Ticket> {
        private LayoutInflater mInflater;

        public EditAdapter(Context context, ArrayList<Ticket> content) {
            super(context, android.R.layout.simple_list_item_1, content);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = mInflater.inflate(R.layout.edit_ticket_row, null);
            }

            Ticket ticket = getItem(position);
//            final TextView gameName = (TextView) v.findViewById(R.id.row_game_name);
//            gameName.setText(ticket.getGameName());

            LinearLayout numbers = (LinearLayout)v.findViewById(R.id.edit_ticket_numbers);
            for(int i = 0; i < ticket.getNumbers().size(); i++){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
                params.setMargins(500, 0, 50, 0);
                final EditText ball = new EditText(getApplicationContext());
                //ball.setBackgroundResource(R.drawable.square);
                ball.setText(ticket.getNumbers().get(i));
                ball.setTextColor(getResources().getColor(R.color.black));
                ball.setGravity(Gravity.CENTER);
                ball.setLayoutParams(params);
                ball.requestLayout();
                ball.setInputType(InputType.TYPE_CLASS_NUMBER);
                ball.setImeOptions(EditorInfo.IME_ACTION_DONE);
                ball.setSelection(ball.getText().length());

                InputFilter[] input = new InputFilter[1];
                input[0] = new InputFilter.LengthFilter(1);
                ball.setFilters(input);
                numbers.addView(ball);

                final int originalPosition = i;
                originalTicket = ticket;
                final int originalTicketPos = mainTickets.indexOf(ticket);

                ball.setOnEditorActionListener(
                        new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                        actionId == EditorInfo.IME_ACTION_DONE ||
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    String newNumber = ball.getText().toString();
                                    //int position = ticket.getNumbers().indexOf(ball.getText().toString());
                                    Crouton.makeText(EditTicketsActivity.this, newNumber, Style.ALERT).show();

                                    originalTicket.getNumbers().remove(originalPosition);
                                    originalTicket.getNumbers().add(originalPosition, newNumber);
                                    originalTicket.setNumbers(originalTicket.getNumbers());

                                    mainTickets.remove(originalTicketPos);
                                    mainTickets.add(originalTicket);
                                    editArray.add(mainTickets);
                                    for(ArrayList<Ticket> tickets : editArray){
                                        mainTickets = tickets;
                                        editTicketsList.setAdapter(new EditAdapter(getBaseContext(), tickets));
                                    }

                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                    return true;
                                }
                                return false;
                            }
                        });
            }
            return v;
        }
    }
}
