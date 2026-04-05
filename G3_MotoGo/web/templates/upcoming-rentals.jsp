<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Upcoming Rentals | MotoGo</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/upcoming.css">
    </head>
    <body>
   <body>

        <jsp:include page="navbar.jsp" />

        <header class="page-header">
            <h1>Your upcoming bookings</h1>
            <a href="past-rentals" class="past-link">VIEW PAST BOOKINGS</a>
        </header>

        <section class="rentals-section">
            <!-- Messages -->
            <c:if test="${not empty msg}"><p class="alert-msg" style="color: green; font-weight: bold;">${msg}</p></c:if>
            <c:if test="${not empty error}"><p class="alert-error" style="color: red; font-weight: bold;">${error}</p></c:if>

            <div class="rentals-top">
                <p class="rental-count">
                    You have <strong>${total}</strong> upcoming rentals
                </p>

                <form action="UpcomingRental" method="get" style="display: flex; gap: 10px;">
                    <input type="text" name="search" value="${search}" placeholder="Search bike name...">
                    <div class="sort-box">
                        <label>SORT BY</label>
                        <select name="sort" onchange="this.form.submit()">
                            <option value="SOONEST" ${sort == 'SOONEST' ? 'selected':''}>SOONEST</option>
                            <option value="LATEST" ${sort == 'LATEST' ? 'selected':''}>LATEST</option>
                        </select>
                    </div>
                    <button type="submit" style="display:none"></button>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-state">
                        <h3>No upcoming bookings yet</h3>
                        <p>Browse motorcycles and start your next adventure.</p>
                        <a href="home" class="browse-btn">Browse Bikes</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${orders}" var="o">
                        <div class="rental-card" style="border: 1px solid #ddd; padding: 20px; margin-bottom: 15px; border-radius: 10px; background: #fff;">
                            <div style="display: flex; justify-content: space-between;">
                                <div style="flex: 1;">
                                    <h3 style="margin-top: 0;">${o.bikeName}</h3>
                                    <p><strong>Period:</strong> ${o.startDate} to ${o.endDate} (${o.totalDays} days)</p>
                                    <p><strong>Total Price:</strong> <fmt:formatNumber value="${o.totalAmount}" type="number"/> VND</p>
                                    <p style="color: #28a745;"><strong>Deposit Paid (15%):</strong> - <fmt:formatNumber value="${o.depositAmount}" type="number"/> VND</p>

                                    <hr style="border: 0; border-top: 1px dashed #eee; margin: 10px 0;">

                                    <p style="font-size: 1.1em; color: #d9534f;">
                                        <strong>Remaining to pay:</strong> 
                                        <strong>
                                            <c:choose>
                                                <c:when test="${o.paymentStatus == 'paid'}">
                                                    0 VND
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${o.totalAmount - o.depositAmount}" type="number"/> VND
                                                </c:otherwise>
                                            </c:choose>
                                        </strong>
                                    </p>

                                    <p>
                                        <strong>Status:</strong> 
                                        <span class="status-badge ${o.status}" style="font-weight: bold; text-transform: uppercase;">${o.status}</span> 


                                        | <strong>Payment:</strong> 
                                        <span style="font-weight: bold; color: ${o.paymentStatus == 'paid' ? '#28a745' : '#f0ad4e'};">
                                            ${o.paymentStatus}
                                        </span>
                                    </p>
                                </div>

                                <div class="order-actions" style="display: flex; flex-direction: column; gap: 10px; min-width: 180px;">

                                    <!-- VIEW DETAILS -->
                                    <a href="orderDetail?id=${o.id}" 
                                       style="background: #007bff; color: white; padding: 10px; text-align: center; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                        VIEW DETAILS
                                    </a>

                                    <c:choose>
                                        <%-- Đã PAID thì hiện thông báo hoàn tất --%>
                                        <c:when test="${o.paymentStatus == 'paid'}">
                                            <span style="background: #28a745; color: white; padding: 10px; text-align: center; border-radius: 5px; font-weight: bold;">
                                                PAID - READY!
                                            </span>
                                        </c:when>

                                        <%-- Chỉ hiện nút Pay nếu CHƯA PAID và ADMIN ĐÃ CONFIRM --%>
                                        <c:when test="${o.paymentStatus == 'deposit_paid' && o.status == 'confirmed'}">
                                            <a href="UpcomingRental?action=pay&id=${o.id}" 
                                               onclick="return confirm('Pay the remaining balance to start your trip?')" 
                                               class="btn-pay" style="background: #28a745; color: white; padding: 10px; text-align: center; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                                PAY & PICK UP
                                            </a>
                                        </c:when>

                                        <%-- Chờ Admin duyệt --%>
                                        <c:when test="${o.status == 'pending'}">
                                            <span style="background: #f0ad4e; color: white; padding: 8px; text-align: center; border-radius: 5px; font-size: 0.9em; font-weight: bold;">
                                                WAITING FOR ADMIN...
                                            </span>
                                        </c:when>
                                    </c:choose>

                                    <!-- Cancel: Chỉ hiện nếu chưa thanh toán xong (paid) -->
                                    <c:if test="${(o.status == 'pending' || o.status == 'confirmed') && o.paymentStatus != 'paid'}">
                                        <a href="UpcomingRental?action=cancel&id=${o.id}" 
                                           onclick="return confirm('Refund Policy:\n- Pending: 100% refund\n- Confirmed: 50% refund\nProceed with cancellation?')" 
                                           class="btn-cancel" style="color: #666; text-align: center; font-size: 0.9em; text-decoration: underline; margin-top: 5px;">
                                            Cancel Booking
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- PAGINATION -->
                    <div class="pagination" style="margin-top: 20px; display: flex; justify-content: center;">
                        <c:forEach begin="1" end="${endP}" var="i">
                            <a class="${tag == i ? 'active' : ''}" 
                               href="UpcomingRental?page=${i}&search=${search}&sort=${sort}"
                               style="padding: 8px 16px; border: 1px solid #ccc; margin: 0 5px; text-decoration: none; ${tag == i ? 'background: #333; color: #fff;' : 'color: #333;'}">
                                ${i}
                            </a>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

    </body>
</html>