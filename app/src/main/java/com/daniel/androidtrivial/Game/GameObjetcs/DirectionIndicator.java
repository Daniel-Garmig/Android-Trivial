package com.daniel.androidtrivial.Game.GameObjetcs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Renderizable;
import com.daniel.androidtrivial.Game.Utils.Vector2;

public class DirectionIndicator implements Renderizable
{
    private static final int STROKE_WIDTH = 5;

    public int sqID;

    public Vector2 initialPos;
    public Vector2 endPos;

    public Paint color;


    public DirectionIndicator()
    {
        color = new Paint();
        color.setColor(Color.RED);
        color.setStrokeWidth(STROKE_WIDTH);
    }

    public DirectionIndicator(Vector2 initialPos, Vector2 endPos, int sqID)
    {
        this.sqID = sqID;
        this.initialPos = initialPos;
        this.endPos = endPos;

        color = new Paint();
        color.setColor(Color.RED);
        color.setStrokeWidth(STROKE_WIDTH);
    }

    public DirectionIndicator(Vector2 initialPos, Vector2 endPos, Paint color, int strokeWidth)
    {
        this.initialPos = initialPos;
        this.endPos = endPos;
        this.color = color;
        this.color.setStrokeWidth(strokeWidth);
    }


    @Override
    public void Render(Canvas canvas, Camera cam)
    {
        Vector2 screenInitPos = cam.worldToScreenCoords(initialPos);
        Vector2 screenEndPos = cam.worldToScreenCoords(endPos);

        canvas.drawLine(screenInitPos.x, screenInitPos.y,
                screenEndPos.x, screenEndPos.y,
                color);
    }
}
