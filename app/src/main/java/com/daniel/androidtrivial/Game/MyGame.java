package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.util.Log;

import com.daniel.androidtrivial.Model.Board;
import com.daniel.androidtrivial.Model.BoardSquare;
import com.daniel.androidtrivial.Game.GameObjetcs.PlayerPiece;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_App;

import java.util.HashMap;
import java.util.List;

public class MyGame implements RTG_App
{
    private static final String TAG = "GameBoardApp";

    boolean doingStuff = false;


    @Override
    public void Start()
    {
        GameData.getInstance().initGameData();

        //ThreadOrchestrator
        ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgGameDataLoaded);
    }

    @Override
    public void Render(Canvas c)
    {
        //Clear Screen.
        GameData data = GameData.getInstance();
        c.drawColor(data.bgColor);

        data.boardSprite.Render(c, data.mainCam);

        for (PlayerPiece p : data.playerPieceList.values())
        {
            p.Render(c, data.mainCam);
        }
    }

    @Override
    public void Update(float dt)
    {
        //Log.i(TAG, "Game Updated!!");

        GameData d = GameData.getInstance();

        Camera cam = d.mainCam;
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

        if(doingStuff)
        {
            ThreadOrchestrator.getInstance().sendGameEndsInteraction();
            doingStuff = false;
        }
    }


    private void movePlayer(PlayerPiece p)
    {
        GameData d = GameData.getInstance();

        //get board data.
        Board b = d.boardData;

        //Search next square.
        BoardSquare lastSq = b.squares.get(p.sqId);
        BoardSquare nextSq = b.squares.get(lastSq.continuousSquares.get(1));

        p.setToSquare(nextSq);
    }

    public void startMoveState(int movs, Player currentPlayer)
    {
        GameData d = GameData.getInstance();

        PlayerPiece p = d.playerPieceList.get(currentPlayer.getId());

        //FIXME: Move don't go here!
        for(int i = 0; i < movs; i++)
        {
            movePlayer(p);
        }

        doingStuff = true;
    }


    public void createPlayers(List<Player> players)
    {
        GameData.getInstance().createPlayers(players);
    }

    public void updatePositions(HashMap<Integer, Integer> positions)
    {
        GameData.getInstance().updatePositions(positions);
    }
}
