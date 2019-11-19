package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AMazeActivity extends AppCompatActivity {
    Spinner difficultySpinner;
    Spinner mazeAlgoSpinner;
    Spinner driverSpinner;
    Button newMazeButton;
    Button loadMazeButton;
    Button settingsButton;

    /**
     * Load the view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent goToGeneratingScreenIntent = new Intent(this, GeneratingActivity.class);

                difficultySpinner = findViewById(R.id.difficulty_spinner);
                setUpSpinnerAdapter(difficultySpinner, R.array.title_screen_difficulty_list);

                mazeAlgoSpinner = findViewById(R.id.maze_algo_spinner);
                setUpSpinnerAdapter(mazeAlgoSpinner, R.array.title_screen_maze_algo_list);

                driverSpinner = findViewById(R.id.driver_spinner);
                setUpSpinnerAdapter(driverSpinner, R.array.title_screen_driver_list);

                newMazeButton = findViewById(R.id.new_maze_button);
                newMazeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String difficulty = difficultySpinner.getSelectedItem().toString();
                        String mazeAlgo = mazeAlgoSpinner.getSelectedItem().toString();
                        String driver = driverSpinner.getSelectedItem().toString();

                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_difficulty), difficulty);
                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_maze_algo), mazeAlgo);
                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_driver), driver);

                        startActivity(goToGeneratingScreenIntent);
                        finish();
                    }
                });

                loadMazeButton = findViewById(R.id.load_maze_button);
                loadMazeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String difficulty = difficultySpinner.getSelectedItem().toString();
                        String mazeAlgo = mazeAlgoSpinner.getSelectedItem().toString();
                        String driver = driverSpinner.getSelectedItem().toString();

                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_difficulty), difficulty);
                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_maze_algo), mazeAlgo);
                        goToGeneratingScreenIntent.putExtra(getString(R.string.intent_key_driver), driver);

                        startActivity(goToGeneratingScreenIntent);
                        finish();
                    }
                });

                settingsButton = findViewById(R.id.settings_button);
                final Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(goToSettingsIntent);
                        finish();
                    }
                });
    }

    /**
     * Private helper method to load a spinner with an array of strings.
     * @param spinner Spinner object.
     * @param stringsResource R id of a string array.
     */
    private void setUpSpinnerAdapter(Spinner spinner, int stringsResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, stringsResource, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
