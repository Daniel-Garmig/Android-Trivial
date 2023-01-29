package com.daniel.androidtrivial.Game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.daniel.androidtrivial.Game.GameObjetcs.AnimatedCamera;
import com.daniel.androidtrivial.Game.GameObjetcs.DirectionIndicator;
import com.daniel.androidtrivial.Game.GameObjetcs.PopupIcon;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.daniel.androidtrivial.Model.Board;
import com.daniel.androidtrivial.Game.GameObjetcs.GameObject;
import com.daniel.androidtrivial.Game.GameObjetcs.PlayerPiece;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Model.BoardSquare;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.R;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    int screenWidth;
    int screenHeight;

    AnimatedCamera mainCam;

    int bgColor;
    Paint mainPaint;


    GameObject boardSprite;
    Board boardData;

    HashMap<Integer, PlayerPiece> playerPieceList;

    ArrayList<DirectionIndicator> possibleDirections;
    PopupIcon icon;


    int remainingMovs;
    int currentPlayerID;

    GameState currentState;

    boolean doingAnimations = false;
    boolean waitingUserMovement = false;


    private GameData()
    {
        //Camera may be accessed before initGameData as it need to be updated on SurfaceCreated and surfaceChanged.
        mainCam = new AnimatedCamera(0, 0, 1200, 1200);
        mainCam.initAnimation();
    }

    public void initGameData()
    {
        bgColor = Color.BLACK;

        mainPaint = new Paint();
        mainPaint.setColor(Color.CYAN);


        boardSprite = new GameObject("board");

        if(playerPieceList == null)
        {
            playerPieceList = new HashMap<>();
        }
        if(possibleDirections == null)
        {
            possibleDirections = new ArrayList<>();
        }

        if(currentState == null)
        {
            currentState = GameState.StartGame;
        }

        icon = null;
    }

    public void gameDataResets()
    {
        playerPieceList = null;
        possibleDirections = null;
        currentState = null;

        doingAnimations = false;
        waitingUserMovement = false;
        remainingMovs = 0;
        currentPlayerID = -1;
        icon = null;
    }

    public void loadBoardData(Context appContext)
    {
        if(boardData != null) { return; }

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
        Log.i("GameData", "Board Data Loaded");
    }

    public BoardSquare getSquare(int id)
    {
        return boardData.squares.get(id);
    }

    public void setScreenSize(int width, int height)
    {
        screenWidth = width;
        screenHeight = height;

        mainCam.updateScreenSize(width, height);
    }


    public void createPlayers(List<Player> players)
    {
        //Clear previous data.
        playerPieceList.clear();
        icon = null;

        for(Player p : players)
        {
            PlayerPiece piece = new PlayerPiece();
            //TODO: Custom Color.
            piece.sqId = 1;
            piece.transform.setSize(100,100);
            playerPieceList.put(p.getId(), piece);
        }
    }

    public void updatePositions(HashMap<Integer, Integer> positions)
    {
        for(Map.Entry<Integer, Integer> playerPos : positions.entrySet())
        {
            BoardSquare moveSquare = boardData.squares.get(playerPos.getValue());
            playerPieceList.get(playerPos.getKey()).setToSquare(moveSquare);
        }
    }

    public boolean isSquareOccupied(int sqID)
    {
        for(PlayerPiece p : playerPieceList.values())
        {
            if(p.sqId == sqID) { return true; }
        }
        return false;
    }

    public int getPlayerSquareID(int playerID)
    {
        PlayerPiece piece = playerPieceList.get(playerID);
        if(piece == null) { return -1; }
        return piece.sqId;
    }

    public PlayerPiece getCurrentPlayerPiece()
    {
        return playerPieceList.get(currentPlayerID);
    }



}
