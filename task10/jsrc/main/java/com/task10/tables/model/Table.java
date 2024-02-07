package com.task10.tables.model;

public class Table {

    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private int minOrder;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(final int places) {
        this.places = places;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(final boolean vip) {
        isVip = vip;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(final int minOrder) {
        this.minOrder = minOrder;
    }

    @Override
    public String toString() {
        return "Table{" +
            "id=" + id +
            ", number=" + number +
            ", places=" + places +
            ", isVip=" + isVip +
            ", minOrder=" + minOrder +
            '}';
    }
}
