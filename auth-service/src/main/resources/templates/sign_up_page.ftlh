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
    <form id="registerForm">
        <input id="name" type="text" class="auth-input" placeholder="Ваше имя" required>
        <input id="email" type="email" class="auth-input" placeholder="Email" required>
        <input id="password" type="password" class="auth-input" placeholder="Пароль" required>

        <div id="registerError" class="auth-error" style="display: none; margin-bottom: 10px;"></div>

        <input type="submit" value="Зарегистрироваться" class="btn" style="width: 100%;">
    </form>
    <div style="margin-top: 15px; font-size: 0.9rem;">
        Уже есть аккаунт?
        <a href="/sign-in" class="auth-link">Войти</a>
    </div>
</div>

<script>
    const GATEWAY_URL = "${GATEWAY_URL!'http://localhost:8080'}";

    document.getElementById('registerForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorDiv = document.getElementById('registerError');

        errorDiv.style.display = 'none';

        try {
            const response = await fetch(`${GATEWAY_URL}/api/v1/auth/sign-up`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, email, password })
            });

            if (response.ok) {
                const data = await response.json();

                localStorage.setItem('accessToken', data.accessToken);
                if (data.refreshToken) {
                    localStorage.setItem('refreshToken', data.refreshToken);
                }

                window.location.href = `${GATEWAY_URL}/sign-in`;
            } else {
                errorDiv.innerText = 'Некорректные данные или этот Email уже зарегистрирован';
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