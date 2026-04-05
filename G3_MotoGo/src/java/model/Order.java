/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Order {

    private int id;
    private int userId;         // ID người dùng để tiện truy vấn nếu cần
    private String userName; 
    private String userEmail;
    private String phone;       // Cần thiết để Admin gọi xác nhận đơn
    
    private int bikeId;
    private String bikeName; 
    private double pricePerDayAtRent; // Giá thuê xe/ngày tại thời điểm đó
    
    private String startDate;   // Có thể để String hoặc Date (java.sql.Date)
    private String endDate;
    private int totalDays;      // Để giải thích cách tính tiền (startDate -> endDate)
    
    private double totalBikeCost;  // Tiền thuê xe (days * price)
    private double totalStuffCost; // Tiền thuê đồ phượt (gear)
    private double depositAmount;  // Tiền đặt cọc (Rất quan trọng với thuê xe)
    private double totalAmount;    // Tổng cộng: Bike + Stuff
    
    private String status;         // // pending, confirmed, active, completed, cancelled
    private String paymentStatus;  // unpaid, deposit_paid, paid, refunded, failed
    

    public Order() {
    }

    public Order(int id, int userId, String userName, String userEmail, String phone, int bikeId, String bikeName, double pricePerDayAtRent, String startDate, String endDate, int totalDays, double totalBikeCost, double totalStuffCost, double depositAmount, double totalAmount, String status, String paymentStatus) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.phone = phone;
        this.bikeId = bikeId;
        this.bikeName = bikeName;
        this.pricePerDayAtRent = pricePerDayAtRent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.totalBikeCost = totalBikeCost;
        this.totalStuffCost = totalStuffCost;
        this.depositAmount = depositAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    public Order(int id, String userName, String userEmail, String bikeName, String startDate, String endDate, double totalAmount, String status) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bikeName = bikeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }


    public Order(int id, String userEmail, String bikeName,
            double totalAmount, String status) {
        this.id = id;
        this.userEmail = userEmail;
        this.bikeName = bikeName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public double getPricePerDayAtRent() {
        return pricePerDayAtRent;
    }

    public void setPricePerDayAtRent(double pricePerDayAtRent) {
        this.pricePerDayAtRent = pricePerDayAtRent;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public double getTotalAmount() {
          if (this.totalAmount <= 0) {
        return this.totalBikeCost + this.totalStuffCost;
    }
    return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
