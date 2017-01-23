package com.shoutz.toussaintpeterson.electronicpayslip;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by toussaintpeterson on 8/2/16.
 */
public class IntroSlideOne extends AbstractEPSFragment {
    // Store instance variables
    private String title;
    private int page;
    private ImageView asset1;
    private ImageView asset2;

    // newInstance constructor for creating fragment with arguments
    public static IntroSlideOne newInstance() {
        IntroSlideOne fragmentFirst = new IntroSlideOne();
        Bundle args = new Bundle();
        args.putInt("someInt", 0);
        args.putString("someTitle", "Slide One");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_one, container, false);

        //asset1 = (ImageView)view.findViewById(R.id.asset1);
        //asset2 = (ImageView)view.findViewById(R.id.asset2);

        //Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.pop);
        //asset1.startAnimation(in);
        //asset2.startAnimation(in);
        //asset1.setVisibility(View.VISIBLE);
        //asset2.setVisibility(View.VISIBLE);
        return view;
    }
}