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
                <a href="${GATEWAY_URL!'http://localhost:8080'}/api/v1/auth/logout" id="logout-btn" class="btn">Выйти</a>
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
        document.getElementById('logout-btn').addEventListener('click', function(event) {
            event.preventDefault();

            localStorage.removeItem('accessToken');
            document.cookie = "JWT=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            fetch(`http://localhost:8080/api/v1/auth/logout`, {
                method: 'GET'
            })
                .then(() => {
                    window.location.href = `http://localhost:8080/sign-in`;
                })
                .catch(err => {
                    console.error("Ошибка при логауте на бэкенде, всё равно редиректим:", err);
                    window.location.href = `http://localhost:8080/sign-in`;
                });
        });
    </script>
    </body>
    </html>
</#macro>