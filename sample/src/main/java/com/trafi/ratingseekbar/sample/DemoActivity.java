package com.trafi.ratingseekbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.trafi.ratingseekbar.RatingSeekBar;

public class DemoActivity extends AppCompatActivity implements RatingSeekBar.OnRatingSeekBarChangeListener {

    TextView ratingLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ((RatingSeekBar) findViewById(R.id.rating_seek_bar_one)).setOnSeekBarChangeListener(this);
        ratingLabel = (TextView) findViewById(R.id.rating_label_one);

        RatingSeekBar ratingSeekBar = (RatingSeekBar) findViewById(R.id.rating_seek_bar_two);
        ratingSeekBar.setProgress(3);
    }

    @Override
    public void onProgressChanged(RatingSeekBar ratingSeekBar, int progress) {
        ratingLabel.setText("Rating " + progress + " / " + ratingSeekBar.getMax());
    }
}
