package gov.cipam.gi.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gov.cipam.gi.R;
import gov.cipam.gi.database.Database;

/**
 * Created by NITANT SOOD on 08-01-2018.
 */

public class SearchCursorAdapter extends CursorAdapter {

    private Cursor cursor;
    private setOnSuggestionClickListener mListener;
    private String mQuery;

    public SearchCursorAdapter(Context context, Cursor c, boolean autoRequery, setOnSuggestionClickListener mListener, String query) {
        super(context, c, autoRequery);
        this.mContext = context;
        this.cursor = c;
        mQuery = query;
        this.mListener = mListener;
    }

    public interface setOnSuggestionClickListener {
        void onSuggestionClickListener(View view, String type, String name);
    }

    public void updateQuery(String query) {
        mQuery = query;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView txtSuggestionName = view.findViewById(R.id.text_suggestion_name);
        ImageView ImgSuggestion = view.findViewById(R.id.image_suggestion);
        TextView txtSuggestionType = view.findViewById(R.id.text_suggestion_type);

        final String suggestion_name = cursor.getString(cursor.getColumnIndex(Database.GI_SEARCH_NAME));
        final String suggestion_type = cursor.getString(cursor.getColumnIndex(Database.GI_SEARCH_TYPE));

        int querySize = mQuery.length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(suggestion_name);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, querySize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, querySize, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        txtSuggestionName.setText(sb);
        txtSuggestionType.setText("in " + suggestion_type);

        if (suggestion_type.equals(Database.GI_HISTORY)) {
            ImgSuggestion.setImageResource(R.drawable.ic_history);
        } else {
            ImgSuggestion.setImageResource(R.drawable.ic_information_outline);
        }
        view.setTag(cursor.getPosition());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = (int) v.getTag();
                if (mCursor.moveToPosition(k)) {
                    String name = mCursor.getString(mCursor.getColumnIndex(Database.GI_SEARCH_NAME));
                    String type = mCursor.getString(mCursor.getColumnIndex(Database.GI_SEARCH_TYPE));

                    mListener.onSuggestionClickListener(v, type, name);
                }
            }
        });
    }
}
