package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.fundamental.VegaFragment;
import com.polycom.vega.interfaces.IDataBind;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.localstorage.LocalStorageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentAdministration extends VegaFragment implements IView, IDataBind {
    private Spinner languageListSpinner;
    private ArrayAdapter languageListAdapter;
    private BootstrapButton changePasswordBootstrapButton;
    private BootstrapButton logsBootstrapButton;
    private ProgressBar checkUpdateProgressBar;
    private BootstrapButton checkUpdateBootstrapButton;
    private CharSequence[] languages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.fragment_administration, container, false);
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
        languageListSpinner = (Spinner) fragment.findViewById(R.id.fragment_administration_languageListSpinner);

        changePasswordBootstrapButton = (BootstrapButton) fragment.findViewById(R.id.fragment_administration_changePasswordButton);
        changePasswordBootstrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(R.string.button_changepassword_text);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setView(R.layout.fragment_changepassword);
                alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Hello, world!", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        });

        logsBootstrapButton = (BootstrapButton) fragment.findViewById(R.id.fragment_administration_logsButton);
        logsBootstrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id
                        .fragment_main, new LogFragment()).commit();
            }
        });

        checkUpdateProgressBar = (ProgressBar) fragment.findViewById(R.id.fragment_administration_checkUpdateProgressBar);

        checkUpdateBootstrapButton = (BootstrapButton) fragment.findViewById(R.id.fragment_administration_checkUpdateButton);
        checkUpdateBootstrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdateProgressBar.setVisibility(View.VISIBLE);
                checkUpdateBootstrapButton.setEnabled(false);

                new Timer(true).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                checkUpdateProgressBar.setVisibility(View.GONE);
                                checkUpdateBootstrapButton.setEnabled(true);
                                Toast.makeText(context, getString(R.string.checkupdate_alreadylatestversion_message), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 3000);
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
        languages = context.getResources().getTextArray(R.array.languages);
        ArrayList<String> languageDisplayNames = new ArrayList<String>();

        for (CharSequence language : languages) {
            int languageId = getResources().getIdentifier(language.toString().replace("-", ""), "string", getActivity().getPackageName());
            String languageDisplayName = getString(languageId);

            languageDisplayNames.add(languageDisplayName);
        }

        languageListAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, languageDisplayNames);
        languageListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageListSpinner.setAdapter(languageListAdapter);

        String language = LocalStorageHelper.getInstance().getLanguage(context);

        if (TextUtils.isEmpty(language)) {
            language = getResources().getConfiguration().locale.toString();

            try {
                LocalStorageHelper.getInstance().saveLanguage(context, language);
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        languageListSpinner.setSelection(Arrays.asList(languages).indexOf(language));
        languageListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();

                if (!TextUtils.equals(LocalStorageHelper.getInstance().getLanguage(context), languages[position])) {
                    configuration.locale = new Locale(languages[position].toString());
                    resources.updateConfiguration(configuration, displayMetrics);
                    LocalStorageHelper.getInstance().saveLanguage(context, languages[position].toString());
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
