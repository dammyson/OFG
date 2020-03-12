package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.R;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Address> mCarts;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;
    String[] types;

    public AddressAdapter(Context context, List<Address> products,
                          View.OnClickListener itemClickListener) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCarts = products;
        mItemClickListener = itemClickListener;
        types = context.getResources().getStringArray(R.array.address_type);

        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        int count = mCarts.size();
        return count;
    }

    public Address getItem(int position) {
        return mCarts.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.address_list_item, parent, false);
        return new AddressViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AddressViewHolder postVH = (AddressViewHolder) viewHolder;
        Address product = getItem(position);
        bindPost(postVH, product, position);
    }

    private void bindPost(AddressViewHolder viewHolder, final Address address, int position) {
        StringBuilder sb = new StringBuilder();
        if(!TextUtils.isEmpty(address.getCity()))
            sb.append(address.getCity());
        if(!TextUtils.isEmpty(address.getCountry()))
            sb.append(", ").append(address.getCountry());
        viewHolder.mCity.setText(sb.toString());
        sb = new StringBuilder();
        if(!TextUtils.isEmpty(address.getAddressLine1()))
            sb.append(address.getAddressLine1());
        if(!TextUtils.isEmpty(address.getAddressLine2()))
            sb.append(", ").append(address.getAddressLine2());
        viewHolder.mStreet.setText(sb.toString());
        if(address.getAddressType() > 0)
            viewHolder.mType.setText(types[address.getAddressType() - 1]);
        else
            viewHolder.mType.setText(types[0]);
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }


    static class AddressViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.street)
        TextView mStreet;
        @Bind(R.id.type)
        TextView mType;
        @Bind(R.id.city)
        TextView mCity;

        public AddressViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
        }
    }

}
