package com.task10.tables.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Table")//cmtr-6e999703-Tables-test
public class Table {

    @DynamoDBHashKey(attributeName="id")
    private int id;
    @DynamoDBAttribute(attributeName="number")
    private int number;
    @DynamoDBAttribute(attributeName="places")
    private int places;
    @DynamoDBAttribute(attributeName="isVip")
    private boolean isVip;
    @DynamoDBAttribute(attributeName="minOrder")
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
