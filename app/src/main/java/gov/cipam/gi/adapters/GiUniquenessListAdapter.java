package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Uniqueness;

/**
 * Created by NITANT SOOD on 24-12-2017.
 */
public class GiUniquenessListAdapter extends RecyclerView.Adapter<GiUniquenessListAdapter.UniquenessViewHolder> {

    Context mContext;
    List<Uniqueness> mUniquenessList;
    public GiUniquenessListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public UniquenessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_uniqueness,parent,false);
        return  new GiUniquenessListAdapter.UniquenessViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(UniquenessViewHolder holder, int position) {
        final String[] unique=holder.itemView.getResources().getStringArray(R.array.dummy_text);
        //holder.tvUniqueness.setText(mUniquenessList.get(position).getInfo());

        for(int i=0;i<=position;i++){
            holder.tvUniqueness.setText(unique[i]);
            holder.tvposition.setText(String.valueOf(i+1));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class UniquenessViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUniqueness,tvposition;

        public UniquenessViewHolder(View itemView) {
            super(itemView);
            tvUniqueness = itemView.findViewById(R.id.text_uniqueness);
            tvposition=itemView.findViewById(R.id.text_uniqueness_position);
        }
    }
}
