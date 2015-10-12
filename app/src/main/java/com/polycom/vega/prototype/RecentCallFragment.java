package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.polycom.vega.fundamental.CallingInformationObject;
import com.polycom.vega.fundamental.ContactObject;
import com.polycom.vega.fundamental.ExceptionHandler;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.interfaces.PlaceACallListener;
import com.polycom.vega.resthelper.RestHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michael on 9/10/2015.
 */
public class RecentCallFragment extends VegaFragment implements IView, IDataBind, AdapterView.OnItemClickListener, PlaceACallListener {
    private int conferenceIndex;
    private ListView listView;
    private ArrayList<String> calls;
    private RecentCallAdapter recentCallAdapter;
    private String destinationIp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        fragment = inflater.inflate(R.layout.fragment_recentcall, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        listView = (ListView) fragment.findViewById(R.id.fragment_recentcall_listView);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            placeACall(calls.get(position));
        } catch (Exception e) {
            ExceptionHandler.getInstance().handle(context, e);
        }
    }

    @Override
    public void dataBind() {
        calls = new ArrayList<String>();
        calls.add("172.21.97.208");
        calls.add("172.21.97.151");
        calls.add("172.21.97.190");

        recentCallAdapter = new RecentCallAdapter(context, calls);

        listView.setAdapter(recentCallAdapter);
        listView.setOnItemClickListener(this);
    }

    private void placeACall(final String destinationIp) throws Exception {
        this.destinationIp = destinationIp;
        JSONObject json = new JSONObject("{\"address\":\"" + destinationIp + "\",\"dialType\":\"AUTO\",\"rate\":\"0\"}");
        RestHelper.getInstance().PlaceACall(context, json);
        RestHelper.getInstance().setPlaceACallListener(this);
    }

    @Override
    public void onCallPlaced(JSONObject response) {

    }

    @Override
    public void onPlaceACallError(VolleyError error) {
        toast(error.getMessage());

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
            toast(error.getMessage());

            return;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("callingInfo", callingInfo);

        InCallFragment inCallFragment = new InCallFragment();
        inCallFragment.setArguments(bundle);

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, inCallFragment).commit();
    }
}
