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
    <form id="loginForm">
        <input id="email" type="email" class="auth-input" placeholder="Email" required>
        <input id="password" type="password" class="auth-input" placeholder="Пароль" required>

        <div id="authError" class="auth-error" style="display: none; margin-bottom: 10px;"></div>

        <input type="submit" value="Войти" class="btn" style="width: 100%;">
    </form>
    <div style="margin-top: 15px; font-size: 0.9rem;">
        Нет аккаунта?
        <a href="/sign-up" class="auth-link">Зарегистрироваться</a>
    </div>
</div>

<script>
    const GATEWAY_URL = "${GATEWAY_URL!'http://localhost:8080'}";

    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorDiv = document.getElementById('authError');

        errorDiv.style.display = 'none';

        try {
            const response = await fetch(`${GATEWAY_URL}/api/v1/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();

                localStorage.setItem('accessToken', data.accessToken);
                if (data.refreshToken) {
                    localStorage.setItem('refreshToken', data.refreshToken);
                }

                window.location.href = `${GATEWAY_URL}/home`;
            } else {
                errorDiv.innerText = 'Неверный логин или пароль';
                errorDiv.style.display = 'block';
            }
        } catch (error) {
            errorDiv.innerText = 'Ошибка соединения с сервером';
            errorDiv.style.display = 'block';
            console.error(error);
        }
    });
</script>
</body>
</html>