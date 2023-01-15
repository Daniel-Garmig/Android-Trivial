package com.daniel.androidtrivial.Game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.daniel.androidtrivial.Game.Data.Board;
import com.daniel.androidtrivial.Game.GameObjetcs.GameObject;
import com.daniel.androidtrivial.Game.GameObjetcs.Player;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

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

    Context appContext;

    int bgColor;
    Paint mainPaint;

    GameObject boardSprite;
    Board boardData;

    Player p1;

    Camera mainCam;

    int screenWidth;
    int screenHeight;


    private GameData()
    {
        //Camera may be accessed before initGameData as it need to be updated on SurfaceCreated and surfaceChanged.
        mainCam = new Camera(0, 0, 1200, 1200);
    }

    public void setContext(Context c)
    {
        this.appContext = c;
    }

    public void initGameData()
    {
        bgColor = Color.BLACK;

        mainPaint = new Paint();
        mainPaint.setColor(Color.CYAN);


        boardSprite = new GameObject("board");

        //Load BoardData from Json.
        String data = "";
        try
        {
            //Read data from file.
            InputStream is = appContext.getResources().openRawResource(R.raw.squares);
            data = new Scanner(is).useDelimiter("\\A").next();
            is.close();

            //Deserialize data.
            Gson gson = new Gson();
            boardData = gson.fromJson(data, Board.class);
        } catch (Exception e)
        {
            Log.e("GameData", "Error loading Board Squares.");
        }

        p1 = new Player(boardData.squares.get(1));
    }


    public void setScreenSize(int width, int height)
    {
        screenWidth = width;
        screenHeight = height;

        mainCam.updateScreenSize(width, height);
    }

}
