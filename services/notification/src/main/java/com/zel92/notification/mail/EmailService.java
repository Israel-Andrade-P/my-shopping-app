package com.zel92.notification.mail;

public interface EmailService {

    void sendAccountVerificationEmail(String username, String to, String key);
}