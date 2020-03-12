package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddressLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OfidyDB;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.AddressActivity;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.BundleKeys;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.AddressAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressFragment extends BaseFragment {

    private AddressAdapter adapter;
    private List<Address> mAddress;
    @Bind(R.id.list)
    RecyclerView mPostList;
    @Bind(R.id.container)
    ViewFlipper flipper;

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance() {
        AddressFragment fragment = new AddressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this, view);

        mAddress = OfidyDB.getInstance(getActivity()).getAddresses();

        adapter = new AddressAdapter(getContext(), mAddress, v -> {
            int pos = mPostList.getChildLayoutPosition(v);
            Address address = adapter.getItem(pos);
            Intent intent = new Intent(getContext(), AddressActivity.class);
            intent.putExtra(BundleKeys.ADDRESS, address);
            startActivity(intent);
        });
        int i;
        mPostList.setAdapter(adapter);
        mPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostList.setItemAnimator(new DefaultItemAnimator());

        if(!mAddress.isEmpty())
            flipper.setDisplayedChild(0);
        else
            flipper.setDisplayedChild(1);

        return view;
    }

    @OnClick(R.id.add)
    protected void goToCart(){
        getContext().startActivity(new Intent(getContext(), AddressActivity.class));
    }

    @Subscribe
    public void onAddressLoadedEvent(AddressLoadedEvent event) {
        mAddress = OfidyDB.getInstance(getActivity()).getAddresses();
        adapter.notifyDataSetChanged();
    }


}
