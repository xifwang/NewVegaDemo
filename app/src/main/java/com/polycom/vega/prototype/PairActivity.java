package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.polycom.vega.fundamental.VegaActivity;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.interfaces.IView;
import com.polycom.vega.interfaces.PairLitenser;
import com.polycom.vega.localstorage.LocalStorageHelper;
import com.polycom.vega.resthelper.RestHelper;
import com.polycom.vega.restobject.SystemObject;

import java.util.Locale;

public class PairActivity extends VegaActivity implements IView, PairLitenser {
    private BootstrapEditText urlTextEdit = null;
    private BootstrapButton pairButton = null;
    private BootstrapButton demoButton = null;
    private VegaApplication application;
    private View.OnClickListener pairButtonClickListerner = new View.OnClickListener() {
        public void onClick(View view) {
            if (TextUtils.isEmpty(urlTextEdit.getText().toString())) {
                return;
            }

            RestHelper.getInstance().setPairLitenser(PairActivity.this);
            RestHelper.getInstance().Pair(PairActivity.this, urlTextEdit.getText().toString());
        }
    };
    private View.OnClickListener demoButtonClickListerner = new View.OnClickListener() {
        public void onClick(View view) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(PairActivity.this);
            dialog.setTitle("Demo");
            dialog.setMessage("This is only for demonstration, functions require connection may not work.");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    application.setServerUrl("https://172.21.97.190");

                    startActivity(new Intent(PairActivity.this, MainActivity.class));
                }
            });
            dialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.currentThread().setUncaughtExceptionHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        getSupportActionBar().hide();

        initUILanguage();
        initComponent();
        initComponentState();
    }

    private void parseData(String response) {
        SystemObject systemInfo = JSON.parseObject(response, SystemObject.class);

//        resultTextView.setText(response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUILanguage() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        String language = LocalStorageHelper.getInstance().getLanguage(getApplicationContext());

        if (!TextUtils.equals(configuration.locale.toString(), language)) {
            configuration.locale = new Locale(language);
            resources.updateConfiguration(configuration, displayMetrics);
            this.finish();
            this.startActivity(this.getIntent());
        }
    }

    @Override
    public void initComponent() {
        application = (VegaApplication) getApplicationContext();
        urlTextEdit = (BootstrapEditText) findViewById(R.id.urlEditText);
        pairButton = (BootstrapButton) findViewById(R.id.activity_pair_pairButton);
        demoButton = (BootstrapButton) findViewById(R.id.activity_pair_demoButton);
    }

    @Override
    public void initComponentState() {
        pairButton.setOnClickListener(pairButtonClickListerner);

        demoButton.setOnClickListener(demoButtonClickListerner);
    }

    @Override
    public void initAnimation() {
    }

    @Override
    public void registerNotification() {
    }

    @Override
    public void onPaired(String response) {
        Intent intent = new Intent(PairActivity.this, MainActivity.class);
        intent.putExtra("response", response);

        startActivity(intent);
    }
}
