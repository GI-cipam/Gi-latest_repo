package gov.cipam.gi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import gov.cipam.gi.model.States;
import gov.cipam.gi.utils.PaletteGenerate;
import gov.cipam.gi.utils.RoundedTransformation;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateViewHolder> {
    setOnStateClickedListener mListener;
    private ArrayList<States> mListOfStates;
    private Bitmap bitmap;
    Context mContext;

    public StatesAdapter(setOnStateClickedListener mListener, ArrayList<States> mListOfStates, Context mContext) {
        this.mListener = mListener;
        this.mListOfStates = mListOfStates;
        this.mContext = mContext;
    }

    public interface setOnStateClickedListener {
        void onStateClickedListener(View view, int position);
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_state_item, parent, false);
        return new StatesAdapter.StateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StateViewHolder holder, final int position) {
        final PaletteGenerate paletteGenerate = new PaletteGenerate();
        holder.mName.setText(mListOfStates.get(position).getName());
        String DpUrl = mListOfStates.get(position).getDpurl();

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(mContext)
                .load(DpUrl)
                .placeholder(R.drawable.image)
                .transform(new RoundedTransformation(5, 0))
                .resize(300, 300)
                .centerCrop()
                .into(holder.mDp, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        bitmap = ((BitmapDrawable) holder.mDp.getDrawable()).getBitmap();
                        paletteGenerate.setViewColor(bitmap, holder.mName);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.mDp.setImageResource(R.drawable.image);
                    }

                });
    }

    @Override
    public int getItemCount() {
        return mListOfStates.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName;
        private ImageView mDp;
        private ProgressBar progressBar;
        private RelativeLayout relativeLayout;

        private StateViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.stateListRelativeLayout);
            mName = itemView.findViewById(R.id.stateListName);
            mDp = itemView.findViewById(R.id.stateListImage);
            progressBar = itemView.findViewById(R.id.progressBarState);
            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onStateClickedListener(v, getAdapterPosition());
        }

    }

}
