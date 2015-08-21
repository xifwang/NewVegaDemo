package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeScreenFragment extends Fragment implements IActivity, IDataBind {
    private RelativeLayout view;
    private EditText fragment_homescreen_contactEditText;
    private Button placeACallButton;
    private boolean inACall;
    private int conferenceIndex;
    private VegaApplication application;

    public boolean isInACall() {
        return inACall;
    }

    public void setInACall(boolean inACall) {
        this.inACall = inACall;

        if (placeACallButton != null) {
            placeACallButton.setText(inACall ? getString(R.string.button_endCall_text) : getString(R.string.button_placeACall_text));
        }
    }

    public HomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = (RelativeLayout) inflater.inflate(R.layout.fragment_homescreen, container, false);

        this.initComponent();
        this.initComponentState();
        this.initAnimation();
        this.registerNotification();
        this.dataBind();

        getActivity().setTitle("Connected with " + application.getServerUrl());

        return this.view;
    }

    private View.OnClickListener onPlaceACallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!inACall) {
                placeACall();
            } else {
                endCall();
            }
        }
    };

    private void placeACall() {
        String url = application.getServerUrl() + "/rest/conferences?_dc=1439978043968";
        final String destinationIp = fragment_homescreen_contactEditText.getText().toString();
        final ProgressDialog dialog = new ProgressDialog(view.getContext());
        dialog.setMessage(getString(R.string.message_placeACall));

        try {
            dialog.show();

            JSONObject json = new JSONObject("{\"address\":\"" + destinationIp + "\",\"dialType\":\"AUTO\",\"rate\":\"0\"}");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    // TODO: Need to improve.
                    conferenceIndex = Integer.parseInt(error.getMessage().substring(error.getMessage().indexOf("connections")).charAt(13) + "");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setMessage("In call with " + destinationIp);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton(getString(R.string.button_endCall_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endCall();

                            dialog.dismiss();
                        }
                    });
                    alertDialogBuilder.show();
                }
            };

            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonArrayRequest);

            setInACall(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void endCall() {
        String url = application.getServerUrl() + "/rest/conferences/0/connections/" + conferenceIndex;

        try {
            JSONObject json = new JSONObject("{\"action\":\"hangup\"}");
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Call has been ended.", Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, json, responseListener, errorListener);

            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setInACall(false);
    }

    @Override
    public void initComponent() {
        this.application = (VegaApplication) getActivity().getApplicationContext();

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
    public void dataBind() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            Toast.makeText(this.view.getContext(), bundle.getString("response"), Toast.LENGTH_SHORT);
        }
    }
}
