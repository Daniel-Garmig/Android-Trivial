package com.daniel.androidtrivial.Game.Data;

import java.util.HashMap;

/**
 * Data class deserialized from JSON.
 * Store board squares.
 */
public class Board
{
    public HashMap<Integer, BoardSquare> squares = new HashMap<>();

    Board() {}
}
