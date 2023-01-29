package com.daniel.androidtrivial.Game.Animation;

import android.graphics.RectF;

import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

public class CamFollowAnim implements Animation
{
    public float velocity = 100;

    private final float followVelocity = 120;
    private final float returnVelocity = 240;
    //Default size and position. Used when no target is set.
    private final Vector2 defaultPos = new Vector2(600, 600);
    private final Vector2 defaultSize = new Vector2(1200, 1200);

    //Size when following a target.
    private final Vector2 followSize = new Vector2(750, 750);


    private final ScaleAnimation scaleAnimation;

    private Transform target;
    private final Transform camTransform;

    private Vector2 moveToPos;


    public CamFollowAnim(Transform camTransform)
    {
        this.camTransform = camTransform;
        scaleAnimation = new ScaleAnimation(camTransform);
        scaleAnimation.velocity = 300;
    }

    @Override
    public void update(float dt)
    {
        scaleAnimation.update(dt);

        //Update movePos to target.
        if(target != null) { moveToPos = target.getPosition(); }

        //Return if there is no movement to do.
        if(moveToPos == null) { return; }

        //Move to movePos.
        RectF camRect = camTransform.getRectF();
        Vector2 transformCenterPos = new Vector2(camRect.centerX(), camRect.centerY());
        Vector2 director = Vector2.getDirector(transformCenterPos, moveToPos);

        double distance = director.getLength();
        //Return if we are almost there (so it don't shakes).
        if(distance < 10)
        {
            moveToPos = null;
            return;
        }

        //Move.
        Vector2 movementVector = director.normalize().multiplyScalar(velocity * dt);
        camTransform.moveAmount(movementVector.x, movementVector.y);
    }

    @Override
    public boolean isFinished() {
        return Animation.super.isFinished();
    }

    public void setTarget(Transform target)
    {
        this.target = target;
        velocity = followVelocity;

        scaleAnimation.targetSizes.clear();
        scaleAnimation.addTargetSize(followSize);
    }

    public void removeTarget()
    {
        target = null;
        velocity = returnVelocity;

        //Set scaleAnim to default size.
        scaleAnimation.targetSizes.clear();
        scaleAnimation.addTargetSize(defaultSize);

        //Set targetPos = defaultPos.
        moveToPos = defaultPos;
    }
}
