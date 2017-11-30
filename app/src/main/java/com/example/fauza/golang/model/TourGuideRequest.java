package com.example.fauza.golang.model;

public class TourGuideRequest {
    String idTourGuide;
    String tujuanWisata;
    String tanggalWisata;
    String jumlahWisatawan;
    String status;


    public TourGuideRequest() {

    }

    public TourGuideRequest(String idTourGuide, String tujuanWisata, String tanggalWisata, String jumlahWisatawan, String status) {
        this.idTourGuide = idTourGuide;
        this.tujuanWisata = tujuanWisata;
        this.tanggalWisata = tanggalWisata;
        this.jumlahWisatawan = jumlahWisatawan;
        this.status = status;
    }

    public String getIdTourGuide() {
        return idTourGuide;
    }

    public void setIdTourGuide(String idTourGuide) {
        this.idTourGuide = idTourGuide;
    }

    public String getTujuanWisata() {
        return tujuanWisata;
    }

    public void setTujuanWisata(String tujuanWisata) {
        this.tujuanWisata = tujuanWisata;
    }

    public String getTanggalWisata() {
        return tanggalWisata;
    }

    public void setTanggalWisata(String tanggalWisata) {
        this.tanggalWisata = tanggalWisata;
    }

    public String getJumlahWisatawan() {
        return jumlahWisatawan;
    }

    public void setJumlahWisatawan(String jumlahWisatawan) {
        this.jumlahWisatawan = jumlahWisatawan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
