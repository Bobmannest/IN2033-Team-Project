package com.example.catalogue;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogueItem {
    @JsonProperty("item_id")
    private String item_id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("package_type")
    private String package_type;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("units_per_pack")
    private int units_per_pack;
    @JsonProperty("package_cost")
    private double package_cost;
    @JsonProperty("availability")
    private int availability;
    @JsonProperty("stock_limit")
    private int stock_limit;
    @JsonProperty("status")
    private String status;
    @JsonProperty("order_percentage")
    private double order_percentage;

    public CatalogueItem() {}

    // Getters
    public String getItem_id() {return item_id;}
    public String getDescription() {return description;}
    public String getPackage_type() {return package_type;}
    public String getUnit() {return unit;}
    public int getUnits_per_pack() {return units_per_pack;}
    public double getPackage_cost() {return package_cost;}
    public int getAvailability() {return availability;}
    public int getStock_limit() {return stock_limit;}
    public String getStatus() {return status;}
    public double getOrder_percentage() {return order_percentage;}

    // Setters
    public void setItem_id(String item_id) {this.item_id = item_id;}
    public void setDescription(String description) {this.description = description;}
    public void setPackage_type(String package_type) {this.package_type = package_type;}
    public void setUnit(String unit) {this.unit = unit;}
    public void setUnits_per_pack(int units_per_pack) {this.units_per_pack = units_per_pack;}
    public void setPackage_cost(double package_cost) {this.package_cost = package_cost;}
    public void setAvailability(int availability) {this.availability = availability;}
    public void setStock_limit(int stock_limit) {this.stock_limit = stock_limit;}
    public void setStatus(String status) {this.status = status;}
    public void setOrder_percentage(double order_percentage) {this.order_percentage = order_percentage;}
}
