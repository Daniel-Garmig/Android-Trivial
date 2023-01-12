package com.daniel.androidtrivial.Game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Transform;

public class GameData
{
    // SINGLETON.
    private static GameData instance;

    public static GameData getInstance()
    {
        //Singleton with double-checked locking (DCL).

        GameData gameData = instance;
        if(gameData != null) { return gameData; }

        synchronized (GameData.class)
        {
            if(instance == null)
            {
                instance = new GameData();
            }
            return instance;
        }
    }

    // END SINGLETON.


    int bgColor;
    Paint mainPaint;
    Transform testTransf;

    Camera mainCam;

    int screenWidth;
    int screenHeight;


    private GameData()
    {
        mainCam = new Camera(0, 0, 100, 100);

        screenHeight = 500;
        screenWidth = 500;
    }

    public void initGameData()
    {
        bgColor = Color.BLACK;

        mainPaint = new Paint();
        mainPaint.setColor(Color.CYAN);

        testTransf = new Transform(50, 50, 50, 50);

        mainCam = new Camera(0, 0, 200, 200);
    }


    public void setScreenSize(int width, int height)
    {
        screenWidth = width;
        screenHeight = height;

        mainCam.updateScreenSize(width, height);
    }

}
