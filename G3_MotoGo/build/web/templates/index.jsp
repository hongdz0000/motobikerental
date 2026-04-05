<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>MotoGo</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/navbar.css">
    </head>

    <body>

        <!-- NAVBAR -->
        <jsp:include page="navbar.jsp" />
    
    <!-- HERO -->
    <section class="hero">
        <img src="${pageContext.request.contextPath}/images/background.jpg" class="hero-bg">

        <div class="hero-content">
            <span class="hero-tag">Rent a motorbike anywhere in the world</span>
            <h1>Motorcycle & Scooter<br>rentals in Vietnam</h1>


        </div>
    </section>

    <!-- MOTORCYCLE SECTION -->

    <section class="section bikes-section">
        <div class ="carousel-wrapper">
            <div class="section-header">
                <h2>Motorcycles for rent in Vietnam</h2>

                <a href="${pageContext.request.contextPath}/bikes">
                    <button class="view-all-btn">View All Motorcycles</button>
                </a>
            </div>

            <div class="carousel">
                <div class="carousel-track" id="bikeTrack">

                    <c:forEach var="bike" items="${topBikes}">
                        <div class="card">
                            <img src="${pageContext.request.contextPath}/${bike.bikeIcon}" 
                                 alt="${bike.name}">

                            <h3>${bike.name}</h3>
                            <p>from <strong><fmt:formatNumber value="${bike.currentPricePerDay}" type="number" groupingUsed="true"/> VND / day</strong></p>

                            <a href="${pageContext.request.contextPath}/bike-detail?id=${bike.id}">
                                <button>View</button>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="carousel-controls">
                <button class="arrow" onclick="slide(-1)">&#10094;</button>
                <button class="arrow" onclick="slide(1)">&#10095;</button>
            </div>  
        </div>       
    </section>
    <jsp:include page="footer.jsp" />
    <script>
        let index = 0;

        const track = document.getElementById("bikeTrack");
        const cards = track.children;
        const cardsPerPage = 4;

        function slide(direction) {
            const cardWidth = cards[0].offsetWidth;
            const gap = 20; // must match CSS gap
            const pageWidth = (cardWidth + gap) * cardsPerPage;

            const totalPages = Math.ceil(cards.length / cardsPerPage);

            index += direction;

            if (index < 0)
                index = 0;
            if (index > totalPages - 1)
                index = totalPages - 1;

            track.style.transform = "translateX(-" + (index * pageWidth) + "px)";
        }
    </script>
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

</body>
</html>
