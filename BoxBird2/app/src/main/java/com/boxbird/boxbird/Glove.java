package com.boxbird.boxbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by riasat.ullah on 12/5/2015.
 */
public class Glove extends GameObject {

    private Bitmap glove_picture;
    private Animation animation = new Animation();

    public Glove( Bitmap res, boolean up, int glove_width, int glove_height, int numFrames)
    {
        width = glove_width;
        height = glove_height;

        if (up){
            y = 0;
        } else{
            y = GamePanel.HEIGHT - height;
        }
        y = 200;
        x = GamePanel.WIDTH;

        Bitmap[] image = new Bitmap[ numFrames ];
        glove_picture = res;

        for( int i =0; i < image.length; i++ )
        {
            image[ i ] = Bitmap.createBitmap( glove_picture, i*width, 0, width, height );
        }

        animation.setFrames( image );
        animation.setDelay(10);
        //startTime = System.nanoTime();
    }

    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }

    public void update(){
        x -= GamePanel.MOVESPEED;
    }
}
