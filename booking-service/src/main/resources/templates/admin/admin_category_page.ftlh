<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Админ: Категория">
    <h3>Создать/изменить категорию</h3>

    <form action="/admin/category" method="get" class="info-box flex-row">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <select name="selectedCategoryId" class="form-control">
            <option value="">--создать новый--</option>
            <#list categories as c>
                <option value="${c.id}" <#if selectedCategory?? && selectedCategory.id == c.id>selected</#if>>${c.id}. ${c.name}</option>
            </#list>
        </select>
        <input type="submit" value="выбрать" class="btn">
    </form>

    <form action="/admin/category" method="post" class="item-card">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <input type="hidden" name="id" value="${(selectedCategory.id)!''}">
        <div class="form-group"><input type="text" name="name" class="form-control" placeholder="название" value="${(selectedCategory.name)!''}" required></div>
        <div class="form-group"><input type="text" name="description" class="form-control" placeholder="описание" value="${(selectedCategory.description)!''}" required></div>
        <input type="submit" value="отправить" class="btn">
    </form>

    <#if errors??><div class="text-danger"><#list errors as e><p>${e}</p></#list></div></#if>

    <h3>Добавить событие к категории</h3>
    <form action="/admin/category/addEvent" method="post" class="item-card flex-row">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <select name="selectedEventId" class="form-control">
            <#list events as e><option value="${e.id}">${e.id}. ${e.title}</option></#list>
        </select>
        <select name="selectedCategoryId" class="form-control">
            <#list categories as c><option value="${c.id}">${c.id}. ${c.name}</option></#list>
        </select>
        <input type="submit" value="Добавить" class="btn">
    </form>
</@layout.mainLayout>