package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.util.Log;

import com.daniel.androidtrivial.Game.Data.Board;
import com.daniel.androidtrivial.Game.Data.BoardSquare;
import com.daniel.androidtrivial.Game.GameObjetcs.PlayerPiece;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_App;

public class MyGame implements RTG_App
{
    private static final String TAG = "GameBoardApp";

    @Override
    public void Start()
    {
        GameData.getInstance().initGameData();

        //ThreadOrchestrator
        ThreadOrchestrator.getInstance().onDataLoaded(ThreadOrchestrator.msgGameDataLoaded);
    }

    @Override
    public void Render(Canvas c)
    {
        //Clear Screen.
        GameData data = GameData.getInstance();
        c.drawColor(data.bgColor);

        data.boardSprite.Render(c, data.mainCam);

        if(data.p1 != null) { data.p1.Render(c); }
    }

    @Override
    public void Update(float dt)
    {
        //Log.i(TAG, "Game Updated!!");

        GameData d = GameData.getInstance();

        Camera cam = d.mainCam;

        /*cam.transform.moveAmount(1, 1);
        Vector2 pos = cam.transform.getPosition();
        if(pos.x > 200)
        {
            cam.transform.setPosition(0, (int) pos.y);
        }

        if(pos.y > 200)
        {
            cam.transform.setPosition((int) pos.x, 0);
        }*/
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


    public void onSurfaceTouch()
    {
        Log.i("MyGame", "Touched!!");

        GameData d = GameData.getInstance();

        //get board data.
        Board b = d.boardData;
        PlayerPiece p = d.p1;

        //Search next square.
        BoardSquare lastSq = b.squares.get(p.sqId);
        BoardSquare nextSq = b.squares.get(lastSq.continuousSquares.get(1));

        p.moveToSquare(nextSq);
    }


    private void movePlayer(PlayerPiece p1)
    {

    }

    public void startMoveState(int movs)
    {
        for(int i = 0; i < movs; i++)
        {
            onSurfaceTouch();
        }

        //FIXME: Esto debería llamarse después de terminar las animaciones de movimiento.
        ThreadOrchestrator.getInstance().sendGameEndsInteraction();
    }
}
