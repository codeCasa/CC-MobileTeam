package com.yorel.watchmego;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * Created by yorel on 11/9/2016.
 */

public class PudgeyWalk extends SurfaceView implements SurfaceHolder.Callback {
    GameThread gameThread;
    int screenW; //Device's screen width.
    int screenH; //Devices's screen height.
    int pudgeyX;
    int pudgeyY;
    int initialY ;
    int pudgeyW;
    int pudgeyH;
    //background
    int bgrW;
    int bgrH;
    int bgrScroll;
    int dBgrY; //Background scroll speed.
    //tresurw
    int treasureY, treasureX, treasureH, treasureW;
    float deltaY, deltaX;
    Bitmap pudgey, bgr, bgrReverse,treasure;
    boolean reverseBackroundFirst;

    long score = 0;
    Paint scorePaint =new Paint();

    //Frame speed
    long timeNow;
    long timePrevFrame = 0;
    long timeDelta;

    boolean forward = true, up = true;
    Rect fromRect1 = new Rect(), toRect1 = new Rect() , fromRect2 = new Rect(), toRect2 = new Rect(), treasureRct = new Rect(), pudgeRect = new Rect();
    public PudgeyWalk(Context context){
        super(context);
        pudgey = BitmapFactory.decodeResource(getResources(),R.drawable.pudge); //Load a pudge image.
        bgr = BitmapFactory.decodeResource(getResources(),R.drawable.sky_bgr); //Load a background.
        treasure = BitmapFactory.decodeResource(getResources(),R.drawable.tresure);
        pudgeyW = pudgey.getWidth();
        pudgeyH = pudgey.getHeight();

        //Create a flag for the onDraw method to alternate background with its mirror image.
        reverseBackroundFirst = false;

        initialY = 100; //Initial vertical position
        bgrScroll = 0;  //Background scroll position
        dBgrY = 1; //Scrolling background speed

        scorePaint.setTextSize(30);

        //Set thread
        getHolder().addCallback(this);

        setFocusable(true);
    }
    public void setRun(boolean run){
        if(gameThread!=null)
        gameThread.setRunning(run);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new GameThread(getHolder(), this);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        //This event-method provides the real dimensions of this custom view.
        screenW = w;
        screenH = h;

        bgr = Bitmap.createScaledBitmap(bgr, w, h, true); //Scale background to fit the screen.
        bgrW = bgr.getWidth();
        bgrH = bgr.getHeight();

        //Create a mirror image of the background (horizontal flip) - for a more circular background.
        Matrix matrix = new Matrix();  //Like a frame or mould for an image.
        matrix.setScale(-1, 1); //Horizontal mirror effect.
        bgrReverse = Bitmap.createBitmap(bgr, 0, 0, bgrW, bgrH, matrix, true); //Create a new mirrored bitmap by applying the matrix.
        pudgey = Bitmap.createScaledBitmap(pudgey,w/4,h/4,true);
        pudgeyW = pudgey.getWidth();
        pudgeyH = pudgey.getHeight();
        pudgeyX = (pudgeyW / 2) ;
        pudgeyY = screenH - pudgeyH-20;
        treasure = Bitmap.createScaledBitmap(treasure,w/15,w/15,true);
        treasureH = treasure.getHeight();
        treasureW = treasure.getWidth();
        treasureX = screenW /2;
        treasureY = (screenH /2) - treasureH;
        treasureRct.set(treasureX,treasureY-treasureH,treasureW+treasureX,treasureY);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        treasureRct.set(treasureX,treasureY-treasureH,treasureW+treasureX,treasureY);
        //Draw scrolling background.
        fromRect1.set(0, 0, bgrW - bgrScroll, bgrH);
        toRect1.set(bgrScroll, 0, bgrW, bgrH);

        fromRect2.set(bgrW - bgrScroll, 0, bgrW, bgrH);
        toRect2.set(0, 0, bgrScroll, bgrH);
        if (!reverseBackroundFirst) {
            canvas.drawBitmap(bgr, fromRect1, toRect1, null);
            canvas.drawBitmap(bgrReverse, fromRect2, toRect2, null);
        }
        else{
            canvas.drawBitmap(bgr, fromRect2, toRect2, null);
            canvas.drawBitmap(bgrReverse, fromRect1, toRect1, null);
        }

        //Next value for the background's position.
        if ( (bgrScroll += dBgrY) >= bgrW) {
            bgrScroll = 0;
            reverseBackroundFirst = !reverseBackroundFirst;
        }
        canvas.drawBitmap(treasure, treasureX, treasureY,null);
        //move the pudge in the x and y direction
        if (forward) {
            pudgeyX += (int) deltaX;
            if (pudgeyX > (screenW - pudgeyW))
                forward = false;
        }else {
            pudgeyX -= (int) deltaX;
            if(pudgeyX < pudgeyW/4 )
                forward = true;
        }
        if(up) {
            pudgeyY -= (int) deltaY;
            if (pudgeyY < pudgeyH/4)
                up = false;
        }else {
            pudgeyY += (int) deltaY;
            if(pudgeyY>(screenH-pudgeyH-20))
                up = true;
        }
        pudgeRect.set(pudgeyX,pudgeyY,pudgeyX+pudgeyW,pudgeyY+pudgeyH);
        if(Rect.intersects(pudgeRect,treasureRct)) {
            score += (int) (Math.random() + 5) * 10;
            treasureX = (int)(Math.random() * screenW)-treasureW;
            treasureY = (int)(Math.random() * screenH)-treasureH;
        }
        //treasure.
        canvas.save(); //Save the position of the canvas matrix.
        canvas.drawBitmap(pudgey, pudgeyX, pudgeyY, null); //Draw the pudge
        canvas.restore(); //Rotate the canvas matrix back to its saved position

        canvas.drawText("Score: "+score, 40, 70, scorePaint);
    }

    private class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private PudgeyWalk gameView;
        private boolean run = false;

        public GameThread(SurfaceHolder surfaceHolder, PudgeyWalk gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setRunning(boolean run) {
            this.run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return surfaceHolder;
        }

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            Canvas c;
            while (run) {
                c = null;

                //limit frame rate to max 60fps
                timeNow = System.currentTimeMillis();
                timeDelta = timeNow - timePrevFrame;
                if ( timeDelta < 16) {
                    try {
                        Thread.sleep(16 - timeDelta);
                    }
                    catch(InterruptedException e) {

                    }
                }
                timePrevFrame = System.currentTimeMillis();

                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        if(display.getRotation() == Surface.ROTATION_90 && !WatchMe.isFlat){
                            gameView.deltaX = WatchMe.deltaZ;
                            gameView.deltaY = WatchMe.deltaX;

                        }
                        else if(display.getRotation() == Surface.ROTATION_90 && WatchMe.isFlat){
                            gameView.deltaX = WatchMe.deltaX;
                            gameView.deltaY = WatchMe.deltaZ;
                        }
                        //call methods to draw and process next fame
                        gameView.onDraw(c);
                        //gameView.draw(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
