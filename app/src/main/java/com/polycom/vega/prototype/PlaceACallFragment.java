package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class PlaceACallFragment extends Fragment implements Thread.UncaughtExceptionHandler, IActivity, IDataBind {
    private RelativeLayout fragment;
    private LinearLayout header;
    private boolean inACall;
    private int conferenceIndex;
    private VegaApplication application;

    public boolean isInACall() {
        return inACall;
    }

    public void setInACall(boolean inACall) {
        inACall = inACall;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = (RelativeLayout) inflater.inflate(R.layout.fragment_placeacall, container, false);

        initComponent();
        initComponentState();
        initAnimation();
        registerNotification();
        dataBind();

        return fragment;
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
        final String destinationIp = "172.21.97.153";
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
    }

    private View.OnClickListener recentCallsImageButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
            builder.setIcon(R.drawable.icon_recentcalls);
            builder.setTitle(getString(R.string.option_item_recentCalls_title));

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(fragment.getContext(), android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("172.21.97.153");
            arrayAdapter.add("172.21.97.190");
            arrayAdapter.add("172.21.97.157");

            builder.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        }
    };

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
