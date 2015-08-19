package com.polycom.vega.myapplicationdemo_01;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeScreenFragment extends Fragment implements IActivity, IDataBind {
    private LinearLayout view;
    private EditText fragment_homescreen_contactEditText;
    private Button placeACallButton;

    public HomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = (LinearLayout) inflater.inflate(R.layout.fragment_homescreen, container, false);

        this.initComponent();
        this.initComponentState();
        this.initAnimation();
        this.registerNotification();
        this.DataBind();

        return this.view;
    }

    private View.OnClickListener onPlaceACallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            placeACall();
        }
    };

    private void placeACall() {
        try {
            new PlaceACallAsyncTask().execute(this.fragment_homescreen_contactEditText.getText().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initComponent() {
        this.fragment_homescreen_contactEditText = (EditText) this.view.findViewById(R.id.fragment_homescreen_contactEditText);

        this.placeACallButton = (Button) this.view.findViewById(R.id.fragment_homescreen_placeACallButton);
        this.placeACallButton.setOnClickListener(this.onPlaceACallClickListener);
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
            Toast.makeText(this.view.getContext(), bundle.getString("response"), Toast.LENGTH_SHORT);
        }
    }

    class PlaceACallAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            placeACallButton.setEnabled(true);
        }

        @Override
        protected Void doInBackground(String... params) {
            String contact = params[0];

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            placeACallButton.setEnabled(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            placeACallButton.setEnabled(true);
        }
    }
}

