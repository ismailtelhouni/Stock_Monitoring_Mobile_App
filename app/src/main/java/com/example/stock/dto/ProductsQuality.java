package com.example.stock.dto;

public class ProductsQuality {

    double ripe , underripe , overripe ;

    @Override
    public String toString() {
        return "ProductsQuality{" +
                "ripe=" + ripe +
                ", underripe=" + underripe +
                ", overripe=" + overripe +
                '}';
    }

    public ProductsQuality(double ripe, double underripe, double overripe) {
        this.ripe = ripe;
        this.underripe = underripe;
        this.overripe = overripe;
    }

    public double getRipe() {
        return ripe;
    }

    public void setRipe(double ripe) {
        this.ripe = ripe;
    }

    public double getUnderripe() {
        return underripe;
    }

    public void setUnderripe(double underripe) {
        this.underripe = underripe;
    }

    public double getOverripe() {
        return overripe;
    }

    public void setOverripe(double overripe) {
        this.overripe = overripe;
    }
}
