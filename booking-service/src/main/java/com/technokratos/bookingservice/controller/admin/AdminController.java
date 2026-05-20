package com.technokratos.bookingservice.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String getMainAdminPage() {
        return "admin/admin_main_page";
    }
}
