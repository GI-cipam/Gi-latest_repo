package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Bio;

/**
 * Created by karan on 1/25/2018.
 */

public class BioAdapter extends RecyclerView.Adapter<BioAdapter.BioViewHolder> {

    List<Bio> mBioList;
    Context mContext;
    BioAdapter.setOnBioClickListener mListener;

    public BioAdapter(List<Bio> mBioList, BioAdapter.setOnBioClickListener mListener) {
        this.mBioList = mBioList;
        this.mListener = mListener;
    }

    public interface setOnBioClickListener{
        void onItemClicked(View view, int position);
    }


    @Override
    public BioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_bio_item,parent,false);
        return  new BioAdapter.BioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BioViewHolder holder, int position) {
        holder.mEmail.setText(mBioList.get(position).getEmail());
        switch (position){
            case 0:holder.mDp.setImageResource(R.drawable.kd);
                holder.mName.setText("Kunwar Deepak");
                break;

        }



    }

    @Override
    public int getItemCount() {
        return mBioList.size();
    }

    public class BioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName,mEmail;
        ImageView mDp;

        private BioViewHolder(View itemView) {
            super(itemView);

            mName =itemView.findViewById(R.id.bioName);
            mEmail=itemView.findViewById(R.id.bioEmail);
            mDp =itemView.findViewById(R.id.bioImage);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v,getAdapterPosition());
        }
    }
}