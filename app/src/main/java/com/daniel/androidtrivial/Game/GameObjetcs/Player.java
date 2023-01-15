package com.daniel.androidtrivial.Game.GameObjetcs;

import android.graphics.Canvas;

import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Renderizable;
import com.daniel.androidtrivial.Game.Utils.Sprite;
import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayList;

public class Player extends GameObject
{
    //TODO: Montar un sistema "global" de casillas.
    ArrayList<Vector2> positions;


    public Player()
    {
        super("playerPiece");
    }

}
