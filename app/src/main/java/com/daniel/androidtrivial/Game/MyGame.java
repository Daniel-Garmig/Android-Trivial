package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

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
        c.drawRect(data.testRect, data.mainPaint);
    }

    @Override
    public void Update()
    {
        Log.i(TAG, "Game Updated!!");

        //Move testRect around.
        GameData d = GameData.getInstance();
        Rect testRect = d.testRect;

        testRect.offset(10, 10);
        if(testRect.right > d.screenWidth)
        {
            testRect.offsetTo(0, testRect.top);
        }

        if(testRect.bottom > d.screenHeight)
        {
            testRect.offsetTo(testRect.left, 0);
        }
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GameData.getInstance().setScreenSize(width, height);
    }

    @Override
    public void Stop() {

    }
}
