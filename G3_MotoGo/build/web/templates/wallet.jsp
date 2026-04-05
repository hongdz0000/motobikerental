<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Wallet - MotoGo</title>
    <style>
        .wallet-container { padding: 20px; max-width: 1000px; margin: auto; font-family: Arial, sans-serif; }
        .wallet-top-row { display: flex; gap: 20px; margin-bottom: 20px; }
        .wallet-card { border: 1px solid #ddd; border-radius: 8px; flex: 1; padding: 15px; background: #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card-header { font-weight: bold; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 15px; font-size: 1.2em; }
        .amount-display { font-size: 24px; font-weight: bold; color: #333; }
        
        /* Màu sắc số tiền */
        .pos { color: #28a745 !important; font-weight: bold; }
        .neg { color: #dc3545 !important; font-weight: bold; }
        
        .transaction-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        .transaction-table th, .transaction-table td { border: 1px solid #eee; padding: 12px; text-align: left; }
        .transaction-table th { background-color: #f8f9fa; }
        
        .btn-deposit-blue { background: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
        .input-group { margin-bottom: 10px; }
        .input-group input { padding: 8px; width: 200px; border: 1px solid #ccc; border-radius: 4px; }

        /* --- CSS CHO PHÂN TRANG --- */
        .pagination-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
            gap: 5px;
        }
        .pagination-item {
            padding: 8px 16px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #007bff;
            border-radius: 4px;
            transition: background 0.3s;
        }
        .pagination-item.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
            cursor: default;
        }
        .pagination-item:hover:not(.active) {
            background-color: #f1f1f1;
        }
        .pagination-item.disabled {
            color: #ccc;
            pointer-events: none;
            background-color: #fafafa;
        }
    </style>
</head>
<body>
    <jsp:include page="navbar.jsp" />

    <section class="wallet-container">
        <h1 class="page-title">Wallet Management</h1>

        <div class="wallet-top-row">
            <div class="wallet-card">
                <div class="card-header">Current Balance</div>
                <div class="amount-display">
                    <%-- Lấy balance từ request attribute do WalletServlet gửi sang --%>
                    <fmt:formatNumber value="${balance}" type="number" groupingUsed="true" /> VND
                </div>
            </div>

            <div class="wallet-card">
                <div class="card-header">Deposit Money</div>
                <form action="wallet" method="post">
                    <div class="input-group">
                        <input type="number" name="amount" placeholder="Min 10,000 VND" min="10000" required>
                        <button type="submit" class="btn-deposit-blue">Deposit</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="wallet-card">
            <div class="card-header">Transaction History</div>
            <table class="transaction-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Amount (VND)</th>
                        <th>Date</th>
                        <th>Details</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${transactions}">
                        <tr>
                            <td>#${t.id}</td>
                            <td style="text-transform: capitalize;">${t.type}</td>
                            <td class="${t.type == 'payment' || t.amount < 0 ? 'neg' : 'pos'}">
                                <c:choose>
                                    <c:when test="${t.type == 'payment' || t.amount < 0}">
                                        -<fmt:formatNumber value="${t.amount < 0 ? -t.amount : t.amount}" type="number" groupingUsed="true" />
                                    </c:when>
                                    <c:otherwise>
                                        +<fmt:formatNumber value="${t.amount}" type="number" groupingUsed="true" />
                                    </c:otherwise>
                                </c:choose>
                                VND
                            </td>
                            <td><fmt:formatDate value="${t.createdAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.orderId > 0}">Order #${t.orderId}</c:when>
                                    <c:otherwise>Direct Balance Action</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty transactions}">
                        <tr>
                            <td colspan="5" style="text-align: center; color: #999;">No transactions found.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <!-- HIỂN THỊ PHÂN TRANG -->
            <c:if test="${totalPages > 1}">
                <div class="pagination-container">
                    <%-- Nút Previous --%>
                    <a href="wallet?page=${currentPage - 1}" class="pagination-item ${currentPage == 1 ? 'disabled' : ''}">&laquo; Previous</a>

                    <%-- Danh sách các trang --%>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="wallet?page=${i}" class="pagination-item ${currentPage == i ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <%-- Nút Next --%>
                    <a href="wallet?page=${currentPage + 1}" class="pagination-item ${currentPage == totalPages ? 'disabled' : ''}">Next &raquo;</a>
                </div>
            </c:if>
        </div>
    </section>
</body>
</html>