package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.OptionObject;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class OptionListFragment extends VegaFragment implements IActivity, IDataBind, AdapterView.OnItemClickListener {
    private ArrayList<OptionObject> optionList;
    private OptionAdapter optionAdapter;
    private GridView optionListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        context = getActivity().getApplicationContext();
        fragment = inflater.inflate(R.layout.fragment_optionlist, container, false);
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        initComponent();
        initComponentState();
        initAnimation();

        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        if (position == 0) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new FragmentAdministration()).commit();
        } else if (position == 1) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new PlaceACallFragment()).commit();
        } else if (position == 3) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new SystemInfoFragment()).commit();
        } else if (position == 4) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_main, new LocalGsControlFragment()).commit();
        }

    }

    @Override
    public void initComponent() {
        optionList = new ArrayList<OptionObject>();
        optionList.add(new OptionObject(R.drawable.icon_administration, getString(R.string.option_item_administration_title), 0));
        optionList.add(new OptionObject(R.drawable.icon_placeacall, getString(R.string.option_item_placeACall_title), R.layout.fragment_placeacall));
        optionList.add(new OptionObject(R.drawable.icon_showcontent, getString(R.string.option_item_showContent_title), 0));
//        optionList.add(new OptionObject(R.drawable.icon_keypad, getString(R.string.option_item_keypad_title), 0));
//        optionList.add(new OptionObject(R.drawable.icon_contacts, getString(R.string.option_item_contacts_title), 0));
//        optionList.add(new OptionObject(R.drawable.icon_recentcalls, getString(R.string.option_item_recentCalls_title), 0));
        optionList.add(new OptionObject(R.drawable.icon_systeminformation, getString(R.string.option_item_systemInformation_title), 0));
        optionList.add(new OptionObject(R.drawable.menu_icon_test, getString(R.string.option_item_gs_control), 0));
        optionList.add(new OptionObject(R.drawable.icon_usersettings, getString(R.string.option_item_userSettings_title), 0));
//        optionList.add(new OptionObject(R.drawable.icon_favorite, getString(R.string.option_item_favorite_title), 0));

        optionAdapter = new OptionAdapter(context, optionList);

        optionListView = (GridView) fragment.findViewById(R.id.fragment_optionList_optionListView);
        optionListView.setAdapter(optionAdapter);
        optionListView.setOnItemClickListener(this);
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
