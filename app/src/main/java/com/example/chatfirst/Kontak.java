package com.example.chatfirst;

public class Kontak {
    private String nama;
    private String noHp;

    public Kontak(String nama, String noHp) {
        this.nama = nama;
        this.noHp = noHp;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}