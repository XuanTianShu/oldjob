package com.yuepei.system.domain;

public class Item {
    private int index;
    private int status;

    public Item() {}

    public Item(int index, int status) {
        this.index = index;
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Item{" +
                "index=" + index +
                ", status=" + status +
                '}';
    }
}
