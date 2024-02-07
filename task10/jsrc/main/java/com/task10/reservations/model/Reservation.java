package com.task10.reservations.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Reservation")//cmtr-6e999703-Reservations-test
public class Reservation {

    @DynamoDBHashKey(attributeName="tableNumber")
    private int tableNumber;
    @DynamoDBAttribute(attributeName="clientName")
    private String clientName;
    @DynamoDBAttribute(attributeName="phoneNumber")
    private String phoneNumber;
    @DynamoDBAttribute(attributeName="date")
    private String date;
    @DynamoDBAttribute(attributeName="slotTimeStart")
    private String slotTimeStart;
    @DynamoDBAttribute(attributeName="slotTimeEnd")
    private String slotTimeEnd;

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(final int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getSlotTimeStart() {
        return slotTimeStart;
    }

    public void setSlotTimeStart(final String slotTimeStart) {
        this.slotTimeStart = slotTimeStart;
    }

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
