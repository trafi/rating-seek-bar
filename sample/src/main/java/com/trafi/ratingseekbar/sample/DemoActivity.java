package com.trafi.ratingseekbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.trafi.ratingseekbar.RatingSeekBar;

public class DemoActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    TextView ratingLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ((RatingSeekBar) findViewById(R.id.rating_seek_bar_one)).setOnSeekBarChangeListener(this);
        ratingLabel = (TextView) findViewById(R.id.rating_label_one);

        RatingSeekBar ratingSeekBar = (RatingSeekBar) findViewById(R.id.rating_seek_bar_two);
        ratingSeekBar.setProgress(3);
        ratingSeekBar.setActive(true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        ratingLabel.setText("Rating " + progress + " / " + seekBar.getMax());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
