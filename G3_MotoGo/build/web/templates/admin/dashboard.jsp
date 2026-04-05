<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - MotoGo</title>
    <!-- Đảm bảo đường dẫn CSS đúng với cấu trúc project -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="admin-container">
    <h2>MotoGo Control Panel</h2>
    <p class="admin-subtitle">Hệ thống quản lý dịch vụ cho thuê xe</p>

    <div class="admin-table-card">
        <h2>Dashboard Overview</h2>

        <div class="admin-stats">
            <!-- 1. Tổng người dùng -->
            <a href="${pageContext.request.contextPath}/AdminUser" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Users</h3>
                    <div class="stat-number">${totalUsers}</div>
                </div>
            </a>

            <!-- 2. Tổng đơn hàng -->
            <a href="${pageContext.request.contextPath}/AdminOrder" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Orders</h3>
                    <div class="stat-number">${totalOrders}</div>
                </div>
            </a>

            <!-- 3. Doanh thu -->
            <a href="${pageContext.request.contextPath}/AdminTransaction" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Revenue</h3>
                    <div class="stat-number">$${totalRevenue}</div>
                </div>
            </a>

            <!-- 4. Đơn hàng chờ xử lý (Thêm màu cảnh báo nếu có đơn) -->
            <a href="${pageContext.request.contextPath}/AdminOrder?status=pending" class="admin-card-link">
                <div class="admin-card">
                    <h3>Pending Orders</h3>
                    <div class="stat-number warning">${pendingOrders}</div>
                </div>
            </a>

            <!-- 5. Tổng số xe -->
            <a href="${pageContext.request.contextPath}/AdminBike" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Bikes</h3>
                    <div class="stat-number">${totalBikes}</div>
                </div>
            </a>

            <!-- 6. Tổng số Đồ phượt (Gear/Stuff) -->
            <a href="${pageContext.request.contextPath}/stuff" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Stuff (Gear)</h3>
                    <div class="stat-number">${totalStuff}</div>
                </div>
            </a>

            <!-- 7. Tổng số nhân viên (Nếu bạn muốn hiển thị thêm) -->
            <a href="${pageContext.request.contextPath}/AdminUser" class="admin-card-link">
                <div class="admin-card">
                    <h3>Total Staff</h3>
                    <div class="stat-number">${totalStaff}</div>
                </div>
            </a>
        </div>
    </div>
</div>

</body>
</html>
