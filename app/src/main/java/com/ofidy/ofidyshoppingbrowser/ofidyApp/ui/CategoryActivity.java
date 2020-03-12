package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.BrandsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadBrandsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadProductsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductsLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Brand;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Currency;
import com.ofidy.ofidyshoppingbrowser.Materials.model.FilterObject;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.DeviceUtils;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.BrandAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.FilterAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.ProductAdapter;

import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.widget.SpaceItemDecoration;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import mehdi.sakout.fancybuttons.FancyButton;

public class CategoryActivity extends BaseActivity {

    private List<Product> mProducts;
    private ProductAdapter mProductAdapter;

    private ArrayList<FilterObject> objects;

    private List<Brand> mBrands;
    private BrandAdapter mBrandAdapter;

    //Current category name
    private String mCategory = "";
    //Current category id
    private int mCatVal = 0;
    //Current category group id
    private int mCatGroupVal = 0;
    //Current subcategory id
    private int mSubCatVal = 0;

    //Value set for minimum price in filter
    private String minPrice = "";
    //Value set for maximum price in filter
    private String maxPrice = "";
    //Value set for region in filter
    private int region = 0;
    //Value set for currency in filter
    private String currency = "";
    //Page value for pagination
    private int mPage = 1;
    int mBrand = 0;

    protected MenuItem filterMenu;
    private EndlessRecyclerViewScrollListener scrollListener;

    int[] regionValues;
    List<Currency> savedCurrencies;
    //TypedArray currencyValues;

    private boolean canLoadMore = true;

    //UI fields
    @Bind(R.id.min_price)
    TextView mMinPriceText;
    @Bind(R.id.max_price)
    TextView mMaxPriceText;
    @Bind(R.id.region)
    Spinner mRegionSpinner;
    @Bind(R.id.currency)
    Spinner mCurrencySpinner;
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.container)
    ViewFlipper flipperContainer;
    @Bind(R.id.list)
    RecyclerView mPostList;
    //@Bind(R.id.title)
    //TextView mTitleView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.reload)
    FancyButton mReload;
    //Filter  widget
    private CoordinatorLayout mRelativeLayout;
    private Context mContext;
    private Activity mActivity;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_category);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currency = Ofidy.getInstance().getCurrency();

        savedCurrencies = OfidyDB.getInstance(this).getCurrencies();
        int selectedCurrency = 0;
        if(!savedCurrencies.isEmpty()){
            List<String> li = new ArrayList<>(savedCurrencies.size());
            int a = 0;
            for(Currency c : savedCurrencies) {
                if(c.getCode().equals(currency = Ofidy.getInstance().getCurrency()))
                    selectedCurrency = a;
                li.add(c.getName());
                a++;
            }
            ArrayAdapter adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, li);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mCurrencySpinner.setAdapter(adapter);
        }
        else{
            savedCurrencies = new ArrayList<>();
            String[] curs = getResources().getStringArray(R.array.currency);
            String[] curs_val = getResources().getStringArray(R.array.currency_value);
            int i = 0;
            for(String s: curs){
                savedCurrencies.add(new Currency("0", s, curs_val[i]));
                if(curs_val[i].equals(currency = Ofidy.getInstance().getCurrency()))
                    selectedCurrency = i;
                i++;
            }
        }
        mCurrencySpinner.setSelection(selectedCurrency);

        regionValues = getResources().getIntArray(R.array.region_value);


        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mCategory = bundle.getString(BundleKeys.CATEGORY);
            mCatVal = bundle.getInt(BundleKeys.CATEGORY_VALUE, 0);
            mCatGroupVal = bundle.getInt(BundleKeys.CAT_GROUP_VALUE, 0);
            mSubCatVal = bundle.getInt(BundleKeys.SUB_CAT_VALUE, 0);
            region = bundle.getInt(BundleKeys.CATEGORY_REGION);
        }

        // mTitleView.setText(mCategory);
        getSupportActionBar().setTitle(mCategory);

        mProducts = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, mProducts, getPicasso(), view -> {
            int pos = mPostList.getChildLayoutPosition(view);
            if (pos == RecyclerView.NO_POSITION) return;
            Product product = (Product) mProductAdapter.getItem(pos);
            ProductActivity.navigate(CategoryActivity.this, view.findViewById(R.id.product_image), product, mCategory);
        }, BaseActivity.SHOP_TYPE_PRODUCT);
        int i;
        i = getResources().getInteger(R.integer.product_grid_num_columns);
        mPostList.setAdapter(mProductAdapter);
        StaggeredGridLayoutManager stg = new StaggeredGridLayoutManager(i,StaggeredGridLayoutManager.VERTICAL);
        mPostList.setLayoutManager(stg);
        mPostList.setItemAnimator(new DefaultItemAnimator());
        int hSpace = getResources().getDimensionPixelOffset(R.dimen.card_grid_hspace);
        int vSpace = getResources().getDimensionPixelOffset(R.dimen.card_grid_vspace);
        mPostList.addItemDecoration(new SpaceItemDecoration(hSpace, vSpace));

        int screenWidth = DeviceUtils.getScreenWidth(this);
        int maxContainerWidth = getResources().getDimensionPixelSize(R.dimen.post_grid_max_width);
        if (screenWidth > maxContainerWidth) {
            int containerPadding = (screenWidth - maxContainerWidth) / 2;
            ViewCompat.setPaddingRelative(mPostList,
                    ViewCompat.getPaddingStart(mPostList) + containerPadding,
                    mPostList.getPaddingTop(),
                    ViewCompat.getPaddingEnd(mPostList) + containerPadding,
                    mPostList.getPaddingBottom());
        }

        final Drawable appbarShadowDrawable;
        appbarShadowDrawable = ContextCompat.getDrawable(this, R.drawable.appbar_shadow);
        flipperContainer.setForeground(null);     // hide the shadow initially
        mPostList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scrollY = mPostList.computeVerticalScrollOffset();
                flipperContainer.setForeground(scrollY <= 0 ? null : appbarShadowDrawable);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(stg) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(canLoadMore)
                    loadProducts(false);
            }
        };
        mPostList.addOnScrollListener(scrollListener);
        flipperContainer.setDisplayedChild(PROGRESS_DISPLAY);
        loadProducts(false);
        loadBrands(false);

        // filter
        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = CategoryActivity.this;
        mRelativeLayout = (CoordinatorLayout) findViewById(R.id.rl);

        //filter spiner


        loadFilter();




    }

    @OnClick(R.id.appNig)
    public void goToApp(){
        if (AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN) && !UserPrefs.getInstance(CategoryActivity.this).getString(UserPrefs.Key.SID).isEmpty() ){
            Intent intent = new Intent(this, com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }


    }
    private void loadFilter(){
        String[] strings = {"Filter...", "Filter By Brands"};
        objects = new ArrayList<FilterObject>();
        for (int k = 0; k < strings.length; k++) {
            FilterObject obj = new FilterObject();
            obj.setAll(R.drawable.ic_action_minus, strings[k]);
            objects.add(obj);
        }



        mBrands = new ArrayList<>();

        // ListView listView = (ListView) customView.findViewById(R.id.listbrand);
        // mBrandAdapter = new BrandAdapter(mContext,mBrands);
        // listView.setAdapter(mBrandAdapter);

        // Toast.makeText(mContext,mBrands.get(3).getName(), Toast.LENGTH_LONG).show();


        Spinner mySpinner = (Spinner) findViewById(R.id.mspiner);
        mySpinner.setAdapter(new FilterAdapter(this, objects));
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mySpinner.setSelection(0);


                if(position != 0){
                    BrandDialog();
                }else {
//show Brand

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void BrandDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CategoryActivity.this, R.style.DialogStyle);
        LayoutInflater inflater = CategoryActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customdialogwithlist_layout, null);
        dialogBuilder.setView(dialogView);



        final AlertDialog alertDialog = dialogBuilder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER); // set alert dialog in center
        // window.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL); // set alert dialog in Bottom

        // Cancel Button
        Button cancel_btn = (Button) dialogView.findViewById(R.id.buttoncancellist);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });



        final ListView listView = (ListView) dialogView.findViewById(R.id.listview);
        mBrandAdapter = new BrandAdapter(mContext,mBrands);


        listView.setAdapter(mBrandAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mProducts.clear();
                mBrand = mBrands.get(position).getId();
                mPage = 1;


                Log.d("products", String.valueOf(mBrand));
                flipperContainer.setDisplayedChild(PROGRESS_DISPLAY);
                loadProducts(false);

                alertDialog.hide();


            }
        });

        alertDialog.show();

    }

    /**
     * Method called when region spinner is clicked in filter
     *
     * @param spinner Spinner view performing action
     * @param position
     */
    @OnItemSelected(R.id.region)
    protected void onRegionItemSelected(Spinner spinner, int position) {
        region = regionValues[position];
    }

    /**
     * Method called when currency spinner is clicked in filter
     *
     * @param spinner Spinner view performing action
     * @param position
     */
    @OnItemSelected(R.id.currency)
    protected void onCurrencyItemSelected(Spinner spinner, int position) {
        if(savedCurrencies != null && !savedCurrencies.isEmpty())
            currency = savedCurrencies.get(position).getCode();
//        else
//            currency = currencyValues.getString(position);
    }

    /**
     * Method called when 'Apply filter' button is clicked
     */
    @OnClick(R.id.apply_filter)
    protected void onFilter(){
        mProducts.clear();
        flipperContainer.setDisplayedChild(PROGRESS_DISPLAY);
        minPrice = mMinPriceText.getText().toString();
        maxPrice = mMaxPriceText.getText().toString();
        mPage = 1;
        scrollListener.resetState();
        loadProducts(false);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            filterMenu.setIcon(R.drawable.ic_filter_list_white_24dp);
        }
    }

    /**
     * Method called when 'Reload' button is clicked
     */
    @OnClick(R.id.reload)
    protected void reload(){
        flipperContainer.setDisplayedChild(PROGRESS_DISPLAY);
        loadProducts(false);
    }

    /**
     * Method called to call products based on category, price, region and currency
     *
     * @param cache Boolean, to load if cached products are available
     */
    private void loadProducts(boolean cache){
        System.out.println("................................categoryies = "+mCatVal);
        System.out.println("................................categoryies = "+mCatGroupVal);
        System.out.println("................................categoryies = "+mSubCatVal);
        getBus().post(new LoadProductsEvent(mCatGroupVal, mCatVal, mSubCatVal, "", minPrice, maxPrice, region, currency, mPage, mBrand, cache));
    }


    private void loadBrands(boolean cache){

        System.out.println("................................categoryies = "+mCatVal);
        System.out.println("................................categoryies = "+mCatGroupVal);
        System.out.println("................................categoryies = "+mSubCatVal);
        getBus().post(new LoadBrandsEvent(mCatVal,0,0,0,0,cache));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        filterMenu = menu.findItem(R.id.menu_filter);
        inflateCommonMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                    filterMenu.setIcon(R.drawable.ic_filter_list_white_24dp);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                    filterMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method called when products for category are successfully loaded from server
     *
     * @param event ProductsLoadedEvent
     */
    @Subscribe
    public void onProductsLoadedEvent(ProductsLoadedEvent event) {
        if(event.products != null && !event.products.isEmpty()){
            mPage = event.page;
            if(event.page < 2)
                mProducts.clear();
            mProducts.addAll(event.products);
            if (event.products.size() < event.productsFetchLimit) {
                CharSequence message = Html.fromHtml(String.format(
                        getString(R.string.post_limit_exceeded), String.valueOf(mProducts.size())));
                mProductAdapter.showFooter(message);
                canLoadMore = false;
            } else {
                mProductAdapter.hideFooter();
            }
            flipperContainer.setDisplayedChild(LIST_DISPLAY);
            mProductAdapter.notifyDataSetChanged();
        }
        else if((event.products == null || event.products.isEmpty()) && mProducts.isEmpty()){
            mText.setText("No product found in category");
            mReload.setVisibility(View.GONE);
            flipperContainer.setDisplayedChild(TEXT_DISPLAY);
            canLoadMore = false;
        }
        else if(!mProducts.isEmpty()){
            CharSequence message = Html.fromHtml(String.format(
                    getString(R.string.post_limit_exceeded), String.valueOf(event.productsFetchLimit)));
            mProductAdapter.showFooter(message);
            canLoadMore = false;
        }
        else if(mProducts.isEmpty()){
            mText.setText("An error occurred, please check internet connection");
            flipperContainer.setDisplayedChild(TEXT_DISPLAY);
        }
    }


    @Subscribe
    public void onBrandsLoadedEvent(BrandsLoadedEvent event){


        if(event.brande != null && !event.brande.isEmpty()){
            mBrands.clear();
            mBrands.addAll(event.brande);


        }
    }




    /**
     * Method called when error occurs during products load
     *
     * @param event ProductsLoadErrorEvent
     */
    @Subscribe
    public void onProductsLoadErrorEvent(ProductsLoadErrorEvent event) {
        if(mProducts.isEmpty()) {
            mText.setText("An error occurred, please check internet connection");
            flipperContainer.setDisplayedChild(TEXT_DISPLAY);
        }
        else{
            // Snackbar.make(mTitleView, event.message, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called when cart is loaded from server
     *
     * @param event ShoppingCartLoadedEvent
     */
    @Subscribe
    public void onShoppingCartLoadedEvent(ShoppingCartLoadedEvent event) {
        super.onShoppingCartLoadedEvent(event);
    }

    /**
     * Method called when cart item is added
     *
     * @param event CartItemAddedEvent
     */
    @Subscribe
    public void onCartItemAddedEvent(CartItemAddedEvent event) {
        refreshCart();
    }

    @Subscribe
    public void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        super.onInterntStatusChangedEvent(event);
    }

}
