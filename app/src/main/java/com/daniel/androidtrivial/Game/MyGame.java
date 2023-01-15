package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.daniel.androidtrivial.Game.Utils.AssetManager;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.uberelectron.androidrtg.RTG_App;

public class MyGame implements RTG_App
{
    private static final String TAG = "GameBoardApp";

    @Override
    public void Start()
    {
        GameData.getInstance().initGameData();
    }

    @Override
    public void Render(Canvas c)
    {
        //Clear Screen.
        GameData data = GameData.getInstance();
        c.drawColor(data.bgColor);

        //Draw test stuff.
        Transform tf = data.testTransf;
        Rect screenTestRect = data.mainCam.worldToScreenRect(tf.getRectF());
        c.drawRect(screenTestRect, data.mainPaint);

        data.board.Render(c, data.mainCam);
    }

    @Override
    public void Update()
    {
        //Log.i(TAG, "Game Updated!!");

        //Move testRect around.
        GameData d = GameData.getInstance();

        Camera cam = d.mainCam;

        cam.transform.moveAmount(1, 1);
        Vector2 pos = cam.transform.getPosition();
        if(pos.x > 200)
        {
            cam.transform.setPosition(0, (int) pos.y);
        }

        if(pos.y > 200)
        {
            cam.transform.setPosition((int) pos.x, 0);
        }

    }

    @Override
    public void onSurfaceChanged(int width, int height)
    {
        GameData.getInstance().setScreenSize(width, height);
    }

    @Override
    public void Stop()
    {

    }
}
