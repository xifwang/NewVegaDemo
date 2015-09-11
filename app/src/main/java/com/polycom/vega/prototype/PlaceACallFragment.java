package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class PlaceACallFragment extends Fragment implements Thread.UncaughtExceptionHandler, IActivity, IDataBind {
    private RelativeLayout fragment;
    private LinearLayout header;
    private View headerBar;
    private View bottomBar;
    private VegaApplication application;
    private RadioGroup bottomBarRadioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            fragment = (RelativeLayout) inflater.inflate(R.layout.fragment_placeacall, container, false);
        } catch (Exception ex) {
            Toast.makeText(getView().getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        initComponent();
        initComponentState();
        initAnimation();
        registerNotification();
        dataBind();

        headerBar.setVisibility(View.GONE);
        getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new KeypadFragment()).commit();

        return fragment;
    }

    @Override
    public void initComponent() {
        header = (LinearLayout) fragment.findViewById(R.id.header_placeacall_fragment);

        ((TextView) header.findViewById(R.id.header_option_item_layout_options_titleTextView))
                .setText(getString(R.string.option_item_placeACall_title));

        ((ImageButton) header.findViewById(R.id
                .header_option_item_layout_back_icon_imageButton))
                .setOnClickListener(backHeaderButton_OnClickListener);

        ((ImageButton) header.findViewById(R.id.header_option_item_layout_options_icon_imageButton))
                .setOnClickListener(optionsHeaderButton_OnClickListener);

        application = (VegaApplication) getActivity().getApplicationContext();

        ImageButton optionMenuButton = (ImageButton) header.findViewById(R.id.header_option_item_layout_options_icon_imageButton);
        optionMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuObject close = new MenuObject();
                close.setResource(R.drawable.menu_icon_close);

                MenuObject send = new MenuObject("Test");
                send.setResource(R.drawable.menu_icon_test);

                List<MenuObject> menuObjects = new ArrayList<>();
                menuObjects.add(close);
                menuObjects.add(send);

                MenuParams menuParams = new MenuParams();
                menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.abc_action_bar_stacked_max_height));
                menuParams.setMenuObjects(menuObjects);
                menuParams.setClosableOutside(true);
                ContextMenuDialogFragment menuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
                menuDialogFragment.show(getFragmentManager(), "ContextMenuDialogFragment");
            }
        });

        headerBar = fragment.findViewById(R.id.header_placeacall_fragment);

        bottomBar = fragment.findViewById(R.id.bottombar_placeacall_fragment);

        bottomBarRadioGroup = (RadioGroup) bottomBar.findViewById(R.id
                .bootombar_placeacall_fragment_radiogroup);
        bottomBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                headerBar.setVisibility(View.GONE);

                if (checkedId == R.id.bottombar_placeacall_fragment_favoriteRadioButton) {

                } else if (checkedId == R.id.bottombar_placeacall_fragment_recentCallsRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new RecentCallFragment()).commit();
                } else if (checkedId == R.id.bottombar_placeacall_fragment_contactsRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new ContactsFragment()).commit();
                } else if (checkedId == R.id.bottombar_placeacall_fragment_keypadRadioButton) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_placeacall_maincontainer, new KeypadFragment()).commit();
                }
            }
        });
    }

//    private View.OnClickListener recentCallsImageButton_OnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
//            builder.setIcon(R.drawable.icon_recentcalls);
//            builder.setTitle(getString(R.string.option_item_recentCalls_title));
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(fragment.getContext(), android.R.layout.simple_selectable_list_item);
//            arrayAdapter.add("172.21.97.215");
//            arrayAdapter.add("172.21.97.190");
//            arrayAdapter.add("172.21.97.157");
//
//            builder.setAdapter(arrayAdapter,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            builder.show();
//        }
//    };

    private View.OnClickListener backHeaderButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    private View.OnClickListener optionsHeaderButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            fragment.showContextMenu();
        }
    };

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        scaleAnimation.setDuration(500);
        LayoutAnimationController animationController = new LayoutAnimationController(scaleAnimation, 0.1f);

        fragment.setLayoutAnimation(animationController);
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

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
}
