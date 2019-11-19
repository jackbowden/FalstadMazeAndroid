//package edu.wm.cs.amazebyjackandzeal.maze;
//
//import edu.wm.cs.amazebyjackandzeal.maze.MazePanel.MazePanelColors;
//
///**
// * Implements the screens that are displayed whenever the game is not in
// * the playing state. The screens shown are the title screen,
// * the generating screen with the progress bar during maze generation,
// * and the final screen when the game finishes.
// * @author pk
// *
// */
//public class MazeView extends DefaultViewer {
//
//    // need to know the maze model to check the state
//    // and to provide progress information in the generating state
//    private Controller controller ;
//
//
//
//    public MazeView(Controller c) {
//        super() ;
//        controller = c ;
//    }
//
//    @Override
//    public void redraw(MazePanel mazePanel, int px, int py, int view_dx,
//                       int view_dy, int walk_step, int view_offset, RangeSet rset, int ang) {
//    }
//
//    private void dbg(String str) {
//        System.out.println("MazeView:" + str);
//    }
//
//    //
//
//    /**
//     * Helper method for redraw to draw the title screen, screen is hard coded
//     * @param  mazePanel graphics is the off screen image
//     */
//    void redrawTitle(MazePanel mazePanel) {
//        // produce white background
//        mazePanel.setColor(MazePanelColors.WHITE);
//        mazePanel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//        // write the title
//        mazePanel.setFont(largeBannerFont);
//        FontMetrics fm = mazePanel.getFontMetrics();
//        mazePanel.setColor(MazePanelColors.RED);
//        centerString(mazePanel, fm, "MAZE", 100);
//        // write the reference to falstad
//        mazePanel.setColor(MazePanelColors.BLUE);
//        mazePanel.setFont(smallBannerFont);
//        fm = mazePanel.getFontMetrics();
//        centerString(mazePanel, fm, "by Paul Falstad", 30);
//        centerString(mazePanel, fm, "www.falstad.com", 50);
//        mazePanel.setColor(MazePanelColors.BLACK);
//		/*
//		centerString(gc, fm, "To start, select a skill level.", 250);
//		centerString(gc, fm, "(Press a number from 0 to 9,", 300);
//		centerString(gc, fm, "or a letter from A to F)", 320);
//		centerString(gc, fm, "Version 2.0", 350);
//		*/
//        Panel panel = controller.getPanel();
//
//        JPanel driverJPanel = new JPanel();
//        mazePanel.setColor(MazePanelColors.BLUE);
//        centerString(mazePanel, fm, "Choose driver:", 190);
//        driverJPanel.setBackground(Color.WHITE);
//        driverJPanel.setLocation(145, 195);
//        driverJPanel.setLayout(new FlowLayout());
//        driverJPanel.setSize(130, 30);
//        String[] drivers = {"Wizard", "Wall Follower", "Pledge", "Explorer", "Manual"};
//        JComboBox<String> driverComboBox = new JComboBox<String>(drivers);
//        driverJPanel.add(driverComboBox);
//        driverJPanel.setVisible(true);
//        panel.add(driverJPanel);
//
//        JPanel algorithmJPanel = new JPanel();
//        mazePanel.setColor(MazePanelColors.BLUE);
//        centerString(mazePanel, fm, "Choose algorithm:", 260);
//        algorithmJPanel.setBackground(Color.WHITE);
//        algorithmJPanel.setLocation(145,265);
//        algorithmJPanel.setLayout(new FlowLayout());
//        algorithmJPanel.setSize(130,30);
//        String[] algorithms = {"DFS", "Prim", "Eller"};
//        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
//        algorithmJPanel.add(algorithmComboBox);
//        algorithmJPanel.setVisible(true);
//        panel.add(algorithmJPanel);
//
//        JPanel skillLevelJPanel = new JPanel();
//        mazePanel.setColor(MazePanelColors.BLUE);
//        centerString(mazePanel, fm, "Select skill level:", 130);
//        skillLevelJPanel.setBackground(Color.WHITE);
//        skillLevelJPanel.setLocation(145,130);
//        skillLevelJPanel.setLayout(new FlowLayout());
//        skillLevelJPanel.setSize(130,30);
//        String[] difficulties = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
//        skillLevelJPanel.add(difficultyComboBox);
//        skillLevelJPanel.setVisible(true);
//        panel.add(skillLevelJPanel);
//
//        driverComboBox.setLightWeightPopupEnabled(false);
//        algorithmComboBox.setLightWeightPopupEnabled(false);
//        difficultyComboBox.setLightWeightPopupEnabled(false);
//
//        JPanel startPanel = new JPanel();
//        startPanel.setBackground(Color.WHITE);
//        startPanel.setLocation(145, 295);
//        startPanel.setSize(130, 30);
//        JButton start = new JButton();
//        start.setText("Start");
//
//        start.addActionListener(e-> {
//            if (algorithmComboBox.getSelectedIndex() != -1 && driverComboBox.getSelectedIndex() != -1) {
//                int selectedDriverIndex = driverComboBox.getSelectedIndex();
//                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
//
//                panel.removeAll();
//                panel.repaint();
//                Builder builder;
//
//                switch(selectedAlgorithm) {
//                    case "DFS":
//                        builder = Builder.DFS;
//                        break;
//                    case "Eller":
//                        builder = Builder.Eller;
//                        break;
//                    case "Prim":
//                        builder = Builder.Prim;
//                        break;
//                    default:
//                        builder = Builder.DFS;
//                        break;
//                }
//
//                RobotDriver driver;
//                switch(selectedDriverIndex) {
//                    case 0:
//                        driver = new Wizard();
//                        break;
//                    case 1:
//                        driver = new WallFollower();
//                        break;
//                    case 2:
//                        driver = new Pledge();
//                        break;
//                    case 3:
//                        driver = new Explorer();
//                        break;
//                    case 4:
//                        driver = new ManualDriver();
//                        System.out.println("Creating new driver: " + driver);
//                        break;
//                    default:
//                        driver = new ManualDriver();
//                        System.out.println("Error selecting driver. Using a manual driver.");
//                        break;
//                }
//                controller.setRobot(new BasicRobot(controller));
//                controller.setDriver(driver);
//                controller.setSkillLevel(difficultyComboBox.getSelectedIndex());
//                controller.setState(StateGUI.STATE_GENERATING);
//                controller.setBuilder(builder);
//
//                controller.switchToGeneratingScreen();
//            } else {
//                System.out.println("Not selected");
//            }
//        });
//        algorithmJPanel.add(start);
//        panel.add(algorithmJPanel);
//
//        panel.paintComponents(mazePanel.getBufferGraphics());
//    }
//    /**
//     * Helper method for redraw to draw final screen, screen is hard coded
//     * @param mazePanel graphics is the off screen image
//     */
//    void redrawFinish(MazePanel mazePanel) {
//        Robot robot = this.controller.getRobot();
//        // produce blue background
//        mazePanel.setColor(MazePanelColors.BLUE);
//        mazePanel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//        // write the title
//        mazePanel.setFont(largeBannerFont);
//        FontMetrics fm = mazePanel.getFontMetrics();
//        mazePanel.setColor(MazePanelColors.YELLOW);
//        if (robot.hasStopped()) {
//            centerString(mazePanel, fm, "Aw shucks", 80);
//            centerString(mazePanel, fm, "you Died", 140);
//        } else {
//            centerString(mazePanel, fm, "Congration", 80);
//            centerString(mazePanel, fm, "you Done it", 140);
//        }
//        // write some extra blufb
//        mazePanel.setColor(MazePanelColors.ORANGE);
//        mazePanel.setFont(smallBannerFont);
//        fm = mazePanel.getFontMetrics();
//        centerString(mazePanel, fm, "your path was " + robot.getOdometerReading() + " spaces long", 180);
//        centerString(mazePanel, fm, "you have " + robot.getBatteryLevel() + " battery left", 220);
//        // write the instructions
//        mazePanel.setColor(MazePanelColors.WHITE);
//        centerString(mazePanel, fm, "Hit any key to restart.", 260);
//        centerString(mazePanel, fm, "Or don't.", 290);
//        centerString(mazePanel, fm, "I'm a string, not a cop.", 320);
//    }
//
//    /**
//     * Helper method for redraw to draw screen during phase of maze generation, screen is hard coded
//     * only attribute percentdone is dynamic
//     * @param mazePanel graphics is the off screen image
//     */
//    void redrawGenerating(MazePanel mazePanel) {
//        // produce yellow background
//        mazePanel.setColor(MazePanelColors.YELLOW);
//        mazePanel.fillRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
//        // write the title
//        mazePanel.setFont(largeBannerFont);
//        FontMetrics fm = mazePanel.getFontMetrics();
//        mazePanel.setColor(MazePanelColors.RED);
//        centerString(mazePanel, fm, "Building maze", 150);
//        mazePanel.setFont(smallBannerFont);
//        fm = mazePanel.getFontMetrics();
//        // show progress
//        mazePanel.setColor(MazePanelColors.BLACK);
//        if (null != controller)
//            centerString(mazePanel, fm, controller.getPercentDone()+"% completed", 200);
//        else
//            centerString(mazePanel, fm, "Error: no controller, no progress", 200);
//        // write the instructions
//        centerString(mazePanel, fm, "Hit escape to stop", 300);
//    }
//
//    private void centerString(MazePanel mazePanel, FontMetrics fm, String str, int ypos) {
//        mazePanel.drawString(str, (Constants.VIEW_WIDTH-fm.stringWidth(str))/2, ypos);
//    }
//
//    final Font largeBannerFont = new Font("TimesRoman", Font.BOLD, 48);
//    final Font smallBannerFont = new Font("TimesRoman", Font.BOLD, 16);
//
//}