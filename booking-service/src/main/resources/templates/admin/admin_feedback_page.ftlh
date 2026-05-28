<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Админ: Отзыв">
    <div class="info-box">
        <form action="/admin/feedback" method="get" class="flex-row">

            <select name="selectedEventId" class="form-control">
                <#list events as e><option value="${e.id}" <#if selectedFeedback?? && selectedFeedback.eventId == e.id>selected</#if>>${e.id}. ${e.title}</option></#list>
            </select>
            <select name="selectedUserId" class="form-control">
                <#list users as u><option value="${u.id}" <#if selectedFeedback?? && selectedFeedback.userId == u.id>selected</#if>>${u.id}. ${u.email}</option></#list>
            </select>
            <input type="submit" value="Выбрать" class="btn">
        </form>
    </div>

    <#if selectedFeedback??>
        <form action="/admin/feedback/save" method="post" class="item-card">

            <input type="hidden" name="userId" value="${selectedFeedback.userId}">
            <input type="hidden" name="eventId" value="${selectedFeedback.eventId}">
            <div class="form-group">
                <label>Комментарий</label>
                <input type="text" name="text" class="form-control" value="${(selectedFeedback.text)!''}" required>
            </div>
            <div class="form-group">
                <label>Оценка</label>
                <input type="number" min="1" max="5" step="1" name="score" class="form-control" value="${(selectedFeedback.score)!''}" placeholder="рейтинг">
            </div>
            <input type="submit" value="Сохранить рейтинг" class="btn">
        </form>
    </#if>
</@layout.mainLayout>