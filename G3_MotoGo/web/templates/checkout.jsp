<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Xác nhận đặt xe</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/checkout.css">
        <style>
            /* Style cho thông báo lỗi */
            .error-alert {
                background-color: #fee2e2;
                border: 1px solid #f87171;
                color: #dc2626;
                padding: 12px;
                border-radius: 8px;
                margin-bottom: 20px;
                font-size: 14px;
                display: flex;
                align-items: center;
            }
        </style>
    </head>
    <body>
        <jsp:include page="navbar.jsp" />
        <div class="checkout-container">
            <!-- Cột trái: Chi tiết -->
            <section class="checkout-info">
                <h2>Xác nhận đặt xe</h2>

                <!-- HIỂN THỊ LỖI NẾU CÓ (Lấy từ request.setAttribute("error", ...)) -->
                <c:if test="${not empty error}">
                    <div class="error-alert">
                        <span style="margin-right: 8px;">⚠️</span> ${error}
                    </div>
                </c:if>

                <div class="user-card">
                    <p><strong>Người đặt:</strong> <span>${sessionScope.user.userName}</span></p>
                    <p><strong>Email:</strong> <span>${sessionScope.user.userEmail}</span></p>
                    <p><strong>Số điện thoại:</strong> <span>${sessionScope.user.phone}</span></p>
                </div>

                <h3>Phụ kiện đi kèm</h3>
                <ul class="stuff-list">
                    <c:forEach items="${selectedStuffs}" var="s">
                        <li>
                            <span style="color: #10b981;">✔</span> ${s.stuffName} 
                            <span style="float: right; font-weight: 600;">
                                +<fmt:formatNumber value="${s.basePricePerDay}" pattern="#,###" /> đ/ngày
                            </span>
                        </li>
                    </c:forEach>
                    <c:if test="${empty selectedStuffs}">
                        <li style="color: #94a3b8; border: none;">Không có phụ kiện thêm</li>
                    </c:if>
                </ul>
            </section>

            <!-- Cột phải: Thanh toán -->
            <aside class="summary-card">
                <h3 style="border: none; padding: 0; margin-bottom: 20px;">Tóm tắt đơn hàng</h3>

                <div class="summary-row">
                    <span>Tên xe</span>
                    <strong>${pendingOrder.bikeName}</strong>
                </div>
                <div class="summary-row">
                    <span>Thời gian</span>
                    <span style="font-size: 13px;">${pendingOrder.startDate} - ${pendingOrder.endDate}</span>
                </div>
                <div class="summary-row">
                    <span>Thời gian thuê</span>
                    <span>${pendingOrder.totalDays} ngày</span>
                </div>

                <div class="summary-row">
                    <span>Tiền thuê xe</span>
                    <span><fmt:formatNumber value="${pendingOrder.totalBikeCost}" pattern="#,###" /> đ</span>
                </div>
                <div class="summary-row">
                    <span>Tiền phụ kiện</span>
                    <span><fmt:formatNumber value="${pendingOrder.totalStuffCost}" pattern="#,###" /> đ</span>
                </div>

                <div class="summary-row total">
                    <span>Tổng cộng</span>
                    <span><fmt:formatNumber value="${pendingOrder.totalAmount}" pattern="#,###" /> đ</span>
                </div>

                <div class="summary-row deposit">
                    <span>Tiền cọc (15%)</span>
                    <strong><fmt:formatNumber value="${pendingOrder.depositAmount}" pattern="#,###" /> đ</strong>
                </div>

                <form action="ConfirmBooking" method="POST">
                    <!-- Kiểm tra số dư ở phía Client một chút (Tùy chọn) -->
                    <button type="submit" class="confirm-btn" 
                            <c:if test="${sessionScope.user.balance < pendingOrder.depositAmount}">disabled style="background: #cbd5e1; cursor: not-allowed;"</c:if>>
                        XÁC NHẬN THANH TOÁN
                    </button>
                </form>
                
                <c:if test="${sessionScope.user.balance < pendingOrder.depositAmount}">
                    <p style="color: #dc2626; font-size: 12px; text-align: center; margin-top: 5px;">
                        Số dư không đủ (${sessionScope.user.balance} đ)
                    </p>
                </c:if>

                <p style="font-size: 12px; color: #94a3b8; text-align: center; margin-top: 15px;">
                    * Bằng cách nhấn xác nhận, bạn đồng ý với điều khoản thuê xe của chúng tôi.
                </p>
            </aside>
        </div>
    </body>
</html>