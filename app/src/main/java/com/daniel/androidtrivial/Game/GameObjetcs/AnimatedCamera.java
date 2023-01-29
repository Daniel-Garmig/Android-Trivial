package com.daniel.androidtrivial.Game.GameObjetcs;

import com.daniel.androidtrivial.Game.Animation.Animated;
import com.daniel.androidtrivial.Game.Animation.CamFollowAnim;
import com.daniel.androidtrivial.Game.Utils.Camera;
import com.daniel.androidtrivial.Game.Utils.Transform;

public class AnimatedCamera extends Camera implements Animated
{
    private boolean init = false;

    public CamFollowAnim animation;


    public AnimatedCamera()
    {
        transform = new Transform(0, 0, defaultSize.x, defaultSize.y);
    }

    public AnimatedCamera(int posX, int posY)
    {
        transform = new Transform(posX, posY, defaultSize.x, defaultSize.y);
    }

    public AnimatedCamera(int posX, int posY, int sizeX, int sizeY)
    {
        transform = new Transform(posX, posY, sizeX, sizeY);
    }


    public void initAnimation()
    {
        animation = new CamFollowAnim(transform);
        init = true;
    }

    @Override
    public void updateAnimation(float dt)
    {
        if(!init) { return; }

        animation.update(dt);
        updateScreenSize();
    }

    @Override
    public boolean isAnimationFinished()
    {
        return false;
    }
}
