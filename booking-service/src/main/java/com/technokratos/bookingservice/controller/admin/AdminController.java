package com.technokratos.bookingservice.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Admin Main UI", description = "Панель администратора: Главная страница")
public class AdminController {

    @GetMapping("/admin")
    @Operation(summary = "Панель админа", description = "Отображает меню-навигацию")
    @ApiResponse(responseCode = "200", description = "Страница панели успешно загружена",
            content = @Content(mediaType = "text/html"))
    public String getMainAdminPage() {
        return "admin/admin_main_page";
    }
}
