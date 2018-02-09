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

    private String[]dev_names;
    private String[]dev_email;
    /*int[] dev_dp={R.drawable.circle_bg_red,
    R.drawable.circle_bg_purple,R.drawable.circle_bg_blue
    ,R.drawable.circle_bg_green,R.drawable.circle_bg_orange};*/
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_bio_item,parent,false);
        return  new BioAdapter.BioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BioViewHolder holder, int position) {

        dev_names=holder.itemView.getResources().getStringArray(R.array.dev_names);
        dev_email=holder.itemView.getResources().getStringArray(R.array.dev_emails);
        if(position==0){
            holder.mDp.setImageResource(R.drawable.ns1);
        }else {
            holder.mDp.setImageResource(R.drawable.kd);
        }
        for (int i=0;i<=position;i++){
            holder.mName.setText(dev_names[i]);
            holder.mEmail.setText(dev_email[i]);
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