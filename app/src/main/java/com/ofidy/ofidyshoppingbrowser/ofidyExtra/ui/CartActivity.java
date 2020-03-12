package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemRemovedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemUpdatedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.DeleteCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.DeleteCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.EmptyCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadShoppingCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.CartAdapter;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    @Bind(R.id.list)
    RecyclerView mPostList;
    @Bind(R.id.total)
    TextView mTotal;
    private CartAdapter adapter;
    private List<Cart> mCarts;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCarts = OfidyDB.getInstance(this).getCartItems();
       // mCarts = Ofidy.cartItems;

        adapter = new CartAdapter(this, mCarts, R.layout.cart_list_item2, view -> {
            int pos = mPostList.getChildLayoutPosition(view);
        });
        mPostList.setAdapter(adapter);
        mPostList.setLayoutManager(new LinearLayoutManager(this));
        mPostList.setItemAnimator(new DefaultItemAnimator());
        setTotal();
        getBus().post(new LoadShoppingCartEvent(true));
    }

    private void setTotal(){
        double t = 0;
        for(Cart c : mCarts){
            t += c.getTotalPrice();
        }
        DecimalFormat precision = new DecimalFormat("0.00");
        mTotal.setText(precision.format(t));


        mCarts = Ofidy.cartItems;

        adapter = new CartAdapter(this, mCarts, R.layout.cart_list_item2, view -> {
            int pos = mPostList.getChildLayoutPosition(view);
        });
        mPostList.setAdapter(adapter);
        mPostList.setLayoutManager(new LinearLayoutManager(this));
        mPostList.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick(R.id.checkout)
    protected void onCheckoutClicked() {
        double t = 0;
        for(Cart c : mCarts){
            t += c.getTotalPrice();
        }
       Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(BundleKeys.CART_TOTAL, t);
        startActivity(intent);
        finish();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        inflateCommonMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_clear){
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Clear shopping cart?")
                    .setMessage("Clearing the shopping cart will permanently remove all items from cart")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        getBus().post(new EmptyCartEvent());
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        }
        if(id == R.id.menu_dollar){
            Ofidy.getInstance().setCurrency("USD");

            mCarts = Ofidy.cartItems;

            adapter = new CartAdapter(this, mCarts, R.layout.cart_list_item2, view -> {
                int pos = mPostList.getChildLayoutPosition(view);
            });
            mPostList.setAdapter(adapter);
            mPostList.setLayoutManager(new LinearLayoutManager(this));
            mPostList.setItemAnimator(new DefaultItemAnimator());
            setTotal();
            getBus().post(new LoadShoppingCartEvent(true));


        }
        if(id == R.id.menu_naira){
            Ofidy.getInstance().setCurrency("NGN");

            mCarts = Ofidy.cartItems;

            adapter = new CartAdapter(this, mCarts, R.layout.cart_list_item2, view -> {
                int pos = mPostList.getChildLayoutPosition(view);
            });
            mPostList.setAdapter(adapter);
            mPostList.setLayoutManager(new LinearLayoutManager(this));
            mPostList.setItemAnimator(new DefaultItemAnimator());
            setTotal();
            getBus().post(new LoadShoppingCartEvent(true));


        } if(id == R.id.menu_pounds){
            Ofidy.getInstance().setCurrency("GBP");

            mCarts = Ofidy.cartItems;

            adapter = new CartAdapter(this, mCarts, R.layout.cart_list_item2, view -> {
                int pos = mPostList.getChildLayoutPosition(view);
            });
            mPostList.setAdapter(adapter);
            mPostList.setLayoutManager(new LinearLayoutManager(this));
            mPostList.setItemAnimator(new DefaultItemAnimator());
            setTotal();
            getBus().post(new LoadShoppingCartEvent(true));

        }


        return super.onOptionsItemSelected(item);
    }




    @Subscribe
    public void onUpdateCartItemEvent(final UpdateCartItemEvent event) {
        progress = ProgressDialog.show(this, "Updating....",
                "Please wait", true);
    }

    @Subscribe
    public void onDeleteCartItemEvent(final DeleteCartItemEvent event) {
        progress = ProgressDialog.show(this, "Deleting....",
                "Please wait", true);
    }

    @Subscribe
    public void onDeleteCartItemEvent(final EmptyCartEvent event) {
        progress = ProgressDialog.show(this, "Updating....",
                "Please wait", true);
    }

    @Subscribe
    public void onCartsLoadedEvent(ShoppingCartLoadedEvent event) {
        if(progress != null)
            progress.dismiss();
        //mCarts.addAll(event.cartItems);
        mCarts = Ofidy.cartItems;

        adapter.notifyDataSetChanged();
        setTotal();
    }

    @Subscribe
    public void onCartsLoadedEvent(CartItemUpdatedEvent event) {
        if(progress != null)
            progress.dismiss();
        mCarts = Ofidy.cartItems;
        adapter.notifyDataSetChanged();
        setTotal();
        Snackbar.make(mTotal, "Cart item updated successfully", Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onCartsLoadedEvent(CartItemRemovedEvent event) {
        if(progress != null)
            progress.dismiss();
        mCarts = Ofidy.cartItems;
        adapter.notifyDataSetChanged();
        setTotal();
        Snackbar.make(mTotal, "Cart item(s) removed successfully", Snackbar.LENGTH_SHORT).show();
        if(mCarts.isEmpty())
            new Handler().postDelayed(
                    () -> finish(), 1000);
    }

    @Subscribe
    public void onUpdateCartItemErrorEvent(UpdateCartItemErrorEvent event) {
        if(progress != null)
            progress.dismiss();
        Snackbar.make(mTotal, event.message, Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onDeleteCartItemErrorEvent(DeleteCartItemErrorEvent event) {
        if(progress != null)
            progress.dismiss();
        Snackbar.make(mTotal, event.message, Snackbar.LENGTH_SHORT).show();
    }

}
