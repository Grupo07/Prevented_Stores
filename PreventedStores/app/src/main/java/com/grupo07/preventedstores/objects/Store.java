package com.grupo07.preventedstores.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Store {

    public String name;
    private String category;
    private Double latitude;
    private Double longitude;
    private String sanitaryOptions;
    private String author;

    public Store() {}

    public Store(String name, String category, Double latitude, Double longitude, String sanitaryOptions, String author) {
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sanitaryOptions = sanitaryOptions;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getSanitaryOptions() {
        return sanitaryOptions;
    }

    public String getAuthor() {
        return author;
    }
}
