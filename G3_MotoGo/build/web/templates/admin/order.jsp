<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Quản lý đơn hàng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
        <style>
            /* Tùy chỉnh thêm cho Search & Pagination */
            .admin-header-actions {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                background: #fff;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            }

            .search-form {
                display: flex;
                gap: 10px;
            }
            .search-form input {
                padding: 8px 15px;
                border: 1px solid #ddd;
                border-radius: 4px;
                width: 250px;
            }

            .pagination {
                display: flex;
                justify-content: center;
                gap: 5px;
                margin-top: 25px;
            }
            .pagination a {
                padding: 8px 16px;
                border: 1px solid #dee2e6;
                text-decoration: none;
                color: #007bff;
                background-color: #fff;
                border-radius: 4px;
                transition: all 0.3s;
            }
            .pagination a.active {
                background-color: #007bff;
                color: white;
                border-color: #007bff;
            }
            .pagination a:hover:not(.active) {
                background-color: #f8f9fa;
            }

            /* Màu sắc trạng thái */
            .status {
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 0.85em;
                font-weight: bold;
                text-transform: capitalize;
            }
            .status.pending {
                background: #ffeeba;
                color: #856404;
            }
            .status.confirmed {
                background: #b8daff;
                color: #004085;
            }
            .status.active {
                background: #c3e6cb;
                color: #155724;
            }
            .status.completed {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .status.cancelled {
                background: #f8d7da;
                color: #721c24;
            }

            .btn-action {
                text-decoration: none;
                padding: 6px 12px;
                border-radius: 4px;
                font-size: 13px;
                color: white;
                border: none;
                cursor: pointer;
            }
            .btn-view {
                background-color: #17a2b8;
            }
            .btn-confirm {
                background-color: #007bff;
            }
            .btn-pickup {
                background-color: #28a745;
            }
            .btn-complete {
                background-color: #2c3e50;
            }
            .btn-cancel {
                background-color: #dc3545;
            }
        </style>
    </head>
    <body>

        <%@ include file="../navbar.jsp" %>

        <div class="admin-container" style="max-width: 1200px; margin: 30px auto; padding: 0 20px;">
            <h2>Quản lý đơn hàng</h2>
            <p class="admin-subtitle">Hệ thống xử lý thuê xe Nhom3_PRJ301</p>

            <!-- THANH TÌM KIẾM & BỘ LỌC -->
            <div class="admin-header-actions">
                <form action="AdminOrder" method="get" class="search-form">
                    <!-- Giữ lại trạng thái filter khi search -->
                    <select name="status" style="padding: 8px; border-radius: 4px; border: 1px solid #ddd;">
                        <option value="">-- Tất cả trạng thái --</option>
                        <option value="pending" ${currentStatus == 'pending' ? 'selected' : ''}>Chờ xác nhận</option>
                        <option value="confirmed" ${currentStatus == 'confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                        <option value="active" ${currentStatus == 'active' ? 'selected' : ''}>Đang thuê</option>
                        <option value="completed" ${currentStatus == 'completed' ? 'selected' : ''}>Hoàn tất</option>
                        <option value="cancelled" ${currentStatus == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
                    </select>

                    <input type="text" name="txtSearch" value="${currentSearch}" placeholder="Tìm theo xe hoặc email khách...">
                    <button type="submit" class="btn-action btn-confirm">Tìm kiếm</button>
                    <a href="AdminOrder" class="btn-action btn-view" style="line-height: 20px;">Làm mới</a>
                </form>

                <div class="stats">
                    Tổng số: <strong>${orderList.size()}</strong> đơn hàng trên trang này
                </div>
            </div>

            <div class="admin-table-card" style="background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                <table class="admin-table" style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr style="background: #f8f9fa; border-bottom: 2px solid #dee2e6; text-align: left;">
                            <th style="padding: 15px;">Mã đơn</th>
                            <th style="padding: 15px;">Khách hàng</th>
                            <th style="padding: 15px;">Xe thuê</th>
                            <th style="padding: 15px;">Tổng tiền</th>
                            <th style="padding: 15px;">Trạng thái</th>
                            <th style="padding: 15px;">Hành động</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="order" items="${orderList}">
                            <tr style="border-bottom: 1px solid #eee;">
                                <td style="padding: 15px;">#${order.id}</td>
                                <td style="padding: 15px;">${order.userEmail}</td>
                                <td style="padding: 15px;"><strong>${order.bikeName}</strong></td>
                                <td style="padding: 15px;">
                                    <span style="color: #e67e22; font-weight: bold;">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" />
                                    </span>
                                    <div style="font-size: 11px; color: #999;">TT: ${order.paymentStatus}</div>
                                </td>

                                <td style="padding: 15px;">
                                    <span class="status ${order.status.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${order.status == 'pending'}">Chờ xác nhận</c:when>
                                            <c:when test="${order.status == 'confirmed'}">Đã xác nhận</c:when>
                                            <c:when test="${order.status == 'active'}">Đang thuê</c:when>
                                            <c:when test="${order.status == 'completed'}">Hoàn tất</c:when>
                                            <c:when test="${order.status == 'cancelled'}">Đã hủy</c:when>
                                            <c:otherwise>${order.status}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>

                                <td style="padding: 15px; display: flex; gap: 5px;">
                                    <a href="orderDetail?id=${order.id}" class="btn-action btn-view">Xem</a>

                                    <c:choose>
                                        <c:when test="${order.status == 'pending'}">
                                            <c:if test="${order.paymentStatus == 'deposit_paid' || order.paymentStatus == 'paid'}">
                                                <a href="updateOrder?id=${order.id}&status=confirmed" 
                                                   class="btn-action btn-confirm" onclick="return confirm('Xác nhận đơn hàng này?')">Duyệt</a>
                                            </c:if>
                                            <a href="updateOrder?id=${order.id}&status=cancelled" 
                                               class="btn-action btn-cancel" onclick="return confirm('Hủy đơn hàng này?')">Hủy</a>
                                        </c:when>

                                        <c:when test="${order.status == 'confirmed'}">
                                            <a href="updateOrder?id=${order.id}&status=active" 
                                               class="btn-action btn-pickup">Giao xe</a>
                                        </c:when>

                                        <c:when test="${order.status == 'active'}">
                                            <a href="updateOrder?id=${order.id}&status=completed" 
                                               class="btn-action btn-complete">Nhận xe</a>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty orderList}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 40px; color: #999;">
                                    Không tìm thấy đơn hàng nào phù hợp.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- PHÂN TRANG (PAGINATION) -->
            <c:if test="${endP > 1}">
                <div class="pagination">
                    <%-- Nút Previous --%>
                    <c:if test="${tag > 1}">
                        <a href="AdminOrder?page=${tag-1}&status=${currentStatus}&txtSearch=${currentSearch}">&laquo;</a>
                    </c:if>

                    <%-- Các con số trang --%>
                    <c:forEach begin="1" end="${endP}" var="i">
                        <a href="AdminOrder?page=${i}&status=${currentStatus}&txtSearch=${currentSearch}" 
                           class="${tag == i ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <%-- Nút Next --%>
                    <c:if test="${tag < endP}">
                        <a href="AdminOrder?page=${tag+1}&status=${currentStatus}&txtSearch=${currentSearch}">&raquo;</a>
                    </c:if>
                </div>
            </c:if>
        </div>

    </body>
</html>
