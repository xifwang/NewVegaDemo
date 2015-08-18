package com.polycom.vega.myapplicationdemo_01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeScreenFragment extends Fragment implements IActivity, IDataBind {
    private LinearLayout view;
    private TextView contentTextView;

    public HomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = (LinearLayout) inflater.inflate(R.layout.fragment_homescreen, container, false);

        this.initComponent();
        this.initComponentState();
        this.registerNotification();
        this.DataBind();

        return this.view;
    }

    @Override
    public void initComponent() {
        this.contentTextView = (TextView) this.view.findViewById(R.id.fragementhomescreen_contentTextView);
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(1000);
        LayoutAnimationController animationController = new LayoutAnimationController(scaleAnimation, 0.1f);

        this.view.setLayoutAnimation(animationController);
    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void DataBind() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            this.contentTextView.setText(bundle.getString("response"));
        }
    }
}
