package com.daniel.androidtrivial.Model;

import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;

import java.util.HashMap;
import java.util.List;

public class SavedMatch
{
    public String name;

    public int[] playerOrder;
    public HashMap<Integer, Integer> playerPositions;
    public List<Player> playerList;

    //Game Stage.
    public GameState stage;
    public int gameTurnPosition;

    //CategoryID - Color relation.
    public HashMap<WedgesColors, Integer> colorsCategories;

    public SavedMatch() {}
}
