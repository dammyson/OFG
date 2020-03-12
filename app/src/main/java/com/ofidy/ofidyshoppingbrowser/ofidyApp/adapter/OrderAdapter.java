package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Transaction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<Transaction> mTransactions;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;

    // animation stuff
    private static final DecelerateInterpolator ANIM_INTERPOLATOR = new DecelerateInterpolator();
    private boolean mAnimateOnAttach = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mAnimationDelay = 0;

    public OrderAdapter(Context context, List<Transaction> products,
                        View.OnClickListener itemClickListener) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTransactions = products;
        mItemClickListener = itemClickListener;

        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        int count = mTransactions.size();
        return count;
    }

    public Object getItem(int position) {
        return mTransactions.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.order_list_item, parent, false);
        return new PostViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PostViewHolder postVH = (PostViewHolder) viewHolder;
        Transaction product = (Transaction) getItem(position);
        bindPost(postVH, product, position);
    }

    private void bindPost(PostViewHolder viewHolder, Transaction post, int position) {
        viewHolder.position.setText("#"+ String.valueOf(position + 1));
        viewHolder.invoice.setText("Invoice Id "+post.getId());
        viewHolder.qty.setText(post.getItemCount());
        viewHolder.total.setText(post.getCurrency() + post.getTotalPrice());
        viewHolder.status.setText("Delivery Status: " + post.getDelivered());
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.order_position)
        TextView position;
        @Bind(R.id.invoice_id)
        TextView invoice;
        @Bind(R.id.total)
        TextView total;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.qty)
        TextView qty;

        public PostViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
        }
    }

}
