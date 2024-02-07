package com.task10.tables.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Tables")//cmtr-6e999703-Tables-test
public class Table {

    private Integer id;
    private Integer number;
    private Integer places;
    private Boolean isVip;
    private Integer minOrder;

    @DynamoDBHashKey(attributeName="id")
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName="number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    @DynamoDBAttribute(attributeName="places")
    public Integer getPlaces() {
        return places;
    }

    public void setPlaces(final Integer places) {
        this.places = places;
    }

    @DynamoDBAttribute(attributeName="isVip")
    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(final Boolean vip) {
        isVip = vip;
    }

    @DynamoDBAttribute(attributeName="minOrder")
    public Integer getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(final Integer minOrder) {
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
