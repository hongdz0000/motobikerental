/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Stuff;

/**
 *
 * @author thais
 */
public class StuffDAO extends DBContext {

    PreparedStatement st;
    ResultSet rs;

    // READ
    public List<Stuff> getAllStuff() {
        List<Stuff> list = new ArrayList<>();
        String sql = "SELECT * FROM stuff";

        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Stuff(
                        rs.getInt("stuffId"),
                        rs.getString("stuffName"),
                        rs.getDouble("base_price_per_day"),
                        rs.getString("stuffIcon")
                ));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }

    // CREATE
    public void addStuff(String name, double price) {
        String sql = "INSERT INTO stuff(stuffName, base_price_per_day) VALUES(?, ?)";

        try {
            st = connection.prepareStatement(sql);
            st.setString(1, name);
            st.setDouble(2, price);
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // UPDATE
    public void updateStuff(int id, double price) {
        String sql = "UPDATE stuff SET base_price_per_day=? WHERE stuffId=?";

        try {
            st = connection.prepareStatement(sql);
            st.setDouble(1, price);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // DELETE
    public void deleteStuff(int stuffId) {

        try {

            // delete child rows first
            String sql1 = "DELETE FROM order_stuff_details WHERE stuff_id = ?";
            st = connection.prepareStatement(sql1);
            st.setInt(1, stuffId);
            st.executeUpdate();

            // delete accessory
            String sql2 = "DELETE FROM stuff WHERE stuffId = ?";
            st = connection.prepareStatement(sql2);
            st.setInt(1, stuffId);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }

    public double getBasePrice(int id) {

        String sql = "SELECT base_price_per_day FROM stuff WHERE stuffId = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("base_price_per_day");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Map<Stuff, Integer> getStuffMapByBike(int bikeId) {
        // Using LinkedHashMap to preserve the order from the database query
        Map<Stuff, Integer> map = new LinkedHashMap<>();

        String sql = "SELECT "
                + " s.stuffId, "
                + " s.stuffName, "
                + " COALESCE(msc.price_override_per_day, s.base_price_per_day) AS price_per_day, "
                + " msc.included_quantity, " // Retrieve from config table
                + " s.stuffIcon "
                + " FROM motorbike_stuff_config msc "
                + " LEFT JOIN stuff s ON s.stuffId = msc.stuff_id "
                + " WHERE msc.motorbike_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, bikeId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Create the Stuff object with basic info
                Stuff s = new Stuff(
                        rs.getInt("stuffId"),
                        rs.getString("stuffName"),
                        rs.getDouble("price_per_day"),
                        rs.getString("stuffIcon")
                );

                // Map the Stuff object (Key) to the included_quantity (Value)
                map.put(s, rs.getInt("included_quantity"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public void updateBikeStuffConfig(int bikeId, int stuffId, Double price, int qty) {

        // SQL với 2 dấu hỏi cho SET và 2 dấu hỏi cho WHERE
        String sql = "UPDATE motorbike_stuff_config "
                + "SET price_override_per_day = ?, "
                + "included_quantity = ? "
                + "WHERE motorbike_id = ? AND stuff_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Tham số 1: Price Override (có thể null)
            if (price == null) {
                ps.setNull(1, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(1, price);
            }

            // Tham số 2: Included Quantity (kiểu int)
            ps.setInt(2, qty);

            // Tham số 3 & 4: Các điều kiện trong WHERE clause
            ps.setInt(3, bikeId);
            ps.setInt(4, stuffId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalStuffCount() {
        String sql = "SELECT COUNT(*) FROM stuff";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm số lượng stuff: " + e.getMessage());
        }
        return 0;
    }

    public List<Stuff> getAll() {
        List<Stuff> list = new ArrayList<>();
        String sql = "SELECT * FROM stuff";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Stuff s = new Stuff();
                s.setStuffId(rs.getInt("stuffId"));
                s.setStuffName(rs.getNString("stuffName"));
                s.setBasePricePerDay(rs.getDouble("base_price_per_day"));
                s.setStuffIcon(rs.getString("stuffIcon"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void insert(Stuff s) {
        String sql = "INSERT INTO stuff (stuffName, base_price_per_day, stuffIcon) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setNString(1, s.getStuffName());
            st.setDouble(2, s.getBasePricePerDay());
            st.setString(3, s.getStuffIcon());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Stuff getById(int id) {
        String sql = "SELECT * FROM stuff WHERE stuffId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Stuff s = new Stuff();
                s.setStuffId(rs.getInt("stuffId"));
                s.setStuffName(rs.getNString("stuffName"));
                s.setBasePricePerDay(rs.getDouble("base_price_per_day"));
                s.setStuffIcon(rs.getString("stuffIcon"));
                return s;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void update(Stuff s) {
        String sql = "UPDATE stuff SET stuffName=?, base_price_per_day=?, stuffIcon=? WHERE stuffId=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setNString(1, s.getStuffName());
            st.setDouble(2, s.getBasePricePerDay());
            st.setString(3, s.getStuffIcon());
            st.setInt(4, s.getStuffId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM stuff WHERE stuffId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<Stuff> getStuffByMotorbikeId(int motorbikeId) {
        List<Stuff> list = new ArrayList<>();
        // COALESCE: Nếu c.price_override_per_day NULL thì lấy s.base_price_per_day
        // Nếu c.included_quantity NULL thì lấy 0 (nghĩa là không tặng kèm)
        String sql = "SELECT s.stuffId, s.stuffName, s.stuffIcon, "
                + "COALESCE(c.price_override_per_day, s.base_price_per_day) AS final_price, "
                + "COALESCE(c.included_quantity, 1) AS included_quantity "
                + "FROM stuff s "
                + "LEFT JOIN motorbike_stuff_config c ON s.stuffId = c.stuff_id AND c.motorbike_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, motorbikeId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Stuff s = new Stuff();
                s.setStuffId(rs.getInt("stuffId"));
                s.setStuffName(rs.getString("stuffName"));
                s.setStuffIcon(rs.getString("stuffIcon"));
                s.setBasePricePerDay(rs.getDouble("final_price"));
                s.setIncludedQuantity(rs.getInt("included_quantity"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Stuff> getStuffWithPaging(String search, int page, int pageSize) {
        List<Stuff> list = new ArrayList<>();
        // SQL Server sử dụng OFFSET ... FETCH để phân trang
        String sql = "SELECT * FROM stuff WHERE stuffName LIKE ? "
                + "ORDER BY stuffId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setNString(1, "%" + search + "%");
            st.setInt(2, (page - 1) * pageSize);
            st.setInt(3, pageSize);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Stuff s = new Stuff();
                s.setStuffId(rs.getInt("stuffId"));
                s.setStuffName(rs.getNString("stuffName"));
                s.setBasePricePerDay(rs.getDouble("base_price_per_day"));
                s.setStuffIcon(rs.getString("stuffIcon"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public int countSearch(String search) {
        String sql = "SELECT COUNT(*) FROM stuff WHERE stuffName LIKE ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setNString(1, "%" + search + "%");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public Map<Stuff, Integer> getStuffMapByBikePaging(int bikeId, String search, int page, int pageSize) {
        Map<Stuff, Integer> map = new LinkedHashMap<>();
        // Đảm bảo search không bị null để dùng trong LIKE
        String keyword = "%" + (search == null ? "" : search) + "%";

        // SQL: Join bảng stuff và config để lấy giá trị override và số lượng kèm theo
        String sql = "SELECT s.stuffId, s.stuffName, s.stuffIcon, "
                + "COALESCE(msc.price_override_per_day, s.base_price_per_day) AS final_price, "
                + "msc.included_quantity "
                + "FROM motorbike_stuff_config msc "
                + "JOIN stuff s ON s.stuffId = msc.stuff_id "
                + "WHERE msc.motorbike_id = ? AND s.stuffName LIKE ? "
                + "ORDER BY s.stuffId "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bikeId);
            ps.setNString(2, keyword);
            ps.setInt(3, (page - 1) * pageSize);
            ps.setInt(4, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tạo đối tượng Stuff với giá đã được xử lý (override hoặc base)
                    Stuff s = new Stuff();
                    s.setStuffId(rs.getInt("stuffId"));
                    s.setStuffName(rs.getNString("stuffName"));
                    s.setStuffIcon(rs.getString("stuffIcon"));
                    s.setBasePricePerDay(rs.getDouble("final_price")); // Giá áp dụng cho xe này

                    // Đưa vào Map: Key là đối tượng Stuff, Value là số lượng đi kèm (included_quantity)
                    map.put(s, rs.getInt("included_quantity"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tại getStuffMapByBikePaging: " + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Hàm bổ trợ để đếm tổng số phụ kiện của xe nhằm phục vụ tính toán phân
     * trang
     */
    public int countStuffByBike(int bikeId, String search) {
        String keyword = "%" + (search == null ? "" : search) + "%";
        String sql = "SELECT COUNT(*) FROM motorbike_stuff_config msc "
                + "JOIN stuff s ON s.stuffId = msc.stuff_id "
                + "WHERE msc.motorbike_id = ? AND s.stuffName LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bikeId);
            ps.setNString(2, keyword);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Stuff> getAvailableStuffToAddToBike(int bikeId, String search) {
        List<Stuff> list = new ArrayList<>();
        String keyword = "%" + (search == null ? "" : search) + "%";

        // Lấy tất cả stuff mà stuffId KHÔNG nằm trong cấu hình của xe hiện tại
        String sql = "SELECT * FROM stuff "
                + "WHERE stuffName LIKE ? "
                + "AND stuffId NOT IN (SELECT stuff_id FROM motorbike_stuff_config WHERE motorbike_id = ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setNString(1, keyword);
            ps.setInt(2, bikeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Stuff s = new Stuff();
                    s.setStuffId(rs.getInt("stuffId"));
                    s.setStuffName(rs.getNString("stuffName"));
                    s.setBasePricePerDay(rs.getDouble("base_price_per_day"));
                    s.setStuffIcon(rs.getString("stuffIcon"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Hàm thực hiện INSERT mới vào bảng config
    public void addNewBikeStuffConfig(int bikeId, int stuffId) {
        // Mặc định khi mới add: số lượng = 1, giá override = NULL (dùng giá gốc)
        String sql = "INSERT INTO motorbike_stuff_config (motorbike_id, stuff_id, included_quantity, price_override_per_day) "
                + "VALUES (?, ?, 1, NULL)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bikeId);
            ps.setInt(2, stuffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBikeStuffConfig(int bikeId, int stuffId) {
        String sql = "DELETE FROM motorbike_stuff_config WHERE motorbike_id = ? AND stuff_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bikeId);
            ps.setInt(2, stuffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
