package com.polycom.vega.prototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.VegaApplication;
import com.polycom.vega.restobject.SystemObject;

public class PairActivity extends AppCompatActivity implements IActivity, Thread.UncaughtExceptionHandler {
    private BootstrapEditText urlTextEdit = null;
    private BootstrapButton pairButton = null;
    private BootstrapButton demoButton = null;
    private VegaApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        getSupportActionBar().hide();

        this.initComponent();
        this.initComponentState();
    }

    private View.OnClickListener pairButtonClickListerner = new View.OnClickListener() {
        public void onClick(View view) {
            if (TextUtils.isEmpty(urlTextEdit.getText().toString())) {
                return;
            }

            pair();
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

    private void pair() {
        String url = urlTextEdit.getText().toString();

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!url.startsWith("https://")) {
            url = "https://" + url;
        }

        try {
            HttpsTrustHelper.allowAllSSL();
        } catch (Exception ex) {
            Toast.makeText(PairActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final String finalUrl = url;
        StringRequest request = new StringRequest(url + "/rest/system", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                parseData(response);
                application.setServerUrl(finalUrl);

                Intent intent = new Intent(PairActivity.this, MainActivity.class);
                intent.putExtra("response", response);

                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), (error.networkResponse != null ? error.networkResponse.statusCode + "" : error.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
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

    @Override
    public void initComponent() {
        this.application = (VegaApplication) getApplicationContext();
        this.urlTextEdit = (BootstrapEditText) findViewById(R.id.urlEditText);
        this.pairButton = (BootstrapButton) findViewById(R.id.activity_pair_pairButton);
        this.demoButton = (BootstrapButton) findViewById(R.id.activity_pair_demoButton);
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
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
    }
}
