<%-- 
    Document   : past-rentals
    Created on : Feb 12, 2026, 9:47:01 PM
    Author     : thais
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Past Rentals | MotoGo</title>

        <!-- Shared navbar styles -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/index.css">

        <!-- Page styles -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/past.css">
    </head>
    <body>

        <!-- NAVBAR -->
        <jsp:include page="navbar.jsp" />


        <!-- PAGE HEADER -->
        <header class="page-header">
            <div>
                <h1>Your past bookings</h1>
            </div>

            <div>
                <a href="UpcomingRental" class="past-link">
                    VIEW UPCOMING BOOKINGS
                </a>
            </div>
        </header>


        <!-- CONTENT -->
        <section class="rentals-section">

            <div class="rentals-top">
                <p>
                    You have 
                    <strong>${totalOrdersCount}</strong> 
                    completed rentals
                </p>

                <div class="sort-box">
                    <label>SORT BY</label>
                    <form action="past-rentals" method="GET" id="sortForm">
                        <select name="sort" onchange="document.getElementById('sortForm').submit()">
                            <option value="DESC" ${param.sort == 'DESC' ? 'selected' : ''}>LATEST</option>
                            <option value="ASC" ${param.sort == 'ASC' ? 'selected' : ''}>OLDEST</option>
                        </select>
                    </form>
                </div>
            </div>

            <!-- Booking cards -->
            <div class="booking-list">

                <c:forEach var="r" items="${rentals}">

                    <div class="booking-card">
                        <img src="${pageContext.request.contextPath}/${r.motorbikeIcon}">

                        <div class="booking-info">
                            <h3>${r.motorbikeName}</h3>
                            <p>
                                <fmt:formatDate value="${r.startDate}" pattern="MMM dd" /> - 
                                <fmt:formatDate value="${r.endDate}" pattern="MMM dd" />
                            </p>
                            <p class="price"><fmt:formatNumber value="${r.totalBikeCost + r.totalStuffCost}" type="number" groupingUsed="true"/> VND total</p>
                        </div>

                        <div class="booking-status ${r.status.toLowerCase()}">
                            ${r.status}
                        </div>
                    </div>

                </c:forEach>

            </div>

            <div class="pagination">

                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}&sort=${param.sort}">Previous</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="?page=${i}&sort=${param.sort}"
                       class="${i == currentPage ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}&sort=${param.sort}">Next</a>
                </c:if>

            </div>

        </section>
        <jsp:include page="footer.jsp" />
    </body>
</html>
