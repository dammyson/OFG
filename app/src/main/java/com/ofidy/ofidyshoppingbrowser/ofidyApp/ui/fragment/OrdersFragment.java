package com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.TransactionsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Transaction;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter.OrderAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrdersFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private List<Transaction> mTransactions;

    @Bind(R.id.list)
    RecyclerView mPostList;
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.flipper)
    ViewFlipper flipper;

    private OrderAdapter mOrderAdapter;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance() {
        OrdersFragment fragment = new OrdersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);
        mTransactions = OfidyDB.getInstance(getContext()).getTransactions();
        //flipper.setDisplayedChild(1);


        mOrderAdapter = new OrderAdapter(getContext(), mTransactions, view1 -> {
            int pos = mPostList.getChildLayoutPosition(view1);
        });
        int i;
        i = getResources().getInteger(R.integer.product_grid_num_columns);
        mPostList.setAdapter(mOrderAdapter);
        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setItemAnimator(new DefaultItemAnimator());
        mText.setText("No recent transaction");
        if(mTransactions.isEmpty()){
            flipper.setDisplayedChild(0);
        }
        else{
            flipper.setDisplayedChild(1);
        }
        return view;
    }

    @Subscribe
    public void onTransactionsLoadedEvent(TransactionsLoadedEvent event) {
        mTransactions = OfidyDB.getInstance(getActivity()).getTransactions();
        mOrderAdapter.notifyDataSetChanged();
        if(mTransactions.isEmpty())
            flipper.setDisplayedChild(0);
        else {
            flipper.setDisplayedChild(1);
        }
    }

}
