package com.daniel.androidtrivial.Model;

import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayList;

public class BoardSquare
{
    // //FIXME: But it results into duplicated data!!
    //Every square have a id.
    public int id;

    public WedgesColors categoryColor;
    public boolean isQuesito;

    public Vector2 pos;

    //Store continuous squares for navigating between them.
    public ArrayList<Integer> continuousSquares = new ArrayList<>();


    //Constructors.

    public BoardSquare() {}

    public BoardSquare(int id, Vector2 pos) {
        this.id = id;
        this.pos = pos;
    }

    public BoardSquare(int id, Vector2 pos, ArrayList<Integer> continuousSquares) {
        this.id = id;
        this.pos = pos;
        this.continuousSquares = continuousSquares;
    }

    public BoardSquare(int id, WedgesColors categoryColor, boolean isQuesito, Vector2 pos, ArrayList<Integer> continuousSquares) {
        this.id = id;
        this.categoryColor = categoryColor;
        this.isQuesito = isQuesito;
        this.pos = pos;
        this.continuousSquares = continuousSquares;
    }

    // Methods.
    public int getContinuousCount() { return continuousSquares.size(); }

}
