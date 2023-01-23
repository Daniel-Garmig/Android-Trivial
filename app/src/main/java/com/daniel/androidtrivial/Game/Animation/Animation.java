package com.daniel.androidtrivial.Game.Animation;

public interface Animation
{
    boolean isFinished = false;

    public void update(float dt);

    public default boolean isFinished() { return isFinished; }
}
