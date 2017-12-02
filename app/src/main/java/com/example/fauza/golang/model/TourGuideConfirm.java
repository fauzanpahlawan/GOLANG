package com.example.fauza.golang.model;

public class TourGuideConfirm {
    private String idTourguide;
    private String idMember;
    private String namaTourGuide;
    private String status;

    public TourGuideConfirm() {
    }

    public TourGuideConfirm(String idTourguide, String idMember, String namaTourGuide, String status) {
        this.idTourguide = idTourguide;
        this.idMember = idMember;
        this.namaTourGuide = namaTourGuide;
        this.status = status;
    }

    public String getIdTourguide() {
        return idTourguide;
    }

    public void setIdTourguide(String idTourguide) {
        this.idTourguide = idTourguide;
    }

    public String getNamaTourGuide() {
        return namaTourGuide;
    }

    public void setNamaTourGuide(String namaTourGuide) {
        this.namaTourGuide = namaTourGuide;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }
}
