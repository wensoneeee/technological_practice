<#import "layout.ftlh" as layout>
<@layout.mainLayout title="Корзина">
    <#if success?? && success == true>
        <h1 class="text-success">Билеты успешно приобретены!</h1>
    <#else>
        <#if error??><h1 class="text-danger">Некоторые билеты закончились в продаже(</h1></#if>
        <h1>Корзина</h1>
        <#if cartItems?? && (cartItems?size > 0)>
            <div class="info-box">
                <h3>Ваш заказ:</h3>
                <h5>Итого: ${totalPriceRub} руб.</h5>
                <#list cartItems as cartItem>
                    <div class="item-card">
                        <strong>${cartItem.eventName}</strong><br>
                        Количество: ${cartItem.quantity} | Цена за шт: ${cartItem.price} руб.<br>
                        Подытог: ${cartItem.totalPrice} руб.

                        <form action="/cart/delete/${cartItem.id}" method="post" style="margin-top: 10px;">

                            <input type="submit" value="Удалить" class="btn text-danger" style="background: #fee2e2; padding: 5px 10px;">
                        </form>
                    </div>
                </#list>
                <form action="/purchase" method="post">

                    <input type="submit" value="Оформить покупку" class="btn">
                </form>
            </div>
        <#else>
            <p>Ваша корзина пуста.</p>
        </#if>
    </#if>
    <#if errors??>
        <div class="text-danger"><#list errors as e><p>${e}</p></#list></div>
    </#if>
</@layout.mainLayout>