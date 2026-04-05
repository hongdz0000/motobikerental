<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>${bike.name} | Thuê xe máy MotoGo</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bike-detail.css">
        <style>
            /* Tinh chỉnh thêm để giao diện chuyên nghiệp hơn */
            .total-summary {
                border-top: 1px dashed #ddd;
                padding-top: 15px;
                margin-top: 15px;
            }
            .total-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 8px;
            }
            .final-price {
                color: #e44d26;
                font-weight: bold;
                font-size: 1.5rem;
            }
            .addon-card {
                cursor: pointer;
                border: 1px solid #eee;
                border-radius: 8px;
                padding: 12px; /* Increased padding for better tap targets */
                display: flex; /* Ensure this is the only display property for this class */
                align-items: center;
                transition: 0.2s;
                width: 100%; /* Ensure it stretches to fill the grid cell */
                box-sizing: border-box; /* Includes padding in the width calculation */
            }
            .addon-card:hover {
                border-color: #e44d26;
                background-color: #fff9f8;
            }
            .addon-checkbox {
                margin-right: 10px;
            }
            /* Khống chế kích thước icon phụ kiện */
            .addon-icon {
                width: 40px;          /* Cố định chiều rộng */
                height: 40px;         /* Cố định chiều cao */
                object-fit: contain;  /* Giữ nguyên tỉ lệ ảnh không bị méo */
                margin-right: 12px;   /* Khoảng cách với chữ */
                flex-shrink: 0;       /* Ngăn icon bị bóp méo khi chữ quá dài */
            }

            /* Căn chỉnh hàng ngang cho đẹp */
            .addon-content {
                display: flex;
                justify-content: space-between; /* Đẩy giá tiền sang bên phải */
                align-items: center;            /* Căn giữa theo chiều dọc */
                width: 100%;
                flex-grow: 1;
            }

            .addon-left {
                display: flex;
                align-items: center;
                overflow: hidden; /* Chống tràn chữ */
            }

            .addon-card {
                display: flex;
                align-items: center;
                padding: 12px;
                gap: 10px; /* Khoảng cách giữa checkbox và nội dung */
            }

            .addon-name {
                white-space: nowrap;     /* Không cho chữ xuống dòng */
                overflow: hidden;        /* Ẩn phần chữ thừa */
                text-overflow: ellipsis; /* Hiện dấu ... nếu tên quá dài */
                font-weight: 500;
            }

            .addon-price {
                white-space: nowrap;     /* Giữ giá tiền trên 1 dòng */
                font-size: 0.9rem;
                color: #666;
                margin-left: 10px;
            }
        </style>
    </head>
    <body>

        <jsp:include page="navbar.jsp" />

        <main class="page">
            <!-- Form gửi dữ liệu sang trang Thanh toán -->
            <form action="Checkout" method="POST" id="bookingForm">
                <!-- Inputs ẩn lưu thông tin xe -->
                <input type="hidden" name="bikeId" value="${bike.id}">
                <input type="hidden" id="bikeBasePrice" value="${bike.currentPricePerDay}">

                <div class="layout">

                    <!-- BÊN TRÁI: THÔNG TIN CHI TIẾT XE -->
                    <section class="bike-info">

                        <div class="image-wrapper">
                            <img src="${pageContext.request.contextPath}/${bike.bikeIcon}" alt="${bike.name}">
                        </div>

                        <div class="bike-header">
                            <h1>${bike.name}</h1>
                            <span class="category">${bike.brand} — Đời xe ${bike.manufactureYear}</span>
                        </div>

                        <div class="description">
                            <p>${bike.description != null ? bike.description : "Dòng xe đời mới, vận hành êm ái, tiết kiệm xăng, phù hợp cho cả di chuyển trong phố và đi phượt."}</p>
                        </div>

                        <div class="specs">
                            <div class="specs-grid">
                                <div><strong>Dung tích:</strong> ${bike.engineSize}cc</div>
                                <div><strong>Loại xe:</strong> ${bike.transmission}</div>
                                <div><strong>Năm SX:</strong> ${bike.manufactureYear}</div>
                            </div>
                        </div>

                        <!-- 1. ĐÃ BAO GỒM TRONG GIÁ THUÊ -->
                        <section class="included">
                            <h2>Giá thuê đã bao gồm</h2>
                            <ul>
                                <li>✔ Bảo hiểm trách nhiệm dân sự</li>
                                <li>✔ 02 Mũ bảo hiểm tiêu chuẩn</li>
                                    <c:forEach items="${listStuff}" var="s">
                                        <c:if test="${s.basePricePerDay == 0}">
                                        <li>✔ ${s.stuffName} (Miễn phí)</li>
                                        </c:if>
                                    </c:forEach>
                            </ul>
                        </section>

                        <!-- 2. DỊCH VỤ THÊM (ADD-ONS) -->
                        <section class="addons-section">
                            <span class="eyebrow">Trải nghiệm tiện nghi hơn</span>
                            <h2>Dịch vụ & Phụ kiện thêm</h2>
                            <div class="addons-grid">
                                <c:forEach items="${listStuff}" var="s">
                                    <c:if test="${s.basePricePerDay > 0}">
                                        <label class="addon-card">
                                            <input type="checkbox" name="selectedStuff" value="${s.stuffId}" 
                                                   data-price="${s.basePricePerDay}" class="addon-checkbox">
                                            <div class="addon-content">
                                                <div class="addon-left">
                                                    <img class="addon-icon" src="${pageContext.request.contextPath}/${s.stuffIcon}" alt="${s.stuffName}">

                                                    <span class="addon-name">${s.stuffName}</span>
                                                </div>
                                                <div class="addon-price">
                                                    +<fmt:formatNumber value="${s.basePricePerDay}" pattern="#,###"/> đ/ngày
                                                </div>
                                            </div>
                                        </label>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </section>

                        <!-- 3. CHÍNH SÁCH THUÊ XE (VIỆT HÓA) -->
                        <section class="policy-section">
                            <span class="eyebrow">Quy định chung</span>
                            <h2>Chính sách tại MotoGo</h2>
                            <ul class="policy-list">
                                <li><span class="policy-icon">💰</span><p><strong>Đặt cọc trước 15%</strong> để giữ xe, phần còn lại thanh toán khi nhận xe.</p></li>
                                <li><span class="policy-icon">⏰</span><p>Hủy lịch miễn phí trước <strong>48 giờ</strong> nhận xe.</p></li>
                                <li><span class="policy-icon">🪪</span><p>Yêu cầu <strong>Bằng lái xe (A1/A2)</strong> và <strong>CCCD/Passport</strong> bản gốc.</p></li>
                                <li><span class="policy-icon">💳</span><p>Cần để lại <strong>Tiền cọc (1-5 triệu)</strong> hoặc tài sản tương đương khi nhận xe.</p></li>
                                <li><span class="policy-icon">🛣️</span><p>Giới hạn: <strong>400 km/ngày</strong>. Phí phát sinh: 2.000đ/km.</p></li>
                            </ul>
                        </section>
                    </section>

                    <!-- BÊN PHẢI: THẺ ĐẶT XE (STICKY) -->
                    <aside class="booking-card">
                        <div class="price">
                            <span class="days">Giá thuê gốc</span>
                            <strong>
                                <fmt:formatNumber value="${bike.currentPricePerDay}" pattern="#,###"/> đ
                            </strong>
                            <small>/ngày</small>
                        </div>

                        <div class="field">
                            <label>Ngày nhận xe</label>
                            <input type="date" name="startDate" id="startDate" class="booking-input" required>
                        </div>

                        <div class="field">
                            <label>Ngày trả xe</label>
                            <input type="date" name="endDate" id="endDate" class="booking-input" required>
                        </div>

                        <div class="time-row">
                            <div class="time-field">
                                <label>Giờ nhận</label>
                                <input type="time" name="startTime" value="08:00" class="booking-input">
                            </div>
                            <div class="time-field">
                                <label>Giờ trả</label>
                                <input type="time" name="endTime" value="20:00" class="booking-input">
                            </div>
                        </div>

                        <!-- TÓM TẮT CHI PHÍ TẠM TÍNH -->
                        <div class="total-summary">
                            <div class="total-row">
                                <span>Số ngày thuê:</span>
                                <span id="rentalDays">0 ngày</span>
                            </div>
                            <div class="total-row">
                                <span><strong>Tổng cộng:</strong></span>
                                <span class="final-price" id="displayTotal">
                                    <fmt:formatNumber value="${bike.currentPricePerDay}" pattern="#,###"/> đ
                                </span>
                            </div>
                        </div>

                        <button type="submit" class="checkout-btn">TIẾP TỤC ĐẶT XE</button>

                        <div class="benefits">
                            <p>✔ Hỗ trợ sự cố đường dây nóng 24/7</p>
                            <p>✔ Xe sạch, đầy xăng khi giao</p>
                            <p>✔ Tặng kèm áo mưa mỏng</p>
                        </div>
                    </aside>
                </div>
            </form>
        </main>

        <jsp:include page="footer.jsp" />

        <!-- JS TÍNH TOÁN TỔNG TIỀN THEO NGÀY VÀ ADD-ONS -->
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const basePrice = parseFloat(document.getElementById('bikeBasePrice').value) || 0;
                const checkboxes = document.querySelectorAll('.addon-checkbox');
                const displayTotal = document.getElementById('displayTotal');
                const displayDays = document.getElementById('rentalDays');
                const startDateInput = document.getElementById('startDate');
                const endDateInput = document.getElementById('endDate');

                // Chặn ngày quá khứ
                const today = new Date().toISOString().split('T')[0];
                startDateInput.setAttribute('min', today);
                endDateInput.setAttribute('min', today);

                function calculateTotal() {
                    let days = 0;
                    const start = new Date(startDateInput.value);
                    const end = new Date(endDateInput.value);

                    if (startDateInput.value && endDateInput.value && end >= start) {
                        const diffTime = Math.abs(end - start);
                        // Tính số ngày, ít nhất là 1
                        days = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                        if (days === 0)
                            days = 1;
                    }

                    let extraPerDay = 0;
                    checkboxes.forEach(cb => {
                        if (cb.checked) {
                            extraPerDay += parseFloat(cb.getAttribute('data-price')) || 0;
                        }
                    });

                    // Tổng = (Giá xe + Phụ kiện) * Số ngày
                    // Nếu chưa chọn ngày, hiển thị giá của 1 ngày mặc định
                    const multiplier = (days > 0) ? days : 1;
                    const total = (basePrice + extraPerDay) * multiplier;

                    // Hiển thị kết quả
                    if (displayDays) {
                        displayDays.innerText = (days > 0) ? days + " ngày" : "Chưa chọn ngày";
                    }
                    displayTotal.innerText = total.toLocaleString('vi-VN') + " đ";
                }

                // Gán sự kiện
                checkboxes.forEach(cb => cb.addEventListener('change', calculateTotal));
                [startDateInput, endDateInput].forEach(input => {
                    input.addEventListener('change', function () {
                        // Tự động đẩy ngày trả lên nếu ngày nhận lớn hơn ngày trả hiện tại
                        if (input.id === 'startDate') {
                            endDateInput.setAttribute('min', startDateInput.value);
                        }
                        calculateTotal();
                    });
                });

                // Chạy khởi tạo giá trị ban đầu
                calculateTotal();
            });
        </script>
    </body>
</html>