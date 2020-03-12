package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddWishListItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemRemovedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadProductEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.WishItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Seller;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Size;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.R;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import mehdi.sakout.fancybuttons.FancyButton;

public class ProductActivity extends BaseActivity {

    @Bind(R.id.product_specs)
    TextView mSpecText;
    @Bind(R.id.product_details)
    TextView mDetailsText;
    @Bind(R.id.product_price)
    TextView mPriceText;
    @Bind(R.id.product_title)
    TextView mTitleText;
    @Bind(R.id.rice)
    TextView mAvaText;
    @Bind(R.id.product_image)
    ImageView mImageView;
    @Bind(R.id.container)
    ViewFlipper container;
    @Bind(R.id.size)
    Spinner mSize;
    @Bind(R.id.pay_layout)
    LinearLayout mPayLayout;
    @Bind(R.id.size_layout)
    CardView mSizeLayout;
    @Bind(R.id.picker_ui_view)
    PickerUI mPickerUI;
    @Bind(R.id.product_slider)
    SliderLayout mProductSlider;
    @Bind(R.id.image_flipper)
    ViewFlipper mImageFlipper;
    @Bind(R.id.add_wish)
    FancyButton mWish;

    ProgressDialog progress;
    int qty = 1;

    private Product product;
    private boolean loading;
    private boolean cartLoading;
    private boolean wishClicked;

    private String size = "0";

    public static void navigate(AppCompatActivity activity, View transitionImage, Product viewModel, String section) {
        Intent intent = new Intent(activity, ProductActivity.class);
        intent.putExtra(BundleKeys.PRODUCT, viewModel);
        intent.putExtra(BundleKeys.CATEGORY, section);

        String EXTRA_IMAGE = activity.getString(R.string.product_transition);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.get(BundleKeys.PRODUCT);
        if(product == null)
            finish();

        setLayout(R.layout.activity_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mPickerUI.setOnClickItemPickerUIListener((which, position, valueResult) -> {
            if(wishClicked)
                addToWist(product.getSellers().get(which));
            else
                addToCart(product.getSellers().get(which));
            mPickerUI.slide();
        });
        loading = true;
        if(!TextUtils.isEmpty(product.getAgentSeller()))
            mWish.setVisibility(View.GONE);
       refreshProductView(false);
        getBus().post(new LoadProductEvent(product.getId(), false));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product, menu);
        inflateCommonMenu(menu);
        return true;
    }

    private void refreshProductView(boolean cache){
        StringBuilder sb;

        if(!TextUtils.isEmpty(product.getName()))
            mTitleText.setText(Html.fromHtml(product.getName()));
        if(String.valueOf(product.getQuantity()) != null){
            if(product.getQuantity() < 0 && product.getPrice() > 0){
                mAvaText.setTextColor(Color.BLACK);
                mAvaText.setText("Availability : Pre Order");

            }else if(product.getQuantity() > 0 && product.getPrice() > 0){
                mAvaText.setTextColor(Color.BLACK);

                mAvaText.setText("Availability : In Stock");
            }else{
                mAvaText.setTextColor(Color.RED);
                mAvaText.setText("Availability : Out Of Stock");
            }

        }


        if (!TextUtils.isEmpty(product.getImage())) {
            String imageUrl = "https://ofidy.com/ProductImages" + product.getImage();
            getPicasso().load(imageUrl).placeholder(R.drawable.logo_ofidy_s)
                    .fit().centerCrop()
                    .into(mImageView);
        }
        if(!TextUtils.isEmpty(product.getDescription())) {
            container.setDisplayedChild(LIST_DISPLAY);
            if(product.getPrice() > 0) {
                mPriceText.setVisibility(View.VISIBLE);
                mPayLayout.setVisibility(View.VISIBLE);
            }
            else
                mPriceText.setVisibility(View.GONE);
            mDetailsText.setText(Html.fromHtml(product.getDescription()));
            if (!TextUtils.isEmpty(product.getSpec())) {
                sb = new StringBuilder();
                sb.append(product.getSpec()).append("\n");
                if (!TextUtils.isEmpty(product.getWeight()))
                    sb.append("Weight: ").append(product.getWeight()).append("\n").append("\n");
                if(!TextUtils.isEmpty(product.getDimB()) || !TextUtils.isEmpty(product.getDimL())
                        || !TextUtils.isEmpty(product.getDimH())){
                    sb.append(Html.fromHtml("<b>Product Dimensions (mm):</b>")).append("\n");
                    sb.append("Breadth: ").append(product.getDimB()).append("\n");
                    sb.append("Length: ").append(product.getDimL()).append("\n");
                    sb.append("Height: ").append(product.getDimH()).append("\n");
                    sb.append("\n");
                }
                if(!TextUtils.isEmpty(product.getBattery()))
                    sb.append("Batteries Required: ").append(product.getBattery()).append("\n");
                if(!TextUtils.isEmpty(product.getModelNum()))
                    sb.append("Model Number: ").append(product.getModelNum()).append("\n");
                if(!TextUtils.isEmpty(product.getNum()))
                    sb.append("Other Product ID Number: ").append(product.getNum()).append("\n");
                if(!TextUtils.isEmpty(product.getIsbn()))
                    sb.append("ISBN: ").append(product.getIsbn()).append("\n");
                if(!TextUtils.isEmpty(product.getPublisherName()))
                    sb.append("Publisher Name: ").append(product.getPublisherName()).append("\n");
                if(!TextUtils.isEmpty(product.getManNumber()))
                    sb.append("MAN Number: ").append(product.getManNumber()).append("\n");
                if(!TextUtils.isEmpty(product.getNAFDACNumber()))
                    sb.append("NAFDAC Number: ").append(product.getNAFDACNumber()).append("\n");
                if(!TextUtils.isEmpty(product.getProcessorType()))
                    sb.append("Processor Type: ").append(product.getProcessorType()).append("\n");
                if(!TextUtils.isEmpty(product.getProcessorSpeed()))
                    sb.append("Processor Speed: ").append(product.getProcessorSpeed()).append("\n");
                if(!TextUtils.isEmpty(product.getRAMSize()))
                    sb.append("RAM Size: ").append(product.getRAMSize()).append("\n");
                if(!TextUtils.isEmpty(product.getHDSize()))
                    sb.append("Hard Drive Size: ").append(product.getHDSize()).append("\n");
                if(!TextUtils.isEmpty(product.getHDType()))
                    sb.append("Hard Drive Type: ").append(product.getHDType()).append("\n");
                if(!TextUtils.isEmpty(product.getOSType()))
                    sb.append("OS Type: ").append(product.getOSType()).append("\n");
                if(!TextUtils.isEmpty(product.getWiFi()))
                    sb.append("WiFi: ").append(product.getWiFi()).append("\n");
                if(!TextUtils.isEmpty(product.getHDMI()))
                    sb.append("HDMI-compatible: ").append(product.getHDMI()).append("\n");
                if(!TextUtils.isEmpty(product.getBluetooth()))
                    sb.append("Bluetooth Capability: ").append(product.getBluetooth()).append("\n");
                if(!TextUtils.isEmpty(product.getUSB()))
                    sb.append("USB Ports: ").append(product.getUSB()).append("\n");
                if(!TextUtils.isEmpty(product.getDVD()))
                    sb.append("DVD reader: ").append(product.getDVD()).append("\n");
                if(!TextUtils.isEmpty(product.getScreenSize()))
                    sb.append("Screen Size: ").append(product.getScreenSize()).append("\n");
                if(!TextUtils.isEmpty(product.getKeyboardLight()))
                    sb.append("Keyboard Light: ").append(product.getKeyboardLight()).append("\n");
                if(!TextUtils.isEmpty(product.getPSUPower()))
                    sb.append("PSU Power Rating: ").append(product.getPSUPower()).append("\n");
                if(!TextUtils.isEmpty(product.getGender()))
                    sb.append("Gender: ").append(product.getGender()).append("\n");
                if(2 > 0)
                    sb.append("NotAvalable: ").append(product.getQuantity()).append("\n");
                mSpecText.setText(sb.toString());

            }
            if(product.hasSize() && !product.getSizes().isEmpty()){
                mSizeLayout.setVisibility(View.VISIBLE);
                List<String> li = new ArrayList<>(product.getSizes().size());

                System.out.println("................................product_details = Work " +product.getSizes().size());
                for(Size c : product.getSizes()){
                    System.out.println("................................product_details = Work ");
                    li.add(c.getValue() + "-" + c.getSpec());
                }


                size  = product.getSizes().get(0).getValue();
                ArrayAdapter adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, li);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mSize.setAdapter(adapter);
            }
            else
                mSizeLayout.setVisibility(View.GONE);
            if(product.getImages() != null && product.getImages().length > 0){
                for(String name : product.getImages()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    textSliderView
                            .image("https://m.ofidy.com/ProductImages" +name)
                            .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                    mProductSlider.addSlider(textSliderView);
                }
                mProductSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                mProductSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mImageFlipper.setDisplayedChild(1);
            }
            if (product.getPrice() > 0) {
                mPriceText.setText(UserPrefs.getInstance(this).getString(UserPrefs.Key.CURRENCY) + " " + product.getPrice());
                mPriceText.setVisibility(View.VISIBLE);
            }
            else
                mPriceText.setVisibility(View.GONE);
            if(product.getSellers() != null && !product.getSellers().isEmpty()){
                List<String> ss = new ArrayList<>();
                int i = 1;
                for(Seller s : product.getSellers()){
                    sb = new StringBuilder();
                    sb.append("Seller ").append(s.getAgentID()).append(" ").append(s.getCurrency()).append(s.getRprice());
                    ss.add(sb.toString());
                    i++;
                }
                PickerUISettings pickerUISettings = new PickerUISettings.Builder()
                        .withItems(ss)
                        .withAutoDismiss(false)
                        .withItemsClickables(true)
                        .withUseBlur(false)
                        .build();


                mPickerUI.setSettings(pickerUISettings);
            }
        }
        else{
            if(!cache && loading)
                container.setDisplayedChild(PROGRESS_DISPLAY);
            else
                container.setDisplayedChild(TEXT_DISPLAY);
        }

    }

    @OnItemSelected(R.id.size)
    protected void onSizeSelected(Spinner spinner, int position) {
        size  = product.getSizes().get(position).getValue();
        System.out.println("New size......................................."+size);
    }

    private void addToCart(Seller seller){
        View v = getLayoutInflater().inflate(R.layout.product_quantity, null);
        final EditText inputQty = (EditText) v.findViewById(R.id.quantity);
        v.findViewById(R.id.quantity_down).setOnClickListener(view -> {
            if(qty > 1)
                qty -= 1;
            else
                qty = 1;
            inputQty.setText(String.valueOf(qty));
        });
        v.findViewById(R.id.quantity_up).setOnClickListener(view -> {
            qty += 1;
            inputQty.setText(String.valueOf(qty));
        });
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Add to Cart?")
                .setMessage("Add "+product.getName()+" from seller "+seller.getAgentID()+" to shopping cart")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String s = inputQty.getText().toString().trim();
                    if(!TextUtils.isEmpty(s)) {
                        if(TextUtils.isDigitsOnly(s)) {
                            qty = Integer.parseInt(s);
                            cartLoading = true;
                            progress = new ProgressDialog(this);
                            progress.setOwnerActivity(ProductActivity.this);
                            progress.setMessage("Updating....");
                            //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progress.setCancelable(false);
                            progress.show();
                            getBus().post(new AddCartItemEvent(product, seller, qty, size));
                        }
                    }
                    else{
                        Snackbar.make(mPriceText, product.getName()+" added to cart", Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        alertDialog.setView(v);
        alertDialog.show();
    }

    private void addToWist(Seller seller){
        cartLoading = true;
        progress = new ProgressDialog(this);
        progress.setOwnerActivity(ProductActivity.this);
        progress.setMessage("Updating....");
        progress.setCancelable(false);
        progress.show();
        getBus().post(new AddWishListItemEvent(product, seller));
    }

    @OnClick(R.id.add_cart)
    protected void addToCartPressed(){
        wishClicked = false;
        if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN)) {
            if(!TextUtils.isEmpty(product.getAgentSeller())){
                Seller seller = new Seller();
                seller.setRprice(product.getPrice());
                seller.setAgentID(product.getAgentSeller());
                seller.setCurrency(product.getCurrency());
                addToCart(seller);
            }
            else if (product.getSellers() != null && !product.getSellers().isEmpty()) {
                if (product.getSellers().size() > 1) {
                    mPickerUI.slide();
                } else
                    addToCart(product.getSellers().get(0));
            }
        }
        else{
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("You have to be signed in to add product to cart")
                    .setPositiveButton("Sign in now", (dialog, which) -> {
                        dialog.dismiss();
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra(BundleKeys.FROM_ACTIVITY, true);
                        startActivity(i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        }
    }

    @OnClick(R.id.add_wish)
    protected void addToWishlist(){
        wishClicked = true;
        if(AppState.getInstance(this).getBoolean(AppState.Key.LOGGED_IN)) {
            if (product.getSellers() != null && !product.getSellers().isEmpty()) {
                if (product.getSellers().size() > 1) {
                    mPickerUI.slide();
                } else
                    addToWist(product.getSellers().get(0));
            }
        }
        else{
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("You have to be signed in to add product to wish list")
                    .setPositiveButton("Sign in now", (dialog, which) -> {
                        dialog.dismiss();
                        Intent i = new Intent(this, LoginActivity.class);
                        i.putExtra(BundleKeys.FROM_ACTIVITY, true);
                        startActivity(i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        }
    }

    @Subscribe
    public void onProductLoadedEvent(ProductLoadedEvent event) {
        loading = false;
        if(event.product != null){
            product = event.product;
            refreshProductView(false);
        }
        else{
            container.setDisplayedChild(TEXT_DISPLAY);
        }
    }

    @Subscribe
    public void onProductsLoadErrorEvent(ProductLoadErrorEvent event) {
        loading = false;
        container.setDisplayedChild(TEXT_DISPLAY);
        Snackbar.make(mPriceText, event.message, Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onCartItemAddedEvent(CartItemAddedEvent event) {
        cartLoading = false;
        progress.dismiss();
        Snackbar.make(mPriceText, product.getName()+" added to cart", Snackbar.LENGTH_SHORT).show();
        refreshCart();
    }

    @Subscribe
    public void onCartItemRemovedEvent(CartItemRemovedEvent event) {
        refreshCart();
    }

    @Subscribe
    public void onWishItemAddedEvent(WishItemAddedEvent event) {
        cartLoading = false;
        progress.dismiss();
        Snackbar.make(mPriceText, product.getName()+" added to wish list", Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onAddCartItemErrorEvent(AddCartItemErrorEvent event) {
        cartLoading = false;
        progress.dismiss();
//        badgeCount++;
//        if(cartMenu != null) {
//            ActionItemBadge.update(cartMenu, badgeCount);
//        }
        invalidateOptionsMenu();
        Snackbar.make(mPriceText, event.message, Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onShoppingCartLoadedEvent(ShoppingCartLoadedEvent event) {
        super.onShoppingCartLoadedEvent(event);
    }

//    @Subscribe
//    protected void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
//        super.onInterntStatusChangedEvent(event);
//    }

    @Override
    public void onBackPressed() {
        if(mPickerUI.isPanelShown())
            mPickerUI.slide();
        else
            super.onBackPressed();
    }
}
