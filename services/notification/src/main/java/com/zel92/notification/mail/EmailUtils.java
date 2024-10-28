package com.zel92.notification.mail;

public class EmailUtils {

    public static String getEmailText(String username, String key){
        return "Hello " + username + ",\n\nTo activate your newly created account click on the link bellow.\n\n" + getAccVerificationUrl(key) + "\n\nSupport Team.";
    }

    public static String getEmailText(String username){
        return "Hello " + username + ",\n\nYour order has been placed successfully. Thank you for choosing Zel Shop.\n\n" + "\n\nSupport Team.";
    }

    private static String getAccVerificationUrl(String key) {
        return "http://localhost:9000/api/v1/auth/verify?key=" + key;
    }
}
