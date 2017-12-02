package com.example.fauza.golang.model;

public class TourGuideConfirm {
    private String idTourguide;
    private String namaTourGuide;

    public TourGuideConfirm() {
    }

    public TourGuideConfirm(String idTourguide, String namaTourGuide) {
        this.idTourguide = idTourguide;
        this.namaTourGuide = namaTourGuide;
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
}
