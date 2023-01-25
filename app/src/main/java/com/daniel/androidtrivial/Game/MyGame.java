package com.daniel.androidtrivial.Game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.daniel.androidtrivial.Game.GameObjetcs.DirectionIndicator;
import com.daniel.androidtrivial.Game.GameObjetcs.PopupIcon;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.daniel.androidtrivial.Model.Board;
import com.daniel.androidtrivial.Model.BoardSquare;
import com.daniel.androidtrivial.Game.GameObjetcs.PlayerPiece;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGame implements RTG_App
{
    private static final String TAG = "GameBoardApp";


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

        //data.boardData.debugRender(c, cam);
        if(data.icon != null)
        {
            data.icon.Render(c, cam);
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
        if(d.icon != null)
        {
            d.icon.updateAnimation(dt);
        }
        //if(!d.icon.isAnimationFinished()) { animEnded = false; }


        //If we where doing anims and them have ended.
        if(d.doingAnimations && animEnded)
        {
            d.doingAnimations = false;

            if(d.currentState == GameState.Move)
            {
                //Move all pieces to the correct position.
                for(PlayerPiece p : d.playerPieceList.values())
                {
                    p.setToSquare(d.getSquare(p.sqId));
                }

                //No remaining movements.
                if(d.remainingMovs <= 0)
                {
                    //TODO: QuestionIcon.
                    createPopUpIcon("playerPiece");

                } else
                {
                    PlayerPiece currentPlayer = d.getCurrentPlayerPiece();
                    showMovementOptions(d.getSquare(currentPlayer.sqId));
                }
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
        if(d.waitingUserMovement && !d.doingAnimations)
        {
            //TODO: Mb move this into methods or smth.
            //Handle user input.
            d.waitingUserMovement = false;
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
                    //Wait for animations to end and receive new user input.
                    d.remainingMovs--;
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
            d.doingAnimations = true;

            //TODO: Test if final sq is empty.
        }

        //This is utilized by all interations to return!!
        if(d.remainingMovs <= 0 && !d.doingAnimations)
        {
            d.icon = null;
            ThreadOrchestrator.getInstance().sendGameEndsInteraction();
        }
    }

    private void showMovementOptions(BoardSquare currentSquare)
    {
        GameData d = GameData.getInstance();
        d.doingAnimations = false;
        d.waitingUserMovement = true;

        //Generate Possible directions.
        d.possibleDirections.clear();

        for(int sqID : currentSquare.continuousSquares)
        {
            //TODO: Check route is possible.

            //Get ContinuousSquare position.
            Vector2 initPos = currentSquare.pos;
            Vector2 endPos = d.getSquare(sqID).pos;
            DirectionIndicator di = new DirectionIndicator(initPos, endPos, sqID);
            d.possibleDirections.add(di);
        }

    }

    private void createPopUpIcon(String assetID)
    {
        GameData d = GameData.getInstance();

        d.icon = new PopupIcon(assetID);
        d.icon.transform.setPosition(d.getSquare(0).pos);
        d.icon.transform.setSize(0, 0);
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


    public void startRollDiceState()
    {
        GameData d = GameData.getInstance();
        d.currentState = GameState.RollDice;

        //TODO: Change Icon.
        createPopUpIcon("playerPiece");
    }

    public void startMoveState(int movs, Player currentPlayer)
    {
        GameData d = GameData.getInstance();
        d.currentState = GameState.Move;

        d.currentPlayerID = currentPlayer.getId();
        PlayerPiece p = d.getCurrentPlayerPiece();

        //Select initial direction.
        BoardSquare currentSquare = d.getSquare(p.sqId);
        showMovementOptions(currentSquare);

        d.remainingMovs = movs;
    }
}
