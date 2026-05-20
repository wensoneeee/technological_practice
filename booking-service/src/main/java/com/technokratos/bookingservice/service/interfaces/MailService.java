package com.technokratos.bookingservice.service.interfaces;

public interface MailService {
    void sendMailForConfirm(String email, String code);
}
