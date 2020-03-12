package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.WishItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.WishlistLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.WishAdapter;

import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.ProductActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Damilola on 5/23/2018.
 */

public class WishListFragment extends BaseFragment {

    private List<Product> mProducts;
    private WishAdapter mWishAdapter;

    @Bind(R.id.list)
    RecyclerView mPostList;
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.flipper)
    ViewFlipper flipper;

    public WishListFragment() {
        // Required empty public constructor
    }

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);
        mProducts = OfidyDB.getInstance(getContext()).getWishlist();

        mWishAdapter = new WishAdapter(getContext(), mProducts, getPicasso(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mPostList.getChildLayoutPosition(view);
                Product product = mWishAdapter.getItem(pos);
                ProductActivity.navigate((AppCompatActivity) getActivity(), view.findViewById(R.id.product_image), product, "");
            }
        });
        mPostList.setAdapter(mWishAdapter);
        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setItemAnimator(new DefaultItemAnimator());

        mText.setText("No item in wish list");
        if(mProducts.isEmpty()){
            flipper.setDisplayedChild(0);
        }
        else{
            flipper.setDisplayedChild(1);
        }

        return view;
    }

    @Subscribe
    public void onWishlistLoadedEvent(WishlistLoadedEvent event) {
        mProducts = OfidyDB.getInstance(getActivity()).getWishlist();
        mWishAdapter.notifyDataSetChanged();
        if(mProducts.isEmpty())
            flipper.setDisplayedChild(0);
        else {
            flipper.setDisplayedChild(1);
        }
    }

    @Subscribe
    public void onWishItemAddedEvent(WishItemAddedEvent event) {
        mProducts = OfidyDB.getInstance(getActivity()).getWishlist();
        mWishAdapter.notifyDataSetChanged();
        if(mProducts.isEmpty())
            flipper.setDisplayedChild(0);
        else {
            flipper.setDisplayedChild(1);
        }
    }

}
