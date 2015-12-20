package com.boxbird.boxbird;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    private long glove_start_time;
    private ArrayList<Glove> gloves;
    private Random rand = new Random();
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

        gloves = new ArrayList<Glove>();
        glove_start_time = System.nanoTime();

        // 3 second timer before starting the game
        /*
        new CountDownTimer(3000, 1000){

            public void onTick(long millisUntilFinished){
                countDownText.setText(millisUntilFinished/1000);
            }

            public void onFinish(){
                countDownText.setText("Go!");

            }
        }.start();
        */
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

            //add missiles on timer
            long glove_elapsed = (System.nanoTime() - glove_start_time)/1000000;
            if(total_updates % 50 == 0){

                boolean ground = false;
                if (rand.nextInt(2) == 0){
                    ground = true;
                }
                System.out.println("add a glove");

                gloves.add(new Glove(BitmapFactory.decodeResource(getResources(),R.drawable.boxing_glove),
                        WIDTH - 30, 26, 21, player.getScore(), 1, ground));
                //reset timer
                glove_start_time = System.nanoTime();
            }
            //loop through every missile and check collision and remove
            for(int i = 0; i<gloves.size();i++)
            {
                //update missile
                gloves.get(i).update();

                if(collision(gloves.get(i), player))
                {
                    player.setPlaying(false);
                    System.out.println("Hit");
                    break;
                }
                //remove missile if it is way off the screen
                if(gloves.get(i).getXCoordinate()< -100)
                {
                    gloves.remove(i);
                    break;
                }
            }
            total_updates += 1;
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

            for(Glove g: gloves)
            {
                g.draw(canvas);
            }
            total_updates += 1;
            System.out.println("total updates: " + total_updates);
            canvas.restoreToCount(savedState);
        }
    }


    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }
}