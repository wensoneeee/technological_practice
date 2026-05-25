<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
</head>
<body>

<h2>Вход в систему</h2>

<form id="loginForm">
    <div>
        <label>Email:</label>
        <input type="email" id="email" required>
    </div>
    <div>
        <label>Пароль:</label>
        <input type="password" id="password" required>
    </div>
    <button type="submit">Войти</button>
</form>

<div id="message"></div>

<script>
    const GATEWAY_URL = 'http://localhost:8080';

    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const messageDiv = document.getElementById('message');

        try {
            const response = await fetch(`${GATEWAY_URL}/api/v1/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (response.ok) {
                messageDiv.innerText = 'Успешно!';

                // Сохраняем в LocalStorage для удобства
                localStorage.setItem('accessToken', data.accessToken);
                if(data.refreshToken) {
                    localStorage.setItem('refreshToken', data.refreshToken);
                }

                // Перенаправление на корень вашего гейтвея
                setTimeout(() => {
                    window.location.href = `${GATEWAY_URL}/`;
                }, 500);

            } else {
                messageDiv.innerText = data.message || 'Ошибка входа';
            }
        } catch (error) {
            messageDiv.innerText = 'Ошибка соединения с сервером';
            console.error(error);
        }
    });
</script>

</body>
</html>