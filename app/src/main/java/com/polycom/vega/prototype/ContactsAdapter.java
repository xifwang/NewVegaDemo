package com.polycom.vega.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.polycom.vega.fundamental.ContactObject;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/11/2015.
 */
public class ContactsAdapter extends ArrayAdapter<ContactObject> {
    public ContactsAdapter(Context context, ArrayList<ContactObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ContactObject contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts_list_item, parent, false);
        }

        final View finalConvertView = convertView;
        final AdapterView adapterView = (AdapterView) parent;

        ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.fragment_contact_list_item_avatarImageView);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterView.getOnItemClickListener().onItemClick(adapterView, finalConvertView, position, 0);
            }
        });

        TextView displayNameTextView = (TextView) convertView.findViewById(R.id.fragment_contact_list_item_displayNameTextView);
        displayNameTextView.setText(contact.getDisplayName());
        displayNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterView.getOnItemClickListener().onItemClick(adapterView, finalConvertView, position, 0);
            }
        });

        ImageButton deleteImageButton = (ImageButton) convertView.findViewById(R.id.fragment_contact_list_item_deleteUserImageButton);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
