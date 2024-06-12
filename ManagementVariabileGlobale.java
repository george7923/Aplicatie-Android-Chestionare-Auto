package com.example.autoexpert;

public class ManagementVariabileGlobale {
    public static String getUserName() {
        return User.getUsername();
    }

    public static void setUserName(String newUsername) {
        User.setUsername(newUsername);
    }

    public static String getMod() {
        return ModulDeJoc.getMod();
    }

    public static void setMod(String mod) {
        ModulDeJoc.setMod(mod);
    }
    public static String getCalif(){return Calificativ.getCalificativ();}
    public static void setCalif(String calif){Calificativ.setCalificativ(calif);}
}
