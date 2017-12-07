package com.example.fauza.golang.model;

public class TourGuideRequest {
    String idMember;
    String idTourGuide;
    int requestStatus;
    String idMember_status;
    String idTourGuide_status;
    String tempatWisata;
    String jumlahWisatawan;
    String tanggalWisata;


    public TourGuideRequest() {

    }

    public TourGuideRequest(
            String idMember,
            String idTourGuide,
            int requestStatus,
            String idMember_status,
            String idTourGuide_status,
            String tempatWisata,
            String jumlahWisatawan,
            String tanggalWisata) {
        this.idMember = idMember;
        this.idTourGuide = idTourGuide;
        this.requestStatus = requestStatus;
        this.idMember_status = idMember_status;
        this.idTourGuide_status = idTourGuide_status;
        this.tempatWisata = tempatWisata;
        this.jumlahWisatawan = jumlahWisatawan;
        this.tanggalWisata = tanggalWisata;
    }

    public String getIdMember() {
        return idMember;
    }

    public String getIdTourGuide() {
        return idTourGuide;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public String getIdMember_status() {
        return idMember_status;
    }

    public String getIdTourGuide_status() {
        return idTourGuide_status;
    }

    public String getTempatWisata() {
        return tempatWisata;
    }

    public String getJumlahWisatawan() {
        return jumlahWisatawan;
    }

    public String getTanggalWisata() {
        return tanggalWisata;
    }
}
