package com.example.swt369.linkup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by swt369 on 2017/8/2.
 */

final class GameController implements View.OnTouchListener {
    private GameView mGameView;
    private Handler mHandler;
    private int[][] map;
    private int selectedCount = 0;
    private Pair lastSelected = null;
    private int lastType = -1;
    GameController(GameView gameView, Handler handler){
        this.mGameView = gameView;
        this.mHandler = handler;
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
            mGameView.setSelected(pair);
            lastType = map[pair.getRow()][pair.getColumn()];
            selectedCount = 1;
        }else if(selectedCount == 1){
            if(!pair.equals(lastSelected) && map[pair.getRow()][pair.getColumn()] == lastType){
                LinkPath linkPath = new LinkPath(map,lastSelected,pair);
                LinkedList<Pair> path = linkPath.findPath();
                if(path != null){
                    map[lastSelected.getRow()][lastSelected.getColumn()] = MapGenerator.NOT_EXIST;
                    map[pair.getRow()][pair.getColumn()] = MapGenerator.NOT_EXIST;
                    path = mGameView.modifyPath(path);
                    sendMessageForDrawPath(path);
                    mGameView.invalidate();
                }
            }
            resetSelectStatus();
        }
        return true;
    }

    void resetMap(){
        map = MapGenerator.getMap();
        selectedCount = 0;
        lastSelected = null;
        mGameView.resetMap();
    }


    private void resetSelectStatus(){
        selectedCount = 0;
        lastSelected = null;
        mGameView.removeSelected();
    }

    private void sendMessageForDrawPath(LinkedList<Pair> path){
        Message message = mHandler.obtainMessage();
        message.what = Code.CODE_DRAW_PATH;
        Bundle data = new Bundle();
        data.putSerializable("path",path);
        message.setData(data);
        mHandler.sendMessage(message);
    }
}
