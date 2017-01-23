package com.shoutz.toussaintpeterson.electronicpayslip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by toussaintpeterson on 11/18/15.
 */

public class ScratcherViewUser extends SurfaceView implements Animation.AnimationListener{
    private Paint paint = new Paint();
    private Paint mPaint;
    private Path path = new Path();
    private SurfaceHolder holder;
    public Bitmap  drawableBitmap;
    private Canvas  canvas;
    private ArrayList<Bitmap> scratcherImages;
    private boolean isShown;
    private Bitmap original;
    private ScratcherViewUser scratcherView;
    private Bitmap mBitmap;
    private EPSApplication application;

    public ScratcherViewUser(Context context, AttributeSet attrs) {
        super(context, attrs);

        scratcherImages = new ArrayList<Bitmap>();

        scratcherView = this;

        isShown = false;

        //Bitmap one = BitmapFactory.decodeResource(context.getResources(),
        //        R.drawable.find9_numbers_your_scratched);

        //scratcherImages.add(one);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100f);

        path = new Path();
        //this.setBackgroundResource(R.drawable.modern_tag_white);
        this.setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //loadedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.modern_logo_black);
        //random = scratcherImages.get(new Random().nextInt(scratcherImages.size()));
        //loadedBitmap = BitmapFactory.decodeResource(getResources(),
        //        R.drawable.find9_numbers_your_scratched);
        //Bitmap scaledloadedBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
        //        R.drawable.find9_numbers_scratched),  scratcherView.getWidth(), scratcherView.getHeight(), false);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(getEpsApplication().userBitmap,  scratcherView.getWidth(), scratcherView.getHeight(), false);
        drawableBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.modern_tag_white);
        //canvas = new Canvas(drawableBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        original = drawableBitmap;
        Canvas originalCanvas = new Canvas(original);
        super.onDraw(originalCanvas);

        Shader shader = new BitmapShader(original,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //paint.setXfermode(new PorterDuffXfermode(
        //        PorterDuff.Mode.CLEAR));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100f);
        paint.setShader(shader);

        //canvas.drawColor(Color.DKGRAY);
        Bitmap scaledloadedBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.alphabet_soup_your_scratch),  scratcherView.getWidth(), scratcherView.getHeight(), false);
        canvas.drawBitmap(scaledloadedBitmap, 0, 0, paint);
        canvas.drawPath(path, paint);

        scaledloadedBitmap.recycle();
        //original.recycle();
        //drawableBitmap.recycle();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //vib.vibrate(100);
                path.moveTo(xPos, yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                //vib.vibrate(100);
                path.lineTo(xPos, yPos);
                break;
            case MotionEvent.ACTION_UP:
                /*if(random.getIsWinner()){
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    this.startAnimation(shake);

                    ClassB newOne = new ClassB(getContext());
                    newOne.Update();
                }
                Toast.makeText(getContext(), String.valueOf(percentTransparent(drawableBitmap, 20)), Toast.LENGTH_LONG).show();*/
                break; // do nothing, finger lifted
            default:
                return false;
        }
        //schedule repaint
        invalidate();
        return true;
    }

    public EPSApplication getEpsApplication() {
        if(application==null)
            application = (EPSApplication)getApplicationContext();

        return application;
    }

    static public float percentTransparent(Bitmap bm, int scale)
    {

        final int width = bm.getWidth();
        final int height = bm.getHeight();

        // size of sample rectangles
        final int xStep = width / scale;
        final int yStep = height / scale;

        // center of the first rectangle
        final int xInit = xStep / 2;
        final int yInit = yStep / 2;

        // center of the last rectangle
        final int xEnd = width - xStep / 2;
        final int yEnd = height - yStep / 2;

        int totalTransparent = 0;

        for (int x = xInit; x <= xEnd; x += xStep)
        {
            for (int y = yInit; y <= yEnd; y += yStep)
            {
                if (bm.getPixel(x, y) == Color.TRANSPARENT)
                {
                    totalTransparent++;
                }
            }
        }
        return ((float) totalTransparent) / (scale * scale);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public class ClassB {
        Context context;

        public ClassB(Context context) {
            this.context = context;
        }

        /*public void Update() {
            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            final ImageView star = (ImageView) ((Activity) context).findViewById(R.id.star);
            final ImageView star2 = (ImageView) ((Activity) context).findViewById(R.id.star2);
            final ImageView star3 = (ImageView) ((Activity) context).findViewById(R.id.star3);
            final ImageView star4 = (ImageView) ((Activity) context).findViewById(R.id.star4);
            final ImageView star5 = (ImageView) ((Activity) context).findViewById(R.id.star5);
            final View view = (View) ((Activity) context).findViewById(R.id.view);
            TextView prompt = (TextView) ((Activity) context).findViewById(R.id.scratch_text);
            TextView promptSub = (TextView) ((Activity) context).findViewById(R.id.scratch_sub);

            prompt.setText("Winner!");
            promptSub.setVisibility(View.VISIBLE);

            view.startAnimation(shake);




            if (!isShown) {
                star.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);


                ObjectAnimator anim = ObjectAnimator.ofFloat(star, "translationY", -600);
                anim.setDuration(500);
                anim.start();

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(star, "translationX", -600);
                anim2.setDuration(500);
                anim2.start();
                anim2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation fade = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                        star.startAnimation(fade);
                        star.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                ObjectAnimator anim3 = ObjectAnimator.ofFloat(star2, "translationY", -600);
                anim3.setDuration(500);
                anim3.start();

                ObjectAnimator anim4 = ObjectAnimator.ofFloat(star2, "translationX", 600);
                anim4.setDuration(500);
                anim4.start();
                anim4.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation fade = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                        star2.startAnimation(fade);
                        star2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                ObjectAnimator anim5 = ObjectAnimator.ofFloat(star3, "translationY", -600);
                anim5.setDuration(500);
                anim5.start();
                anim5.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation fade = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                        star3.startAnimation(fade);
                        star3.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                ObjectAnimator anim6 = ObjectAnimator.ofFloat(star4, "translationY", -300);
                anim6.setDuration(500);
                anim6.start();

                ObjectAnimator anim7 = ObjectAnimator.ofFloat(star4, "translationX", -300);
                anim7.setDuration(500);
                anim7.start();
                anim7.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation fade = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                        star4.startAnimation(fade);
                        star4.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                ObjectAnimator anim8 = ObjectAnimator.ofFloat(star5, "translationY", -300);
                anim8.setDuration(500);
                anim8.start();

                ObjectAnimator anim9 = ObjectAnimator.ofFloat(star5, "translationX", 300);
                anim9.setDuration(500);
                anim9.start();
                anim9.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star5.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation fade = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
                        star5.startAnimation(fade);
                        star5.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                isShown = true;
            }
            else{
                //do nothing
            }
        }*/
    }

}