package com.technokratos.bookingservice.filter;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    // ThreadLocal — класс в Java, который позволяет хранить данные так, чтобы они были доступны только текущему потоку
    // Когда приложение получает http-запросы от разных юзеров, он обрабатывает каждый запрос в отдельном потоке
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentUserRole = new ThreadLocal<>();


    public void setUserId(Long userId) {
        currentUserId.set(userId);
    }

    public Long getUserId() {
        return currentUserId.get();
    }

    public static void setUserRole(String role) {
        currentUserRole.set(role);
    }

    public String getUserRole() {
        return currentUserRole.get();
    }

    public void clear() {
        currentUserId.remove();
        currentUserRole.remove();
    }
}
