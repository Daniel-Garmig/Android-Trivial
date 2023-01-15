package com.daniel.androidtrivial.Game.GameObjetcs;

import android.graphics.Canvas;

import com.daniel.androidtrivial.Game.Data.BoardSquare;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Renderizable;
import com.daniel.androidtrivial.Game.Utils.Sprite;
import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayList;

public class Player extends GameObject
{

    public Player(BoardSquare startSq)
    {
        super("playerPiece");
        sqId = startSq.id;
        moveToSquare(startSq);
    }

    //Store current sqId.
    public int sqId;

    public void moveToSquare(BoardSquare sq)
    {
        transform.setPosition(sq.pos.x, sq.pos.y);
        sqId = sq.id;
    }
}
