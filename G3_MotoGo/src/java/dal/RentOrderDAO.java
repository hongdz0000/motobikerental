/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.*;
import model.RentOrder;

public class RentOrderDAO extends DBContext {

    public List<RentOrder> getPastRentals(int userId, String sortOrder, int page, int pageSize) {
        List<RentOrder> list = new ArrayList<>();
        int offset = (page - 1) * pageSize; // Tính toán vị trí bắt đầu

        String sql = "SELECT r.*, m.name, m.bikeIcon"
                + " FROM rent_orders r"
                + " JOIN motorbikes m ON r.motorbike_id = m.id"
                + " WHERE r.user_id = ?"
                + " ORDER BY r.start_date " + sortOrder
                + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // SQL Server paging

        try {

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                RentOrder r = new RentOrder();

                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setMotorbikeId(rs.getInt("motorbike_id"));
                r.setPricePerDay(rs.getDouble("price_per_day_at_rent"));
                r.setStartDate(rs.getTimestamp("start_date"));
                r.setEndDate(rs.getTimestamp("end_date"));
                r.setTotalDays(rs.getInt("total_days"));
                r.setTotalBikeCost(rs.getDouble("total_bike_cost"));
                r.setTotalStuffCost(rs.getDouble("total_stuff_cost"));
                r.setDepositAmount(rs.getDouble("deposit_amount"));
                r.setStatus(rs.getString("status"));
                r.setPaymentStatus(rs.getString("payment_status"));
                r.setMotorbikeName(rs.getString("name"));
                r.setMotorbikeIcon(rs.getString("bikeIcon"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalOrdersCount(int userId) {
        String sql = "SELECT COUNT(*) FROM rent_orders WHERE user_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalOrders() {
    String sql = "SELECT COUNT(*) FROM rent_orders";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public int getPendingOrdersCount() {
    String sql = "SELECT COUNT(*) FROM rent_orders WHERE status = 'pending'";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public double getTotalRevenue() {
    // Tính tổng tiền từ các đơn đã thanh toán (payment_status = 'paid')
    String sql = "SELECT SUM(total_bike_cost + total_stuff_cost) FROM rent_orders WHERE payment_status = 'paid'";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getDouble(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

  

}
