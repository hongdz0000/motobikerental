<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Bike List | MotoGo</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bike-list.css">
    </head>
    <body>

        <jsp:include page="navbar.jsp" />

        <main class="page-container">

            <!-- LEFT: FILTER / SORT (1/3) -->
            <aside class="filter-panel">
                <h2>Filter</h2>
                <form method="get" action="bikes">
                    <div class="filter-group">
                        <label>Displacement (cc)</label>
                        <select name="engineSize">
                            <option value="">All</option>
                            <option value="small" 
                                    ${selectedEngine == 'small' ? "selected" : ""}>
                                &lt;175cc
                            </option>

                            <option value="medium" 
                                    ${selectedEngine == 'medium' ? "selected" : ""}>
                                &lt;600cc
                            </option>

                            <option value="large" 
                                    ${selectedEngine == 'large' ? "selected" : ""}>
                                600cc+
                            </option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Transmission</label>
                        <select name="transmission">
                            <option value="">All</option>
                            <option value="Automatic"
                                    ${selectedTransmission == 'Automatic' ? "selected" : ""}>
                                Automatic
                            </option>

                            <option value="Semi-Auto"
                                    ${selectedTransmission == 'Semi-Auto' ? "selected" : ""}>
                                Semi-Auto
                            </option>

                            <option value="Manual"
                                    ${selectedTransmission == 'Manual' ? "selected" : ""}>
                                Manual
                            </option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Brand</label>
                        <select name="brand">
                            <option value="">All</option>

                            <c:forEach var="b" items="${brands}">
                                <option value="${b}" ${b == selectedBrand ? "selected" : ""}>${b}</option>
                            </c:forEach>

                        </select>
                    </div>

                    <button class="apply-btn">Apply filter</button>
                </form>
            </aside>

            <!-- RIGHT: BIKE LIST (2/3) -->
            <section class="bike-list-wrapper">

                <!-- TOP RIGHT SORT -->
                <div class="list-header">
                    <div></div> <!-- spacer -->
                    <form method="get" action="bikes" class="sort-box">

                        <input type="hidden" name="brand" value="${selectedBrand}">
                        <input type="hidden" name="engine" value="${selectedEngine}">
                        <input type="hidden" name="transmission" value="${selectedTransmission}">
                        <input type="hidden" name="page" value="${currentPage}">

                        <label for="sort">Sort by</label>
                        <select id="sort" name="sort" onchange="this.form.submit()">

                            <option value="" ${selectedSort == 'default' ? "selected" : ""}>
                                Default
                            </option>

                            <option value="priceDesc" ${selectedSort == 'priceDesc' ? "selected" : ""}>
                                Price (Highest)
                            </option>

                            <option value="priceAsc" ${selectedSort == 'priceAsc' ? "selected" : ""}>
                                Price (Lowest)
                            </option>

                        </select>

                    </form>
                </div>

                <section class="bike-list">
                    <c:if test="${empty bikeList}">
                        <p>No bikes match your filters.</p>
                    </c:if>

                    <c:forEach var="bike" items="${bikeList}">
                        <div class="bike-card">

                            <div class="bike-image-box">
                                <img src="${pageContext.request.contextPath}/${bike.bikeIcon}" 
                                     alt="${bike.name}">
                            </div>

                            <div class="bike-info">
                                <h3>${bike.name}</h3>

                                <p><fmt:formatNumber value="${bike.currentPricePerDay}" type="number" groupingUsed="true"/> VND / day</p>
                                <a href="bike-detail?id=${bike.id}" class="view-btn">
                                    View
                                </a>
                            </div>
                        </div>
                    </c:forEach>

                </section>
                <div class="pagination">

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="bikes?page=${i}&brand=${selectedBrand}&engine=${selectedEngine}&transmission=${selectedTransmission}&sort=${selectedSort}"
                           class="${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                </div>

            </section>


        </main>
        <jsp:include page="footer.jsp" />
    </body>
</html>
