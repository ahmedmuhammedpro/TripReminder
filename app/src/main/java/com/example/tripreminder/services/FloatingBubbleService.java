package com.example.tripreminder.services;


import android.app.IntentService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.view.activities.MainActivity;

public class FloatingBubbleService extends IntentService {

    private static final double SMOOTH_X_FACTOR = 1.3;
    private static final double SMOOTH_Y_FACTOR = 1.2;

    private View floatingBubbleView ;
    private WindowManager windowManager;
     RelativeLayout layout;
    public FloatingBubbleService() {
        super("FloatingBubbleService");
    }


    private void handleStart() {
        floatingBubbleView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.floating_bubble_layout, null);
        layout  = floatingBubbleView.findViewById(R.id.floating_bubble_root);
        handleFloatingBubble();

    }

    private void handleFloatingBubble() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

       windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(floatingBubbleView, params);
        }
        // Set the close button.
        ImageView closeButton = (ImageView) floatingBubbleView.findViewById(R.id.close_button_bubble);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        // set the image click
        ImageView floatingHeadImage = (ImageView) floatingBubbleView.findViewById(R.id.floating_head_image);
        floatingHeadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("clicked", "click registered!");
                Intent youtubeIntent =
                        getPackageManager().getLaunchIntentForPackage(getPackageName());
                if (youtubeIntent != null) {
                    startActivity(youtubeIntent);
                }
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            int lastAction;
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.d("clicked", "touch down");
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;
                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d("clicked", "touch up");
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_POINTER_DOWN) {
                            Intent intent = new Intent(FloatingBubbleService.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //close the service and remove the chat heads
                            stopSelf();
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("clicked", "touch move");
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(floatingBubbleView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void onHandleIntent( Intent intent) {
        Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                handleStart();
            }
        });

    }
}
