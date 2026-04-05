<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Admin - MotoGo</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
        <style>
            /* Custom CSS to override and beautify */
            :root {
                --primary-color: #2ecc71;
                --primary-dark: #27ae60;
                --danger-color: #e74c3c;
                --text-main: #2c3e50;
                --bg-light: #f8f9fa;
                --shadow: 0 4px 15px rgba(0,0,0,0.1);
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f4f7f6;
            }

            .admin-container {
                padding: 30px;
                max-width: 1200px;
                margin: 0 auto;
            }

            h2 {
                color: var(--text-main);
                margin-bottom: 5px;
                font-weight: 700;
            }
            .admin-subtitle {
                color: #7f8c8d;
                margin-bottom: 25px;
            }

            /* Card Container */
            .admin-table-card {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow: var(--shadow);
                border: none;
            }

            /* Top Bar: Search & Add Button */
            .admin-toolbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 25px;
                gap: 15px;
            }

            .search-group {
                display: flex;
                gap: 10px;
                flex: 1;
            }

            .search-group input {
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 8px;
                width: 100%;
                max-width: 300px;
                outline: none;
                transition: border 0.3s;
            }

            .search-group input:focus {
                border-color: var(--primary-color);
            }

            /* Button Styles */
            .btn-custom {
                padding: 10px 20px;
                border-radius: 8px;
                text-decoration: none;
                font-weight: 600;
                transition: all 0.3s;
                border: none;
                cursor: pointer;
                display: inline-flex;
                align-items: center;
            }

            .btn-search {
                background: var(--text-main);
                color: white;
            }
            .btn-add {
                background: var(--primary-color);
                color: white;
            }
            .btn-edit {
                background: #3498db;
                color: white;
                padding: 6px 12px;
                font-size: 13px;
            }
            .btn-delete {
                background: var(--danger-color);
                color: white;
                padding: 6px 12px;
                font-size: 13px;
            }

            /* Add this alongside your other button styles */
            .btn-accessory {
                background: #f39c12;
                color: white;
                padding: 6px 12px;
                font-size: 13px;
            }

            .btn-accessory:hover {
                background: #d35400; /* Darker orange for better feedback */
                color: white;
            }

            .btn-custom:hover {
                opacity: 0.85;
                transform: translateY(-1px);
                color: white;
            }

            /* Table Style */
            .admin-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                margin-bottom: 20px;
            }

            .admin-table thead th {
                background: #f8f9fa;
                color: #34495e;
                font-weight: 600;
                padding: 15px;
                border-bottom: 2px solid #edf2f7;
                text-align: left;
            }

            .admin-table tbody tr {
                transition: background 0.2s;
            }
            .admin-table tbody tr:hover {
                background-color: #f1f9f5;
            }

            .admin-table td {
                padding: 15px;
                border-bottom: 1px solid #edf2f7;
                vertical-align: middle;
                color: #4a5568;
            }

            /* Status Badge */
            .status-badge {
                padding: 5px 12px;
                border-radius: 20px;
                font-size: 12px;
                font-weight: bold;
                text-transform: uppercase;
            }
            .status-badge.Available, .status-badge.available {
                background: #d4edda;
                color: #155724;
            }
            .status-badge.Rented, .status-badge.rented {
                background: #fff3cd;
                color: #856404;
            }

            /* Pagination */
            .pagination-container {
                display: flex;
                justify-content: center;
                margin-top: 25px;
                gap: 8px;
            }

            .btn-page {
                padding: 8px 16px;
                border: 1px solid #dee2e6;
                background: white;
                color: var(--text-main);
                text-decoration: none;
                border-radius: 6px;
                transition: all 0.3s;
            }

            .btn-page:hover {
                background: #f8f9fa;
                border-color: #bbb;
            }
            .btn-page.active {
                background: var(--primary-color);
                color: white;
                border-color: var(--primary-color);
            }
        </style>
    </head>
    <body>

        <%@ include file="../navbar.jsp" %>

        <div class="admin-container">
            <h2>Quản lý xe</h2>
            <p class="admin-subtitle">Danh sách xe máy hiện có trong hệ thống</p>

            <div class="admin-table-card">
                <!-- Toolbar: Gộp Search và Add -->
                <div class="admin-toolbar">
                    <form action="${pageContext.request.contextPath}/AdminBike" method="get" class="search-group">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="txtSearch" placeholder="Tìm tên xe hoặc hãng..." value="${param.txtSearch}">
                        <button type="submit" class="btn-custom btn-search">Tìm kiếm</button>
                    </form>

                    <a href="${pageContext.request.contextPath}/AdminBike?action=add" class="btn-custom btn-add">
                        + Thêm xe mới
                    </a>
                </div>

                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ảnh</th>
                            <th>Tên xe</th>
                            <th>Hãng</th>
                            <th>Phân khối</th>
                            <th>Giá/Ngày</th>
                            <th>Trạng thái</th>
                            <th style="text-align: center;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="bike" items="${bikeList}">
                            <tr>
                                <td>#${bike.id}</td>
                                <td>
                                    <img src="${pageContext.request.contextPath}/${bike.bikeIcon}" 
                                         alt="bike" 
                                         style="width: 70px; height: 45px; object-fit: cover; border-radius: 6px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                                </td>
                                <td><strong>${bike.name}</strong></td>
                                <td>${bike.brand}</td>
                                <td>${bike.engineSize}</td>
                                <td>
                                    <span style="color: #27ae60; font-weight: 600;">
                                        <fmt:formatNumber value="${bike.currentPricePerDay}" type="currency" currencySymbol="₫"/>
                                    </span>
                                </td>
                                <td>
                                    <span class="status-badge ${bike.status}">
                                        ${bike.status}
                                    </span>
                                </td>
                                <td style="text-align: center;">
                                    <a href="AdminBike?action=edit&id=${bike.id}" class="btn-custom btn-edit">Sửa</a>
                                    <a href="accessories?bikeId=${bike.id}" class="btn-custom btn-accessory" style="background:#f39c12">Phụ kiện</a>
                                    <a href="AdminBike?action=delete&id=${bike.id}" 
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa xe này?')" 
                                       class="btn-custom btn-delete">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Pagination -->
                <div class="pagination-container">
                    <c:if test="${currentPage > 1}">
                        <a href="AdminBike?page=${currentPage - 1}&txtSearch=${txtSearch}" class="btn-page">« Trước</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="AdminBike?page=${i}&txtSearch=${txtSearch}" 
                           class="btn-page ${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="AdminBike?page=${currentPage + 1}&txtSearch=${txtSearch}" class="btn-page">Sau »</a>
                    </c:if>
                </div>
            </div>
        </div>

    </body>
</html>
