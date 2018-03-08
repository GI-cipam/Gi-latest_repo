package gov.cipam.gi.adapters;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import gov.cipam.gi.R;

/**
 * Created by karan on 3/8/2018.
 */

public class MapsSellerAdapter extends RecyclerView.Adapter<MapsSellerAdapter.MapsSellerViewHolder> {

    private String[] name;
    private String[] address;
    private String[] contacts;
    setOnMapSellerClickedListener mListener;

    public MapsSellerAdapter(setOnMapSellerClickedListener mListener) {
        this.mListener = mListener;
    }

    public interface setOnMapSellerClickedListener {
        void onMapSellerClicked(MapsSellerAdapter.MapsSellerViewHolder view, int position);
    }


    @Override
    public MapsSellerAdapter.MapsSellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_seller,parent,false);
        return  new MapsSellerAdapter.MapsSellerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MapsSellerViewHolder holder, int position) {
        name=holder.itemView.getContext().getResources().getStringArray(R.array.dev_names);
        address=holder.itemView.getContext().getResources().getStringArray(R.array.dev_emails);
        contacts=holder.itemView.getContext().getResources().getStringArray(R.array.dev_emails);

        for(int i=0;i<=position;i++){
            holder.mName.setText(name[i]);
            holder.mAddress.setText(name[i]);
            holder.mContact.setText(contacts[i]);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MapsSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName,mContact,mAddress;
        LinearLayout linearLayout;

        private MapsSellerViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.linear_layout_map_seller);
            mName=itemView.findViewById(R.id.text_map_seller_name);
            mAddress=itemView.findViewById(R.id.text_map_seller_address);
            mContact=itemView.findViewById(R.id.text_map_seller_contact);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onMapSellerClicked(this,getAdapterPosition());
        }
    }
}
