package com.tony.aipca.Utils;

public class AnnouncementModel {
    String AnnDate, AnnDescription, AnnImage;

    public AnnouncementModel() {
    }

    public AnnouncementModel(String annDate, String annDescription, String annImage) {
        AnnDate = annDate;
        AnnDescription = annDescription;
        AnnImage = annImage;
    }

    public String getAnnDate() {
        return AnnDate;
    }

    public void setAnnDate(String annDate) {
        AnnDate = annDate;
    }

    public String getAnnDescription() {
        return AnnDescription;
    }

    public void setAnnDescription(String annDescription) {
        AnnDescription = annDescription;
    }

    public String getAnnImage() {
        return AnnImage;
    }

    public void setAnnImage(String annImage) {
        AnnImage = annImage;
    }
}
