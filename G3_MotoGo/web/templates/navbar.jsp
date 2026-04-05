<%-- 
    Document   : navbar
    Created on : Feb 12, 2026, 10:23:27 PM
    Author     : thais
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/navbar.css">

<header class="navbar">
    <div class="logo">
    <c:choose>
        <%-- Nếu chưa đăng nhập: Về trang Login hoặc Home tùy bạn --%>
        <c:when test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/home" style="text-decoration:none; color:inherit;">
                MotoGo
            </a>
        </c:when>

        <%-- Nếu là Admin: Về Dashboard --%>
        <c:when test="${sessionScope.user.role eq 'admin'}">
            <a href="${pageContext.request.contextPath}/DashBoardInfor" style="text-decoration:none; color:inherit;">
                MotoGo
            </a>
        </c:when>

        <%-- Nếu là User (hoặc các role khác): Về Home --%>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/home" style="text-decoration:none; color:inherit;">
                MotoGo
            </a>
        </c:otherwise>
    </c:choose>
</div>
<nav>

    <c:choose>
        <c:when test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/templates/login.jsp">
                <button class="btn-primary">Login</button>
            </a>
        </c:when>

        <c:otherwise>
            <div class="user-wrapper">

                <div class="user-box" id="profileToggle">
                    <span class="welcome">
                        Welcome, ${sessionScope.user.userName}!
                    </span>

                <img 
                    src="${pageContext.request.contextPath}/${sessionScope.user.avatar}"
                    class="avatar"
                    alt="User Avatar">
                </div>

                <div class="profile-dropdown" id="profileDropdown">
                        <!-- Links common to everyone -->
                        <a href="${pageContext.request.contextPath}/Profile">Profile</a>

                        <!-- Only show these if the user is NOT an admin -->
                        <c:if test="${sessionScope.user.role ne 'admin'}">
                            <a href="${pageContext.request.contextPath}/wallet">Wallet</a>
                            <a href="${pageContext.request.contextPath}/UpcomingRental">Upcoming Rentals</a>
                            <a href="${pageContext.request.contextPath}/past-rentals">Bookings History</a>
                        </c:if>

                        <div class="divider"></div>

                        <!-- Logout is common to everyone -->
                        <a href="${pageContext.request.contextPath}/logout">Log out</a>
                    </div>

            </div>
        </c:otherwise>

    </c:choose>

</nav>   
    
</header>

<script>
    const toggle = document.getElementById("profileToggle");
    const dropdown = document.getElementById("profileDropdown");

    if (toggle) {
        toggle.addEventListener("click", function (e) {
            e.stopPropagation();
            dropdown.classList.toggle("show");
        });

        document.addEventListener("click", function () {
            dropdown.classList.remove("show");
        });
    }
</script>

