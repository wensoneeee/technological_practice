<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Админ: Пользователь">
    <h3>Обновите или создайте пользователя</h3>

    <form action="/admin/user" method="get" class="info-box flex-row">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <select name="userId" class="form-control">
            <option value="">-- создать нового --</option>
            <#list users as u>
                <option value="${u.id}" <#if selectedUser?? && selectedUser.id == u.id>selected</#if>>${u.id}. ${u.email}</option>
            </#list>
        </select>
        <input type="submit" value="выбрать" class="btn">
    </form>

    <form action="/admin/user/save" method="post" class="item-card">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <div class="form-group"><input type="email" name="email" class="form-control" value="${(selectedUser.email)!''}" placeholder="почта" required></div>
        <div class="form-group">
            <label>Пароль:</label>
            <input type="password" name="password" class="form-control" placeholder="введите текущий или новый пароль">
        </div>
        <div class="form-group"><input type="text" name="name" class="form-control" value="${(selectedUser.name)!''}" placeholder="имя"></div>
        <div class="form-group">
            <label>Роль:</label>
            <select name="role" class="form-control">
                <option value="USER" <#if selectedUser?? && selectedUser.role == "USER">selected</#if>>USER</option>
                <option value="ADMIN" <#if selectedUser?? && selectedUser.role == "ADMIN">selected</#if>>ADMIN</option>
            </select>
        </div>
        <input type="submit" value="Сохранить" class="btn mt-20">
    </form>
</@layout.mainLayout>