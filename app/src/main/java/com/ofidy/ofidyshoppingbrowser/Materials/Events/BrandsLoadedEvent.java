package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Brand;

import java.util.List;

/**
 * Created by damil on 8/2/2017.
 */
public class BrandsLoadedEvent {

    public final List<Brand> brande;
    public final int productsFetchLimit;
    public final int page;



    public BrandsLoadedEvent(List<Brand> brands, int productsFetchLimit, int page) {
       this.brande = brands;
        this.productsFetchLimit = productsFetchLimit;
        this.page = page;

    }


}
