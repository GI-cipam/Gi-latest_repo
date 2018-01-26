package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.youtubeItem;

/**
 * Created by hp on 1/1/2018.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.YoutubeViewHolder> {
    YoutubeAdapter.setOnYoutubeItemClickedListener mListener;
    ArrayList<youtubeItem> mListOfVideos;
    Context mContext;

    public YoutubeAdapter(YoutubeAdapter.setOnYoutubeItemClickedListener mListener, ArrayList<youtubeItem> mListOfVideos, Context mContext) {
        this.mListener = mListener;
        this.mListOfVideos = mListOfVideos;
        this.mContext = mContext;
    }

    public interface setOnYoutubeItemClickedListener{
        void onItemClickedListener(View view, int position);
    }
    @Override
    public YoutubeAdapter.YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_youtube_item,parent,false);
        return  new YoutubeAdapter.YoutubeViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder(YoutubeAdapter.YoutubeViewHolder holder, int position) {
        holder.mName.setText(mListOfVideos.get(position).getTitle());
        holder.mDesc.setText(mListOfVideos.get(position).getDescription());
        holder.mDate.setText(mListOfVideos.get(position).getPublishedAt());

        Picasso.with(holder.itemView.getContext())
                .load(mListOfVideos.get(position).getUrl())
                .fit()
                .into(holder.mDp);
    }


    @Override
    public int getItemCount() {
        return mListOfVideos.size();
    }

    public class YoutubeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName,mDate,mDesc;
        private ImageView mDp;

        private YoutubeViewHolder(View itemView) {
            super(itemView);

            mName =itemView.findViewById(R.id.youtubeTitle);
            mDesc=itemView.findViewById(R.id.youtubeDesc);
            mDate= itemView.findViewById(R.id.youtubeDate);
            mDp =itemView.findViewById(R.id.youtubeImage);

            mDp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClickedListener(v,getAdapterPosition());
        }
    }
}
