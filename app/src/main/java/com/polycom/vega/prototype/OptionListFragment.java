package com.polycom.vega.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.OptionObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class OptionListFragment extends Fragment implements IActivity, IDataBind, AdapterView.OnItemClickListener, Thread.UncaughtExceptionHandler {

    private RelativeLayout fragment;
    private Context context;
    private ArrayList<OptionObject> optionList;
    private OptionAdapter optionAdapter;
    private ListView optionListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        fragment = (RelativeLayout) inflater.inflate(R.layout.fragment_optionlist, container, false);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initComponent();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        if (position == 1) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new PlaceACallFragment()).commit();
        }
    }

    @Override
    public void initComponent() {
        try {
            optionList = new ArrayList<OptionObject>();
            optionList.add(new OptionObject(R.drawable.icon_administration, getString(R.string.option_item_administration_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_placeacall, getString(R.string.option_item_placeACall_title), R.layout.fragment_placeacall));
            optionList.add(new OptionObject(R.drawable.icon_showcontent, getString(R.string.option_item_showContent_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_keypad, getString(R.string.option_item_keypad_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_contacts, getString(R.string.option_item_contacts_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_recentcalls, getString(R.string.option_item_recentCalls_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_systeminformation, getString(R.string.option_item_systemInformation_title), 0));
            optionList.add(new OptionObject(R.drawable.icon_usersettings, getString(R.string.option_item_userSettings_title), 0));

            optionAdapter = new OptionAdapter(getView().getContext(), optionList);

            optionListView = (ListView) getView().findViewById(R.id.fragment_optionList_optionListView);
            optionListView.setAdapter(optionAdapter);
            optionListView.setOnItemClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
    }
}
