package com.example.accessibilitytool;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;
//import androidx.test.espresso.contrib.AccessibilityChecks;

public class AccessibilityTool extends AccessibilityService {
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
}
