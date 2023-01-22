package com.daniel.androidtrivial.Game;

import androidx.lifecycle.ViewModel;

import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.GameState;

import java.util.ArrayList;
import java.util.HashMap;

public class GameViewModel extends ViewModel
{
    //List of players.
    private ArrayList<Player> players;

    //Store player ID on order. Created after orderGenerator stage.
    private int[] playerOrder;
    //Sotre player position ID given his ID.
    private HashMap<Integer, Integer> playerPositions;

    //Game Stage.
    private GameState stage = null;
    private int gameTurnPosition = -1;
    private int currentPlayerID = -1;


    int diceRoll;



    public void setDiceRoll(int rs) { this.diceRoll = rs; }
    public int getDiceRoll() { return diceRoll; }

    public ArrayList<Player> getPlayers() { return players; }
    public void setPlayers(ArrayList<Player> players) { this.players = players; }

    public int[] getPlayerOrder() { return playerOrder; }
    public void setPlayerOrder(int[] playerOrder) { this.playerOrder = playerOrder; }

    public HashMap<Integer, Integer> getPlayerPositions() { return playerPositions; }
    public void setPlayerPositions(HashMap<Integer, Integer> playerPositions) { this.playerPositions = playerPositions; }

    public GameState getStage() { return stage; }
    public void setStage(GameState stage)
    {
        this.stage = stage;
    }


    public int getCurrentPlayerID() { return currentPlayerID; }
    public Player getCurrentPlayer() { return getPlayer(currentPlayerID); }


    public Player getPlayer(int playerID)
    {
        for(Player p : players)
        {
            if(p.getId() == playerID) { return p; }
        }

        return null;
    }


    public void nextTurn()
    {
        //Reset if it's at the end.
        if(gameTurnPosition >= playerOrder.length - 1)
        {
            gameTurnPosition = -1;
        }

        //Next turn.
        gameTurnPosition++;
        currentPlayerID = playerOrder[gameTurnPosition];
    }


}
