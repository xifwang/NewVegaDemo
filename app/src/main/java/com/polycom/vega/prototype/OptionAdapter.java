package com.polycom.vega.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polycom.vega.fundamental.OptionObject;

import java.util.ArrayList;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class OptionAdapter extends ArrayAdapter<OptionObject> {
    public OptionAdapter(Context context, ArrayList<OptionObject> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OptionObject option = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.optionlist_item_layout, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.layout_optionlist_item_icon_imageView);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.layout_optionlist_item_optionTitle_TextView);

        iconImageView.setImageResource(option.getIconId());
        titleTextView.setText(option.getTitle());

        return convertView;
    }
}
