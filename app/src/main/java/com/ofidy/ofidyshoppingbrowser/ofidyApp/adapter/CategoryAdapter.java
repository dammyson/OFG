package com.ofidy.ofidyshoppingbrowser.ofidyApp.adapter;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Category;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.BundleKeys;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.CategoryActivity;


import java.util.List;

/**
 * Created by ari on 10/11/16.
 */
public class CategoryAdapter extends ExpandableRecyclerAdapter<CategoryAdapter.CategoryViewHolder,
        CategoryAdapter.SubCategoryViewHolder> {

    private LayoutInflater mInflator;
    private Context mContext;

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    public CategoryAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        mContext = context;
    }

    // onCreate ...
    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.category_view, parentViewGroup, false);
        return new CategoryViewHolder(recipeView);
    }

    @Override
    public SubCategoryViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.sub_category_view, childViewGroup, false);
        return new SubCategoryViewHolder(ingredientView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(CategoryViewHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        Category recipe = (Category) parentListItem;
        recipeViewHolder.bind(recipe);
    }

    @Override
    public void onBindChildViewHolder(SubCategoryViewHolder ingredientViewHolder, int position, Object childListItem) {
        Category ingredient = (Category) childListItem;
        ingredientViewHolder.bind(ingredient);
    }

    class CategoryViewHolder extends ParentViewHolder {

        TextView mRecipeTextView;
        ImageView mArrowExpandImageView;
        IconicsImageView mIconicsImageView;
        private View mView;

        CategoryViewHolder(final View itemView) {
            super(itemView);
            mRecipeTextView = (TextView) itemView.findViewById(R.id.category_text);
            mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.category_icon);
            mIconicsImageView = (IconicsImageView) itemView.findViewById(R.id.sub_category_icon);
            mArrowExpandImageView.setOnClickListener(v -> {
                if (isExpanded()) {
                    collapseView();
                    this.mArrowExpandImageView.setImageResource(R.drawable.ic_add_black_24dp);
                    itemView.invalidate();
                } else {
                    expandView();
                    this.mArrowExpandImageView.setImageResource(R.drawable.ic_remove_black_24dp);
                    itemView.invalidate();
                }
            });
            mView = itemView;
        }

        public void bind(final Category recipe) {
            mRecipeTextView.setText(recipe.getName());
            IconicsDrawable icon = new IconicsDrawable(mContext).icon(recipe.getLogo());
            mIconicsImageView.setImageDrawable(icon);
            mRecipeTextView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra(BundleKeys.CATEGORY, recipe.getName());
                intent.putExtra(BundleKeys.CAT_GROUP_VALUE, recipe.getId());
                //mContext.startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bundle activityOptions = ActivityOptions.makeScaleUpAnimation(view, 0, 0,
                            view.getWidth(), view.getHeight()).toBundle();
                    mContext.startActivity(intent, activityOptions);
                } else {
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }

        @SuppressLint("NewApi")
        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (expanded) {
                    mArrowExpandImageView.setRotation(ROTATED_POSITION);
                } else {
                    mArrowExpandImageView.setRotation(INITIAL_POSITION);
                }
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                if (expanded) { // rotate clockwise
                    rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                } else { // rotate counterclockwise
                    rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                }

                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                mArrowExpandImageView.startAnimation(rotateAnimation);
            }
        }
    }

    class SubCategoryViewHolder extends ChildViewHolder {

        private TextView mIngredientTextView;
        private IconicsImageView mIconicsImageView;
        private View mView;

        SubCategoryViewHolder(View itemView) {
            super(itemView);
            mIngredientTextView = (TextView) itemView.findViewById(R.id.sub_category_text);
            mIconicsImageView = (IconicsImageView) itemView.findViewById(R.id.sub_category_icon);
            mView = itemView;
        }

        public void bind(final Category ingredient) {
            mIngredientTextView.setText(ingredient.getName());
            if(ingredient.getLogo() != null)
                mIconicsImageView.setIcon(ingredient.getLogo());
            mView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra(BundleKeys.CATEGORY, ingredient.getName());
                intent.putExtra(BundleKeys.CATEGORY_VALUE, ingredient.getId());
                //intent.putExtra(BundleKeys.CAT_GROUP_VALUE, ingredient.getGroudId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bundle activityOptions = ActivityOptions.makeScaleUpAnimation(view, 0, 0,
                            view.getWidth(), view.getHeight()).toBundle();
                    mContext.startActivity(intent, activityOptions);
                } else {
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
