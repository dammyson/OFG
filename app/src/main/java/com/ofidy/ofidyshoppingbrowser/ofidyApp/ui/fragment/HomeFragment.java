package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadSpecialProductsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SpecialProductsLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SpecialProductsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.FeaturedCategory;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Image;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.FeaturedCategoryAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.ProductAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.BaseActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.BundleKeys;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.CategoryActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.ProductActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.widget.SpaceItemDecoration;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Damilola on 5/23/2018.
 */

public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener{

    private List<Product> mBestSellingProducts;
    private List<Product> mTrendingProducts;
    private List<FeaturedCategory> mFeaturedCategories;

    //cggs == group
    //cgs == category
    //scgs == sub

    @Bind(R.id.best_seller_container)
    ViewFlipper mBestContainer;
    @Bind(R.id.trending_container)
    ViewFlipper mTrendingContainer;
    @Bind(R.id.best_list)
    RecyclerView mBestSellingList;
    @Bind(R.id.trending_list)
    RecyclerView mTrendingList;
    @Bind(R.id.featured_list)
    RecyclerView mFeaturedList;
    @Bind(R.id.slider)
    SliderLayout mDemoSlider;

    ProductAdapter mBestSellerAdapter;
    ProductAdapter mTrendingAdapter;
    FeaturedCategoryAdapter mFeaturedAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        int hSpace = getResources().getDimensionPixelOffset(R.dimen.card_grid_hspace);
        int vSpace = getResources().getDimensionPixelOffset(R.dimen.card_grid_vspace);

        mBestSellingProducts =  new ArrayList<>();
        mBestSellerAdapter = new ProductAdapter(getContext(), mBestSellingProducts, getPicasso(), view1 -> {

            int pos = mBestSellingList.getChildLayoutPosition(view1);
            if (pos == RecyclerView.NO_POSITION) return;
            Product product = (Product) mBestSellerAdapter.getItem(pos);
            ProductActivity.navigate((AppCompatActivity) getActivity(), view1.findViewById(R.id.product_image),
                    product, "");
        }, BaseActivity.CATALOG_TYPE_PRODUCT);
        mBestSellingList.setAdapter(mBestSellerAdapter);
        mBestSellingList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBestSellingList.addItemDecoration(new SpaceItemDecoration(hSpace, vSpace));

        mTrendingProducts =  new ArrayList<>();
        mTrendingAdapter = new ProductAdapter(getContext(), mTrendingProducts, getPicasso(), view12 -> {

            int pos = mTrendingList.getChildLayoutPosition(view12);
            if (pos == RecyclerView.NO_POSITION) return;
            Product product = (Product) mTrendingAdapter.getItem(pos);
            ProductActivity.navigate((AppCompatActivity) getActivity(), view12.findViewById(R.id.product_image), product, "");

        }, BaseActivity.CATALOG_TYPE_PRODUCT);
        mTrendingList.setAdapter(mTrendingAdapter);
        mTrendingList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mTrendingList.addItemDecoration(new SpaceItemDecoration(hSpace, vSpace));

        mFeaturedCategories = new ArrayList<>();
        mFeaturedCategories.add(new FeaturedCategory(140, "Computers & Laptops", R.drawable.computers));
        mFeaturedCategories.add(new FeaturedCategory(1160, "Men Fashion", R.drawable.men_fashion));
        mFeaturedCategories.add(new FeaturedCategory(1163, "Women Fashion", R.drawable.women_fashion));
        mFeaturedCategories.add(new FeaturedCategory(23, "Health", R.drawable.health_beauty));
        mFeaturedCategories.add(new FeaturedCategory(154, "Phones & Tablets", R.drawable.phones_tablet));
        mFeaturedCategories.add(new FeaturedCategory(141, "Home, Garden & Pets", R.drawable.home));
        mFeaturedCategories.add(new FeaturedCategory(18, "Electronics", R.drawable.electronics));
        mFeaturedCategories.add(new FeaturedCategory(20, "Kids & Babies", R.drawable.babykids));
        mFeaturedCategories.add(new FeaturedCategory(22, "Sports", R.drawable.sport_travel));
        mFeaturedAdapter = new FeaturedCategoryAdapter(getContext(), mFeaturedCategories, getPicasso(), view13 -> {
            int pos = mTrendingList.getChildLayoutPosition(view13);
            if (pos == RecyclerView.NO_POSITION) return;
            FeaturedCategory cat = mFeaturedAdapter.getItem(pos);
            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            intent.putExtra(BundleKeys.CATEGORY, cat.getName());
            intent.putExtra(BundleKeys.CATEGORY_VALUE, cat.getId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Bundle activityOptions = ActivityOptions.makeScaleUpAnimation(view13, 0, 0,
                        view13.getWidth(), view13.getHeight()).toBundle();
                startActivity(intent, activityOptions);
            } else {
                startActivity(intent);
            }
        });
        int i;
        i = getResources().getInteger(R.integer.post_grid_num_columns);
        mFeaturedList.setAdapter(mFeaturedAdapter);
        mFeaturedList.setLayoutManager(new StaggeredGridLayoutManager(i,StaggeredGridLayoutManager.VERTICAL));//new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hSpace = getResources().getDimensionPixelOffset(R.dimen.card_one);
        vSpace = getResources().getDimensionPixelOffset(R.dimen.card_one);
        mFeaturedList.addItemDecoration(new SpaceItemDecoration(hSpace, vSpace));

        //HashMap<String,String> file_maps = new HashMap<String, Integer>();
        List<Image> images = OfidyDB.getInstance(getContext()).getImages();
        if(images != null && !images.isEmpty()){
            for(Image image : images){
                TextSliderView textSliderView = new TextSliderView(getContext());
                // initialize a SliderLayout
                textSliderView
                        // .description(name)
                        .image(image.getUrl())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                //textSliderView.getBundle().putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            //mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(8000);
            //mDemoSlider.addOnPageChangeListener(this);
            loadList(LoadSpecialProductsEvent.BEST_SELLING);
            loadList(LoadSpecialProductsEvent.TRENDING);
        }
        else
            mDemoSlider.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @OnClick(R.id.ng_store)
    protected void onNgStoreClicked(){
        openRegionCategory("Products from Nigeria", 11);
    }

    @OnClick(R.id.uk_store)
    protected void onUkStoreClicked(){
        openRegionCategory("Products from UK", 12);
    }

    @OnClick(R.id.us_store)
    protected void onUsStoreClicked(){
        openRegionCategory("Products from US", 13);
    }

    private void openRegionCategory(String title, int rgn){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra(BundleKeys.CATEGORY_REGION, rgn);
        intent.putExtra(BundleKeys.CATEGORY, title);
        startActivity(intent);
    }

    private void loadList(int type){
        switch (type){
            case LoadSpecialProductsEvent.BEST_SELLING:
                mBestContainer.setDisplayedChild(2);
                break;
            case LoadSpecialProductsEvent.TRENDING:
                mTrendingContainer.setDisplayedChild(2);
                break;
        }
        getBus().post(new LoadSpecialProductsEvent(type, false));
    }

    @Subscribe
    public void onSpecialProductsLoadedEvent(SpecialProductsLoadedEvent event) {
        switch (event.tab){
            case LoadSpecialProductsEvent.BEST_SELLING:
                mBestSellingProducts.addAll(event.products);
                mBestSellerAdapter.notifyDataSetChanged();
                mBestContainer.setDisplayedChild(0);
                break;
            case LoadSpecialProductsEvent.TRENDING:
                mTrendingProducts.addAll(event.products);
                mTrendingAdapter.notifyDataSetChanged();
                mTrendingContainer.setDisplayedChild(0);
                break;
        }
    }

    @Subscribe
    public void onSpecialProductsLoadErrorEvent(SpecialProductsLoadErrorEvent event) {
        switch (event.tab){
            case LoadSpecialProductsEvent.BEST_SELLING:
                mBestContainer.setDisplayedChild(1);
                break;
            case LoadSpecialProductsEvent.TRENDING:
                mTrendingContainer.setDisplayedChild(1);
                break;
        }
    }

}
