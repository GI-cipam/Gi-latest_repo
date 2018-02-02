package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Uniqueness;

/**
 * Created by NITANT SOOD on 24-12-2017.
 */
public class GiUniquenessListAdapter extends RecyclerView.Adapter<GiUniquenessListAdapter.SellerViewHolder> {

    Context mContext;
    private List<Uniqueness> mUniquenessList;
    private setOnItemClickListener mListener;

    public GiUniquenessListAdapter(Context mContext, List<Uniqueness> mUniquenessList, setOnItemClickListener mListener) {
        this.mContext = mContext;
        this.mUniquenessList = mUniquenessList;
        this.mListener = mListener;
    }

    public interface setOnItemClickListener{
        void onItemClicked(View v, int Position);
    }
    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_uniqueness_item,parent,false);
        return  new GiUniquenessListAdapter.SellerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        holder.tvUniqueness.setText(mUniquenessList.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return mUniquenessList.size();
    }

    public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUniqueness;

        public SellerViewHolder(View itemView) {
            super(itemView);

            tvUniqueness =itemView.findViewById(R.id.card_gi_uniqueness);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v,getAdapterPosition());
        }
    }

}
