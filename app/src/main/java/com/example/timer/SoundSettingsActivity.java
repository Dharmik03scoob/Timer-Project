package com.example.timer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {

    private RadioGroup soundOptionsGroup;
    private Button previewButton, saveSoundButton;
    private MediaPlayer mediaPlayer;
    private int selectedSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_settings);

        soundOptionsGroup = findViewById(R.id.soundOptionsGroup);
        previewButton = findViewById(R.id.previewButton);
        saveSoundButton = findViewById(R.id.saveSoundButton);

        // Load previously selected sound from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("SoundPrefs", Context.MODE_PRIVATE);
        selectedSoundId = preferences.getInt("selectedSound", R.raw.sound1); // Default to Sound 1

        // Preview button logic
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = soundOptionsGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.soundOption1) {
                    playSound(R.raw.sound1);
                } else if (selectedId == R.id.soundOption2) {
                    playSound(R.raw.sound2);
                } else if (selectedId == R.id.soundOption3) {
                    playSound(R.raw.sound3);
                } else {
                    Toast.makeText(SoundSettingsActivity.this, "Select a sound to preview", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Save button logic
        saveSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = soundOptionsGroup.getCheckedRadioButtonId();
                SharedPreferences.Editor editor = preferences.edit();

                if (selectedId == R.id.soundOption1) {
                    selectedSoundId = R.raw.sound1;
                } else if (selectedId == R.id.soundOption2) {
                    selectedSoundId = R.raw.sound2;
                } else if (selectedId == R.id.soundOption3) {
                    selectedSoundId = R.raw.sound3;
                } else {
                    Toast.makeText(SoundSettingsActivity.this, "Please select a sound", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the selected sound in SharedPreferences
                editor.putInt("selectedSound", selectedSoundId);
                editor.apply();

                Toast.makeText(SoundSettingsActivity.this, "Sound saved", Toast.LENGTH_SHORT).show();

                // Return to TimerActivity
                finish();
            }
        });
    }

    private void playSound(int soundResource) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
