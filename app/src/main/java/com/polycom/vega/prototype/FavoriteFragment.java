package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
 * Created by xwcheng on 9/11/2015.
 */
public class FavoriteFragment extends VegaFragment implements IDataBind, AdapterView.OnItemClickListener, IView, PlaceACallListener {
    private int conferenceIndex;
    private GridView favoriteGridView;
    private ArrayList<ContactObject> favorites;
    private FavoriteAdapter favoriteAdapter;
    private String destinationIp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        fragment = inflater.inflate(R.layout.fragment_favorite, container, false);
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
        favoriteGridView = (GridView) fragment.findViewById(R.id.fragment_favorite_favoriteGridView);
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
        favorites = new ArrayList<ContactObject>();
        favorites.add(new ContactObject("老孙", "172.21.97.151"));
        favorites.add(new ContactObject("王诚", "172.21.97.208"));

        favoriteAdapter = new FavoriteAdapter(context, favorites);

        favoriteGridView.setAdapter(favoriteAdapter);
        favoriteGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            placeACall(favorites.get(position).getDestinationIp());
        } catch (Exception e) {
            ExceptionHandler.getInstance().handle(context, e);
        }
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
