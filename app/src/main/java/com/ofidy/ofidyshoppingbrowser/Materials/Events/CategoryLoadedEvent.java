package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Category;

import java.util.List;

public class CategoryLoadedEvent {

    public final List<Category> categories;

    public CategoryLoadedEvent(List<Category> categories) {
        this.categories = categories;
    }

}
