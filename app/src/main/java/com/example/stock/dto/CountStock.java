package com.example.stock.dto;

public class CountStock {

    private double nbrTotal ;

    public CountStock() {
    }

    public CountStock(double nbrTotal) {
        this.nbrTotal = nbrTotal;
    }

    public double getNbrTotal() {
        return nbrTotal;
    }

    public void setNbrTotal(double nbrTotal) {
        this.nbrTotal = nbrTotal;
    }
}
