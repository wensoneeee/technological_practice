<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Ошибка">
    <div style="text-align: center; padding: 50px;">
        <h2>Произошла ошибка ${status}</h2>
        <p>${error}</p>
        <p>Мы уже сохранили информацию об этом в логах и скоро всё починим!</p>
        <a href="/home" class="btn">На главную</a>
    </div>
</@layout.mainLayout>