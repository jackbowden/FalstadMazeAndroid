package edu.wm.cs.amazebyjackandzeal.maze;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import edu.wm.cs.amazebyjackandzeal.GeneratingActivity;
import edu.wm.cs.amazebyjackandzeal.PlayActivity;
import edu.wm.cs.amazebyjackandzeal.R;
import edu.wm.cs.amazebyjackandzeal.generation.MazeConfiguration;
import edu.wm.cs.amazebyjackandzeal.generation.MazeFactory;
import edu.wm.cs.amazebyjackandzeal.generation.Order;

/**
 * Created by Zeal on 12/9/2017.
 */

public class GenerationOrder implements Order {
    private int skillLevel;
    private Builder builder;
    private boolean isPerfect;
    private GeneratingActivity generatingActivity;

    private MazeFactory factory;

    public GenerationOrder(int skillLevel, Builder builder, boolean isPerfect, GeneratingActivity generatingActivity) {
        this.skillLevel = skillLevel;
        this.builder = builder;
        this.isPerfect = isPerfect;

        this.generatingActivity = generatingActivity;

        this.factory = new MazeFactory();
    }

    public int getSkillLevel() {
        return this.skillLevel;
    }

    public Builder getBuilder() {
        return this.builder;
    }

    public boolean isPerfect() {
        return this.isPerfect;
    }

    public void deliver(MazeConfiguration mazeConfiguration) {
        /* receives a mazeConfiguration from MazeBuilder and decides what to do with it */
        generatingActivity.startGame(mazeConfiguration);
    }

    public void updateProgress(int percentage) {
        if (percentage > 100) {
            percentage = 100;
            /* Falstad screwed up and occasionally this will be called with an argument > 100 */
        }

        generatingActivity.setProgressBar(percentage);
    }
}
