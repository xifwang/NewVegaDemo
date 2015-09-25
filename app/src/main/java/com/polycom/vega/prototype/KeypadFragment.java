package com.polycom.vega.prototype;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.polycom.vega.fundamental.CallingInformationObject;
import com.polycom.vega.fundamental.ContactObject;
import com.polycom.vega.fundamental.ExceptionHandler;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.interfaces.PlaceACallListener;
import com.polycom.vega.resthelper.RestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xwcheng on 9/9/2015.
 */
public class KeypadFragment extends VegaFragment implements IView, AdapterView.OnItemClickListener, PlaceACallListener {
    TextView numberTextView;
    private int conferenceIndex;
    private ArrayList<String> keyList;
    private KeypadAdapter keypadAdapter;
    private GridView keypadGridView;
    private String destinationIp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        application = (VegaApplication) getActivity().getApplication();
        fragment = inflater.inflate(R.layout.fragment_keypad, container, false);
        context = fragment.getContext();
        fragmentManager = getActivity().getSupportFragmentManager();

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
                try {
                    placeACall();
                } catch (JSONException e) {
                    ExceptionHandler.getInstance().handle(context, e);
                } catch (Exception e) {
                    ExceptionHandler.getInstance().handle(context, e);
                }
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

    private void placeACall() throws Exception {
        destinationIp = (!TextUtils.isEmpty(numberTextView.getText().toString()) ? numberTextView.getText().toString() : "172.21.97.208");
        JSONObject json = new JSONObject("{\"address\":\"" + destinationIp + "\",\"dialType\":\"AUTO\",\"rate\":\"0\"}");
        RestHelper.getInstance().PlaceACall(context, json);
        RestHelper.getInstance().setPlaceACallListener(this);
    }

    @Override
    public void onCallPlaced(JSONObject response) {
    }

    @Override
    public void onPlaceACallError(VolleyError error) {
        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

        // TODO: Need to improve.
        conferenceIndex = Integer.parseInt(error.getMessage().substring(error.getMessage().indexOf("connections")).charAt(13) + "");

        CallingInformationObject callingInfo = new CallingInformationObject();

        try {
            // TODO: Need to improve.
            conferenceIndex = Integer.parseInt(error.getMessage().substring(error.getMessage().indexOf("connections")).charAt(13) + "");

            ContactObject contact = new ContactObject(destinationIp, destinationIp);

            callingInfo.setContact(contact);
            callingInfo.setConferenceIndex(conferenceIndex);
            callingInfo.setStartTime(new Date());
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();

            return;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("callingInfo", callingInfo);

        InCallFragment inCallFragment = new InCallFragment();
        inCallFragment.setArguments(bundle);

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, inCallFragment).commit();
    }
}
