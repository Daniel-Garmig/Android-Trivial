package com.daniel.androidtrivial.Game.Utils;

import android.graphics.Rect;
import android.graphics.RectF;

public class Camera
{
    //For setting the default size if not set on the constructor.
    private static final Vector2 defaultSize = new Vector2(100, 100);

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
        //Recalculate scales.
        Vector2 camSize = transform.getSize();

        //Para que sea "cuadrado" tomaremos como referencia el más pequeño.
        //TODO: Comprobar que esto de la escala tiene sentido así...
        int smaller = Integer.min(newWidth, newHeight);
        horizontalScale = smaller / camSize.x;
        verticalScale = smaller / camSize.y;
    }


    public Vector2 worldToScreenCoords(Vector2 worldCoords)
    {
        return worldCoords.subtract(transform.getPosition());
    }

    public Vector2 screenToWorldCoords(Vector2 screenCoords)
    {
        return screenCoords.sum(transform.getPosition());
    }


    public Rect worldToScreenRect(RectF worldRect)
    {
        //Transform Coords.
        Rect screenRect = new Rect();
        Vector2 screenCoords = worldToScreenCoords(new Vector2(worldRect.left, worldRect.top));
        Vector2 scaledScreenCoords = new Vector2(screenCoords.x * horizontalScale, screenCoords.y * verticalScale);
        Vector2 screenRectSize = new Vector2(worldRect.width() * horizontalScale, worldRect.height() * verticalScale);

        //TODO: Revisar Cast.
        screenRect.left = (int) scaledScreenCoords.x;
        screenRect.top = (int) scaledScreenCoords.y;
        screenRect.right = (int) (scaledScreenCoords.x + screenRectSize.x);
        screenRect.bottom = (int) (scaledScreenCoords.y + screenRectSize.y);

        return screenRect;
    }

    //TODO: Add screenToWorldRect.
}
