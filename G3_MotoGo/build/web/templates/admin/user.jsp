<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management | MotoGo Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/user-profile.css">
    <style>
        /* Modal & Animations */
        .modal { display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.6); backdrop-filter: blur(3px); }
        .modal-content { 
    position: relative; 
    background-color: #fff; 
    margin: 5% auto; 
    padding: 30px; 
    border-radius: 15px; 
    width: 500px; /* Bản User2 dùng 500px thay vì 800px */
    box-shadow: 0 10px 30px rgba(0,0,0,0.3); 
    animation: modalSlideDown 0.3s ease-out; 
}.form-group { margin-bottom: 15px; }
.form-group label { 
    display: block; 
    font-size: 12px; 
    font-weight: bold; 
    margin-bottom: 5px; 
    color: #666; 
    text-transform: uppercase; /* Nhãn viết hoa giống User2 */
}
.form-group input, .form-group select {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    box-sizing: border-box;
}
        @keyframes modalSlideDown { from { transform: translateY(-50px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        
        /* Toolbar Layout */
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; gap: 15px; flex-wrap: wrap; }
        .filter-group { display: flex; gap: 10px; align-items: center; flex-grow: 1; }
        .filter-group input { padding: 10px 15px; border: 1px solid #ddd; border-radius: 8px; width: 250px; }
        .filter-group select { padding: 10px; border: 1px solid #ddd; border-radius: 8px; }

        /* Button Styles */
        .btn-group { display: flex; gap: 10px; }
        .btn-activate-modal { background-color: #007bff; color: white; padding: 10px 18px; border-radius: 8px; font-weight: bold; border: none; cursor: pointer; }
        .btn-create { background-color: #28a745; color: white; padding: 10px 18px; border-radius: 8px; font-weight: bold; border: none; cursor: pointer; }
        .btn-delete-mode { background-color: #dc3545; color: white; padding: 10px 18px; border-radius: 8px; font-weight: bold; border: none; cursor: pointer; }
        .btn-choose-all-small { background-color: #6c757d; color: white; padding: 5px 12px; border-radius: 5px; font-size: 12px; border: none; cursor: pointer; margin-bottom: 10px; }

        /* Activate Table inside Modal */
        #activateSearch { width: 100%; padding: 12px; margin: 15px 0; border-radius: 8px; border: 1px solid #ddd; box-sizing: border-box; }
        .activate-container { max-height: 400px; overflow-y: auto; border: 1px solid #eee; border-radius: 8px; }
        
        .row-hidden { display: none !important; }
        .col-checkbox { display: none; text-align: center; }
        .modal-confirm-footer { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
    </style>
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="admin-container">
    <div class="page-header">
        <h2 class="page-title">User Management</h2>
        <p class="page-description">Quản lý tài khoản người dùng, phân quyền và trạng thái hệ thống.</p>
    </div>

    <div class="header-actions">
        <div class="filter-group">
            <input type="text" id="searchInput" placeholder="Tìm kiếm tài khoản..." onkeyup="filterTable()">
            <select id="roleFilter" onchange="filterTable()">
                <option value="">All Roles</option>
                <option value="admin">Admin</option>
                <option value="user">User</option>
            </select>
        </div>

        <div class="btn-group">
            <button id="btnOpenActivate" class="btn-activate-modal" onclick="showActivateModal()">Activate</button>
            <button id="btnCreate" class="btn-create" onclick="showCreateForm()">+ Create New Account</button>
            <button id="btnDeleteMode" class="btn-delete-mode" onclick="enableDeleteMode()">Delete Accounts</button>
            
            <button id="btnChooseAll" class="btn-cancel-mode" style="display:none; background:#6c757d; color:white" onclick="toggleSelectAll()">Select All</button>
            <button id="btnConfirmDelete" class="btn-confirm-delete" style="display:none; background:#dc3545; color:white" onclick="openConfirmModal()">Confirm Delete</button>
            <button id="btnCancelMode" class="btn-cancel-mode" style="display:none" onclick="disableDeleteMode()">Cancel</button>
        </div>
    </div>

    <div id="activateModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="hideActivateModal()" style="float:right; cursor:pointer; font-size: 28px;">&times;</span>
            <h3 style="color: #007bff; margin-top: 0;">Activate Inactive Accounts</h3>
            
            <input type="text" id="activateSearch" placeholder="Tìm theo tên, email, role, phone..." onkeyup="filterActivateTable()">
            <button type="button" class="btn-choose-all-small" onclick="toggleActivateAll()">Choose All / Unselect All</button>

            <form id="activateForm" action="${pageContext.request.contextPath}/ActivateUsers" method="POST">
                <div class="activate-container">
                    <table class="admin-table" id="activateTable" style="width: 100%;">
                        <thead>
                            <tr style="background: #f8f9fa;">
                                <th style="width: 40px; text-align: center;">Select</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Phone</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${inactiveList}">
                                <tr class="activate-row-item">
                                    <td style="text-align: center;">
                                        <input type="checkbox" name="activateIds" value="${u.userId}" class="activate-checkbox">
                                    </td>
                                    <td class="act-name"><strong>${u.userName}</strong></td>
                                    <td class="act-email">${u.userEmail}</td>
                                    <td class="act-role">${u.role}</td>
                                    <td class="act-phone">${u.phone != null ? u.phone : "N/A"}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="form-actions" style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px;">
                    <button type="button" class="cancel-btn" onclick="hideActivateModal()">Close</button>
                    <button type="button" class="save-btn" style="background:#28a745" onclick="openConfirmActivateModal()">Activate Selected</button>
                </div>
            </form>
        </div>
    </div>

    <div id="confirmActivateModal" class="modal">
        <div class="modal-content" style="width: 400px; text-align: center; margin-top: 10%;">
            <h3 style="color: #28a745;">Confirm Activation</h3>
            <p>Bạn có chắc chắn muốn kích hoạt lại các tài khoản đã chọn không?</p>
            <div class="modal-confirm-footer">
                <button class="cancel-btn" onclick="closeConfirmActivateModal()">Cancel</button>
                <button class="save-btn" style="background: #28a745;" onclick="submitActivate()">Yes, Activate</button>
            </div>
        </div>
    </div>

    <div id="confirmDeleteModal" class="modal">
        <div class="modal-content" style="width: 400px; text-align: center; margin-top: 10%;">
            <h3 style="color: #dc3545;">Confirm Deletion</h3>
            <p>Are you sure you want to delete the selected accounts? This action cannot be undone.</p>
            <div class="modal-confirm-footer">
                <button class="cancel-btn" onclick="closeConfirmModal()">Cancel</button>
                <button class="save-btn" style="background: #dc3545;" onclick="submitDelete()">Yes, Delete</button>
            </div>
        </div>
    </div>

    <form id="deleteForm" action="${pageContext.request.contextPath}/DeleteUsers" method="POST">
        <div class="admin-table-card">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th class="col-checkbox">Select</th>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Phone</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th class="col-action">Action</th>
                    </tr>
                </thead>
                <tbody id="userTableBody">
                    <c:forEach var="u" items="${userList}">
                        <tr class="user-row-item" data-role="${u.role.toLowerCase()}" data-status="${u.status.toLowerCase()}">
                            <td class="col-checkbox"><input type="checkbox" name="deleteIds" value="${u.userId}" class="user-checkbox"></td>
                            <td class="cell-id">#${u.userId}</td>
                            <td class="cell-name"><strong>${u.userName}</strong></td>
                            <td>${u.phone != null ? u.phone : "N/A"}</td>
                            <td class="cell-email">${u.userEmail}</td>
                            <td style="text-transform: capitalize;">${u.role}</td>
                            <td class="col-action">
                                <button type="button" class="btn-primary" onclick="showEditForm('${u.userId}', '${u.userName}', '${u.userEmail}', '${u.role}', '${u.phone}')">Update</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </form>

    <div id="userModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="hideModal()" style="float:right; cursor:pointer; font-size: 24px;">&times;</span>
        <h3 id="modalTitle" style="color: #007bff; border-bottom: 1px solid #eee; padding-bottom: 10px;">Account Info</h3>
        
        <form id="userForm" action="" method="POST" style="margin-top:20px;">
            <input type="hidden" id="editUserId" name="userId">
            
            <div class="form-group">
                <label>USER NAME</label>
                <input type="text" id="editUserName" name="userName" required>
            </div>
            
            <div class="form-group" id="emailGroup">
                <label>EMAIL ADDRESS</label>
                <input type="email" id="editUserEmail" name="userEmail" required>
            </div>
            
            <div class="form-group" id="passwordGroup">
                <label>PASSWORD</label>
                <input type="password" name="password" id="editPassword">
            </div>
            
            <div class="form-group">
                <label>PHONE NUMBER</label>
                <input type="text" id="editPhone" name="phone">
            </div>
            
            <div class="form-group">
                <label>ROLE</label>
                <select id="editRole" name="role">
                    <option value="user">User</option>
                    <option value="admin">Admin</option>
                </select>
            </div>
            
            <div class="form-actions" style="margin-top: 30px; display: flex; justify-content: flex-end; gap: 10px;">
                <button type="button" class="cancel-btn" onclick="hideModal()">Cancel</button>
                <button type="submit" class="save-btn" id="submitBtn" style="background: #28a745; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-weight: bold;">Save</button>
            </div>
        </form>
    </div>
</div>

<script>
    /* LOGIC XÁC NHẬN ACTIVATE */
    function openConfirmActivateModal() {
        // Kiểm tra xem có ai được chọn không
        const selected = document.querySelectorAll('.activate-checkbox:checked').length;
        if (selected === 0) {
            alert("Vui lòng chọn ít nhất một tài khoản để kích hoạt.");
            return;
        }
        document.getElementById("confirmActivateModal").style.display = "block";
    }

    function closeConfirmActivateModal() {
        document.getElementById("confirmActivateModal").style.display = "none";
    }

    function submitActivate() {
        document.getElementById("activateForm").submit();
    }

    

    function filterTable() {
        const searchText = document.getElementById("searchInput").value.toLowerCase();
        const roleFilter = document.getElementById("roleFilter").value.toLowerCase();
        const statusFilter = document.getElementById("statusFilter").value.toLowerCase();
        const rows = document.querySelectorAll(".user-row-item");

        rows.forEach(row => {
            const name = row.querySelector(".cell-name").innerText.toLowerCase();
            const email = row.querySelector(".cell-email").innerText.toLowerCase();
            const role = row.getAttribute("data-role");
            const status = row.getAttribute("data-status");

            const matchesSearch = name.includes(searchText) || email.includes(searchText);
            const matchesRole = roleFilter === "" || role === roleFilter;
            const matchesStatus = statusFilter === "" || status === statusFilter;

            if (matchesSearch && matchesRole && matchesStatus) row.classList.remove("row-hidden");
            else row.classList.add("row-hidden");
        });
    }

    function showActivateModal() { document.getElementById("activateModal").style.display = "block"; }
    function hideActivateModal() { document.getElementById("activateModal").style.display = "none"; }

    function filterActivateTable() {
        const input = document.getElementById("activateSearch").value.toLowerCase();
        const rows = document.querySelectorAll(".activate-row-item");
        rows.forEach(row => {
            const text = row.innerText.toLowerCase();
            if (text.includes(input)) row.style.display = "";
            else {
                row.style.display = "none";
                row.querySelector(".activate-checkbox").checked = false;
            }
        });
    }

    function toggleActivateAll() {
        const visibleCheckboxes = document.querySelectorAll('.activate-row-item:not([style*="display: none"]) .activate-checkbox');
        if (visibleCheckboxes.length === 0) return;
        const allChecked = Array.from(visibleCheckboxes).every(cb => cb.checked);
        visibleCheckboxes.forEach(cb => cb.checked = !allChecked);
    }

    function enableDeleteMode() {
        document.getElementById("btnOpenActivate").style.display = "none";
        document.getElementById("btnCreate").style.display = "none";
        document.getElementById("btnDeleteMode").style.display = "none";
        document.getElementById("btnChooseAll").style.display = "inline-block";
        document.getElementById("btnConfirmDelete").style.display = "inline-block";
        document.getElementById("btnCancelMode").style.display = "inline-block";
        document.querySelectorAll('.col-checkbox').forEach(el => el.style.display = 'table-cell');
        document.querySelectorAll('.col-action').forEach(el => el.style.display = 'none');
    }

    function disableDeleteMode() { location.reload(); }

    function toggleSelectAll() {
        const visibleCB = document.querySelectorAll('.user-row-item:not(.row-hidden) .user-checkbox');
        const allChecked = Array.from(visibleCB).every(cb => cb.checked);
        visibleCB.forEach(cb => cb.checked = !allChecked);
    }

    function openConfirmModal() {
        if (document.querySelectorAll('.user-checkbox:checked').length === 0) {
            alert("Please select at least one account to proceed."); return;
        }
        document.getElementById("confirmDeleteModal").style.display = "block";
    }

    function closeConfirmModal() { document.getElementById("confirmDeleteModal").style.display = "none"; }
    function submitDelete() { document.getElementById("deleteForm").submit(); }
    
    function showCreateForm() {
    document.getElementById("userForm").reset();
    // Đảm bảo action dẫn đến Servlet tạo User của bạn
    document.getElementById("userForm").action = "${pageContext.request.contextPath}/CreateUser"; 
    
    document.getElementById("modalTitle").innerText = "Create New Account";
    document.getElementById("emailGroup").style.display = "block";
    document.getElementById("passwordGroup").style.display = "block";
    document.getElementById("userModal").style.display = "block";
}

    function showEditForm(id, name, email, role, phone) {
        document.getElementById("userForm").action = "${pageContext.request.contextPath}/UpdateUser"; 
        document.getElementById("editUserId").value = id;
        document.getElementById("editUserName").value = name;
        document.getElementById("editUserEmail").value = email;
        document.getElementById("editRole").value = role;
        document.getElementById("editPhone").value = (phone && phone !== 'null') ? phone : "";
        document.getElementById("passwordGroup").style.display = "none";
        document.getElementById("modalTitle").innerText = "Update User";
        document.getElementById("userModal").style.display = "block";
    }

    function hideModal() { document.getElementById("userModal").style.display = "none"; }

    window.onclick = function(event) {
        if (event.target.className === "modal") {
            event.target.style.display = "none";
        }
    }
    function filterTable() {
    const searchText = document.getElementById("searchInput").value.toLowerCase();
    const roleFilter = document.getElementById("roleFilter").value.toLowerCase();
    const rows = document.querySelectorAll(".user-row-item");

    rows.forEach(row => {
        const name = row.querySelector(".cell-name").innerText.toLowerCase();
        const email = row.querySelector(".cell-email").innerText.toLowerCase();
        const role = row.getAttribute("data-role");

        const matchesSearch = name.includes(searchText) || email.includes(searchText);
        const matchesRole = roleFilter === "" || role === roleFilter;

        // Chỉ lọc theo Tìm kiếm và Quyền (Role)
        if (matchesSearch && matchesRole) {
            row.classList.remove("row-hidden");
        } else {
            row.classList.add("row-hidden");
        }
    });
}
</script>
<style>
    .pagination-container { margin-top: 20px; display: flex; justify-content: center; gap: 5px; align-items: center; }
    .page-btn { padding: 8px 14px; border: 1px solid #ddd; text-decoration: none; color: #007bff; border-radius: 5px; transition: 0.3s; }
    .page-btn.active { background-color: #007bff; color: white; border-color: #007bff; }
    .page-btn.disabled { color: #ccc; pointer-events: none; background: #f9f9f9; }
</style>

<div class="pagination-container">
    <a href="AdminUser?page=${currentPage - 1}" class="page-btn ${currentPage == 1 ? 'disabled' : ''}">Precious</a>

    <c:set var="begin" value="${currentPage - 2}" />
    <c:if test="${begin < 1}"> <c:set var="begin" value="1" /> </c:if>
    
    <c:set var="end" value="${begin + 4}" />
    <c:if test="${end > totalPages}">
        <c:set var="end" value="${totalPages}" />
        <c:set var="begin" value="${end - 4}" />
        <c:if test="${begin < 1}"> <c:set var="begin" value="1" /> </c:if>
    </c:if>

    <c:forEach begin="${begin}" end="${end}" var="i">
        <a href="AdminUser?page=${i}" class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
    </c:forEach>

    <a href="AdminUser?page=${currentPage + 1}" class="page-btn ${currentPage == totalPages ? 'disabled' : ''}">Next</a>
</div>


</body>
</html>