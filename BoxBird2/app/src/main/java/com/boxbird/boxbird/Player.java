package com.boxbird.boxbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;

/**
 * Created by Stoyta on 11/28/2015.
 */
public class Player extends GameObject{

        private Bitmap spritesheet;
        private int score;
        private double dya;
        private boolean up;
        private boolean playing;
        private Animation animation = new Animation();
        private long startTime;

        public Player( Bitmap res, int w, int h, int numFrames)
        {
            x = GamePanel.WIDTH/10;
            y = GamePanel.HEIGHT/2;
            dy = 0;
            score = 0;
            height = h;
            width  = w;

            Bitmap[] image = new Bitmap[ numFrames ];
            spritesheet = res;

            for( int i =0; i < image.length; i++ )
            {
                image[ i ] = Bitmap.createBitmap( spritesheet, i*width, 0, width, height );
            }

            animation.setFrames( image );
            animation.setDelay( 10 );
            startTime = System.nanoTime();

        }

        public void setUp( boolean b )
        {
            up = b;
        }

        public void update()
        {
            long elapsed = (System.nanoTime() - startTime)/1000000;

            if( elapsed > 100 )
            {
                score ++;
                startTime = System.nanoTime();
            }

            animation.update();

            if(up) {
                y = (int) (y - (GamePanel.HEIGHT/15));
                x = (int) (x + (GamePanel.MOVESPEED * 2));
            }else{
                y = (int)(y + (GamePanel.HEIGHT/15));
                //x = (int) (x + (GamePanel.WIDTH/400));
                x = (int) (x + (GamePanel.MOVESPEED * 2));
            }

            // make sure that the bird does not cross
            // side borders
            if( x > (5 * GamePanel.WIDTH)/6) {
                x = (5 * GamePanel.WIDTH)/6; //- width;
            }
            if( x < 0 ){
                x = 0;
            }

            // make sure that the bird does not cross
            // the bottom and top border
            if (y > GamePanel.HEIGHT - (height * 2)){
                y = GamePanel.HEIGHT - (height * 2);
            }
            if (y < height){
                y = height;
            }

            // keep the bird moving with the background
            // if it is not accelerating as it moves up
            x -= GamePanel.MOVESPEED;
        }

    public void draw( Canvas canvas )
    {
        canvas.drawBitmap( animation.getImage(), x, y, null);
    }

    public int getScore()
    {
        return score;
    }

    public boolean getPlaying()
    {
        return playing;
    }

    public void setPlaying( boolean b )
    {
        playing = b;
    }

    public void resetDYA()
    {
        dya = 0;
    }

    public void resetScore()
    {
        score = 0;
    }

}
