package com.example.fauza.golang.model;

public class TourGuideConfirm {
    private String idRequest;
    private String idTourguide;
    private String idMember;
    private String namaTourGuide;
    private String status;

    public TourGuideConfirm() {
    }

    public TourGuideConfirm(String idRequest, String idTourguide, String idMember, String namaTourGuide, String status) {
        this.idRequest = idRequest;
        this.idTourguide = idTourguide;
        this.idMember = idMember;
        this.namaTourGuide = namaTourGuide;
        this.status = status;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public String getIdTourguide() {
        return idTourguide;
    }

    public String getIdMember() {
        return idMember;
    }

    public String getNamaTourGuide() {
        return namaTourGuide;
    }

    public String getStatus() {
        return status;
    }
}
