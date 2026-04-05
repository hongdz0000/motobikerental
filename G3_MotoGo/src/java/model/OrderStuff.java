/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author MSI LAPTOP
 */
public class OrderStuff {
    private int stuffId;
    private String name;
    private int quantity;
    private double pricePerDay; // Giá thuê món đồ đó/ngày
    private double totalPrice;

    public OrderStuff() {
    }

    public OrderStuff(int stuffId, String name, int quantity, double pricePerDay, double totalPrice) {
        this.stuffId = stuffId;
        this.name = name;
        this.quantity = quantity;
        this.pricePerDay = pricePerDay;
        this.totalPrice = totalPrice;
    }

    public OrderStuff(String name, double pricePerDay, int quantity) {
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.quantity = quantity;
    }

    public int getStuffId() {
        return stuffId;
    }

    public void setStuffId(int stuffId) {
        this.stuffId = stuffId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
