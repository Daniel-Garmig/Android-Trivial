package com.daniel.androidtrivial.Game.Utils;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite
{
    private Bitmap image;
    private Rect imageRect;

    private Paint imagePaint;

    //Constructors

    public Sprite(Bitmap image)
    {
        this.image = image;
        //Generate rect as full image.
        imageRect = new Rect(0, 0, image.getWidth(), image.getHeight());

        imagePaint = new Paint();
    }

    public Sprite(Bitmap image, Rect imageRect)
    {
        this.image = image;
        this.imageRect = imageRect;

        imagePaint = new Paint();
    }


    //Methods

    public void setColor(int c)
    {
        imagePaint.setColor(c);
    }

    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; }

    public Rect getImageRect() { return imageRect; }
    public void setImageRect(Rect imageRect) { this.imageRect = imageRect; }

    public Paint getImagePaint() { return imagePaint; }
    public void setImagePaint(Paint imagePaint) { this.imagePaint = imagePaint; }

    public int getWidth() { return imageRect.width(); }
    public int getHeight() { return imageRect.height(); }


    public double getScaledWidth(double scalar) { return imageRect.width() * scalar; }
    public double getScaledHeight(double scalar) { return imageRect.height() * scalar; }
}
