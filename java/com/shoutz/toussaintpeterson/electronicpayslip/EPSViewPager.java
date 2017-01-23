package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by toussaintpeterson on 6/2/16.
 */
public class EPSViewPager extends ViewPager {

        private boolean enabled;

        public EPSViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (this.enabled) {
                return super.onTouchEvent(event);
            }

            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (this.enabled) {
                return super.onInterceptTouchEvent(event);
            }

            return false;
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }
}
