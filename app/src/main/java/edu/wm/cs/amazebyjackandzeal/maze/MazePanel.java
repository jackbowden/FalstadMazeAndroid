package edu.wm.cs.amazebyjackandzeal.maze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MazePanel extends View  {
    private static final int SIZE = Constants.VIEW_HEIGHT;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    public MazePanel(Context context) {
        super(context);
        init();
    }

    public MazePanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setColor(Color.GREEN);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.bitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas androidCanvas) {
        Log.v("MAZEPANEL", "Drawing!");

        super.onDraw(androidCanvas);
        androidCanvas.drawBitmap(bitmap, 0, 0, paint);

        Path path = new Path();
        path.reset();
        path.moveTo(0, 0);
        path.moveTo(100, 0);
        path.moveTo(100, 100);
        path.moveTo(0, 0);
        androidCanvas.drawPath(path, paint);
    }

    public void update() {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    public Object getBufferGraphics() {
        return canvas;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void drawString(String string, int x, int y) {
        canvas.drawText(string, x, y, paint);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        path.reset();
        for (int i = 0; i < xPoints.length; i++) {
            path.moveTo(xPoints[i], yPoints[i]);
        }
        path.moveTo(xPoints[0], yPoints[0]);

        canvas.drawPath(path, paint);
    }

    public void fillOval(int x, int y, int width, int height) {
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int left = x;
        int top = y;
        int right = x + width;
        int bottom = y + height;
        canvas.drawOval(new RectF(left, top, right, bottom), paint);
    }

    public void fillRect(int x, int y, int width, int height) {
        Log.v("MazePanel", "drawing rectangle at (" + x + ", " + y + ") " + width + "x" + height);
        this.canvas.drawRect(x, y, x + width, y + height, this.paint);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Log.v("MazePanel", "Drawing polygon!");
        Path path = new Path();
        path.reset();
        for (int i = 0; i < nPoints; i++) {
            path.moveTo(xPoints[i], yPoints[i]);
        }
        path.moveTo(xPoints[0], yPoints[0]);

        canvas.drawPath(path, paint);
    }

    public enum MazePanelColors {
        BLACK, BLUE, DARKGRAY, GRAY, ORANGE, RED, WHITE, YELLOW
    }

    public void setColor(MazePanelColors color) {
        Log.v("MazePanel", "setColor() called");
        switch (color) {
            case BLACK:
                paint.setColor(Color.BLACK);
                break;
            case BLUE:
                paint.setColor(Color.BLUE);
                break;
            case DARKGRAY:
                paint.setColor(Color.GREEN);
                break;
            case GRAY:
                paint.setColor(Color.GRAY);
                break;
            case ORANGE:
                paint.setColor(0xffb600);
                break;
            case RED:
                paint.setColor(Color.RED);
                break;
            case WHITE:
                paint.setColor(Color.WHITE);
                break;
            case YELLOW:
                paint.setColor(Color.YELLOW);
                break;
        }
    }

    public void setColor(int r, int g, int b) {
        paint.setColor(Color.rgb(r, g, b));
    }

    public void setColor(int colorInt) {
        paint.setColor(colorInt);
    }

    public static int getRGB(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }

    public static int[] getRGBArray(int colorInt) {
        return new int[] {Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt)};
    }
}
