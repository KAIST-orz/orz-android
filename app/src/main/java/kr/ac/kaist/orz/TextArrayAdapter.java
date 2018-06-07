package kr.ac.kaist.orz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// An ArrayAdapter to show simple TextViews on a ListView,
// which sets the last element's color to lighter to others.
public class TextArrayAdapter extends ArrayAdapter {
    TextArrayAdapter(Context context, List<String> elements) {
        super(context, R.layout.text_view, elements);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.text_view, parent, false);
        }
        TextView textView = (TextView) convertView;

        // Set text.
        textView.setText((String) getItem(position));
        // Change color for the last element.
        if (position == getCount() - 1) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }
        else {
            textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }

        return super.getView(position, convertView, parent);
    }
}