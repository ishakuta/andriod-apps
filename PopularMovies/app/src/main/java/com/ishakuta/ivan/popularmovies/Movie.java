package com.ishakuta.ivan.popularmovies;

public class Movie {
    int id;
    String name;
    double popularity;
    double voteAverage;

    private String imageUri;
    String movieUrl;

    String baseImageUrl;

    public Movie(int id, String name, double popularity, double voteAverage, String imageUri) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.imageUri = imageUri;
    }

    public void setBaseImageUrl(String url) {
        this.baseImageUrl = url;
    }

    public String getImageUrl() {
        if (baseImageUrl == null) {
            throw new RuntimeException("You must set base image URL with setBaseImageUrl");
        }
        return baseImageUrl.concat(imageUri);
    }
}
