package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Transaction;
import java.sql.SQLException;
import java.util.List;

public class TransactionDAO extends DBContext {

    PreparedStatement st;
    ResultSet rs;

    public ArrayList<Transaction> getTransactionsByUser(int userId) {

        ArrayList<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY created_at DESC";

        try {

            st = connection.prepareStatement(sql);
            st.setInt(1, userId);

            rs = st.executeQuery();

            while (rs.next()) {

                Transaction t = new Transaction();

                t.setId(rs.getInt("id"));
                t.setUserId(rs.getInt("user_id"));
                t.setOrderId(rs.getInt("order_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setType(rs.getString("type"));
                t.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public double getWalletBalance(int userId) {
        double balance = 0;
        // Tương tự, tính toán số dư thực tế dựa trên type
        String sql = "SELECT "
                + "  ISNULL(SUM(CASE WHEN type IN ('deposit', 'refund') THEN amount ELSE 0 END), 0) - "
                + "  ISNULL(SUM(CASE WHEN type = 'payment' THEN amount ELSE 0 END), 0) "
                + "FROM transactions WHERE user_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public ArrayList<Transaction> getTransactionsByUserPaging(int userId, int page, int pageSize) {

        ArrayList<Transaction> list = new ArrayList<>();

        int offset = (page - 1) * pageSize;

        String sql = "SELECT * FROM transactions "
                + "WHERE user_id = ? "
                + "ORDER BY created_at DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {

            st = connection.prepareStatement(sql);

            st.setInt(1, userId);
            st.setInt(2, offset);
            st.setInt(3, pageSize);

            rs = st.executeQuery();

            while (rs.next()) {

                Transaction t = new Transaction();

                t.setId(rs.getInt("id"));
                t.setUserId(rs.getInt("user_id"));
                t.setOrderId(rs.getInt("order_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setType(rs.getString("type"));
                t.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countTransactionsByUser(int userId) {

        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ?";

        try {

            st = connection.prepareStatement(sql);
            st.setInt(1, userId);

            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void syncUserBalance(int userId) {
        // Logic: Tổng (Deposit + Refund) - Tổng (Payment)
        String sql = "UPDATE users SET balance = ("
                + "  SELECT "
                + "    ISNULL(SUM(CASE WHEN type IN ('deposit', 'refund') THEN amount ELSE 0 END), 0) - "
                + "    ISNULL(SUM(CASE WHEN type = 'payment' THEN amount ELSE 0 END), 0) "
                + "  FROM transactions WHERE user_id = ?"
                + ") WHERE userId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setInt(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void depositMoney(int userId, double amount) {
        // Đảm bảo nạp tiền luôn là số dương
        String sql = "INSERT INTO transactions (user_id, order_id, amount, type, created_at) "
                + "VALUES (?, NULL, ?, 'deposit', GETDATE())";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            st.setDouble(2, Math.abs(amount)); // Luôn lấy giá trị tuyệt đối
            st.executeUpdate();

            // Sau khi nạp, đồng bộ ngay vào bảng Users
            syncUserBalance(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// 1. Lấy danh sách giao dịch có phân trang
    public List<Transaction> getFilteredTransactions(String email, String type, String fromDate, String toDate,
            String sortBy, int page, int pageSize) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT t.*, u.userEmail FROM transactions t JOIN users u ON t.user_id = u.userId WHERE 1=1 ";

        if (email != null && !email.isEmpty()) {
            sql += " AND u.userEmail LIKE ? ";
        }
        if (type != null && !type.isEmpty()) {
            sql += " AND t.type = ? ";
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql += " AND CAST(t.created_at AS DATE) >= ? ";
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql += " AND CAST(t.created_at AS DATE) <= ? ";
        }

        if ("date_asc".equals(sortBy)) {
            sql += " ORDER BY t.created_at ASC ";
        } else if ("amount_desc".equals(sortBy)) {
            sql += " ORDER BY t.amount DESC ";
        } else if ("amount_asc".equals(sortBy)) {
            sql += " ORDER BY t.amount ASC ";
        } else {
            sql += " ORDER BY t.created_at DESC ";
        }

        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int idx = 1;
            if (email != null && !email.isEmpty()) {
                st.setString(idx++, "%" + email + "%");
            }
            if (type != null && !type.isEmpty()) {
                st.setString(idx++, type);
            }
            if (fromDate != null && !fromDate.isEmpty()) {
                st.setString(idx++, fromDate);
            }
            if (toDate != null && !toDate.isEmpty()) {
                st.setString(idx++, toDate);
            }
            st.setInt(idx++, (page - 1) * pageSize);
            st.setInt(idx++, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Transaction tx = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setAmount(rs.getDouble("amount"));
                tx.setType(rs.getString("type"));
                tx.setCreatedAt(rs.getTimestamp("created_at"));
                tx.setUserEmail(rs.getString("userEmail"));
                list.add(tx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Đếm tổng số bản ghi (Phục vụ phân trang)
    public int countFilteredTransactions(String email, String type, String fromDate, String toDate) {
        String sql = "SELECT COUNT(*) FROM transactions t JOIN users u ON t.user_id = u.userId WHERE 1=1 ";
        if (email != null && !email.isEmpty()) {
            sql += " AND u.userEmail LIKE ? ";
        }
        if (type != null && !type.isEmpty()) {
            sql += " AND t.type = ? ";
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql += " AND CAST(t.created_at AS DATE) >= ? ";
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql += " AND CAST(t.created_at AS DATE) <= ? ";
        }

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            int idx = 1;
            if (email != null && !email.isEmpty()) {
                st.setString(idx++, "%" + email + "%");
            }
            if (type != null && !type.isEmpty()) {
                st.setString(idx++, type);
            }
            if (fromDate != null && !fromDate.isEmpty()) {
                st.setString(idx++, fromDate);
            }
            if (toDate != null && !toDate.isEmpty()) {
                st.setString(idx++, toDate);
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
