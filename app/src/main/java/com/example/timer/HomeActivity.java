package com.example.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private EditText hoursInput, minutesInput, secondsInput;
    private TextView timerTextView;
    private Button startButton, pauseButton, resetButton, soundSettingsButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeInMillis; // Total time in milliseconds
    private long timeLeftInMillis; // Remaining time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_screen);

        // Find views by ID
        timerTextView = findViewById(R.id.timerTextView);
        hoursInput = findViewById(R.id.hoursInput);
        minutesInput = findViewById(R.id.minutesInput);
        secondsInput = findViewById(R.id.secondsInput);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        soundSettingsButton = findViewById(R.id.soundSettingsButton);

        // Start button logic
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                }
            }
        });

        // Pause button logic
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                }
            }
        });

        // Reset button logic
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        // Sound Settings button logic
        soundSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SoundSettingsActivity
                Intent intent = new Intent(HomeActivity.this, SoundSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to start the timer
    private void startTimer() {
        int hours = getIntFromEditText(hoursInput);
        int minutes = getIntFromEditText(minutesInput);
        int seconds = getIntFromEditText(secondsInput);

        if (hours == 0 && minutes == 0 && seconds == 0) {
            Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
        timeLeftInMillis = timeInMillis;

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timerTextView.setText("00:00:00");
                Toast.makeText(HomeActivity.this, "Timer finished", Toast.LENGTH_SHORT).show();
                // TODO: Play sound (handle sound based on user preference)
            }
        }.start();

        isTimerRunning = true;
    }

    // Method to pause the timer
    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
    }

    // Method to reset the timer
    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        timeLeftInMillis = 0;
        updateTimerDisplay();
    }

    // Method to update the timer display
    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }


    // Utility method to get integer value from EditText
    private int getIntFromEditText(EditText editText) {
        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(input);
    }
}
