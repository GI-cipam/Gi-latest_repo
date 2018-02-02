package gov.cipam.gi.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;
import retrofit2.http.Url;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Categories> mCategoryList;
    Context mContext;
    setOnCategoryClickListener mListener;

    public CategoryAdapter(ArrayList<Categories> mCategoryList, Context mContext, setOnCategoryClickListener mListener) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public interface setOnCategoryClickListener{
        void onCategoryClicked(View view, int position);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_category_item_alternate,parent,false);
        return  new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {

        String DpUrl=mCategoryList.get(position).getDpurl();
        holder.mName.setText(mCategoryList.get(position).getName());

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(mContext)
                .load(DpUrl)
                .resize(400,600)
                .into(holder.mDp, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.mDp.setImageResource(R.drawable.image);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName;
        private ImageView mDp;
        private ProgressBar progressBar;
        private LinearLayout linearLayout;

        private CategoryViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.categoryLinearLayout);
            mName =itemView.findViewById(R.id.card_name_category_alternate);
            mDp =itemView.findViewById(R.id.image_category);
            progressBar=itemView.findViewById(R.id.progressBarCategoryAlternate);
            //mDp.setImageResource(R.drawable.circle);
            mDp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCategoryClicked(v,getAdapterPosition());
        }
    }

}