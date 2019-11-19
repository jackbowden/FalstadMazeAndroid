package edu.wm.cs.amazebyjackandzeal.maze;

import edu.wm.cs.amazebyjackandzeal.generation.Distance;

public abstract class AutomaticDriver implements Driver {
    protected Robot robot;
    protected int width;
    protected int height;
    protected Distance distance;

    private float initialRobotBatteryLevel;

    public AutomaticDriver(Robot robot) {
        this.robot = robot;
        this.initialRobotBatteryLevel = robot.getBatteryLevel();
    }

    /**
     * Set the dimensions of the maze this driver is driving in.
     * @author jabowden and tcmzeal
     * @param width The width of the maze.
     * @param height The height of the maze.
     */
    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Set the Distance object that corresponds to the maze this driver is driving in.
     * @author jabowden and tcmzeal
     * @param distance The Distances object that corresponds to the maze this driver is driving in.
     */
    @Override
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     * An abstract method that takes the robot and drives it out of the maze exit.
     * @author jabowden and tcmzeal
     * @return whether the robot was successfully able to exit the maze
     */
    @Override
    public abstract boolean drive2Exit() throws Exception;

    /**
     * Returns the energy consumption of the robot, based on comparing its current battery level to the one recorded at the start.
     * @author jabowden and tcmzeal
     * @return the robot's energy consumption
     */
    @Override
    public float getEnergyConsumption() {
        return this.initialRobotBatteryLevel - robot.getBatteryLevel();
    }

    /**
     * Returns the odometer reading of the robot.
     * @author jabowden and tcmzeal
     * @return odometer reading of the robot
     */
    @Override
    public int getPathLength() {
        return robot.getOdometerReading();
    }
}