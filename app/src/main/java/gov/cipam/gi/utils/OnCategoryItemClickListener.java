package gov.cipam.gi.utils;
import android.os.Bundle;
import android.view.View;

//Interface to add user defined functions to handle click activities on recyclerview
public interface OnCategoryItemClickListener {
    void onClick(Bundle bundle);
    void onLongClick(View view, int position);
}



