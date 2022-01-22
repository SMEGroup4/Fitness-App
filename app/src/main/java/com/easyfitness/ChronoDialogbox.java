package com.easyfitness;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import gr.antoniom.chronometer.Chronometer;

public class ChronoDialogbox extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnStartStop, exit, btnResetLap;
    public Chronometer chrono;

    long startTime = 0;
    long stopTime = 0;
    ArrayAdapter<String> lapTimerAdapter;
    private boolean chronoStarted = false;
    private boolean chronoResetted = true;
    private List<String> lapTimes;

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

        lapTimes = new ArrayList<>();
        lapTimerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lapTimes);
        ((ListView) findViewById(R.id.timer_lap_list)).setAdapter(lapTimerAdapter);
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

                if (chronoStarted) {
                    lapTimes.add(chrono.getFormattedText(SystemClock.elapsedRealtime() - startTime));
                } else {
                    startTime = SystemClock.elapsedRealtime();
                    chrono.setBase(startTime);
                    chrono.setText("00:00:00");
                    chronoResetted = true;

                    lapTimes.clear();
                }
                
                lapTimerAdapter.notifyDataSetChanged();

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
