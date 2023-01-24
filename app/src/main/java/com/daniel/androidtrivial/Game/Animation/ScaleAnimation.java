package com.daniel.androidtrivial.Game.Animation;

import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

import java.util.ArrayDeque;

public class ScaleAnimation implements Animation
{
    Transform transform;
    ArrayDeque<Vector2> targetSizes;

    public float velocity = 25;

    public ScaleAnimation(Transform transform)
    {
        this.transform = transform;
        targetSizes = new ArrayDeque<>();
    }


    @Override
    public void update(float dt)
    {
        //We have finished
        if(targetSizes.isEmpty()) { return; }

        //"Direction" between sizes
        Vector2 director = Vector2.getDirector(transform.getSize(), targetSizes.getFirst());

        //Check distance between positions.
        double distance = director.getLength();

        if(distance < 10)
        {
            //Next position.
            targetSizes.pollFirst();
            //We might have finish anim.
            if(targetSizes.isEmpty()) { return; }
        }

        //Resize
        Vector2 dtVector = director.normalize().multiplyScalar(velocity * dt);
        transform.resizeAmount(dtVector.x, dtVector.y);
    }

    @Override
    public boolean isFinished() {
        return Animation.super.isFinished();
    }
}
