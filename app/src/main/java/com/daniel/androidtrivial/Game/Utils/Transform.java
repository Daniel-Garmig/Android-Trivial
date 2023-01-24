package com.daniel.androidtrivial.Game.Utils;

import android.graphics.Rect;
import android.graphics.RectF;

public class Transform
{
    private RectF rect;

    //Constructors.

    public Transform()
    {
        rect = new RectF();
    }

    public Transform(Rect rect)
    {
        this.rect = new RectF(rect);
    }

    public Transform(float posX, float posY, float sizeX, float sizeY)
    {
        rect = new RectF(posX, posY, posX + sizeX, posY + sizeY);
    }


    // Methods

    public void setPosition(int x, int y)
    {
        rect.offsetTo(x, y);
    }

    public void setPosition(float x, float y) { rect.offsetTo(x, y);}

    public void setPosition(Vector2 newPos) { rect.offsetTo(newPos.x, newPos.y);}

    public void setCenterPosition(Vector2 newCenterPos)
    {
        setPosition(newCenterPos.x - getSize().x/2, newCenterPos.y - getSize().y/2);
    }

    public void moveAmount(int movX, int movY)
    {
        rect.offset(movX, movY);
    }

    public void moveAmount(float movX, float movY)
    {
        rect.offset(movX, movY);
    }


    public Vector2 getPosition() { return new Vector2(rect.left, rect.top); }



    public void setSize(float sizeX, float sizeY)
    {
        float posX = rect.left;
        float posY = rect.top;
        rect.set(posX, posY, posX + sizeX, posY + sizeY);
    }

    public void setSize(int sizeX, int sizeY)
    {
        float posX = rect.left;
        float posY = rect.top;
        rect.set(posX, posY, posX + sizeX, posY + sizeY);
    }

    public void resizeAmount(float dtX, float dtY)
    {
        setSize(rect.width() + dtX, rect.height() + dtY);
    }


    public Vector2 getSize()
    {
        return new Vector2(rect.width(), rect.height());
    }



    public RectF getRectF() { return rect; }

    public Rect getRect()
    {
        Rect rs = new Rect();
        //TODO: Test this up. I'm not sure how this works...
        rect.round(rs);
        return rs;
    }

}
