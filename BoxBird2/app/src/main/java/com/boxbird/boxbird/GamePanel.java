package com.boxbird.boxbird;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.security.PublicKey;

/**
 * Created by Stoyta on 11/28/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 298;
    public static final int HEIGHT = 341;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background background;
    private Player player;

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
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player = new Player( BitmapFactory.decodeResource( getResources(), R.drawable.bird ), 50, 40, 4 );
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
                player.setUp( true );
            }
            return true;
        }
        if( event.getAction() == MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent( event );
    }

    public void update()
    {
        if( player.getPlaying() )
        {
            background.update();
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if (canvas != null)
        {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
            player.draw( canvas );
            canvas.restoreToCount(savedState);
        }
    }
}
