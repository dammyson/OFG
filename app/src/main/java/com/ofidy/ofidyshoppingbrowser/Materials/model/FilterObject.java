package com.ofidy.ofidyshoppingbrowser.Materials.model;

/**
 * Created by damil on 7/28/2017.
 */
public class FilterObject {
    String company;
    int image_id;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setAll(int img_id, String title) {
        setImage_id(img_id);
        setCompany(title);

    }

}
