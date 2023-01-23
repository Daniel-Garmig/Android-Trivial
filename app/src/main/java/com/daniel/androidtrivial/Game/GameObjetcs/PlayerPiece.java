package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Game.Animation.Animated;
import com.daniel.androidtrivial.Game.Animation.MoveAnimation;
import com.daniel.androidtrivial.Game.Utils.Vector2;
import com.daniel.androidtrivial.Model.BoardSquare;

public class PlayerPiece extends GameObject implements Animated
{
    private MoveAnimation animation;

    public PlayerPiece()
    {
        super("playerPiece");
        animation = new MoveAnimation(transform);
        animation.velocity = 80;
    }

    public PlayerPiece(BoardSquare startSq)
    {
        super("playerPiece");
        sqId = startSq.id;
        setToSquare(startSq);

        animation = new MoveAnimation(transform);
        animation.velocity = 5;
    }

    //Store current sqId.
    public int sqId;

    public void setToSquare(BoardSquare sq)
    {
        transform.setPosition(sq.pos.x, sq.pos.y);
        sqId = sq.id;
    }

    @Override
    public void updateAnimation(float dt)
    {
        animation.update(dt);
    }

    @Override
    public boolean isAnimationFinished() {
        return animation.isFinished();
    }


    public void addMovementTarget(Vector2 target) { animation.addTargetPosition(target); }
}
