package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Bus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Cart> mCarts;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;
    private int mRes;

    public CartDetailsAdapter(Context context, List<Cart> products, int res,
                              View.OnClickListener itemClickListener) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCarts = products;
        mItemClickListener = itemClickListener;
        mRes = res;
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        int count = mCarts.size();
        return count;
    }

    public Object getItem(int position) {
        return mCarts.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mRes, parent, false);
        return new PostViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PostViewHolder postVH = (PostViewHolder) viewHolder;
        Cart product = (Cart) getItem(position);
        bindPost(postVH, product, position);
    }

    private void bindPost(PostViewHolder viewHolder, final Cart cart, int position) {
        viewHolder.mCartName.setText(Html.fromHtml(cart.getName()));
        viewHolder.mCartPrice.setText(cart.getCurrency()+cart.getTotalPrice());
        viewHolder.mCartSize.setText(cart.getIsize());
        viewHolder.mCartColor.setText(cart.getColour());
        viewHolder.mCartQty.setText(String.valueOf(cart.getQuantity()));
        viewHolder.mShippingCost.setText( cart.getCurrency()+ String.valueOf(cart.getAdminShipping()));
        viewHolder.mTax.setText(cart.getCurrency()+ String.valueOf(cart.getSrcregiontax()));
        viewHolder.mItemPrice.setText(cart.getCurrency()+ String.valueOf(cart.getTotalPrice()));
        viewHolder.mCartSpec.setText(cart.getWeight());
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }

    private Bus getBus() {
        return BusProvider.getBus();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.qty)
        TextView mCartQty;
        @Bind(R.id.cart_name)
        TextView mCartName;
        @Bind(R.id.cart_price)
        TextView mCartPrice;
        @Bind(R.id.item_Price)
        TextView mItemPrice;
        @Bind(R.id.cart_spec)
        TextView mCartSpec;
        @Bind(R.id.cart_color)
        TextView mCartColor;

        @Bind(R.id.cart_size)
        TextView mCartSize;

        @Bind(R.id.tax)
        TextView mTax;

        @Bind(R.id.shipping_cost)
        TextView mShippingCost;



        public PostViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
        }
    }

}
