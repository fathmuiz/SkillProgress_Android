package com.skillprogress.app.model;

import java.util.ArrayList;

public class Skill {
    private int id;
    private int userId;
    private String namaSkill;
    private String materiHariIni;
    private int statusPaham;
    private String catatan;
    private String createdAt;
    private ArrayList<RiwayatMateri> riwayatList;

    public Skill() {
        riwayatList = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getNamaSkill() { return namaSkill; }
    public void setNamaSkill(String namaSkill) { this.namaSkill = namaSkill; }

    public String getMateriHariIni() { return materiHariIni; }
    public void setMateriHariIni(String materiHariIni) { this.materiHariIni = materiHariIni; }

    public int getStatusPaham() { return statusPaham; }
    public void setStatusPaham(int statusPaham) { this.statusPaham = statusPaham; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public ArrayList<RiwayatMateri> getRiwayatList() { return riwayatList; }
    public void setRiwayatList(ArrayList<RiwayatMateri> riwayatList) { this.riwayatList = riwayatList; }
}
