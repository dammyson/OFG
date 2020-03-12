package com.ofidy.ofidyshoppingbrowser.Materials.model;

/**
 * Created by ari on 10/11/16.
 */
public class FeaturedCategory extends Category {

    private int imageId;

    public FeaturedCategory(int id, String name, int imageId) {
        super(id, name);
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
