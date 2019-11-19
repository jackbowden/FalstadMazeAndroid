package edu.wm.cs.amazebyjackandzeal.maze;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import edu.wm.cs.amazebyjackandzeal.generation.CardinalDirection;
import edu.wm.cs.amazebyjackandzeal.generation.Cells;
import edu.wm.cs.amazebyjackandzeal.generation.Factory;
import edu.wm.cs.amazebyjackandzeal.generation.MazeConfiguration;
import edu.wm.cs.amazebyjackandzeal.generation.MazeContainer;
import edu.wm.cs.amazebyjackandzeal.generation.MazeFactory;
import edu.wm.cs.amazebyjackandzeal.generation.Order;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class handles the user interaction.
 * It implements a state-dependent behavior that controls the display and reacts to key board input from a user.
 * At this point user keyboard input is first dealt with a key listener (SimpleKeyListener)
 * and then handed over to a Controller object by way of the keyDown method.
 *
 * This code is refactored code from Maze.java by Paul Falstad, www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 */
public class Controller /* implements Order */ {
    // Follows a variant of the Model View Controller pattern (MVC).
    // This class acts as the controller that gets user input and operates on the model.
    // A MazeConfiguration acts as the model and this class has a reference to it.
    private MazeConfiguration mazeConfig ;
    // Deviating from the MVC pattern, the controller has a list of viewers and
    // notifies them if user input requires updates on the UI.
    // This is normally the task of the model in the MVC pattern.

    // views is the list of registered viewers that get notified
    final private ArrayList<Viewer> views = new ArrayList<Viewer>() ;
    // all viewers share access to the same graphics object, the panel, to draw on
    private MazePanel panel;

    private int percentdone = 0; 		// describes progress during generation phase
    private boolean showMaze;		 	// toggle switch to show overall maze on screen
    private boolean showSolution;		// toggle switch to show solution in overall maze on screen
    private boolean mapMode; // true: display map of maze, false: do not display map of maze
    // map_mode is toggled by user keyboard input, causes a call to draw_map during play mode

    // current position and direction with regard to MazeConfiguration
    private int px, py ; // current position on maze grid (x,y)
    private int dx, dy;  // current direction

    // current position and direction with regard to graphics view
    // graphics has intermediate views for a smoother experience of turns
    private int viewx, viewy; // current position
    private int viewdx, viewdy; // current view direction, more fine grained than (dx,dy)
    private int angle; // current viewing angle, east == 0 degrees
    //static final int viewz = 50;
    private int walkStep; // counter for intermediate steps within a single step forward or backward
    private Cells seencells; // a matrix with cells to memorize which cells are visible from the current point of view
    // the FirstPersonDrawer obtains this information and the MapDrawer uses it for highlighting currently visible walls on the map

    // about the maze and its generation
    private int skill; // user selected skill level, i.e. size of maze
    private Order.Builder builder; // selected maze generation algorithm
    private boolean perfect; // selected type of maze, i.e.
    // perfect == true: no loops, i.e. no rooms
    // perfect == false: maze can support rooms

    // The factory is used to calculate a new maze configuration
    // The maze is computed in a separate thread which makes
    // communication with the factory slightly more complicated.
    // Check the factory interface for details.
    protected Factory factory;

    // Filename if maze is loaded from file
    private String filename;

    //private int zscale = Constants.VIEW_HEIGHT/2;
    private RangeSet rset;

    // debug stuff
    private boolean deepdebug = false;
    private boolean allVisible = false;
    private boolean newGame = false;

    /**
     * Constructor
     * Default setting for maze generating algorithm is DFS.
     */
    public Controller(Context context, MazeConfiguration mazeConfiguration, MazePanel panel) {
        super() ;
        setBuilder(Order.Builder.DFS);
        this.panel = panel;
        mazeConfig = mazeConfiguration;
        factory = new MazeFactory() ;
        filename = null;
        init();
    }
    /**
     * Constructor to read maze from file
     * @param filename
     */
    public Controller(Context context, MazeConfiguration mazeConfiguration, MazePanel panel, String filename) {
        super();
        setBuilder(Order.Builder.DFS);
        this.panel = panel;
        mazeConfig = mazeConfiguration;
        factory = new MazeFactory(); // no factory needed but to allow user to play another round
        this.filename = filename;
        init();
    }
    /**
     * Loads maze from file and returns a corresponding maze configuration.
     * @param filename
     */
    private MazeConfiguration loadMazeConfigurationFromFile(String filename) {
        // load maze from file
        MazeFileReader mfr = new MazeFileReader(filename) ;
        // obtain MazeConfiguration
        return mfr.getMazeConfiguration();
    }
    /**
     * Method to initialize internal attributes. Called separately from the constructor.
     */
    public void init() {
        showMaze = false ;
        showSolution = false ;
        mapMode = false;
        // init data structure for visible walls
        seencells = new Cells(mazeConfig.getWidth()+1,mazeConfig.getHeight()+1) ;
        int[] start = mazeConfig.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        // set current view direction and angle
        setCurrentDirection(1, 0) ; // east direction
        viewdx = dx<<16;
        viewdy = dy<<16;
        angle = 0; // angle matches with east direction, hidden consistency constraint!
        walkStep = 0; // counts incremental steps during move/rotate operation

        Log.v("Controller", "Adding first person drawer!");
        this.addView(new FirstPersonDrawer(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Constants.MAP_UNIT, Constants.STEP_SIZE, seencells, mazeConfig.getRootnode()));

        // special case: load maze from file
        if (null != filename) {
            rset = new RangeSet();
            //addView(new MazeView(this)) ;
            // push results into controller, imitating maze factory delivery
            //deliver(loadMazeConfigurationFromFile(filename));
            // reset filename, next round will be generated again
            filename = null;
            return;
        }
        // common case: generate maze with some algorithm
        assert null != factory : "Controller.init: factory must be present";
        rset = new RangeSet();
        //panel.initBufferImage() ;
        //addView(new MazeView(this)) ;
        notifyViewerRedraw() ;
    }

    public MazeConfiguration getMazeConfiguration() {
        return mazeConfig ;
    }

    public void switchToGeneratingScreen() {
        percentdone = 0;
        setPerfect(false);
        //factory.order();
    }
    /////////////////////////////// Methods for the Model-View-Controller Pattern /////////////////////////////
    /**
     * Register a view
     */
    public void addView(Viewer view) {
        views.add(view) ;
    }
    /**
     * Unregister a view
     */
    public void removeView(Viewer view) {
        views.remove(view) ;
    }
    /**
     * Remove obsolete FirstPersonDrawer and MapDrawer
     */
    private void cleanViews() {
        // go through views and remove viewers as needed
        Iterator<Viewer> it = views.iterator() ;
//        while (it.hasNext())
//        {
//            Viewer v = it.next() ;
//            if ((v instanceof FirstPersonDrawer)||(v instanceof MapDrawer))
//            {
//                it.remove() ;
//            }
//        }

    }
    /**
     * Notify all registered viewers to redraw their graphics
     */
    public void notifyViewerRedraw() {
        Iterator<Viewer> it = views.iterator() ;
        while (it.hasNext())
        {
            Viewer v = it.next() ;
            // viewers draw on the buffer graphics
            if (null == panel.getBufferGraphics()) {
                System.out.println("Maze.notifierViewerRedraw: can't get graphics object to draw on, skipping redraw operation") ;
            }
            else {
                Log.v("Controller", "Redraw() called");
                Log.v("Controller", v.toString());
                v.redraw(panel, px, py, viewdx, viewdy, walkStep, Constants.VIEW_OFFSET, rset, angle) ;
            }
        }
        // update the screen with the buffer graphics
        panel.update() ;
    }
    /**
     * Notify all registered viewers to increment the map scale
     */
    private void notifyViewerIncrementMapScale() {
        // go through views and notify each one
        Iterator<Viewer> it = views.iterator() ;
        while (it.hasNext())
        {
            Viewer v = it.next() ;
            v.incrementMapScale() ;
        }
        // update the screen with the buffer graphics
        panel.update() ;
    }
    /**
     * Notify all registered viewers to decrement the map scale
     */
    private void notifyViewerDecrementMapScale() {
        // go through views and notify each one
        Iterator<Viewer> it = views.iterator() ;
        while (it.hasNext())
        {
            Viewer v = it.next() ;
            v.decrementMapScale() ;
        }
        // update the screen with the buffer graphics
        panel.update() ;
    }
    ////////////////////////////// get methods ///////////////////////////////////////////////////////////////
    boolean isInMapMode() {
        return mapMode ;
    }
    boolean isInShowMazeMode() {
        return showMaze ;
    }
    boolean isInShowSolutionMode() {
        return showSolution ;
    }
    public String getPercentDone(){
        return String.valueOf(percentdone) ;
    }
    public MazePanel getPanel() {
        return panel ;
    }
    ////////////////////////////// set methods ///////////////////////////////////////////////////////////////
    ////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
    protected void setCurrentPosition(int x, int y)
    {
        px = x ;
        py = y ;
    }
    private void setCurrentDirection(int x, int y)
    {
        dx = x ;
        dy = y ;
    }
    protected int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    protected CardinalDirection getCurrentDirection() {
        return CardinalDirection.getDirection(dx, dy);
    }

    /////////////////////// Methods for debugging ////////////////////////////////
    private void dbg(String str) {
        //System.out.println(str);
    }

    private void logPosition() {
        if (!deepdebug)
            return;
        dbg("x="+viewx/Constants.MAP_UNIT+" ("+
                viewx+") y="+viewy/Constants.MAP_UNIT+" ("+viewy+") ang="+
                angle+" dx="+dx+" dy="+dy+" "+viewdx+" "+viewdy);
    }

    //////////////////////// Methods for move and rotate operations ///////////////
    final double radify(int x) {
        return x*Math.PI/180;
    }
    /**
     * Helper method for walk()
     * @param dir
     * @return true if there is no wall in this direction
     */
    protected boolean checkMove(int dir) {
        CardinalDirection cd = null;
        switch (dir) {
            case 1: // forward
                cd = getCurrentDirection();
                break;
            case -1: // backward
                cd = getCurrentDirection().oppositeDirection();
                break;
            default:
                throw new RuntimeException("Unexpexted direction value: " + dir);
        }
        //return mazeConfig.getMazecells().hasNoWall(px, py, cd);
        return !mazeConfig.hasWall(px, py, cd);
    }
    /**
     * Redraw and wait, used to obtain a smooth appearance for rotate and move operations
     */
    private void slowedDownRedraw() {
        notifyViewerRedraw() ;
        try {
            Thread.currentThread().sleep(25);
        } catch (Exception e) { }
    }
    /**
     * Intermediate step during rotation, updates the screen
     */
    private void rotateStep() {
        angle = (angle+1800) % 360;
        viewdx = (int) (Math.cos(radify(angle))*(1<<16));
        viewdy = (int) (Math.sin(radify(angle))*(1<<16));
        slowedDownRedraw();
    }
    /**
     * Performs a rotation with 4 intermediate views,
     * updates the screen and the internal direction
     * @param dir for current direction
     */
	/* TODO this method was originally private */
    synchronized public void rotate(int dir) {
        final int originalAngle = angle;
        final int steps = 4;

        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            rotateStep();
        }
        setCurrentDirection((int) Math.cos(radify(angle)), (int) Math.sin(radify(angle))) ;
        logPosition();
    }
    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
	/* TODO this method was originally private */
    synchronized public void walk(int dir) {
        if (!checkMove(dir))
            return;
        // walkStep is a parameter of the redraw method in FirstPersonDrawer
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw which triggers the redraw
        // operation on all listed viewers
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw();
        }
        setCurrentPosition(px + dir*dx, py + dir*dy) ;
        walkStep = 0;
        logPosition();
    }

    /**
     * checks if the given position is outside the maze
     * @param x
     * @param y
     * @return true if position is outside, false otherwise
     */
    private boolean isOutside(int x, int y) {
        return !mazeConfig.isValidPosition(x, y) ;
    }

    public enum UserInput {ReturnToTitle, Start, Up, Down, Left, Right, Jump, ToggleLocalMap, ToggleFullMap, ToggleSolution, ZoomIn, ZoomOut };

    ////////// set methods for fields ////////////////////////////////
    public void setSkillLevel(int skill) {
        System.out.println(skill);
		/*if (skill == 1) {
			this.skill = 1;
		}*/
        this.skill = skill ;
    }

    public void setBuilder(Order.Builder builder) {
        System.out.println(builder);
        this.builder = builder ;
        //if (builder == "DFS") {
        //	this.builder = Order.Builder.DFS;
        //}
        //setBuilder(Order.Builder.DFS);
    }

    private void setPerfect(boolean perfect) {
        this.perfect = perfect ;
    }
}

