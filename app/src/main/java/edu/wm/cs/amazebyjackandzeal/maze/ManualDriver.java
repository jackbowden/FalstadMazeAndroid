package edu.wm.cs.amazebyjackandzeal.maze;

import edu.wm.cs.amazebyjackandzeal.maze.Robot.Turn;
import edu.wm.cs.amazebyjackandzeal.generation.Distance;

public class ManualDriver implements Driver {
    private Robot robot;
    private float initialRobotBatteryLevel;

    public ManualDriver(Robot robot) {
        this.robot = robot;
        this.initialRobotBatteryLevel = robot.getBatteryLevel();
    }

    public void keyUp() {
        this.robot.move(1, true);
    }

    public void keyDown() {
        this.robot.move(-1, true);
    }

    public void keyLeft() {
        this.robot.rotate(Turn.LEFT);
    }

    public void keyRight() {
        this.robot.rotate(Turn.RIGHT);
    }

    @Override
    public void setDimensions(int width, int height) {
		/*         _
		 __      _| |__   ___     ___ __ _ _ __ ___  ___
		 \ \ /\ / / '_ \ / _ \   / __/ _` | '__/ _ \/ __|
		  \ V  V /| | | | (_) | | (_| (_| | | |  __/\__ \
		   \_/\_/ |_| |_|\___/   \___\__,_|_|  \___||___/ */
    }

    @Override
    public void setDistance(Distance distance) {
		/*         _
		 __      _| |__   ___     ___ __ _ _ __ ___  ___
		 \ \ /\ / / '_ \ / _ \   / __/ _` | '__/ _ \/ __|
		  \ V  V /| | | | (_) | | (_| (_| | | |  __/\__ \
		   \_/\_/ |_| |_|\___/   \___\__,_|_|  \___||___/ */
    }

    @Override
    public boolean drive2Exit() throws Exception {
        return false;
    }

    @Override
    public float getEnergyConsumption() {
        return initialRobotBatteryLevel - robot.getBatteryLevel();
    }

    @Override
    public int getPathLength() {
        return robot.getOdometerReading();
    }

}