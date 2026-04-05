/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author thais
 */
public class RentOrder {

    private int id;
    private int userId;
    private int motorbikeId;
    private double pricePerDay;
    private Timestamp startDate;
    private Timestamp endDate;
    private int totalDays;
    private double totalBikeCost;
    private double totalStuffCost;
    private double depositAmount;
    private String status;
    private String paymentStatus;
    private String motorbikeName;
    private String motorbikeIcon;

    public String getMotorbikeName() {
        return motorbikeName;
    }

    public void setMotorbikeName(String motorbikeName) {
        this.motorbikeName = motorbikeName;
    }

    public String getMotorbikeIcon() {
        return motorbikeIcon;
    }

    public void setMotorbikeIcon(String motorbikeIcon) {
        this.motorbikeIcon = motorbikeIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMotorbikeId() {
        return motorbikeId;
    }

    public void setMotorbikeId(int motorbikeId) {
        this.motorbikeId = motorbikeId;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public double getTotalBikeCost() {
        return totalBikeCost;
    }

    public void setTotalBikeCost(double totalBikeCost) {
        this.totalBikeCost = totalBikeCost;
    }

    public double getTotalStuffCost() {
        return totalStuffCost;
    }

    public void setTotalStuffCost(double totalStuffCost) {
        this.totalStuffCost = totalStuffCost;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
