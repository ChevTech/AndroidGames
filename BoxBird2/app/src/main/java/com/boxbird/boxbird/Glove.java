package com.boxbird.boxbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by riasat.ullah on 12/5/2015.
 */
import java.util.Random;

public class Glove extends GameObject{
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private int numframes;
    private boolean ground;
    private boolean shoot = false;
    private int rotation_degree = 0;
    private Matrix matrix = new Matrix();
    public boolean punched = false;

    public Glove(Bitmap res, int x, int w, int h, int numframes, boolean ground)
    {
        spritesheet = res;
        super.xCoordinate = x;
        super.yCoordinate = GamePanel.HEIGHT - h;
        width = w;
        height = h;
        this.numframes = numframes;
        this.ground = ground;

        if (ground == false){
            super.yCoordinate = 0;
            rotation_degree = 180;
        }

        matrix.postRotate(rotation_degree);

        speed = GamePanel.BACKGROUND_MOVESPEED;

        Bitmap[] image = new Bitmap[numframes];

        for(int i = 0; i< numframes;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height, matrix, true);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);
    }

    public void update()
    {
        xCoordinate += speed;
        animation.update();
    }

    public void updateNumFrames(int num){
        numframes = num;
    }

    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(), xCoordinate, yCoordinate,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth()
    {
        //offset slightly for more realistic collision detection
        return width-10;
    }

    public boolean getGround(){
        return ground;
    }

    // Checks if a glove has punched or not
    // If it has then returns true.
    // This will help us to alternate betweeen images.
    public boolean hasPunched(){
        return punched;
    }

    // Sets a boolean value to specify whether the glove
    // has shot out and punched or not. This helps to control
    // the punching motion.
    public void setPunched(boolean value){ punched = value; }

    // Updates the image of the glove in order to get the boxing motion
    public void updateImage(Bitmap res, int w, int h, int numframes){
        spritesheet = res;
        width = w;
        height = h;
        this.numframes = numframes;

        if (ground){
            super.yCoordinate = GamePanel.HEIGHT - h;
        }

        Bitmap[] image = new Bitmap[numframes];

        for(int i = 0; i< numframes;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height, matrix, true);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);
    }
}