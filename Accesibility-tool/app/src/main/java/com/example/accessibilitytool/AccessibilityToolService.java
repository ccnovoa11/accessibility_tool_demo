package com.example.accessibilitytool;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import androidx.test.espresso.contrib.AccessibilityChecks;

public class AccessibilityToolService extends AccessibilityService {
    FrameLayout mLayout;

    @Override
    protected void onServiceConnected() {
        // Create an overlay and display the action bar
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.action_bar, mLayout);
        wm.addView(mLayout, lp);
        configureStartButton();
        configureStopButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("WARDIOLA", event.toString());

        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                try{
                    Log.d("WARDIOLA2", String.valueOf(event.getSource()));
                }catch (Exception e){
                    Log.d("WARDIOLA2", e.getMessage());
                }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void configureStartButton() {
        Button startButton = (Button) mLayout.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Commands commands = new Commands();
                try {
                    commands.showDevicesWithADB();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Start button",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void configureStopButton() {
        Button stopButton = (Button) mLayout.findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Stop button",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
