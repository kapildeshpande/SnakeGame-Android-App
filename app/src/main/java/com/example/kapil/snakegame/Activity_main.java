package com.example.kapil.snakegame;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.graphics.Color.rgb;

/**
 * Created by kapil on 01-02-2018.
 */

public class Activity_main extends View {

    private Paint paintBlackStroke;
    private Paint paintBlackFill;
    private boolean gameOver,flag = false;
    private int score,radius,speed = 50;
    private int width,height;
    private ArrayList <Coordinate> snake;
    private Coordinate head;
    private Fruit fruit;
    private TextView textView;
    private boolean firstPlay = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (firstPlay) {
            firstPlay = false;
            reset();
        }

        return super.onTouchEvent(event);
    }

    enum Diretion {
        LEFT,RIGHT,UP,DOWN
    }

    public void setTextView (TextView textView) {this.textView = textView;}

    private void setText (String text) {
        textView.setText(text);
    }

    public void reset () {
        score = 0;
        gameOver = false;
        snake.clear();
        snake.add(new Coordinate(radius,100));
        snake.add(new Coordinate(3*radius,100));
        snake.add(new Coordinate(5*radius,100));
        head = snake.get(snake.size()-1);
        setText("Score : 0");
    }

    private Diretion getDirection () {

        Coordinate prev = snake.get(snake.size()-2);
        if (prev.x - head.x == 2*radius) {//left
            return Diretion.LEFT;
        } else if (head.x - prev.x == 2*radius) {//right
            return Diretion.RIGHT;
        } else if (prev.y - head.y == 2*radius) {//up
            return Diretion.UP;
        } else if (head.y - prev.y == 2*radius) {//down
            return Diretion.DOWN;
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();

        paintBlackStroke.setColor(rgb(169, 5, 0));
        paintBlackStroke.setStyle(Paint.Style.STROKE);
        paintBlackStroke.setStrokeWidth(10);

        /*
            Borders

            for (int i=0;i<=width;i+=2*radius) {
                canvas.drawCircle(i,10, radius, paintBlackStroke);
                canvas.drawCircle(i,height-10, radius, paintBlackStroke);
            }

            for (int i=0;i<=height;i+= 2*radius) {
                canvas.drawCircle(10,i, radius, paintBlackStroke);
                canvas.drawCircle(width-10,i, radius, paintBlackStroke);
            }
        */

        paintBlackFill.setColor(Color.BLUE);
        paintBlackFill.setStyle(Paint.Style.FILL);
        canvas.drawCircle(fruit.f_x,fruit.f_y, fruit.radius, paintBlackFill);

        for (int i=0;i<snake.size();i++) {
            if (i == snake.size()-1) {
                paintBlackFill.setColor(rgb(123, 201, 0));
                paintBlackFill.setStyle(Paint.Style.FILL);
                canvas.drawCircle(snake.get(i).x, snake.get(i).y, radius, paintBlackFill);
            } else {
                paintBlackFill.setColor(rgb(247, 248, 0));
                paintBlackFill.setStyle(Paint.Style.FILL);
                canvas.drawCircle(snake.get(i).x, snake.get(i).y, radius, paintBlackFill);
            }
        }

        invalidate();
        if (!gameOver) {
            flag = true;
            move();
            logic();
            try {
                Thread.sleep(speed);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Activity_main (Context context, AttributeSet attributeSet) {

        super(context,attributeSet);
        setBackgroundColor(Color.BLACK);
        height = 400;
        width = 500;
        score = 0;
        radius = 10;
        gameOver = true;
        snake = new ArrayList <> ();
        snake.add(new Coordinate(radius,100));
        snake.add(new Coordinate(3*radius,100));
        snake.add(new Coordinate(5*radius,100));
        head = snake.get(snake.size()-1);
        fruit = new Fruit();
        fruit.getRandomPos();

        paintBlackStroke = new Paint();
        paintBlackFill = new Paint();
    }

    public boolean isIntersect (int x1, int y1, int x2, int y2)  {
        int distSq = (x1 - x2) * (x1 - x2) +
                (y1 - y2) * (y1 - y2);
        int radSumSq = 4 * (fruit.radius) * (fruit.radius);
        if (distSq <= radSumSq)
            return true;
        return false;
    }

    private void logic () {

        //Check for gameOver condition
        for (int i=0;i<snake.size()-1;i++) {
            Coordinate curr = snake.get(i);
            if (curr.x == head.x && curr.y == head.y) {
                gameOver = true;
                dialogBox();
                setText("GameOver Score : " + Integer.toString(score));
                return;
            }
        }

        //If snake head hits fruit
        if (isIntersect(head.x,head.y,fruit.f_x,fruit.f_y)){
            int moveX = head.x,moveY = head.y;

            switch (getDirection()) {
                case LEFT:
                    moveX -= 2*radius;
                    break;
                case RIGHT:
                    moveX += 2*radius;
                    break;
                case UP:
                    moveY -= 2*radius;
                    break;
                case DOWN:
                    moveY += 2*radius;
                    break;
            }

            snake.add(new Coordinate(moveX,moveY));
            head = snake.get(snake.size() - 1);
            fruit.getRandomPos();
            score++;
            setText("Score : " + Integer.toString(score));
        }

        //If snake head cross boundary
        if (!((head.x >= 0 && head.x <= width) && (head.y >= 0 && head.y <= height))) {
            Coordinate next = new Coordinate(head.x,head.y);
            switch (getDirection()) {
                case LEFT:
                    head.x  = width;
                    next.x = width - 2*radius;
                    break;
                case RIGHT:
                    head.x  = 2*radius;
                    next.x = 4*radius;
                    break;
                case UP:
                    head.y = height;
                    next.y = height - 2*radius;
                    break;
                case DOWN:
                    head.y = 2*radius;
                    next.y = 4*radius;
                    break;
            }
            snake.set(snake.size()-2,head);
            snake.set(snake.size()-1,next);
            head = snake.get(snake.size()-1);
        }

    }

    private void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Game Over Score " +  Integer.toString(score));
        alertDialogBuilder.setPositiveButton("Play Again",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset();
                    }
                });

        alertDialogBuilder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void move () {

        int moveX = 0,moveY = 0;

        switch (getDirection()) {
            case LEFT:
                moveX -= 2*radius;
                break;
            case RIGHT:
                moveX += 2*radius;
                break;
            case UP:
                moveY -= 2*radius;
                break;
            case DOWN:
                moveY += 2*radius;
                break;
        }

        snake.add(new Coordinate(head.x+moveX,head.y+moveY));
        snake.remove(0);
        head = snake.get(snake.size()-1);
    }

    public void moveUp () {
        if (!gameOver) {
            Coordinate prev = snake.get(snake.size() - 2);
            if (Math.abs(prev.x - head.x) == 2 * radius) {
                snake.add(new Coordinate(head.x, head.y - 2 * radius));
                snake.remove(0);
                head = snake.get(snake.size() - 1);
            }
        }
    }

    public void moveDown () {
        if (!gameOver) {
            Coordinate prev = snake.get(snake.size() - 2);
            if (Math.abs(prev.x - head.x) == 2 * radius) {
                snake.add(new Coordinate(head.x, head.y + 2 * radius));
                snake.remove(0);
                head = snake.get(snake.size() - 1);
            }
        }
    }

    public void moveLeft () {
        if (!gameOver) {
            Coordinate prev = snake.get(snake.size() - 2);
            if (Math.abs(prev.y - head.y) == 2 * radius) {
                snake.add(new Coordinate(head.x - 2 * radius, head.y));
                snake.remove(0);
                head = snake.get(snake.size() - 1);
            }
        }
    }

    public void moveRight () {

        if (!gameOver) {
            Coordinate prev = snake.get(snake.size() - 2);
            if (Math.abs(prev.y - head.y) == 2 * radius) {
                snake.add(new Coordinate(head.x + 2 * radius, head.y));
                snake.remove(0);
                head = snake.get(snake.size() - 1);
            }
        }
    }

    private class Coordinate {
        private int x,y;

        private Coordinate (int x,int y) {
            this.x = x;
            this.y = y;
        }


    }

    private class Fruit {

        private int f_x,f_y,radius=20;

        private void getRandomPos () {
            Random random = new Random();
            f_x = random.nextInt(width-2*radius);
            f_y = random.nextInt(height-2*radius);
            if (f_x < 2*radius) f_x = 2*radius;
            if (f_y < 2*radius) f_y = 2*radius;
        }
    }

}
