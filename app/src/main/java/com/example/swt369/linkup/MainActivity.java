package com.example.swt369.linkup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    private static final int ROW_COUNT = 18;
    private static final int COLUMN_COUNT = 12;
    private FrameLayout frameLayout;
    private Button button;
    private GameView gameView;
    private GameController gameController;
    private Handler handler;
    private LinkedList<PathView> queueForPathView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        button = (Button)findViewById(R.id.button);

        MapGenerator.generateMap(ROW_COUNT,COLUMN_COUNT);

        gameView = new GameView(this);
        frameLayout.addView(gameView);

        LoadBitmapTask task = new LoadBitmapTask();
        task.execute();

        queueForPathView = new LinkedList<>();

        handler = new Handler(new Handler.Callback(){

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case Code.CODE_DRAW_PATH:
                        LinkedList path = (LinkedList<Pair>) msg.getData().getSerializable("path");
                        PathView pathView = new PathView(MainActivity.this,path,handler);
                        queueForPathView.addLast(pathView);
                        frameLayout.addView(pathView);
                        return true;
                    case Code.CODE_REMOVE_PATH:
                        PathView pathViewForRemove = queueForPathView.removeFirst();
                        frameLayout.removeView(pathViewForRemove);
                        return true;
                }
                return false;
            }
        });

        gameController = new GameController(gameView,handler);
        gameView.setOnTouchListener(gameController);

        button.setBackgroundColor(0x7f040000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapGenerator.generateMap(ROW_COUNT,COLUMN_COUNT);
                gameController.resetMap();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        gameView.initializeSize();
    }

    private class LoadBitmapTask extends AsyncTask<Void,Void,Bitmap[]>{
        @Override
        protected Bitmap[] doInBackground(Void... params) {
            int[] imgs = new int[]{
                    R.drawable.img0,
                    R.drawable.img1,
                    R.drawable.img2,
                    R.drawable.img3,
                    R.drawable.img4,
                    R.drawable.img5,
                    R.drawable.img6,
                    R.drawable.img7,
                    R.drawable.img8,
                    R.drawable.img9,
            };
            Bitmap[] bitmaps = new Bitmap[MapGenerator.TYPE_COUNT];
            for(int i = 0 ; i < bitmaps.length ; i++){
                bitmaps[i] = BitmapFactory.decodeResource(getResources(),imgs[i]);
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            gameView.setBitmaps(bitmaps);
        }
    }
}
