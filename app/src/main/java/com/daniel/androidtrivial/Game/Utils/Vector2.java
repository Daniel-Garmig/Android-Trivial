package com.daniel.androidtrivial.Game.Utils;

public class Vector2
{
    public float x;
    public float y;

    public Vector2() {}

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2 normalize()
    {
        double length = getLength();

        //TODO: TEST FLOAT CASTING!!!
        Vector2 normalized = new Vector2();
        normalized.x = (float) (x/length);
        normalized.y = (float) (y/length);

        return normalized;
    }

    /**
     * Get length of a vector.
     * @return Length of the vector.
     */
    public double getLength()
    {
        return Math.sqrt(x*x + y*y);
    }


    public Vector2 sum(Vector2 other)
    {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other)
    {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 multiplyScalar(float scalar)
    {
        return new Vector2(this.x * scalar, this.y * scalar);
    }



    // Static Methods!

    /**
     * Return "Vector director" dados dos puntos (representados mediante vectores).
     * @param inPos Initial position.
     * @param finPos Final position.
     * @return Vector Director entre los puntos.
     */
    public static Vector2 getDirector(Vector2 inPos, Vector2 finPos)
    {
        return new Vector2(finPos.x - inPos.x, finPos.y - inPos.y);
    }

    //TODO: Quizás añadir un getDirector sobrecargado con floats en vez de vectores.
}
