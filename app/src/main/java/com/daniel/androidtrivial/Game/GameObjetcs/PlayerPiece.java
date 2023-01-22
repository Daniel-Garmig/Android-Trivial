package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Model.BoardSquare;

public class PlayerPiece extends GameObject
{
    public PlayerPiece() {
        super("playerPiece");
    }

    public PlayerPiece(BoardSquare startSq)
    {
        super("playerPiece");
        sqId = startSq.id;
        setToSquare(startSq);
    }

    //Store current sqId.
    public int sqId;

    public void setToSquare(BoardSquare sq)
    {
        transform.setPosition(sq.pos.x, sq.pos.y);
        sqId = sq.id;
    }
}
