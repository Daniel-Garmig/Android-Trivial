package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.daniel.androidtrivial.Game.GameObjetcs.DirectionIndicator;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.daniel.androidtrivial.Model.Board;
import com.daniel.androidtrivial.Model.BoardSquare;
import com.daniel.androidtrivial.Game.GameObjetcs.PlayerPiece;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGame implements RTG_App
{
    private static final String TAG = "GameBoardApp";

    //FIXME: This should be saved somewhere else in case app changes.
    boolean doingAnimations = false;
    boolean waitingUserMovement = false;


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
        GameData data = GameData.getInstance();
        Camera cam = data.mainCam;

        //Clear Screen.
        c.drawColor(data.bgColor);

        data.boardSprite.Render(c, cam);

        for (PlayerPiece p : data.playerPieceList.values())
        {
            p.Render(c, cam);
        }
        for(DirectionIndicator di : data.possibleDirections)
        {
            di.Render(c, cam);
        }


    }

    @Override
    public void Update(float dt)
    {
        //Log.i(TAG, "Game Updated!!");

        GameData d = GameData.getInstance();

        Camera cam = d.mainCam;


        //Update Anims.
        boolean animEnded = true;
        for (PlayerPiece p : d.playerPieceList.values())
        {
            p.updateAnimation(dt);
            if(!p.isAnimationFinished())
            {
                animEnded = false;
            }
        }

        if(animEnded)
        {
            doingAnimations = false;
            //Move all pieces to the correct position.
            for(PlayerPiece p : d.playerPieceList.values())
            {
                p.setToSquare(d.getSquare(p.sqId));
            }
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


    public void onSurfaceTouch(MotionEvent event)
    {
        Log.i("MyGame", "Touched!!");
        GameData d = GameData.getInstance();

        if(event.getActionMasked() != MotionEvent.ACTION_DOWN) { return; }

        //TODO: Check event type.
        if(waitingUserMovement && !doingAnimations)
        {
            //TODO: Mb move this into methods or smth.
            //Handle user input.
            waitingUserMovement = false;
            Vector2 touchPos = new Vector2(event.getX(), event.getY());

            //Calculate closer DirectionPointer.
            int closestIndex = -1;
            double closestDistance = Double.MAX_VALUE;
            for(int i = 0; i < d.possibleDirections.size(); i++)
            {
                DirectionIndicator di = d.possibleDirections.get(i);
                //Calculate vector length.
                double length = Vector2.getDirector(touchPos, di.endPos).getLength();
                //If closer, update closest one.
                if(length < closestDistance)
                {
                    closestIndex = i;
                    closestDistance = length;
                }
            }

            //We got direction.
            DirectionIndicator closestIndicator = d.possibleDirections.get(closestIndex);
            PlayerPiece p = d.getCurrentPlayerPiece();

            int lastSquareID = p.sqId;
            BoardSquare nextSquare = d.getSquare(closestIndicator.sqID);


            //Add movements to that direction.
            for(; d.remainingMovs > 0 ; d.remainingMovs--)
            {
                //Move to next Square.
                p.addMovementTarget(nextSquare.pos);
                p.sqId = nextSquare.id;

                //If intersection -> ask player.
                if(nextSquare.continuousSquares.size() > 2)
                {
                    //TODO.
                    //  Esperar que terminen las anims y volver a recibir input.
                    break;
                }
                //Look for other id -> There are only 2.
                ArrayList<Integer> possibleNextSquares = nextSquare.continuousSquares;
                if(possibleNextSquares.get(0) != lastSquareID)
                {
                    nextSquare = d.getSquare(possibleNextSquares.get(0));
                }
                else
                {
                    nextSquare = d.getSquare(possibleNextSquares.get(1));
                }
                lastSquareID = p.sqId;
            }

            d.possibleDirections.clear();
            doingAnimations = true;
        }

        if(d.remainingMovs <= 0 && !doingAnimations)
        {
            ThreadOrchestrator.getInstance().sendGameEndsInteraction();
        }
    }


    private void movePlayer(PlayerPiece p)
    {
        GameData d = GameData.getInstance();

        //get board data.
        Board b = d.boardData;

        //Search next square.
        //TODO: Check if more than 2 -> Ask player.
        BoardSquare lastSq = b.squares.get(p.sqId);
        BoardSquare nextSq = b.squares.get(lastSq.continuousSquares.get(1));

        Vector2 nextPos = nextSq.pos;
        p.addMovementTarget(nextPos);
        p.sqId = nextSq.id;
    }

    private void showMovementOptions(BoardSquare currentSquare)
    {
        GameData d = GameData.getInstance();
        doingAnimations = false;
        waitingUserMovement = true;

        //Generate Possible directions.
        d.possibleDirections.clear();

        for(int sqID : currentSquare.continuousSquares)
        {
            //Get ContinuousSquare position.
            Vector2 initPos = currentSquare.pos;
            Vector2 endPos = d.getSquare(sqID).pos;
            DirectionIndicator di = new DirectionIndicator(initPos, endPos, sqID);
            d.possibleDirections.add(di);
        }

    }

    // Handler / State Methods.

    public void createPlayers(List<Player> players)
    {
        GameData.getInstance().createPlayers(players);
    }

    public void updatePositions(HashMap<Integer, Integer> positions)
    {
        GameData.getInstance().updatePositions(positions);
    }


    public void startMoveState(int movs, Player currentPlayer)
    {
        GameData d = GameData.getInstance();
        d.currentPlayerID = currentPlayer.getId();
        PlayerPiece p = d.getCurrentPlayerPiece();

        //Select initial direction.
        BoardSquare currentSquare = d.getSquare(p.sqId);
        showMovementOptions(currentSquare);

        d.remainingMovs = movs;
    }
}
