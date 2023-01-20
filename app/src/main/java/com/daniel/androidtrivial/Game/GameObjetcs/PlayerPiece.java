package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Game.Data.BoardSquare;

public class PlayerPiece extends GameObject
{
    public PlayerPiece() {
        super("playerPiece");
    }

    public PlayerPiece(BoardSquare startSq)
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
