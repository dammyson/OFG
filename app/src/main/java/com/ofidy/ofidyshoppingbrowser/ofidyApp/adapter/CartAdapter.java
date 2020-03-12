package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.DeleteCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.squareup.otto.Bus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Cart> mCarts;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;
    private int mRes;

    public CartAdapter(Context context, List<Cart> products, int res,
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
        viewHolder.mCartSpec.setText(cart.getWeight());
        viewHolder.mCartColor.setText(cart.getColour());
        viewHolder.mCartSize.setText(cart.getIsize());
        viewHolder.mCartQty.setText(String.valueOf(cart.getQuantity()));
        viewHolder.mCartDown.setOnClickListener(view -> {
            if(cart.getQuantity() > 1){
                final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("Update cart?")
                        .setMessage("Reduce quantity of "+cart.getName())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            cart.setQuantity(cart.getQuantity() - 1);
                            viewHolder.mCartQty.setText(String.valueOf(cart.getQuantity()));
                            getBus().post(new UpdateCartItemEvent(cart.getId(), cart.getQuantity(), ""));
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();
                alertDialog.show();
            }
            else{
                final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setMessage("Delete cart item?")
                        .setMessage("This will permanently remove item from shopping cart")
                        .setPositiveButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setNegativeButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            BusProvider.getBus().post(new DeleteCartItemEvent(cart.getId()));
                        })
                        .create();
                alertDialog.show();
            }
        });
        viewHolder.mCartUp.setOnClickListener(view -> {
            final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setTitle("Update cart?")
                    .setMessage("Increase quantity of "+cart.getName())
                    .setPositiveButton("Yes", (dialog, which) -> {
                        cart.setQuantity(cart.getQuantity() + 1);
                        viewHolder.mCartQty.setText(String.valueOf(cart.getQuantity()));
                        getBus().post(new UpdateCartItemEvent(cart.getId(), cart.getQuantity(), ""));
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create();
            alertDialog.show();
        });
        viewHolder.mCartDelete.setOnClickListener(view -> {
            final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setMessage("Delete cart item?")
                    .setMessage("This will permanently remove item from shopping cart")
                    .setPositiveButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        BusProvider.getBus().post(new DeleteCartItemEvent(cart.getId()));
                    })
                    .create();
            alertDialog.show();
        });
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }

    private Bus getBus() {
        return BusProvider.getBus();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cart_qty)
        TextView mCartQty;
        @Bind(R.id.cart_name)
        TextView mCartName;
        @Bind(R.id.cart_price)
        TextView mCartPrice;
        @Bind(R.id.cart_spec)
        TextView mCartSpec;
        @Bind(R.id.cart_size)
        TextView mCartSize;
        @Bind(R.id.cart_color)
        TextView mCartColor;
        @Bind(R.id.cart_down)
        ImageView mCartDown;
        @Bind(R.id.cart_up)
        ImageView mCartUp;
        @Bind(R.id.cart_delete)
        ImageView mCartDelete;

        public PostViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
        }
    }

}
