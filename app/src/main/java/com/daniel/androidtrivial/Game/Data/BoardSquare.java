package com.daniel.androidtrivial.Game.Data;

import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayList;

public class BoardSquare
{
    // //FIXME: But it results into duplicated data!!
    //Every square have a id.
    public int id;

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


    // Methods.
    public int getContinuousCount() { return continuousSquares.size(); }
}
