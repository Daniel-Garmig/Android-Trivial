package com.daniel.androidtrivial.Game.Utils;

import android.graphics.Rect;
import android.graphics.RectF;

public class Camera
{
    //For setting the default size if not set on the constructor.
    protected static final Vector2 defaultSize = new Vector2(100, 100);

    private Vector2 screenSize;

    public Transform transform;

    float verticalScale;
    float horizontalScale;

    public Camera()
    {
        transform = new Transform(0, 0, defaultSize.x, defaultSize.y);
    }

    public Camera(int posX, int posY)
    {
        transform = new Transform(posX, posY, defaultSize.x, defaultSize.y);
    }

    public Camera(int posX, int posY, int sizeX, int sizeY)
    {
        transform = new Transform(posX, posY, sizeX, sizeY);
    }


    /**
     * Update Scales.
     * @param newWidth New Available Screen Width.
     * @param newHeight New available screen height.
     */
    public void updateScreenSize(int newWidth, int newHeight)
    {
        screenSize = new Vector2(newWidth, newHeight);
        //Recalculate scales.
        Vector2 camSize = transform.getSize();

        //Para que sea "cuadrado" tomaremos como referencia el más pequeño.
        int smaller = Integer.min(newWidth, newHeight);
        horizontalScale = smaller / camSize.x;
        verticalScale = smaller / camSize.y;
    }

    /**
     * Update Scale in case cam size changes.
     */
    public void updateScreenSize()
    {
        if(screenSize == null) { return; }

        //Recalculate scales.
        Vector2 camSize = transform.getSize();

        //Para que sea "cuadrado" tomaremos como referencia el más pequeño.
        float smaller = Float.min(screenSize.x, screenSize.y);
        horizontalScale = smaller / camSize.x;
        verticalScale = smaller / camSize.y;
    }


    public Vector2 worldToScreenCoords(Vector2 worldCoords)
    {
        Vector2 screenCoords = worldCoords.subtract(transform.getPosition());
        return new Vector2(screenCoords.x * horizontalScale, screenCoords.y * verticalScale);
    }

    public Vector2 screenToWorldCoords(Vector2 screenCoords)
    {
        Vector2 worldCoors =  screenCoords.sum(transform.getPosition());
        return new Vector2(worldCoors.x * horizontalScale, worldCoors.y * verticalScale);
    }


    public Rect worldToScreenRect(RectF worldRect)
    {
        //Transform Coords.
        Rect screenRect = new Rect();
        Vector2 screenCoords = worldToScreenCoords(new Vector2(worldRect.left, worldRect.top));
        Vector2 screenRectSize = new Vector2(worldRect.width() * horizontalScale, worldRect.height() * verticalScale);

        //TODO: Revisar Cast.
        screenRect.left = (int) screenCoords.x;
        screenRect.top = (int) screenCoords.y;
        screenRect.right = (int) (screenCoords.x + screenRectSize.x);
        screenRect.bottom = (int) (screenCoords.y + screenRectSize.y);

        return screenRect;
    }

    //TODO: Add screenToWorldRect.
}
