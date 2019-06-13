<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录首页</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
    <link rel="stylesheet" href="../static/css/login/style.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js"></script>
</head>
<div class="login">
    <h1>Login</h1>
    <form method="post" action="">
        <input type="text" name="username" placeholder="用户名" required="required"/>
        <input type="password" name="password" placeholder="密码" required="required"/>
        <button type="submit" class="btn btn-primary btn-block btn-large">登录</button>
    </form>
</div>
</body>
</html>