package gov.cipam.gi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Seller;

/**
 * Created by NITANT SOOD on 24-12-2017.
 */

public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SellerViewHolder> {

    private ArrayList<Seller> mSellerList;
    private setOnSellerClickListener mListener;

    public SellerListAdapter(ArrayList<Seller> mSellerList, setOnSellerClickListener mListener) {
        this.mSellerList = mSellerList;
        this.mListener = mListener;
    }

    public interface setOnSellerClickListener{
        void onSellerClicked(View v,int Position);
    }
    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller,parent,false);
        return  new SellerListAdapter.SellerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        holder.textViewSellerContact.setText(mSellerList.get(position).getcontact());
      holder.textViewSellerAddress.setText(mSellerList.get(position).getaddress());
      holder.textViewSellerName.setText(mSellerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSellerList.size();
    }

    public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewSellerName,textViewSellerAddress,textViewSellerContact;

        public SellerViewHolder(View itemView) {
            super(itemView);

            textViewSellerName =itemView.findViewById(R.id.text_seller_name);
            textViewSellerAddress =itemView.findViewById(R.id.text_seller_address);
            textViewSellerContact=itemView.findViewById(R.id.text_seller_contact);
            textViewSellerContact.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onSellerClicked(v,getAdapterPosition());
        }
    }

}
