/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderStuff;
import model.Stuff;

/**
 *
 * @author Admin
 */
public class OrderDAO extends DBContext {

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        // Thêm r.payment_status vào câu SELECT
        String sql = "SELECT r.id, u.userEmail, m.name AS bikeName, "
                + "r.total_bike_cost, r.total_stuff_cost, r.status, r.payment_status "
                + "FROM rent_orders r "
                + "JOIN users u ON r.user_id = u.userId "
                + "JOIN motorbikes m ON r.motorbike_id = m.id "
                + "ORDER BY r.id DESC"; // Sắp xếp đơn mới nhất lên đầu

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserEmail(rs.getString("userEmail"));
                o.setBikeName(rs.getString("bikeName"));
                o.setTotalBikeCost(rs.getDouble("total_bike_cost"));
                o.setTotalStuffCost(rs.getDouble("total_stuff_cost"));
                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status")); // Cần thiết cho JSP
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> list = new ArrayList<>();
        // SQL đầy đủ các cột tương tự getAllOrders và có thêm điều kiện WHERE
        String sql = "SELECT r.id, u.userEmail, m.name AS bikeName, "
                + "r.total_bike_cost, r.total_stuff_cost, r.status, r.payment_status "
                + "FROM rent_orders r "
                + "JOIN users u ON r.user_id = u.userId "
                + "JOIN motorbikes m ON r.motorbike_id = m.id "
                + "WHERE r.status = ? "
                + "ORDER BY r.id DESC";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserEmail(rs.getString("userEmail"));
                o.setBikeName(rs.getString("bikeName"));
                o.setTotalBikeCost(rs.getDouble("total_bike_cost"));
                o.setTotalStuffCost(rs.getDouble("total_stuff_cost"));
                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status"));

                // Nếu bạn vẫn muốn dùng setTotalAmount cho JSP (tổng 2 loại phí)
                o.setTotalAmount(rs.getDouble("total_bike_cost") + rs.getDouble("total_stuff_cost"));

                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order getOrderById(int orderId) {
        // Truy vấn lấy đầy đủ thông tin chi tiết
        String sql = "SELECT o.*, u.userName, u.userEmail, u.phone, m.name as bikeName "
                + "FROM rent_orders o "
                + "JOIN users u ON o.user_id = u.userId "
                + "JOIN motorbikes m ON o.motorbike_id = m.id "
                + "WHERE o.id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserName(rs.getString("userName"));
                o.setUserEmail(rs.getString("userEmail"));
                o.setPhone(rs.getString("phone")); // Thêm trường này vào Model Order
                o.setBikeName(rs.getString("bikeName"));
                o.setStartDate(rs.getString("start_date"));
                o.setEndDate(rs.getString("end_date"));
                o.setTotalDays(rs.getInt("total_days"));
                o.setPricePerDayAtRent(rs.getDouble("price_per_day_at_rent"));
                o.setTotalBikeCost(rs.getDouble("total_bike_cost"));
                o.setTotalStuffCost(rs.getDouble("total_stuff_cost"));
                o.setDepositAmount(rs.getDouble("deposit_amount"));
                o.setTotalAmount(o.getTotalBikeCost() + o.getTotalStuffCost());
                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status"));
                return o;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderStuff> getGearByOrderId(int orderId) {
        List<OrderStuff> list = new ArrayList<>();
        // Join với rent_orders để lấy total_days nhằm tính toán chính xác sub-total
        String sql = "SELECT s.stuffName, osd.price_per_day_at_rent, osd.quantity, "
                + "(osd.price_per_day_at_rent * osd.quantity * o.total_days) as sub_total "
                + "FROM order_stuff_details osd "
                + "JOIN stuff s ON osd.stuff_id = s.stuffId "
                + "JOIN rent_orders o ON osd.order_id = o.id "
                + "WHERE osd.order_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                OrderStuff os = new OrderStuff();
                os.setName(rs.getString("stuffName"));
                os.setPricePerDay(rs.getDouble("price_per_day_at_rent"));
                os.setQuantity(rs.getInt("quantity"));
                os.setTotalPrice(rs.getDouble("sub_total"));
                list.add(os);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        // 1. Cập nhật trạng thái đơn hàng
        String sqlOrder = "UPDATE rent_orders SET status = ? WHERE id = ?";

        // 2. Cập nhật trạng thái xe (Dùng Subquery để tìm motorbike_id từ orderId)
        String sqlBike = "UPDATE motorbikes SET status = ? WHERE id = (SELECT motorbike_id FROM rent_orders WHERE id = ?)";

        try {
            connection.setAutoCommit(false); // Bắt đầu giao dịch (Transaction)

            // BƯỚC 1: Cập nhật trạng thái Đơn hàng
            PreparedStatement psOrder = connection.prepareStatement(sqlOrder);
            psOrder.setString(1, newStatus);
            psOrder.setInt(2, orderId);
            int orderUpdated = psOrder.executeUpdate();

            if (orderUpdated > 0) {
                // BƯỚC 2: Xác định trạng thái Xe dựa trên trạng thái Đơn mới
                String bikeStatus = "";
                if (newStatus.equalsIgnoreCase("confirmed") || newStatus.equalsIgnoreCase("active")) {
                    bikeStatus = "rented"; // Xe đang được giữ hoặc đang đi
                } else if (newStatus.equalsIgnoreCase("completed") || newStatus.equalsIgnoreCase("cancelled")) {
                    bikeStatus = "available"; // Xe đã trả hoặc đơn bị hủy -> Sẵn sàng cho khách khác
                }

                // BƯỚC 3: Cập nhật trạng thái Xe (nếu trạng thái đơn thuộc các diện trên)
                if (!bikeStatus.isEmpty()) {
                    PreparedStatement psBike = connection.prepareStatement(sqlBike);
                    psBike.setString(1, bikeStatus);
                    psBike.setInt(2, orderId);
                    psBike.executeUpdate();
                }

                connection.commit(); // Lưu tất cả thay đổi
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

// Nếu cần cập nhật cả thanh toán (ví dụ khi khách trả máy và trả nốt tiền)
    public boolean updatePaymentStatus(int orderId, String newPaymentStatus) {
        String sql = "UPDATE rent_orders SET payment_status = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newPaymentStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int saveOrder(Order order, List<Stuff> selectedStuffs) {
        // 1. Câu lệnh Insert đơn hàng
        String insertOrder = "INSERT INTO rent_orders (user_id, motorbike_id, price_per_day_at_rent, "
                + "start_date, end_date, total_days, total_bike_cost, total_stuff_cost, "
                + "deposit_amount, status, payment_status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        // 2. Câu lệnh trừ tiền Balance của User
        String updateBalance = "UPDATE users SET balance = balance - ? WHERE userId = ? AND balance >= ?";

        // 3. Câu lệnh ghi lịch sử giao dịch (Transaction log)
        String insertTrans = "INSERT INTO transactions (user_id, order_id, amount, type, created_at) VALUES (?,?,?,?,GETDATE())";

        // 4. Câu lệnh lưu chi tiết phụ kiện
        String insertDetail = "INSERT INTO order_stuff_details (order_id, stuff_id, quantity, "
                + "price_per_day_at_rent, total_price) VALUES (?,?,?,?,?)";

        int orderId = -1;

        try {
            connection.setAutoCommit(false); // Bắt đầu giao dịch

            // BƯỚC 1: TRỪ TIỀN CỌC TRƯỚC
            PreparedStatement psBal = connection.prepareStatement(updateBalance);
            psBal.setDouble(1, order.getDepositAmount());
            psBal.setInt(2, order.getUserId());
            psBal.setDouble(3, order.getDepositAmount()); // Điều kiện balance >= deposit
            int rowUpdated = psBal.executeUpdate();

            if (rowUpdated == 0) {
                throw new SQLException("Không đủ số dư tài khoản hoặc User không tồn tại!");
            }

            // BƯỚC 2: LƯU ĐƠN HÀNG
            PreparedStatement ps = connection.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getBikeId());
            ps.setDouble(3, order.getPricePerDayAtRent());
            // Fix lỗi định dạng Date: Nếu String là "yyyy-MM-dd", SQL Server sẽ tự hiểu
            ps.setString(4, order.getStartDate());
            ps.setString(5, order.getEndDate());
            ps.setInt(6, order.getTotalDays());
            ps.setDouble(7, order.getTotalBikeCost());
            ps.setDouble(8, order.getTotalStuffCost());
            ps.setDouble(9, order.getDepositAmount());
            ps.setString(10, "pending");
            ps.setString(11, "deposit_paid");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // BƯỚC 3: GHI LOG GIAO DỊCH (TRANSACTION)
            PreparedStatement psTrans = connection.prepareStatement(insertTrans);
            psTrans.setInt(1, order.getUserId());
            psTrans.setInt(2, orderId);
            psTrans.setDouble(3, order.getDepositAmount());
            psTrans.setString(4, "payment");
            psTrans.executeUpdate();

            // BƯỚC 4: LƯU CHI TIẾT PHỤ KIỆN (BATCH)
            if (orderId > 0 && selectedStuffs != null && !selectedStuffs.isEmpty()) {
                PreparedStatement psDetail = connection.prepareStatement(insertDetail);
                for (Stuff s : selectedStuffs) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, s.getStuffId());
                    psDetail.setInt(3, 1);
                    psDetail.setDouble(4, s.getBasePricePerDay());
                    psDetail.setDouble(5, s.getBasePricePerDay() * order.getTotalDays());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            connection.commit(); // Thành công tất cả thì mới lưu thật sự
            return orderId;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
            }
            System.err.println("LỖI SQL: " + e.getMessage()); // IN LỖI RA CONSOLE ĐỂ DEBUG
            e.printStackTrace();
            return -1;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
            }
        }
    }

    public List<Order> getUpcomingOrders(int userId, String sort, String search, int index) {
        List<Order> list = new ArrayList<>();
        // Mặc định sort theo ngày bắt đầu
        String orderBy = sort.equals("LATEST") ? "DESC" : "ASC";

        String query = "SELECT o.*, m.name as bikeName FROM rent_orders o "
                + "JOIN motorbikes m ON o.motorbike_id = m.id "
                + "WHERE o.user_id = ? AND o.status NOT IN ('completed', 'cancelled') "
                + "AND m.name LIKE ? "
                + "ORDER BY o.start_date " + orderBy + " "
                + "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, "%" + search + "%");
            ps.setInt(3, (index - 1) * 5);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setBikeName(rs.getString("bikeName"));

                // Lấy ngày tháng (ép kiểu về String hoặc Date tùy class của bạn)
                o.setStartDate(rs.getString("start_date"));
                o.setEndDate(rs.getString("end_date"));

                // CÁC THUỘC TÍNH BỊ THIẾU CẦN BỔ SUNG:
                o.setTotalDays(rs.getInt("total_days")); // Để hiển thị (X days)
                o.setDepositAmount(rs.getDouble("deposit_amount")); // Để tính tiền còn lại

                // Giá trị tổng để JSP hiển thị ${o.totalAmount}
                double bikeCost = rs.getDouble("total_bike_cost");
                double stuffCost = rs.getDouble("total_stuff_cost");
                o.setTotalAmount(bikeCost + stuffCost);

                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status"));

                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String processPayment(int userId, int orderId) {
        String message = "";
        // 1. Sửa lỗi JOIN: Thêm o.user_id = u.userId để tránh lấy sai dữ liệu
        String checkData = "SELECT u.balance, (o.total_bike_cost + o.total_stuff_cost - o.deposit_amount) as remainingAmount "
                + "FROM users u "
                + "JOIN rent_orders o ON u.userId = o.user_id "
                + "WHERE u.userId = ? AND o.id = ?";

        String updateBalance = "UPDATE users SET balance = balance - ? WHERE userId = ?";
        String updateOrder = "UPDATE rent_orders SET payment_status = 'paid', status = 'confirmed' WHERE id = ?";

        // 2. Sửa lỗi SQL: Dùng 4 dấu ? để truyền 'payment' vào tham số thứ 4
        String insertTransaction = "INSERT INTO transactions (user_id, order_id, amount, type, created_at) "
                + "VALUES (?, ?, ?, ?, GETDATE())";

        // Dùng try-with-resources để tự động đóng kết nối/statement
        try {
            connection.setAutoCommit(false);

            double remainingAmount = 0;
            try (PreparedStatement psCheck = connection.prepareStatement(checkData)) {
                psCheck.setInt(1, userId);
                psCheck.setInt(2, orderId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        double currentBalance = rs.getDouble("balance");
                        remainingAmount = rs.getDouble("remainingAmount");
                        if (currentBalance < remainingAmount) {
                            connection.rollback();
                            return "Insufficient balance! Need " + remainingAmount + " VND more.";
                        }
                    } else {
                        connection.rollback();
                        return "Order or User not found!";
                    }
                }
            }

            // 1. Trừ tiền
            try (PreparedStatement psUser = connection.prepareStatement(updateBalance)) {
                psUser.setDouble(1, remainingAmount);
                psUser.setInt(2, userId);
                psUser.executeUpdate();
            }

            // 2. Cập nhật trạng thái đơn hàng
            try (PreparedStatement psOrder = connection.prepareStatement(updateOrder)) {
                psOrder.setInt(1, orderId);
                psOrder.executeUpdate();
            }

            // 3. Lưu giao dịch (4 tham số)
            try (PreparedStatement psTrans = connection.prepareStatement(insertTransaction)) {
                psTrans.setInt(1, userId);
                psTrans.setInt(2, orderId);
                psTrans.setDouble(3, remainingAmount);
                psTrans.setString(4, "payment"); // Index 4 khớp với dấu ? thứ 4
                psTrans.executeUpdate();
            }

            connection.commit();
            message = "success";
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
            }
            message = "Error: " + e.getMessage();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
        return message;
    }

    public int getTotalUpcoming(int userId, String search) {
        String query = "SELECT COUNT(*) FROM rent_orders o "
                + "JOIN motorbikes m ON o.motorbike_id = m.id "
                + "WHERE o.user_id = ? AND o.status NOT IN ('completed', 'cancelled') "
                + "AND m.name LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, "%" + search + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public String processCancelAndRefund(int userId, int orderId) {
        String message = "";
        // Lấy tiền cọc và trạng thái hiện tại
        String checkOrder = "SELECT deposit_amount, status FROM rent_orders WHERE id = ? AND user_id = ?";
        String updateBalance = "UPDATE users SET balance = balance + ? WHERE userId = ?";
        String updateOrder = "UPDATE rent_orders SET status = 'cancelled', payment_status = 'refunded' WHERE id = ?";
        String insertTrans = "INSERT INTO transactions (user_id, order_id, amount, type, created_at) VALUES (?, ?, ?, 'refund', GETDATE())";

        try {
            connection.setAutoCommit(false);

            double deposit = 0;
            String status = "";
            PreparedStatement psCheck = connection.prepareStatement(checkOrder);
            psCheck.setInt(1, orderId);
            psCheck.setInt(2, userId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                deposit = rs.getDouble("deposit_amount");
                status = rs.getString("status");
            } else {
                return "Order not found!";
            }

            // Tính toán số tiền hoàn lại
            double refundAmount = 0;
            if (status.equalsIgnoreCase("pending")) {
                refundAmount = deposit; // Hoàn 100%
            } else if (status.equalsIgnoreCase("confirmed")) {
                refundAmount = deposit * 0.5; // Hoàn 50%
            } else {
                connection.rollback();
                return "Cannot cancel/refund this order status!";
            }

            // 1. Cộng tiền lại cho User (nếu refundAmount > 0)
            if (refundAmount > 0) {
                PreparedStatement psUser = connection.prepareStatement(updateBalance);
                psUser.setDouble(1, refundAmount);
                psUser.setInt(2, userId);
                psUser.executeUpdate();

                // 2. Ghi log giao dịch refund
                PreparedStatement psTrans = connection.prepareStatement(insertTrans);
                psTrans.setInt(1, userId);
                psTrans.setInt(2, orderId);
                psTrans.setDouble(3, refundAmount);
                psTrans.executeUpdate();
            }

            // 3. Cập nhật trạng thái đơn hàng
            PreparedStatement psOrder = connection.prepareStatement(updateOrder);
            psOrder.setInt(1, orderId);
            psOrder.executeUpdate();

            connection.commit();
            message = "Refunded " + refundAmount + " VND successfully!";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            message = "Error: " + e.getMessage();
        }
        return message;
    }

    public List<Order> getAllOrdersAdmin(String status, String search, int index) {
    List<Order> list = new ArrayList<>();
    // Mỗi trang hiển thị 10 đơn hàng
    int pageSize = 10;
    
    // Xây dựng câu SQL động
    StringBuilder sql = new StringBuilder(
        "SELECT r.id, u.userEmail, m.name AS bikeName, " +
        "r.total_bike_cost, r.total_stuff_cost, r.status, r.payment_status " +
        "FROM rent_orders r " +
        "JOIN users u ON r.user_id = u.userId " +
        "JOIN motorbikes m ON r.motorbike_id = m.id WHERE 1=1 "
    );

    if (status != null && !status.isEmpty()) sql.append(" AND r.status = ? ");
    if (search != null && !search.isEmpty()) sql.append(" AND (m.name LIKE ? OR u.userEmail LIKE ?) ");
    
    sql.append(" ORDER BY r.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try {
        PreparedStatement ps = connection.prepareStatement(sql.toString());
        int paramIdx = 1;
        if (status != null && !status.isEmpty()) ps.setString(paramIdx++, status);
        if (search != null && !search.isEmpty()) {
            ps.setString(paramIdx++, "%" + search + "%");
            ps.setString(paramIdx++, "%" + search + "%");
        }
        ps.setInt(paramIdx++, (index - 1) * pageSize);
        ps.setInt(paramIdx++, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setUserEmail(rs.getString("userEmail"));
            o.setBikeName(rs.getString("bikeName"));
            o.setTotalAmount(rs.getDouble("total_bike_cost") + rs.getDouble("total_stuff_cost"));
            o.setStatus(rs.getString("status"));
            o.setPaymentStatus(rs.getString("payment_status"));
            list.add(o);
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return list;
}

// Phương thức đếm tổng số bản ghi để tính số trang
public int getTotalOrders(String status, String search) {
    String sql = "SELECT COUNT(*) FROM rent_orders r JOIN users u ON r.user_id = u.userId " +
                 "JOIN motorbikes m ON r.motorbike_id = m.id WHERE 1=1 ";
    if (status != null && !status.isEmpty()) sql += " AND r.status = '" + status + "' ";
    if (search != null && !search.isEmpty()) sql += " AND (m.name LIKE ? OR u.userEmail LIKE ?) ";
    
    try {
        PreparedStatement ps = connection.prepareStatement(sql);
        if (search != null && !search.isEmpty()) {
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { }
    return 0;
}
}
