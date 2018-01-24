package gov.cipam.gi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import gov.cipam.gi.R;
import gov.cipam.gi.model.StatePreference;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class StatePreferenceAdapter extends RecyclerView.Adapter<StatePreferenceAdapter.StatePreferenceViewHolder> {

    private List<StatePreference> statePreferences;
    setOnPreferenceStateClickListener mListener;

    public StatePreferenceAdapter(List<StatePreference> statePreferences,setOnPreferenceStateClickListener onPreferenceStateClickListener) {
        this.statePreferences = statePreferences;
        this.mListener = onPreferenceStateClickListener;
    }

    public interface setOnPreferenceStateClickListener{
        void onPreferenceStateClicked(View view, int position);
    }

    @Override
    public StatePreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_state_preference,parent,false);
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
        private RelativeLayout relativeLayout;

        private StatePreferenceViewHolder(View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.state_preference_parent);
            mName =itemView.findViewById(R.id.statePreferenceName);
            mDp =itemView.findViewById(R.id.statePreferenceImage);
            //progressBar=itemView.findViewById(R.id.progressBarCategory);

            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onPreferenceStateClicked(v,getAdapterPosition());
        }
    }

}
