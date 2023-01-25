package com.daniel.androidtrivial.Model;

import java.util.HashMap;

public class Player
{
    //Player ID, for internal reference.
    int id;

    String name;

    WedgesColors playerColor;

    //Store true if player have a given wedge.
    HashMap<WedgesColors, Boolean> wedges;
    //Player Stats.
    int score;
    int squaresTraveled;
    int questionsAnswered;
    int correctAnswers;
    int questionsOutOfTime;


    //Constructors.

    public Player(int id, String name, WedgesColors playerColor) {
        this.id = id;
        this.name = name;
        this.playerColor = playerColor;

        initVars();
    }

    public Player()
    {
        this.id = -1;
        this.name = "Player";
        this.playerColor = WedgesColors.blue;

        initVars();
    }

    private void initVars()
    {
        this.wedges = new HashMap<>();
        this.score = 0;
        this.squaresTraveled = 0;
        this.questionsAnswered = 0;
        this.correctAnswers = 0;
    }

    // Methods.


    public int getId() {return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public WedgesColors getPlayerColor() { return playerColor; }
    public void setPlayerColor(WedgesColors playerColor) { this.playerColor = playerColor; }

    public void setWedge(WedgesColors color, boolean haveIt)
    {
        wedges.put(color, haveIt);
    }
    public boolean haveWedge(WedgesColors color) { return wedges.get(color); }
    public boolean haveALllWedges()
    {
        //Check for false values.
        for(Boolean b : wedges.values())
        {
            if(!b) { return false; }
        }
        //None of the values was false.
        return true;
    }

    public int getScore() { return score; }
    public void addScorePoints(int pointsToAdd) { score += pointsToAdd; }

    public int getSquaresTraveled() { return squaresTraveled; }
    public void addSquaresTraveled(int amount) { squaresTraveled += amount; }

    public int getQuestionsAnswered() { return questionsAnswered; }
    public void addQuestionAnswered() { questionsAnswered++; }
    public void addQuestionAnswered(int amount) { questionsAnswered += amount; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void addCorrectAnswer() { correctAnswers++; }

    public int getQuestionsOutOfTime() { return questionsOutOfTime; }
    public void addQuestionOutOfTime() { questionsOutOfTime++; }
}
