package com.example.swt369.linkup;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by swt369 on 2017/8/2.
 */
final class MapGenerator {
    static final int NOT_EXIST = -1;
    private static final int LEAST_FOR_RANDOM = 11;
    static final int TYPE_COUNT = 10;
    private static int[][] map;
    private MapGenerator() {

    }

    static int[][] getMap(){
        return map;
    }

    //根据传入行列数生成一个Map，注意（1）最外围一圈是空白的（2）传入行列数的乘积必须是偶数
    static void generateMap(int rowCount, int columnCount) {
        int total = (rowCount - 2) * (columnCount - 2);
        int groupCount = total / 2;
        int eachTypeLeast = groupCount / TYPE_COUNT;
        int[] remain = new int[TYPE_COUNT];
        boolean flag = false;
        Queue<Pair> queue = null;
        Arrays.fill(remain, eachTypeLeast);
        for (int i = 0; i < groupCount - TYPE_COUNT * eachTypeLeast; i++) {
            Random r = new Random();
            int type = r.nextInt(TYPE_COUNT);
            remain[type]++;
        }
        MapGenerator.map = new int[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Arrays.fill(map[i], NOT_EXIST);
        }
        for (int i = 0; i < TYPE_COUNT; i++) {
            while (remain[i] > 0) {
                remain[i]--;
                if (!flag) {
                    generateOneGroup(map, rowCount, columnCount, i);
                    total -= 2;
                    if (total < LEAST_FOR_RANDOM) {
                        flag = true;
                        queue = generateQueueForRemains(map, rowCount, columnCount);
                    }
                } else {
                    generateOneGroup(map, i, queue);
                    total -= 2;
                }
            }
        }
    }

    private static void generateOneGroup(int[][] map, int rowCount, int columnCount, int type) {
        Random r = new Random();
        int row = r.nextInt(rowCount - 2) + 1;
        int column = r.nextInt(columnCount - 2) + 1;
        while (map[row][column] != NOT_EXIST) {
            row = r.nextInt(rowCount - 2) + 1;
            column = r.nextInt(columnCount - 2) + 1;
        }
        map[row][column] = type;
        row = r.nextInt(rowCount - 2) + 1;
        column = r.nextInt(columnCount - 2) + 1;
        while (map[row][column] != NOT_EXIST) {
            row = r.nextInt(rowCount - 2) + 1;
            column = r.nextInt(columnCount - 2) + 1;
        }
        map[row][column] = type;
    }

    private static void generateOneGroup(int[][] map, int type, Queue<Pair> queue) {
        Pair pair = queue.poll();
        map[pair.getRow()][pair.getColumn()] = type;
        pair = queue.poll();
        map[pair.getRow()][pair.getColumn()] = type;
    }

    private static Queue<Pair> generateQueueForRemains(int[][] map, int rowCount, int columnCount) {
        Queue<Pair> queue = new LinkedList<>();
        for (int i = 1; i < rowCount - 1; i++)
            for (int j = 1; j < columnCount - 1; j++) {
                if (map[i][j] == NOT_EXIST) {
                    queue.add(new Pair(i, j));
                }
            }
        return queue;
    }

}
