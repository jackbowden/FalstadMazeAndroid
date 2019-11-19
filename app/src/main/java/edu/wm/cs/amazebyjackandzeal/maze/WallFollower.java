package edu.wm.cs.amazebyjackandzeal.maze;

import edu.wm.cs.amazebyjackandzeal.maze.Robot.Turn;

public class WallFollower extends AutomaticDriver {

    public WallFollower(Robot robot) {
        super(robot);
    }

    /**
     * Drives the robot out of the exit of the maze using a wall follower algorithm.
     * @author jabowden and tcmzeal
     * @return whether the robot was able to exit the maze
     */
    @Override
    public boolean drive2Exit() throws Exception {
        while (robot.isAtExit() == false) {
            boolean hasWallInFront = robot.hasWallInDirection(robot.getCurrentDirection());
            boolean hasWallToLeft = robot.hasWallInDirection(robot.getCurrentDirection().rotateClockwise().rotateClockwise().rotateClockwise());

            if (!hasWallInFront && !hasWallToLeft) {
                robot.rotate(Robot.Turn.LEFT);
            } else if (hasWallInFront && !hasWallToLeft) {
                robot.rotate(Turn.LEFT);
            } else if (hasWallInFront && hasWallToLeft) {
                robot.rotate(Turn.RIGHT);
            } /* else, if !hasWallinFront && hasWallToLeft, do nothing */

            if (robot.hasWallInDirection(robot.getCurrentDirection()) == false) {
                robot.move(1, false);
            }

            if (robot.hasStopped()) {
                return false;
            }
        }

        return robot.stepOutOfExit();
    }
}
