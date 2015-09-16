package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/9/2015.
 */
public class KeypadFragment extends VegaFragment implements IActivity, AdapterView.OnItemClickListener, Thread.UncaughtExceptionHandler {
    private int conferenceIndex;
    private ArrayList<String> keyList;
    private KeypadAdapter keypadAdapter;
    private GridView keypadGridView;
    TextView numberTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = (VegaApplication) getActivity().getApplication();
        fragment = (LinearLayout) inflater.inflate(R.layout.fragment_keypad, container, false);
        context = fragment.getContext();
        fragmentManager = getActivity().getSupportFragmentManager();

        Thread.currentThread().setUncaughtExceptionHandler(this);

        initComponent();
        initComponentState();
        initAnimation();

        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.5F);

        switch (position) {
            case 0:
                numberTextView.setText(numberTextView.getText() + "1");
                break;
            case 1:
                numberTextView.setText(numberTextView.getText() + "2");
                break;
            case 2:
                numberTextView.setText(numberTextView.getText() + "3");
                break;
            case 3:
                numberTextView.setText(numberTextView.getText() + "4");
                break;
            case 4:
                numberTextView.setText(numberTextView.getText() + "5");
                break;
            case 5:
                numberTextView.setText(numberTextView.getText() + "6");
                break;
            case 6:
                numberTextView.setText(numberTextView.getText() + "7");
                break;
            case 7:
                numberTextView.setText(numberTextView.getText() + "8");
                break;
            case 8:
                numberTextView.setText(numberTextView.getText() + "9");
                break;
            case 9:
                numberTextView.setText(numberTextView.getText() + ".");
                break;
            case 10:
                numberTextView.setText(numberTextView.getText() + "0");
                break;
            case 11:
                numberTextView.setText(numberTextView.getText() + "#");
                break;
            case 13:
                placeACall();
                break;
            case 14:
                String number = numberTextView.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    numberTextView.setText(number.substring(0, number.length() - 1));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initComponent() {
        numberTextView = (TextView) fragment.findViewById(R.id.fragment_keypad_numberTextView);

        keyList = new ArrayList<String>();
        keyList.add("1");
        keyList.add("2");
        keyList.add("3");
        keyList.add("4");
        keyList.add("5");
        keyList.add("6");
        keyList.add("7");
        keyList.add("8");
        keyList.add("9");
        keyList.add("dot");
        keyList.add("0");
        keyList.add("slash");
        keyList.add("");
        keyList.add("call");
        keyList.add("back");

        keypadAdapter = new KeypadAdapter(context, keyList);

        keypadGridView = (GridView) fragment.findViewById(R.id.fragment_keypad_keypadGridView);
        keypadGridView.setAdapter(keypadAdapter);
        keypadGridView.setOnItemClickListener(this);
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
    public void uncaughtException(Thread thread, Throwable ex) {

    }

    private void placeACall() {
        String url = application.getServerUrl() + "/rest/conferences?_dc=1439978043968";
        final String destinationIp = "172.21.97.215";
        final ProgressDialog dialog = new ProgressDialog(fragment.getContext());
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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.getContext
                            ());
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
    }
}
