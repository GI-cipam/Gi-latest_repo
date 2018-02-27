package gov.cipam.gi.adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.StatePreference;
import gov.cipam.gi.utils.RoundedTransformation;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class StatePreferenceAdapter extends RecyclerView.Adapter<StatePreferenceAdapter.StatePreferenceViewHolder> {

    private ArrayList<StatePreference> statePreferences;
    private ArrayList<StatePreference> statePreference =new ArrayList<>();
    setOnPreferenceStateClickListener mListener;

    public StatePreferenceAdapter(ArrayList<StatePreference> statePreferences,setOnPreferenceStateClickListener onPreferenceStateClickListener) {
        this.statePreferences = statePreferences;
        this.mListener = onPreferenceStateClickListener;
    }

    public interface setOnPreferenceStateClickListener{
        void onPreferenceStateClicked(Bundle bundle);
    }

    @Override
    public StatePreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state_preference_item,parent,false);
        return  new StatePreferenceAdapter.StatePreferenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StatePreferenceViewHolder holder, int position) {

        holder.mName.setText(statePreferences.get(position).getState());

        holder.progressBar.setVisibility(View.VISIBLE);

        Picasso.with(holder.itemView.getContext())
                .load("https://www.w3schools.com/howto/img_fjords.jpg")
                .transform(new RoundedTransformation(10,0))
                .resize(200,200)
                .centerCrop()
                .into(holder.mDp, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.mDp.setImageResource(R.drawable.ic_placeholder_image);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return statePreferences.size();
    }

    public class StatePreferenceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName;
        private ImageView mDp;
        private RelativeLayout relativeLayout;
        private ProgressBar progressBar;

        private StatePreferenceViewHolder(View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.state_preference_parent);
            mName =itemView.findViewById(R.id.text_state_preference);
            mDp =itemView.findViewById(R.id.image_state_preference);
            progressBar=itemView.findViewById(R.id.progressBarStatePreference);

            progressBar.setVisibility(View.INVISIBLE);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            mListener.onPreferenceStateClicked(bundle);

            if(statePreference.contains(statePreferences.get(getAdapterPosition()))){
                statePreference.remove(statePreferences.get(getAdapterPosition()));
                relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            }
            else {
                statePreference.add(statePreferences.get(getAdapterPosition()));
                relativeLayout.setBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.colorSelector));
            }

        }
    }

}
