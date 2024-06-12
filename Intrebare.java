package com.example.autoexpert;
import android.graphics.Bitmap;
import android.media.Image;


public class Intrebare {
    private String intrebare;
    private Raspuns raspuns1;
    private Raspuns raspuns2;
    private Raspuns raspuns3;
    private String categorie;
    private Image img;
    private Bitmap btm;
    private int ID;

    public Intrebare(String i, Raspuns r1, Raspuns r2, Raspuns r3, String c, int id, Image  img) {
        this.intrebare = i;
        this.raspuns1 = r1;
        this.raspuns2 = r2;
        this.raspuns3 = r3;
        this.categorie = c;
        this.ID = id;
        this.img = img;
    }
    public Intrebare(String i, Raspuns r1, Raspuns r2, Raspuns r3, String c, int id, Bitmap  btm) {
        this.intrebare = i;
        this.raspuns1 = r1;
        this.raspuns2 = r2;
        this.raspuns3 = r3;
        this.categorie = c;
        this.ID = id;
        this.btm = btm;
    }


    // Metodele getter și setter pentru toate proprietățile

    public String getIntrebare() {
        return intrebare;
    }

    public void setIntrebare(String intrebare) {
        this.intrebare = intrebare;
    }

    public Raspuns getRaspuns1() {
        return raspuns1;
    }

    public void setRaspuns1(Raspuns raspuns1) {
        this.raspuns1 = raspuns1;
    }

    public Raspuns getRaspuns2() {
        return raspuns2;
    }

    public void setRaspuns2(Raspuns raspuns2) {
        this.raspuns2 = raspuns2;
    }

    public Raspuns getRaspuns3() {
        return raspuns3;
    }

    public void setRaspuns3(Raspuns raspuns3) {
        this.raspuns3 = raspuns3;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }


    public Image  getImg() {
        return img;
    }

    public void setImg(Image  img) {
        this.img = img;
    }
    public Bitmap  getImagine() {
        return btm;
    }

    public void setImagine(Bitmap  img) {
        this.btm = img;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

