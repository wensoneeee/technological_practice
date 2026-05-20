<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="404 - Страница не найдена">
    <div class="error-container">
        <h1 class="error-code">404</h1>
        <h2 class="error-title">Упс! Страница не найдена</h2>
        <p class="error-message">Кажется, это мероприятие уже прошло или адрес был введен неверно.</p>
        <a href="/home" class="error-btn">Вернуться на главную</a>
    </div>
</@layout.mainLayout>