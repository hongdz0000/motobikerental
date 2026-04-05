<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Quản lý đồ phượt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
    <style>
        .search-container { margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; }
        .search-form input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; width: 250px; }
        .pagination { display: flex; justify-content: center; gap: 5px; margin-top: 20px; }
        .pagination a { padding: 8px 12px; border: 1px solid #ddd; text-decoration: none; color: #333; border-radius: 4px; }
        .pagination a.active { background-color: #28a745; color: white; border-color: #28a745; }
        .pagination a:hover:not(.active) { background-color: #f1f1f1; }
    </style>
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="admin-container">
    <h2>QUẢN LÝ ĐỒ PHƯỢT</h2>
    <p class="admin-subtitle">Danh sách các phụ kiện đi kèm</p>

    <div class="admin-table-card">
        <div class="search-container">
            <!-- Nút thêm mới -->
            <a href="stuff?action=add" class="btn-success">+ Add Item</a>

            <!-- Form Tìm kiếm -->
            <form action="stuff" method="get" class="search-form">
                <input type="hidden" name="action" value="list">
                <input type="text" name="txt" value="${txt}" placeholder="Nhập tên đồ phượt...">
                <button type="submit" class="btn-success">Tìm kiếm</button>
            </form>
        </div>

        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Icon</th>
                    <th>Name</th>
                    <th>Price/Day</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty stuffList}">
                    <tr><td colspan="5" style="text-align:center">Không tìm thấy kết quả nào.</td></tr>
                </c:if>
                <c:forEach var="item" items="${stuffList}">
                    <tr>
                        <td>${item.stuffId}</td>
                        <td><img src="${item.stuffIcon}" width="40" height="40" style="object-fit: cover;"></td>
                        <td>${item.stuffName}</td>
                        <td>${item.basePricePerDay} VNĐ</td>
                        <td>
                            <a href="stuff?action=edit&id=${item.stuffId}" class="btn-success">Edit</a>
                            <a href="stuff?action=delete&id=${item.stuffId}" 
                               class="btn-danger" 
                               onclick="return confirm('Bạn có chắc muốn xóa?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Thanh Phân Trang -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="stuff?action=list&page=${i}&txt=${txt}" 
                       class="${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>