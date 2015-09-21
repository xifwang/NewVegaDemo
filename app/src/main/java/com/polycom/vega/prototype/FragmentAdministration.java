package com.polycom.vega.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;

import java.util.ArrayList;

public class FragmentAdministration extends VegaFragment implements IActivity, IDataBind, Thread.UncaughtExceptionHandler {
    private Spinner languageListSpinner;
    private ArrayAdapter languageListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.fragment_administration, container, false);
        context = fragment.getContext();
        application = (VegaApplication) getActivity().getApplication();
        fragmentManager = getActivity().getSupportFragmentManager();

        Thread.currentThread().setUncaughtExceptionHandler(this);

        initComponent();
        initComponentState();
        initAnimation();
        dataBind();

        return fragment;
    }

    @Override
    public void initComponent() {
        languageListSpinner = (Spinner) fragment.findViewById(R.id.fragment_administration_languageListSpinner);
        languageListSpinner.getSelectedItem();

        languageListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        CharSequence[] languages = context.getResources().getTextArray(R.array.languages);
        ArrayList<String> languageDisplayNames = new ArrayList<String>();

        for (CharSequence language : languages) {
            int languageId = getResources().getIdentifier(language.toString().replace("-", ""), "string", getActivity().getPackageName());
            String languageDisplayName = getString(languageId);

            languageDisplayNames.add(languageDisplayName);
        }

        languageListAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, languageDisplayNames);
        languageListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageListSpinner.setAdapter(languageListAdapter);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
