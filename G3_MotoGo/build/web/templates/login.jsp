<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>MotoGo | Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!-- Lưu ý: Nếu file nằm trong /templates/, dùng ../ để ra ngoài lấy static -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css">
        <style>
            .alert { padding: 12px; border-radius: 4px; margin-bottom: 20px; font-size: 14px; text-align: center; }
            .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
            .alert-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        </style>
    </head>
    <body>
        <div class="container">

            <div class="form-section">
                <div class="form-box">
                    <h1 class="brand">MotoGo</h1>
                    <h2>Welcome back</h2>

                    <!-- 1. Thông báo đăng ký thành công (nhận từ signup.jsp?success=1) -->
                    <c:if test="${param.success == '1'}">
                        <div class="alert alert-success">
                            🎉 Đăng ký thành công! Mời bạn đăng nhập.
                        </div>
                    </c:if>

                    <!-- 2. Thông báo lỗi đăng nhập (nhận từ biến "err" trong LoginServlet) -->
                    <c:if test="${not empty err}">
                        <div class="alert alert-danger">
                            ${err}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/Login" method="post">
                        <label>Email address</label>
                        <input type="email" name="email" required placeholder="example@mail.com" />

                        <div class="password-row">
                            <label>Password</label>
                        </div>

                        <div class="password-input" style="position: relative;">
                            <input type="password" name="password" id="password" required>
                            <span class="toggle-password" onclick="togglePassword()" 
                                  style="position: absolute; right: 10px; top: 10px; cursor: pointer;">👁</span>
                        </div>

                        <button type="submit">Login</button>
                    </form>

                    <p class="subtext center">
                        Don't have an account?
                        <a href="signup.jsp">Sign up</a>
                    </p>
                </div>
            </div>

            <div class="image-section">
                <img src="${pageContext.request.contextPath}/images/vietnam.jpeg" alt="Motorcycle rider" />
            </div>

        </div>

        <script>
            function togglePassword() {
                const password = document.getElementById("password");
                if (password.type === "password") {
                    password.type = "text";
                } else {
                    password.type = "password";
                }
            }
        </script>
    </body>
</html>
