
package model;

/**
 *
 * @author MSI LAPTOP
 */
public class Motorbike {
   
    private int id;
    private String name;
    private String brand;
    private double currentPricePerDay;
    private String status;
    private String description;
    private String engineSize;
    private String transmission;
    private Integer manufactureYear; // use Integer because DB can be NULL
    private String bikeIcon;

    // Empty constructor
    public Motorbike() {
    }

    // Constructor without optional fields
    public Motorbike(int id, String name, String brand, double currentPricePerDay, String status) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.currentPricePerDay = currentPricePerDay;
        this.status = status;
    }

    // Full constructor
    public Motorbike(int id, String name, String brand, double currentPricePerDay,
                     String status, String description, String engineSize,
                     String transmission, Integer manufactureYear, String bikeIcon) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.currentPricePerDay = currentPricePerDay;
        this.status = status;
        this.description = description;
        this.engineSize = engineSize;
        this.transmission = transmission;
        this.manufactureYear = manufactureYear;
        this.bikeIcon = bikeIcon;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getCurrentPricePerDay() {
        return currentPricePerDay;
    }

    public void setCurrentPricePerDay(double currentPricePerDay) {
        this.currentPricePerDay = currentPricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(String engineSize) {
        this.engineSize = engineSize;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getBikeIcon() {
        return bikeIcon;
    }

    public void setBikeIcon(String bikeIcon) {
        this.bikeIcon = bikeIcon;
    }

}
