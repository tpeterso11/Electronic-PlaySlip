package com.shoutz.toussaintpeterson.electronicpayslip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import models.Ticket;
import models.User;

/**
 * Created by toussaintpeterson on 11/18/15.
 */

public class TouchEventView extends SurfaceView implements Animation.AnimationListener{
    private Paint mPaint;
    private SurfaceHolder holder;
    //private Bitmap mutableBitmap = Bitmap.createBitmap(erasableBitmap.getWidth(), erasableBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    private Bitmap loadedBitmap;
    private Bitmap drawableBitmap;
    private Canvas mCanvas;
//    Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    private boolean isShown;
    private Bitmap cover;
    private Bitmap  mBitmap;
    private Bitmap bitmap;
    private Paint paint = new Paint();
    private Path path = new Path();
    private Boolean isEmpty;
    private EPSApplication application;
    private ArrayList<ArrayList> masterArray;
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private User user;
    private JSONObject response;
    private byte[] byteArray;
    //private Point point = new Point
    //();

    public TouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        mCanvas = new Canvas();
        isEmpty = true;

        /*masterArray = getEpsApplication().masterArray;

        if(getEpsApplication().getUser() != null) {
            user = getEpsApplication().getUser();
        }*/

        //this.setBackgroundResource(R.drawable.modern_tag_white);
        this.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        mCanvas = canvas;
        canvas.drawPath(path, paint);
        //canvas.drawCircle((float) point.x, (float) point.y, 100, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                // There is no end point yet, so don't waste cycles invalidating.
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                ClassB newOne = new ClassB(getContext());
                //newOne.Update();
                // Start tracking the dirty region.
                //resetDirtyRect(eventX, eventY);

                // When the hardware tracks events faster than they are delivered, the
                // event will contain a history of those skipped points.
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    //expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }

                // After replaying history, connect the line to the touch point.
                path.lineTo(eventX, eventY);
                break;

            default:
                return false;
        }

        // Include half the stroke width to avoid clipping.
        invalidate(
                (int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
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
            final Button save = (Button) ((Activity) context).findViewById(R.id.save_sig);
            final Button discard = (Button) ((Activity) context).findViewById(R.id.discard_sig);
            final Button clear = (Button) ((Activity) context).findViewById(R.id.clear_sig);
            final TouchEventView sig = (TouchEventView) ((Activity) context).findViewById(R.id.sign_field);
            final ImageView signature = (ImageView)((Activity) context).findViewById(R.id.signature_image);
            final Button confirm = (Button)((Activity) context).findViewById(R.id.checkout);
            final RelativeLayout sign = (RelativeLayout)((Activity) context).findViewById(R.id.sign_layout);
            final RelativeLayout loading = (RelativeLayout)((Activity) context).findViewById(R.id.loading);
            final TextView confTag = (TextView)((Activity) context).findViewById(R.id.confirmation_tag);
            final TextView confNum = (TextView)((Activity) context).findViewById(R.id.confirmation_num);
            final FloatingActionButton send = (FloatingActionButton)((Activity) context).findViewById(R.id.send_button);
            final Button redeem = (Button)((Activity) context).findViewById(R.id.redeem_button);
            final Button redeemLater = (Button)((Activity) context).findViewById(R.id.redeem_later_button);
            final RelativeLayout instructions = (RelativeLayout)((Activity) context).findViewById(R.id.instructions_vendor);
            final ImageView line = (ImageView)((Activity) context).findViewById(R.id.line);
            final TextView shoppingInstructions = (TextView)((Activity) context).findViewById(R.id.shopping_instructions);
            //final RelativeLayout finalize = (RelativeLayout)((Activity) context).findViewById(R.id.finalize);
            //save.startAnimation(shake);
            clear.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    sig.clear();
                }
            });

            save.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    saveSignature();
                    Animation in = AnimationUtils.loadAnimation(((Activity) context), R.anim.abc_slide_in_bottom);

                    shoppingInstructions.setVisibility(View.GONE);
                    line.startAnimation(in);
                    line.setVisibility(View.VISIBLE);

                    bitmap = BitmapFactory.decodeByteArray(saveSignature(), 0, saveSignature().length);

                    signature.setImageBitmap(bitmap);
                    signature.startAnimation(in);
                    signature.setVisibility(View.VISIBLE);

                    ObjectAnimator anim = ObjectAnimator.ofFloat(sign, "translationY", 0);
                    anim.start();

                    ObjectAnimator move = ObjectAnimator.ofFloat(confirm, "translationY", 100);
                    move.start();
                    confirm.setText(R.string.continue_button);
                    confirm.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View view) {
                                    Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                                    loading.startAnimation(in);
                                    loading.setVisibility(View.VISIBLE);

                                    confirm.setEnabled(false);
                                    if (user != null) {
                                        if (!(masterArray.isEmpty())) {
                                            for (ArrayList<Ticket> array : masterArray) {
                                                for (int i = 0; i < array.size(); i++) {
                                                    if(array.get(i).getGameName() == null){
                                                        array.get(i).setUserName(user.getPhoneNumber());
                                                    }
                                                    else if (array.get(i).getUserName().equals("null")) {
                                                        array.get(i).setUserName(user.getPhoneNumber());
                                                    }
                                                }
                                            }
                                        }
                                        //if(masterArray.size() > 1){
                                            new BatchTask(getResources().getString(R.string.create_batch), masterArray, context).execute();
                                        //}
                                        //else {
                                        //    new PostTicketTask(getResources().getString(R.string.post_ticket_service), masterArray, null, context).execute();
                                        //}
                                    }
                                }
                        });
                    }
                });

            discard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(sign, "translationY", 0);
                    anim.start();

                    if(!(sig.isEmpty)) {
                        sig.clear();
                    }
                }
            });
            //final View view = (View) ((Activity) context).findViewById(R.id.view);
            //view.startAnimation(shake);

        }
    }*/

    /*private void resetDirtyRect(float eventX, float eventY) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }


    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }*/

    public void clear() {
        path = new Path();
        invalidate();
    }

    /*public byte[] saveSignature(){

        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();

        bitmap.recycle();
        //String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //File file = new File(Environment.getExternalStorageDirectory() + "/sign.png");

        /*try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArray;
    }*/

    /*public static Bitmap decodeSampledBitmapFromResource(Resources res, int id, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeResource(res, id, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public class PostTicketTask extends AbstractWebService {
        private ArrayList<ArrayList> masterArray;
        private String batch;

        public PostTicketTask(String urlPath, ArrayList<ArrayList> masterArray, String batch, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.masterArray = masterArray;
            this.batch = batch;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            final ImageView signature = (ImageView)((Activity) context).findViewById(R.id.signature_image);
            signature.setDrawingCacheEnabled(true);
            signature.buildDrawingCache();
            Bitmap b = signature.getDrawingCache();

            try {
                JSONObject answer = new JSONObject(response.toString());
                String confirmation = answer.getString("batchid");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] byteArray = stream.toByteArray();

                bitmap.recycle();

                Intent i = new Intent(getContext(), FinalizeActivity.class);
                i.putExtra("signature", byteArray);
                i.putExtra("confirmation", confirmation);
                context.startActivity(i);
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            Button confirm = (Button)((Activity) context).findViewById(R.id.checkout);
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_LONG).show();
            RelativeLayout loading = (RelativeLayout)((Activity) context).findViewById(R.id.loading);
            Animation out = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            loading.startAnimation(out);
            loading.setVisibility(View.GONE);
            confirm.setEnabled(true);
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            for(ArrayList<Ticket> array : masterArray) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject params = new JSONObject();
                    params.put("gameid", array.get(i).getGameId());
                    params.put("betamount", array.get(i).getWager());
                    params.put("numbers", array.get(i).getNumbers());
                    params.put("authtoken", getEpsApplication().getUser().getAuthToken());
                    if(!(batch == null)){
                        params.put("batchid", batch);
                    }

                    String s = Base64.encodeToString(saveSignature(),
                            Base64.NO_WRAP);
                    params.put("signature", s);

                    response = doPost(params);
                }
            }

            return response; //@todo stop using handler and use onSuccess\Error
        }
    }

    public EPSApplication getEpsApplication() {
        if(application==null)
            application = (EPSApplication)getContext().getApplicationContext();

        return application;
    }

    private Bitmap returnImage() {
        Bitmap nad = bitmap;

        return nad;
    }

    public class BatchTask extends AbstractWebService {
        private ArrayList<ArrayList> masterArray;

        public BatchTask(String urlPath, ArrayList<ArrayList> masterArray, Context context){
            super(urlPath, false, false, context);
            this.urlPath = urlPath;
            this.masterArray = masterArray;


        }

        @Override
        protected void onFinish() {
        }

        @Override
        protected void onSuccess(Object response) {
            try {
                JSONObject responseObj = new JSONObject(response.toString());
                new PostTicketTask(getResources().getString(R.string.post_ticket_service), masterArray, responseObj.getString("batchid"), context).execute();
            }
            catch(JSONException ex){

            }
        }

        @Override
        protected void onError(Object response) {
            //Toast.makeText(ShoppingListViewActivity.this, "Fail", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doWebOperation() throws Exception {
            Map<String, String> param2 = new HashMap<String, String>();


            /*for(ArrayList<Ticket> array : masterArray) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject params = new JSONObject();
                    params.put("gameid", 1);
                    params.put("betamount", array.get(i).getWager());
                    params.put("numbers", array.get(i).getNumbers());

                    response = doPost(params);
                }
            }*/

            /*JSONObject params = new JSONObject();
            response = doPost(params);

            return response; //@todo stop using handler and use onSuccess\Error
        }*/
    }
}
