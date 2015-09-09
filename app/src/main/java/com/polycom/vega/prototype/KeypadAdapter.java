package com.polycom.vega.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/9/2015.
 */
public class KeypadAdapter extends ArrayAdapter<String> {
    public KeypadAdapter(Context context, ArrayList<String> keyList) {
        super(context, 0, keyList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_keypad_key, parent, false);
        ImageView imageView = ((ImageView) convertView.findViewById(R.id.fragment_keypad_key_keyImageView));

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.icon_keypad_1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.icon_keypad_2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.icon_keypad_3);
                break;
            case 3:
                imageView.setImageResource(R.drawable.icon_keypad_4);
                break;
            case 4:
                imageView.setImageResource(R.drawable.icon_keypad_5);
                break;
            case 5:
                imageView.setImageResource(R.drawable.icon_keypad_6);
                break;
            case 6:
                imageView.setImageResource(R.drawable.icon_keypad_7);
                break;
            case 7:
                imageView.setImageResource(R.drawable.icon_keypad_8);
                break;
            case 8:
                imageView.setImageResource(R.drawable.icon_keypad_9);
                break;
            case 9:
                imageView.setImageResource(R.drawable.icon_keypad_dot);
                break;
            case 10:
                imageView.setImageResource(R.drawable.icon_keypad_0);
                break;
            case 11:
                imageView.setImageResource(R.drawable.icon_keypad_slash);
                break;
            case 13:
                imageView.setImageResource(R.drawable.icon_keypad_call);
                break;
            case 14:
                imageView.setImageResource(R.drawable.icon_keypad_back);
                break;
            default:
                break;
        }

        return convertView;
    }
}
