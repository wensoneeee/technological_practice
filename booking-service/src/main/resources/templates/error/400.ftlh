<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="400 - Плохой запрос">
    <div class="error-container">
        <h1 class="error-code">400</h1>
        <h2 class="error-title">Некорректный запрос</h2>
        <p class="error-message">Сервер не смог распознать ваш запрос из-за недопустимых символов или ошибки в данных.</p>
        <a href="/home" class="error-btn">На главную</a>
    </div>
</@layout.mainLayout>