package com.daniel.androidtrivial.Model;

import java.util.HashMap;

public class Player
{
    //Player ID, for internal reference.
    int id;

    String name;
    int score;

    WedgesColors playerColor;

    //Store true if player have a given wedge.
    HashMap<WedgesColors, Boolean> wedges;


    //Constructors.

    public Player(int id, String name, WedgesColors playerColor) {
        this.id = id;
        this.name = name;
        this.playerColor = playerColor;

        this.score = 0;
        this.wedges = new HashMap<>();
    }

    public Player()
    {
        this.id = -1;
        this.name = "Player";

        this.playerColor = WedgesColors.blue;
        this.score = 0;
        this.wedges = new HashMap<>();
    }

    // Methods.


    public int getId() {return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public WedgesColors getPlayerColor() { return playerColor; }
    public void setPlayerColor(WedgesColors playerColor) { this.playerColor = playerColor; }
}
