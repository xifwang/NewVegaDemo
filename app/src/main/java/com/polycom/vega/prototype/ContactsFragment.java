package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;
import com.polycom.vega.fundamental.ContactObject;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xwcheng on 9/11/2015.
 */
public class ContactsFragment extends Fragment implements IActivity, IDataBind, AdapterView.OnItemClickListener, Thread.UncaughtExceptionHandler {
    private View fragment;
    private Context context;
    private ListView listView;
    private FloatingActionButton addButton;
    private ArrayList<ContactObject> contacts;
    private ArrayAdapter<ContactObject> contactAdapter;
    private int conferenceIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_contacts, container, false);

        fragment.isInEditMode();

        context = fragment.getContext();

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        listView = (ListView) fragment.findViewById(R.id.fragment_contacts_listView);

        addButton = (FloatingActionButton) fragment.findViewById(R.id.fragment_contacts_floatingActionButton);
        addButton.attachToListView(listView);
        addButton.setOnClickListener(addButton_OnClickListener);
    }

    private View.OnClickListener addButton_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            final View contentView = View.inflate(context, R.layout.fragment_addcontact_dialog_content, null);
            contentView.setPadding(16, 16, 16, 16);
            dialogBuilder.setView(contentView);
            dialogBuilder.setTitle(R.string.add_contact_dialog_title);
            dialogBuilder.setIcon(R.drawable.icon_default_user_avatar);
            dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String displayName = ((EditText) contentView.findViewById(R.id.fragment_addcontact_dialog_content_displayNameEditText)).getText().toString().trim();
                    String destinationIp = ((EditText) contentView.findViewById(R.id.fragment_addcontact_dialog_content_destinationIpEditText)).getText().toString().trim();

                    contacts.add(new ContactObject(displayName, destinationIp));
                    contactAdapter.notifyDataSetChanged();
                }
            });
            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.show();
        }
    };

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
        placeACall(contacts.get(position).getDestinationIp());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }

    @Override
    public void dataBind() {
        contacts = new ArrayList<ContactObject>();

        contacts.add(new ContactObject("王诚", "172.21.97.215"));
        contacts.add(new ContactObject("老孙", "172.21.97.151"));

        contactAdapter = new ContactsAdapter(context, contacts);

        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(this);
    }

    private void placeACall(final String destinationIp) {
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/conferences?_dc=1439978043968";
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
        String url = ((VegaApplication) getActivity().getApplicationContext()).getServerUrl() + "/rest/conferences/0/connections/" + conferenceIndex;

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
