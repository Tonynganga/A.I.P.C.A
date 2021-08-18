package com.tony.aipca.Utils;

public class ReadingModel {
    private String readingday, readingdate, englishreading, kiswahilireading, kikuyureading,
    englishinjili, kiswahiliinjili, kikuyuinjili;

    public ReadingModel() {
    }

    public ReadingModel(String readingday, String readingdate, String englishreading,
                        String kiswahilireading, String kikuyureading, String englishinjili, String kiswahiliinjili, String kikuyuinjili) {
        this.readingday = readingday;
        this.readingdate = readingdate;
        this.englishreading = englishreading;
        this.kiswahilireading = kiswahilireading;
        this.kikuyureading = kikuyureading;
        this.englishinjili = englishinjili;
        this.kiswahiliinjili = kiswahiliinjili;
        this.kikuyuinjili = kikuyuinjili;
    }

    public String getReadingday() {
        return readingday;
    }

    public void setReadingday(String readingday) {
        this.readingday = readingday;
    }

    public String getReadingdate() {
        return readingdate;
    }

    public void setReadingdate(String readingdate) {
        this.readingdate = readingdate;
    }

    public String getEnglishreading() {
        return englishreading;
    }

    public void setEnglishreading(String englishreading) {
        this.englishreading = englishreading;
    }

    public String getKiswahilireading() {
        return kiswahilireading;
    }

    public void setKiswahilireading(String kiswahilireading) {
        this.kiswahilireading = kiswahilireading;
    }

    public String getKikuyureading() {
        return kikuyureading;
    }

    public void setKikuyureading(String kikuyureading) {
        this.kikuyureading = kikuyureading;
    }

    public String getEnglishinjili() {
        return englishinjili;
    }

    public void setEnglishinjili(String englishinjili) {
        this.englishinjili = englishinjili;
    }

    public String getKiswahiliinjili() {
        return kiswahiliinjili;
    }

    public void setKiswahiliinjili(String kiswahiliinjili) {
        this.kiswahiliinjili = kiswahiliinjili;
    }

    public String getKikuyuinjili() {
        return kikuyuinjili;
    }

    public void setKikuyuinjili(String kikuyuinjili) {
        this.kikuyuinjili = kikuyuinjili;
    }
}
