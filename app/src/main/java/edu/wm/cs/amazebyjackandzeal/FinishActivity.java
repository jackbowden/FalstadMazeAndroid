package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {

    private static final String TAG = "EndActivity";
    TextView messageTextView;
    Button backToStartButton;

    /**
     * Load the view, taking into account whether the robot finished successfully or not from the bundle.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        messageTextView = findViewById(R.id.end_game_message_text_view);
        backToStartButton = findViewById(R.id.button_back);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.v(TAG,"Unable to receive generation specifications. Finishing.");
            finish();
        }

        boolean wasSuccessful = bundle.getBoolean(getString(R.string.was_successful_key), true);

        if (wasSuccessful) {
            messageTextView.setText(R.string.end_screen_victory);
        } else {
            messageTextView.setText(R.string.end_screen_loss);
        }

        final Intent backToStartIntent = new Intent(this, FakeAdActivity.class);
        backToStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backToStartIntent);
                finish();
            }
        });
    }

    /**
     * Return to the start screen when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        final Intent quitGameIntent = new Intent(this, AMazeActivity.class);
        startActivity(quitGameIntent);
        finish();
    }
}
