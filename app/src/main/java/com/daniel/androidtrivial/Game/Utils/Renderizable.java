package com.daniel.androidtrivial.Game.Utils;

import android.graphics.Canvas;

public interface Renderizable
{

    public default void Render(Canvas canvas)
    {
        throw new UnsupportedOperationException("No implementation for render");
    }

    public default void Render(Canvas canvas, Camera cam)
    {
        throw new UnsupportedOperationException("No implementation for use with cameras");
    }

}
