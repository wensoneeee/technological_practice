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
                <#if userRole == 'ADMIN'>
                    <a href="/admin">Админ</a>
                </#if>
                <form action="/logout" method="post" style="display:inline;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <button type="submit" class="btn">Выйти</button>
                </form>
            <#else>
                <a href="/login">Войти</a>
                <a href="/register">Регистрация</a>
            </#if>
        </nav>
    </header>
    <main>
        <#nested>
    </main>
    </body>
    </html>
</#macro>