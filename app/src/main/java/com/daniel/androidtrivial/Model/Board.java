package com.daniel.androidtrivial.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daniel.androidtrivial.Game.Utils.Camera;

import java.util.HashMap;

/**
 * Data class deserialized from JSON.
 * Store board squares.
 */
public class Board
{
    public HashMap<Integer, BoardSquare> squares = new HashMap<>();

    Board() {}

    // Can be drawn for debugging.
    public void debugRender(Canvas c, Camera cam)
    {
        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.BLACK);
        debugPaint.setTextSize(40);

        for(BoardSquare sq : squares.values())
        {
            c.drawText(sq.id+"", sq.pos.x, sq.pos.y, debugPaint);
        }
    }
}
