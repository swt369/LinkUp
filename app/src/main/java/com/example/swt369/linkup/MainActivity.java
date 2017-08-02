package com.example.swt369.linkup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private Button button;
    private GameView gameView;
    private GameController gameController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        button = (Button)findViewById(R.id.button);

        MapGenerator.generateMap(18,12);

        gameView = new GameView(this);
        frameLayout.addView(gameView);

        LoadBitmapTask task = new LoadBitmapTask();
        task.execute();

        gameController = new GameController(gameView);
        gameView.setOnTouchListener(gameController);
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
