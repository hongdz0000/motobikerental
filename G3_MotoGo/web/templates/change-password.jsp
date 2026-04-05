<link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css"> 
<div class="change-password-container">
    <form action="${pageContext.request.contextPath}/changepassword" method="post">

        <div class="form-group">
            <label>Old Password</label>
            <input type="password" name="oldPassword" required>
        </div>

        <div class="form-group">
            <label>New Password</label>
            <input type="password" name="newPassword" required>
        </div>

        <div class="form-group">
            <label>Confirm Password</label>
            <input type="password" name="confirmPassword" required>
        </div>

        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>

        <c:if test="${not empty success}">
            <p style="color:green">${success}</p>
        </c:if>

        <button type="submit">Change Password</button>

    </form>
</div>