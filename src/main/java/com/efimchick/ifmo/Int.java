package com.efimchick.ifmo;

import java.util.Collection;

public class Int {

    private int total;
    private int count;
    private boolean isHistorical;

    public Int() {
        total = 1;
    }

    public Int(int total) {
        this.total = total;
        this.isHistorical = false;
        this.count = 0;
    }

    public void multiply(int k){
        total = total*k;
    }

    public void add(int k){
        total = total+k;
        count++;
    }

    public int getTotal() {
        return total;
    }

    public boolean isHistorical() {
        return isHistorical;
    }

    public void setHistorical(boolean historical) {
        isHistorical = historical;
    }

    public int getCount() {
        return count;
    }
}
