package com.boxbird.boxbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Stoyta on 11/28/2015.
 */
public class Player extends GameObject{

        private Bitmap spritesheet;
        private int score;
        private int playerHorizontalAcceleration;
        private double dya;
        private double gravity;
        private boolean up;
        private boolean playing;
        private Animation animation = new Animation();
        private long startTime;



        public Player( Bitmap res, int w, int h, int numFrames)
        {

            // Set the location of the bird in relation to the display screen
            xCoordinate = GamePanel.WIDTH/8;
            yCoordinate = GamePanel.HEIGHT/2;

            dy = 0;
            score = 0;
            height = h;
            width  = w;

            // Reverse the movement direction of the screen for the bird
            playerHorizontalAcceleration = Math.abs( GamePanel.BACKGROUND_MOVESPEED ) * 2;

            // Bird gravity
            gravity = -9.8;

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

        public void update()
        {
            // Set-up timer to track animation update frequency
            long elapsed = ( System.nanoTime() - startTime )/1000000;

            if( elapsed > 100 )
            {
                score ++;
                startTime = System.nanoTime();
            }

            // Update animation and keep the bird horizontally fixed.
            animation.update();
            //xCoordinate = playerHorizontalAcceleration;

            if(up) {
                yCoordinate = (int) ( yCoordinate - ( GamePanel.HEIGHT/10 ) );
                //xCoordinate = (int) ( xCoordinate + playerHorizontalAcceleration );
            }else{
                yCoordinate = (int) ( yCoordinate - gravity );
                //xCoordinate = (int) ( xCoordinate + ( GamePanel.BACKGROUND_MOVESPEED * 2 ) );
            }

            // make sure that the bird does not cross side borders
            if( xCoordinate > ( 5 * GamePanel.WIDTH )/6 ) {
                xCoordinate += playerHorizontalAcceleration;
            }

            // make sure that the bird does not cross the bottom or top border
            if( yCoordinate > GamePanel.HEIGHT - ( height ) ){
                yCoordinate = GamePanel.HEIGHT - ( height );
            }

            if( yCoordinate <= 0 ){
                yCoordinate = 0;
            }
        }

    public void draw( Canvas canvas )
    {
        canvas.drawBitmap( animation.getImage(), xCoordinate, yCoordinate, null);
    }

    public void setUp( boolean b )
    {
        up = b;
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
