package com.task10.tables.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Table")//cmtr-6e999703-Tables-test
public class Table {

    private int id;
    private int number;
    private int places;
    private boolean isVip;
    private int minOrder;

    @DynamoDBHashKey(attributeName="id")
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName="number")
    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    @DynamoDBAttribute(attributeName="places")
    public int getPlaces() {
        return places;
    }

    public void setPlaces(final int places) {
        this.places = places;
    }

    @DynamoDBAttribute(attributeName="isVip")
    public boolean isVip() {
        return isVip;
    }

    public void setVip(final boolean vip) {
        isVip = vip;
    }

    @DynamoDBAttribute(attributeName="minOrder")
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
