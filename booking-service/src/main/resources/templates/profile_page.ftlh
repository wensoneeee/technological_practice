<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Мой профиль">
    <h1>Личный кабинет</h1>
    <div class="flex-row">
        <div>
            <div class="profile-img-wrapper">
                <img src="/image/${user_data.imageId}">
            </div>
            <form action="/img/profile/update" method="post" enctype="multipart/form-data" class="mt-20">
                <input type="hidden" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
                <input type="file" name="file" class="form-control" required style="margin-bottom: 5px;">
                <input type="submit" value="Загрузить" class="btn">
            </form>
            <form action="/img/profile/delete" method="post" class="mt-20">
                <input type="hidden" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
                <input type="submit" value="Удалить фото" class="btn text-danger" style="background: #fee2e2;">
            </form>
        </div>
        <div class="info-box" style="flex-grow: 1;">
            <p><strong>ID:</strong> ${user_data.id}</p>
            <p><strong>Имя:</strong> ${user_data.name}</p>
            <p><strong>Email:</strong> ${user_data.email}</p>
            <p><strong>Роль:</strong> ${user_data.role}</p>
            <hr>
            <h4>Сменить пароль</h4>
            <form action="/profile" method="post" class="form-group">
                <input type="hidden" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
                <input type="password" name="oldPassword" class="form-control" placeholder="Старый пароль" required>
                <input type="password" name="newPassword" class="form-control" placeholder="Новый пароль" required>
                <input type="password" name="confirmPassword" class="form-control" placeholder="Подтвердите пароль" required>
                <input type="submit" value="Обновить пароль" class="btn" style="width: fit-content; margin-top: 10px;">
            </form>
            <#if errors??>
                <div class="text-danger"><#list errors as e><p>${e}</p></#list></div>
            </#if>
        </div>
    </div>
</@layout.mainLayout>