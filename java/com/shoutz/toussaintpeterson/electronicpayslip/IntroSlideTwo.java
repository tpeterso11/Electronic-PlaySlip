package com.shoutz.toussaintpeterson.electronicpayslip;

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
public class IntroSlideTwo extends AbstractEPSFragment {
    // Store instance variables
    private String title;
    private int page;
    private ImageView asset3;

    // newInstance constructor for creating fragment with arguments
    public static IntroSlideTwo newInstance() {
        IntroSlideTwo fragmentFirst = new IntroSlideTwo();
        Bundle args = new Bundle();
        args.putInt("someInt", 1);
        args.putString("someTitle", "Slide Two");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 1);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_two, container, false);

        asset3 = (ImageView)view.findViewById(R.id.asset3);
        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom);
        asset3.startAnimation(in);
        asset3.setVisibility(View.VISIBLE);
        return view;
    }
}