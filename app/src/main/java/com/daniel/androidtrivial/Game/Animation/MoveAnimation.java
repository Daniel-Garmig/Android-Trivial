package com.daniel.androidtrivial.Game.Animation;

import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayDeque;

public class MoveAnimation implements Animation
{
    Transform transform;
    ArrayDeque<Vector2> targetPositions;

    public float velocity = 10;

    public MoveAnimation(Transform transform)
    {
        this.transform = transform;
        targetPositions = new ArrayDeque<>();
    }


    @Override
    public void update(float dt)
    {
        //We have finished
        if(targetPositions.isEmpty()) { return; }

        Vector2 director = Vector2.getDirector(transform.getPosition(), targetPositions.getFirst());

        //Check distance between positions.
        double distance = director.getLength();

        if(distance < 10)
        {
            //Next position.
            targetPositions.pollFirst();
            //We might have finish anim.
            if(targetPositions.isEmpty()) { return; }
        }

        //Move towards target.
        Vector2 movementVector = director.normalize().multiplyScalar(velocity * dt);

        transform.moveAmount(movementVector.x, movementVector.y);
    }

    @Override
    public boolean isFinished()
    {
        return targetPositions.isEmpty();
    }


    public void addTargetPosition(Vector2 pos) { targetPositions.addLast(pos);}
}
