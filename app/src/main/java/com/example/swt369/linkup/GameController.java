package com.example.swt369.linkup;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by swt369 on 2017/8/2.
 */

final class GameController implements View.OnTouchListener {
    private GameView mGameView;
    private int[][] map;
    private int selectedCount = 0;
    private Pair lastSelected = null;
    private int lastType = -1;
    GameController(GameView gameView){
        this.mGameView = gameView;
        map = MapGenerator.getMap();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_DOWN){
            return false;
        }
        Pair pair = mGameView.getBrickByPixelXY((int)event.getX(),(int)event.getY());
        if(pair == null || map[pair.getRow()][pair.getColumn()] == MapGenerator.NOT_EXIST){
            resetSelectStatus();
            return true;
        }
        if(selectedCount == 0){
            lastSelected = pair;
            lastType = map[pair.getRow()][pair.getColumn()];
            selectedCount = 1;
        }else if(selectedCount == 1){
            if(!pair.equals(lastSelected) && map[pair.getRow()][pair.getColumn()] == lastType){

            }else {
                resetSelectStatus();
            }
        }
        return true;
    }

    private void resetSelectStatus(){
        selectedCount = 0;
        lastSelected = null;
    }
}
