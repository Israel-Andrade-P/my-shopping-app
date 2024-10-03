package com.zel92.notification.mail;

public class EmailUtils {

    public static String getEmailText(String username, String key){
        return "Hello " + username + ",\n\nTo activate your newly created account click on the link bellow.\n\n" + getAccVerificationUrl(key) + "\n\nSupport Team.";
    }

    private static String getAccVerificationUrl(String key) {
        return "http://localhost:9000/api/v1/users/verify?key=" + key;
    }
}
