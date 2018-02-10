package com.example.acer.chef;

/**
 * Created by Acer E1 571G on 30.12.2017.
 */

public class KullaniciBilgileri {

    private String k_adi;
    private String k_soyadi;
    private String k_mail;
    private String k_sifre;
    private String k_ID;

    public KullaniciBilgileri( String k_ID,String k_adi , String k_soyadi , String k_mail , String k_sifre ){

        this.k_ID = k_ID;
        this.k_adi = k_adi;
        this.k_soyadi = k_soyadi;
        this.k_mail = k_mail;
        this.k_sifre = k_sifre;
    }

    public String getK_adi() {
        return k_adi;
    }

    public void setK_adi(String k_adi) {
        this.k_adi = k_adi;
    }

    public String getK_soyadi() {
        return k_soyadi;
    }

    public void setK_soyadi(String k_soyadi) {
        this.k_soyadi = k_soyadi;
    }

    public String getK_mail() {
        return k_mail;
    }

    public void setK_mail(String k_mail) {
        this.k_mail = k_mail;
    }

    public String getK_sifre() {
        return k_sifre;
    }

    public void setK_sifre(String k_sifre) {
        this.k_sifre = k_sifre;
    }
}

