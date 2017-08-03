package com.example.swt369.linkup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

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

    private Pair mSelected = null;
    private Paint paintForSelected;

    public GameView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        paintForSelected = new Paint();
        paintForSelected.setStrokeWidth(5);
        paintForSelected.setColor(Color.CYAN);
        paintForSelected.setStyle(Paint.Style.STROKE);
    }

    void initializeSize(){
        mWidth = getWidth();
        mHeight = getHeight();

        map = MapGenerator.getMap();
        calcRowCountAndColumnCount();
        int length_x = (int)(mWidth * (1 - Settings.BRICKS_LEFT_RATIO - Settings.BRICKS_RIGHT_RATIO) / columnCount);
        int length_y = mHeight / rowCount;
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

    LinkedList<Pair> modifyPath(LinkedList<Pair> path){
        LinkedList<Pair> pathInPixel = new LinkedList<>();
        for(Pair pair : path){
            Rect curRect = fields[pair.getRow()][pair.getColumn()];
            pathInPixel.addLast(new Pair(curRect.centerX(),curRect.centerY()));
        }
        return pathInPixel;
    }

    void setSelected(Pair Selected){
        this.mSelected = Selected;
        invalidate(fields[mSelected.getRow()][mSelected.getColumn()]);
    }

    void removeSelected(){
        if(mSelected != null){
            int row = mSelected.getRow();
            int column = mSelected.getColumn();
            mSelected = null;
            invalidate(fields[row][column]);
        }
    }

    void resetMap(){
        this.map = MapGenerator.getMap();
        removeSelected();
        invalidate();
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
        drawSelected(canvas);
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

    private void drawSelected(Canvas canvas){
        if(mSelected != null){
            canvas.drawRect(fields[mSelected.getRow()][mSelected.getColumn()],paintForSelected);
        }
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
