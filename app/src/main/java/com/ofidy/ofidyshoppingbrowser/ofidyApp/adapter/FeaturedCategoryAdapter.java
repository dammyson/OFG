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
import android.widget.ImageView;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.FeaturedCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeaturedCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<FeaturedCategory> mCarts;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;

    // animation stuff
    private static final DecelerateInterpolator ANIM_INTERPOLATOR = new DecelerateInterpolator();
    private boolean mAnimateOnAttach = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mAnimationDelay = 0;

    public FeaturedCategoryAdapter(Context context, List<FeaturedCategory> products, Picasso picasso,
                                   View.OnClickListener itemClickListener) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCarts = products;
        mItemClickListener = itemClickListener;

        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        int count = mCarts.size();
        return count;
    }

    public FeaturedCategory getItem(int position) {
        return mCarts.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.category_list_item, parent, false);
        return new PostViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        PostViewHolder postVH = (PostViewHolder) viewHolder;
        FeaturedCategory product = getItem(position);
        bindPost(postVH, product, position);
    }

    private void bindPost(PostViewHolder viewHolder, FeaturedCategory post, int position) {
        //viewHolder.position.setText(String.valueOf(position + 1));
        viewHolder.image.setImageResource(post.getImageId());
        viewHolder.text.setText(post.getName());
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.image)
        ImageView image;

        public PostViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
        }
    }

}
