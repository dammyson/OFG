package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.ofidy.ofidyshoppingbrowser.Materials.utils.DeviceUtils;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_POST = 1;
    private static final int TYPE_FOOTER = 2;

    private static final int SHOP_TYPE_PRODUCT = 1;
    private static final int CATALOG_TYPE_PRODUCT = 2;

    private final LayoutInflater mLayoutInflater;
    private final List<Product> mProducts;
    private final Picasso mPicasso;
    private final View.OnClickListener mItemClickListener;
    private final Context mContext;
    private CharSequence mFooterText;

    // animation stuff
    private static final DecelerateInterpolator ANIM_INTERPOLATOR = new DecelerateInterpolator();
    private boolean mAnimateOnAttach = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mAnimationDelay = 0; // for staggering
    private int mType;

    public ProductAdapter(Context context, List<Product> products, Picasso picasso,
                          View.OnClickListener itemClickListener, int type) {
        mContext = context;
        mPicasso = picasso;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProducts = products;
        mItemClickListener = itemClickListener;
        mType = type;

        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        int count = mProducts.size();
        if (mFooterText != null) {
            ++count; // +1 for footer
        }
        return count;
    }

    public Object getItem(int position) {
        if (position < mProducts.size()) {
            return mProducts.get(position);
        } else {
            return mFooterText;
        }
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == TYPE_POST) {
            if(!TextUtils.isEmpty(((Product) getItem(position)).getId()))
                return Integer.parseInt(((Product) getItem(position)).getId());
            else
                return 0;
        } else {
            return -9999;   // footer
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mProducts.size()) {
            return TYPE_POST;
        } else {
            return TYPE_FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POST) {
            View view;
            if(mType == SHOP_TYPE_PRODUCT)
                view = mLayoutInflater.inflate(R.layout.product_list_item, parent, false);
            else
                view = mLayoutInflater.inflate(R.layout.product_list_item_small, parent, false);
            return new PostViewHolder(view, mItemClickListener);
        } else if (viewType == TYPE_FOOTER) {
            View view = mLayoutInflater.inflate(R.layout.product_list_footer, parent, false);
            // make sure the footer spans the entire grid
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            if (lp == null) {
                lp = new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            lp.setFullSpan(true);
            view.setLayoutParams(lp);

            return new FooterViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PostViewHolder) {
            PostViewHolder postVH = (PostViewHolder) viewHolder;
            Product product = (Product) getItem(position);
            bindPost(postVH, product);
        } else if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder footerVH = (FooterViewHolder) viewHolder;
            CharSequence footerText = (CharSequence) getItem(position);
            bindFooter(footerVH, footerText);
        } else {
            throw new IllegalArgumentException("Invalid ViewHolder type: " +
                    viewHolder.getClass().getSimpleName());
        }
    }

    private void bindPost(PostViewHolder viewHolder, Product product) {
        if(!TextUtils.isEmpty(product.getName()))
            viewHolder.title.setText(Html.fromHtml(product.getName()));
        if (! TextUtils.isEmpty(product.getImage())) {
            String imageUrl = "https://m.ofidy.com/ProductImages"+product.getImage();
            mPicasso.load(imageUrl).placeholder(R.drawable.logo_ofidy_s)
                    .fit().centerCrop()
                    .into(viewHolder.image);
        }
        viewHolder.price.setText(product.getDisplayPrice());
        if(viewHolder.priceLayer.getVisibility() == View.VISIBLE){
            if(product.getQuantity() <= 0 || product.getPrice() <= 0)
                viewHolder.outImage.setVisibility(View.VISIBLE);
            else
                viewHolder.outImage.setVisibility(View.GONE);
            if(product.getPrice() <= 0)
                viewHolder.price.setVisibility(View.GONE);
        }

        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        // only show card animations when the adapter is initially created
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimateOnAttach = false;
            }
        }, 1000);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        if (! (viewHolder instanceof PostViewHolder)) {
            return;
        }
        // play a little cards animation similar to Google Now and Google+
        PostViewHolder postVH = (PostViewHolder) viewHolder;
        if (mAnimateOnAttach) {
            final View itemView = postVH.itemView;
            itemView.setTranslationY(DeviceUtils.dpToPx(300));
            itemView.setRotation(10);
            itemView.setAlpha(0f);
            ViewCompat.animate(itemView)
                    .withLayer()
                    .translationY(0f)
                    .rotation(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(ANIM_INTERPOLATOR)
                    .setStartDelay(mAnimationDelay)   // stagger the animation
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            mAnimationDelay += 100;
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            mAnimationDelay -= 100;
                        }

                        @Override
                        public void onAnimationCancel(View view) {
                            mAnimationDelay -= 100;
                            itemView.setTranslationY(0f);
                            itemView.setRotation(0f);
                            itemView.setAlpha(1f);
                        }
                    })
                    .start();
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof PostViewHolder) {
            ((PostViewHolder) holder).cleanup();
        }
    }

    private void bindFooter(FooterViewHolder viewHolder, CharSequence footerText) {
        viewHolder.textView.setText(footerText);
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void showFooter(CharSequence footerText) {
        mFooterText = footerText;
    }

    public void hideFooter() {
        mFooterText = null;
    }


    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.product_title)
        TextView title;
        @Bind(R.id.product_price)
        TextView price;
        @Bind(R.id.product_price_old)
        TextView oldPrice;
        @Bind(R.id.product_image)
        ImageView image;
        @Bind(R.id.product_out)
        ImageView outImage;
        @Bind(R.id.price_layer)
        View priceLayer;

        public PostViewHolder(@NonNull View view, View.OnClickListener clickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(clickListener);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // courtesy http://stackoverflow.com/a/33961706/504611
        public void cleanup() {
            Picasso.with(image.getContext())
                    .cancelRequest(image);
        }
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.product_limit_exceeded)
        TextView textView;

        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
