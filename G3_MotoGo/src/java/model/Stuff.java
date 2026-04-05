/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thais
 */
public class Stuff {

    private int stuffId;
    private String stuffName;
    private double basePricePerDay;
    private String stuffIcon;
    private int includedQuantity;

    public Stuff(int stuffId, String stuffName, double basePricePerDay, String stuffIcon) {
        this.stuffId = stuffId;
        this.stuffName = stuffName;
        this.basePricePerDay = basePricePerDay;
        this.stuffIcon = stuffIcon;
    }

    public Stuff() {
    }

    public int getStuffId() {
        return stuffId;
    }

    public String getStuffName() {
        return stuffName;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public String getStuffIcon() {
        return stuffIcon;
    }

    public void setStuffId(int stuffId) {
        this.stuffId = stuffId;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public void setBasePricePerDay(double basePricePerDay) {
        this.basePricePerDay = basePricePerDay;
    }

    public void setStuffIcon(String stuffIcon) {
        this.stuffIcon = stuffIcon;
    }

    public int getIncludedQuantity() {
        return includedQuantity;
    }

    public void setIncludedQuantity(int includedQuantity) {
        this.includedQuantity = includedQuantity;
    }
    

}
