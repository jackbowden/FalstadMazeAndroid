package edu.wm.cs.amazebyjackandzeal.maze;

/**
 * Interface to specify all functionality provided by a viewer to the maze model.
 * The Viewer is used to uniformly treat the MazeView, the FirstPersonDrawer and the MapDrawer
 * by the Maze class. Viewers can register with a maze and get notified via method calls
 * if a corresponding event is triggered on the GUI.
 *
 * @author Kemper
 *
 */
public interface Viewer {
    /**
     * Updates what is on display on the screen. The behavior depends on the particular state of the maze.
     * Classes that implement this interface use the state parameter to recognize if they should react to
     * the method call or ignore it.
     *
     * @param px x coordinate of current position, only used to get viewx
     * @param py y coordinate of current position, only used to get viewy
     * @param view_dx view direction, x coordinate
     * @param view_dy view direction, y coordinate
     * @param rset
     * @param ang
     */
    public void redraw(MazePanel mazePanel, int px, int py, int view_dx, int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) ;
    /**
     * Increases the map if the map is on display.
     */
    public void incrementMapScale() ;
    /**
     * Shrinks the map if the map is on display.
     */
    public void decrementMapScale() ;
}
