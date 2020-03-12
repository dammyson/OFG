package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.CartAdapter;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.LoginActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.CartActivity;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ari on 10/14/16.
 */
public class CartFragment extends AppCompatDialogFragment {

    private CartAdapter adapter;
    private List<Cart> mCarts;
    @Bind(R.id.list)
    RecyclerView mPostList;
    @Bind(R.id.flipper)
    ViewFlipper flipper;
    @Bind(R.id.total)
    TextView mTotal;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, v);

        mCarts = OfidyDB.getInstance(getActivity()).getCartItems();

        adapter = new CartAdapter(getContext(), mCarts, R.layout.cart_list_item, view -> {
            int pos = mPostList.getChildLayoutPosition(view);
        });
        int i;
        mPostList.setAdapter(adapter);
        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setItemAnimator(new DefaultItemAnimator());

        if(mCarts.isEmpty())
            flipper.setDisplayedChild(0);
        else {
            flipper.setDisplayedChild(1);
            setTotal();
        }
        return v;
    }

    private void setTotal(){
        double t = 0;
        for(Cart c : mCarts){
            t += c.getTotalPrice();
        }
        DecimalFormat precision = new DecimalFormat("0.00");
        mTotal.setText(precision.format(t));
    }

    @OnClick(R.id.cart)
    protected void goToCart(){
        if(UserPrefs.getInstance(getContext()).getString(UserPrefs.Key.EMAIL).isEmpty()){
            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
        }else{
            getContext().startActivity(new Intent(getContext(), CartActivity.class));
        }


        dismiss();
    }

    @Subscribe
    public void onCartsLoadedEvent(ShoppingCartLoadedEvent event) {
        mCarts = OfidyDB.getInstance(getActivity()).getCartItems();
        adapter.notifyDataSetChanged();
        if(mCarts.isEmpty())
            flipper.setDisplayedChild(0);
        else {
            flipper.setDisplayedChild(1);
            setTotal();
        }
    }
}
