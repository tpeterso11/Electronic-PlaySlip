package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by toussaintpeterson on 8/2/16.
 */
public class TestIntroSlides extends AbstractEPSActivity {
    @Bind(R.id.slide_holder) ViewPager holder;
    private CircleIndicator springIndicator;
    @Bind(R.id.skip) TextView skip;
    @Bind(R.id.next) ImageView next;
    @Bind(R.id.start) TextView start;
    private int selectedIndex;

    @Override
    public void onBackPressed(){
        if(start.getVisibility() == View.VISIBLE){
            Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
            start.startAnimation(out);
            start.setVisibility(View.GONE);
            next.startAnimation(in);
            next.setVisibility(View.VISIBLE);

            holder.setCurrentItem(getItem(-1));
        }

        else if(holder.getCurrentItem() != 0){
            holder.setCurrentItem(getItem(-1));
        }

        else{
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("introShown", "true").apply();
            edit.putString("firstTime", "false").apply();
            Intent i = new Intent(TestIntroSlides.this, SplashLandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.tran_to_left, R.anim.tran_from_right);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_slides);
        ButterKnife.bind(this);



        setupViewPager(holder);
        holder.setOffscreenPageLimit(2);
        holder.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(position == 2){
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                    //next.startAnimation(out);
                    next.setVisibility(View.GONE);
                    start.startAnimation(in);
                    start.setVisibility(View.VISIBLE);
                }
                else if(position == 1 && start.getVisibility() == View.VISIBLE){
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                    //start.startAnimation(out);
                    start.setVisibility(View.GONE);
                    next.startAnimation(in);
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(holder.getCurrentItem() == 1){
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);
                    //next.startAnimation(out);
                    next.setVisibility(View.GONE);
                    start.startAnimation(in);
                    start.setVisibility(View.VISIBLE);

                    holder.setCurrentItem(getItem(+1));
                }
                else{
                    holder.setCurrentItem(getItem(+1));
                }
                return false;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("firstTime", "false").apply();
                edit.putString("introShown", "true").apply();
                Intent i = new Intent(TestIntroSlides.this, LandingActivity.class);
                i.putExtra("location", "slides");
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
            }
        });

        skip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("firstTime", "false").apply();
                edit.putString("introShown", "true").apply();
                Intent i = new Intent(TestIntroSlides.this, LandingActivity.class);
                i.putExtra("location", "slides");
                startActivity(i);
                overridePendingTransition(R.anim.tran_to_right, R.anim.tran_from_left);
                return false;
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

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(IntroSlideOne.newInstance(), "1");
        adapter.addFragment(IntroSlideTwo.newInstance(), "2");
        adapter.addFragment(IntroSlideThree.newInstance(), "3");
        viewPager.setAdapter(adapter);

        springIndicator = (CircleIndicator) findViewById(R.id.spring);
        springIndicator.setViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private int getItem(int i) {
        return holder.getCurrentItem() + i;
    }
}
