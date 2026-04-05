/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Motorbike;

/**
 *
 * @author MSI LAPTOP
 */
public class MotorbikeDAO extends DBContext {

    PreparedStatement st;
    ResultSet rs;

    // ✅ Get all motorbikes
    public List<Motorbike> getAllMotorbikes() {
        List<Motorbike> list = new ArrayList<>();
        String sql = "SELECT * FROM motorbikes";

        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapMotorbike(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching motorbikes: " + e.getMessage());
        }

        return list;
    }

    // ✅ Get only available motorbikes
    public List<Motorbike> getAvailableMotorbikes() {
        List<Motorbike> list = new ArrayList<>();
        String sql = "SELECT * FROM motorbikes WHERE status = 'available'";

        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapMotorbike(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching available motorbikes: " + e.getMessage());
        }

        return list;
    }

    // ✅ Get motorbike by ID
    public Motorbike getMotorbikeById(int id) {
        String sql = "SELECT * FROM motorbikes WHERE id = ?";
        System.out.println("Searching bike with id: " + id);

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                System.out.println("Bike found in DB!");
                return new Motorbike(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        // Database has 'current_price_per_day'
                        rs.getDouble("current_price_per_day"),
                        rs.getString("status"),
                        rs.getString("description"),
                        // Database has 'engine_size'
                        rs.getString("engine_size"),
                        rs.getString("transmission"),
                        // Database has 'manufacture_year'
                        rs.getObject("manufacture_year") != null
                        ? rs.getInt("manufacture_year")
                        : null,
                        // Database has 'bikeIcon'
                        rs.getString("bikeIcon")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ Map ResultSet → Motorbike object
    private Motorbike mapMotorbike(ResultSet rs) throws SQLException {

        Motorbike bike = new Motorbike();

        bike.setId(rs.getInt("id"));
        bike.setName(rs.getString("name"));
        bike.setBrand(rs.getString("brand"));
        bike.setCurrentPricePerDay(rs.getDouble("current_price_per_day"));
        bike.setStatus(rs.getString("status"));
        bike.setDescription(rs.getString("description"));
        bike.setEngineSize(rs.getString("engine_size"));
        bike.setTransmission(rs.getString("transmission"));

        int year = rs.getInt("manufacture_year");
        if (!rs.wasNull()) {
            bike.setManufactureYear(year);
        }

        bike.setBikeIcon(rs.getString("bikeIcon"));

        return bike;
    }

    public List<Motorbike> getTop20Motorbikes() {
        List<Motorbike> list = new ArrayList<>();

    String sql = "SELECT TOP 20 * FROM motorbikes WHERE status = 'available' ORDER BY id DESC";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Motorbike bike = new Motorbike(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        // Database has 'current_price_per_day'
                        rs.getDouble("current_price_per_day"),
                        rs.getString("status"),
                        rs.getString("description"),
                        // Database has 'engine_size'
                        rs.getString("engine_size"),
                        rs.getString("transmission"),
                        // Database has 'manufacture_year'
                        rs.getObject("manufacture_year") != null
                        ? rs.getInt("manufacture_year")
                        : null,
                        // Database has 'bikeIcon'
                        rs.getString("bikeIcon")
                );

                list.add(bike);
            }

        } catch (SQLException e) {
            System.out.println("Error getTop20Motorbikes: " + e.getMessage());
        }

        return list;
    }

// 1. Get all unique brands for the first dropdown
    public List<String> getAllBrands() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM motorbikes ORDER BY brand";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("brand"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Get bikes by brand for the second dropdown
    public List<Motorbike> getBikesByBrand(String brand) {
        List<Motorbike> list = new ArrayList<>();
        String sql = "SELECT * FROM motorbikes WHERE brand = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, brand);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Motorbike b = new Motorbike();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setBrand(rs.getString("brand"));
                b.setCurrentPricePerDay(rs.getDouble("current_price_per_day"));
                b.setStatus(rs.getString("status"));
                // Map other fields from your Motorbike model
                b.setEngineSize(rs.getString("engine_size"));
                b.setTransmission(rs.getString("transmission"));
                b.setManufactureYear(rs.getObject("manufacture_year", Integer.class));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Motorbike> getMotorbikesPaging(int offset, int limit, String sort) {

        List<Motorbike> list = new ArrayList<>();

        String orderBy = "ORDER BY id";

        if ("priceAsc".equals(sort)) {
            orderBy = "ORDER BY current_price_per_day ASC";
        } else if ("priceDesc".equals(sort)) {
            orderBy = "ORDER BY current_price_per_day DESC";
        } else if ("name".equals(sort)) {
            orderBy = "ORDER BY name ASC";
        }

        String sql
                = "SELECT * FROM motorbikes "
                + orderBy
                + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, offset);
            st.setInt(2, limit);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapMotorbike(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalMotorbikes() {

        String sql = "SELECT COUNT(*) FROM motorbikes";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Motorbike> getMotorbikesFiltered(String brand, String engine, String transmission, String sort, int offset, int limit) {
    List<Motorbike> list = new ArrayList<>();
    // Thay đổi: Chỉ lấy xe có status = 'available'
    String sql = "SELECT * FROM motorbikes WHERE status = 'available'"; 
    
    if (brand != null && !brand.isEmpty()) {
        sql += " AND brand = ?";
    }
    if (transmission != null && !transmission.isEmpty()) {
        sql += " AND transmission = ?";
    }
    if ("small".equals(engine)) {
        sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) < 175";
    } else if ("medium".equals(engine)) {
        sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) >= 175 AND CAST(REPLACE(engine_size,'cc','') AS INT) < 600";
    } else if ("large".equals(engine)) {
        sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) >= 600";
    }

    if ("priceAsc".equals(sort)) {
        sql += " ORDER BY current_price_per_day ASC";
    } else if ("priceDesc".equals(sort)) {
        sql += " ORDER BY current_price_per_day DESC";
    } else {
        sql += " ORDER BY id";
    }
    sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        int paramIndex = 1;
        if (brand != null && !brand.isEmpty()) {
            st.setString(paramIndex++, brand);
        }
        if (transmission != null && !transmission.isEmpty()) {
            st.setString(paramIndex++, transmission);
        }
        st.setInt(paramIndex++, offset);
        st.setInt(paramIndex, limit);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(mapMotorbike(rs));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    public int getTotalMotorbikesFiltered(String brand, String engine, String transmission) {

        int total = 0;

         String sql = "SELECT COUNT(*) FROM motorbikes WHERE status = 'available'";

        if (brand != null && !brand.isEmpty()) {
            sql += " AND brand = ?";
        }

        if (transmission != null && !transmission.isEmpty()) {
            sql += " AND transmission = ?";
        }

        if ("small".equals(engine)) {
            sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) < 175";
        } else if ("medium".equals(engine)) {
            sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) >= 175 AND CAST(REPLACE(engine_size,'cc','') AS INT) < 600";
        } else if ("large".equals(engine)) {
            sql += " AND CAST(REPLACE(engine_size,'cc','') AS INT) >= 600";
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            int paramIndex = 1;

            if (brand != null && !brand.isEmpty()) {
                st.setString(paramIndex++, brand);
            }

            if (transmission != null && !transmission.isEmpty()) {
                st.setString(paramIndex++, transmission);
            }

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

     // 1. Chỉ đếm những xe chưa bị xóa mềm
    public int getTotalBikes() {
        String sql = "SELECT COUNT(*) FROM motorbikes WHERE status <> 'Deleted'";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Lấy danh sách xe: Thêm điều kiện status <> 'Deleted'
    public List<Motorbike> getAllMotorbikes(String search) {
        List<Motorbike> list = new ArrayList<>();
        // Thêm ngoặc đơn cho cụm LIKE để không bị lẫn với điều kiện status
        String sql = "SELECT * FROM motorbikes WHERE (name LIKE ? OR brand LIKE ?) AND status <> 'Deleted'";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            String keyword = "%" + (search == null ? "" : search) + "%";
            st.setString(1, keyword);
            st.setString(2, keyword);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Motorbike b = mapResultSetToMotorbike(rs);
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Hàm XÓA MỀM: Update status thay vì DELETE
    public void delete(int id) {
        String sql = "UPDATE motorbikes SET status = 'Deleted' WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Đếm có phân trang: Thêm status <> 'Deleted'
    public int getTotalMotorbikes(String search) {
        String sql = "SELECT COUNT(*) FROM motorbikes WHERE (name LIKE ? OR brand LIKE ?) AND status <> 'Deleted'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            String keyword = "%" + (search == null ? "" : search) + "%";
            st.setString(1, keyword);
            st.setString(2, keyword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 5. Lấy danh sách phân trang: Thêm status <> 'Deleted'
    public List<Motorbike> getBikesPaging(String search, int pageIndex, int pageSize) {
        List<Motorbike> list = new ArrayList<>();
        String sql = "SELECT * FROM motorbikes WHERE (name LIKE ? OR brand LIKE ?) AND status <> 'Deleted' "
                + "ORDER BY id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            String keyword = "%" + (search == null ? "" : search) + "%";
            st.setString(1, keyword);
            st.setString(2, keyword);
            st.setInt(3, (pageIndex - 1) * pageSize);
            st.setInt(4, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToMotorbike(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm phụ để tránh lặp code mapping dữ liệu
    private Motorbike mapResultSetToMotorbike(ResultSet rs) throws SQLException {
        Motorbike b = new Motorbike();
        b.setId(rs.getInt("id"));
        b.setName(rs.getNString("name"));
        b.setBrand(rs.getNString("brand"));
        b.setCurrentPricePerDay(rs.getDouble("current_price_per_day"));
        b.setStatus(rs.getString("status"));
        b.setDescription(rs.getNString("description"));
        b.setEngineSize(rs.getString("engine_size"));
        b.setTransmission(rs.getString("transmission"));
        int year = rs.getInt("manufacture_year");
        if (!rs.wasNull()) {
            b.setManufactureYear(year);
        }
        b.setBikeIcon(rs.getString("bikeIcon"));
        return b;
    }
      public void insert(Motorbike b) {
        String sql = "INSERT INTO motorbikes (name, brand, current_price_per_day, status, description, engine_size, transmission, manufacture_year, bikeIcon) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, b.getName());
            st.setString(2, b.getBrand());
            st.setDouble(3, b.getCurrentPricePerDay());
            st.setString(4, b.getStatus());
            st.setString(5, b.getDescription());
            st.setString(6, b.getEngineSize());
            st.setString(7, b.getTransmission());
            // Xử lý Integer có thể null cho manufacture_year
            if (b.getManufactureYear() != null) {
                st.setInt(8, b.getManufactureYear());
            } else {
                st.setNull(8, java.sql.Types.INTEGER);
            }
            st.setString(9, b.getBikeIcon());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Motorbike b) {
        String sql = "UPDATE motorbikes SET name=?, brand=?, current_price_per_day=?, status=?, "
                + "description=?, engine_size=?, transmission=?, manufacture_year=?, bikeIcon=? "
                + "WHERE id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, b.getName());
            st.setString(2, b.getBrand());
            st.setDouble(3, b.getCurrentPricePerDay());
            st.setString(4, b.getStatus());
            st.setString(5, b.getDescription());
            st.setString(6, b.getEngineSize());
            st.setString(7, b.getTransmission());
            if (b.getManufactureYear() != null) {
                st.setInt(8, b.getManufactureYear());
            } else {
                st.setNull(8, java.sql.Types.INTEGER);
            }
            st.setString(9, b.getBikeIcon());
            st.setInt(10, b.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
