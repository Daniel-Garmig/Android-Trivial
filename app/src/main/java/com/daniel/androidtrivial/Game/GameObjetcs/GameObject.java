package com.daniel.androidtrivial.Game.GameObjetcs;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.daniel.androidtrivial.Game.Utils.AssetManager;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Renderizable;
import com.daniel.androidtrivial.Game.Utils.Sprite;
import com.daniel.androidtrivial.Game.Utils.Transform;

public class GameObject implements Renderizable
{
    public Sprite sprite;
    public Transform transform;


    public GameObject(String assetName)
    {
        sprite = new Sprite(AssetManager.getInstance().getBitmap(assetName));
        transform = new Transform(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void Render(Canvas canvas)
    {
        canvas.drawBitmap(sprite.getImage(), sprite.getImageRect(), transform.getRect(), sprite.getImagePaint());
    }

    @Override
    public void Render(Canvas canvas, Camera cam)
    {
        Rect screenRect = cam.worldToScreenRect(transform.getRectF());
        canvas.drawBitmap(sprite.getImage(), sprite.getImageRect(), screenRect, sprite.getImagePaint());
    }
}
