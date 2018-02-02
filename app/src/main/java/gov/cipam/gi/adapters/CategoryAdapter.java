package gov.cipam.gi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import gov.cipam.gi.R;
import gov.cipam.gi.model.Categories;

/**
 * Created by NITANT SOOD on 28-11-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Categories> mCategoryList;
    Context mContext;
    setOnCategoryClickListener mListener;
    private int iconResource[];
    private int bgDrawable[];

    public CategoryAdapter(ArrayList<Categories> mCategoryList, Context mContext, setOnCategoryClickListener mListener) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public interface setOnCategoryClickListener{
        void onCategoryClicked(View view, int position);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.card_view_category_item_alternate,parent,false);
        return  new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {

        iconResource= new int[]{R.drawable.corn,R.drawable.truck,R.drawable.palette,R.drawable.cookie,R.drawable.earth,R.drawable.view_dashboard};
        bgDrawable=new int[]{R.drawable.circle_bg_blue,R.drawable.circle_bg_orange,R.drawable.circle_bg_red,R.drawable.circle_bg_green,R.drawable.circle_bg_pink,R.drawable.circle_bg_purple};
        String DpUrl=mCategoryList.get(position).getDpurl();
        holder.mName.setText(mCategoryList.get(position).getName());

        for (int i=0;i<position+1;i++){

            holder.view.setBackgroundResource(iconResource[i]);
            holder.mDp.setImageResource(bgDrawable[i]);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName;
        private ImageView mDp;
        private LinearLayout linearLayout;
        private View view;

        private CategoryViewHolder(View itemView) {
            super(itemView);

            view=itemView.findViewById(R.id.categoryIcon);
            linearLayout=itemView.findViewById(R.id.categoryLinearLayout);
            mName =itemView.findViewById(R.id.card_name_category_alternate);
            mDp =itemView.findViewById(R.id.image_category);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onCategoryClicked(v,getAdapterPosition());
        }
    }

}