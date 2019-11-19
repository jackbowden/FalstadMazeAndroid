package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.wm.cs.amazebyjackandzeal.generation.MazeConfiguration;
import edu.wm.cs.amazebyjackandzeal.maze.BasicRobot;
import edu.wm.cs.amazebyjackandzeal.maze.Constants;
import edu.wm.cs.amazebyjackandzeal.maze.Controller;
import edu.wm.cs.amazebyjackandzeal.maze.Driver;
import edu.wm.cs.amazebyjackandzeal.maze.FirstPersonDrawer;
import edu.wm.cs.amazebyjackandzeal.maze.ManualDriver;
import edu.wm.cs.amazebyjackandzeal.maze.MazePanel;
import edu.wm.cs.amazebyjackandzeal.maze.Robot;
import edu.wm.cs.amazebyjackandzeal.maze.WallFollower;

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "MazeActivity";

    boolean isManual;
    ImageButton button_up;
    ImageButton button_down;
    ImageButton button_left;
    ImageButton button_right;
    Button button_start_pause;

    boolean isStarted = true;
    boolean areVisitedPortionsShown = false;
    boolean isWholeMazeShown = false;
    boolean isSolutionShown = false;

    MazeConfiguration mazeConfiguration;
    Controller controller;
    Robot robot;
    Driver driver;

    /**
     * Load the view, taking into account whether the robot driver is manual or not from the bundle.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        mazeConfiguration = GeneratingActivity.mazeConfiguration;
        GeneratingActivity.mazeConfiguration = null;

        if (bundle == null) {
            isManual = true;
        } else {
            isManual = bundle.getBoolean(getString(R.string.is_manual_key));
        }

        if (isManual) {
            setContentView(R.layout.activity_maze_w_controls);
        } else {
            setContentView(R.layout.activity_maze_no_controls);
        }

        MazePanel panel = findViewById(R.id.maze_panel);
        controller = new Controller(getApplicationContext(), mazeConfiguration, panel);
        robot = new BasicRobot(controller);

        if (bundle == null) {
            driver = new ManualDriver(robot);
        } else {
            String driverString = bundle.getString(getString(R.string.driver_key));
            if (driverString.equals("Manual play")) {
                driver = new ManualDriver(robot);
            } else if (driverString.equals("Wizard")) {

            } else if (driverString.equals("Wall follower")) {
                driver = new WallFollower(robot);
            } else if (driverString.equals("Pledge's algorithm")) {

            } else if (driverString.equals("Explorer")) {

            } else {
                driver = new ManualDriver(robot);
            }
        }

        controller.notifyViewerRedraw();

        if (isManual) {
            button_up = findViewById(R.id.button_up);
            button_down = findViewById(R.id.button_down);
            button_left = findViewById(R.id.button_left);
            button_right = findViewById(R.id.button_right);

            final ManualDriver manualDriver = (ManualDriver) driver;

            button_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualDriver.keyUp();
                    controller.notifyViewerRedraw();
                    Log.v(TAG, "Up");
                }
            });

            button_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualDriver.keyDown();
                    controller.notifyViewerRedraw();
                    Log.v(TAG, "Down");
                }
            });

            button_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualDriver.keyLeft();
                    Log.v(TAG, "Left");
                }
            });

            button_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualDriver.keyRight();
                    Log.v(TAG, "Right");
                }
            });
        } else {
            button_start_pause = findViewById(R.id.button_start_pause);
            button_start_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isStarted) {
                        Toast.makeText(getApplicationContext(), "Pausing", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Pausing");
                    } else {
                        Toast.makeText(getApplicationContext(), "Starting", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Starting");
                    }
                    isStarted = !isStarted;

                    if (isStarted) {
                        button_start_pause.setText(R.string.maze_pause);
                    } else {
                        button_start_pause.setText(R.string.maze_start);
                    }
                }
            });
        }
    }

    /**
     * Inflate the menu with menu options.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maze_menu, menu);
        return true;
    }

    /**
     * Assign actions to each menu item. Either toggle a maze visualization or quit the game.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_visited_portions:
                if (areVisitedPortionsShown) {
                    Toast.makeText(getApplicationContext(), "Hiding visited portions!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Hiding visited portions!");
                } else {
                    Toast.makeText(getApplicationContext(), "Showing visited portions!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Showing visited portions!");
                }
                areVisitedPortionsShown = !areVisitedPortionsShown;
                break;
            case R.id.action_show_whole_map:
                if (isWholeMazeShown) {
                    Toast.makeText(getApplicationContext(), "Hiding whole map!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Hiding whole map!");
                } else {
                    Toast.makeText(getApplicationContext(), "Showing whole map!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Showing whole map!");
                }
                isWholeMazeShown = !isWholeMazeShown;
                break;
            case R.id.action_show_solution:
                if (isSolutionShown) {
                    Toast.makeText(getApplicationContext(), "Hiding solution!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Hiding solution!");
                } else {
                    Toast.makeText(getApplicationContext(), "Showing solution!", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Showing solution!");
                }
                isSolutionShown = !isSolutionShown;
                break;
            case R.id.action_open_app_store_for_maze:
                Intent openAppStoreIntent = new Intent(Intent.ACTION_VIEW);
                openAppStoreIntent.setData(Uri.parse("market://search?q=maze"));
                Intent openAppStoreInBrowserIntent = new Intent(Intent.ACTION_VIEW);
                openAppStoreIntent.setData(Uri.parse("http://play.google.com/store/search?q=maze"));
                try {
                    startActivity(openAppStoreIntent);
                } catch (Exception e) {
                    startActivity(openAppStoreInBrowserIntent);
                }
                break;
            case R.id.action_quit_game:
                Intent quitGameIntent = new Intent(this, WastedActivity.class);{
                startActivity(quitGameIntent);
                finish();
                break;
            }
        }
        return true;
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
