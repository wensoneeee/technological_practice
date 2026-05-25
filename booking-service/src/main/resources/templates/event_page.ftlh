<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Мероприятие">
    <#if error??>
        <h1 class="text-danger">Упс! Такого мероприятия не существует</h1>
    <#else>
        <div class="info-box">
            <h2>${event.title}</h2>
            <p>${event.description}</p>
            <p><strong>Когда:</strong> ${event.timestamp}</p>
            <p><strong>Где:</strong> ${event.location}</p>
            <p><strong>Цена:</strong> ${event.price} руб.</p>
            <p><strong>Билетов осталось:</strong> ${event.availableTickets}</p>
            <p><strong>Оценка:</strong> ${avgScore}</p>
            <div class="details-image-wrapper">
                <img src="/image/${event.imageId}">
            </div>
        </div>

        <h4>Добавить в корзину</h4>
        <form action="/cart/add" method="post" class="item-card">
            <input type="hidden" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
            <input type="hidden" name="eventId" value="${event.id}">
            <input type="hidden" name="price" value="${event.price}">
            <div class="form-group">
                <label>Количество билетов:</label>
                <input type="number" name="quantity" class="form-control" min="1" max="${event.availableTickets}" value="1">
            </div>
            <input type="submit" value="В корзину" class="btn">
        </form>

        <#if errors??>
            <div class="text-danger">
                <#list errors as e><p>${e}</p></#list>
            </div>
        </#if>

        <hr class="mt-20">
        <h3>Отзывы</h3>

        <form action="/event/${event.id}" method="post" class="item-card">
            <input type="hidden" id="csrf" name="${(_csrf.parameterName)!' '}" value="${(_csrf.token)!' '}">
            <input type="hidden" name="eventId" value="${event.id}">
            <div class="form-group">
                <input type="text" name="text" class="form-control" placeholder="Введите комментарий" required>
            </div>
            <div class="form-group">
                <input type="number" name="score" class="form-control" min="1" max="5" step="1" placeholder="Оценка (1-5)" required>
            </div>
            <input type="submit" value="Отправить отзыв" class="btn">
        </form>

        <#if errorFeedback??><p class="text-danger">${errorFeedback}</p></#if>

        <#list feedbacks as feedback>
            <div class="info-box">
                <p><strong>${feedback.userName}</strong> (Оценка: ${feedback.score})</p>
                <p>${feedback.comment}</p>
                <#if feedback.confirmed?? && feedback.confirmed>
                    <p class="text-success" style="font-size: 0.8rem;">КОММЕНТАРИЙ ПОДТВЕРЖДЕН</p>
                </#if>
            </div>
        </#list>
    </#if>
</@layout.mainLayout>