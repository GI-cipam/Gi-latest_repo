package gov.cipam.gi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Seller;

/**
 * Created by karan on 3/8/2018.
 */

public class MapsSellerAdapter extends RecyclerView.Adapter<MapsSellerAdapter.MapsSellerViewHolder> {

    ArrayList<Seller> selectedSellerList;
    setOnMapSellerClickedListener mListener;

    public MapsSellerAdapter(setOnMapSellerClickedListener mListener, ArrayList<Seller> selectedSellerList) {
        this.selectedSellerList = selectedSellerList;
        this.mListener = mListener;
    }

    public interface setOnMapSellerClickedListener {
        void onMapSellerClicked(MapsSellerAdapter.MapsSellerViewHolder view, int position);
    }


    @Override
    public MapsSellerAdapter.MapsSellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_seller, parent, false);
        return new MapsSellerAdapter.MapsSellerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MapsSellerViewHolder holder, int position) {

        holder.mName.setText(selectedSellerList.get(position).getName());
        holder.mAddress.setText(selectedSellerList.get(position).getaddress());
        holder.mContact.setText(selectedSellerList.get(position).getcontact());
    }

    @Override
    public int getItemCount() {
        if (selectedSellerList != null) {
            return selectedSellerList.size();
        }
        else
            return 0;
    }


    public class MapsSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName, mContact, mAddress;
        LinearLayout linearLayout;

        private MapsSellerViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout_map_seller);
            mName = itemView.findViewById(R.id.text_map_seller_name);
            mAddress = itemView.findViewById(R.id.text_map_seller_address);
            mContact = itemView.findViewById(R.id.text_map_seller_contact);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onMapSellerClicked(this, getAdapterPosition());
        }
    }
}
