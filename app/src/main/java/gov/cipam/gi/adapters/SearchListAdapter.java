package gov.cipam.gi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;
import gov.cipam.gi.model.Categories;
import gov.cipam.gi.model.Product;
import gov.cipam.gi.model.Seller;
import gov.cipam.gi.model.States;

/**
 * Created by NITANT SOOD on 10-01-2018.
 */

public class SearchListAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> parentHeaders;
    private Map<String, ArrayList> parentChildListMapping;


    public SearchListAdapter(ArrayList<String> myParentHeaders, Map<String, ArrayList> parentChildListMapping) {
        this.parentHeaders = myParentHeaders;
        this.parentChildListMapping = parentChildListMapping;
    }

    @Override
    public int getGroupCount() {
        return parentHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (parentChildListMapping.get(parentHeaders.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentChildListMapping.get(parentHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.search_list_parent_item, parent, false);
        TextView parentText = convertView.findViewById(R.id.searchListParentName);
        parentText.setText((String) getGroup(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) convertView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.item_search, null);
        TextView tvTitle = convertView.findViewById(R.id.text_search_title);
        TextView tvFiller = convertView.findViewById(R.id.text_search_state);
        TextView tvFiller1 = convertView.findViewById(R.id.text_search_category);
        TextView tvExtra = convertView.findViewById(R.id.text_search_contact);
        ImageView imageView = convertView.findViewById(R.id.image_search);

        String parentName = parentHeaders.get(groupPosition);
        switch (parentName) {
            case Database.GI_PRODUCT:

                Product product = (Product) parentChildListMapping.get(parentName).get(childPosition);
                String strHashtag = convertView.getResources().getString(R.string.note);
                tvTitle.setText(product.getName());
                tvFiller.setText(strHashtag.concat(product.getState()));
                tvFiller1.setText(strHashtag.concat(product.getCategory()));
                tvExtra.setVisibility(View.INVISIBLE);
                Picasso.with(parent.getContext())
                        .load(product.getDpurl())
                        .resize(300, 300)
                        .centerCrop()
                        .into(imageView);
                break;

            case Database.GI_CATEGORY:
                Categories categories = (Categories) parentChildListMapping.get(parentName).get(childPosition);
                tvTitle.setText(categories.getName());
                tvFiller.setVisibility(View.INVISIBLE);
                tvExtra.setVisibility(View.INVISIBLE);
                Picasso.with(parent.getContext())
                        .load(categories.getDpurl())
                        .resize(300, 300)
                        .centerCrop()
                        .into(imageView);
                break;

            case Database.GI_STATE:
                States state = (States) parentChildListMapping.get(parentName).get(childPosition);
                tvTitle.setText(state.getName());
                tvFiller.setVisibility(View.INVISIBLE);
                tvExtra.setVisibility(View.INVISIBLE);
                Picasso.with(parent.getContext())
                        .load(state.getDpurl())
                        .resize(300, 300)
                        .centerCrop()
                        .into(imageView);
                break;

            case Database.GI_SELLER:
                Seller seller = (Seller) parentChildListMapping.get(parentName).get(childPosition);
                tvTitle.setText(seller.getName());
                tvExtra.setText(seller.getcontact());
                imageView.setImageResource(R.drawable.ic_account);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                tvFiller.setText(seller.getaddress());
                break;

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

