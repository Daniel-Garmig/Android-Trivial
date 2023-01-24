package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Game.Animation.Animated;
import com.daniel.androidtrivial.Game.Animation.MoveAnimation;
import com.daniel.androidtrivial.Game.Animation.ScaleAnimation;

public class PopupIcon extends GameObject implements Animated
{
    private ScaleAnimation animation;


    public PopupIcon(String assetName)
    {
        super(assetName);
        animation = new ScaleAnimation(transform);
        animation.velocity = 30;
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
}
