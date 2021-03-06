package com.example.tripreminder.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.activities.MainActivity;
import com.google.android.material.button.MaterialButton;


public class FloatingBubbleService extends Service {

    private LayoutInflater liClose, liChatHeads;
    private ImageView ivCloseView;
    private View closeView;
    private View floatingBubbleView ;
    private WindowManager windowManager;
    RelativeLayout layout;
    String[] notes;

    private WindowManager.LayoutParams mCloseViewParams;

    public FloatingBubbleService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notes = intent.getStringArrayExtra(Constants.TRIP_NOTES_KEY);
       // Log.i("notes", "not: "+ notes);
        handleStart();
        return super.onStartCommand(intent, flags, startId);
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
        params.x = 20;
        params.y = 100;

       windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(floatingBubbleView, params);
        }
        final View collapsedView = floatingBubbleView.findViewById(R.id.collapse_view);
        final LinearLayout expandedView = floatingBubbleView.findViewById(R.id.expanded_container);
      if(notes != null && notes.length >0) {
          for (String note : notes) {
              LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                      LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
              CheckBox box = new CheckBox(this);
              box.setLayoutParams(lparams);
              box.setText(note);
              box.setPadding(5,5,5,5);
              //box.setBackground(getDrawable( R.drawable.custom_rounded_view_light));
              box.setTextSize(18);
              expandedView.addView(box);
          }
      }else{
          TextView tv = new TextView(this);
          tv.setText("No notes to show !");
         // tv.setBackground(getDrawable( R.drawable.custom_rounded_view_light));
          tv.setTextSize(14);
          expandedView.addView(tv);
      }
        // Set the close button.
        ImageView closeButton =  floatingBubbleView.findViewById(R.id.close_button_bubble);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        //expand view
        //Set the close button
        ImageView closeButtonExpand = (ImageView) floatingBubbleView.findViewById(R.id.close_button_expand);
        closeButtonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

      Button openButton = (Button) floatingBubbleView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
                onDestroy();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("action", "down ");
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d("action", "up");
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        //Update the layout with new X & Y coordinate
                      if(floatingBubbleView != null)
                        windowManager.updateViewLayout(floatingBubbleView, params);
                        return true;
                }
                return false;
            }
        });
    }
    private boolean isViewCollapsed() {
        return floatingBubbleView== null || floatingBubbleView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

@Override
public void onCreate() {
    super.onCreate();

    }
@Override
public void onDestroy() {
    super.onDestroy();
    if (floatingBubbleView != null) windowManager.removeView(floatingBubbleView);
}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
