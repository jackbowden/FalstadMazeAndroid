package edu.wm.cs.amazebyjackandzeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class SettingsActivity extends AppCompatActivity {

    Spinner languageSpinner;
    Button backButton;

    /**
     * Load the view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageSpinner = findViewById(R.id.language_select_spinner);
        setUpSpinnerAdapter(languageSpinner, R.array.languages);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = adapterView.getItemAtPosition(i).toString();
                if (language.equals("English")) {
                    setLocale("en");
                } else if (language.equals("Deutsch")) {
                    setLocale("de");
                } else if (language.equals("español")) {
                    setLocale("es");
                } else if (language.equals("Polski")) {
                    setLocale("pl");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backButton = findViewById(R.id.back_button);
        final Intent backToStartIntent = new Intent(this, AMazeActivity.class);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backToStartIntent);
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

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
