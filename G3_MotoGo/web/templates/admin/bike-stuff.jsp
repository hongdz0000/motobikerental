<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Configure Bike Accessories</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin-bike-stuff.css">
    </head>
    <body>

        <%@ include file="../navbar.jsp" %>    

   <style>
    :root {
        --primary-color: #2563eb;
        --secondary-color: #64748b;
        --danger-color: #ef4444;
        --success-color: #10b981;
        --bg-body: #f8fafc;
        --text-main: #1e293b;
        --shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
    }

    body {
        background-color: var(--bg-body);
        font-family: 'Inter', system-ui, -apple-system, sans-serif;
        color: var(--text-main);
        margin: 0;
        padding: 0;
    }

    .admin-container {
        max-width: 1100px;
        margin: 40px auto;
        padding: 0 20px;
    }

    h2 {
        font-size: 1.5rem;
        font-weight: 700;
        margin-bottom: 1.5rem;
        color: #0f172a;
        border-left: 5px solid var(--primary-color);
        padding-left: 15px;
    }

    /* Toolbar & Search */
    .admin-toolbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        gap: 15px;
        flex-wrap: wrap;
    }

    .search-group {
        display: flex;
        gap: 10px;
        flex-grow: 1;
    }

    input[type="text"], input[type="number"] {
        padding: 10px 15px;
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        outline: none;
        transition: border-color 0.2s;
    }

    input:focus {
        border-color: var(--primary-color);
        box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
    }

    /* Table Styling */
    .admin-table-card {
        background: white;
        padding: 24px;
        border-radius: 16px;
        box-shadow: var(--shadow);
    }

    .admin-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0 10px;
    }

    .admin-table th {
        padding: 12px 15px;
        text-align: left;
        color: var(--secondary-color);
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.8rem;
        letter-spacing: 0.05em;
    }

    .admin-table tbody tr {
        background: #ffffff;
        transition: transform 0.2s;
    }

    .admin-table td {
        padding: 15px;
        border-top: 1px solid #f1f5f9;
        border-bottom: 1px solid #f1f5f9;
    }

    .admin-table td:first-child { border-left: 1px solid #f1f5f9; border-radius: 10px 0 0 10px; }
    .admin-table td:last-child { border-right: 1px solid #f1f5f9; border-radius: 0 10px 10px 0; }

    .admin-table img {
        border-radius: 8px;
        object-fit: cover;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }

    /* Buttons */
    .btn-custom {
        padding: 10px 20px;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        border: none;
        transition: all 0.2s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-search { background: var(--secondary-color); color: white; }
    .btn-add { background: var(--primary-color); color: white; }
    .btn-edit { background: var(--success-color); color: white; font-size: 0.9rem; }
    .btn-delete { background: #fee2e2; color: var(--danger-color); font-size: 0.9rem; }
    
    .btn-custom:hover { opacity: 0.9; transform: translateY(-1px); }

    /* Modal */
    .modal { 
        display: none; position: fixed; z-index: 2000; 
        left: 0; top: 0; width: 100%; height: 100%; 
        background: rgba(15, 23, 42, 0.6); 
        backdrop-filter: blur(4px);
    }
    .modal-content { 
        background: white; margin: 5% auto; padding: 30px; 
        border-radius: 20px; width: 40%; box-shadow: 0 25px 50px -12px rgb(0 0 0 / 0.25);
        animation: slideDown 0.3s ease-out;
    }

    @keyframes slideDown {
        from { transform: translateY(-20px); opacity: 0; }
        to { transform: translateY(0); opacity: 1; }
    }

    .close-btn { float: right; font-size: 28px; cursor: pointer; color: #94a3b8; }
</style>

<!-- Nội dung chính -->
<div class="admin-container">
    <h2>Cấu hình phụ kiện: ${selectedBike.name}</h2>
    
    <div class="admin-table-card">
        <div class="admin-toolbar">
            <form action="accessories" method="get" class="search-group">
                <input type="hidden" name="bikeId" value="${selectedBikeId}">
                <input type="text" name="txtSearch" placeholder="Tìm phụ kiện đã gán..." value="${txtSearch}">
                <button type="submit" class="btn-custom btn-search">Tìm kiếm</button>
            </form>
            <!-- Nút mở Pop-up -->
            <button onclick="openModal()" class="btn-custom btn-add">+ Thêm phụ kiện mới</button>
        </div>

        <table class="admin-table">
            <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Tên</th>
                    <th>Số lượng</th>
                    <th>Giá ghi đè</th>
                    <th style="text-align: center;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="entry" items="${stuffMap}">
                <tr>
                    <td><img src="${entry.key.stuffIcon}" width="40"></td>
                    <td><strong>${entry.key.stuffName}</strong></td>
                    <form action="accessories" method="post">
                        <input type="hidden" name="bikeId" value="${selectedBikeId}">
                        <input type="hidden" name="stuffId" value="${entry.key.stuffId}">
                        <td><input type="number" name="includedQty" value="${entry.value}" style="width: 60px;"></td>
                        <td><input type="number" name="overridePrice" value="${entry.key.basePricePerDay}"></td>
                        <td style="text-align: center;">
                            <button type="submit" name="action" value="save" class="btn-custom btn-edit">Lưu</button>
                            <button type="submit" name="action" value="delete" class="btn-custom btn-delete" onclick="return confirm('Xóa cấu hình này?')">Xóa</button>
                        </td>
                    </form>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- POP-UP (MODAL) -->
<div id="addStuffModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="closeModal()">&times;</span>
        <h3>Chọn phụ kiện để thêm vào xe</h3>
        <hr>
        <div style="max-height: 400px; overflow-y: auto;">
            <table class="admin-table">
                <c:forEach var="s" items="${availableToAdd}">
                <tr>
                    <td><img src="${s.stuffIcon}" width="30"></td>
                    <td>${s.stuffName}</td>
                    <td><fmt:formatNumber value="${s.basePricePerDay}" type="currency" currencySymbol="đ""")/></td>
                    <td>
                        <form action="accessories" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="bikeId" value="${selectedBikeId}">
                            <input type="hidden" name="stuffId" value="${s.stuffId}">
                            <button type="submit" class="btn-custom btn-add" style="padding: 5px 10px;">Thêm</button>
                        </form>
                    </td>
                </tr>
                </c:forEach>
            </table>
            <c:if test="${empty availableToAdd}">
                <p style="text-align: center; color: #7f8c8d;">Xe này đã sở hữu tất cả phụ kiện hiện có.</p>
            </c:if>
        </div>
    </div>
</div>

<script>
function openModal() { document.getElementById("addStuffModal").style.display = "block"; }
function closeModal() { document.getElementById("addStuffModal").style.display = "none"; }
window.onclick = function(event) {
    if (event.target == document.getElementById("addStuffModal")) closeModal();
}
</script>
    </body>

    <script>
        function updateBikeNames(brand) {
        console.log("Inside updateBikeNames - received brand:", brand);
        const bikeSelect = document.getElementById("bikeSelect");
        const infoDisplay = document.getElementById("bikeInfoDisplay");
        const selectedBikeId = "${selectedBikeId}";
        if (!brand) {
        console.log("No brand → disabling bike select and hiding info");
        bikeSelect.disabled = true;
        infoDisplay.style.display = "none";
        return;
        }

        // Build the URL — using the context path (adjust if needed)
        const contextPath = "${pageContext.request.contextPath}";
        const url = contextPath + "/accessories?action=getBikes&brand=" + encodeURIComponent(brand);
        console.log("About to fetch bikes from this URL:", url);
        fetch(url)
                .then(response => {
                console.log("Fetch response received. Status:", response.status);
                if (!response.ok) {
                throw new Error("Server responded with status " + response.status);
                }
                return response.text(); // ← first get as text so we can see raw content
                })
                .then(text => {
                console.log("Raw response from server:", text);
                try {
                const data = JSON.parse(text);
                console.log("Parsed JSON successfully. Number of bikes:", data.length);
                console.log("First bike (if any):", data[0] || "no bikes");
                // Now fill the dropdown
                bikeSelect.innerHTML = '<option value="">-- Choose Bike --</option>';
                data.forEach(bike => {
                let opt = document.createElement("option");
                opt.value = bike.id;
                opt.text = bike.name;
                opt.dataset.price = bike.price;
                opt.dataset.status = bike.status;
                bikeSelect.appendChild(opt);
                });
                bikeSelect.disabled = (data.length === 0);
                console.log("Bike dropdown updated. Items added:", data.length);
                } catch (jsonError) {
                console.error("JSON parse failed:", jsonError);
                console.error("Raw text was:", text);
                }
                })
                .catch(error => {
                console.error("Fetch failed:", error);
                });
        }

        function updateBikeInfo() {
            const bikeSelect = document.getElementById("bikeSelect");
            const selected = bikeSelect.options[bikeSelect.selectedIndex];
            if (selected.value) {
                document.getElementById("infoBrand").innerText =
                        document.getElementById("brandSelect").value;
                document.getElementById("infoPrice").innerText =
                        parseFloat(selected.dataset.price).toLocaleString();
                document.getElementById("infoStatus").innerText =
                        selected.dataset.status;
                document.getElementById("bikeInfoDisplay").style.display = "block";

                const contextPath = "${pageContext.request.contextPath}";
                window.location.href =
                        contextPath + "/accessories?bikeId=" + selected.value;
            }
        }
    </script>
</html>