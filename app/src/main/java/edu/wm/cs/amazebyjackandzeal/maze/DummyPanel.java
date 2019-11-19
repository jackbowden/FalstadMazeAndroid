package edu.wm.cs.amazebyjackandzeal.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zeal on 12/5/2017.
 */

public class DummyPanel extends View {
    Paint paint;

    public DummyPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public DummyPanel(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 100, 100, paint);
    }
}
