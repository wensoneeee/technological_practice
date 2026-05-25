<#import "../layout.ftlh" as layout>
<@layout.mainLayout title="Админ: Мероприятия">
    <h3>Создать/изменить мероприятие</h3>

    <form action="/admin/event" method="get" class="info-box flex-row">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <select name="selectedEventId" class="form-control">
            <option value="">--создать новый--</option>
            <#list events as e>
                <option value="${e.id}" <#if selectedEvent?? && selectedEvent.id == e.id>selected</#if>>${e.id}
                    . ${e.title}</option>
            </#list>
        </select>
        <input type="submit" value="выбрать" class="btn">
    </form>

    <form action="/admin/event/save" method="post" enctype="multipart/form-data" class="item-card">
        <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
        <input type="hidden" name="id" value="${(selectedEvent.id)!''}">
        <div class="form-group"><input type="text" name="title" class="form-control" placeholder="название"
                                       value="${(selectedEvent.title)!''}" required></div>
        <div class="form-group"><input type="text" name="description" class="form-control" placeholder="описание"
                                       value="${(selectedEvent.description)!''}" required></div>
        <div class="form-group"><input name="date" type="datetime-local" class="form-control"
                                       value="${(selectedEvent.timestamp)!''}" required></div>
        <div class="form-group"><input type="number" min="0" step="1" name="availableTickets" class="form-control"
                                       value="${(selectedEvent.availableTickets)!''}" placeholder="кол-во билетов"
                                       required></div>
        <div class="form-group"><input type="number" min="0" step="0.1" name="price" class="form-control"
                                       value="${(selectedEvent.price)!''}" placeholder="цена" required></div>
        <div class="form-group"><input type="text" name="location" class="form-control" placeholder="место проведения"
                                       value="${(selectedEvent.location)!''}" required></div>
        <div class="form-group"><input type="file" name="file" class="form-control" placeholder="выберите фотографию">
        </div>
        <input type="submit" value="отправить" class="btn">
    </form>

    <#if selectedEvent??>
        <form action="/admin/event/delete" method="post" class="mt-20">
            <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
            <input type="hidden" name="id" value="${selectedEvent.id}">
            <input type="submit" value="удалить" class="btn" style="background-color: #ef4444;">
        </form>
    </#if>
</@layout.mainLayout>