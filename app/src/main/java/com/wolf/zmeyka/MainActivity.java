package com.wolf.zmeyka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

import com.wolf.zmeyka.ForClasses.Databaseclass;
import com.wolf.zmeyka.ForClasses.SnakeDetails;
import com.wolf.zmeyka.Model.Record;


import org.jetbrains.annotations.NonNls;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


 /*
    Автор Евгений Веденеев

  */

public class MainActivity<userrname> extends AppCompatActivity implements SurfaceHolder.Callback,
        GestureDetector.OnGestureListener {
    Databaseclass databaseclass;
    TextView scoreTV;
    private SurfaceView surfaceView;


    //данные змейки
    private final List<SnakeDetails> snakeDetailsList = new ArrayList<>();

    //виджет для рисования змейки
    private SurfaceHolder surfaceHolder;

    //направление движения движения змейки(right, left, bottom, top)
    private String movingPosition = "right";

    //значения: счёт, базовый размер точки, базоовое количество сегментов змейки
    private int score = 0;
    private static final int pointSize = 28;
    private static final int defaultTalePoints = 3;



    //цвет змейки
    private static final int snakeColor = Color.GREEN;
    //скрость
    private static final int snakeMovingSpeed = 800;
    //старт
    private int positionX = 0, positionY = 0;

    //
    private Timer timer;

    private Canvas canvas = null;

    private Paint pointColor = null;

    //свайпы
    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;
    String userrname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreTV = findViewById(R.id.scoreTV);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        surfaceView = findViewById(R.id.surfaceView);

        surfaceView.getHolder().addCallback(this);
        this.gestureDetector = new GestureDetector(MainActivity.this, this);
        databaseclass = new Databaseclass(MainActivity.this);

        Bundle arguments = getIntent().getExtras();

        if(arguments != null){
            userrname = arguments.getString("name");



        } else {
            userrname = "NULL";
        }

    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        this.surfaceHolder = surfaceHolder;

        init();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return swipeDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case  MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                //горизонтальный свайп
                float valueX = x2 - x1;
                //вертикальный свайп
                float valueY = y2 - y1;

                if (Math.abs(valueX) > MIN_DISTANCE){

                    if (x2 > x1){
                        if (!movingPosition.equals("left"))
                        {
                            movingPosition = "right";
                        }
                        //Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (!movingPosition.equals("right"))
                        {
                            movingPosition = "left";
                        }
                        //Toast.makeText(this, "left", Toast.LENGTH_LONG).show();
                    }
                }
                else if (Math.abs(valueY) > MIN_DISTANCE)
                {
                    if (y2 > y1)
                    {
                        if (!movingPosition.equals("top"))
                        {
                            movingPosition = "bottom";
                        }
                        //Toast.makeText(this, "down", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (!movingPosition.equals("bottom"))
                        {
                            movingPosition = "top";
                        }
                        //Toast.makeText(this, "up", Toast.LENGTH_LONG).show();
                    }
                }

        }

        return super.onTouchEvent(event);

    }

    private void init() {
        snakeDetailsList.clear();

        scoreTV.setText("0");

        score = 0;

        movingPosition = "right";

        int startPositionX = (pointSize) * defaultTalePoints;

        for (int i = 0; i < defaultTalePoints; i++) {

            SnakeDetails snakeDetails = new SnakeDetails(startPositionX, pointSize);
            snakeDetailsList.add(snakeDetails);

            startPositionX = startPositionX - (pointSize * 2);


        }
        addPoint();
        moveSnake();
    }

    private void addPoint() {

        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        if ((randomXPosition % 2) != 0) {
            randomXPosition = randomXPosition + 1;

        }

        if ((randomYPosition % 2) != 0) {
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;

    }

    private void moveSnake() {


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headPositionX = snakeDetailsList.get(0).getPositionX();
                int headPositionY = snakeDetailsList.get(0).getPositionY();

                if (headPositionX == positionX && positionY == headPositionY) {

                    growSnake();
                    addPoint();
                }
                switch (movingPosition) {
                    case "right":
                        snakeDetailsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakeDetailsList.get(0).setPositionY(headPositionY);
                        break;

                    case "left":
                        snakeDetailsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakeDetailsList.get(0).setPositionY(headPositionY);
                        break;

                    case "top":
                        snakeDetailsList.get(0).setPositionX(headPositionX);
                        snakeDetailsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;

                    case "bottom":
                        snakeDetailsList.get(0).setPositionX(headPositionX);
                        snakeDetailsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }


                if (checkGameOver(headPositionX, headPositionY)) {
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Твой счёт = " + score);
                    builder.setMessage("GAME OVER");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ещё раз", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            init();
                        }
                    });
                    builder.setNegativeButton("Cохранить рекорд", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            databaseclass.insertRecord(new Record(1, score, userrname));

                            databasestart();

                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }
                else {

                    canvas = surfaceHolder.lockCanvas();

                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    canvas.drawCircle(snakeDetailsList.get(0).getPositionX(),
                            snakeDetailsList.get(0).getPositionY(), pointSize, createPointColor());

                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    for (int i = 1; i < snakeDetailsList.size(); i++){
                        int getTempPositionX = snakeDetailsList.get(i).getPositionX();
                        int getTempPositionY = snakeDetailsList.get(i).getPositionY();

                        snakeDetailsList.get(i).setPositionX(headPositionX);
                        snakeDetailsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakeDetailsList.get(i).getPositionX(),
                                snakeDetailsList.get(i).getPositionY(),
                                pointSize, createPointColor());

                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }

                    surfaceHolder.unlockCanvasAndPost(canvas);

                }
            }
        }, 1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }

    private void databasestart(){

        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);

    }

    private void growSnake() {
        SnakeDetails snakeDetails = new SnakeDetails(0, 0);
        snakeDetailsList.add(snakeDetails);
        score++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });
    }

    private boolean checkGameOver(int headPositionX, int headPositionY) {
        boolean gameOver = false;

        if (snakeDetailsList.get(0).getPositionX() < 0 ||
            snakeDetailsList.get(0).getPositionY() < 0 ||
                    snakeDetailsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                    snakeDetailsList.get(0).getPositionY() >= surfaceView.getHeight())
        {

            gameOver = true;
        }
        else{
            for (int i = 1; i < snakeDetailsList.size(); i++){
                if (headPositionX == snakeDetailsList.get(i).getPositionX() &&
                    headPositionY == snakeDetailsList.get(i).getPositionY()){
                    gameOver = true;
                    break;
                }
            }
        }

        return gameOver;
    }

    private Paint createPointColor() {


        if (pointColor == null) {
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);
        }

        return pointColor;

    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }
}