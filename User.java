package com.example.autoexpert;

public class User {
    private static String user;
    private static String username;

    // Metodele getter și setter pentru toate proprietățile

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        User.user = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }
}
