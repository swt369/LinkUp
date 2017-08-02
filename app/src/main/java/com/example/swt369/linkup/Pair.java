package com.example.swt369.linkup;

/**
 * Created by swt369 on 2017/8/2.
 */
class Pair {
    private int row;
    private int column;

    Pair(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Pair && this.row == ((Pair) obj).getRow() && this.column == ((Pair) obj).getColumn();
    }
}
