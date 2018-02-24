package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class GIAdapter extends RecyclerView.Adapter<GIAdapter.GiViewHolder> {

    private ArrayList<Categories> mCategoryList;
    Context mContext;
    setOnGIClickListener mListener;

    public GIAdapter(ArrayList<Categories> mCategoryList, Context mContext, setOnGIClickListener mListener) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public interface setOnGIClickListener{
        void onGIClicked(View view, int position);
    }

    @Override
    public GiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_gi_item,parent,false);
        return  new GIAdapter.GiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GiViewHolder holder, int position) {

        String DpUrl=mCategoryList.get(position).getDpurl();
        holder.mName.setText(mCategoryList.get(position).getName());
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(mContext)
                .load(DpUrl)
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

    public class GiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName;
        private ImageView mDp;
        private ProgressBar progressBar;
        private RelativeLayout relativeLayout;

        private GiViewHolder(View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.giRelativeLayout);
            mName =itemView.findViewById(R.id.card_name_gi);
            mDp =itemView.findViewById(R.id.card_dp_gi);
            progressBar=itemView.findViewById(R.id.progressBarGi);

            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onGIClicked(v,getAdapterPosition());
        }
    }

}
