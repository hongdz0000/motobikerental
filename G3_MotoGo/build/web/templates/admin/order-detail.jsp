<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin - Chi tiết đơn hàng #${order.id}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
        <!-- Thêm Font Awesome để dùng Icon nếu bạn có link, nếu không icon sẽ hiện dạng text đơn giản -->
        <style>
            .info-section h4 {
                border-bottom: 2px solid #eee;
                padding-bottom: 10px;
                color: #2c3e50;
            }
            .info-section p {
                margin: 12px 0;
            }

            /* 1. Bổ sung đầy đủ CSS cho các trạng thái */
            .badge {
                padding: 8px 15px;
                border-radius: 20px;
                font-weight: bold;
                font-size: 12px;
                display: inline-block;
            }
            .status-pending {
                background: #ffeeba;
                color: #856404;
            }
            .status-confirmed {
                background: #b8daff;
                color: #004085;
            }
            .status-active {
                background: #c3e6cb;
                color: #155724;
            }
            .status-completed {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .status-cancelled {
                background: #f8d7da;
                color: #721c24;
            }

            .payment-tag {
                font-weight: bold;
                color: #495057;
                background: #e9ecef;
                padding: 2px 8px;
                border-radius: 4px;
            }
            .btn-action {
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                color: white;
                transition: 0.3s;
            }
            .btn-confirm {
                background: #007bff;
            }
            .btn-pickup {
                background: #28a745;
            }
            .btn-complete {
                background: #2c3e50;
            }
            .btn-cancel {
                background: #dc3545;
            }
            .btn-action:hover {
                opacity: 0.8;
            }
        </style>
    </head>
    <body>

        <%@ include file="../navbar.jsp" %>

        <div class="admin-container">
            <div class="admin-table-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;">
                    <h2 style="margin:0;">📦 Đơn hàng #${order.id}</h2>
                    <span class="badge status-${order.status.toLowerCase()}">
                        <c:choose>
                            <c:when test="${order.status == 'pending'}">Chờ xác nhận</c:when>
                            <c:when test="${order.status == 'confirmed'}">Đã xác nhận</c:when>
                            <c:when test="${order.status == 'active'}">Đang thuê</c:when>
                            <c:when test="${order.status == 'completed'}">Hoàn tất</c:when>
                            <c:when test="${order.status == 'cancelled'}">Đã hủy</c:when>
                            <c:otherwise>${order.status}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 40px;">
                    <!-- 👤 Khối Khách hàng -->
                    <div class="info-section">
                        <h4>👤 Thông tin khách hàng</h4>
                        <p><strong>Họ tên:</strong> ${order.userName}</p>
                        <p><strong>Email:</strong> ${order.userEmail}</p>
                        <p><strong>Số điện thoại:</strong> ${order.phone}</p>
                    </div>

                    <!-- 🏍 Khối Xe & Thời gian -->
                    <div class="info-section">
                        <h4>🏍 Thông tin thuê xe</h4>
                        <p><strong>Xe thuê:</strong> <span style="color: #e67e22; font-weight: bold;">${order.bikeName}</span></p>
                        <p><strong>Đơn giá xe:</strong> <fmt:formatNumber value="${order.pricePerDayAtRent}" type="currency" currencySymbol="₫"/></p> 
                        <p><strong>Thời gian:</strong> ${order.startDate} ➜ ${order.endDate}</p>
                        <p><strong>Tổng cộng:</strong> ${order.totalDays} ngày</p>
                    </div>
                </div>

                <!-- 🎒 Khối Phụ kiện -->
                <h4 style="margin-top: 30px;">🎒 Phụ kiện đi kèm</h4>
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>Tên phụ kiện</th>
                            <th>Giá/Ngày</th>
                            <th>Số lượng</th>
                            <th>Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="gear" items="${gearList}">
                            <tr>
                                <td>${gear.name}</td>
                                <td><fmt:formatNumber value="${gear.pricePerDay}" type="currency" currencySymbol="₫"/></td>
                                <td>x ${gear.quantity}</td>
                                <td><fmt:formatNumber value="${gear.totalPrice}" type="currency" currencySymbol="₫"/></td>
                            </tr>
                        </c:forEach>
                        <%-- 3. Xử lý khi gearList rỗng --%>
                        <c:if test="${empty gearList}">
                            <tr>
                                <td colspan="4" style="text-align: center; color: #888; padding: 20px;">
                                    Khách hàng không thuê phụ kiện kèm theo.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- 💰 Tổng kết tài chính -->
                <div style="margin-top: 20px; padding: 25px; background: #f8f9fa; border-radius: 8px; text-align: right;">
                    <p>Tiền thuê xe: <strong><fmt:formatNumber value="${order.totalBikeCost}" type="currency" currencySymbol="₫"/></strong></p>
                    <p>Tiền phụ kiện: <strong><fmt:formatNumber value="${order.totalStuffCost}" type="currency" currencySymbol="₫"/></strong></p>
                    <p style="color: #dc3545;">Tiền đặt cọc (Security Deposit): <strong><fmt:formatNumber value="${order.depositAmount}" type="currency" currencySymbol="₫"/></strong></p>

                    <%-- 2. Map Payment Status đẹp hơn --%>
                    <p>Trạng thái thanh toán: 
                        <span class="payment-tag">
                            <c:choose>
                                <c:when test="${order.paymentStatus == 'unpaid'}">Chưa thanh toán</c:when>
                                <c:when test="${order.paymentStatus == 'deposit_paid'}">Đã đặt cọc</c:when>
                                <c:when test="${order.paymentStatus == 'paid'}">Đã thanh toán đủ</c:when>
                                <c:when test="${order.paymentStatus == 'refunded'}">Đã hoàn tiền</c:when>
                                <c:otherwise>${order.paymentStatus}</c:otherwise>
                            </c:choose>
                        </span>
                    </p>
                    <hr style="border: 0; border-top: 1px solid #ddd; margin: 15px 0;">
                    <h3 style="margin:0;">Tổng giá trị: <span style="color: #28a745;"><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></span></h3>
                </div>

                <!-- 4. Logic Actions đầy đủ flow -->
                <div style="margin-top: 30px; display: flex; gap: 15px; justify-content: flex-end; align-items: center;">

                    <c:choose>
                        <%-- TRƯỜNG HỢP LÀ ADMIN: Hiện các nút điều phối đơn hàng --%>
                        <c:when test="${sessionScope.user.role == 'admin'}">
                            <c:choose>
                                <c:when test="${order.status == 'pending'}">
                                    <c:if test="${order.paymentStatus == 'deposit_paid' || order.paymentStatus == 'paid'}">
                                        <button onclick="updateStatus(${order.id}, 'confirmed')" class="btn-action btn-confirm">Xác nhận đơn</button>
                                    </c:if>
                                    <button onclick="updateStatus(${order.id}, 'cancelled')" class="btn-action btn-cancel">Hủy đơn</button>
                                </c:when>

                                <c:when test="${order.status == 'confirmed'}">
                                    <button onclick="updateStatus(${order.id}, 'active')" class="btn-action btn-pickup">Giao xe cho khách</button>
                                    <button onclick="updateStatus(${order.id}, 'cancelled')" class="btn-action btn-cancel">Hủy đơn</button>
                                </c:when>

                                <c:when test="${order.status == 'active'}">
                                    <button onclick="updateStatus(${order.id}, 'completed')" class="btn-action btn-complete">Nhận xe & Hoàn tất</button>
                                </c:when>
                            </c:choose>
                        </c:when>

                        <%-- TRƯỜNG HỢP LÀ USER: Dùng logic giống trang Upcoming --%>
                        <c:otherwise>
                            <c:choose>
                                <%-- Nút Pay & Pick up --%>
                                <c:when test="${order.paymentStatus == 'deposit_paid' && order.status == 'confirmed'}">
                                    <a href="UpcomingRental?action=pay&id=${order.id}" 
                                       onclick="return confirm('Pay the remaining balance to start your trip?')" 
                                       style="background: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                        PAY & PICK UP
                                    </a>
                                </c:when>

                                <%-- Trạng thái chờ Admin duyệt --%>
                                <c:when test="${order.status == 'pending'}">
                                    <span style="background: #f0ad4e; color: white; padding: 10px 20px; border-radius: 5px; font-weight: bold;">
                                        WAITING FOR ADMIN...
                                    </span>
                                </c:when>
                            </c:choose>

                            <%-- Nút Hủy đơn cho User (Nếu đơn còn mới) --%>
                            <c:if test="${order.status == 'pending' || order.status == 'confirmed'}">
                                <a href="UpcomingRental?action=cancel&id=${order.id}" 
                                   onclick="return confirm('Refund Policy:\n- Pending: 100% refund\n- Confirmed: 50% refund\nProceed with cancellation?')" 
                                   style="color: #666; text-decoration: underline; font-size: 0.9em;">
                                    Cancel Booking
                                </a>
                            </c:if>
                        </c:otherwise>
                    </c:choose>

                    <%-- Nút quay lại linh hoạt dựa trên role --%>
                    <a href="${sessionScope.user.role == 'admin' ? 'AdminOrder' : 'UpcomingRental'}" 
                       style="text-decoration:none; background:#6c757d; color:white; padding:10px 20px; border-radius:5px; font-weight:bold;">
                        Quay lại
                    </a>
                </div>
            </div>
        </div>

        <script>
            function updateStatus(orderId, status) {
                let msg = "";
                if (status === 'confirmed')
                    msg = "Xác nhận đơn hàng này?";
                if (status === 'cancelled')
                    msg = "Bạn có chắc chắn muốn HỦY đơn hàng?";
                if (status === 'active')
                    msg = "Xác nhận đã GIAO XE cho khách?";
                if (status === 'completed')
                    msg = "Xác nhận khách đã TRẢ XE và kết thúc đơn hàng?";

                if (confirm(msg)) {
                    window.location.href = "updateOrder?id=" + orderId + "&status=" + status;
                }
            }
        </script>
    </body>
</html>
