package com.boxbird.boxbird;

import android.graphics.Rect;

/**
 * Created by Stoyta on 11/28/2015.
 */
public abstract class GameObject {
    protected int xCoordinate;
    protected int yCoordinate;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;

    public void setXCoordinate(int xCoordinate)
    {
        this.xCoordinate = xCoordinate;
    }

    public int getXCoordinate()
    {
        return xCoordinate;
    }

    public void setYCoordinate(int yCoordinate)
    {
        this.yCoordinate = yCoordinate;
    }

    public int getYCoordinate()
    {
        return yCoordinate;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public Rect getRectangle()
    {
        return new Rect(xCoordinate, yCoordinate + (int)(height * 0.1), xCoordinate + width, yCoordinate + (int)(height * 0.9) );
    }

}
