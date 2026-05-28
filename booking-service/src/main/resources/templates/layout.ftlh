<#macro mainLayout title="Заголовок">
    <!doctype html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>${title}</title>
        <link rel="stylesheet" type="text/css" href="/css/style.css">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    </head>
    <body>
    <header>
        <nav>
            <a href="/home">Главная</a>
            <a href="/cart">Корзина</a>
            <a href="/profile">Профиль</a>
            <#if userRole??>
                <#if userRole == 'ROLE_ADMIN'>
                    <a href="/admin">Админ</a>
                </#if>
                <button id="logout-btn" class="btn">Выйти</button>
            <#else>
                <a href="/sign-in">Войти</a>
                <a href="/register">Регистрация</a>
            </#if>
        </nav>
    </header>
    <main>
        <#nested>
    </main>
    <script>
        document.getElementById('logout-btn')?.addEventListener('click', function(event) {
            event.preventDefault();

            localStorage.removeItem('accessToken');
            document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            fetch(`${GATEWAY_URL!'http://localhost:8080'}/api/v1/auth/logout`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(() => {
                    window.location.href = `/sign-in`;
                })
                .catch(err => {
                    console.error("Ошибка при логауте:", err);
                    window.location.href = `/sign-in`;
                });
        });
    </script>
    </body>
    </html>
</#macro>