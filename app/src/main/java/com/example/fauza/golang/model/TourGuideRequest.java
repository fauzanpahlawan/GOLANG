package com.example.fauza.golang.model;

public class TourGuideRequest {
    String idMember;
    String tempatWisata;
    String jumlahWisatawan;
    String tanggalWisata;
    int status;


    public TourGuideRequest() {

    }

    public TourGuideRequest(String idMember, String tempatWisata, String jumlahWisatawan, String tanggalWisata, int status) {
        this.idMember = idMember;
        this.tempatWisata = tempatWisata;
        this.jumlahWisatawan = jumlahWisatawan;
        this.tanggalWisata = tanggalWisata;
        this.status = status;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getTempatWisata() {
        return tempatWisata;
    }

    public void setTempatWisata(String tempatWisata) {
        this.tempatWisata = tempatWisata;
    }

    public String getJumlahWisatawan() {
        return jumlahWisatawan;
    }

    public void setJumlahWisatawan(String jumlahWisatawan) {
        this.jumlahWisatawan = jumlahWisatawan;
    }

    public String getTanggalWisata() {
        return tanggalWisata;
    }

    public void setTanggalWisata(String tanggalWisata) {
        this.tanggalWisata = tanggalWisata;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
