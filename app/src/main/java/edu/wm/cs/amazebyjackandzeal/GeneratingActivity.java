package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import edu.wm.cs.amazebyjackandzeal.generation.MazeConfiguration;
import edu.wm.cs.amazebyjackandzeal.generation.MazeFactory;
import edu.wm.cs.amazebyjackandzeal.generation.Order.Builder;
import edu.wm.cs.amazebyjackandzeal.maze.GenerationOrder;

public class GeneratingActivity extends AppCompatActivity {
    private static final String TAG = "GeneratingActivity";

    ProgressBar progressBar;
    Button backButton;

    /* needs to be accessed in startGame() */
    private String mostRecentDriver;

    public static MazeConfiguration mazeConfiguration;
    private AsyncTask generateMazeTask;

    /**
     * Load the view, taking into account settings from the bundle.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.v(TAG,"Unable to receive generation specifications. Finishing.");
            finish();
        }

        String difficulty = bundle.getString(getString(R.string.intent_key_difficulty), getResources().getStringArray(R.array.title_screen_difficulty_list)[0]);
        String mazeAlgo = bundle.getString(getString(R.string.intent_key_maze_algo), getResources().getStringArray(R.array.title_screen_maze_algo_list)[0]);
        String driver = bundle.getString(getString(R.string.intent_key_driver), getResources().getStringArray(R.array.title_screen_driver_list)[0]);

        Toast.makeText(getApplicationContext(), "Difficulty: " + difficulty + "\nMaze algorithm: " + mazeAlgo + "\nDriver: " + driver, Toast.LENGTH_SHORT).show();
        Log.v(TAG, difficulty);
        Log.v(TAG, mazeAlgo);
        Log.v(TAG, driver);

        this.mostRecentDriver = driver;

        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.button_back);

        final Handler handler = new Handler(Looper.getMainLooper());

        final Intent backToStartIntent = new Intent(this, AMazeActivity.class);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null); /* null = remove all */
                startActivity(backToStartIntent);
                finish();
            }
        });

        int skillLevel = Integer.parseInt(difficulty);

        Builder builder = Builder.DFS;
        if (mazeAlgo.equals("Prim's algorithm")) {
            builder = Builder.Prim;
        }
        if (mazeAlgo.equals("Eller's algorithm")) {
            builder = Builder.Eller;
        }

        final GenerationOrder order = new GenerationOrder(skillLevel, builder, false, this);
        generateMazeTask = new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                MazeFactory factory = new MazeFactory();
                factory.order(order); /* <- this eventually calls deliver(), which in turn starts the next activity */
                factory.waitTillDelivered();
                return "Finished!";
            }
        };

        generateMazeTask.execute();
    }

    public void setProgressBar(int progress) {
        if (progressBar == null) {
            return;
        }

        progressBar.setProgress(progress);
    }

    public void startGame(MazeConfiguration mazeConfiguration) {

        this.mazeConfiguration = mazeConfiguration;
        Intent playActivityIntent = new Intent(this, PlayActivity.class);

        if (this.mostRecentDriver.equals("Manual play")) {
            playActivityIntent.putExtra(getString(R.string.is_manual_key), true);
        } else {
            playActivityIntent.putExtra(getString(R.string.is_manual_key), false);
        }

        playActivityIntent.putExtra(getString(R.string.driver_key), this.mostRecentDriver);

        startActivity(playActivityIntent);
        finish();
    }

    /**
     * Return to the start screen when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        generateMazeTask.cancel(true);
        final Intent quitGameIntent = new Intent(this, AMazeActivity.class);
        startActivity(quitGameIntent);
        finish();
    }
}
