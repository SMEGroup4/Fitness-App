package com.easyfitness;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import gr.antoniom.chronometer.Chronometer;

// TODO: Get lapping time to log
// TODO: List of lapped times
// TODO: Reset clears list

public class ChronoDialogbox extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnStartStop, exit, btnResetLap;
    public Chronometer chrono;
    long startTime = 0;
    long stopTime = 0;

    private boolean chronoStarted = false;
    private boolean chronoResetted = true;

    public ChronoDialogbox(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(c.getResources().getString(R.string.ChronometerLabel));
        setContentView(R.layout.dialog_chrono);

        this.setCanceledOnTouchOutside(false); // Make it a modal

        btnStartStop = findViewById(R.id.btn_start_stop);
        btnResetLap = findViewById(R.id.btn_reset_lap);
        exit = findViewById(R.id.btn_exit);
        chrono = findViewById(R.id.chronoValue);

        btnStartStop.setOnClickListener(this);
        btnResetLap.setOnClickListener(this);
        exit.setOnClickListener(this);

        chrono.setBase(SystemClock.elapsedRealtime());
        startTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_stop:

                if (chronoStarted) {
                    chrono.stop();
                    stopTime = SystemClock.elapsedRealtime();
                    chronoStarted = false;

                    btnStartStop.setText("Start");
                    btnResetLap.setText("Reset");
                } else {
                    if (chronoResetted) {
                        startTime = SystemClock.elapsedRealtime();
                    } else {
                        startTime = SystemClock.elapsedRealtime() - (stopTime - startTime);
                    }

                    chrono.setBase(startTime);
                    chrono.start();
                    chronoStarted = true;

                    btnStartStop.setText("Stop");
                    btnResetLap.setText("Lap");
                }

                chronoResetted = false;

                break;
            case R.id.btn_reset_lap:

                if(chronoStarted) {
                    // Lap
                    Log.println(Log.INFO, "Tag", Long.toString(chrono.getBase()));
                } else {
                    startTime = SystemClock.elapsedRealtime();
                    chrono.setBase(startTime);
                    chrono.setText("00:00:00");
                    chronoResetted = true;
                }

                break;
            case R.id.btn_exit:

                chrono.stop();
                chronoStarted = false;
                chrono.setText("00:00:00");
                btnStartStop.setText("Start");

                dismiss();
                break;
            default:
                break;
        }
    }
}
