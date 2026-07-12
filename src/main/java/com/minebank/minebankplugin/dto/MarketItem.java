package com.minebank.minebankplugin.dto;

public class MarketItem {
    private String minecraftMaterial;
    private double currentPrice;
    private Integer stock;

    public String getminecraftMaterial() {
        return minecraftMaterial;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public Integer getStock() {
        return stock;
    }
}
