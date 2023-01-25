package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Game.Animation.Animated;
import com.daniel.androidtrivial.Game.Animation.MoveAnimation;
import com.daniel.androidtrivial.Game.Animation.ScaleAnimation;
import com.daniel.androidtrivial.Game.Utils.Transform;
import com.daniel.androidtrivial.Game.Utils.Vector2;

public class PopupIcon extends GameObject implements Animated
{
    private ScaleAnimation animation;


    public PopupIcon(String assetName)
    {
        super(assetName);
        transform = new Transform(0, 0,0 ,0);

        animation = new ScaleAnimation(transform);
        animation.velocity = 200;

        animation.addTargetSize(new Vector2(300, 300));
    }


    @Override
    public void updateAnimation(float dt)
    {
        animation.update(dt);
    }

    @Override
    public boolean isAnimationFinished()
    {
        return animation.isFinished();
    }
    
    public void addTargetSize(Vector2 size)
    {
        animation.addTargetSize(size);
    }
}
