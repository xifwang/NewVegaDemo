package com.polycom.vega.prototype;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

/**
 * Created by zerocool on 8/24/15.
 */
public class OptionItemLayoutHeader extends Fragment implements IActivity, IDataBind {
    private LinearLayout header;
    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView optionsImageView;

    public String getTitle() {
        return (titleTextView != null ? titleTextView.getText().toString() : "");
    }

    public void setTitle(String title) {
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    public OptionItemLayoutHeader() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        header = (LinearLayout) inflater.inflate(R.layout.header_option_fragment, container,
                false);

        return header;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent();
        initComponentState();
        dataBind();
    }

    @Override
    public void initComponent() {
        backImageView = (ImageView) header.findViewById(R.id
                .header_option_item_layout_back_icon_imageButton);
        titleTextView = (TextView) header.findViewById(R.id
                .header_option_item_layout_options_titleTextView);
        optionsImageView = (ImageView) header.findViewById(R.id
                .header_option_item_layout_options_icon_imageButton);
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {

    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void dataBind() {

    }
}
