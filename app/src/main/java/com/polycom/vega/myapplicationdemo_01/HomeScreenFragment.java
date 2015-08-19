package com.polycom.vega.myapplicationdemo_01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.Constants;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

import org.json.JSONException;
import org.json.JSONObject;

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
        String url = Constants.getServerUrl() + "/rest/conferences?_dc=1439978043968";

        try {
            JSONObject json = new JSONObject("{\"address\":\"" + fragment_homescreen_contactEditText.getText().toString() + "\",\"dialType\":\"AUTO\",\"rate\":\"0\"}");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("jsonObjectResponse: ", response.toString());
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
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
}
