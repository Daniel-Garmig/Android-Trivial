package com.daniel.androidtrivial.Model;

import androidx.lifecycle.ViewModel;

import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Model.BoardSquare;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Category;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionWithOptions;
import com.daniel.androidtrivial.Model.WedgesColors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Contains data related to game state and status.
 */
public class GameViewModel extends ViewModel
{
    //List of players.
    private ArrayList<Player> players = new ArrayList<>();

    //Store player ID on order. Created after orderGenerator stage.
    private int[] playerOrder;
    //Sotre player position ID given his ID.
    private HashMap<Integer, Integer> playerPositions = new HashMap<>();

    //Game Stage.
    private GameState stage = null;
    private int gameTurnPosition = -1;
    private int currentPlayerID = -1;

    //CategoryID - Color relation.
    private HashMap<WedgesColors, Integer> colorsCategories = new HashMap<>();


    private int diceRoll;

    private WedgesColors currentColor;
    private Category currentCategory;
    private QuestionWithOptions currentQuestion;
    private boolean isCurrentQuestionQuesito;

    //Used as ID for saving match every time GameFragment Loads.
    private String matchName = null;
    private GameState loadedState = null;


    public String getMatchName() { return matchName; }
    public void setMatchName(String matchName) { this.matchName = matchName; }

    public void setDiceRoll(int rs) { this.diceRoll = rs; }
    public int getDiceRoll() { return diceRoll; }

    public WedgesColors getCurrentColor() { return currentColor; }
    public void setCurrentColor(WedgesColors currentColor) { this.currentColor = currentColor; }

    public Category getCurrentCategory() { return currentCategory; }
    public void setCurrentCategory(Category currentCategory) { this.currentCategory = currentCategory; }

    public QuestionWithOptions getCurrentQuestion() { return currentQuestion; }
    public void setCurrentQuestion(QuestionWithOptions currentQuestion) { this.currentQuestion = currentQuestion; }

    public boolean isCurrentQuestionQuesito() { return isCurrentQuestionQuesito; }
    public void setCurrentQuestionQuesito(boolean currentQuestionQuesito) { isCurrentQuestionQuesito = currentQuestionQuesito; }

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

    public HashMap<WedgesColors, Integer> getColorsCategories() { return colorsCategories; }
    public void setColorsCategories(HashMap<WedgesColors, Integer> colorsCategories) { this.colorsCategories = colorsCategories; }

    public int getCurrentPlayerID() { return currentPlayerID; }
    public Player getCurrentPlayer() { return getPlayer(currentPlayerID); }
    public int getCurrentPlayerPosition() { return playerPositions.get(currentPlayerID); }


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


    public void updateCurrentQuestion()
    {
        BoardSquare squareData = GameData.getInstance().getSquare(getCurrentPlayerPosition());

        currentColor = squareData.categoryColor;
        isCurrentQuestionQuesito = squareData.isQuesito;
    }


    public SavedMatch generateSave()
    {
        SavedMatch save = new SavedMatch();
        save.name = matchName;
        save.playerList = players;
        save.playerOrder = playerOrder;
        save.playerPositions = playerPositions;
        save.stage = stage;
        save.gameTurnPosition = gameTurnPosition;
        save.colorsCategories = colorsCategories;

        return save;
    }

    public void loadSave(SavedMatch save)
    {
        this.matchName = save.name;
        this.players = (ArrayList<Player>) save.playerList;
        this.playerOrder = save.playerOrder;
        this.playerPositions  = save.playerPositions;
        this.loadedState  = save.stage;
        this.stage = GameState.LoadedGame;
        this.gameTurnPosition  = save.gameTurnPosition;
        this.colorsCategories  = save.colorsCategories;

        //Load other vars.
        if(gameTurnPosition != -1) {
            this.currentPlayerID = playerOrder[gameTurnPosition];
        }
    }
    
    public void continueLoadMatch()
    {
        this.stage = this.loadedState;
    }
}
