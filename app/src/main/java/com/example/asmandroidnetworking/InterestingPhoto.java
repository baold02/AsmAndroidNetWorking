package com.example.asmandroidnetworking;

import android.text.SpannableString;

public class InterestingPhoto {
    String id;
    String title;
    String datetaken;
    String photoURL;

    public InterestingPhoto(String id, String title, String datetaken, String photoURL) {
        this.id = id;
        this.title = title;
        this.datetaken = datetaken;
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "InterestingPhoto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dateTaken='" + datetaken + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetaken() {
        return datetaken;
    }

    public void setDatetaken(String datetaken) {
        this.datetaken = datetaken;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
