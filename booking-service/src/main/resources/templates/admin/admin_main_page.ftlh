<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Панель управления">
    <h2>Админ-панель</h2>
    <div class="admin-menu-grid">
        <a href="/admin/event" class="btn">Управление мероприятиями</a>
        <a href="/admin/category" class="btn">Управление категориями</a>
        <a href="/admin/user" class="btn">Управление пользователями</a>
        <a href="/admin/feedback" class="btn">Управление отзывами</a>
    </div>
</@layout.mainLayout>