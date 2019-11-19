package edu.wm.cs.amazebyjackandzeal.generation;

public class MazeBuilderEller extends MazeBuilder implements Runnable {
    private int lowestUnusedID;

    public MazeBuilderEller() {
        super();
        lowestUnusedID = 0;
    }

    public MazeBuilderEller(boolean deterministic) {
        super(deterministic);
        lowestUnusedID = 0;
    }

    protected void resetLowestUnusedID() {
        this.lowestUnusedID = 0;
    }

    /**
     * Generates pathways through a maze by deleting walls in the cells object
     * in accordance with Eller's algorithm.
     *
     */
    @Override
    protected void generatePathways() {
		/* define the start position randomly */
        startx = random.nextIntWithinInterval(0, width-1);
        starty = random.nextIntWithinInterval(0, height-1);

        int[] currentRowSetIDs = new int[width];

        for (int y = 0; y < height; y++) { /* for each row, going up */
            setUpSetIDs(y, currentRowSetIDs);
            removeRightWalls(y, currentRowSetIDs);
            removeBottomWalls(y, currentRowSetIDs);
        }
    }

    /**
     * Given a row, and an array of set IDs in the row, assigns new set IDs and merges sets if needed.
     *
     * @param y the height of the row
     * @param currentRowSetIDs an array holding the set IDs of the cells in the row
     */
    protected int[] setUpSetIDs(int y, int[] currentRowSetIDs) {
        if (y == 0) { /* if we're at the bottom row */
			/* assign all new set IDs to fresh IDs */
            for (int i = 0; i < width ; i++) {
                currentRowSetIDs[i] = lowestUnusedID++;
            }
        } else {
            for (int i = 0 ; i < width ; i++) {
				/* if the cell has a top wall, reassign its ID to something new */
				/* otherwise, merge this cell's set with the set on the left */
                if (cells.hasWall(i, y, CardinalDirection.North)) {
                    currentRowSetIDs[i] = lowestUnusedID++;
                } else {
                    currentRowSetIDs[i] = currentRowSetIDs[i];
                }
            }
        }
        return currentRowSetIDs;
    }

    /**
     * Given a row, and an array of set IDs in the row, determines when a right wall should be removed.
     *
     * @param y the height of the row
     * @param currentRowSetIDs an array holding the set IDs of the cells in the row
     */
    protected int[] removeRightWalls(int y, int[] currentRowSetIDs) {
        for (int x = 0; x < width; x++) { /* for each cell in the row... */
			/* if we're at the last row */
            if (y == height - 1) {
				/* if this cell is not equal to its right neighbor */
				/* and also make sure we're not at the rightmost column */
                if (x != width - 1 && currentRowSetIDs[x] != currentRowSetIDs[x + 1]) {
					/* delete the right wall of this cell */
                    cells.deleteWall(new Wall(x, y, CardinalDirection.East));
                }
            } else { /* if we're in the middle rows */
				/* if we're not at the rightmost column */
                if (x != width - 1) {
					/* if this cell is not equal to its right neighbor */
                    if (currentRowSetIDs[x] != currentRowSetIDs[x + 1]) {
						/* 50% shot of deleting this cell's right wall
						 * and merging it with its right neighbor
						 */
                        if (random.nextIntWithinInterval(0, 1) == 1) {
                            cells.deleteWall(new Wall(x, y, CardinalDirection.East));
                            currentRowSetIDs[x + 1] = currentRowSetIDs[x];
                        }
                    }
                }
            }
        }
        return currentRowSetIDs;
    }

    /**
     * Given a row, and an array of set IDs in the row, determines when a bottom wall should be removed.
     *
     * @param y the height of the row
     * @param currentRowSetIDs an array holding the set IDs of the cells in the row
     */
    protected int[] removeBottomWalls(int y, int[] currentRowSetIDs) {
        if (y == height - 1) {
            return currentRowSetIDs;
        } /* do nothing if we're at the last row! */

        int leftBound = 0;
        for (int x = 0; x < width; x++) { /* for each cell in the row... */
            if (x == width - 1 || currentRowSetIDs[x] != currentRowSetIDs[x + 1]) {
				/* the right bound of the current consecutive set is x. */
				/* choose a random position in the current consecutive set we're in. */
				/* remove the bottom wall at this position. */
                int randX = random.nextIntWithinInterval(leftBound, x);
                cells.deleteWall(new Wall(randX, y, CardinalDirection.South));

				/* a new consecutive set will start at position x + 1. */
                leftBound = x + 1;
            }
        }

        return currentRowSetIDs;
    }
}
