package com.boxbird.boxbird;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
    public static final int BACKGROUND_MOVESPEED = -10;
    public static final long second = 1000000000;
    private MainThread thread;
    private Background background;
    private Player player;
    private long game_start_time;
    private ArrayList<Glove> gloves;
    private Random rand = new Random();
    private int score = 0;
    private long score_timer;
    private boolean gloves_on = false;
    private boolean ground = false;

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
        if( event.getAction() == MotionEvent.ACTION_DOWN ) {
            if( !player.getPlaying()) {
                player.setPlaying(true);
                game_start_time = System.nanoTime();
                score_timer = System.nanoTime();
            }
            else {
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

    public void update() {
        if(player.getPlaying()) {
            background.update();
            player.update();
            add_gloves();

            //loop through every missile and check collision and remove
            for(int i = 0; i<gloves.size();i++) {

                if(playerCloseBy(gloves.get(i)) && !gloves.get(i).hasPunched()){
                    gloves.get(i).updateImage(BitmapFactory.decodeResource(getResources(), R.drawable.glove_spring), 50, 123, 1);
                } else if (playerCloseBy(gloves.get(i)) && gloves.get(i).hasPunched()){
                    gloves.get(i).updateImage(BitmapFactory.decodeResource(getResources(), R.drawable.glove_spring), 50, 49, 1);
                }
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

            if (System.nanoTime() - score_timer > 2 * second){
                score += 1;
                score_timer = System.nanoTime();
            }
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
                g.setPunched(!g.hasPunched());
            }
            showScore(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void add_gloves(){
        if(gloves_on){
            if (gloves.size() > 0){
                Glove last_glove = gloves.get(gloves.size() - 1);
                if (WIDTH - last_glove.getXCoordinate() >= 2 * player.getWidth()){
                    setGround();
                    gloves.add(new Glove(BitmapFactory.decodeResource(getResources(), R.drawable.glove_stationary),
                            WIDTH, 50, 49, 5, ground));
                }
            } else{
                gloves.add(new Glove(BitmapFactory.decodeResource(getResources(), R.drawable.glove_stationary),
                        WIDTH, 50, 49, 5, ground));
            }
        } else{
            if (System.nanoTime() - game_start_time > 2 * second){
                gloves_on = true;
            }
        }
    }

    // Sets the level at which the glove will be drawn - up or down.
    private void setGround(){
        if (gloves.size() > 1){
            if (gloves.get(gloves.size()-1).getGround() == gloves.get(gloves.size()-2).getGround()){
                ground = !gloves.get(gloves.size()-1).getGround();
            } else {
                if (rand.nextInt(2) == 0){
                    ground = true;
                }
            }
        }
    }

    // Shows the score on the top right hand corner.
    // The score is based on how long a player lasts.
    private void showScore(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(String.valueOf(score), WIDTH - 50, 40, paint);
    }

    // Checks if the player is close to a glove or not.
    // Returns true if the player is close.
    // Otherwise returns false.
    private boolean playerCloseBy(Glove glove){
        if (player.getXCoordinate() <= glove.getXCoordinate() &&
                player.getXCoordinate() >= (glove.getXCoordinate() - (3 * player.getWidth()))){
            return true;
        } else if (player.getXCoordinate() > glove.getXCoordinate() &&
                glove.getXCoordinate() > (player.getXCoordinate() - (2 * player.getWidth()))){
            return true;
        } else{
            return false;
        }
    }

    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }
}