<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="403 - Доступ запрещен">
    <div class="error-container">
        <h1 class="error-code">403</h1>
        <h2 class="error-title">Доступ ограничен</h2>
        <p class="error-message">У вас недостаточно прав для просмотра этой страницы. Возможно, нужно войти как администратор.</p>
        <a href="/home" class="error-btn">Вернуться на главную</a>
    </div>
</@layout.mainLayout>