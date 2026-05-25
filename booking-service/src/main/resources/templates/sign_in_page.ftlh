<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body class="auth-body">
<div class="auth-card">
    <h2>Вход</h2>
    <form action="/login" method="post">
        <input type="hidden" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <input name="email" type="email" class="auth-input" placeholder="Email" required>
        <input name="password" type="password" class="auth-input" placeholder="Пароль" required>

        <#if error??><div class="auth-error">Неверный логин или пароль</div></#if>

        <input type="submit" value="Войти" class="btn" style="width: 100%;">
    </form>
    <div style="margin-top: 15px; font-size: 0.9rem;">
        Нет аккаунта? <a href="/register" class="auth-link">Зарегистрироваться</a>
    </div>
</div>
</body>
</html>