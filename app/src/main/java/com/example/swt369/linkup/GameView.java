package com.example.swt369.linkup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by swt369 on 2017/8/2.
 */

final class GameView extends View {
    int mWidth = 0;
    int mHeight = 0;

    private int[][] map;
    private int rowCount;
    private int columnCount;

    private int brickLength;
    private int brickLeft;
    private int brickTop;

    private Bitmap[] bitmaps = null;
    private Rect[][] fields = null;

    public GameView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    void initializeSize(){
        mWidth = getWidth();
        mHeight = getHeight();

        map = MapGenerator.getMap();
        calcRowCountAndColumnCount();
        int length_x = (int)(mWidth * (1 - Settings.BRICKS_LEFT_RATIO - Settings.BRICKS_RIGHT_RATIO) / columnCount);
        int length_y = (int)(mHeight / rowCount);
        brickLength = length_x < length_y ? length_x : length_y;
        brickLeft = (mWidth - brickLength * columnCount) / 2;
        brickTop = 0;

        fields = new Rect[rowCount][columnCount];
        for(int i = 0 ; i < rowCount ; i++)
            for(int j = 0 ; j < columnCount ; j++){
                fields[i][j] = new Rect(
                        brickLeft + brickLength * j,
                        brickTop + brickLength * i,
                        brickLeft + brickLength * (j + 1),
                        brickTop + brickLength * (i + 1));
            }

        invalidate();
    }

    void setBitmaps(Bitmap[] bitmaps){
        this.bitmaps = bitmaps;
    }

    @Nullable
    Pair getBrickByPixelXY(int x, int y){
        int column = (x - brickLeft) / brickLength;
        int row = (y - brickTop) / brickLength;
        if(row >= 0 && row < rowCount && column >= 0 && column < columnCount){
            return new Pair(row,column);
        }else {
            return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mWidth == 0 || mHeight == 0 || bitmaps == null || fields == null){
            return;
        }
        drawBricks(canvas);
    }

    private void drawBricks(Canvas canvas){
        for(int i = 0 ; i < rowCount ; i++)
            for(int j = 0 ; j < columnCount ; j++){
                drawSingleBrick(canvas,i,j);
            }
    }

    private void drawSingleBrick(Canvas canvas,int row,int column){
        int type = map[row][column];
        if(type == -1){
            return;
        }
        Bitmap bitmap = bitmaps[type];
        canvas.drawBitmap(
                bitmap,
                new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                fields[row][column],
                null);
    }

    private void calcRowCountAndColumnCount(){
        rowCount = map.length;
        columnCount = map[0].length;
    }

    private static final class Settings{
        private static final float BRICKS_LEFT_RATIO = 0;
        private static final float BRICKS_RIGHT_RATIO = 0;
    }
}
