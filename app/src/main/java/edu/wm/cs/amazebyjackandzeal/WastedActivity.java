package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class WastedActivity extends AppCompatActivity {
    ImageView image;
    MediaPlayer mediaPlayer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasted);

        image = findViewById(R.id.wasted_image);
        image.setVisibility(View.INVISIBLE);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wasted);
        mediaPlayer.start();

        final Intent goToEndScreenIntent = new Intent(this, FinishActivity.class);
        goToEndScreenIntent.putExtra(getString(R.string.was_successful_key), false);

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                image.setVisibility(View.VISIBLE);
            }
        }, 2254);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(goToEndScreenIntent);
                finish();
            }
        }, 8000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        final Intent quitGameIntent = new Intent(this, AMazeActivity.class);
        startActivity(quitGameIntent);
        finish();
    }
}
