package gov.cipam.gi.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gov.cipam.gi.R;

/**
 * Created by karan on 1/25/2018.
 */

public class BioAdapter extends RecyclerView.Adapter<BioAdapter.BioViewHolder> {

    private String[]dev_names;
    TypedArray dev_dp;
    private String[]dev_email;
    Context mContext;
    BioAdapter.setOnBioClickListener mListener;

    public BioAdapter( BioAdapter.setOnBioClickListener mListener) {
        this.mListener = mListener;
    }

    public interface setOnBioClickListener{
        void onItemClicked(View view, int position);
    }


    @Override
    public BioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bio,parent,false);
        return  new BioAdapter.BioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BioViewHolder holder, int position) {

        dev_dp=holder.itemView.getResources().obtainTypedArray(R.array.dev_dp);
        dev_names=holder.itemView.getResources().getStringArray(R.array.dev_names);
        dev_email=holder.itemView.getResources().getStringArray(R.array.dev_emails);

        for (int i=0;i<=position;i++){
            holder.mName.setText(dev_names[i]);
            holder.mEmail.setText(dev_email[i]);
            holder.mDp.setImageDrawable(dev_dp.getDrawable(i));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class BioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName,mEmail;
        ImageView mDp;

        private BioViewHolder(View itemView) {
            super(itemView);

            mName =itemView.findViewById(R.id.text_bio_name);
            mEmail=itemView.findViewById(R.id.text_bio_email);
            mDp =itemView.findViewById(R.id.image_bio);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v,getAdapterPosition());
        }
    }
}