<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>MotoGo | Sign up</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css">
    </head>
    <body>
        <div class="container">

            <!-- Left: Sign up form -->
            <div class="form-section">
                <div class="form-box">
                    <h1 class="brand">MotoGo</h1>
                    <h2>Sign up</h2>
                    <p class="subtext">
                        Have an account already?
                        <a href="${pageContext.request.contextPath}/templates/login.jsp">Login instead</a>
                    </p>

                    <form action="${pageContext.request.contextPath}/signup" method="post">
                        <label>Username</label>
                        <input type="text" name="username" placeholder="yourusername" required />

                        <label>Email address</label>
                        <input type="email" name="email" placeholder="you@email.com" required />

                        <label>Mobile number</label>
                        <input type="tel" name="phone" placeholder="+84 912 345 678" required />

                        <label>Password</label>
                        <input type="password" name="password" required />

                        <label>Confirm your password</label>
                        <input type="password" name="confirmPassword" required />

                        <p style="color:red">${error}</p>

                        <button type="submit">Sign up</button>
                    </form>

                </div>
            </div>

            <!-- Right: Image -->
            <div class="image-section">
                <img src="${pageContext.request.contextPath}/images/hero.jpg" alt="Motorcycle riders" />
            </div>

        </div>
    </body>
</html>
