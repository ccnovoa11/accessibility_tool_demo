package com.example.accessibilitytool;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
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
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import androidx.test.espresso.contrib.AccessibilityChecks;

public class AccessibilityToolService extends AccessibilityService {
    FrameLayout mLayout;
    FileOutputStream fOut;
    OutputStreamWriter osw;
    Boolean writeReport = false;

    static final String HTML = new String("<html>");
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

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(writeReport){
            switch (event.getEventType()){
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    try{
                        writeInFile(A11yNodeInfo.wrap(getRootInActiveWindow()).toViewHeirarchy());
                        Log.d("WARDIOLA2", A11yNodeInfo.wrap(getRootInActiveWindow()).toViewHeirarchy());

                    }catch (Exception e){
                        Log.d("WARDIOLA2", e.getMessage());
                    }
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
                writeReport = true;
                writeInFile("<html><body><h2>ACCESSIBILITY REPORT</h2>");
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
                writeReport = false;
                writeInFile("</body></html>");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Stop button",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void writeInFile(String append){
        try {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            fOut = new FileOutputStream(new File(getExternalFilesDir(null),"report.html"), true);
            osw = new OutputStreamWriter(fOut);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the string to the file
            osw.write(append);
            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
