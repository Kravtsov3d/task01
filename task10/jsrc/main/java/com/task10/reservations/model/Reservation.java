package com.task10.reservations.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Reservations")//cmtr-6e999703-Reservations-test
public class Reservation {

    private Integer tableNumber;
    private String clientName;
    private String phoneNumber;
    private String date;
    private String slotTimeStart;
    private String slotTimeEnd;

    @DynamoDBHashKey(attributeName="tableNumber")
    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(final Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    @DynamoDBRangeKey(attributeName="clientName")
    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    @DynamoDBAttribute(attributeName="phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DynamoDBAttribute(attributeName="date")
    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    @DynamoDBAttribute(attributeName="slotTimeStart")
    public String getSlotTimeStart() {
        return slotTimeStart;
    }

    public void setSlotTimeStart(final String slotTimeStart) {
        this.slotTimeStart = slotTimeStart;
    }

    @DynamoDBAttribute(attributeName="slotTimeEnd")
    public String getSlotTimeEnd() {
        return slotTimeEnd;
    }

    public void setSlotTimeEnd(final String slotTimeEnd) {
        this.slotTimeEnd = slotTimeEnd;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "tableNumber=" + tableNumber +
            ", clientName='" + clientName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", date='" + date + '\'' +
            ", slotTimeStart='" + slotTimeStart + '\'' +
            ", slotTimeEnd='" + slotTimeEnd + '\'' +
            '}';
    }
}
