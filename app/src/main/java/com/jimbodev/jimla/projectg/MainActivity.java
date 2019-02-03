package com.jimbodev.jimla.projectg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import java.util.HashMap;

public class MainActivity extends Activity {

    private Game gameView;

    Switch switch1;

    public static HashMap<B, Integer> buttonsPressed = new HashMap<>();

    public enum B {
        START_BUTTON, CLEAR_BUTTON, RESTART_BUTTON, SEEK_BAR, SWITCH
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.surfaceView);

        setupButtons();
    }

    private void setupButtons() {

        Button startButton = findViewById(R.id.button);
        Button clearButton = findViewById(R.id.button2);
        Button restartButton = findViewById(R.id.button3);

        SeekBar nodeSizeBar = findViewById(R.id.seekBar);
        buttonsPressed.put(B.SEEK_BAR, 50);

        switch1 = findViewById(R.id.switch1);
        buttonsPressed.put(B.SWITCH, 1);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsPressed.put(B.START_BUTTON, 1);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsPressed.put(B.CLEAR_BUTTON, 1);
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsPressed.put(B.RESTART_BUTTON, 1);
            }
        });

        nodeSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                buttonsPressed.put(B.SEEK_BAR, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onVisualizeSwitchClick(View view) {
        if (switch1.isChecked())
            buttonsPressed.put(B.SWITCH, 1);
        else
            buttonsPressed.put(B.SWITCH, 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}