package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class PlaceACallFragment extends VegaFragment implements IView, IDataBind {
    private View bottomBar;
    private RadioGroup bottomBarRadioGroup;
    private View.OnClickListener backHeaderButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentManager.popBackStack();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        fragment = inflater.inflate(R.layout.fragment_placeacall, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        initComponent();
        initComponentState();
        initAnimation();
        registerNotification();
        dataBind();

        getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new KeypadFragment()).commit();

        return fragment;
    }

    @Override
    public void initComponent() {
        bottomBar = fragment.findViewById(R.id.bottombar_placeacall_fragment);

        bottomBarRadioGroup = (RadioGroup) bottomBar.findViewById(R.id
                .bootombar_placeacall_fragment_radiogroup);
        bottomBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bottombar_placeacall_fragment_favoriteRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new FavoriteFragment()).commit();
                } else if (checkedId == R.id.bottombar_placeacall_fragment_recentCallsRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new RecentCallFragment()).commit();
                } else if (checkedId == R.id.bottombar_placeacall_fragment_contactsRadioButton) {
                    try {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new ContactsFragment()).commit();
                    } catch (Exception ex) {
                        Log.d(fragment.getId() + "", ex.getMessage());
                    }
                } else if (checkedId == R.id.bottombar_placeacall_fragment_keypadRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new KeypadFragment()).commit();
                }
            }
        });
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(500);
        LayoutAnimationController animationController = new LayoutAnimationController(scaleAnimation, 0.1f);

        ((RelativeLayout) fragment).setLayoutAnimation(animationController);
    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void dataBind() {
    }

    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(fragment);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.add("Clear");
//
//        super.onCreateContextMenu(menu, fragment, menuInfo);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        return super.onContextItemSelected(item);
//    }
}
