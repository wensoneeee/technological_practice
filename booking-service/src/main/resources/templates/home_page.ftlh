<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Главная страница">
    <h1>Мероприятия</h1>
    <select id="category" name="category" onchange="getEvent(this.value)" class="form-control" style="width: auto; margin-bottom: 20px;">
        <option value="Все">Все мероприятия</option>
        <#list categories as c><option value="${c.name}">${c.name}</option></#list>
    </select>

    <div class="events-grid" id="events_list"></div>

    <script>
        function renderEvents(events, events_list) {
            let innerHtml = '';
            for (let i = 0; i < events.length; i++) {
                innerHtml += '<a href="/event/' + events[i].id + '">';
                innerHtml += '<div class="event-card">';
                innerHtml += '<div class="card-image-wrapper"><img src="/image/' + events[i]['imageId'] + '"></div>';
                innerHtml += '<div class="card-body">';
                innerHtml += '<h3>' + events[i]['title'] + '</h3>';
                innerHtml += '<p>' + events[i]['description'] + '</p>';
                innerHtml += '<p>📍 ' + events[i]['location'] + ' | 🎟️ ' + events[i]['availableTickets'] + '</p>';
                innerHtml += '<p style="color: #2563eb; font-weight: bold;">' + events[i]['price'] + ' руб.</p>';
                if (events[i]['inWeeklyTop'] === true) {
                    innerHtml += '<p class="text-danger" style="font-weight: bold;">🔥 Хит продаж!</p>';
                }
                innerHtml += '</div></div></a>';
            }
            events_list.html(innerHtml);
        }

        function getEvent(categoryName) {
            $.ajax({
                type: "GET", url: "/home/data", data: { "categoryName": categoryName },
                success: function(response) {
                    let list = $('#events_list'); list.empty(); renderEvents(response, list);
                },
                error: function(xhr, status, error) {
                    alert("Произошла ошибка при загрузке данных");
                },
                dataType: "json"
            });
        }
        $(document).ready(function() { getEvent('Все'); });
    </script>
</@layout.mainLayout>