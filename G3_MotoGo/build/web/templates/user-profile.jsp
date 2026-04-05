<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Account Settings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/user-profile.css">
</head>
<body>

<!-- NAVBAR -->

    <jsp:include page="navbar.jsp" />

<main class="page">
    <section class="profile-container">

        <header class="page-header">
            <h1>Account Settings</h1>
            <p class="subtitle">
                Manage your profile information, passwords, and other settings.
            </p>
        </header>

        <hr>

        <!-- DRIVER INFORMATION -->
        <section class="section">
            <h2>Driver Information</h2>

            <div class="row">
        <div>
            <h3>Profile Picture</h3>
            <!-- Hiển thị ảnh hiện tại -->
            <img id="profilePreview" class="avatar" src="${profileUser.avatar}" alt="Profile" style="width:100px; height:100px; object-fit:cover;">
        </div>
        
        <!-- Form upload ảnh -->
        <form action="${pageContext.request.contextPath}/UploadAvatar" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="userId" value="${profileUser.userId}">
            <!-- Input file ẩn -->
            <input type="file" id="fileInput" name="avatarFile" accept="image/*" style="display: none;" onchange="previewImage(this)">
            
            <!-- Nút bấm giả để kích hoạt input file -->
            <button type="button" class="link-btn" onclick="document.getElementById('fileInput').click()">Change</button>
            
            <!-- Nút Save chỉ hiện khi đã chọn file mới -->
            <button type="submit" id="saveAvatarBtn" class="save-btn" style="display:none; margin-top:10px;">Save Photo</button>
        </form>
    </div>

           <div class="row">
    <div>
        <h3>Details</h3>
        <ul class="details">
            <!-- Sử dụng EL để lấy dữ liệu từ profileUser -->
            <li>👤 ${profileUser.userName}</li>

            <li>🪪 ID: ${profileUser.userId}</li>
            <li>📧 ${profileUser.userEmail}</li>

            <li>🛡️ Role: ${profileUser.role}</li>
        </ul>
    </div>
    <button class="update-link" onclick="toggleEditForm()">Update</button>
</div>
<div id="editProfileForm" class="edit-form">
    <!-- Bọc toàn bộ nội dung vào thẻ form -->
    <form action="${pageContext.request.contextPath}/Profile" method="POST">
        
        <!-- Truyền ngầm userID để Servlet biết đang cập nhật cho ai -->
        <input type="hidden" name="userId" value="${profileUser.userId}">

        <div class="form-grid">
            <div class="form-group">
                <label>NAME</label>
                <!-- Thêm thuộc tính name="userName" -->
                <input type="text" name="userName" value="${profileUser.userName}">
            </div>

            <div class="form-group">
                <label>EMAIL ADDRESS</label>
                <input type="email" value="${profileUser.userEmail}" disabled>
                <small>(Email cannot be changed)</small>
            </div>
<!--
            <div class="form-group">
                <label>CURRENT BALANCE</label>
                <input type="text" value="${profileUser.balance}" disabled>
            </div>
-->
            <div class="form-group">
                <label>PHONE NUMBER</label>
                <!-- Thêm thuộc tính name="phone" -->
                <input type="text" name="phone" placeholder="+84" value="${profileUser.phone}"> 
            </div>
        </div>

        <div class="form-actions">
            <button type="button" class="cancel-btn" onclick="toggleEditForm()">Cancel</button>
            <!-- Đổi thành type="submit" để gửi form -->
            <button type="submit" class="save-btn">Save changes</button>
        </div>
    </form>
</div>

        </section>

        <hr>

        <!-- LOGIN INFORMATION -->
        <section class="section">
            <h2>Login Information</h2>

            <div class="row">
                <div>
                    <h3>Password</h3>
                    <p class="muted">
                        Date joined Jan. 25, 2026, 3:04 p.m.
                    </p>
                </div>
                <a href="${pageContext.request.contextPath}/changepassword" class="link-btn">Change</a>
            </div>
        </section>

    </section>
    
<script>
function toggleEditForm() {
    const form = document.getElementById("editProfileForm");
    form.classList.toggle("open");

    if (form.classList.contains("open")) {
        form.scrollIntoView({ behavior: "smooth", block: "start" });
    }
}
function previewImage(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('profilePreview').src = e.target.result;
            document.getElementById('saveAvatarBtn').style.display = 'block'; // Hiện nút Save
        }
        reader.readAsDataURL(input.files[0]);
    }
}
</script>

</main>

</body>
</html>

