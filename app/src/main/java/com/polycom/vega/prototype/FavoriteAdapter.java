package com.polycom.vega.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polycom.vega.fundamental.ContactObject;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/11/2015.
 */
public class FavoriteAdapter extends ArrayAdapter<ContactObject> {
    public FavoriteAdapter(Context context, ArrayList<ContactObject> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ContactObject contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_item_laytou, parent, false);
        }

        final View finalConvertView = convertView;
        final AdapterView adapterView = (AdapterView) parent;

        ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.favorite_item_imageView);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterView.getOnItemClickListener().onItemClick(adapterView, finalConvertView, position, 0);
            }
        });

        TextView displayNameTextView = (TextView) convertView.findViewById(R.id.favorite_item_displayNameTextView);
        displayNameTextView.setText(contact.getDisplayName());
        displayNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterView.getOnItemClickListener().onItemClick(adapterView, finalConvertView, position, 0);
            }
        });

        return convertView;
    }
}
