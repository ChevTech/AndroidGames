package com.boxbird.boxbird;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stoyta on 11/28/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 601;
    public static final int HEIGHT = 301;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background background;
    private Player player;
    private ArrayList<Glove> gloves = new ArrayList<Glove>();
    private int level = 1;
    private int total_updates = 1;

    public GamePanel( Context context)
    {
        super( context );

        getHolder().addCallback(this);

        thread = new MainThread( getHolder(), this );

        setFocusable( true );
    }

    @Override
    public void surfaceChanged( SurfaceHolder holder, int format, int width, int height )
    {

    }

    @Override
    public void surfaceDestroyed( SurfaceHolder holder )
    {
        boolean retry = true;

        while ( retry )
        {
            try{
                thread.setRunning(false);
                thread.join(); // What is this?
            }catch (InterruptedException e ){
                e.printStackTrace();
            }

            retry = false;
        }
    }

    @Override
    public void surfaceCreated( SurfaceHolder holder )
    {
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background_green));
        player = new Player( BitmapFactory.decodeResource( getResources(), R.drawable.bird ), 63, 50, 5 );
        thread.setRunning( true );
        thread.start();
    }

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {

        if( event.getAction() == MotionEvent.ACTION_DOWN )
        {
            if( !player.getPlaying()) {
                player.setPlaying(true);
            }
            else
            {
                player.setUp( false );
            }
            return true;
        }
        if( event.getAction() == MotionEvent.ACTION_UP)
        {
            player.setUp(true);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){

        }

        return super.onTouchEvent( event );
    }

    public void update()
    {
        if( player.getPlaying() )
        {
            background.update();
            player.update();
/*            addGloves();
            increaseLevel();
            for (int i=0; i < gloves.size(); i++){
                gloves.get(i).update();
            }*/
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
            player.draw(canvas);
            /*for (int i=0; i < gloves.size(); i++){
                gloves.get(i).draw(canvas);
            }*/
            canvas.restoreToCount(savedState);
        }
    }

    public void addGloves(){
        if (total_updates % 50 == 0){
            Random rand = new Random();
            int val = rand.nextInt(2);
            boolean up = true;
            if (val != 0){
                up = false;
            }
            Glove new_glove = new Glove( BitmapFactory.decodeResource( getResources(), R.drawable.boxing_glove ), up, 26, 21, 1 );
            gloves.add(new_glove);
        }
        total_updates += 1;
        //System.out.println("Updates: " + total_updates);
        System.out.println(gloves);
    }

    public void increaseLevel(){
        if (total_updates % 1000 == 0){
            level += 1;
        }
    }
}