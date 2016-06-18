package com.abhishek.microsofttask;

/**
 * Created by abhishek.tyagi1 on 17/06/16.
 */
public class Page {
    String id;
    String title;
    String imageUrl;
    public Page(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
