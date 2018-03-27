package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Product;

/**
 * Created by NITANT SOOD on 29-11-2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private ArrayList<Product> GIList;
    private setOnProductClickedListener mListener;

    public ProductListAdapter(ArrayList<Product> GIList, setOnProductClickedListener mListener) {
        this.GIList = GIList;
        this.mListener = mListener;
    }

    public interface setOnProductClickedListener {
        void onProductClicked(ProductListAdapter.ProductViewHolder view, int position);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductListAdapter.ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        final String strHashtag = holder.itemView.getResources().getString(R.string.note);
        ViewCompat.setTransitionName(holder.itemView.findViewById(R.id.image_product), String.valueOf(position + "_image"));
        holder.mTitle.setText(GIList.get(position).getName());
        holder.mFiller.setText(GIList.get(position).getDetail());
        holder.mState.setText(strHashtag.concat(GIList.get(position).getState()));
        holder.mCategory.setText(strHashtag.concat(GIList.get(position).getCategory()));
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(holder.itemView.getContext())
                .load(GIList.get(position).getDpurl())
                .fit()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        ViewCompat.setTransitionName(holder.imageView, GIList.get(position).getUid());
    }

    @Override
    public int getItemCount() {
        return GIList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle, mFiller, mState, mCategory;
        public ImageView imageView;
        private LinearLayout linearLayout;
        private ProgressBar progressBar;

        private ProductViewHolder(View itemView) {
            super(itemView);

            linearLayout= itemView.findViewById(R.id.parent_product);
            mTitle = itemView.findViewById(R.id.text_product_title);
            mFiller = itemView.findViewById(R.id.text_product_desc);
            mState = itemView.findViewById(R.id.text_product_state);
            mCategory = itemView.findViewById(R.id.text_product_category);
            imageView = itemView.findViewById(R.id.image_product);
            progressBar=itemView.findViewById(R.id.product_progress);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onProductClicked(this, getAdapterPosition());
        }
    }
}
