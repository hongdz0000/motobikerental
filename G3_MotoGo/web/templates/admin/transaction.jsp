<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Giao Dịch - Admin</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
        <!-- Import font từ Google để giao diện hiện đại hơn -->
        <link href="https://fonts.googleapis.com" rel="stylesheet">

        <style>
            :root {
                --primary-color: #2563eb;
                --success-color: #059669;
                --danger-color: #dc2626;
                --warning-color: #d97706;
                --info-color: #0891b2;
                --bg-body: #f8fafc;
                --text-main: #1e293b;
            }

            body {
                font-family: 'Inter', sans-serif;
                background-color: var(--bg-body);
                color: var(--text-main);
                margin: 0;
            }

            .admin-container {
                max-width: 1200px;
                margin: 20px auto;
                padding: 0 20px;
            }

            .admin-header h2 {
                font-weight: 700;
                color: #0f172a;
                margin-bottom: 20px;
                border-left: 5px solid var(--primary-color);
                padding-left: 15px;
            }

            /* Filter Card */
            .admin-filter-card {
                background: white;
                padding: 20px;
                border-radius: 12px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }

            .filter-grid-form {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                gap: 15px;
                align-items: end;
            }

            .filter-grid-form input,
            .filter-grid-form select {
                padding: 10px;
                border: 1px solid #e2e8f0;
                border-radius: 8px;
                outline: none;
                font-size: 14px;
                transition: border 0.2s;
            }

            .filter-grid-form input:focus,
            .filter-grid-form select:focus {
                border-color: var(--primary-color);
            }

            .btn-primary {
                background: var(--primary-color);
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 8px;
                cursor: pointer;
                font-weight: 600;
                transition: opacity 0.2s;
            }

            .btn-secondary {
                background: #f1f5f9;
                color: #475569;
                text-decoration: none;
                padding: 10px 20px;
                border-radius: 8px;
                text-align: center;
                font-size: 14px;
                font-weight: 600;
            }

            .btn-primary:hover {
                opacity: 0.9;
            }
            .btn-secondary:hover {
                background: #e2e8f0;
            }

            /* Table Style */
            .admin-table-card {
                background: white;
                border-radius: 12px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                overflow: hidden;
            }

            .admin-table {
                width: 100%;
                border-collapse: collapse;
                text-align: left;
            }

            .admin-table th {
                background: #f8fafc;
                padding: 15px;
                font-weight: 600;
                color: #64748b;
                border-bottom: 1px solid #e2e8f0;
                text-transform: uppercase;
                font-size: 12px;
            }

            .admin-table td {
                padding: 15px;
                border-bottom: 1px solid #f1f5f9;
                font-size: 14px;
            }

            .admin-table tr:hover {
                background-color: #f1f5f9;
            }

            /* Badges */
            .badge {
                padding: 4px 10px;
                border-radius: 20px;
                font-size: 11px;
                font-weight: 700;
                display: inline-block;
            }
            .badge.deposit {
                background: #dcfce7;
                color: var(--success-color);
            }
            .badge.payment {
                background: #dbeafe;
                color: var(--primary-color);
            }
            .badge.refund  {
                background: #ede9fe;
                color: #7c3aed;
            }
            .badge.penalty {
                background: #fee2e2;
                color: var(--danger-color);
            }

            /* Amount Colors */
            .amt-pos {
                color: var(--success-color);
                font-weight: 600;
            }
            .amt-neg {
                color: var(--danger-color);
                font-weight: 600;
            }

            /* Pagination */
            .pagination {
                margin: 30px 0;
                display: flex;
                justify-content: center;
                gap: 5px;
            }
            .pagination a, .pagination span {
                padding: 8px 14px;
                border: 1px solid #e2e8f0;
                text-decoration: none;
                color: var(--text-main);
                border-radius: 6px;
                background: white;
                transition: all 0.2s;
            }
            .pagination .active {
                background: var(--primary-color);
                color: white;
                border-color: var(--primary-color);
            }
            .pagination a:hover:not(.active) {
                background: #f1f5f9;
            }
        </style>
    </head>
    <body>
        <%@ include file="../navbar.jsp" %>

        <div class="admin-container">
            <div class="admin-header">
                <h2>Quản lý giao dịch hệ thống</h2>
            </div>

            <div class="admin-filter-card">
                <form action="AdminTransaction" method="GET" class="filter-grid-form">
                    <div>
                        <label style="font-size: 12px; font-weight: 600; margin-bottom: 5px; display: block;">Email người dùng</label>
                        <input type="text" name="email" placeholder="Tìm theo email..." value="${param.email}" style="width: 90%;">
                    </div>
                    <div>
                        <label style="font-size: 12px; font-weight: 600; margin-bottom: 5px; display: block;">Loại giao dịch</label>
                        <select name="type" style="width: 100%;">
                            <option value="">-- Tất cả loại --</option>
                            <option value="deposit" ${param.type == 'deposit' ? 'selected' : ''}>Nạp tiền (+)</option>
                            <option value="payment" ${param.type == 'payment' ? 'selected' : ''}>Thanh toán (-)</option>
                            <option value="refund" ${param.type == 'refund' ? 'selected' : ''}>Hoàn tiền (+)</option>
                            <option value="penalty" ${param.type == 'penalty' ? 'selected' : ''}>Tiền phạt (-)</option>
                        </select>
                    </div>
                    <div>
                        <label style="font-size: 12px; font-weight: 600; margin-bottom: 5px; display: block;">Sắp xếp</label>
                        <select name="sortBy" style="width: 100%;">
                            <option value="date_desc" ${param.sortBy == 'date_desc' ? 'selected' : ''}>Mới nhất</option>
                            <option value="amount_desc" ${param.sortBy == 'amount_desc' ? 'selected' : ''}>Số tiền giảm dần</option>
                        </select>
                    </div>
                    <div>
                        <label style="font-size: 12px; font-weight: 600; margin-bottom: 5px; display: block;">Từ ngày</label>
                        <input type="date" id="fromDate" name="fromDate" value="${param.fromDate}" style="width: 90%;">
                    </div>
                    <div>
                        <label style="font-size: 12px; font-weight: 600; margin-bottom: 5px; display: block;">Đến ngày</label>
                        <input type="date" id="toDate" name="toDate" value="${param.toDate}" style="width: 90%;">
                    </div>
                    <div style="display: flex; gap: 10px;">
                        <button type="submit" class="btn-primary">Lọc</button>
                        <a href="AdminTransaction" class="btn-secondary">Đặt lại</a>
                    </div>
                </form>
            </div>

            <div class="admin-table-card">
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>Mã GD</th> 
                            <th>Email người dùng</th> 
                            <th>Loại hình</th> 
                            <th style="text-align: right;">Số tiền</th> 
                            <th style="text-align: right;">Thời gian</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tx" items="${transactionList}">
                            <tr>
                                <td><span style="color: #64748b; font-weight: 500;">#${tx.id}</span></td>
                                <td>${tx.userEmail}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tx.type == 'deposit'}"><span class="badge deposit">Nạp tiền</span></c:when>
                                        <c:when test="${tx.type == 'payment'}"><span class="badge payment">Thanh toán</span></c:when>
                                        <c:when test="${tx.type == 'refund'}"><span class="badge refund">Hoàn tiền</span></c:when>
                                        <c:when test="${tx.type == 'penalty'}"><span class="badge penalty">Tiền phạt</span></c:when>
                                        <c:otherwise><span class="badge">${tx.type}</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="${(tx.type == 'deposit' || tx.type == 'refund') ? 'amt-pos' : 'amt-neg'}" style="text-align: right;">
                                    <c:choose>
                                        <c:when test="${tx.type == 'deposit' || tx.type == 'refund'}">+</c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                    <c:set var="absAmount" value="${tx.amount < 0 ? -tx.amount : tx.amount}" />
                                    <%-- Định dạng VNĐ: Dấu chấm phân cách hàng nghìn --%>
                                    <fmt:formatNumber value="${absAmount}" pattern="#,###"/> VNĐ
                                </td>
                                <td style="text-align: right; color: #64748b;">
                                    <fmt:formatDate value="${tx.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty transactionList}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 30px; color: #94a3b8;">
                                    Không tìm thấy giao dịch nào phù hợp.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="AdminTransaction?page=${i}&email=${param.email}&type=${param.type}&fromDate=${param.fromDate}&toDate=${param.toDate}&sortBy=${param.sortBy}" 
                           class="${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </body>
    <script>
        const fromDate = document.getElementById('fromDate');
        const toDate = document.getElementById('toDate');

        fromDate.addEventListener('change', function () {
            if (fromDate.value) {
                // Set the minimum selectable date for "To" to be the value of "From"
                toDate.min = fromDate.value;
            }
        });

        toDate.addEventListener('change', function () {
            if (toDate.value) {
                // Optionally set the maximum for "From" to be the value of "To"
                fromDate.max = toDate.value;
            }
        });
    </script> 
</html>
