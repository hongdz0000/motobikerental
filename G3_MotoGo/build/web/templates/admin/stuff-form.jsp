<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - MotoGo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/admin.css">
</head>
<body>

<%@ include file="../navbar.jsp" %>

<div class="admin-container">
    <h2>${item != null ? "CHỈNH SỬA" : "THÊM MỚI"} ĐỒ PHƯỢT</h2>
    <p class="admin-subtitle">Vui lòng nhập đầy đủ thông tin bên dưới</p>

    <div class="admin-table-card">
    <!-- THÊM enctype để upload file -->
    <form action="stuff" method="post" enctype="multipart/form-data" class="admin-form-card">
        <input type="hidden" name="id" value="${item.stuffId}" />
        
        <!-- Hidden field để giữ đường dẫn ảnh cũ nếu không upload ảnh mới -->
        <input type="hidden" name="existingIcon" value="${item.stuffIcon}" />

        <div class="form-group">
            <label>Tên đồ phượt</label>
            <input type="text" name="name" value="${item.stuffName}" required>
        </div>

        <div class="form-group">
            <label>Giá thuê/ngày</label>
            <input type="number" step="0.01" name="price" value="${item.basePricePerDay}" required>
        </div>

        <div class="form-group">
            <label>Chọn Icon từ máy tính</label>
            <input type="file" name="imageFile" id="imageFile" accept="image/*" onchange="previewImage(this)">
            
            <div style="margin-top: 10px;">
                <p>Preview:</p>
                <img id="imgPreview" 
                     src="${(item.stuffIcon != null && !item.stuffIcon.isEmpty()) ? item.stuffIcon : 'images/no-image.png'}" 
                     style="max-width: 150px; border: 1px solid #ddd; padding: 5px;">
            </div>
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" class="btn-success">Lưu thay đổi</button>
            <a href="stuff" class="btn-danger" style="text-decoration: none; padding: 10px;">Hủy</a>
        </div>
    </form>
</div>

<script>
function previewImage(input) {
    const preview = document.getElementById('imgPreview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
        }
        reader.readAsDataURL(input.files[0]);
    }
}
</script>
</div>

</body>
</html>