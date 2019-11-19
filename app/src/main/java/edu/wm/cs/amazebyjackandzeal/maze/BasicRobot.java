package edu.wm.cs.amazebyjackandzeal.maze;

import edu.wm.cs.amazebyjackandzeal.generation.CardinalDirection;
import edu.wm.cs.amazebyjackandzeal.generation.Cells;
import edu.wm.cs.amazebyjackandzeal.generation.MazeConfiguration;

public class BasicRobot implements Robot {
    private static final float SENSOR_COST = 1;
    private static final float TURN_COST = 3;
    private static final float MOVE_FORWARD_COST = 5;
    private static final float DEFAULT_BATTERY_LEVEL = 3000;

    private static final String OUT_OF_BOUNDS_MESSAGE = "The robot is not in-bounds.";
    private static final String NOT_AT_EXIT_MESSAGE = "The robot is not at the exit.";

    private float batteryLevel;
    private int odometer;
    protected Controller mazeController;
    private CardinalDirection directionFacing;

    private boolean hasStopped;

    protected Cells cell;

    /**
     * Creates a BasicRobot and assigns a Controller that the robot is in.
     * @author jabowden and tcmzeal
     * @param mazeController Controller that robot is in
     */
    public BasicRobot(Controller mazeController) {
        this.batteryLevel = DEFAULT_BATTERY_LEVEL;
        this.odometer = 0;
        this.mazeController = mazeController;
        this.directionFacing = CardinalDirection.East;
        this.hasStopped = false;
    }

    @Override
    public void rotate(Turn turn) {
        if (hasStopped) {
            return;
        }
        if (turn == Turn.LEFT) {
            tryToRotate(1);
        } else if (turn == Turn.RIGHT) {
            tryToRotate(-1);
        } else if (turn == Turn.AROUND) {
            tryToRotate(1);
            tryToRotate(1);
        }
    }

    /**
     * Helper method called by rotate() that uses battery power, calls Controller.rotate(), and updates directionFacing.
     * @author jabowden and tcmzeal
     * @param direction Direction integer as defined in Controller. 1 = left; -1 = right
     */
    private void tryToRotate(int direction) {
        usePower(TURN_COST);
        mazeController.rotate(direction);
        if (direction == 1) {
			/* TODO add a rotateCounterclockwise() ? */
            this.directionFacing = this.directionFacing.rotateClockwise().rotateClockwise().rotateClockwise();
        } else if (direction == -1) {
            this.directionFacing = this.directionFacing.rotateClockwise();
        }
    }

    @Override
    public void move(int distance, boolean manual) {
        if (hasStopped) {
            return;
        }
        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();

        for (int i = 0; i < distance; i++) {
            usePower(SENSOR_COST);
            int[] pos = this.mazeController.getCurrentPosition();
            assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;
            int x = pos[0];
            int y = pos[1];

            if (!hasWall(x, y, directionFacing)) {
                usePower(MOVE_FORWARD_COST);
                mazeController.walk(1);
            } else if (!manual) {
                this.hasStopped = true;
                endGame();
            }

			/* check if the maze has been exited */
            pos = this.mazeController.getCurrentPosition();
            x = pos[0];
            y = pos[1];
            if (!mazeConfiguration.isValidPosition(x, y)) {
                endGame();
            }
        }

        this.odometer++;
    }

    @Override
    public int[] getCurrentPosition() throws Exception {
        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();
        int[] pos = this.mazeController.getCurrentPosition();
        int x = pos[0];
        int y = pos[1];

        if (!mazeConfiguration.isValidPosition(x, y)) {
            throw new Exception();
			/* TODO should we return a certain type of exception or just an Exception? */
        }

        return pos;
    }

    @Override
    public void setMaze(Controller mazeController) {
        this.mazeController = mazeController;
    }

    @Override
    public boolean isAtExit() {
        int[] pos = this.mazeController.getCurrentPosition();
        assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;
        int x = pos[0];
        int y = pos[1];
        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();


        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.North) && !mazeConfiguration.isValidPosition(x, y + 1)) {
            return true;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.East) && !mazeConfiguration.isValidPosition(x + 1, y)) {
            return true;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.West) && !mazeConfiguration.isValidPosition(x - 1, y)) {
            return true;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.South) && !mazeConfiguration.isValidPosition(x, y - 1)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canSeeExit(Direction direction)
            throws UnsupportedOperationException {
        usePower(SENSOR_COST);

        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();
        CardinalDirection cardinalDirection = getCardinalDirection(direction);

        int[] pos = this.mazeController.getCurrentPosition();
        assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;
        int x = pos[0];
        int y = pos[1];

        while (mazeConfiguration.isValidPosition(x, y)) {
            if (hasWall(x, y, cardinalDirection)) {
                return false;
            }

            switch(cardinalDirection) {
                case North:
                    y++;
                case East:
                    x++;
                case South:
                    y--;
                case West:
                    x--;
            }
        }

        return true;
    }

    @Override
    public boolean isInsideRoom() throws UnsupportedOperationException {
        int[] pos = this.mazeController.getCurrentPosition();
        assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;

        usePower(SENSOR_COST);
        return this.mazeController.getMazeConfiguration().getMazecells().isInRoom(pos[0], pos[1]);
    }

    @Override
    public boolean hasRoomSensor() {
        return true;
    }

    @Override
    public CardinalDirection getCurrentDirection() {
        return this.directionFacing;
    }

    @Override
    public float getBatteryLevel() {
        return this.batteryLevel;
    }

    @Override
    public void setBatteryLevel(float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public int getOdometerReading() {
        return this.odometer;
    }

    @Override
    public void resetOdometer() {
        this.odometer = 0;
    }

    @Override
    public float getEnergyForFullRotation() {
        return TURN_COST;
    }

    @Override
    public float getEnergyForStepForward() {
        return MOVE_FORWARD_COST;
    }

    @Override
    public boolean hasStopped() {
        return this.hasStopped;
    }

    @Override
    public int distanceToObstacle(Direction direction)
            throws UnsupportedOperationException {
        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();
        CardinalDirection cardinalDirection = getCardinalDirection(direction);
        System.out.println(cardinalDirection);
        int steps = 0;

        int[] pos = this.mazeController.getCurrentPosition();
        assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;
        int x = pos[0];
        int y = pos[1];

        usePower(SENSOR_COST);

        while (!hasWall(x, y, cardinalDirection)) {
            System.out.println(x + ", " + y);

            if (!mazeConfiguration.isValidPosition(x, y)) {
                return Integer.MAX_VALUE;
            }

            switch(cardinalDirection) {
                case North:
                    y++;
                case East:
                    x++;
                case South:
                    y--;
                case West:
                    x--;
            }
            steps++;
        }

        return steps;
    }

    @Override
    public boolean hasDistanceSensor(Direction direction) {
        return true;
    }

    /**
     * Decreases battery level by a certain amount. Checks to see if battery has run out, and if so, sets hasStopped to true and the Controller's state to STATE_FINISH, ending the game.
     * @author jabowden and tcmzeal
     * @param cost Amount that should be subtracted from battery level
     */
    public void usePower(float cost) {
        if (batteryLevel - cost <= 0) {
            this.hasStopped = true;
            batteryLevel = 0;
            endGame();
        } else {
            this.hasStopped = false;
            batteryLevel -= cost;
        }
    }

    public void reset() {
        this.batteryLevel = DEFAULT_BATTERY_LEVEL;
        this.odometer = 0;
        this.hasStopped = false;
        this.directionFacing = CardinalDirection.East;
    }

    /**
     * Given a Direction, figures out which absolute CardinalDirection that Direction lies from the robot's current perspective.
     * @author jabowden and tcmzeal
     * @param direction
     * @return the absolute CardinalDirection that the Direction is in, from the robot's current perspective
     */
    private CardinalDirection getCardinalDirection(Direction direction) {
        CardinalDirection cardinalDirection = this.getCurrentDirection();

        switch(direction) {
            case LEFT:
                cardinalDirection = cardinalDirection.rotateClockwise().rotateClockwise().rotateClockwise();
                break;
            case BACKWARD:
                cardinalDirection = cardinalDirection.rotateClockwise().rotateClockwise();
                break;
            case RIGHT:
                cardinalDirection = cardinalDirection.rotateClockwise();
                break;
            default:
                break;
        }

        return cardinalDirection;
    }

    /**
     * Determines whether a certain position is inside the maze.
     * @author jabowden and tcmzeal
     * @param pos array representing a position in a maze, format [x, y]
     * @return whether or not the position is in bounds
     */
    private boolean isInBounds(int[] pos) {
        return this.mazeController.getMazeConfiguration().isValidPosition(pos[0], pos[1]);
    }

    /**
     * Ends the game.
     * @author jabowden and tcmzeal
     */
    private void endGame() {
        /* TODO return */
    }

    /**
     * Determines if a wall in a certain CardinalDirection at position (x, y) exists.
     * @param x
     * @param y
     * @param direction
     * @return whether or not there is a wall
     */
    private boolean hasWall(int x, int y, CardinalDirection direction) {
        if (direction == CardinalDirection.North || direction == CardinalDirection.South) {
            direction = direction.oppositeDirection();
        }
        return this.mazeController.getMazeConfiguration().hasWall(x, y, direction);
    }

    @Override
    public boolean hasWallInDirection(CardinalDirection cardinalDirection) {
        int[] pos = this.mazeController.getCurrentPosition();
        assert(isInBounds(pos) == true) : OUT_OF_BOUNDS_MESSAGE;
        return this.hasWall(pos[0], pos[1], cardinalDirection);
    }

    @Override
    public boolean stepOutOfExit() {
        CardinalDirection cardinalDirection = null;

        int[] pos = this.mazeController.getCurrentPosition();
        assert(isAtExit() == true) : NOT_AT_EXIT_MESSAGE;
        int x = pos[0];
        int y = pos[1];
        MazeConfiguration mazeConfiguration = this.mazeController.getMazeConfiguration();

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.North) && !mazeConfiguration.isValidPosition(x, y + 1)) {
            cardinalDirection = CardinalDirection.North;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.East) && !mazeConfiguration.isValidPosition(x + 1, y)) {
            cardinalDirection = CardinalDirection.East;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.West) && !mazeConfiguration.isValidPosition(x - 1, y)) {
            cardinalDirection = CardinalDirection.West;
        }

        usePower(SENSOR_COST);
        if (!hasWall(x, y, CardinalDirection.South) && !mazeConfiguration.isValidPosition(x, y - 1)) {
            cardinalDirection = CardinalDirection.South;
        }

        if (cardinalDirection == null) {
            return false;
        }

        while (this.getCurrentDirection() != cardinalDirection) {
            rotate(Turn.RIGHT);
        }

        move(1, false);

        return true;
    }

    @Override
    public void setMazeCells(Cells cells){
        cell = cells;
    }

    @Override
    public void turnToDirection(CardinalDirection direction) {
        while (getCurrentDirection() != direction) {
            rotate(Turn.RIGHT);
        }
    }
}

