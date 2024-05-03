package com.example.stock.model;

import java.util.Date;

public class Stock
{
    String id , name , category , type ;
    Double quantity , entryDate ;
    Date date;

    public Stock(String name, Double quantity, Double entryDate, String category, String type) {
        this.name = name;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.category = category;
        this.type = type;
    }

    public Stock() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Double entryDate) {
        this.entryDate = entryDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StockDao{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

