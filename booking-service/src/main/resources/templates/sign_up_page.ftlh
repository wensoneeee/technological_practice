<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body class="auth-body">
<div class="auth-card">
    <h2>Регистрация</h2>
    <form action="/register" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <input name="name" type="text" class="auth-input" placeholder="Ваше имя" required>
        <input name="email" type="email" class="auth-input" placeholder="Email" required>
        <input name="password" type="password" class="auth-input" placeholder="Пароль" required>

        <#if errors??>
            <div class="auth-error">
                <#list errors as e><p style="margin: 0;">${e}</p></#list>
            </div>
        </#if>

        <input type="submit" value="Зарегистрироваться" class="btn" style="width: 100%;">
    </form>
    <div style="margin-top: 15px; font-size: 0.9rem;">
        Уже есть аккаунт? <a href="/login" class="auth-link">Войти</a>
    </div>
</div>
</body>
</html>