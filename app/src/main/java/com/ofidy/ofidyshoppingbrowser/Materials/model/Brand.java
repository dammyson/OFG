package com.ofidy.ofidyshoppingbrowser.Materials.model;

import java.util.List;

/**
 * Created by damil on 8/2/2017.
 */
public class Brand {
    private List Data;
    private int id;
    private String name;
    private String region;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
