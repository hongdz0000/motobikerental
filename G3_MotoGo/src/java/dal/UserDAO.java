/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 *
 * @author MSI LAPTOP
 */
public class UserDAO extends DBContext {

    PreparedStatement st;
    ResultSet rs;

    public User getAccountByEmail(String email) {
        String sql = "SELECT userId, userName, userEmail, phone, avatar, userPassword, role, balance, status "
                + "FROM users "
                + "WHERE userEmail = ? AND status = 'active'";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, email);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("userId");
                    String name = rs.getString("userName");
                    String mail = rs.getString("userEmail");
                    String pass = rs.getString("userPassword");
                    String role = rs.getString("role");
                    BigDecimal bal = rs.getBigDecimal("balance");
                    String stat = rs.getString("status");
                    String phone = rs.getString("phone");
                    String avatar = rs.getString("avatar");
                    return new User(id, name, mail, pass, role, bal, stat, phone, avatar);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn User: " + e.getMessage());
        }

        return null;
    }

    public User getAccountByUserId(String userId) {
        // 1. Sửa SQL: WHERE userId = ?
        String sql = "SELECT userId, userName, userEmail, phone, avatar, userPassword, role, balance, status "
                + "FROM users "
                + "WHERE userId = ? ";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            // 2. Ép kiểu userId sang int nếu DB của bạn để kiểu INT
            st.setInt(1, Integer.parseInt(userId));

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    // 3. Lấy dữ liệu từ ResultSet
                    int id = rs.getInt("userId");
                    String name = rs.getString("userName");
                    String mail = rs.getString("userEmail");
                    String pass = rs.getString("userPassword");
                    String role = rs.getString("role");
                    BigDecimal bal = rs.getBigDecimal("balance");
                    String stat = rs.getString("status");
                    String phone = rs.getString("phone");
                    String avatar = rs.getString("avatar");
                    return new User(id, name, mail, pass, role, bal, stat, phone, avatar);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn User theo ID: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ID không hợp lệ: " + userId);
        }

        return null;
    }

    public boolean updateUserProfile(String userId, String newName, String newPhone) {
        String sql = "UPDATE users SET userName = ?, phone = ? WHERE userId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, newName);
            st.setString(2, newPhone);
            st.setInt(3, Integer.parseInt(userId));

            return st.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Lỗi Update: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAvatar(String userId, String avatarRelativePath) {
// Giả sử tên cột trong Database của bạn là 'avatar'
        String sql = "UPDATE users SET avatar = ? WHERE userId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            // Tham số 1: Đường dẫn ảnh (ví dụ: uploads/avatar_123.jpg)
            st.setString(1, avatarRelativePath);

            // Tham số 2: ID người dùng (ép kiểu sang int giống hàm trên của bạn)
            st.setInt(2, Integer.parseInt(userId));

            // Thực thi lệnh và trả về true nếu có ít nhất 1 dòng được cập nhật
            return st.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Lỗi Update Avatar: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<User> getListUser() {
        ArrayList<User> list = new ArrayList<>();
        // 1. Sửa SQL: Bỏ điều kiện WHERE để lấy tất cả
        String sql = "SELECT userId, userName, userEmail, phone, avatar, userPassword, role, balance, status "
                + "FROM users";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            // 2. Sử dụng vòng lặp while để duyệt hết các bản ghi
            while (rs.next()) {
                int id = rs.getInt("userId");
                String name = rs.getString("userName");
                String mail = rs.getString("userEmail");
                String pass = rs.getString("userPassword");
                String role = rs.getString("role");
                BigDecimal bal = rs.getBigDecimal("balance");
                String stat = rs.getString("status");
                String phone = rs.getString("phone");
                String avatar = rs.getString("avatar");

                // 3. Tạo đối tượng User và thêm vào danh sách
                User user = new User(id, name, mail, pass, role, bal, stat, phone, avatar);
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn danh sách User: " + e.getMessage());
        }

        return list;
    }

    public boolean softDeleteUser(String id) {
        String sql = "UPDATE users SET status = 'inactive' WHERE userId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, Integer.parseInt(id));
            return st.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Lỗi khi xóa mềm User: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả người dùng, có thể sắp xếp theo ID mới nhất lên đầu
        String sql = "SELECT * FROM users where status = 'active' ORDER BY userId DESC";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                // Khởi tạo đối tượng User từ dữ liệu trong Database
                User u = new User();
                u.setUserId(rs.getInt("userId"));
                u.setUserName(rs.getString("userName"));
                u.setUserEmail(rs.getString("userEmail"));
                u.setUserPassword(rs.getString("userPassword"));
                u.setPhone(rs.getString("phone"));
                u.setAvatar(rs.getString("avatar"));
                u.setRole(rs.getString("role"));
                u.setBalance(rs.getBigDecimal("balance"));
                u.setStatus(rs.getString("status"));

                // Thêm vào danh sách
                list.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách User: " + e.getMessage());
        }
        return list;
    }

    public boolean insertUser(User u) {
        String sql = "INSERT INTO users (userName, userEmail, userPassword, phone, role, balance, status) "
                + "VALUES (?, ?, ?, ?, ?, 0, 'active')";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, u.getUserName());
            st.setString(2, u.getUserEmail());
            st.setString(3, u.getUserPassword()); // Lưu ý: Nên mã hóa mật khẩu nếu làm thực tế
            st.setString(4, u.getPhone());
            st.setString(5, u.getRole());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi insertUser: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User u) {
        String sql = "UPDATE users SET userName = ?, phone = ?, role = ? WHERE userId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, u.getUserName());
            st.setString(2, u.getPhone());
            st.setString(3, u.getRole());
            st.setInt(4, u.getUserId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi updateUser: " + e.getMessage());
            return false;
        }
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET userPassword = ? WHERE userId = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, newPassword);
            st.setInt(2, userId);

            return st.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, role);
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

    public boolean activateUser(String id) {
        String sql = "UPDATE users SET status = 'active' WHERE userId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi activateUser: " + e.getMessage());
            return false;
        }

    }

    public List<User> getInactiveUsers() {
        List<User> list = new ArrayList<>();
        // Câu lệnh SQL chỉ lấy những user có status là inactive
        String sql = "SELECT userId, userName, userEmail, phone, avatar, userPassword, role, balance, status "
                + "FROM users WHERE status = 'inactive' ORDER BY userId DESC";
        try {
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("userId"));
                u.setUserName(rs.getString("userName"));
                u.setUserEmail(rs.getString("userEmail"));
                u.setPhone(rs.getString("phone"));
                u.setAvatar(rs.getString("avatar"));
                u.setUserPassword(rs.getString("userPassword"));
                u.setRole(rs.getString("role"));
                u.setBalance(rs.getBigDecimal("balance"));
                u.setStatus(rs.getString("status"));
                list.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getInactiveUsers: " + e.getMessage());
        }
        return list;
    }
}
