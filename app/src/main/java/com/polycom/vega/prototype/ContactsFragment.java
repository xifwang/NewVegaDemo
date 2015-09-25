package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.melnykov.fab.FloatingActionButton;
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
public class ContactsFragment extends VegaFragment implements IView, IDataBind, AdapterView.OnItemClickListener, PlaceACallListener {
    private ListView contactListView;
    private FloatingActionButton addButton;
    private ArrayList<ContactObject> contacts;
    private ArrayAdapter<ContactObject> contactAdapter;
    private int conferenceIndex;
    private ContactObject currentContact;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            fragment = inflater.inflate(R.layout.fragment_contacts, container, false);
            context = fragment.getContext();
            application = (VegaApplication) getActivity().getApplication();
            fragmentManager = getActivity().getSupportFragmentManager();

            Thread.currentThread().setUncaughtExceptionHandler(this);

            fragment.isInEditMode();

            initComponent();
            initComponentState();
            initAnimation();
            dataBind();
        } catch (Exception ex) {
            Log.d(fragment.getId() + "", ex.getMessage());
        }

        return fragment;
    }

    @Override
    public void initComponent() {
        contactListView = (ListView) fragment.findViewById(R.id.fragment_contacts_listView);

        addButton = (FloatingActionButton) fragment.findViewById(R.id.fragment_contacts_floatingActionButton);
        addButton.attachToListView(contactListView);
        addButton.setOnClickListener(addButton_OnClickListener);
    }

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
            placeACall(contacts.get(position));
        } catch (Exception e) {
            ExceptionHandler.getInstance().handle(context, e);
        }
    }

    @Override
    public void dataBind() {
        contacts = new ArrayList<ContactObject>();

        contacts.add(new ContactObject("王诚", "172.21.97.208"));
        contacts.add(new ContactObject("老孙", "172.21.97.151"));

        contactAdapter = new ContactsAdapter(context, contacts);

        contactListView.setAdapter(contactAdapter);
        contactListView.setOnItemClickListener(this);
    }

    private void placeACall(final ContactObject contact) throws Exception {
        currentContact = contact;
        JSONObject json = new JSONObject("{\"address\":\"" + contact.getDestinationIp() + "\",\"dialType\":\"AUTO\",\"rate\":\"0\"}");
        RestHelper.getInstance().PlaceACall(context, json);
        RestHelper.getInstance().setPlaceACallListener(this);
    }

    @Override
    public void onCallPlaced(JSONObject response) {
    }

    @Override
    public void onPlaceACallError(VolleyError error) {
        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

        CallingInformationObject callingInfo = new CallingInformationObject();
        try {
            // TODO: Need to improve.
            conferenceIndex = Integer.parseInt(error.getMessage().substring(error.getMessage().indexOf("connections")).charAt(13) + "");

            callingInfo.setConferenceIndex(conferenceIndex);
            callingInfo.setContact(currentContact);
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
