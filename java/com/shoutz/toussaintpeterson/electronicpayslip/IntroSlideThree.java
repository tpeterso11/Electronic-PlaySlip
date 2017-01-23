package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import models.AnimatorPath;
import models.PathEvaluator;
import models.PathPoint;

/**
 * Created by toussaintpeterson on 8/2/16.
 */
public class IntroSlideThree extends AbstractEPSFragment {
    // Store instance variables
    private String title;
    private int page;
    private ImageView asset1;
    private ImageView asset2;
    private ImageView asset3;
    private boolean mTopLeft;
    private static final DecelerateInterpolator sDecelerateInterpolator =
            new DecelerateInterpolator();

    // newInstance constructor for creating fragment with arguments
    public static IntroSlideThree newInstance() {
        IntroSlideThree fragmentFirst = new IntroSlideThree();
        Bundle args = new Bundle();
        args.putInt("someInt", 2);
        args.putString("someTitle", "Slide Three");
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 2);
        title = getArguments().getString("someTitle");

        mTopLeft = true;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_three, container, false);
//        asset1 = (ImageView)view.findViewById(R.id.asset2);
//        asset2 = (ImageView)view.findViewById(R.id.asset3);

//        final int oldLeft = asset1.getLeft();
//        final int oldTop = asset1.getTop();

/*        asset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //moveButton();
            }
        });*/
        return view;
    }
}