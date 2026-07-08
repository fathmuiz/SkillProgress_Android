package com.skillprogress.app.model;

public class RiwayatMateri {
    private int id;
    private String tanggal;
    private String materi;
    private int persentase;

    public RiwayatMateri(int id, String tanggal, String materi, int persentase) {
        this.id = id;
        this.tanggal = tanggal;
        this.materi = materi;
        this.persentase = persentase;
    }

    public int getId() { return id; }
    public String getTanggal() { return tanggal; }
    public String getMateri() { return materi; }
    public int getPersentase() { return persentase; }
}
