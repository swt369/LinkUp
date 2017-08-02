package com.example.swt369.linkup;

import android.support.annotation.Nullable;

import java.util.LinkedList;

/**
 * Created by swt369 on 2017/8/2.
 */

final class LinkPath {
    private static final int MAX_DIVERT_COUNT = 2;
    private static final int[] OFFSET_X = {1,0,-1,0};
    private static final int[] OFFSET_Y = {0,1,0,-1};

    private int[][] map;
    private int rowCount;
    private int columnCount;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    LinkedList<Pair> curPath;
    LinkedList<Pair> resPath;
    int[][] visited;

    int curDirection;
    int curDivertCount;

    boolean finded = false;
    LinkPath(int[][] map,Pair start,Pair end){
        this.map = map;
        startX = start.getRow();
        startY = start.getColumn();
        endX = end.getRow();
        endY = end.getColumn();
        rowCount = map.length;
        columnCount = map[0].length;
    }

    @Nullable
    LinkedList<Pair> findPath(){
        curPath = new LinkedList<>();
        resPath = null;
        visited = new int[rowCount][columnCount];
        finded = false;

        curDivertCount = 0;
        curPath.addLast(new Pair(startX,startY));
        visited[startX][startY] = 1;

        dfsStart(startX,startY);
        return resPath;
    }

    private void dfsStart(int x,int y){
        for(int i = 0 ; i < OFFSET_X.length ; i++){
            int curX = x + OFFSET_X[i];
            int curY = y + OFFSET_Y[i];
            if(curX < 0 || curX >= rowCount || curY < 0 || curY >= columnCount){
                continue;
            }
            if(curX == endX && curY == endY){
                curPath.addLast(new Pair(endX,endY));
                resPath = new LinkedList<>(curPath);
                finded = true;
                return;
            }
            if(map[curX][curY] == MapGenerator.NOT_EXIST){
                curDirection = i;
                dfs(x,y);
            }
        }
    }

    private void dfs(int x,int y){
        if(finded){
            return;
        }
        for(int i = 0 ; i < OFFSET_X.length; i++){
            int curX = x + OFFSET_X[i];
            int curY = y + OFFSET_Y[i];
            if(curX < 0 || curX >= rowCount || curY < 0 || curY >= columnCount || visited[curX][curY] == 1){
                continue;
            }
            int lastDirection = curDirection;
            if(curDirection != i){
                if(curDivertCount < MAX_DIVERT_COUNT){
                    curDirection = i;
                    curDivertCount++;
                }else {
                    continue;
                }
            }
            if(curX == endX && curY == endY){
                curPath.addLast(new Pair(endX,endY));
                resPath = new LinkedList<>(curPath);
                finded = true;
                return;
            }
            if(map[curX][curY] == MapGenerator.NOT_EXIST){
                visited[curX][curY] = 1;
                curPath.addLast(new Pair(curX,curY));
                dfs(curX,curY);
                curPath.removeLast();
                visited[curX][curY] = 0;
            }
            if(curDirection != lastDirection){
                curDivertCount--;
                curDirection = lastDirection;
            }
        }
    }
}
