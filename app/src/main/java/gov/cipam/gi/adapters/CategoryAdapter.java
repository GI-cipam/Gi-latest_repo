package gov.cipam.gi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Categories> mCategoryList;
    private setOnCategoryClickListener mListener;

    public CategoryAdapter(ArrayList<Categories> mCategoryList, setOnCategoryClickListener mListener) {
        this.mCategoryList = mCategoryList;
        this.mListener = mListener;
    }

    public interface setOnCategoryClickListener {
        void onCategoryClicked(View view, int position);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {

        final int[] iconResource = new int[]{R.drawable.ic_category_agriculture, R.drawable.ic_category_manufactured, R.drawable.ic_category_handicraft, R.drawable.ic_category_foodstuff, R.drawable.ic_category_natural, R.drawable.ic_category_textile};
        final int[] bgDrawable = new int[]{R.drawable.circle_bg_agriculture_blue, R.drawable.circle_bg_manufactured_orange, R.drawable.circle_bg_handicraft_red, R.drawable.circle_bg_foodstuff_green, R.drawable.circle_bg_natural_pink, R.drawable.circle_bg_textile_purple};
        holder.mName.setText(mCategoryList.get(position).getName());

        for (int i = 0; i <= position; i++) {
            holder.view.setBackgroundResource(iconResource[i]);
            holder.mDp.setImageResource(bgDrawable[i]);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName;
        private ImageView mDp;
        private LinearLayout linearLayout;
        private View view;

        private CategoryViewHolder(View itemView) {
            super(itemView);


            view = itemView.findViewById(R.id.categoryIcon);
            linearLayout = itemView.findViewById(R.id.categoryLinearLayout);
            mName = itemView.findViewById(R.id.text_category_alternate);
            mDp = itemView.findViewById(R.id.image_category);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCategoryClicked(v, getAdapterPosition());
        }
    }

}