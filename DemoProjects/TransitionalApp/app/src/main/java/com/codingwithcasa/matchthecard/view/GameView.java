package com.codingwithcasa.matchthecard.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.codingwithcasa.matchthecard.R;
import com.codingwithcasa.matchthecard.data.GameModel;

public class GameView extends View {

    private Paint paintOpen;
    private Paint paintBg;
    private Paint paintLine;
    private Bitmap catOne;
    private Bitmap catTwo;
    private Bitmap catThree;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.WHITE);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.GRAY);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(6);


        paintOpen = new Paint();
        paintOpen.setARGB(0, 0, 0, 0);


        catOne = BitmapFactory.decodeResource(getResources(), R.drawable.catone);
        catTwo = BitmapFactory.decodeResource(getResources(), R.drawable.cattwo);
        catThree = BitmapFactory.decodeResource(getResources(), R.drawable.catthree);

        GameModel.getInstance().setCards();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGameArea(canvas);
        drawModel(canvas);
        drawCover(canvas);

    }

    //draw white covering over game model. updates when a space is clicked.
    private void drawCover(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                float centerX = j * getWidth() / 3;
                float centerY = i * getHeight() / 3;

                if (GameModel.getInstance().getCoverContent(i, j) ==
                        GameModel.UNTOUCHED) {
                    canvas.drawRect(centerX, centerY, centerX + (getWidth() / 3), centerY + (getHeight() / 3), paintBg);

                } else if (GameModel.getInstance().getCoverContent(i, j) ==
                        GameModel.TOUCHED) {
                    canvas.drawRect(centerX, centerY, centerX + (getWidth() / 3), centerY + (getHeight() / 3), paintOpen);
                }
            }
        }
        drawGameArea(canvas);
    }

    //place cards in the appropriate location
    private void drawModel(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                float centerX = (i * getWidth() / 3) + getWidth() / 12;
                float centerY = (j * getHeight() / 3) + getHeight() / 12;


                if (GameModel.getInstance().getFieldContent(i, j) ==
                        GameModel.MATCH1) {

                    //draw catone
                    canvas.drawBitmap(catOne, centerX, centerY, null);

                } else if (GameModel.getInstance().getFieldContent(i, j) ==
                        GameModel.MATCH2) {

                    //draw cattwo
                    canvas.drawBitmap(catTwo, centerX, centerY, null);

                } else if (GameModel.getInstance().getFieldContent(i, j) ==
                        GameModel.MATCH3) {

                    //draw catthree
                    canvas.drawBitmap(catThree, centerX, centerY, null);
                }
            }

        }
    }


    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // vertical lines
        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(), paintLine);
        canvas.drawLine(2 * (getWidth() / 3), 0, 2 * (getWidth() / 3), getHeight(), paintLine);
        // horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3, paintLine);
        canvas.drawLine(0, 2 * (getHeight() / 3), getWidth(), 2 * (getHeight() / 3), paintLine);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX() / (getWidth() / 3));
            int tY = ((int) event.getY() / (getHeight() / 3));

            if (GameModel.getInstance().getCoverContent(tY, tX) == GameModel.UNTOUCHED) {
                GameModel.getInstance().setCoverContent(tY, tX, GameModel.TOUCHED);

            }
            invalidate();
        }
        return super.onTouchEvent(event);

    }

    public void reset() {
        GameModel.getInstance().resetGame();
        invalidate();
        GameModel.getInstance().setCards();
    }

}


