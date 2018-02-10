package com.example.acer.chef;

/**
 * Created by Acer E1 571G on 26.12.2017.
 */

public class MPaylasimlar {

    private String id;
    private String paylasimAdi;
    private String tarih;
    private String resimYolu;
    private String kategori;
    private String icindekiler;
    private String tarif;
    private int kisiSayisi;
    private String hazirlanmaSuresi;
    private String pisirmeSuresi;
    private String k_ad_soyad;

    public MPaylasimlar(String id, String paylasimAdi, String tarih, String resimYolu, String kategori,String icindekiler ,String tarif, int kisiSayisi, String hazirlanmaSuresi, String pisirmeSuresi,String k_ad_soyad) {
        this.id = id;
        this.paylasimAdi = paylasimAdi;
        this.tarih = tarih;
        this.resimYolu = resimYolu;
        this.kategori = "";
        this.icindekiler= icindekiler;
        this.tarif = tarif;
        this.kisiSayisi = kisiSayisi;
        this.hazirlanmaSuresi = hazirlanmaSuresi;
        this.pisirmeSuresi = pisirmeSuresi;
        this.k_ad_soyad = k_ad_soyad;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaylasimadi() {
        return paylasimAdi;
    }

    public void setPaylasimadi(String paylasimadi) {
        this.paylasimAdi = paylasimadi;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getResimyolu() {
        return resimYolu;
    }

    public void setResimyolu(String resimyolu) {
        this.resimYolu = resimyolu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getIcindekiler() { return icindekiler; }

    public void setIcindekiler(String icindekiler) { this.icindekiler = icindekiler; }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public int getKisiSayisi() {
        return kisiSayisi;
    }

    public void setKisiSayisi(int kisiSayisi) {
        this.kisiSayisi = kisiSayisi;
    }

    public String getHazirlanmaSuresi() {
        return hazirlanmaSuresi;
    }

    public void setHazirlanmaSuresi(String hazirlanmaSuresi) { this.hazirlanmaSuresi = hazirlanmaSuresi; }

    public String getPisirmeSuresi() {
        return pisirmeSuresi;
    }

    public void setPisirmeSuresi(String pisirmeSuresi) {this.pisirmeSuresi = pisirmeSuresi;}


    public String getK_ad_soyad() {   return k_ad_soyad; }

    public void setK_ad_soyad(String k_ad_soyad) {
        this.k_ad_soyad = k_ad_soyad;
    }
}
