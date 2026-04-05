<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - MotoGo</title>
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com">
    
    <style>
        :root {
            --primary-color: #27ae60;
            --primary-hover: #219150;
            --bg-shadow: 0 4px 20px rgba(0,0,0,0.08);
            --border-color: #e0e0e0;
        }

        /* Reset & Layout chuẩn cho Admin */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            overflow: hidden; /* Chặn cuộn toàn trang */
        }

        .admin-wrapper {
            display: flex;
            flex-direction: column;
            height: 100vh;
        }

        .admin-container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            box-sizing: border-box;
        }

        /* Card chứa form */
        .admin-card {
            background: #fff;
            width: 100%;
            max-width: 850px;
            max-height: 85vh; /* Giới hạn chiều cao card */
            border-radius: 12px;
            box-shadow: var(--bg-shadow);
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }

        /* Header cố định */
        .card-header {
            padding: 20px 25px;
            border-bottom: 1px solid var(--border-color);
            background: #fafafa;
        }

        .card-header h2 { margin: 0; color: #333; font-size: 22px; }
        .card-header p { margin: 5px 0 0; color: #888; font-size: 13px; }

        /* Vùng nội dung có thể cuộn */
        .card-body {
            padding: 25px;
            overflow-y: auto; /* Chỉ cho phép cuộn ở đây */
            flex: 1;
        }

        /* Custom Scrollbar cho mượt */
        .card-body::-webkit-scrollbar { width: 6px; }
        .card-body::-webkit-scrollbar-thumb { background: #ccc; border-radius: 10px; }

        /* Grid hệ thống */
        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }

        .form-group { margin-bottom: 5px; }
        .full-width { grid-column: span 2; }

        .form-group label {
            display: block;
            font-weight: 600;
            font-size: 13px;
            margin-bottom: 8px;
            color: #444;
        }

        .form-group label i { margin-right: 8px; color: var(--primary-color); }

        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
            transition: 0.2s;
        }

        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(39, 174, 96, 0.1);
        }

        /* Khu vực ảnh */
        .image-section {
            display: flex;
            align-items: center;
            gap: 20px;
            background: #f9f9f9;
            padding: 15px;
            border-radius: 8px;
            border: 1px dashed #ccc;
        }

        #preview {
            width: 140px;
            height: 90px;
            object-fit: cover;
            border-radius: 6px;
            border: 1px solid #ddd;
            background: #eee;
        }

        /* Footer cố định chứa nút bấm */
        .card-footer {
            padding: 15px 25px;
            border-top: 1px solid var(--border-color);
            background: #fff;
            display: flex;
            justify-content: flex-end;
        }

        .btn-save {
            background: var(--primary-color);
            color: white;
            border: none;
            padding: 12px 40px;
            border-radius: 6px;
            font-weight: 600;
            cursor: pointer;
            font-size: 15px;
            transition: 0.2s;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .btn-save:hover { background: var(--primary-hover); transform: translateY(-1px); }

        /* Responsive khi thu nhỏ màn hình/Zoom */
        @media (max-width: 600px) {
            .form-grid { grid-template-columns: 1fr; }
            .admin-card { max-height: 95vh; }
        }
    </style>
</head>
<body>

    <div class="admin-wrapper">
        <%@ include file="../navbar.jsp" %>

        <div class="admin-container">
            <div class="admin-card">
                
                <div class="card-header">
                    <h2><i class="fa-solid fa-pen-to-square"></i> Cấu hình thông tin xe</h2>
                    <p>Mã định danh hệ thống: <strong>#${bike.id}</strong></p>
                </div>

                <form action="AdminBike" method="post" enctype="multipart/form-data" style="display: contents;">
                    <div class="card-body">
                        <input type="hidden" name="id" value="${bike.id}" />
                        <input type="hidden" name="existingImage" value="${bike.bikeIcon}" />

                        <div class="form-grid">
                            <div class="form-group">
                                <label><i class="fa-solid fa-bicycle"></i> Tên xe</label>
                                <input type="text" name="name" value="${bike.name}" required>
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-copyright"></i> Hãng xe</label>
                                <input type="text" name="brand" value="${bike.brand}">
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-money-bill"></i> Giá thuê/ngày (VNĐ)</label>
                                <input type="number" name="pricePerDay" value="${bike.currentPricePerDay}" required>
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-toggle-on"></i> Trạng thái</label>
                                <select name="status">
                                    <option value="available" ${bike.status == 'available' ? 'selected' : ''}>Sẵn sàng (Available)</option>
                                    <option value="rented" ${bike.status == 'rented' ? 'selected' : ''}>Đang thuê (Rented)</option>
                                    <option value="maintenance" ${bike.status == 'maintenance' ? 'selected' : ''}>Bảo trì (Maintenance)</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-gauge"></i> Phân khối</label>
                                <input type="text" name="engineSize" value="${bike.engineSize}" placeholder="VD: 150cc">
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-gear"></i> Loại số</label>
                                <select name="transmission">
                                    <option value="Manual" ${bike.transmission == 'Manual' ? 'selected' : ''}>Xe số / Côn tay</option>
                                    <option value="Automatic" ${bike.transmission == 'Automatic' ? 'selected' : ''}>Xe ga</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-calendar"></i> Năm sản xuất</label>
                                <input type="number" name="manufactureYear" value="${bike.manufactureYear}">
                            </div>

                            <div class="form-group">
                                <label><i class="fa-solid fa-image"></i> Hình ảnh xe</label>
                                <input type="file" name="image" id="imageInput" accept="image/*" onchange="previewImage(this)">
                            </div>

                            <!-- Image Preview Area -->
                            <div class="form-group full-width">
                                <div class="image-section">
                                    <c:choose>
                                        <c:when test="${not empty bike.bikeIcon}">
                                            <img id="preview" src="${pageContext.request.contextPath}/${bike.bikeIcon}">
                                        </c:when>
                                        <c:otherwise>
                                            <img id="preview" src="#" style="display:none;">
                                            <span id="no-img-text" style="color: #aaa; font-size: 13px;">Chưa chọn ảnh</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <div style="font-size: 12px; color: #777;">
                                        <p style="margin: 0;"><strong>Preview ảnh:</strong> Tự động cập nhật khi chọn tệp mới.</p>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group full-width">
                                <label><i class="fa-solid fa-comment-dots"></i> Mô tả chi tiết</label>
                                <textarea name="description" rows="3" style="resize: vertical;">${bike.description}</textarea>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer">
                        <button type="button" onclick="window.history.back()" style="background:none; border:none; color:#888; cursor:pointer; margin-right: 20px;">Hủy bỏ</button>
                        <button type="submit" class="btn-save">
                            <i class="fa-solid fa-floppy-disk"></i> Cập nhật thông tin
                        </button>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <script>
        function previewImage(input) {
            const preview = document.getElementById('preview');
            const noImgText = document.getElementById('no-img-text');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    if(noImgText) noImgText.style.display = 'none';
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>
</html>
