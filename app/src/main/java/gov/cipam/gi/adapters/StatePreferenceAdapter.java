package gov.cipam.gi.adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.StatePreference;
import gov.cipam.gi.utils.Constants;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class StatePreferenceAdapter extends RecyclerView.Adapter<StatePreferenceAdapter.StatePreferenceViewHolder> {

    private ArrayList<StatePreference> statePreferences;
    setOnPreferenceStateClickListener mListener;
    ArrayList<StatePreference> statePreference =new ArrayList<>();

    public StatePreferenceAdapter(ArrayList<StatePreference> statePreferences,setOnPreferenceStateClickListener onPreferenceStateClickListener) {
        this.statePreferences = statePreferences;
        this.mListener = onPreferenceStateClickListener;
    }

    public interface setOnPreferenceStateClickListener{
        void onPreferenceStateClicked(Bundle bundle);
    }

    @Override
    public StatePreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_state_preference_item,parent,false);
        return  new StatePreferenceAdapter.StatePreferenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StatePreferenceViewHolder holder, int position) {

        holder.mName.setText(statePreferences.get(position).getState());
        holder.mDp.setImageResource(R.drawable.image1);
    }

    @Override
    public int getItemCount() {
        return statePreferences.size();
    }

    public class StatePreferenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName;
        private ImageView mDp;
        private LinearLayout linearLayout;

        private StatePreferenceViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.state_preference_parent);
            mName =itemView.findViewById(R.id.statePreferenceName);
            mDp =itemView.findViewById(R.id.statePreferenceImage);
            //progressBar=itemView.findViewById(R.id.progressBarCategory);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            mListener.onPreferenceStateClicked(bundle);

            if(statePreference.contains(statePreferences.get(getAdapterPosition()))){
                statePreference.remove(statePreferences.get(getAdapterPosition()));
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
            }
            else {
                statePreference.add(statePreferences.get(getAdapterPosition()));
                linearLayout.setBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.colorSelector));
            }

        }
    }

}
