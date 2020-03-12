package com.ofidy.ofidyshoppingbrowser.Materials.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ari on 10/11/16.
 */
public class Category implements ParentListItem {

    private List mCategories;
    private String name;
    private String desc;
    private String logo = "fa-book";
    private int id;
    private int groupId = -1;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String logo) {
        this(name);
        this.logo = logo;
    }

    public Category(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public Category(int id, String name, String desc, String logo) {
        this(id, name);
        this.desc = desc;
        this.logo = logo;
    }

    public Category(String name, List categories) {
        this(name);
        mCategories = categories;
    }

    @Override
    public List getChildItemList() {
        return mCategories;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        if(!logo.contains("faw"))
            return logo.replaceFirst("fa", "faw");
        else
            return logo;
    }

    public void setLogo(String mImageId) {
        this.logo = mImageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void addSubCategory(Category c){
        if(mCategories == null)
            mCategories = new ArrayList();
        mCategories.add(c);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setSubCategories(List<Category> categories) {
        mCategories = categories;
    }
}
