package com.polycom.vega.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michael on 9/10/2015.
 */
public class RecentCallAdapter extends ArrayAdapter<String> {

    public RecentCallAdapter(Context context, ArrayList<String> calls) {
        super(context, 0, calls);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        String call = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recentcalllist_item_layout, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.recentcalllist_item_textView);
        textView.setText(call);

        final View finalConvertView = convertView;
        final AdapterView adapterView = (AdapterView) parent;

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterView.getOnItemClickListener().onItemClick(adapterView, finalConvertView, position, 0);
            }
        });

        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.recentcalllist_item_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
